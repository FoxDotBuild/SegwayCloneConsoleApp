package processing.mode.android;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import processing.app.RunnerListener;
import processing.app.SketchException;
import processing.mode.java.runner.Runner;

public class AndroidRunner implements DeviceListener {
    private static final Pattern EXCEPTION_PARSER = Pattern.compile("^\\s*([a-z]+(?:\\.[a-z]+)+)(?:: .+)?$", 2);
    private static final Pattern LOCATION = Pattern.compile("\\(([^:]+):(\\d+)\\)");
    AndroidBuild build;
    private volatile Device lastRunDevice = null;
    RunnerListener listener;

    public AndroidRunner(AndroidBuild build2, RunnerListener listener2) {
        this.build = build2;
        this.listener = listener2;
    }

    public void launch(Future<Device> deviceFuture) {
        this.listener.statusNotice("Waiting for device to become available...");
        Device device = waitForDevice(deviceFuture, this.listener);
        if (device == null || !device.isAlive()) {
            this.listener.statusError("Lost connection with device while launching. Try again.");
            Devices.killAdbServer();
            return;
        }
        device.addListener(this);
        this.listener.statusNotice("Installing sketch on " + device.getId());
        if (!device.installApp(this.build.getPathForAPK(), this.listener)) {
            this.listener.statusError("Lost connection with device while installing. Try again.");
            Devices.killAdbServer();
            return;
        }
        this.listener.statusNotice("Starting sketch on " + device.getId());
        if (startSketch(this.build, device)) {
            this.listener.statusNotice("Sketch launched on the " + (device.isEmulator() ? "emulator" : "device") + ".");
        } else {
            this.listener.statusError("Could not start the sketch.");
        }
        this.listener.stopIndeterminate();
        this.lastRunDevice = device;
    }

    private boolean startSketch(AndroidBuild build2, Device device) {
        try {
            if (device.launchApp(build2.getPackageName(), build2.getSketchClassName())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return false;
    }

    private Device waitForDevice(Future<Device> deviceFuture, RunnerListener listener2) {
        int i = 0;
        while (i < 120) {
            if (listener2.isHalted()) {
                deviceFuture.cancel(true);
                return null;
            }
            try {
                return (Device) deviceFuture.get(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                listener2.statusError("Interrupted.");
                return null;
            } catch (ExecutionException e2) {
                listener2.statusError(e2);
                return null;
            } catch (TimeoutException e3) {
                i++;
            }
        }
        listener2.statusError("No, on second thought, I'm giving up on waiting for that device to show up.");
        return null;
    }

    public void stackTrace(List<String> trace) {
        Iterator<String> frames = trace.iterator();
        String exceptionLine = (String) frames.next();
        Matcher m = EXCEPTION_PARSER.matcher(exceptionLine);
        if (!m.matches()) {
            System.err.println("Can't parse this exception line:");
            System.err.println(exceptionLine);
            this.listener.statusError("Unknown exception");
        } else if (!Runner.handleCommonErrors(m.group(1), exceptionLine, this.listener)) {
            while (frames.hasNext()) {
                String line = (String) frames.next();
                if (line.contains("processing.android")) {
                    Matcher lm = LOCATION.matcher(line);
                    if (lm.find()) {
                        SketchException rex = this.build.placeException(exceptionLine, lm.group(1), Integer.parseInt(lm.group(2)) - 1);
                        RunnerListener runnerListener = this.listener;
                        if (rex == null) {
                            rex = new SketchException(exceptionLine, false);
                        }
                        runnerListener.statusError(rex);
                        return;
                    }
                }
            }
        }
    }

    public void close() {
        if (this.lastRunDevice != null) {
            this.lastRunDevice.bringLauncherToFront();
        }
    }

    public void sketchStopped() {
        this.listener.stopIndeterminate();
        this.listener.statusHalt();
    }
}

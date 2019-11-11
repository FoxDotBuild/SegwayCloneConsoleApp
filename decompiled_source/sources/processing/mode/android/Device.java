package processing.mode.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import processing.app.Base;
import processing.app.RunnerListener;
import processing.app.exec.LineProcessor;
import processing.app.exec.ProcessRegistry;
import processing.app.exec.ProcessResult;
import processing.app.exec.StreamPump;
import processing.core.PApplet;
import processing.mode.android.LogEntry.Severity;

class Device {
    /* access modifiers changed from: private */
    public static final Pattern SIG = Pattern.compile("PID:\\s+(\\d+)\\s+SIG:\\s+(\\d+)");
    /* access modifiers changed from: private */
    public final Set<Integer> activeProcesses = new HashSet();
    private final Devices env;

    /* renamed from: id */
    private final String f75id;
    private final Set<DeviceListener> listeners = Collections.synchronizedSet(new HashSet());
    /* access modifiers changed from: private */
    public Process logcat;
    /* access modifiers changed from: private */
    public final List<String> stackTrace = new ArrayList();

    private class LogLineProcessor implements LineProcessor {
        private LogLineProcessor() {
        }

        /* synthetic */ LogLineProcessor(Device device, LogLineProcessor logLineProcessor) {
            this();
        }

        public void processLine(String line) {
            LogEntry entry = new LogEntry(line);
            if (entry.message.startsWith("PROCESSING")) {
                if (entry.message.contains("onStart")) {
                    Device.this.startProc(entry.source, entry.pid);
                } else if (entry.message.contains("onStop")) {
                    Device.this.endProc(entry.pid);
                }
            } else if (entry.source.equals("Process")) {
                handleCrash(entry);
            } else if (Device.this.activeProcesses.contains(Integer.valueOf(entry.pid))) {
                handleConsole(entry);
            }
        }

        private void handleCrash(LogEntry entry) {
            Matcher m = Device.SIG.matcher(entry.message);
            if (m.find()) {
                int pid = Integer.parseInt(m.group(1));
                int signal = Integer.parseInt(m.group(2));
                if (Device.this.activeProcesses.contains(Integer.valueOf(pid)) && signal == 3) {
                    Device.this.endProc(pid);
                    Device.this.reportStackTrace(entry);
                }
            }
        }

        private void handleConsole(LogEntry entry) {
            if (entry.source.equals("AndroidRuntime") && entry.severity == Severity.Error) {
                if (!entry.message.startsWith("Uncaught handler")) {
                    Device.this.stackTrace.add(entry.message);
                    System.err.println(entry.message);
                }
            } else if (!entry.source.equals("System.out") && !entry.source.equals("System.err")) {
            } else {
                if (entry.severity.useErrorStream) {
                    System.err.println(entry.message);
                } else {
                    System.out.println(entry.message);
                }
            }
        }
    }

    public Device(Devices env2, String id) {
        this.env = env2;
        this.f75id = id;
    }

    public void bringLauncherToFront() {
        try {
            adb("shell", "am", "start", "-a", "android.intent.action.MAIN", "-c", "android.intent.category.HOME");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public boolean installApp(String apkPath, RunnerListener status) {
        if (!isAlive()) {
            return false;
        }
        bringLauncherToFront();
        try {
            ProcessResult installResult = adb("install", "-r", apkPath);
            if (!installResult.succeeded()) {
                status.statusError("Could not install the sketch.");
                System.err.println(installResult);
                return false;
            }
            String errorMsg = null;
            Iterator it = installResult.iterator();
            while (it.hasNext()) {
                String line = (String) it.next();
                if (line.startsWith("Failure")) {
                    errorMsg = line.substring(8);
                    System.err.println(line);
                }
            }
            if (errorMsg == null) {
                status.statusNotice("Done installing.");
                return true;
            }
            status.statusError("Error while installing " + errorMsg);
            return false;
        } catch (IOException e) {
            status.statusError(e);
            return false;
        } catch (InterruptedException e2) {
            return false;
        }
    }

    public boolean launchApp(String packageName, String className) throws IOException, InterruptedException {
        if (!isAlive()) {
            return false;
        }
        ProcessResult pr = adb("shell", "am", "start", "-e", "debug", "true", "-a", "android.intent.action.MAIN", "-c", "android.intent.category.LAUNCHER", "-n", new StringBuilder(String.valueOf(packageName)).append("/.").append(className).toString());
        if (Base.DEBUG) {
            System.out.println(pr.toString());
        }
        if (!pr.getStdout().contains("android.util.AndroidException")) {
            return pr.succeeded();
        }
        System.err.println(pr.getStdout());
        return false;
    }

    public boolean isEmulator() {
        return this.f75id.startsWith("emulator");
    }

    /* access modifiers changed from: private */
    public void reportStackTrace(LogEntry entry) {
        if (this.stackTrace.isEmpty()) {
            System.err.println("That's weird. Proc " + entry.pid + " got signal 3, but there's no stack trace.");
        }
        List<String> stackCopy = Collections.unmodifiableList(new ArrayList(this.stackTrace));
        for (DeviceListener listener : this.listeners) {
            listener.stackTrace(stackCopy);
        }
        this.stackTrace.clear();
    }

    /* access modifiers changed from: 0000 */
    public void initialize() throws IOException, InterruptedException {
        adb("logcat", "-c");
        String[] cmd = generateAdbCommand("logcat");
        String title = PApplet.join(cmd, ' ');
        this.logcat = Runtime.getRuntime().exec(cmd);
        ProcessRegistry.watch(this.logcat);
        new StreamPump(this.logcat.getInputStream(), "log: " + title).addTarget(new LogLineProcessor(this, null)).start();
        new StreamPump(this.logcat.getErrorStream(), "err: " + title).addTarget(System.err).start();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Device.this.logcat.waitFor();
                } catch (InterruptedException e) {
                    System.err.println("AndroidDevice: logcat process monitor interrupted");
                } finally {
                    Device.this.shutdown();
                }
            }
        }, "AndroidDevice: logcat process monitor").start();
    }

    /* access modifiers changed from: 0000 */
    public synchronized void shutdown() {
        if (isAlive()) {
            if (this.logcat != null) {
                this.logcat.destroy();
                this.logcat = null;
                ProcessRegistry.unwatch(this.logcat);
            }
            this.env.deviceRemoved(this);
            if (this.activeProcesses.size() > 0) {
                for (DeviceListener listener : this.listeners) {
                    listener.sketchStopped();
                }
            }
            this.listeners.clear();
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized boolean isAlive() {
        return this.logcat != null;
    }

    public String getId() {
        return this.f75id;
    }

    public Devices getEnv() {
        return this.env;
    }

    /* access modifiers changed from: private */
    public void startProc(String name, int pid) {
        this.activeProcesses.add(Integer.valueOf(pid));
    }

    /* access modifiers changed from: private */
    public void endProc(int pid) {
        this.activeProcesses.remove(Integer.valueOf(pid));
        for (DeviceListener listener : this.listeners) {
            listener.sketchStopped();
        }
    }

    public void addListener(DeviceListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(DeviceListener listener) {
        this.listeners.remove(listener);
    }

    private ProcessResult adb(String... cmd) throws InterruptedException, IOException {
        return AndroidSDK.runADB(generateAdbCommand(cmd));
    }

    private String[] generateAdbCommand(String... cmd) {
        return PApplet.concat(new String[]{"adb", "-s", getId()}, cmd);
    }

    public String toString() {
        return "[AndroidDevice " + getId() + "]";
    }
}

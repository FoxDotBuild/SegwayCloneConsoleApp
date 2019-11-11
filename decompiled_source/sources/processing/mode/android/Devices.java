package processing.mode.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import processing.app.Base;
import processing.app.exec.ProcessResult;
import processing.mode.android.EmulatorController.State;

class Devices {
    private static final String ADB_DEVICES_ERROR = "Received unfamiliar output from “adb devices”.\nThe device list may have errors.";
    private static final Devices INSTANCE = new Devices();
    private final ExecutorService deviceLaunchThread = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public final Map<String, Device> devices = new ConcurrentHashMap();

    public static Devices getInstance() {
        return INSTANCE;
    }

    public static void killAdbServer() {
        System.out.println("Shutting down any existing adb server...");
        System.out.flush();
        try {
            AndroidSDK.runADB("kill-server");
        } catch (Exception e) {
            System.err.println("Devices.killAdbServer() failed.");
            e.printStackTrace();
        }
    }

    private Devices() {
        if (Base.DEBUG) {
            System.out.println("Starting up Devices");
        }
        Runtime.getRuntime().addShutdownHook(new Thread("processing.mode.android.Devices Shutdown") {
            public void run() {
                Iterator it = new ArrayList(Devices.this.devices.values()).iterator();
                while (it.hasNext()) {
                    ((Device) it.next()).shutdown();
                }
            }
        });
    }

    public Future<Device> getEmulator() {
        FutureTask<Device> task = new FutureTask<>(new Callable<Device>() {
            public Device call() throws Exception {
                return Devices.this.blockingGetEmulator();
            }
        });
        this.deviceLaunchThread.execute(task);
        return task;
    }

    /* access modifiers changed from: private */
    public final Device blockingGetEmulator() {
        Device emu = find(true);
        if (emu != null) {
            return emu;
        }
        EmulatorController emuController = EmulatorController.getInstance();
        if (emuController.getState() == State.NOT_RUNNING) {
            try {
                emuController.launch();
            } catch (IOException e) {
                System.err.println("Problem while launching emulator.");
                e.printStackTrace(System.err);
                return null;
            }
        } else {
            System.out.println("Emulator is " + emuController.getState() + ", which is not expected.");
        }
        while (!Thread.currentThread().isInterrupted()) {
            if (emuController.getState() == State.NOT_RUNNING) {
                System.err.println("Error while starting the emulator. (" + emuController.getState() + ")");
                return null;
            }
            Device emu2 = find(true);
            if (emu2 != null) {
                return emu2;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e2) {
                System.err.println("Devices: interrupted in loop.");
                return null;
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private processing.mode.android.Device find(boolean r7) {
        /*
            r6 = this;
            r6.refresh()
            java.util.Map<java.lang.String, processing.mode.android.Device> r3 = r6.devices
            monitor-enter(r3)
            java.util.Map<java.lang.String, processing.mode.android.Device> r2 = r6.devices     // Catch:{ all -> 0x0033 }
            java.util.Collection r2 = r2.values()     // Catch:{ all -> 0x0033 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x0033 }
        L_0x0010:
            boolean r4 = r2.hasNext()     // Catch:{ all -> 0x0033 }
            if (r4 != 0) goto L_0x0019
            monitor-exit(r3)     // Catch:{ all -> 0x0033 }
            r0 = 0
        L_0x0018:
            return r0
        L_0x0019:
            java.lang.Object r0 = r2.next()     // Catch:{ all -> 0x0033 }
            processing.mode.android.Device r0 = (processing.mode.android.Device) r0     // Catch:{ all -> 0x0033 }
            java.lang.String r4 = r0.getId()     // Catch:{ all -> 0x0033 }
            java.lang.String r5 = "emulator"
            boolean r1 = r4.contains(r5)     // Catch:{ all -> 0x0033 }
            if (r1 == 0) goto L_0x002d
            if (r7 != 0) goto L_0x0031
        L_0x002d:
            if (r1 != 0) goto L_0x0010
            if (r7 != 0) goto L_0x0010
        L_0x0031:
            monitor-exit(r3)     // Catch:{ all -> 0x0033 }
            goto L_0x0018
        L_0x0033:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0033 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.mode.android.Devices.find(boolean):processing.mode.android.Device");
    }

    public Future<Device> getHardware() {
        FutureTask<Device> task = new FutureTask<>(new Callable<Device>() {
            public Device call() throws Exception {
                return Devices.this.blockingGetHardware();
            }
        });
        this.deviceLaunchThread.execute(task);
        return task;
    }

    /* access modifiers changed from: private */
    public final Device blockingGetHardware() {
        Device hardware = find(false);
        if (hardware != null) {
            return hardware;
        }
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(2000);
                Device hardware2 = find(false);
                if (hardware2 != null) {
                    return hardware2;
                }
            } catch (InterruptedException e) {
                return null;
            }
        }
        return null;
    }

    private void refresh() {
        for (String deviceId : list()) {
            if (!this.devices.containsKey(deviceId)) {
                addDevice(new Device(this, deviceId));
            }
        }
    }

    private void addDevice(Device device) {
        try {
            device.initialize();
            if (this.devices.put(device.getId(), device) != null) {
                throw new IllegalStateException("Adding " + device + ", which already exists!");
            }
        } catch (Exception e) {
            System.err.println("While initializing " + device.getId() + ": " + e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void deviceRemoved(Device device) {
        if (this.devices.remove(device.getId()) == null) {
            throw new IllegalStateException("I didn't know about device " + device.getId() + "!");
        }
    }

    public static List<String> list() {
        try {
            ProcessResult result = AndroidSDK.runADB("devices");
            if (!result.succeeded()) {
                if (result.getStderr().contains("protocol fault (no status)")) {
                    System.err.println("bleh: " + result);
                } else {
                    System.err.println("nope: " + result);
                }
                return Collections.emptyList();
            }
            String stdout = result.getStdout();
            if (stdout.startsWith("List of devices") || stdout.trim().length() == 0) {
                List<String> devices2 = new ArrayList<>();
                Iterator it = result.iterator();
                while (it.hasNext()) {
                    String line = (String) it.next();
                    if (line.contains("\t")) {
                        String[] fields = line.split("\t");
                        if (fields[1].equals("device")) {
                            devices2.add(fields[0]);
                        }
                    }
                }
                return devices2;
            }
            System.err.println(ADB_DEVICES_ERROR);
            System.err.println("Output was “" + stdout + "”");
            return Collections.emptyList();
        } catch (InterruptedException e) {
            return Collections.emptyList();
        } catch (IOException e2) {
            System.err.println("Problem inside Devices.list()");
            e2.printStackTrace();
            return Collections.emptyList();
        }
    }
}

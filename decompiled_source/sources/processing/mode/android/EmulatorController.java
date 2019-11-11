package processing.mode.android;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import processing.app.Base;
import processing.app.Preferences;
import processing.app.exec.LineProcessor;
import processing.app.exec.ProcessRegistry;
import processing.app.exec.StreamPump;
import processing.core.PApplet;

class EmulatorController {
    private static final EmulatorController INSTANCE = new EmulatorController();
    /* access modifiers changed from: private */
    public volatile State state = State.NOT_RUNNING;

    public enum State {
        NOT_RUNNING,
        WAITING_FOR_BOOT,
        RUNNING
    }

    EmulatorController() {
    }

    public State getState() {
        return this.state;
    }

    /* access modifiers changed from: private */
    public void setState(State state2) {
        if (Base.DEBUG) {
            new Exception("setState(" + state2 + ") called").printStackTrace(System.out);
        }
        this.state = state2;
    }

    public synchronized void launch() throws IOException {
        if (this.state != State.NOT_RUNNING) {
            throw new IllegalStateException("You can't launch an emulator whose state is " + this.state);
        }
        String portString = Preferences.get("android.emulator.port");
        if (portString == null) {
            portString = "5566";
            Preferences.set("android.emulator.port", portString);
        }
        String[] cmd = {"emulator", "-avd", AVD.defaultAVD.name, "-port", portString};
        if (Base.DEBUG) {
            System.out.println(PApplet.join(cmd, " "));
        }
        final Process p = Runtime.getRuntime().exec(cmd);
        ProcessRegistry.watch(p);
        setState(State.WAITING_FOR_BOOT);
        final String title = PApplet.join(cmd, ' ');
        new StreamPump(p.getInputStream(), "out: " + title).addTarget(new LineProcessor() {
            public void processLine(String line) {
                if (!line.contains("the cache image is used by another emulator")) {
                    System.out.println(title + ": " + line);
                }
            }
        });
        new StreamPump(p.getErrorStream(), "err: " + title).addTarget(new LineProcessor() {
            public void processLine(String line) {
                if (!line.contains("This application, or a library it uses, is using NSQuickDrawView")) {
                    System.err.println(title + ": " + line);
                }
            }
        });
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:5:0x002a A[SYNTHETIC, Splitter:B:5:0x002a] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r5 = this;
                L_0x0000:
                    processing.mode.android.EmulatorController r2 = processing.mode.android.EmulatorController.this     // Catch:{ Exception -> 0x0079 }
                    processing.mode.android.EmulatorController$State r2 = r2.state     // Catch:{ Exception -> 0x0079 }
                    processing.mode.android.EmulatorController$State r3 = processing.mode.android.EmulatorController.State.WAITING_FOR_BOOT     // Catch:{ Exception -> 0x0079 }
                    if (r2 == r3) goto L_0x002a
                    java.io.PrintStream r2 = java.lang.System.err     // Catch:{ Exception -> 0x0079 }
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0079 }
                    java.lang.String r4 = "EmulatorController: Emulator never booted. "
                    r3.<init>(r4)     // Catch:{ Exception -> 0x0079 }
                    processing.mode.android.EmulatorController r4 = processing.mode.android.EmulatorController.this     // Catch:{ Exception -> 0x0079 }
                    processing.mode.android.EmulatorController$State r4 = r4.state     // Catch:{ Exception -> 0x0079 }
                    java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0079 }
                    java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0079 }
                    r2.println(r3)     // Catch:{ Exception -> 0x0079 }
                    java.util.concurrent.CountDownLatch r2 = r4
                    r2.countDown()
                L_0x0029:
                    return
                L_0x002a:
                    boolean r2 = processing.app.Base.DEBUG     // Catch:{ Exception -> 0x0079 }
                    if (r2 == 0) goto L_0x004b
                    java.io.PrintStream r2 = java.lang.System.out     // Catch:{ Exception -> 0x0079 }
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0079 }
                    java.lang.String r4 = "sleeping for 2 seconds "
                    r3.<init>(r4)     // Catch:{ Exception -> 0x0079 }
                    java.util.Date r4 = new java.util.Date     // Catch:{ Exception -> 0x0079 }
                    r4.<init>()     // Catch:{ Exception -> 0x0079 }
                    java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0079 }
                    java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ Exception -> 0x0079 }
                    java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0079 }
                    r2.println(r3)     // Catch:{ Exception -> 0x0079 }
                L_0x004b:
                    r2 = 2000(0x7d0, double:9.88E-321)
                    java.lang.Thread.sleep(r2)     // Catch:{ Exception -> 0x0079 }
                    java.util.List r2 = processing.mode.android.Devices.list()     // Catch:{ Exception -> 0x0079 }
                    java.util.Iterator r2 = r2.iterator()     // Catch:{ Exception -> 0x0079 }
                L_0x0058:
                    boolean r3 = r2.hasNext()     // Catch:{ Exception -> 0x0079 }
                    if (r3 == 0) goto L_0x0000
                    java.lang.Object r0 = r2.next()     // Catch:{ Exception -> 0x0079 }
                    java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0079 }
                    java.lang.String r3 = "emulator"
                    boolean r3 = r0.contains(r3)     // Catch:{ Exception -> 0x0079 }
                    if (r3 == 0) goto L_0x0058
                    processing.mode.android.EmulatorController r2 = processing.mode.android.EmulatorController.this     // Catch:{ Exception -> 0x0079 }
                    processing.mode.android.EmulatorController$State r3 = processing.mode.android.EmulatorController.State.RUNNING     // Catch:{ Exception -> 0x0079 }
                    r2.setState(r3)     // Catch:{ Exception -> 0x0079 }
                    java.util.concurrent.CountDownLatch r2 = r4
                    r2.countDown()
                    goto L_0x0029
                L_0x0079:
                    r1 = move-exception
                    java.io.PrintStream r2 = java.lang.System.err     // Catch:{ all -> 0x008f }
                    java.lang.String r3 = "Exception while waiting for emulator to boot:"
                    r2.println(r3)     // Catch:{ all -> 0x008f }
                    r1.printStackTrace()     // Catch:{ all -> 0x008f }
                    java.lang.Process r2 = r6     // Catch:{ all -> 0x008f }
                    r2.destroy()     // Catch:{ all -> 0x008f }
                    java.util.concurrent.CountDownLatch r2 = r4
                    r2.countDown()
                    goto L_0x0029
                L_0x008f:
                    r2 = move-exception
                    java.util.concurrent.CountDownLatch r3 = r4
                    r3.countDown()
                    throw r2
                */
                throw new UnsupportedOperationException("Method not decompiled: processing.mode.android.EmulatorController.C00723.run():void");
            }
        }, "EmulatorController: Wait for emulator to boot").start();
        new Thread(new Runnable() {
            public void run() {
                try {
                    int result = p.waitFor();
                    if (result != 0) {
                        System.err.println("Emulator process exited with status " + result + ".");
                        EmulatorController.this.setState(State.NOT_RUNNING);
                    }
                } catch (InterruptedException e) {
                    System.err.println("Emulator was interrupted.");
                    EmulatorController.this.setState(State.NOT_RUNNING);
                } finally {
                    p.destroy();
                    ProcessRegistry.unwatch(p);
                }
            }
        }, "EmulatorController: emulator process waitFor()").start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting for emulator to launch.");
        }
        return;
    }

    public static EmulatorController getInstance() {
        return INSTANCE;
    }
}

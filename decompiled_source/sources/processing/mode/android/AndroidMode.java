package processing.mode.android;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import processing.app.Base;
import processing.app.Editor;
import processing.app.EditorState;
import processing.app.Library;
import processing.app.RunnerListener;
import processing.app.Sketch;
import processing.app.SketchException;
import processing.mode.java.JavaMode;

public class AndroidMode extends JavaMode {
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd.HHmm");
    private File coreZipLocation;
    private AndroidRunner runner;
    private AndroidSDK sdk;

    public AndroidMode(Base base, File folder) {
        super(base, folder);
    }

    public Editor createEditor(Base base, String path, EditorState state) {
        try {
            return new AndroidEditor(base, path, state, this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return "Android";
    }

    public File[] getKeywordFiles() {
        return new File[]{Base.getContentFile("modes/java/keywords.txt")};
    }

    public File[] getExampleCategoryFolders() {
        return new File[]{new File(this.examplesFolder, "Basics"), new File(this.examplesFolder, "Topics"), new File(this.examplesFolder, "Demos"), new File(this.examplesFolder, "Sensors")};
    }

    public Library getCoreLibrary() {
        return null;
    }

    /* access modifiers changed from: protected */
    public File getCoreZipLocation() {
        if (this.coreZipLocation == null) {
            this.coreZipLocation = getContentFile("android-core.zip");
        }
        return this.coreZipLocation;
    }

    public void checkSDK(Editor parent) {
        if (this.sdk == null) {
            try {
                this.sdk = AndroidSDK.load();
                if (this.sdk == null) {
                    this.sdk = AndroidSDK.locate(parent);
                }
            } catch (BadSDKException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (this.sdk == null) {
            Base.showWarning("It's gonna be a bad day", "The Android SDK could not be loaded.\nUse of Android mode will be all but disabled.", null);
        }
    }

    public AndroidSDK getSDK() {
        return this.sdk;
    }

    public static String getDateStamp() {
        return dateFormat.format(new Date());
    }

    public static String getDateStamp(long stamp) {
        return dateFormat.format(new Date(stamp));
    }

    public void handleRunEmulator(Sketch sketch, RunnerListener listener) throws SketchException, IOException {
        listener.startIndeterminate();
        listener.statusNotice("Starting build...");
        AndroidBuild build = new AndroidBuild(sketch, this);
        listener.statusNotice("Building Android project...");
        build.build("debug");
        if (!AVD.ensureProperAVD(this.sdk)) {
            SketchException se = new SketchException("Could not create a virtual device for the emulator.");
            se.hideStackTrace();
            throw se;
        }
        listener.statusNotice("Running sketch on emulator...");
        this.runner = new AndroidRunner(build, listener);
        this.runner.launch(Devices.getInstance().getEmulator());
    }

    public void handleRunDevice(Sketch sketch, RunnerListener listener) throws SketchException, IOException {
        listener.startIndeterminate();
        listener.statusNotice("Starting build...");
        AndroidBuild build = new AndroidBuild(sketch, this);
        listener.statusNotice("Building Android project...");
        build.build("debug");
        listener.statusNotice("Running sketch on device...");
        this.runner = new AndroidRunner(build, listener);
        this.runner.launch(Devices.getInstance().getHardware());
    }

    public void handleStop(RunnerListener listener) {
        listener.statusNotice("");
        listener.stopIndeterminate();
        if (this.runner != null) {
            this.runner.close();
            this.runner = null;
        }
    }
}

package processing.mode.android;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.Future;
import processing.app.Base;
import processing.app.Preferences;
import processing.app.RunnerListener;
import processing.app.Sketch;
import processing.app.SketchException;
import processing.app.contrib.ModeContribution;

public class Commander implements RunnerListener {
    static final int BUILD = 1;
    static final int EXPORT = 4;
    static final int HELP = -1;
    static final int RUN = 2;
    static final String buildArg = "--build";
    static final String exportApplicationArg = "--export";
    static final String forceArg = "--force";
    static final String helpArg = "--help";
    static final String outputArg = "--output=";
    static final String runArg = "--run";
    static final String runArg_DEVICE = "d";
    static final String runArg_EMULATOR = "e";
    static final String sketchArg = "--sketch=";
    static final String targetArg = "--target";
    static final String targetArg_DEBUG = "debug";
    static final String targetArg_RELEASE = "release";
    private AndroidMode androidMode = null;
    private String device = runArg_DEVICE;
    private boolean force = false;
    private File outputFolder = null;
    private String outputPath = null;
    private String pdePath = null;
    private Sketch sketch;
    private File sketchFolder = null;
    private String sketchPath = null;
    private PrintStream systemErr;
    private PrintStream systemOut;
    private String target = targetArg_DEBUG;
    private int task = -1;

    public static void main(String[] args) {
        Base.setCommandLine();
        Base.initPlatform();
        Base.initRequirements();
        new Commander(args).execute();
    }

    public Commander(String[] args) {
        if (Base.DEBUG) {
            System.out.println(Arrays.toString(args));
        }
        try {
            this.systemOut = new PrintStream(System.out, true, "UTF-8");
            this.systemErr = new PrintStream(System.err, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
        }
        parseArgs(args);
        initValues();
    }

    private void parseArgs(String[] args) {
        for (String arg : args) {
            if (arg.length() != 0 && !arg.equals(helpArg)) {
                if (arg.startsWith(targetArg)) {
                    this.target = extractValue(arg, targetArg, targetArg_DEBUG);
                } else if (arg.equals(buildArg)) {
                    this.task = 1;
                } else if (arg.startsWith(runArg)) {
                    this.task = 2;
                    this.device = extractValue(arg, runArg, runArg_DEVICE);
                } else if (arg.equals(exportApplicationArg)) {
                    this.task = 4;
                } else if (arg.startsWith(sketchArg)) {
                    this.sketchPath = arg.substring(sketchArg.length());
                    this.sketchFolder = new File(this.sketchPath);
                    checkOrQuit(this.sketchFolder.exists(), this.sketchFolder + " does not exist.", false);
                    File pdeFile = new File(this.sketchFolder, new StringBuilder(String.valueOf(this.sketchFolder.getName())).append(".pde").toString());
                    checkOrQuit(pdeFile.exists(), "Not a valid sketch folder. " + pdeFile + " does not exist.", true);
                    this.pdePath = pdeFile.getAbsolutePath();
                } else if (arg.startsWith(outputArg)) {
                    this.outputPath = arg.substring(outputArg.length());
                } else if (arg.equals(forceArg)) {
                    this.force = true;
                } else {
                    complainAndQuit("I don't know anything about " + arg + ".", true);
                }
            }
        }
    }

    private static String extractValue(String arg, String template, String def) {
        String result = def;
        String withEq = arg.substring(template.length());
        if (withEq.startsWith("=")) {
            return withEq.substring(1);
        }
        return result;
    }

    private void initValues() {
        boolean z;
        boolean z2 = true;
        checkOrQuit(this.outputPath != null, "An output path must be specified.", true);
        this.outputFolder = new File(this.outputPath);
        if (this.outputFolder.exists()) {
            if (this.force) {
                Base.removeDir(this.outputFolder);
            } else {
                complainAndQuit("The output folder already exists. Use --force to remove it.", false);
            }
        }
        Preferences.init();
        Base.locateSketchbookFolder();
        if (this.sketchPath != null) {
            z = true;
        } else {
            z = false;
        }
        checkOrQuit(z, "No sketch path specified.", true);
        if (this.outputPath.equals(this.sketchPath)) {
            z2 = false;
        }
        checkOrQuit(z2, "The sketch path and output path cannot be identical.", false);
        this.androidMode = ModeContribution.load(null, Base.getContentFile("modes/android"), "processing.mode.android.AndroidMode").getMode();
        this.androidMode.checkSDK(null);
    }

    private void execute() {
        Future hardware;
        boolean z;
        boolean z2;
        boolean z3;
        if (Base.DEBUG) {
            this.systemOut.println("Build status: ");
            this.systemOut.println("Sketch:   " + this.sketchPath);
            this.systemOut.println("Output:   " + this.outputPath);
            this.systemOut.println("Force:    " + this.force);
            this.systemOut.println("Target:   " + this.target);
            this.systemOut.println("==== Task ====");
            PrintStream printStream = this.systemOut;
            StringBuilder sb = new StringBuilder("--build:  ");
            if (this.task == 1) {
                z = true;
            } else {
                z = false;
            }
            printStream.println(sb.append(z).toString());
            PrintStream printStream2 = this.systemOut;
            StringBuilder sb2 = new StringBuilder("--run:    ");
            if (this.task == 2) {
                z2 = true;
            } else {
                z2 = false;
            }
            printStream2.println(sb2.append(z2).toString());
            PrintStream printStream3 = this.systemOut;
            StringBuilder sb3 = new StringBuilder("--export: ");
            if (this.task == 4) {
                z3 = true;
            } else {
                z3 = false;
            }
            printStream3.println(sb3.append(z3).toString());
            this.systemOut.println();
        }
        if (this.task == -1) {
            printCommandLine(this.systemOut);
            System.exit(0);
        }
        checkOrQuit(this.outputFolder.mkdirs(), "Could not create the output folder.", false);
        boolean success = false;
        try {
            this.sketch = new Sketch(this.pdePath, this.androidMode);
            if (this.task == 1 || this.task == 2) {
                AndroidBuild build = new AndroidBuild(this.sketch, this.androidMode);
                build.build(this.target);
                if (this.task == 2) {
                    AndroidRunner runner = new AndroidRunner(build, this);
                    if (runArg_EMULATOR.equals(this.device)) {
                        hardware = Devices.getInstance().getEmulator();
                    } else {
                        hardware = Devices.getInstance().getHardware();
                    }
                    runner.launch(hardware);
                }
                success = true;
            } else if (this.task == 4) {
                new AndroidBuild(this.sketch, this.androidMode).exportProject();
                success = true;
            }
            if (!success) {
                System.exit(1);
            }
            this.systemOut.println("Finished.");
            System.exit(0);
        } catch (SketchException re) {
            statusError((Exception) re);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void statusNotice(String message) {
        this.systemErr.println(message);
    }

    public void statusError(String message) {
        this.systemErr.println(message);
    }

    public void statusError(Exception exception) {
        if (exception instanceof SketchException) {
            SketchException re = (SketchException) exception;
            int codeIndex = re.getCodeIndex();
            if (codeIndex != -1) {
                int line = re.getCodeLine() + 1;
                int column = re.getCodeColumn() + 1;
                this.systemErr.println(new StringBuilder(String.valueOf(this.sketch.getCode(codeIndex).getFileName())).append(":").append(line).append(":").append(column).append(":").append(line).append(":").append(column).append(":").append(" ").append(re.getMessage()).toString());
                return;
            }
            exception.printStackTrace();
            return;
        }
        exception.printStackTrace();
    }

    private void checkOrQuit(boolean condition, String lastWords, boolean schoolEmFirst) {
        if (!condition) {
            complainAndQuit(lastWords, schoolEmFirst);
        }
    }

    /* access modifiers changed from: 0000 */
    public void complainAndQuit(String lastWords, boolean schoolEmFirst) {
        if (schoolEmFirst) {
            printCommandLine(this.systemErr);
        }
        this.systemErr.println(lastWords);
        System.exit(1);
    }

    static void printCommandLine(PrintStream out) {
        out.println();
        out.println("Command line edition for Processing " + Base.VERSION_NAME + " (Android Mode)");
        out.println();
        out.println("--help               Show this help text. Congratulations.");
        out.println();
        out.println("--sketch=<name>      Specify the sketch folder (required)");
        out.println("--output=<name>      Specify the output folder (required and");
        out.println("                     cannot be the same as the sketch folder.)");
        out.println();
        out.println("--force              The sketch will not build if the output");
        out.println("                     folder already exists, because the contents");
        out.println("                     will be replaced. This option erases the");
        out.println("                     folder first. Use with extreme caution!");
        out.println();
        out.println("--target=<target>    \"debug\" or \"release\" target.");
        out.println("                     \"debug\" by default.");
        out.println("--build              Preprocess and compile a sketch into .apk file.");
        out.println("--run=<d|e>          Preprocess, compile, and run a sketch on device");
        out.println("                     or emulator. Device will be used by default.");
        out.println();
        out.println("--export             Export an application.");
        out.println();
    }

    public void startIndeterminate() {
    }

    public void stopIndeterminate() {
    }

    public void statusHalt() {
    }

    public boolean isHalted() {
        return false;
    }
}

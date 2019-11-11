package processing.mode.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import processing.app.Base;
import processing.app.Library;
import processing.app.Sketch;
import processing.app.SketchException;
import processing.app.exec.ProcessHelper;
import processing.app.exec.ProcessResult;
import processing.core.PApplet;
import processing.mode.java.JavaBuild;

class AndroidBuild extends JavaBuild {
    static final String ICON_36 = "icon-36.png";
    static final String ICON_48 = "icon-48.png";
    static final String ICON_72 = "icon-72.png";
    static final String basePackage = "processing.test";
    static final String sdkName = "2.3.3";
    static final String sdkTarget = "android-10";
    static final String sdkVersion = "10";
    private File buildFile;
    private final File coreZipFile;
    private Manifest manifest;
    private final AndroidSDK sdk;
    private String target;
    private File tmpFolder;

    public AndroidBuild(Sketch sketch, AndroidMode mode) {
        super(sketch);
        this.sdk = mode.getSDK();
        this.coreZipFile = mode.getCoreZipLocation();
    }

    public File build(String target2) throws IOException, SketchException {
        this.target = target2;
        File folder = createProject();
        if (folder == null || antBuild()) {
            return folder;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean ignorableImport(String pkg) {
        if (!pkg.startsWith("android.") && !pkg.startsWith("java.") && !pkg.startsWith("javax.") && !pkg.startsWith("org.apache.http.") && !pkg.startsWith("org.json.") && !pkg.startsWith("org.w3c.dom.") && !pkg.startsWith("org.xml.sax.") && !pkg.startsWith("processing.core.") && !pkg.startsWith("processing.data.") && !pkg.startsWith("processing.event.") && !pkg.startsWith("processing.opengl.")) {
            return false;
        }
        return true;
    }

    public File createProject() throws IOException, SketchException {
        this.tmpFolder = createTempBuildFolder(this.sketch);
        this.srcFolder = new File(this.tmpFolder, "src");
        this.binFolder = this.srcFolder;
        if (Base.DEBUG) {
            Base.openFolder(this.tmpFolder);
        }
        this.manifest = new Manifest(this.sketch);
        AndroidPreprocessor preproc = new AndroidPreprocessor(this.sketch, getPackageName());
        preproc.initSketchSize(this.sketch.getMainProgram());
        this.sketchClassName = preprocess(this.srcFolder, this.manifest.getPackageName(), preproc, false);
        if (this.sketchClassName != null) {
            this.manifest.writeBuild(new File(this.tmpFolder, "AndroidManifest.xml"), this.sketchClassName, this.target.equals("debug"));
            writeAntProps(new File(this.tmpFolder, "ant.properties"));
            this.buildFile = new File(this.tmpFolder, "build.xml");
            writeBuildXML(this.buildFile, this.sketch.getName());
            writeProjectProps(new File(this.tmpFolder, "project.properties"));
            writeLocalProps(new File(this.tmpFolder, "local.properties"));
            File resFolder = new File(this.tmpFolder, "res");
            writeRes(resFolder, this.sketchClassName);
            File libsFolder = mkdirs(this.tmpFolder, "libs");
            File assetsFolder = mkdirs(this.tmpFolder, "assets");
            Base.copyFile(this.coreZipFile, new File(libsFolder, "processing-core.jar"));
            copyLibraries(libsFolder, assetsFolder);
            copyCodeFolder(libsFolder);
            File sketchDataFolder = this.sketch.getDataFolder();
            if (sketchDataFolder.exists()) {
                Base.copyDir(sketchDataFolder, assetsFolder);
            }
            File sketchResFolder = new File(this.sketch.getFolder(), "res");
            if (sketchResFolder.exists()) {
                Base.copyDir(sketchResFolder, resFolder);
            }
        }
        return this.tmpFolder;
    }

    private File createTempBuildFolder(Sketch sketch) throws IOException {
        File tmp = File.createTempFile("android", "sketch");
        if (tmp.delete() && tmp.mkdir()) {
            return tmp;
        }
        throw new IOException("Cannot create temp dir " + tmp + " to build android sketch");
    }

    /* access modifiers changed from: protected */
    public File createExportFolder() throws IOException {
        File androidFolder = new File(this.sketch.getFolder(), "android");
        if (androidFolder.exists()) {
            File dest = new File(this.sketch.getFolder(), "android." + AndroidMode.getDateStamp(androidFolder.lastModified()));
            if (androidFolder.renameTo(dest)) {
                return androidFolder;
            }
            try {
                System.err.println("createProject renameTo() failed, resorting to mv/move instead.");
                ProcessResult pr = new ProcessHelper(new String[]{"mv", androidFolder.getAbsolutePath(), dest.getAbsolutePath()}).execute();
                if (pr.succeeded()) {
                    return androidFolder;
                }
                System.err.println(pr.getStderr());
                Base.showWarning("Failed to rename", "Could not rename the old “android” build folder.\nPlease delete, close, or rename the folder\n" + androidFolder.getAbsolutePath() + "\n" + "and try again.", null);
                Base.openFolder(this.sketch.getFolder());
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        } else if (androidFolder.mkdirs()) {
            return androidFolder;
        } else {
            Base.showWarning("Folders, folders, folders", "Could not create the necessary folders to build.\nPerhaps you have some file permissions to sort out?", null);
            return null;
        }
    }

    public File exportProject() throws IOException, SketchException {
        this.target = "debug";
        File projectFolder = createProject();
        if (projectFolder == null) {
            return null;
        }
        File exportFolder = createExportFolder();
        Base.copyDir(projectFolder, exportFolder);
        return exportFolder;
    }

    public boolean exportPackage() throws IOException, SketchException {
        File projectFolder = build("release");
        if (projectFolder == null) {
            return false;
        }
        Base.copyDir(projectFolder, createExportFolder());
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean antBuild() throws SketchException {
        Project p = new Project();
        p.setUserProperty("ant.file", this.buildFile.getAbsolutePath().replace('\\', '/'));
        p.setUserProperty("build.compiler", "extJavac");
        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(2);
        p.addBuildListener(consoleLogger);
        DefaultLogger errorLogger = new DefaultLogger();
        ByteArrayOutputStream errb = new ByteArrayOutputStream();
        errorLogger.setErrorPrintStream(new PrintStream(errb));
        ByteArrayOutputStream outb = new ByteArrayOutputStream();
        errorLogger.setOutputPrintStream(new PrintStream(outb));
        errorLogger.setMessageOutputLevel(2);
        p.addBuildListener(errorLogger);
        try {
            p.fireBuildStarted();
            p.init();
            ProjectHelper helper = ProjectHelper.getProjectHelper();
            p.addReference("ant.projectHelper", helper);
            helper.parse(p, this.buildFile);
            p.executeTarget(this.target);
            return true;
        } catch (BuildException e) {
            p.fireBuildFinished(e);
            antBuildProblems(new String(outb.toByteArray()), new String(errb.toByteArray()));
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public void antBuildProblems(String outPile, String errPile) throws SketchException {
        String[] outLines = outPile.split(System.getProperty("line.separator"));
        String[] errLines = errPile.split(System.getProperty("line.separator"));
        for (String line : outLines) {
            String str = "[javac]";
            int javacIndex = line.indexOf("[javac]");
            if (javacIndex != -1) {
                String[] pieces = PApplet.match(line.substring("[javac]".length() + javacIndex + 1), "^(.+):([0-9]+):\\s+(.+)$");
                if (pieces != null) {
                    String fileName = pieces[1];
                    SketchException rex = placeException(pieces[3], fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1), PApplet.parseInt(pieces[2]) - 1);
                    if (rex != null) {
                        throw rex;
                    }
                } else {
                    continue;
                }
            }
        }
        SketchException skex = new SketchException("Error from inside the Android tools, check the console.");
        for (String line2 : errLines) {
            if (line2.contains("Unable to resolve target 'android-10'")) {
                System.err.println("Use the Android SDK Manager (under the Android");
                System.err.println("menu) to install the SDK platform and ");
                System.err.println("Google APIs for Android 2.3.3 (API 10)");
                skex = new SketchException("Please install the SDK platform and Google APIs for API 10");
            }
        }
        skex.hideStackTrace();
        throw skex;
    }

    /* access modifiers changed from: 0000 */
    public String getPathForAPK() {
        File apkFile = new File(this.tmpFolder, "bin/" + this.sketch.getName() + "-" + (this.target.equals("release") ? "release-unsigned" : "debug") + ".apk");
        if (!apkFile.exists()) {
            return null;
        }
        return apkFile.getAbsolutePath();
    }

    private void writeAntProps(File file) {
        PrintWriter writer = PApplet.createWriter(file);
        writer.println("application-package=" + getPackageName());
        writer.flush();
        writer.close();
    }

    private void writeBuildXML(File file, String projectName) {
        PrintWriter writer = PApplet.createWriter(file);
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<project name=\"" + projectName + "\" default=\"help\">");
        writer.println("  <property file=\"local.properties\" />");
        writer.println("  <property file=\"ant.properties\" />");
        writer.println("  <property environment=\"env\" />");
        writer.println("  <condition property=\"sdk.dir\" value=\"${env.ANDROID_HOME}\">");
        writer.println("       <isset property=\"env.ANDROID_HOME\" />");
        writer.println("  </condition>");
        writer.println("  <loadproperties srcFile=\"project.properties\" />");
        writer.println("  <fail message=\"sdk.dir is missing. Make sure to generate local.properties using 'android update project'\" unless=\"sdk.dir\" />");
        writer.println("  <import file=\"custom_rules.xml\" optional=\"true\" />");
        writer.println("  <!-- version-tag: 1 -->");
        writer.println("  <import file=\"${sdk.dir}/tools/ant/build.xml\" />");
        writer.println("</project>");
        writer.flush();
        writer.close();
    }

    private void writeProjectProps(File file) {
        PrintWriter writer = PApplet.createWriter(file);
        writer.println("target=android-10");
        writer.println();
        writer.println("# Suppress the javac task warnings about \"includeAntRuntime\"");
        writer.println("build.sysclasspath=last");
        writer.flush();
        writer.close();
    }

    private void writeLocalProps(File file) {
        PrintWriter writer = PApplet.createWriter(file);
        String sdkPath = this.sdk.getSdkFolder().getAbsolutePath();
        if (Base.isWindows()) {
            writer.println("sdk.dir=" + sdkPath.replace('\\', '/'));
        } else {
            writer.println("sdk.dir=" + sdkPath);
        }
        writer.flush();
        writer.close();
    }

    private void writeRes(File resFolder, String className) throws SketchException {
        writeResLayoutMain(new File(mkdirs(resFolder, "layout"), "main.xml"));
        File sketchFolder = this.sketch.getFolder();
        File localIcon36 = new File(sketchFolder, ICON_36);
        File localIcon48 = new File(sketchFolder, ICON_48);
        File localIcon72 = new File(sketchFolder, ICON_72);
        File buildIcon48 = new File(resFolder, "drawable/icon.png");
        File buildIcon36 = new File(resFolder, "drawable-ldpi/icon.png");
        File buildIcon72 = new File(resFolder, "drawable-hdpi/icon.png");
        if (localIcon36.exists() || localIcon48.exists() || localIcon72.exists()) {
            try {
                if (localIcon36.exists() && new File(resFolder, "drawable-ldpi").mkdirs()) {
                    Base.copyFile(localIcon36, buildIcon36);
                }
                if (localIcon48.exists() && new File(resFolder, "drawable").mkdirs()) {
                    Base.copyFile(localIcon48, buildIcon48);
                }
                if (localIcon72.exists() && new File(resFolder, "drawable-hdpi").mkdirs()) {
                    Base.copyFile(localIcon72, buildIcon72);
                }
            } catch (IOException e) {
                System.err.println("Problem while copying icons.");
                e.printStackTrace();
            }
        } else {
            try {
                if (buildIcon36.getParentFile().mkdirs()) {
                    Base.copyFile(this.mode.getContentFile("icons/icon-36.png"), buildIcon36);
                } else {
                    System.err.println("Could not create \"drawable-ldpi\" folder.");
                }
                if (buildIcon48.getParentFile().mkdirs()) {
                    Base.copyFile(this.mode.getContentFile("icons/icon-48.png"), buildIcon48);
                } else {
                    System.err.println("Could not create \"drawable\" folder.");
                }
                if (buildIcon72.getParentFile().mkdirs()) {
                    Base.copyFile(this.mode.getContentFile("icons/icon-72.png"), buildIcon72);
                } else {
                    System.err.println("Could not create \"drawable-hdpi\" folder.");
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    private File mkdirs(File parent, String name) throws SketchException {
        File result = new File(parent, name);
        if (result.exists() || result.mkdirs()) {
            return result;
        }
        throw new SketchException("Could not create " + result);
    }

    private void writeResLayoutMain(File file) {
        PrintWriter writer = PApplet.createWriter(file);
        writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.println("<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"");
        writer.println("              android:orientation=\"vertical\"");
        writer.println("              android:layout_width=\"fill_parent\"");
        writer.println("              android:layout_height=\"fill_parent\">");
        writer.println("</LinearLayout>");
        writer.flush();
        writer.close();
    }

    private void copyLibraries(File libsFolder, File assetsFolder) throws IOException {
        File[] androidExports;
        Iterator it = getImportedLibraries().iterator();
        while (it.hasNext()) {
            for (File exportFile : ((Library) it.next()).getAndroidExports()) {
                String exportName = exportFile.getName();
                if (!exportFile.exists()) {
                    System.err.println(exportFile.getName() + " is mentioned in export.txt, but it's " + "a big fat lie and does not exist.");
                } else if (exportFile.isDirectory()) {
                    if (exportName.equals("armeabi") || exportName.equals("armeabi-v7a") || exportName.equals("x86")) {
                        Base.copyDir(exportFile, new File(libsFolder, exportName));
                    } else {
                        Base.copyDir(exportFile, new File(assetsFolder, exportName));
                    }
                } else if (exportName.toLowerCase().endsWith(".zip")) {
                    System.err.println(".zip files are not allowed in Android libraries.");
                    System.err.println("Please rename " + exportFile.getName() + " to be a .jar file.");
                    Base.copyFile(exportFile, new File(libsFolder, exportName.substring(0, exportName.length() - 4) + ".jar"));
                } else if (exportName.toLowerCase().endsWith(".jar")) {
                    Base.copyFile(exportFile, new File(libsFolder, exportName));
                } else {
                    Base.copyFile(exportFile, new File(assetsFolder, exportName));
                }
            }
        }
    }

    private void copyCodeFolder(File libsFolder) throws IOException {
        File[] listFiles;
        File codeFolder = this.sketch.getCodeFolder();
        if (codeFolder != null && codeFolder.exists()) {
            for (File item : codeFolder.listFiles()) {
                if (!item.isDirectory()) {
                    String name = item.getName();
                    String lcname = name.toLowerCase();
                    if (lcname.endsWith(".jar") || lcname.endsWith(".zip")) {
                        Base.copyFile(item, new File(libsFolder, name.substring(0, name.length() - 4) + ".jar"));
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public String getPackageName() {
        return this.manifest.getPackageName();
    }

    public void cleanup() {
        this.tmpFolder.deleteOnExit();
    }
}

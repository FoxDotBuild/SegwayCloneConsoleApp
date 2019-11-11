package processing.mode.android;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import processing.app.Base;
import processing.app.Platform;
import processing.app.Preferences;
import processing.app.exec.ProcessHelper;
import processing.app.exec.ProcessResult;
import processing.core.PApplet;

class AndroidSDK {
    private static final String ADB_DAEMON_MSG_1 = "daemon not running";
    private static final String ADB_DAEMON_MSG_2 = "daemon started successfully";
    private static final String ANDROID_SDK_PRIMARY = "Is the Android SDK installed?";
    private static final String ANDROID_SDK_SECONDARY = "The Android SDK does not appear to be installed, <br>because the ANDROID_SDK variable is not set. <br>If it is installed, click “Yes” to select the <br>location of the SDK, or “No” to visit the SDK<br>download site at http://developer.android.com/sdk.";
    private static final String ANDROID_SDK_URL = "http://developer.android.com/sdk/";
    private static final String NOT_ANDROID_SDK = "The selected folder does not appear to contain an Android SDK,\nor the SDK needs to be updated to the latest version.";
    private static final String SELECT_ANDROID_SDK_FOLDER = "Choose the location of the Android SDK";
    private final File androidTool;
    private final File folder;
    private final File platformTools;
    private final File tools;

    public AndroidSDK(File folder2) throws BadSDKException, IOException {
        this.folder = folder2;
        if (!folder2.exists()) {
            throw new BadSDKException(folder2 + " does not exist");
        }
        this.tools = new File(folder2, "tools");
        if (!this.tools.exists()) {
            throw new BadSDKException("There is no tools folder in " + folder2);
        }
        this.platformTools = new File(folder2, "platform-tools");
        if (!this.platformTools.exists()) {
            throw new BadSDKException("There is no platform-tools folder in " + folder2);
        }
        this.androidTool = findAndroidTool(this.tools);
        Platform p = Base.getPlatform();
        String path = p.getenv("PATH");
        p.setenv("ANDROID_SDK", folder2.getCanonicalPath());
        String path2 = new StringBuilder(String.valueOf(this.platformTools.getCanonicalPath())).append(File.pathSeparator).append(this.tools.getCanonicalPath()).append(File.pathSeparator).append(path).toString();
        File javaHome = new File(System.getProperty("java.home")).getCanonicalFile();
        p.setenv("JAVA_HOME", javaHome.getCanonicalPath());
        p.setenv("PATH", new StringBuilder(String.valueOf(new File(javaHome, "bin").getCanonicalPath())).append(File.pathSeparator).append(path2).toString());
        checkDebugCertificate();
    }

    /* access modifiers changed from: protected */
    public void checkDebugCertificate() {
        File keystoreFile = new File(new File(System.getProperty("user.home"), ".android"), "debug.keystore");
        if (keystoreFile.exists()) {
            try {
                ProcessResult result = new ProcessHelper(new String[]{"keytool", "-list", "-v", "-storepass", "android", "-keystore", keystoreFile.getAbsolutePath()}).execute();
                if (result.succeeded()) {
                    String[] lines = PApplet.split(result.getStdout(), 10);
                    int length = lines.length;
                    for (int i = 0; i < length; i++) {
                        String[] m = PApplet.match(lines[i], "Valid from: .* until: (.*)");
                        if (m != null) {
                            String timestamp = m[1].trim();
                            try {
                                long expireMillis = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(timestamp).getTime();
                                if (expireMillis < System.currentTimeMillis()) {
                                    System.out.println("Removing expired debug.keystore file.");
                                    if (!keystoreFile.renameTo(new File(keystoreFile.getParent(), "debug.keystore." + AndroidMode.getDateStamp(expireMillis)))) {
                                        System.err.println("Could not remove the expired debug.keystore file.");
                                        System.err.println("Please remove the file " + keystoreFile.getAbsolutePath());
                                    }
                                }
                            } catch (ParseException e) {
                                System.err.println("The date “" + timestamp + "” could not be parsed.");
                                System.err.println("Please report this as a bug so we can fix it.");
                            }
                        }
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public File getAndroidTool() {
        return this.androidTool;
    }

    public String getAndroidToolPath() {
        return this.androidTool.getAbsolutePath();
    }

    public File getSdkFolder() {
        return this.folder;
    }

    public File getPlatformToolsFolder() {
        return this.platformTools;
    }

    private static File findAndroidTool(File tools2) throws BadSDKException {
        if (new File(tools2, "android.exe").exists()) {
            return new File(tools2, "android.exe");
        }
        if (new File(tools2, "android.bat").exists()) {
            return new File(tools2, "android.bat");
        }
        if (new File(tools2, "android").exists()) {
            return new File(tools2, "android");
        }
        throw new BadSDKException("Cannot find the android tool in " + tools2);
    }

    public static AndroidSDK load() throws BadSDKException, IOException {
        String sdkEnvPath = Base.getPlatform().getenv("ANDROID_SDK");
        if (sdkEnvPath != null) {
            try {
                AndroidSDK androidSDK = new AndroidSDK(new File(sdkEnvPath));
                Preferences.set("android.sdk.path", sdkEnvPath);
                return androidSDK;
            } catch (BadSDKException e) {
            }
        }
        String sdkPrefsPath = Preferences.get("android.sdk.path");
        if (sdkPrefsPath != null) {
            try {
                AndroidSDK androidSDK2 = new AndroidSDK(new File(sdkPrefsPath));
                Preferences.set("android.sdk.path", sdkPrefsPath);
                return androidSDK2;
            } catch (BadSDKException e2) {
                Preferences.unset("android.sdk.path");
            }
        }
        return null;
    }

    public static AndroidSDK locate(Frame window) throws BadSDKException, IOException {
        int result = Base.showYesNoQuestion(window, "Android SDK", ANDROID_SDK_PRIMARY, ANDROID_SDK_SECONDARY);
        if (result == 2) {
            throw new BadSDKException("User canceled attempt to find SDK.");
        } else if (result == 1) {
            Base.openURL(ANDROID_SDK_URL);
            throw new BadSDKException("No SDK installed.");
        } else {
            while (true) {
                File folder2 = selectFolder(SELECT_ANDROID_SDK_FOLDER, null, window);
                if (folder2 == null) {
                    throw new BadSDKException("User canceled attempt to find SDK.");
                }
                try {
                    AndroidSDK androidSDK = new AndroidSDK(folder2);
                    Preferences.set("android.sdk.path", folder2.getAbsolutePath());
                    return androidSDK;
                } catch (BadSDKException e) {
                    JOptionPane.showMessageDialog(window, NOT_ANDROID_SDK);
                }
            }
        }
    }

    public static File selectFolder(String prompt, File folder2, Frame frame) {
        if (Base.isMacOS()) {
            if (frame == null) {
                frame = new Frame();
            }
            FileDialog fd = new FileDialog(frame, prompt, 0);
            if (folder2 != null) {
                fd.setDirectory(folder2.getParent());
            }
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            fd.setVisible(true);
            System.setProperty("apple.awt.fileDialogForDirectories", "false");
            if (fd.getFile() == null) {
                return null;
            }
            return new File(fd.getDirectory(), fd.getFile());
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(prompt);
        if (folder2 != null) {
            fc.setSelectedFile(folder2);
        }
        fc.setFileSelectionMode(1);
        if (fc.showOpenDialog(frame) == 0) {
            return fc.getSelectedFile();
        }
        return null;
    }

    public static ProcessResult runADB(String... cmd) throws InterruptedException, IOException {
        String[] adbCmd;
        String[] split;
        if (!cmd[0].equals("adb")) {
            adbCmd = PApplet.splice(cmd, "adb", 0);
        } else {
            adbCmd = cmd;
        }
        if (Base.DEBUG) {
            PApplet.println((Object) adbCmd);
        }
        ProcessResult adbResult = new ProcessHelper(adbCmd).execute();
        String out = adbResult.getStdout();
        if (!out.contains(ADB_DAEMON_MSG_1) || !out.contains(ADB_DAEMON_MSG_2)) {
            return adbResult;
        }
        StringBuilder sb = new StringBuilder();
        for (String line : out.split("\n")) {
            if (!out.contains(ADB_DAEMON_MSG_1) && !out.contains(ADB_DAEMON_MSG_2)) {
                sb.append(line).append("\n");
            }
        }
        return new ProcessResult(adbResult.getCmd(), adbResult.getResult(), sb.toString(), adbResult.getStderr(), adbResult.getTime());
    }
}

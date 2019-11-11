package processing.mode.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import processing.app.Base;
import processing.app.exec.ProcessHelper;
import processing.app.exec.ProcessResult;
import processing.core.PApplet;

public class AVD {
    private static final String AVD_CREATE_PRIMARY = "An error occurred while running “android create avd”";
    private static final String AVD_CREATE_SECONDARY = "The default Android emulator could not be set up. Make sure<br>that the Android SDK is installed properly, and that the<br>Android and Google APIs are installed for level 10.<br>(Between you and me, occasionally, this error is a red herring,<br>and your sketch may be launching shortly.)";
    private static final String AVD_LOAD_PRIMARY = "There is an error with the Processing AVD.";
    private static final String AVD_LOAD_SECONDARY = "This could mean that the Android tools need to be updated,<br>or that the Processing AVD should be deleted (it will<br>automatically re-created the next time you run Processing).<br>Open the Android SDK Manager (underneath the Android menu)<br>to check for any errors.";
    private static final String AVD_TARGET_PRIMARY = "The Google APIs are not installed properly";
    private static final String AVD_TARGET_SECONDARY = "Please re-read the installation instructions for Processing<br>found at http://android.processing.org and try again.";
    static final String DEFAULT_SDCARD_SIZE = "64M";
    static final String DEFAULT_SKIN = "WVGA800";
    static ArrayList<String> avdList;
    static ArrayList<String> badList;
    public static final AVD defaultAVD = new AVD("Processing-0217", "android-10");
    protected String name;
    protected String target;

    public AVD(String name2, String target2) {
        this.name = name2;
        this.target = target2;
    }

    protected static void list(AndroidSDK sdk) throws IOException {
        try {
            avdList = new ArrayList<>();
            badList = new ArrayList<>();
            ProcessResult listResult = new ProcessHelper(new String[]{sdk.getAndroidToolPath(), "list", "avds"}).execute();
            if (listResult.succeeded()) {
                boolean badness = false;
                Iterator it = listResult.iterator();
                while (it.hasNext()) {
                    String line = (String) it.next();
                    String[] m = PApplet.match(line, "\\s+Name\\:\\s+(\\S+)");
                    if (m != null) {
                        if (!badness) {
                            avdList.add(m[1]);
                        } else {
                            badList.add(m[1]);
                        }
                    }
                    if (line.contains("could not be loaded:")) {
                        badness = true;
                    }
                }
                return;
            }
            System.err.println("Unhappy inside exists()");
            System.err.println(listResult);
        } catch (InterruptedException e) {
        }
    }

    /* access modifiers changed from: protected */
    public boolean exists(AndroidSDK sdk) throws IOException {
        if (avdList == null) {
            list(sdk);
        }
        Iterator it = avdList.iterator();
        while (it.hasNext()) {
            String avd = (String) it.next();
            if (Base.DEBUG) {
                System.out.println("AVD.exists() checking for " + this.name + " against " + avd);
            }
            if (avd.equals(this.name)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean badness() {
        Iterator it = badList.iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equals(this.name)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean create(AndroidSDK sdk) throws IOException {
        String[] params = {sdk.getAndroidToolPath(), "create", "avd", "-n", this.name, "-t", this.target, "-c", DEFAULT_SDCARD_SIZE, "-s", DEFAULT_SKIN};
        avdList = null;
        try {
            ProcessResult createAvdResult = new ProcessHelper(params).execute("no");
            if (createAvdResult.succeeded()) {
                return true;
            }
            if (createAvdResult.toString().contains("Target id is not valid")) {
                Base.showWarningTiered("Android Error", AVD_TARGET_PRIMARY, AVD_TARGET_SECONDARY, null);
            } else {
                Base.showWarningTiered("Android Error", AVD_CREATE_PRIMARY, AVD_CREATE_SECONDARY, null);
                System.out.println(createAvdResult);
            }
            return false;
        } catch (InterruptedException e) {
        }
    }

    public static boolean ensureProperAVD(AndroidSDK sdk) {
        try {
            if (defaultAVD.exists(sdk)) {
                return true;
            }
            if (defaultAVD.badness()) {
                Base.showWarningTiered("Android Error", AVD_LOAD_PRIMARY, AVD_LOAD_SECONDARY, null);
                return false;
            }
            if (defaultAVD.create(sdk)) {
                return true;
            }
            System.out.println("at bottom of ensure proper");
            return false;
        } catch (Exception e) {
            Base.showWarningTiered("Android Error", AVD_CREATE_PRIMARY, AVD_CREATE_SECONDARY, null);
        }
    }
}

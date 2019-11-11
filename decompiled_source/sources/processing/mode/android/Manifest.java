package processing.mode.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import processing.app.Base;
import processing.app.Sketch;
import processing.core.PApplet;
import processing.data.XML;

public class Manifest {
    static final String MANIFEST_XML = "AndroidManifest.xml";
    static final String MULTIPLE_ACTIVITIES = "Processing only supports a single Activity in the AndroidManifest.xml\nfile. Only the first activity entry will be updated, and you better \nhope that's the right one, smartypants.";
    static final String PERMISSION_PREFIX = "android.permission.";
    static final String WORLD_OF_HURT_COMING = "Errors occurred while reading or writing AndroidManifest.xml,\nwhich means lots of things are likely to stop working properly.\nTo prevent losing any data, it's recommended that you use “Save As”\nto save a separate copy of your sketch, and the restart Processing.";
    private Sketch sketch;
    private XML xml;

    public Manifest(Sketch sketch2) {
        this.sketch = sketch2;
        load();
    }

    private String defaultPackageName() {
        return "processing.test." + this.sketch.getName().toLowerCase();
    }

    public String getPackageName() {
        String pkg = this.xml.getString("package");
        return pkg.length() == 0 ? defaultPackageName() : pkg;
    }

    public void setPackageName(String packageName) {
        this.xml.setString("package", packageName);
        save();
    }

    public String[] getPermissions() {
        XML[] elements = this.xml.getChildren("uses-permission");
        int count = elements.length;
        String[] names = new String[count];
        for (int i = 0; i < count; i++) {
            names[i] = elements[i].getString("android:name").substring(PERMISSION_PREFIX.length());
        }
        return names;
    }

    public void setPermissions(String[] names) {
        for (XML kid : this.xml.getChildren("uses-permission")) {
            this.xml.removeChild(kid);
        }
        for (String name : names) {
            this.xml.addChild("uses-permission").setString("android:name", new StringBuilder(PERMISSION_PREFIX).append(name).toString());
        }
        save();
    }

    public void setClassName(String className) {
        XML[] kids = this.xml.getChildren("application/activity");
        if (kids.length != 1) {
            Base.showWarning("Don't touch that", MULTIPLE_ACTIVITIES, null);
        }
        XML activity = kids[0];
        String currentName = activity.getString("android:name");
        if (currentName == null || !currentName.equals(className)) {
            activity.setString("android:name", "." + className);
            save();
        }
    }

    private void writeBlankManifest(File file) {
        PrintWriter writer = PApplet.createWriter(file);
        writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.println("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\" ");
        writer.println("          package=\"\" ");
        writer.println("          android:versionCode=\"1\" ");
        writer.println("          android:versionName=\"1.0\">");
        writer.println("  <uses-sdk android:minSdkVersion=\"10\" />");
        writer.println("  <application android:label=\"\"");
        writer.println("               android:icon=\"@drawable/icon\"");
        writer.println("               android:debuggable=\"true\">");
        writer.println("    <activity android:name=\"\">");
        writer.println("      <intent-filter>");
        writer.println("        <action android:name=\"android.intent.action.MAIN\" />");
        writer.println("        <category android:name=\"android.intent.category.LAUNCHER\" />");
        writer.println("      </intent-filter>");
        writer.println("    </activity>");
        writer.println("  </application>");
        writer.println("</manifest>");
        writer.flush();
        writer.close();
    }

    /* access modifiers changed from: protected */
    public void writeBuild(File file, String className, boolean debug) throws IOException {
        save(file);
        try {
            XML mf = new XML(file);
            try {
                if (mf.getString("package").trim().length() == 0) {
                    mf.setString("package", defaultPackageName());
                }
                XML app = mf.getChild("application");
                if (app.getString("android:label").length() == 0) {
                    app.setString("android:label", className);
                }
                app.setString("android:debuggable", debug ? "true" : "false");
                app.getChild("activity").setString("android:name", "." + className);
                PrintWriter writer = PApplet.createWriter(file);
                writer.print(mf.toString());
                writer.flush();
                writer.close();
                XML xml2 = mf;
            } catch (Exception e) {
                e = e;
                XML xml3 = mf;
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void load() {
        File manifestFile = getManifestFile();
        if (manifestFile.exists()) {
            try {
                this.xml = new XML(manifestFile);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Problem reading AndroidManifest.xml, creating a new version");
                if (!manifestFile.renameTo(new File(this.sketch.getFolder(), "AndroidManifest.xml." + AndroidMode.getDateStamp(manifestFile.lastModified())))) {
                    System.err.println("Could not move/rename " + manifestFile.getAbsolutePath());
                    System.err.println("You'll have to move or remove it before continuing.");
                    return;
                }
            }
        }
        if (this.xml == null) {
            writeBlankManifest(manifestFile);
            try {
                this.xml = new XML(manifestFile);
            } catch (FileNotFoundException e2) {
                System.err.println("Could not read " + manifestFile.getAbsolutePath());
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            } catch (ParserConfigurationException e4) {
                e4.printStackTrace();
            } catch (SAXException e5) {
                e5.printStackTrace();
            }
        }
        if (this.xml == null) {
            Base.showWarning("Error handling AndroidManifest.xml", WORLD_OF_HURT_COMING, null);
        }
    }

    /* access modifiers changed from: protected */
    public void save() {
        save(getManifestFile());
    }

    /* access modifiers changed from: protected */
    public void save(File file) {
        PrintWriter writer = PApplet.createWriter(file);
        writer.print(this.xml.toString());
        writer.flush();
        writer.close();
    }

    private File getManifestFile() {
        return new File(this.sketch.getFolder(), MANIFEST_XML);
    }
}

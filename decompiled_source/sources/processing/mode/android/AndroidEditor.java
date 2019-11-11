package processing.mode.android;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import processing.app.Base;
import processing.app.EditorState;
import processing.app.EditorToolbar;
import processing.app.Mode;
import processing.app.SketchException;
import processing.app.Toolkit;
import processing.core.PApplet;
import processing.mode.java.JavaEditor;

public class AndroidEditor extends JavaEditor {
    /* access modifiers changed from: private */
    public AndroidMode androidMode;

    protected AndroidEditor(Base base, String path, EditorState state, Mode mode) throws Exception {
        super(base, path, state, mode);
        this.androidMode = (AndroidMode) mode;
        this.androidMode.checkSDK(this);
    }

    public EditorToolbar createToolbar() {
        return new AndroidToolbar(this, this.base);
    }

    public JMenu buildFileMenu() {
        JMenuItem exportPackage = Toolkit.newJMenuItem(AndroidToolbar.getTitle(5, false), 69);
        exportPackage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AndroidEditor.this.handleExportPackage();
            }
        });
        JMenuItem exportProject = Toolkit.newJMenuItemShift(AndroidToolbar.getTitle(5, true), 69);
        exportProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AndroidEditor.this.handleExportProject();
            }
        });
        return buildFileMenu(new JMenuItem[]{exportPackage, exportProject});
    }

    public JMenu buildSketchMenu() {
        JMenuItem runItem = Toolkit.newJMenuItem(AndroidToolbar.getTitle(0, false), 82);
        runItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AndroidEditor.this.handleRunDevice();
            }
        });
        JMenuItem presentItem = Toolkit.newJMenuItemShift(AndroidToolbar.getTitle(0, true), 82);
        presentItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AndroidEditor.this.handleRunEmulator();
            }
        });
        JMenuItem stopItem = new JMenuItem(AndroidToolbar.getTitle(1, false));
        stopItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AndroidEditor.this.handleStop();
            }
        });
        return buildSketchMenu(new JMenuItem[]{runItem, presentItem, stopItem});
    }

    public JMenu buildModeMenu() {
        JMenu menu = new JMenu("Android");
        JMenuItem item = new JMenuItem("Sketch Permissions");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Permissions(AndroidEditor.this.sketch);
            }
        });
        menu.add(item);
        menu.addSeparator();
        JMenuItem item2 = new JMenuItem("Signing Key Setup");
        item2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Keys(AndroidEditor.this);
            }
        });
        item2.setEnabled(false);
        menu.add(item2);
        JMenuItem item3 = new JMenuItem("Android SDK Manager");
        item3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PApplet.exec(new String[]{AndroidEditor.this.androidMode.getSDK().getAndroidTool().getAbsolutePath(), "sdk"});
            }
        });
        menu.add(item3);
        JMenuItem item4 = new JMenuItem("Android AVD Manager");
        item4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PApplet.exec(new String[]{AndroidEditor.this.androidMode.getSDK().getAndroidTool().getAbsolutePath(), "avd"});
            }
        });
        menu.add(item4);
        JMenuItem item5 = new JMenuItem("Reset Connections");
        item5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Devices.killAdbServer();
            }
        });
        menu.add(item5);
        return menu;
    }

    public JMenu buildHelpMenu() {
        JMenu menu = AndroidEditor.super.buildHelpMenu();
        menu.addSeparator();
        JMenuItem item = new JMenuItem("Processing for Android Wiki");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Base.openURL("http://wiki.processing.org/w/Android");
            }
        });
        menu.add(item);
        JMenuItem item2 = new JMenuItem("Android Developers Site");
        item2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Base.openURL("http://developer.android.com/index.html");
            }
        });
        menu.add(item2);
        return menu;
    }

    public void showReference(String filename) {
        Base.openURL("file://" + new File(Base.getContentFile("modes/java/reference"), filename).getAbsolutePath());
    }

    public void statusError(String what) {
        AndroidEditor.super.statusError(what);
        this.toolbar.deactivate(0);
    }

    public void sketchStopped() {
        deactivateRun();
        statusEmpty();
    }

    public void handleRunEmulator() {
        new Thread() {
            public void run() {
                AndroidEditor.this.toolbar.activate(0);
                AndroidEditor.this.startIndeterminate();
                AndroidEditor.this.prepareRun();
                try {
                    AndroidEditor.this.androidMode.handleRunEmulator(AndroidEditor.this.sketch, AndroidEditor.this);
                } catch (SketchException e) {
                    AndroidEditor.this.statusError(e);
                } catch (IOException e2) {
                    AndroidEditor.this.statusError(e2);
                }
                AndroidEditor.this.stopIndeterminate();
            }
        }.start();
    }

    public void handleRunDevice() {
        new Thread() {
            public void run() {
                AndroidEditor.this.toolbar.activate(0);
                AndroidEditor.this.startIndeterminate();
                AndroidEditor.this.prepareRun();
                try {
                    AndroidEditor.this.androidMode.handleRunDevice(AndroidEditor.this.sketch, AndroidEditor.this);
                } catch (SketchException e) {
                    AndroidEditor.this.statusError(e);
                } catch (IOException e2) {
                    AndroidEditor.this.statusError(e2);
                }
                AndroidEditor.this.stopIndeterminate();
            }
        }.start();
    }

    public void handleStop() {
        this.toolbar.deactivate(0);
        stopIndeterminate();
        this.androidMode.handleStop(this);
    }

    public void handleExportProject() {
        if (handleExportCheckModified()) {
            new Thread() {
                public void run() {
                    AndroidEditor.this.toolbar.activate(5);
                    AndroidEditor.this.startIndeterminate();
                    AndroidEditor.this.statusNotice("Exporting a debug version of the sketch...");
                    try {
                        File exportFolder = new AndroidBuild(AndroidEditor.this.sketch, AndroidEditor.this.androidMode).exportProject();
                        if (exportFolder != null) {
                            Base.openFolder(exportFolder);
                            AndroidEditor.this.statusNotice("Done with export.");
                        }
                    } catch (IOException e) {
                        AndroidEditor.this.statusError(e);
                    } catch (SketchException e2) {
                        AndroidEditor.this.statusError(e2);
                    }
                    AndroidEditor.this.stopIndeterminate();
                    AndroidEditor.this.toolbar.deactivate(5);
                }
            }.start();
        }
    }

    public void handleExportPackage() {
        statusError("Exporting signed packages is not yet implemented.");
        deactivateExport();
    }
}

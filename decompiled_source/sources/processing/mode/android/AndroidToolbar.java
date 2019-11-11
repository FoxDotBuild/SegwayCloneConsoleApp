package processing.mode.android;

import java.awt.Image;
import java.awt.event.MouseEvent;
import processing.app.Base;
import processing.app.Editor;
import processing.app.EditorToolbar;

public class AndroidToolbar extends EditorToolbar {
    protected static final int EXPORT = 5;
    protected static final int NEW = 2;
    protected static final int OPEN = 3;
    protected static final int RUN = 0;
    protected static final int SAVE = 4;
    protected static final int STOP = 1;

    public AndroidToolbar(Editor editor, Base base) {
        super(editor, base);
    }

    public void init() {
        boolean z;
        Image[][] images = loadImages();
        for (int i = 0; i < 6; i++) {
            String title = getTitle(i, false);
            String title2 = getTitle(i, true);
            Image[] imageArr = images[i];
            if (i == 2) {
                z = true;
            } else {
                z = false;
            }
            addButton(title, title2, imageArr, z);
        }
    }

    public static String getTitle(int index, boolean shift) {
        switch (index) {
            case 0:
                return !shift ? "Run on Device" : "Run in Emulator";
            case 1:
                return "Stop";
            case 2:
                return "New";
            case 3:
                return "Open";
            case 4:
                return "Save";
            case 5:
                return !shift ? "Export Signed Package" : "Export Android Project";
            default:
                return null;
        }
    }

    public void handlePressed(MouseEvent e, int sel) {
        boolean shift = e.isShiftDown();
        AndroidEditor aeditor = this.editor;
        switch (sel) {
            case 0:
                if (!shift) {
                    aeditor.handleRunDevice();
                    return;
                } else {
                    aeditor.handleRunEmulator();
                    return;
                }
            case 1:
                aeditor.handleStop();
                return;
            case 2:
                this.base.handleNew();
                return;
            case 3:
                this.editor.getMode().getToolbarMenu().getPopupMenu().show(this, e.getX(), e.getY());
                return;
            case 4:
                aeditor.handleSave(false);
                return;
            case 5:
                if (!shift) {
                    aeditor.handleExportPackage();
                    return;
                } else {
                    aeditor.handleExportProject();
                    return;
                }
            default:
                return;
        }
    }
}

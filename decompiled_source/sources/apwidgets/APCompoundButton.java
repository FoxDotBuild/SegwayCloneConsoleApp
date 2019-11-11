package apwidgets;

import android.widget.CompoundButton;
import processing.core.PApplet;

public abstract class APCompoundButton extends APButton {
    private boolean checked = false;

    public APCompoundButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height, text);
    }

    public boolean isChecked() {
        if (!this.initialized) {
            return this.checked;
        }
        return ((CompoundButton) this.view).isChecked();
    }

    public void setChecked(boolean checked2) {
        this.checked = checked2;
        if (!this.initialized) {
            return;
        }
        if (checked2) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((CompoundButton) APCompoundButton.this.view).setChecked(true);
                }
            });
        } else {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((CompoundButton) APCompoundButton.this.view).setChecked(false);
                }
            });
        }
    }

    public void init(PApplet pApplet) {
        this.pApplet = pApplet;
        if (this.checked) {
            ((CompoundButton) this.view).setChecked(true);
        } else {
            ((CompoundButton) this.view).setChecked(false);
        }
        super.init(pApplet);
    }
}

package apwidgets;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import processing.core.PApplet;

public class APRadioButton extends APButton {
    protected boolean checked = false;

    public APRadioButton(String text) {
        super(0, 0, -2, -2, text);
    }

    public void init(PApplet pApplet) {
        this.pApplet = pApplet;
        if (this.view == null) {
            this.view = new RadioButton(pApplet);
        }
        super.init(pApplet);
    }

    public void setChecked(boolean checked2) {
        this.checked = checked2;
        if (this.initialized && ((RadioButton) this.view).getParent() != null && (((RadioButton) this.view).getParent() instanceof RadioGroup)) {
            if (checked2) {
                this.pApplet.runOnUiThread(new Runnable() {
                    public void run() {
                        ((RadioButton) APRadioButton.this.view).setChecked(true);
                    }
                });
            } else {
                this.pApplet.runOnUiThread(new Runnable() {
                    public void run() {
                        ((RadioButton) APRadioButton.this.view).setChecked(false);
                    }
                });
            }
        }
    }

    public boolean isChecked() {
        if (!this.initialized) {
            return this.checked;
        }
        return ((RadioButton) this.view).isChecked();
    }
}

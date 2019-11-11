package apwidgets;

import android.widget.ToggleButton;
import processing.core.PApplet;

public class APToggleButton extends APCompoundButton {
    private String textOff = null;
    private String textOn = null;

    public String getTextOn() {
        return this.textOn;
    }

    public String getTextOff() {
        return this.textOff;
    }

    public APToggleButton(int x, int y, String text) {
        super(x, y, -2, -2, text);
    }

    public APToggleButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height, text);
    }

    public void init(PApplet argPApplet) {
        this.pApplet = argPApplet;
        if (this.view == null) {
            this.view = new ToggleButton(this.pApplet);
        }
        if (this.textOn == null) {
            this.textOn = getText();
        }
        if (this.textOff == null) {
            this.textOff = getText();
        }
        ((ToggleButton) this.view).setTextOn(getTextOn());
        ((ToggleButton) this.view).setTextOff(getTextOff());
        super.init(this.pApplet);
    }

    public void setTextOn(String textOn2) {
        this.textOn = textOn2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((ToggleButton) APToggleButton.this.view).setTextOn(APToggleButton.this.getTextOn());
                }
            });
        }
    }

    public void setTextOff(String textOff2) {
        this.textOff = textOff2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((ToggleButton) APToggleButton.this.view).setTextOn(APToggleButton.this.getTextOff());
                }
            });
        }
    }
}

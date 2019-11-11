package apwidgets;

import android.widget.CheckBox;
import processing.core.PApplet;

public class APCheckBox extends APCompoundButton {
    public APCheckBox(int x, int y, String text) {
        super(x, y, -2, -2, text);
    }

    public void init(PApplet argPApplet) {
        this.pApplet = argPApplet;
        if (this.view == null) {
            this.view = new CheckBox(this.pApplet);
        }
        super.init(this.pApplet);
    }
}

package apwidgets;

import android.widget.Button;
import processing.core.PApplet;

public class APButton extends APTextView {
    public APButton(int x, int y, String text) {
        this(x, y, -2, -2, text);
    }

    public APButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height, text);
    }

    public void init(PApplet pApplet) {
        this.pApplet = pApplet;
        if (this.view == null) {
            this.view = new Button(pApplet);
        }
        super.init(pApplet);
    }
}

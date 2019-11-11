package apwidgets;

import android.widget.AnalogClock;
import processing.core.PApplet;

public class APAnalogClock extends APWidget {
    public APAnalogClock(int x, int y) {
        super(x, y, -2, -2);
    }

    public APAnalogClock(int x, int y, int size) {
        super(x, y, size, size);
    }

    public void init(PApplet pApplet) {
        this.pApplet = pApplet;
        if (this.view == null) {
            this.view = new AnalogClock(pApplet);
        }
        super.init(pApplet);
    }
}

package apwidgets;

import android.view.MotionEvent;
import android.widget.ScrollView;
import processing.core.PApplet;

public class MyScrollView extends ScrollView {
    PApplet pApplet;

    public MyScrollView(PApplet pApplet2) {
        super(pApplet2);
        this.pApplet = pApplet2;
    }

    public boolean onTouchEvent(MotionEvent evt) {
        this.pApplet.surfaceTouchEvent(evt);
        return super.onTouchEvent(evt);
    }
}

package apwidgets;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import java.util.Vector;
import processing.core.PApplet;

public class APRadioGroup extends APWidget {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int orientation = 1;
    private Vector<APRadioButton> radioButtons = new Vector<>();

    public APRadioGroup(int x, int y) {
        super(x, y, -2, -2);
    }

    public void init(PApplet pApplet) {
        this.pApplet = pApplet;
        if (this.view == null) {
            this.view = new RadioGroup(pApplet);
        }
        for (int i = 0; i < this.radioButtons.size(); i++) {
            APRadioButton radioButton = (APRadioButton) this.radioButtons.elementAt(i);
            radioButton.init(pApplet);
            ((RadioGroup) this.view).addView(radioButton.getView(), new LayoutParams(radioButton.getWidth(), radioButton.getHeight()));
            if (radioButton.checked) {
                ((RadioButton) radioButton.getView()).setChecked(true);
            }
        }
        ((RadioGroup) this.view).setOrientation(this.orientation);
        super.init(pApplet);
    }

    public void addRadioButton(APRadioButton radioButton) {
        this.radioButtons.addElement(radioButton);
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation2) {
        this.orientation = orientation2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((RadioGroup) APRadioGroup.this.view).setOrientation(APRadioGroup.this.getOrientation());
                }
            });
        }
    }
}

package apwidgets;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import processing.core.PApplet;

public abstract class APTextView extends APWidget implements TextWatcher {
    private String text;
    /* access modifiers changed from: private */
    public int textColor = -1;
    private int textSize = -1;

    public APTextView(int x, int y, int width, int height, String text2) {
        super(x, y, width, height);
        this.text = text2;
    }

    public String getText() {
        return this.text;
    }

    public void setTextSize(int textSize2) {
        this.textSize = textSize2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((TextView) APTextView.this.view).setTextSize((float) APTextView.this.getTextSize());
                }
            });
        }
    }

    public void setText(String text2) {
        this.text = text2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((TextView) APTextView.this.view).setText(APTextView.this.getText());
                }
            });
        }
    }

    public void setTextColor(int r, int g, int b, int a) {
        this.textColor = (a << 24) + (r << 16) + (g << 8) + b;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((TextView) APTextView.this.view).setTextColor(APTextView.this.textColor);
                }
            });
        }
    }

    public void init(PApplet pApplet) {
        this.pApplet = pApplet;
        ((TextView) this.view).setText(this.text);
        if (this.textColor != -1) {
            ((TextView) this.view).setTextColor(this.textColor);
        }
        if (this.textSize != -1) {
            ((TextView) this.view).setTextSize((float) this.textSize);
        }
        ((TextView) this.view).addTextChangedListener(this);
        super.init(pApplet);
    }

    public int getTextSize() {
        return this.textSize;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void afterTextChanged(Editable s) {
        this.text = ((TextView) this.view).getText().toString();
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}

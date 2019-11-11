package apwidgets;

import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import processing.core.PApplet;

public class APEditText extends APTextView implements OnEditorActionListener {
    private boolean closeImeOnDone;
    private int editorInfo;
    private boolean horizontallyScrolling;
    private int inputType;
    private APEditText nextEditText;

    /* access modifiers changed from: private */
    public int getEditorInfo() {
        return this.editorInfo;
    }

    /* access modifiers changed from: private */
    public int getInputType() {
        return this.inputType;
    }

    public boolean getHorizontallyScrolling() {
        return this.horizontallyScrolling;
    }

    public void setCloseImeOnDone(boolean closeImeOnDone2) {
        this.closeImeOnDone = closeImeOnDone2;
    }

    public void setNextEditText(APEditText nextEditText2) {
        if (nextEditText2 == null) {
            throw new NullPointerException("Have you initialized the PEditText used as an argument in calling setNextEditText?");
        }
        this.nextEditText = nextEditText2;
    }

    public APEditText(int x, int y, int width, int height) {
        super(x, y, width, height, "");
        this.nextEditText = null;
        this.closeImeOnDone = false;
        this.editorInfo = 0;
        this.inputType = 131073;
        this.horizontallyScrolling = true;
        this.shouldNotSetOnClickListener = true;
    }

    public void init(PApplet pApplet) {
        this.pApplet = pApplet;
        if (this.view == null) {
            this.view = new EditText(pApplet);
        }
        ((EditText) this.view).setInputType(this.inputType);
        ((EditText) this.view).setImeOptions(this.editorInfo);
        ((EditText) this.view).setHorizontallyScrolling(this.horizontallyScrolling);
        ((EditText) this.view).setOnEditorActionListener(this);
        super.init(pApplet);
    }

    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == 5) {
            if (this.nextEditText == null) {
                return false;
            }
            ((EditText) this.nextEditText.getView()).requestFocus();
            return true;
        } else if (actionId != 6) {
            return false;
        } else {
            onClick(this.view);
            if (this.closeImeOnDone) {
                ((InputMethodManager) this.pApplet.getSystemService("input_method")).hideSoftInputFromWindow(textView.getWindowToken(), 0);
            }
            return true;
        }
    }

    public void setImeOptions(int editorInfo2) {
        this.editorInfo = editorInfo2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((EditText) APEditText.this.view).setImeOptions(APEditText.this.getEditorInfo());
                }
            });
        }
    }

    public void setInputType(int inputType2) {
        this.inputType = inputType2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((EditText) APEditText.this.view).setInputType(APEditText.this.getInputType());
                }
            });
        }
    }

    public void setHorizontallyScrolling(boolean horizontallyScrolling2) {
        this.horizontallyScrolling = horizontallyScrolling2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((EditText) APEditText.this.view).setHorizontallyScrolling(APEditText.this.getHorizontallyScrolling());
                }
            });
        }
    }
}

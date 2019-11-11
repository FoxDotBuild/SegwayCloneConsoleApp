package apwidgets;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout.LayoutParams;
import java.lang.reflect.Method;
import java.util.Vector;
import processing.core.PApplet;

public abstract class APWidget implements OnClickListener {
    protected int height;
    protected boolean initialized = false;
    private Vector<OnClickWidgetListener> onClickWidgetListeners = new Vector<>();
    private Method onClickWidgetMethod;
    protected PApplet pApplet;
    protected boolean shouldNotSetOnClickListener = false;
    protected View view;
    protected int width;

    /* renamed from: x */
    protected int f0x;

    /* renamed from: y */
    protected int f1y;

    public void addOnClickWidgetListener(OnClickWidgetListener listener) {
        this.onClickWidgetListeners.addElement(listener);
    }

    public View getView() {
        if (!this.initialized) {
            return this.view;
        }
        long time = System.currentTimeMillis();
        while (this.view == null) {
            if (System.currentTimeMillis() >= 1000 + time) {
                break;
            }
        }
        return this.view;
    }

    public APWidget(int x, int y, int width2, int height2) {
        this.f0x = x;
        this.f1y = y;
        this.width = width2;
        this.height = height2;
    }

    public void onClick(View view2) {
        if (this.onClickWidgetMethod != null) {
            try {
                this.onClickWidgetMethod.invoke(this.pApplet, new Object[]{this});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < this.onClickWidgetListeners.size(); i++) {
            ((OnClickWidgetListener) this.onClickWidgetListeners.elementAt(i)).onClickWidget(this);
        }
    }

    public void init(PApplet pApplet2) {
        this.pApplet = pApplet2;
        try {
            this.onClickWidgetMethod = pApplet2.getClass().getMethod("onClickWidget", new Class[]{APWidget.class});
        } catch (Exception e) {
        }
        if (!this.shouldNotSetOnClickListener) {
            this.view.setOnClickListener(this);
        }
        this.initialized = true;
    }

    public void setPosition(int x, int y) {
        this.f0x = x;
        this.f1y = y;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((LayoutParams) APWidget.this.view.getLayoutParams()).setMargins(APWidget.this.getX(), APWidget.this.getY(), 0, 0);
                    APWidget.this.view.requestLayout();
                }
            });
        }
    }

    public void setSize(int width2, int height2) {
        this.width = width2;
        this.height = height2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    LayoutParams relLayout = new LayoutParams(APWidget.this.getWidth(), APWidget.this.getHeight());
                    relLayout.setMargins(APWidget.this.f0x, APWidget.this.f1y, 0, 0);
                    relLayout.addRule(9);
                    relLayout.addRule(10);
                    APWidget.this.view.setLayoutParams(relLayout);
                }
            });
        }
    }

    public int getX() {
        return this.f0x;
    }

    public int getY() {
        return this.f1y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public PApplet getPApplet() {
        return this.pApplet;
    }
}

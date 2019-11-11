package apwidgets;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.util.Vector;
import processing.core.PApplet;

public class APWidgetContainer {
    /* access modifiers changed from: private */
    public RelativeLayout layout;
    /* access modifiers changed from: private */
    public PApplet pApplet;
    /* access modifiers changed from: private */
    public MyScrollView scrollView;
    /* access modifiers changed from: private */
    public int scrollViewID = 983475893;
    /* access modifiers changed from: private */
    public Vector<View> views = new Vector<>();

    class AddWidgetTask implements Runnable {
        private APWidget pWidget;

        public AddWidgetTask(APWidget pWidget2) {
            this.pWidget = pWidget2;
        }

        public void run() {
            this.pWidget.init(APWidgetContainer.this.pApplet);
            LayoutParams relLayout = new LayoutParams(this.pWidget.getWidth(), this.pWidget.getHeight());
            relLayout.setMargins(this.pWidget.getX(), this.pWidget.getY(), 0, 0);
            relLayout.addRule(9);
            relLayout.addRule(10);
            APWidgetContainer.this.layout.addView(this.pWidget.getView(), relLayout);
            APWidgetContainer.this.views.addElement(this.pWidget.getView());
        }
    }

    static class EnableGUITask implements Runnable {
        PApplet enableGUITaskPApplet;

        public EnableGUITask(PApplet pApplet) {
            this.enableGUITaskPApplet = pApplet;
        }

        public void run() {
            this.enableGUITaskPApplet.getWindow().clearFlags(1024);
            this.enableGUITaskPApplet.getWindow().setSoftInputMode(16);
        }
    }

    class RemoveWidgetTask implements Runnable {
        private APWidget pWidget;

        public RemoveWidgetTask(APWidget pWidget2) {
            this.pWidget = pWidget2;
        }

        public void run() {
            APWidgetContainer.this.layout.removeView(this.pWidget.getView());
            APWidgetContainer.this.views.removeElement(this.pWidget.getView());
        }
    }

    private static void enableGUI(PApplet argPApplet) {
        argPApplet.runOnUiThread(new EnableGUITask(argPApplet));
    }

    public APWidgetContainer getThis() {
        return this;
    }

    /* access modifiers changed from: private */
    public void createLayout(PApplet pApplet2) {
        this.scrollView = new MyScrollView(pApplet2);
        this.scrollView.setFillViewport(true);
        this.scrollView.setId(this.scrollViewID);
        pApplet2.getWindow().addContentView(this.scrollView, new ViewGroup.LayoutParams(-1, -1));
        this.layout = new RelativeLayout(pApplet2);
        this.scrollView.addView(this.layout, new FrameLayout.LayoutParams(-1, -1));
    }

    public APWidgetContainer(PApplet pApplet2) {
        this.pApplet = pApplet2;
        pApplet2.runOnUiThread(new Runnable() {
            public void run() {
                if (APWidgetContainer.this.getPApplet().getWindow().findViewById(APWidgetContainer.this.scrollViewID) == null) {
                    APWidgetContainer.this.createLayout(APWidgetContainer.this.getPApplet());
                    return;
                }
                APWidgetContainer.this.scrollView = (MyScrollView) APWidgetContainer.this.getPApplet().getWindow().findViewById(APWidgetContainer.this.scrollViewID);
                APWidgetContainer.this.layout = (RelativeLayout) APWidgetContainer.this.scrollView.getChildAt(0);
            }
        });
    }

    public void addWidget(APWidget pWidget) {
        this.pApplet.runOnUiThread(new AddWidgetTask(pWidget));
    }

    public void removeWidget(APWidget pWidget) {
        this.pApplet.runOnUiThread(new RemoveWidgetTask(pWidget));
    }

    public void hide() {
        this.pApplet.runOnUiThread(new Runnable() {
            public void run() {
                for (int i = 0; i < APWidgetContainer.this.views.size(); i++) {
                    ((View) APWidgetContainer.this.views.elementAt(i)).setVisibility(8);
                }
            }
        });
    }

    public void show() {
        this.pApplet.runOnUiThread(new Runnable() {
            public void run() {
                for (int i = 0; i < APWidgetContainer.this.views.size(); i++) {
                    ((View) APWidgetContainer.this.views.elementAt(i)).setVisibility(0);
                }
            }
        });
    }

    public PApplet getPApplet() {
        return this.pApplet;
    }

    public void release() {
        this.pApplet.stop();
    }
}

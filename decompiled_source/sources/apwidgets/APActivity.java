package apwidgets;

import android.os.Bundle;
import processing.core.PApplet;

public class APActivity extends PApplet {
    public FakePApplet fakePApplet;

    public Object createObject(String className) {
        Object object = null;
        try {
            return Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            System.out.println(e);
            return object;
        } catch (IllegalAccessException e2) {
            System.out.println(e2);
            return object;
        } catch (ClassNotFoundException e3) {
            System.out.println(e3);
            return object;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fakePApplet = (FakePApplet) createObject(getIntent().getStringExtra("fakePApplet"));
    }

    public void setup() {
        if (this.fakePApplet != null) {
            this.fakePApplet.setup();
        }
    }

    public void draw() {
        rect(78.0f, 78.0f, 78.0f, 78.0f);
        if (this.fakePApplet != null) {
            ellipse(10.0f, 10.0f, 10.0f, 10.0f);
            this.fakePApplet.draw();
        }
    }
}

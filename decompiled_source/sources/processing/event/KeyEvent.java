package processing.event;

public class KeyEvent extends Event {
    public static final int PRESS = 1;
    public static final int RELEASE = 2;
    public static final int TYPE = 3;
    char key;
    int keyCode;

    public KeyEvent(Object obj, long j, int i, int i2, char c, int i3) {
        super(obj, j, i, i2);
        this.flavor = 1;
        this.key = c;
        this.keyCode = i3;
    }

    public char getKey() {
        return this.key;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}

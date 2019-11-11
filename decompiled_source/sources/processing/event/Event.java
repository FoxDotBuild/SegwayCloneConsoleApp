package processing.event;

public class Event {
    public static final int ALT = 8;
    public static final int CTRL = 2;
    public static final int KEY = 1;
    public static final int META = 4;
    public static final int MOUSE = 2;
    public static final int SHIFT = 1;
    public static final int TOUCH = 3;
    protected int action;
    protected int flavor;
    protected long millis;
    protected int modifiers;
    protected Object nativeObject;

    public Event(Object obj, long j, int i, int i2) {
        this.nativeObject = obj;
        this.millis = j;
        this.action = i;
        this.modifiers = i2;
    }

    public int getAction() {
        return this.action;
    }

    public int getFlavor() {
        return this.flavor;
    }

    public long getMillis() {
        return this.millis;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public Object getNative() {
        return this.nativeObject;
    }

    public boolean isAltDown() {
        return (this.modifiers & 8) != 0;
    }

    public boolean isControlDown() {
        return (this.modifiers & 2) != 0;
    }

    public boolean isMetaDown() {
        return (this.modifiers & 4) != 0;
    }

    public boolean isShiftDown() {
        return (this.modifiers & 1) != 0;
    }
}

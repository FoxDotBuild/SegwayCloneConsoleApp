package processing.event;

public class MouseEvent extends Event {
    public static final int CLICK = 3;
    public static final int DRAG = 4;
    public static final int ENTER = 6;
    public static final int EXIT = 7;
    public static final int MOVE = 5;
    public static final int PRESS = 1;
    public static final int RELEASE = 2;
    protected int button;
    protected int clickCount;

    /* renamed from: x */
    protected int f73x;

    /* renamed from: y */
    protected int f74y;

    public MouseEvent(Object obj, long j, int i, int i2, int i3, int i4, int i5, int i6) {
        super(obj, j, i, i2);
        this.flavor = 2;
        this.f73x = i3;
        this.f74y = i4;
        this.button = i5;
        this.clickCount = i6;
    }

    public int getButton() {
        return this.button;
    }

    public int getClickCount() {
        return this.clickCount;
    }

    public int getX() {
        return this.f73x;
    }

    public int getY() {
        return this.f74y;
    }
}

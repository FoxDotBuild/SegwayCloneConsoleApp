package processing.event;

public class TouchEvent extends Event {
    public TouchEvent(Object obj, long j, int i, int i2) {
        super(obj, j, i, i2);
        this.flavor = 3;
    }
}

package processing.data;

public abstract class Sort implements Runnable {
    public abstract float compare(int i, int i2);

    /* access modifiers changed from: protected */
    public int partition(int i, int i2) {
        int i3 = i2;
        while (true) {
            i++;
            if (compare(i, i2) >= 0.0f) {
                while (i3 != 0) {
                    i3--;
                    if (compare(i3, i2) <= 0.0f) {
                        break;
                    }
                }
                swap(i, i3);
                if (i >= i3) {
                    swap(i, i3);
                    return i;
                }
            }
        }
    }

    public void run() {
        int size = size();
        if (size > 1) {
            sort(0, size - 1);
        }
    }

    public abstract int size();

    /* access modifiers changed from: protected */
    public void sort(int i, int i2) {
        swap((i + i2) / 2, i2);
        int partition = partition(i - 1, i2);
        swap(partition, i2);
        if (partition - i > 1) {
            sort(i, partition - 1);
        }
        if (i2 - partition > 1) {
            sort(partition + 1, i2);
        }
    }

    public abstract void swap(int i, int i2);
}

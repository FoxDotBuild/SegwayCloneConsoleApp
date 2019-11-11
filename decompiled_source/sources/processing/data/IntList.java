package processing.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import processing.core.PApplet;

public class IntList implements Iterable<Integer> {
    protected int count;
    protected int[] data;

    public IntList() {
        this.data = new int[10];
    }

    public IntList(int i) {
        this.data = new int[i];
    }

    public IntList(Iterable<Integer> iterable) {
        this(10);
        for (Integer intValue : iterable) {
            append(intValue.intValue());
        }
    }

    public IntList(int[] iArr) {
        this.count = iArr.length;
        this.data = new int[this.count];
        System.arraycopy(iArr, 0, this.data, 0, this.count);
    }

    private void crop() {
        if (this.count != this.data.length) {
            this.data = PApplet.subset(this.data, 0, this.count);
        }
    }

    public void add(int i, int i2) {
        int[] iArr = this.data;
        iArr[i] = iArr[i] + i2;
    }

    public void append(int i) {
        if (this.count == this.data.length) {
            this.data = PApplet.expand(this.data);
        }
        int[] iArr = this.data;
        int i2 = this.count;
        this.count = i2 + 1;
        iArr[i2] = i;
    }

    public void append(IntList intList) {
        for (int append : intList.values()) {
            append(append);
        }
    }

    public void append(int[] iArr) {
        for (int append : iArr) {
            append(append);
        }
    }

    public int[] array() {
        return array(null);
    }

    public int[] array(int[] iArr) {
        if (iArr == null || iArr.length != this.count) {
            iArr = new int[this.count];
        }
        System.arraycopy(this.data, 0, iArr, 0, this.count);
        return iArr;
    }

    public void clear() {
        this.count = 0;
    }

    public IntList copy() {
        IntList intList = new IntList(this.data);
        intList.count = this.count;
        return intList;
    }

    public void div(int i, int i2) {
        int[] iArr = this.data;
        iArr[i] = iArr[i] / i2;
    }

    public int get(int i) {
        return this.data[i];
    }

    public IntList getSubset(int i) {
        return getSubset(i, this.count - i);
    }

    public IntList getSubset(int i, int i2) {
        IntList intList = new IntList(i2);
        for (int i3 = 0; i3 < i2; i3++) {
            System.arraycopy(this.data, i, intList.data, 0, i2);
        }
        return intList;
    }

    public boolean hasValue(int i) {
        for (int i2 = 0; i2 < this.count; i2++) {
            if (this.data[i2] == i) {
                return true;
            }
        }
        return false;
    }

    public void increment(int i) {
        int[] iArr = this.data;
        iArr[i] = iArr[i] + 1;
    }

    public int index(int i) {
        for (int i2 = 0; i2 < this.count; i2++) {
            if (this.data[i2] == i) {
                return i2;
            }
        }
        return -1;
    }

    public void insert(int i, IntList intList) {
        insert(i, intList.values());
    }

    public void insert(int i, int[] iArr) {
        if (i < 0) {
            throw new IllegalArgumentException("insert() index cannot be negative: it was " + i);
        } else if (i >= iArr.length) {
            throw new IllegalArgumentException("insert() index " + i + " is past the end of this list");
        } else {
            int[] iArr2 = new int[(this.count + iArr.length)];
            System.arraycopy(this.data, 0, iArr2, 0, Math.min(this.count, i));
            System.arraycopy(iArr, 0, iArr2, i, iArr.length);
            System.arraycopy(this.data, i, iArr2, iArr.length + i, this.count - i);
            this.count += iArr.length;
            this.data = iArr2;
        }
    }

    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int index = -1;

            public boolean hasNext() {
                return this.index + 1 < IntList.this.count;
            }

            public Integer next() {
                int[] iArr = IntList.this.data;
                int i = this.index + 1;
                this.index = i;
                return Integer.valueOf(iArr[i]);
            }

            public void remove() {
                IntList.this.remove(this.index);
            }
        };
    }

    public int max() {
        if (this.count == 0) {
            throw new ArrayIndexOutOfBoundsException("Cannot use max() on IntList of length 0.");
        }
        int i = this.data[0];
        for (int i2 = 1; i2 < this.data.length; i2++) {
            if (this.data[i2] > i) {
                i = this.data[i2];
            }
        }
        return i;
    }

    public int min() {
        if (this.count == 0) {
            throw new ArrayIndexOutOfBoundsException("Cannot use min() on IntList of length 0.");
        }
        int i = this.data[0];
        for (int i2 = 1; i2 < this.data.length; i2++) {
            if (this.data[i2] < i) {
                i = this.data[i2];
            }
        }
        return i;
    }

    public void mult(int i, int i2) {
        int[] iArr = this.data;
        iArr[i] = iArr[i] * i2;
    }

    public void remove(int i) {
        while (i < this.count) {
            this.data[i] = this.data[i + 1];
            i++;
        }
        this.count--;
    }

    public boolean removeValue(int i) {
        int index = index(i);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    public boolean removeValues(int i) {
        boolean z = false;
        int i2 = 0;
        for (int i3 = 0; i3 < this.count; i3++) {
            if (this.data[i3] != i) {
                int i4 = i2 + 1;
                this.data[i2] = this.data[i3];
                i2 = i4;
            }
        }
        if (this.count == i2) {
            z = true;
        }
        this.count = i2;
        return z;
    }

    public void resize(int i) {
        if (i > this.data.length) {
            int[] iArr = new int[i];
            System.arraycopy(this.data, 0, iArr, 0, this.count);
            this.data = iArr;
        } else if (i > this.count) {
            Arrays.fill(this.data, this.count, i, 0);
        }
        this.count = i;
    }

    public void reverse() {
        int i = this.count - 1;
        for (int i2 = 0; i2 < this.count / 2; i2++) {
            int i3 = this.data[i2];
            this.data[i2] = this.data[i];
            this.data[i] = i3;
            i--;
        }
    }

    public void set(int i, int i2) {
        if (i >= this.count) {
            this.data = PApplet.expand(this.data, i + 1);
            for (int i3 = this.count; i3 < i; i3++) {
                this.data[i3] = 0;
            }
            this.count = i + 1;
        }
        this.data[i] = i2;
    }

    public void shuffle() {
        Random random = new Random();
        int i = this.count;
        while (i > 1) {
            int nextInt = random.nextInt(i);
            i--;
            int i2 = this.data[i];
            this.data[i] = this.data[nextInt];
            this.data[nextInt] = i2;
        }
    }

    public void shuffle(PApplet pApplet) {
        int i = this.count;
        while (i > 1) {
            int random = (int) pApplet.random((float) i);
            i--;
            int i2 = this.data[i];
            this.data[i] = this.data[random];
            this.data[random] = i2;
        }
    }

    public int size() {
        return this.count;
    }

    public void sort() {
        Arrays.sort(this.data, 0, this.count);
    }

    public void sortReverse() {
        new Sort() {
            public float compare(int i, int i2) {
                return (float) (IntList.this.data[i2] - IntList.this.data[i]);
            }

            public int size() {
                return IntList.this.count;
            }

            public void swap(int i, int i2) {
                int i3 = IntList.this.data[i];
                IntList.this.data[i] = IntList.this.data[i2];
                IntList.this.data[i2] = i3;
            }
        }.run();
    }

    public void sub(int i, int i2) {
        int[] iArr = this.data;
        iArr[i] = iArr[i] - i2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName() + " size=" + size() + " [ ");
        for (int i = 0; i < size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(i + ": " + this.data[i]);
        }
        sb.append(" ]");
        return sb.toString();
    }

    public int[] values() {
        crop();
        return this.data;
    }
}

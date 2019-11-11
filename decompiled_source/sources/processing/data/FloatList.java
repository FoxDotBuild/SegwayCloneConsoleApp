package processing.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import processing.core.PApplet;

public class FloatList implements Iterable<Float> {
    int count;
    float[] data;

    public FloatList() {
        this.data = new float[10];
    }

    public FloatList(int i) {
        this.data = new float[i];
    }

    public FloatList(Iterable<Float> iterable) {
        this(10);
        for (Float floatValue : iterable) {
            append(floatValue.floatValue());
        }
    }

    public FloatList(float[] fArr) {
        this.count = fArr.length;
        this.data = new float[this.count];
        System.arraycopy(fArr, 0, this.data, 0, this.count);
    }

    private void crop() {
        if (this.count != this.data.length) {
            this.data = PApplet.subset(this.data, 0, this.count);
        }
    }

    public void add(int i, float f) {
        float[] fArr = this.data;
        fArr[i] = fArr[i] + f;
    }

    public void append(float f) {
        if (this.count == this.data.length) {
            this.data = PApplet.expand(this.data);
        }
        float[] fArr = this.data;
        int i = this.count;
        this.count = i + 1;
        fArr[i] = f;
    }

    public void append(FloatList floatList) {
        for (float append : floatList.values()) {
            append(append);
        }
    }

    public void append(float[] fArr) {
        for (float append : fArr) {
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

    public FloatList copy() {
        FloatList floatList = new FloatList(this.data);
        floatList.count = this.count;
        return floatList;
    }

    public void div(int i, float f) {
        float[] fArr = this.data;
        fArr[i] = fArr[i] / f;
    }

    public float get(int i) {
        return this.data[i];
    }

    public boolean hasValue(float f) {
        if (Float.isNaN(f)) {
            for (int i = 0; i < this.count; i++) {
                if (Float.isNaN(this.data[i])) {
                    return true;
                }
            }
            return false;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            if (this.data[i2] == f) {
                return true;
            }
        }
        return false;
    }

    public int index(float f) {
        for (int i = 0; i < this.count; i++) {
            if (this.data[i] == f) {
                return i;
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
            float[] fArr = new float[(this.count + iArr.length)];
            System.arraycopy(this.data, 0, fArr, 0, Math.min(this.count, i));
            System.arraycopy(iArr, 0, fArr, i, iArr.length);
            System.arraycopy(this.data, i, fArr, iArr.length + i, this.count - i);
            this.count += iArr.length;
            this.data = fArr;
        }
    }

    public Iterator<Float> iterator() {
        return new Iterator<Float>() {
            int index = -1;

            public boolean hasNext() {
                return this.index + 1 < FloatList.this.count;
            }

            public Float next() {
                float[] fArr = FloatList.this.data;
                int i = this.index + 1;
                this.index = i;
                return Float.valueOf(fArr[i]);
            }

            public void remove() {
                FloatList.this.remove(this.index);
            }
        };
    }

    public float max() {
        float f = Float.NaN;
        if (this.count == 0) {
            throw new ArrayIndexOutOfBoundsException("Cannot use max() on IntList of length 0.");
        }
        if (this.data.length != 0) {
            int i = 0;
            while (true) {
                if (i >= this.data.length) {
                    break;
                } else if (this.data[i] == this.data[i]) {
                    f = this.data[i];
                    while (true) {
                        i++;
                        if (i >= this.data.length) {
                            break;
                        }
                        float f2 = this.data[i];
                        if (!Float.isNaN(f2) && f2 > f) {
                            f = this.data[i];
                        }
                    }
                } else {
                    i++;
                }
            }
        }
        return f;
    }

    public float min() {
        float f = Float.NaN;
        if (this.count == 0) {
            throw new ArrayIndexOutOfBoundsException("Cannot use min() on IntList of length 0.");
        }
        if (this.data.length != 0) {
            int i = 0;
            while (true) {
                if (i >= this.data.length) {
                    break;
                } else if (this.data[i] == this.data[i]) {
                    f = this.data[i];
                    while (true) {
                        i++;
                        if (i >= this.data.length) {
                            break;
                        }
                        float f2 = this.data[i];
                        if (!Float.isNaN(f2) && f2 < f) {
                            f = this.data[i];
                        }
                    }
                } else {
                    i++;
                }
            }
        }
        return f;
    }

    public void mult(int i, float f) {
        float[] fArr = this.data;
        fArr[i] = fArr[i] * f;
    }

    public void remove(int i) {
        while (i < this.count) {
            this.data[i] = this.data[i + 1];
            i++;
        }
        this.count--;
    }

    public boolean removeValue(float f) {
        if (Float.isNaN(f)) {
            for (int i = 0; i < this.count; i++) {
                if (Float.isNaN(this.data[i])) {
                    remove(i);
                    return true;
                }
            }
            return false;
        }
        int index = index(f);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    public boolean removeValues(float f) {
        int i;
        if (Float.isNaN(f)) {
            i = 0;
            for (int i2 = 0; i2 < this.count; i2++) {
                if (!Float.isNaN(this.data[i2])) {
                    int i3 = i + 1;
                    this.data[i] = this.data[i2];
                    i = i3;
                }
            }
        } else {
            i = 0;
            for (int i4 = 0; i4 < this.count; i4++) {
                if (this.data[i4] != f) {
                    int i5 = i + 1;
                    this.data[i] = this.data[i4];
                    i = i5;
                }
            }
        }
        if (this.count == i) {
            return false;
        }
        this.count = i;
        return true;
    }

    public boolean replaceValue(float f, float f2) {
        if (Float.isNaN(f)) {
            for (int i = 0; i < this.count; i++) {
                if (Float.isNaN(this.data[i])) {
                    this.data[i] = f2;
                    return true;
                }
            }
            return false;
        }
        int index = index(f);
        if (index == -1) {
            return false;
        }
        this.data[index] = f2;
        return true;
    }

    public boolean replaceValues(float f, float f2) {
        boolean z;
        int i = 0;
        if (Float.isNaN(f)) {
            z = false;
            while (i < this.count) {
                if (Float.isNaN(this.data[i])) {
                    this.data[i] = f2;
                    z = true;
                }
                i++;
            }
        } else {
            z = false;
            while (i < this.count) {
                if (this.data[i] == f) {
                    this.data[i] = f2;
                    z = true;
                }
                i++;
            }
        }
        return z;
    }

    public void resize(int i) {
        if (i > this.data.length) {
            float[] fArr = new float[i];
            System.arraycopy(this.data, 0, fArr, 0, this.count);
            this.data = fArr;
        } else if (i > this.count) {
            Arrays.fill(this.data, this.count, i, 0.0f);
        }
        this.count = i;
    }

    public void reverse() {
        int i = this.count - 1;
        for (int i2 = 0; i2 < this.count / 2; i2++) {
            float f = this.data[i2];
            this.data[i2] = this.data[i];
            this.data[i] = f;
            i--;
        }
    }

    public void set(int i, float f) {
        if (i >= this.count) {
            this.data = PApplet.expand(this.data, i + 1);
            for (int i2 = this.count; i2 < i; i2++) {
                this.data[i2] = 0.0f;
            }
            this.count = i + 1;
        }
        this.data[i] = f;
    }

    public void shuffle() {
        Random random = new Random();
        int i = this.count;
        while (i > 1) {
            int nextInt = random.nextInt(i);
            i--;
            float f = this.data[i];
            this.data[i] = this.data[nextInt];
            this.data[nextInt] = f;
        }
    }

    public void shuffle(PApplet pApplet) {
        int i = this.count;
        while (i > 1) {
            int random = (int) pApplet.random((float) i);
            i--;
            float f = this.data[i];
            this.data[i] = this.data[random];
            this.data[random] = f;
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
                return FloatList.this.data[i2] - FloatList.this.data[i];
            }

            public int size() {
                return FloatList.this.count;
            }

            public void swap(int i, int i2) {
                float f = FloatList.this.data[i];
                FloatList.this.data[i] = FloatList.this.data[i2];
                FloatList.this.data[i2] = f;
            }
        }.run();
    }

    public void sub(int i, float f) {
        float[] fArr = this.data;
        fArr[i] = fArr[i] - f;
    }

    public void subset(int i) {
        subset(i, this.count - i);
    }

    public void subset(int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            this.data[i3] = this.data[i3 + i];
        }
        this.count = i2;
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

    public float[] values() {
        crop();
        return this.data;
    }
}

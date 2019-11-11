package processing.data;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import processing.core.PApplet;

public class FloatDict {
    protected int count;
    private HashMap<String, Integer> indices;
    protected String[] keys;
    protected float[] values;

    public FloatDict() {
        this.indices = new HashMap<>();
        this.count = 0;
        this.keys = new String[10];
        this.values = new float[10];
    }

    public FloatDict(int i) {
        this.indices = new HashMap<>();
        this.count = 0;
        this.keys = new String[i];
        this.values = new float[i];
    }

    public FloatDict(BufferedReader bufferedReader) {
        this.indices = new HashMap<>();
        String[] loadStrings = PApplet.loadStrings(bufferedReader);
        this.keys = new String[loadStrings.length];
        this.values = new float[loadStrings.length];
        for (String split : loadStrings) {
            String[] split2 = PApplet.split(split, 9);
            if (split2.length == 2) {
                this.keys[this.count] = split2[0];
                this.values[this.count] = PApplet.parseFloat(split2[1]);
                this.count++;
            }
        }
    }

    public FloatDict(String[] strArr, float[] fArr) {
        this.indices = new HashMap<>();
        if (strArr.length != fArr.length) {
            throw new IllegalArgumentException("key and value arrays must be the same length");
        }
        this.keys = strArr;
        this.values = fArr;
        this.count = strArr.length;
        for (int i = 0; i < this.count; i++) {
            this.indices.put(strArr[i], Integer.valueOf(i));
        }
    }

    public void add(String str, float f) {
        int index = index(str);
        if (index == -1) {
            create(str, f);
            return;
        }
        float[] fArr = this.values;
        fArr[index] = fArr[index] + f;
    }

    public void clear() {
        this.count = 0;
        this.indices = new HashMap<>();
    }

    public FloatDict copy() {
        FloatDict floatDict = new FloatDict(this.count);
        System.arraycopy(this.keys, 0, floatDict.keys, 0, this.count);
        System.arraycopy(this.values, 0, floatDict.values, 0, this.count);
        for (int i = 0; i < this.count; i++) {
            floatDict.indices.put(this.keys[i], Integer.valueOf(i));
        }
        floatDict.count = this.count;
        return floatDict;
    }

    /* access modifiers changed from: protected */
    public void create(String str, float f) {
        if (this.count == this.keys.length) {
            this.keys = PApplet.expand(this.keys);
            this.values = PApplet.expand(this.values);
        }
        this.indices.put(str, new Integer(this.count));
        this.keys[this.count] = str;
        this.values[this.count] = f;
        this.count++;
    }

    /* access modifiers changed from: protected */
    public void crop() {
        if (this.count != this.keys.length) {
            this.keys = PApplet.subset(this.keys, 0, this.count);
            this.values = PApplet.subset(this.values, 0, this.count);
        }
    }

    public void div(String str, float f) {
        int index = index(str);
        if (index != -1) {
            float[] fArr = this.values;
            fArr[index] = fArr[index] / f;
        }
    }

    public float get(String str) {
        int index = index(str);
        if (index == -1) {
            return 0.0f;
        }
        return this.values[index];
    }

    public boolean hasKey(String str) {
        return index(str) != -1;
    }

    public int index(String str) {
        Integer num = (Integer) this.indices.get(str);
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }

    public String key(int i) {
        return this.keys[i];
    }

    public String[] keyArray() {
        return keyArray(null);
    }

    public String[] keyArray(String[] strArr) {
        if (strArr == null || strArr.length != this.count) {
            strArr = new String[this.count];
        }
        System.arraycopy(this.keys, 0, strArr, 0, this.count);
        return strArr;
    }

    public Iterable<String> keys() {
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    int index = -1;

                    public boolean hasNext() {
                        return this.index + 1 < FloatDict.this.size();
                    }

                    public String next() {
                        FloatDict floatDict = FloatDict.this;
                        int i = this.index + 1;
                        this.index = i;
                        return floatDict.key(i);
                    }

                    public void remove() {
                        FloatDict.this.removeIndex(this.index);
                    }
                };
            }
        };
    }

    public void mult(String str, float f) {
        int index = index(str);
        if (index != -1) {
            float[] fArr = this.values;
            fArr[index] = fArr[index] * f;
        }
    }

    public void remove(String str) {
        removeIndex(index(str));
    }

    public void removeIndex(int i) {
        this.indices.remove(this.keys[i]);
        while (i < this.count - 1) {
            this.keys[i] = this.keys[i + 1];
            this.values[i] = this.values[i + 1];
            this.indices.put(this.keys[i], Integer.valueOf(i));
            i++;
        }
        this.count--;
        this.keys[this.count] = null;
        this.values[this.count] = 0.0f;
    }

    public void set(String str, int i) {
        int index = index(str);
        if (index == -1) {
            create(str, (float) i);
        } else {
            this.values[index] = (float) i;
        }
    }

    public int size() {
        return this.count;
    }

    /* access modifiers changed from: protected */
    public void sortImpl(final boolean z, final boolean z2) {
        new Sort() {
            public float compare(int i, int i2) {
                float f;
                if (z) {
                    f = (float) FloatDict.this.keys[i].compareToIgnoreCase(FloatDict.this.keys[i2]);
                    if (f == 0.0f) {
                        return FloatDict.this.values[i] - FloatDict.this.values[i2];
                    }
                } else {
                    f = FloatDict.this.values[i] - FloatDict.this.values[i2];
                    if (f == 0.0f) {
                        f = (float) FloatDict.this.keys[i].compareToIgnoreCase(FloatDict.this.keys[i2]);
                    }
                }
                return z2 ? -f : f;
            }

            public int size() {
                return FloatDict.this.count;
            }

            public void swap(int i, int i2) {
                FloatDict.this.swap(i, i2);
            }
        }.run();
    }

    public void sortKeys() {
        sortImpl(true, false);
    }

    public void sortKeysReverse() {
        sortImpl(true, true);
    }

    public void sortValues() {
        sortImpl(false, false);
    }

    public void sortValuesReverse() {
        sortImpl(false, true);
    }

    public void sub(String str, float f) {
        add(str, -f);
    }

    /* access modifiers changed from: protected */
    public void swap(int i, int i2) {
        String str = this.keys[i];
        float f = this.values[i];
        this.keys[i] = this.keys[i2];
        this.values[i] = this.values[i2];
        this.keys[i2] = str;
        this.values[i2] = f;
        this.indices.put(this.keys[i], new Integer(i));
        this.indices.put(this.keys[i2], new Integer(i2));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName() + " size=" + size() + " { ");
        for (int i = 0; i < size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("\"" + this.keys[i] + "\": " + this.values[i]);
        }
        sb.append(" }");
        return sb.toString();
    }

    public float value(int i) {
        return this.values[i];
    }

    public float[] valueArray() {
        return valueArray(null);
    }

    public float[] valueArray(float[] fArr) {
        if (fArr == null || fArr.length != size()) {
            fArr = new float[this.count];
        }
        System.arraycopy(this.values, 0, fArr, 0, this.count);
        return fArr;
    }

    public Iterable<Float> values() {
        return new Iterable<Float>() {
            public Iterator<Float> iterator() {
                return new Iterator<Float>() {
                    int index = -1;

                    public boolean hasNext() {
                        return this.index + 1 < FloatDict.this.size();
                    }

                    public Float next() {
                        FloatDict floatDict = FloatDict.this;
                        int i = this.index + 1;
                        this.index = i;
                        return Float.valueOf(floatDict.value(i));
                    }

                    public void remove() {
                        FloatDict.this.removeIndex(this.index);
                    }
                };
            }
        };
    }

    public void write(PrintWriter printWriter) {
        for (int i = 0; i < this.count; i++) {
            printWriter.println(this.keys[i] + "\t" + this.values[i]);
        }
        printWriter.flush();
    }
}

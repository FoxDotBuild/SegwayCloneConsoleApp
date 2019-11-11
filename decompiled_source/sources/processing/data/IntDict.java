package processing.data;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import processing.core.PApplet;

public class IntDict {
    protected int count;
    private HashMap<String, Integer> indices;
    protected String[] keys;
    protected int[] values;

    public IntDict() {
        this.indices = new HashMap<>();
        this.count = 0;
        this.keys = new String[10];
        this.values = new int[10];
    }

    public IntDict(int i) {
        this.indices = new HashMap<>();
        this.count = 0;
        this.keys = new String[i];
        this.values = new int[i];
    }

    public IntDict(BufferedReader bufferedReader) {
        this.indices = new HashMap<>();
        String[] loadStrings = PApplet.loadStrings(bufferedReader);
        this.keys = new String[loadStrings.length];
        this.values = new int[loadStrings.length];
        for (String split : loadStrings) {
            String[] split2 = PApplet.split(split, 9);
            if (split2.length == 2) {
                this.keys[this.count] = split2[0];
                this.values[this.count] = PApplet.parseInt(split2[1]);
                this.count++;
            }
        }
    }

    public IntDict(String[] strArr, int[] iArr) {
        this.indices = new HashMap<>();
        if (strArr.length != iArr.length) {
            throw new IllegalArgumentException("key and value arrays must be the same length");
        }
        this.keys = strArr;
        this.values = iArr;
        this.count = strArr.length;
        for (int i = 0; i < this.count; i++) {
            this.indices.put(strArr[i], Integer.valueOf(i));
        }
    }

    private void crop() {
        if (this.count != this.keys.length) {
            this.keys = PApplet.subset(this.keys, 0, this.count);
            this.values = PApplet.subset(this.values, 0, this.count);
        }
    }

    public void add(String str, int i) {
        int index = index(str);
        if (index == -1) {
            create(str, i);
            return;
        }
        int[] iArr = this.values;
        iArr[index] = iArr[index] + i;
    }

    public void clear() {
        this.count = 0;
        this.indices = new HashMap<>();
    }

    public IntDict copy() {
        IntDict intDict = new IntDict(this.count);
        System.arraycopy(this.keys, 0, intDict.keys, 0, this.count);
        System.arraycopy(this.values, 0, intDict.values, 0, this.count);
        for (int i = 0; i < this.count; i++) {
            intDict.indices.put(this.keys[i], Integer.valueOf(i));
        }
        intDict.count = this.count;
        return intDict;
    }

    /* access modifiers changed from: protected */
    public void create(String str, int i) {
        if (this.count == this.keys.length) {
            this.keys = PApplet.expand(this.keys);
            this.values = PApplet.expand(this.values);
        }
        this.indices.put(str, new Integer(this.count));
        this.keys[this.count] = str;
        this.values[this.count] = i;
        this.count++;
    }

    public void div(String str, int i) {
        int index = index(str);
        if (index != -1) {
            int[] iArr = this.values;
            iArr[index] = iArr[index] / i;
        }
    }

    public int get(String str) {
        int index = index(str);
        if (index == -1) {
            return 0;
        }
        return this.values[index];
    }

    public boolean hasKey(String str) {
        return index(str) != -1;
    }

    public void increment(String str) {
        add(str, 1);
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

    public Iterator<String> keyIterator() {
        return new Iterator<String>() {
            int index = -1;

            public boolean hasNext() {
                return this.index + 1 < IntDict.this.size();
            }

            public String next() {
                IntDict intDict = IntDict.this;
                int i = this.index + 1;
                this.index = i;
                return intDict.key(i);
            }

            public void remove() {
                IntDict.this.removeIndex(this.index);
            }
        };
    }

    public String[] keys() {
        crop();
        return this.keys;
    }

    public void mult(String str, int i) {
        int index = index(str);
        if (index != -1) {
            int[] iArr = this.values;
            iArr[index] = iArr[index] * i;
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
        this.values[this.count] = 0;
    }

    public void set(String str, int i) {
        int index = index(str);
        if (index == -1) {
            create(str, i);
        } else {
            this.values[index] = i;
        }
    }

    public int size() {
        return this.count;
    }

    /* access modifiers changed from: protected */
    public void sortImpl(final boolean z, final boolean z2) {
        new Sort() {
            public float compare(int i, int i2) {
                int i3;
                if (z) {
                    i3 = IntDict.this.keys[i].compareToIgnoreCase(IntDict.this.keys[i2]);
                    if (i3 == 0) {
                        return (float) (IntDict.this.values[i] - IntDict.this.values[i2]);
                    }
                } else {
                    i3 = IntDict.this.values[i] - IntDict.this.values[i2];
                    if (i3 == 0) {
                        i3 = IntDict.this.keys[i].compareToIgnoreCase(IntDict.this.keys[i2]);
                    }
                }
                return z2 ? (float) (-i3) : (float) i3;
            }

            public int size() {
                return IntDict.this.count;
            }

            public void swap(int i, int i2) {
                IntDict.this.swap(i, i2);
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

    public void sub(String str, int i) {
        add(str, -i);
    }

    /* access modifiers changed from: protected */
    public void swap(int i, int i2) {
        String str = this.keys[i];
        int i3 = this.values[i];
        this.keys[i] = this.keys[i2];
        this.values[i] = this.values[i2];
        this.keys[i2] = str;
        this.values[i2] = i3;
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

    public int value(int i) {
        return this.values[i];
    }

    public int[] valueArray() {
        return valueArray(null);
    }

    public int[] valueArray(int[] iArr) {
        if (iArr == null || iArr.length != size()) {
            iArr = new int[this.count];
        }
        System.arraycopy(this.values, 0, iArr, 0, this.count);
        return iArr;
    }

    public Iterable<Integer> values() {
        return new Iterable<Integer>() {
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    int index = -1;

                    public boolean hasNext() {
                        return this.index + 1 < IntDict.this.size();
                    }

                    public Integer next() {
                        IntDict intDict = IntDict.this;
                        int i = this.index + 1;
                        this.index = i;
                        return Integer.valueOf(intDict.value(i));
                    }

                    public void remove() {
                        IntDict.this.removeIndex(this.index);
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

package processing.data;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import processing.core.PApplet;

public class StringDict {
    protected int count;
    private HashMap<String, Integer> indices;
    protected String[] keys;
    protected String[] values;

    public StringDict() {
        this.indices = new HashMap<>();
        this.count = 0;
        this.keys = new String[10];
        this.values = new String[10];
    }

    public StringDict(int i) {
        this.indices = new HashMap<>();
        this.count = 0;
        this.keys = new String[i];
        this.values = new String[i];
    }

    public StringDict(BufferedReader bufferedReader) {
        this.indices = new HashMap<>();
        String[] loadStrings = PApplet.loadStrings(bufferedReader);
        this.keys = new String[loadStrings.length];
        this.values = new String[loadStrings.length];
        for (String split : loadStrings) {
            String[] split2 = PApplet.split(split, 9);
            if (split2.length == 2) {
                this.keys[this.count] = split2[0];
                this.values[this.count] = split2[1];
                this.count++;
            }
        }
    }

    public StringDict(String[] strArr, String[] strArr2) {
        this.indices = new HashMap<>();
        if (strArr.length != strArr2.length) {
            throw new IllegalArgumentException("key and value arrays must be the same length");
        }
        this.keys = strArr;
        this.values = strArr2;
        this.count = strArr.length;
        for (int i = 0; i < this.count; i++) {
            this.indices.put(strArr[i], Integer.valueOf(i));
        }
    }

    public void clear() {
        this.count = 0;
        this.indices = new HashMap<>();
    }

    public StringDict copy() {
        StringDict stringDict = new StringDict(this.count);
        System.arraycopy(this.keys, 0, stringDict.keys, 0, this.count);
        System.arraycopy(this.values, 0, stringDict.values, 0, this.count);
        for (int i = 0; i < this.count; i++) {
            stringDict.indices.put(this.keys[i], Integer.valueOf(i));
        }
        stringDict.count = this.count;
        return stringDict;
    }

    /* access modifiers changed from: protected */
    public void create(String str, String str2) {
        if (this.count == this.keys.length) {
            this.keys = PApplet.expand(this.keys);
            this.values = PApplet.expand(this.values);
        }
        this.indices.put(str, new Integer(this.count));
        this.keys[this.count] = str;
        this.values[this.count] = str2;
        this.count++;
    }

    /* access modifiers changed from: protected */
    public void crop() {
        if (this.count != this.keys.length) {
            this.keys = PApplet.subset(this.keys, 0, this.count);
            this.values = PApplet.subset(this.values, 0, this.count);
        }
    }

    public String get(String str) {
        int index = index(str);
        if (index == -1) {
            return null;
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
                        return this.index + 1 < StringDict.this.size();
                    }

                    public String next() {
                        StringDict stringDict = StringDict.this;
                        int i = this.index + 1;
                        this.index = i;
                        return stringDict.key(i);
                    }

                    public void remove() {
                        StringDict.this.removeIndex(this.index);
                    }
                };
            }
        };
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
        this.values[this.count] = null;
    }

    public void set(String str, String str2) {
        int index = index(str);
        if (index == -1) {
            create(str, str2);
        } else {
            this.values[index] = str2;
        }
    }

    public int size() {
        return this.count;
    }

    /* access modifiers changed from: protected */
    public void sortImpl(final boolean z, final boolean z2) {
        new Sort() {
            public float compare(int i, int i2) {
                int compareToIgnoreCase;
                if (z) {
                    compareToIgnoreCase = StringDict.this.keys[i].compareToIgnoreCase(StringDict.this.keys[i2]);
                    if (compareToIgnoreCase == 0) {
                        compareToIgnoreCase = StringDict.this.values[i].compareToIgnoreCase(StringDict.this.values[i2]);
                    }
                } else {
                    compareToIgnoreCase = StringDict.this.values[i].compareToIgnoreCase(StringDict.this.values[i2]);
                    if (compareToIgnoreCase == 0) {
                        compareToIgnoreCase = StringDict.this.keys[i].compareToIgnoreCase(StringDict.this.keys[i2]);
                    }
                }
                return z2 ? (float) (-compareToIgnoreCase) : (float) compareToIgnoreCase;
            }

            public int size() {
                return StringDict.this.count;
            }

            public void swap(int i, int i2) {
                StringDict.this.swap(i, i2);
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

    /* access modifiers changed from: protected */
    public void swap(int i, int i2) {
        String str = this.keys[i];
        String str2 = this.values[i];
        this.keys[i] = this.keys[i2];
        this.values[i] = this.values[i2];
        this.keys[i2] = str;
        this.values[i2] = str2;
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
            sb.append("\"" + this.keys[i] + "\": \"" + this.values[i] + "\"");
        }
        sb.append(" }");
        return sb.toString();
    }

    public String value(int i) {
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

    public Iterable<String> values() {
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    int index = -1;

                    public boolean hasNext() {
                        return this.index + 1 < StringDict.this.size();
                    }

                    public String next() {
                        StringDict stringDict = StringDict.this;
                        int i = this.index + 1;
                        this.index = i;
                        return stringDict.value(i);
                    }

                    public void remove() {
                        StringDict.this.removeIndex(this.index);
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

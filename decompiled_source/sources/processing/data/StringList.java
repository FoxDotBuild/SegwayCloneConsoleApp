package processing.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import processing.core.PApplet;

public class StringList implements Iterable<String> {
    int count;
    String[] data;

    public StringList() {
        this(10);
    }

    public StringList(int i) {
        this.data = new String[i];
    }

    public StringList(Iterable<String> iterable) {
        this(10);
        for (String append : iterable) {
            append(append);
        }
    }

    public StringList(String[] strArr) {
        this.count = strArr.length;
        this.data = new String[this.count];
        System.arraycopy(strArr, 0, this.data, 0, this.count);
    }

    private void crop() {
        if (this.count != this.data.length) {
            this.data = PApplet.subset(this.data, 0, this.count);
        }
    }

    private void sortImpl(final boolean z) {
        new Sort() {
            public float compare(int i, int i2) {
                float compareToIgnoreCase = (float) StringList.this.data[i].compareToIgnoreCase(StringList.this.data[i2]);
                return z ? -compareToIgnoreCase : compareToIgnoreCase;
            }

            public int size() {
                return StringList.this.count;
            }

            public void swap(int i, int i2) {
                String str = StringList.this.data[i];
                StringList.this.data[i] = StringList.this.data[i2];
                StringList.this.data[i2] = str;
            }
        }.run();
    }

    public void append(String str) {
        if (this.count == this.data.length) {
            this.data = PApplet.expand(this.data);
        }
        String[] strArr = this.data;
        int i = this.count;
        this.count = i + 1;
        strArr[i] = str;
    }

    public void append(StringList stringList) {
        for (String append : stringList.values()) {
            append(append);
        }
    }

    public void append(String[] strArr) {
        for (String append : strArr) {
            append(append);
        }
    }

    public String[] array() {
        return array(null);
    }

    public String[] array(String[] strArr) {
        if (strArr == null || strArr.length != this.count) {
            strArr = new String[this.count];
        }
        System.arraycopy(this.data, 0, strArr, 0, this.count);
        return strArr;
    }

    public void clear() {
        this.count = 0;
    }

    public StringList copy() {
        StringList stringList = new StringList(this.data);
        stringList.count = this.count;
        return stringList;
    }

    public String get(int i) {
        return this.data[i];
    }

    public IntDict getOrder() {
        IntDict intDict = new IntDict();
        for (int i = 0; i < this.count; i++) {
            intDict.set(this.data[i], i);
        }
        return intDict;
    }

    public StringList getSubset(int i) {
        return getSubset(i, this.count - i);
    }

    public StringList getSubset(int i, int i2) {
        StringList stringList = new StringList(i2);
        for (int i3 = 0; i3 < i2; i3++) {
            System.arraycopy(this.data, i, stringList.data, 0, i2);
        }
        return stringList;
    }

    public IntDict getTally() {
        IntDict intDict = new IntDict();
        for (int i = 0; i < this.count; i++) {
            intDict.increment(this.data[i]);
        }
        return intDict;
    }

    public String[] getUnique() {
        return getTally().keyArray();
    }

    public boolean hasValue(String str) {
        if (str == null) {
            for (int i = 0; i < this.count; i++) {
                if (this.data[i] == null) {
                    return true;
                }
            }
            return false;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            if (str.equals(this.data[i2])) {
                return true;
            }
        }
        return false;
    }

    public int index(String str) {
        int i = 0;
        if (str == null) {
            while (i < this.count) {
                if (this.data[i] == null) {
                    return i;
                }
                i++;
            }
        } else {
            while (i < this.count) {
                if (str.equals(this.data[i])) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public void insert(int i, StringList stringList) {
        insert(i, stringList.values());
    }

    public void insert(int i, String[] strArr) {
        if (i < 0) {
            throw new IllegalArgumentException("insert() index cannot be negative: it was " + i);
        } else if (i >= strArr.length) {
            throw new IllegalArgumentException("insert() index " + i + " is past the end of this list");
        } else {
            String[] strArr2 = new String[(this.count + strArr.length)];
            System.arraycopy(this.data, 0, strArr2, 0, Math.min(this.count, i));
            System.arraycopy(strArr, 0, strArr2, i, strArr.length);
            System.arraycopy(this.data, i, strArr2, strArr.length + i, this.count - i);
            this.count += strArr.length;
            this.data = strArr2;
        }
    }

    public Iterator<String> iterator() {
        return new Iterator<String>() {
            int index = -1;

            public boolean hasNext() {
                return this.index + 1 < StringList.this.count;
            }

            public String next() {
                String[] strArr = StringList.this.data;
                int i = this.index + 1;
                this.index = i;
                return strArr[i];
            }

            public void remove() {
                StringList.this.remove(this.index);
            }
        };
    }

    public void lower() {
        for (int i = 0; i < this.count; i++) {
            if (this.data[i] != null) {
                this.data[i] = this.data[i].toLowerCase();
            }
        }
    }

    public void remove(int i) {
        while (i < this.count) {
            this.data[i] = this.data[i + 1];
            i++;
        }
        this.count--;
    }

    public boolean removeValue(String str) {
        if (str == null) {
            for (int i = 0; i < this.count; i++) {
                if (this.data[i] == null) {
                    remove(i);
                    return true;
                }
            }
            return false;
        }
        int index = index(str);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    public boolean removeValues(String str) {
        int i;
        boolean z = false;
        if (str == null) {
            i = 0;
            for (int i2 = 0; i2 < this.count; i2++) {
                if (this.data[i2] != null) {
                    int i3 = i + 1;
                    this.data[i] = this.data[i2];
                    i = i3;
                }
            }
        } else {
            i = 0;
            for (int i4 = 0; i4 < this.count; i4++) {
                if (!str.equals(this.data[i4])) {
                    int i5 = i + 1;
                    this.data[i] = this.data[i4];
                    i = i5;
                }
            }
        }
        if (this.count == i) {
            z = true;
        }
        this.count = i;
        return z;
    }

    public boolean replaceValue(String str, String str2) {
        if (str == null) {
            for (int i = 0; i < this.count; i++) {
                if (this.data[i] == null) {
                    this.data[i] = str2;
                    return true;
                }
            }
            return false;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            if (str.equals(this.data[i2])) {
                this.data[i2] = str2;
                return true;
            }
        }
        return false;
    }

    public boolean replaceValues(String str, String str2) {
        boolean z;
        int i = 0;
        if (str == null) {
            z = false;
            while (i < this.count) {
                if (this.data[i] == null) {
                    this.data[i] = str2;
                    z = true;
                }
                i++;
            }
        } else {
            z = false;
            while (i < this.count) {
                if (str.equals(this.data[i])) {
                    this.data[i] = str2;
                    z = true;
                }
                i++;
            }
        }
        return z;
    }

    public void resize(int i) {
        if (i > this.data.length) {
            String[] strArr = new String[i];
            System.arraycopy(this.data, 0, strArr, 0, this.count);
            this.data = strArr;
        } else if (i > this.count) {
            Arrays.fill(this.data, this.count, i, Integer.valueOf(0));
        }
        this.count = i;
    }

    public void reverse() {
        int i = this.count - 1;
        for (int i2 = 0; i2 < this.count / 2; i2++) {
            String str = this.data[i2];
            this.data[i2] = this.data[i];
            this.data[i] = str;
            i--;
        }
    }

    public void set(int i, String str) {
        if (i >= this.count) {
            this.data = PApplet.expand(this.data, i + 1);
            for (int i2 = this.count; i2 < i; i2++) {
                this.data[i2] = null;
            }
            this.count = i + 1;
        }
        this.data[i] = str;
    }

    public void shuffle() {
        Random random = new Random();
        int i = this.count;
        while (i > 1) {
            int nextInt = random.nextInt(i);
            i--;
            String str = this.data[i];
            this.data[i] = this.data[nextInt];
            this.data[nextInt] = str;
        }
    }

    public void shuffle(PApplet pApplet) {
        int i = this.count;
        while (i > 1) {
            int random = (int) pApplet.random((float) i);
            i--;
            String str = this.data[i];
            this.data[i] = this.data[random];
            this.data[random] = str;
        }
    }

    public int size() {
        return this.count;
    }

    public void sort() {
        sortImpl(false);
    }

    public void sortReverse() {
        sortImpl(true);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName() + " size=" + size() + " [ ");
        for (int i = 0; i < size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(i + ": \"" + this.data[i] + "\"");
        }
        sb.append(" ]");
        return sb.toString();
    }

    public void upper() {
        for (int i = 0; i < this.count; i++) {
            if (this.data[i] != null) {
                this.data[i] = this.data[i].toUpperCase();
            }
        }
    }

    public String[] values() {
        crop();
        return this.data;
    }
}

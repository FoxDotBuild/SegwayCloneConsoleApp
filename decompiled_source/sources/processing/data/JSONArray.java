package processing.data;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import processing.core.PApplet;

public class JSONArray {
    private final ArrayList<Object> myArrayList;

    public JSONArray() {
        this.myArrayList = new ArrayList<>();
    }

    public JSONArray(Reader reader) {
        this(new JSONTokener(reader));
    }

    protected JSONArray(Object obj) {
        this();
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                append(JSONObject.wrap(Array.get(obj, i)));
            }
            return;
        }
        throw new RuntimeException("JSONArray initial value should be a string or collection or array.");
    }

    public JSONArray(FloatList floatList) {
        this.myArrayList = new ArrayList<>();
        for (float f : floatList.values()) {
            this.myArrayList.add(new Float(f));
        }
    }

    public JSONArray(IntList intList) {
        this.myArrayList = new ArrayList<>();
        for (int num : intList.values()) {
            this.myArrayList.add(new Integer(num));
        }
    }

    protected JSONArray(JSONTokener jSONTokener) {
        this();
        if (jSONTokener.nextClean() != '[') {
            throw new RuntimeException("A JSONArray text must start with '['");
        } else if (jSONTokener.nextClean() != ']') {
            jSONTokener.back();
            while (true) {
                if (jSONTokener.nextClean() == ',') {
                    jSONTokener.back();
                    this.myArrayList.add(JSONObject.NULL);
                } else {
                    jSONTokener.back();
                    this.myArrayList.add(jSONTokener.nextValue());
                }
                switch (jSONTokener.nextClean()) {
                    case ',':
                    case ';':
                        if (jSONTokener.nextClean() != ']') {
                            jSONTokener.back();
                        } else {
                            return;
                        }
                    case ']':
                        return;
                    default:
                        throw new RuntimeException("Expected a ',' or ']'");
                }
            }
        }
    }

    public JSONArray(StringList stringList) {
        this.myArrayList = new ArrayList<>();
        for (String add : stringList.values()) {
            this.myArrayList.add(add);
        }
    }

    private Object get(int i) {
        Object opt = opt(i);
        if (opt != null) {
            return opt;
        }
        throw new RuntimeException("JSONArray[" + i + "] not found.");
    }

    private Object opt(int i) {
        if (i < 0 || i >= size()) {
            return null;
        }
        return this.myArrayList.get(i);
    }

    public static JSONArray parse(String str) {
        try {
            return new JSONArray(new JSONTokener(str));
        } catch (Exception e) {
            return null;
        }
    }

    private JSONArray set(int i, Object obj) {
        JSONObject.testValidity(obj);
        if (i < 0) {
            throw new RuntimeException("JSONArray[" + i + "] not found.");
        }
        if (i < size()) {
            this.myArrayList.set(i, obj);
        } else {
            while (i != size()) {
                append(JSONObject.NULL);
            }
            append(obj);
        }
        return this;
    }

    public JSONArray append(double d) {
        Double d2 = new Double(d);
        JSONObject.testValidity(d2);
        append((Object) d2);
        return this;
    }

    public JSONArray append(float f) {
        return append((double) f);
    }

    public JSONArray append(int i) {
        append((Object) new Integer(i));
        return this;
    }

    public JSONArray append(long j) {
        append((Object) new Long(j));
        return this;
    }

    /* access modifiers changed from: protected */
    public JSONArray append(Object obj) {
        this.myArrayList.add(obj);
        return this;
    }

    public JSONArray append(String str) {
        append((Object) str);
        return this;
    }

    public JSONArray append(JSONArray jSONArray) {
        this.myArrayList.add(jSONArray);
        return this;
    }

    public JSONArray append(JSONObject jSONObject) {
        this.myArrayList.add(jSONObject);
        return this;
    }

    public JSONArray append(boolean z) {
        append((Object) z ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public String format(int i) {
        String obj;
        StringWriter stringWriter = new StringWriter();
        synchronized (stringWriter.getBuffer()) {
            obj = write(stringWriter, i, 0).toString();
        }
        return obj;
    }

    public boolean getBoolean(int i) {
        Object obj = get(i);
        if (obj.equals(Boolean.FALSE) || ((obj instanceof String) && ((String) obj).equalsIgnoreCase("false"))) {
            return false;
        }
        if (obj.equals(Boolean.TRUE) || ((obj instanceof String) && ((String) obj).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new RuntimeException("JSONArray[" + i + "] is not a boolean.");
    }

    public boolean[] getBooleanArray() {
        boolean[] zArr = new boolean[size()];
        for (int i = 0; i < size(); i++) {
            zArr[i] = getBoolean(i);
        }
        return zArr;
    }

    public double getDouble(int i) {
        Object obj = get(i);
        try {
            return obj instanceof Number ? ((Number) obj).doubleValue() : Double.parseDouble((String) obj);
        } catch (Exception e) {
            throw new RuntimeException("JSONArray[" + i + "] is not a number.");
        }
    }

    public double[] getDoubleArray() {
        double[] dArr = new double[size()];
        for (int i = 0; i < size(); i++) {
            dArr[i] = getDouble(i);
        }
        return dArr;
    }

    public float getFloat(int i) {
        return (float) getDouble(i);
    }

    public float[] getFloatArray() {
        float[] fArr = new float[size()];
        for (int i = 0; i < size(); i++) {
            fArr[i] = getFloat(i);
        }
        return fArr;
    }

    public int getInt(int i) {
        Object obj = get(i);
        try {
            return obj instanceof Number ? ((Number) obj).intValue() : Integer.parseInt((String) obj);
        } catch (Exception e) {
            throw new RuntimeException("JSONArray[" + i + "] is not a number.");
        }
    }

    public int[] getIntArray() {
        int[] iArr = new int[size()];
        for (int i = 0; i < size(); i++) {
            iArr[i] = getInt(i);
        }
        return iArr;
    }

    public JSONArray getJSONArray(int i) {
        Object obj = get(i);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        throw new RuntimeException("JSONArray[" + i + "] is not a JSONArray.");
    }

    public JSONObject getJSONObject(int i) {
        Object obj = get(i);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw new RuntimeException("JSONArray[" + i + "] is not a JSONObject.");
    }

    public long getLong(int i) {
        Object obj = get(i);
        try {
            return obj instanceof Number ? ((Number) obj).longValue() : Long.parseLong((String) obj);
        } catch (Exception e) {
            throw new RuntimeException("JSONArray[" + i + "] is not a number.");
        }
    }

    public long[] getLongArray() {
        long[] jArr = new long[size()];
        for (int i = 0; i < size(); i++) {
            jArr[i] = getLong(i);
        }
        return jArr;
    }

    public String getString(int i) {
        Object obj = get(i);
        if (obj instanceof String) {
            return (String) obj;
        }
        throw new RuntimeException("JSONArray[" + i + "] not a string.");
    }

    public String[] getStringArray() {
        String[] strArr = new String[size()];
        for (int i = 0; i < size(); i++) {
            strArr[i] = getString(i);
        }
        return strArr;
    }

    /* access modifiers changed from: protected */
    public boolean isNull(int i) {
        return JSONObject.NULL.equals(opt(i));
    }

    public String join(String str) {
        int size = size();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuffer.append(str);
            }
            stringBuffer.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return stringBuffer.toString();
    }

    public Object remove(int i) {
        Object opt = opt(i);
        this.myArrayList.remove(i);
        return opt;
    }

    public boolean save(File file, String str) {
        return save(PApplet.createWriter(file));
    }

    /* access modifiers changed from: protected */
    public boolean save(OutputStream outputStream) {
        return save(PApplet.createWriter(outputStream));
    }

    public boolean save(PrintWriter printWriter) {
        printWriter.print(format(2));
        printWriter.flush();
        return true;
    }

    public JSONArray setBoolean(int i, boolean z) {
        return set(i, z ? Boolean.TRUE : Boolean.FALSE);
    }

    public JSONArray setDouble(int i, double d) {
        return set(i, new Double(d));
    }

    public JSONArray setFloat(int i, float f) {
        return setDouble(i, (double) f);
    }

    public JSONArray setInt(int i, int i2) {
        set(i, new Integer(i2));
        return this;
    }

    public JSONArray setJSONArray(int i, JSONArray jSONArray) {
        set(i, jSONArray);
        return this;
    }

    public JSONArray setJSONObject(int i, JSONObject jSONObject) {
        set(i, jSONObject);
        return this;
    }

    public JSONArray setLong(int i, long j) {
        return set(i, new Long(j));
    }

    public JSONArray setString(int i, String str) {
        set(i, str);
        return this;
    }

    public int size() {
        return this.myArrayList.size();
    }

    public String toString() {
        try {
            return format(2);
        } catch (Exception e) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public Writer write(Writer writer) {
        return write(writer, -1, 0);
    }

    /* access modifiers changed from: protected */
    public Writer write(Writer writer, int i, int i2) {
        int i3 = 0;
        try {
            int size = size();
            writer.write(91);
            int i4 = i == -1 ? 0 : i;
            if (size == 1) {
                JSONObject.writeValue(writer, this.myArrayList.get(0), i4, i2);
            } else if (size != 0) {
                int i5 = i2 + i4;
                boolean z = false;
                while (i3 < size) {
                    if (z) {
                        writer.write(44);
                    }
                    if (i != -1) {
                        writer.write(10);
                    }
                    JSONObject.indent(writer, i5);
                    JSONObject.writeValue(writer, this.myArrayList.get(i3), i4, i5);
                    i3++;
                    z = true;
                }
                if (i != -1) {
                    writer.write(10);
                }
                JSONObject.indent(writer, i2);
            }
            writer.write(93);
            return writer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

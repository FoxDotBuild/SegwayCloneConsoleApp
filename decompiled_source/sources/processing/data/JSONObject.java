package processing.data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class JSONObject {
    public static final Object NULL = new Null();
    private static HashMap<String, Object> keyPool = new HashMap<>(100);
    private static final int keyPoolSize = 100;
    private final HashMap<String, Object> map;

    private static final class Null {
        private Null() {
        }

        /* access modifiers changed from: protected */
        public final Object clone() {
            return this;
        }

        public boolean equals(Object obj) {
            return obj == null || obj == this;
        }

        public int hashCode() {
            return super.hashCode();
        }

        public String toString() {
            return "null";
        }
    }

    public JSONObject() {
        this.map = new HashMap<>();
    }

    public JSONObject(Reader reader) {
        this(new JSONTokener(reader));
    }

    protected JSONObject(Object obj) {
        this();
        populateMap(obj);
    }

    protected JSONObject(HashMap<String, Object> hashMap) {
        this.map = new HashMap<>();
        if (hashMap != null) {
            for (Entry entry : hashMap.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    hashMap.put((String) entry.getKey(), wrap(value));
                }
            }
        }
    }

    public JSONObject(FloatDict floatDict) {
        this.map = new HashMap<>();
        for (int i = 0; i < floatDict.size(); i++) {
            setFloat(floatDict.key(i), floatDict.value(i));
        }
    }

    public JSONObject(IntDict intDict) {
        this.map = new HashMap<>();
        for (int i = 0; i < intDict.size(); i++) {
            setInt(intDict.key(i), intDict.value(i));
        }
    }

    protected JSONObject(JSONTokener jSONTokener) {
        this();
        if (jSONTokener.nextClean() != '{') {
            throw new RuntimeException("A JSONObject text must begin with '{'");
        }
        while (true) {
            switch (jSONTokener.nextClean()) {
                case 0:
                    throw new RuntimeException("A JSONObject text must end with '}'");
                case '}':
                    return;
                default:
                    jSONTokener.back();
                    String obj = jSONTokener.nextValue().toString();
                    char nextClean = jSONTokener.nextClean();
                    if (nextClean == '=') {
                        if (jSONTokener.next() != '>') {
                            jSONTokener.back();
                        }
                    } else if (nextClean != ':') {
                        throw new RuntimeException("Expected a ':' after a key");
                    }
                    putOnce(obj, jSONTokener.nextValue());
                    switch (jSONTokener.nextClean()) {
                        case ',':
                        case ';':
                            if (jSONTokener.nextClean() != '}') {
                                jSONTokener.back();
                            } else {
                                return;
                            }
                        case '}':
                            return;
                        default:
                            throw new RuntimeException("Expected a ',' or '}'");
                    }
            }
        }
    }

    public JSONObject(StringDict stringDict) {
        this.map = new HashMap<>();
        for (int i = 0; i < stringDict.size(); i++) {
            setString(stringDict.key(i), stringDict.value(i));
        }
    }

    protected static String doubleToString(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return "null";
        }
        String d2 = Double.toString(d);
        if (d2.indexOf(46) <= 0 || d2.indexOf(PConstants.TOP) >= 0 || d2.indexOf(69) >= 0) {
            return d2;
        }
        while (d2.endsWith("0")) {
            d2 = d2.substring(0, d2.length() - 1);
        }
        return d2.endsWith(".") ? d2.substring(0, d2.length() - 1) : d2;
    }

    private Object get(String str) {
        if (str == null) {
            throw new RuntimeException("Null key.");
        }
        Object opt = opt(str);
        if (opt != null) {
            return opt;
        }
        throw new RuntimeException("JSONObject[" + quote(str) + "] not found.");
    }

    static final void indent(Writer writer, int i) throws IOException {
        for (int i2 = 0; i2 < i; i2++) {
            writer.write(32);
        }
    }

    private static String numberToString(Number number) {
        if (number == null) {
            throw new RuntimeException("Null pointer");
        }
        testValidity(number);
        String obj = number.toString();
        if (obj.indexOf(46) <= 0 || obj.indexOf(PConstants.TOP) >= 0 || obj.indexOf(69) >= 0) {
            return obj;
        }
        while (obj.endsWith("0")) {
            obj = obj.substring(0, obj.length() - 1);
        }
        return obj.endsWith(".") ? obj.substring(0, obj.length() - 1) : obj;
    }

    private Object opt(String str) {
        if (str == null) {
            return null;
        }
        return this.map.get(str);
    }

    public static JSONObject parse(String str) {
        return new JSONObject(new JSONTokener(str));
    }

    private void populateMap(Object obj) {
        Class cls = obj.getClass();
        Method[] methods = cls.getClassLoader() != null ? cls.getMethods() : cls.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            try {
                Method method = methods[i];
                if (Modifier.isPublic(method.getModifiers())) {
                    String name = method.getName();
                    String str = "";
                    if (name.startsWith("get")) {
                        str = ("getClass".equals(name) || "getDeclaringClass".equals(name)) ? "" : name.substring(3);
                    } else if (name.startsWith("is")) {
                        str = name.substring(2);
                    }
                    if (str.length() > 0 && Character.isUpperCase(str.charAt(0)) && method.getParameterTypes().length == 0) {
                        String str2 = str.length() == 1 ? str.toLowerCase() : !Character.isUpperCase(str.charAt(1)) ? str.substring(0, 1).toLowerCase() + str.substring(1) : str;
                        Object invoke = method.invoke(obj, null);
                        if (invoke != null) {
                            this.map.put(str2, wrap(invoke));
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private JSONObject put(String str, Object obj) {
        if (str == null) {
            throw new RuntimeException("Null key.");
        }
        if (obj != null) {
            testValidity(obj);
            String str2 = (String) keyPool.get(str);
            if (str2 == null) {
                if (keyPool.size() >= 100) {
                    keyPool = new HashMap<>(100);
                }
                keyPool.put(str, str);
            } else {
                str = str2;
            }
            this.map.put(str, obj);
        } else {
            remove(str);
        }
        return this;
    }

    private JSONObject putOnce(String str, Object obj) {
        if (!(str == null || obj == null)) {
            if (opt(str) != null) {
                throw new RuntimeException("Duplicate key \"" + str + "\"");
            }
            put(str, obj);
        }
        return this;
    }

    protected static Writer quote(String str, Writer writer) throws IOException {
        if (str == null || str.length() == 0) {
            writer.write("\"\"");
        } else {
            int length = str.length();
            writer.write(34);
            int i = 0;
            char c = 0;
            while (i < length) {
                char charAt = str.charAt(i);
                switch (charAt) {
                    case 8:
                        writer.write("\\b");
                        break;
                    case 9:
                        writer.write("\\t");
                        break;
                    case 10:
                        writer.write("\\n");
                        break;
                    case 12:
                        writer.write("\\f");
                        break;
                    case 13:
                        writer.write("\\r");
                        break;
                    case PGraphics.f19EB /*34*/:
                    case '\\':
                        writer.write(92);
                        writer.write(charAt);
                        break;
                    case '/':
                        if (c == '<') {
                            writer.write(92);
                        }
                        writer.write(charAt);
                        break;
                    default:
                        if (charAt >= ' ' && ((charAt < 128 || charAt >= 160) && (charAt < 8192 || charAt >= 8448))) {
                            writer.write(charAt);
                            break;
                        } else {
                            writer.write("\\u");
                            String hexString = Integer.toHexString(charAt);
                            writer.write("0000", 0, 4 - hexString.length());
                            writer.write(hexString);
                            break;
                        }
                }
                i++;
                c = charAt;
            }
            writer.write(34);
        }
        return writer;
    }

    protected static String quote(String str) {
        String str2;
        StringWriter stringWriter = new StringWriter();
        synchronized (stringWriter.getBuffer()) {
            try {
                str2 = quote(str, stringWriter).toString();
            } catch (IOException e) {
                str2 = "";
            }
        }
        return str2;
    }

    protected static Object stringToValue(String str) {
        if (str.equals("")) {
            return str;
        }
        if (str.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (str.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (str.equalsIgnoreCase("null")) {
            return NULL;
        }
        char charAt = str.charAt(0);
        if ((charAt < '0' || charAt > '9') && charAt != '.' && charAt != '-' && charAt != '+') {
            return str;
        }
        try {
            if (str.indexOf(46) > -1 || str.indexOf(PConstants.TOP) > -1 || str.indexOf(69) > -1) {
                Double valueOf = Double.valueOf(str);
                return (valueOf.isInfinite() || valueOf.isNaN()) ? str : valueOf;
            }
            Long l = new Long(str);
            return l.longValue() == ((long) l.intValue()) ? new Integer(l.intValue()) : l;
        } catch (Exception e) {
            return str;
        }
    }

    protected static void testValidity(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Double) {
            if (((Double) obj).isInfinite() || ((Double) obj).isNaN()) {
                throw new RuntimeException("JSON does not allow non-finite numbers.");
            }
        } else if (!(obj instanceof Float)) {
        } else {
            if (((Float) obj).isInfinite() || ((Float) obj).isNaN()) {
                throw new RuntimeException("JSON does not allow non-finite numbers.");
            }
        }
    }

    protected static String valueToString(Object obj) {
        return (obj == null || obj.equals(null)) ? "null" : obj instanceof Number ? numberToString((Number) obj) : ((obj instanceof Boolean) || (obj instanceof JSONObject) || (obj instanceof JSONArray)) ? obj.toString() : obj instanceof Map ? new JSONObject((Object) (Map) obj).toString() : obj instanceof Collection ? new JSONArray((Object) (Collection) obj).toString() : obj.getClass().isArray() ? new JSONArray(obj).toString() : quote(obj.toString());
    }

    protected static Object wrap(Object obj) {
        if (obj == null) {
            try {
                return NULL;
            } catch (Exception e) {
                return null;
            }
        } else if ((obj instanceof JSONObject) || (obj instanceof JSONArray) || NULL.equals(obj) || (obj instanceof Byte) || (obj instanceof Character) || (obj instanceof Short) || (obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Boolean) || (obj instanceof Float) || (obj instanceof Double) || (obj instanceof String)) {
            return obj;
        } else {
            if (obj instanceof Collection) {
                return new JSONArray((Object) (Collection) obj);
            }
            if (obj.getClass().isArray()) {
                return new JSONArray(obj);
            }
            if (obj instanceof Map) {
                return new JSONObject((Object) (Map) obj);
            }
            Package packageR = obj.getClass().getPackage();
            String str = packageR != null ? packageR.getName() : "";
            return (str.startsWith("java.") || str.startsWith("javax.") || obj.getClass().getClassLoader() == null) ? obj.toString() : new JSONObject(obj);
        }
    }

    static final Writer writeValue(Writer writer, Object obj, int i, int i2) throws IOException {
        if (obj == null || obj.equals(null)) {
            writer.write("null");
        } else if (obj instanceof JSONObject) {
            ((JSONObject) obj).write(writer, i, i2);
        } else if (obj instanceof JSONArray) {
            ((JSONArray) obj).write(writer, i, i2);
        } else if (obj instanceof Map) {
            new JSONObject((Object) (Map) obj).write(writer, i, i2);
        } else if (obj instanceof Collection) {
            new JSONArray((Object) (Collection) obj).write(writer, i, i2);
        } else if (obj.getClass().isArray()) {
            new JSONArray(obj).write(writer, i, i2);
        } else if (obj instanceof Number) {
            writer.write(numberToString((Number) obj));
        } else if (obj instanceof Boolean) {
            writer.write(obj.toString());
        } else {
            quote(obj.toString(), writer);
        }
        return writer;
    }

    public String format(int i) {
        String obj;
        StringWriter stringWriter = new StringWriter();
        synchronized (stringWriter.getBuffer()) {
            obj = write(stringWriter, i, 0).toString();
        }
        return obj;
    }

    public boolean getBoolean(String str) {
        Object obj = get(str);
        if (obj.equals(Boolean.FALSE) || ((obj instanceof String) && ((String) obj).equalsIgnoreCase("false"))) {
            return false;
        }
        if (obj.equals(Boolean.TRUE) || ((obj instanceof String) && ((String) obj).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new RuntimeException("JSONObject[" + quote(str) + "] is not a Boolean.");
    }

    public double getDouble(String str) {
        Object obj = get(str);
        try {
            return obj instanceof Number ? ((Number) obj).doubleValue() : Double.parseDouble((String) obj);
        } catch (Exception e) {
            throw new RuntimeException("JSONObject[" + quote(str) + "] is not a number.");
        }
    }

    public float getFloat(String str) {
        return (float) getDouble(str);
    }

    public int getInt(String str) {
        Object obj = get(str);
        try {
            return obj instanceof Number ? ((Number) obj).intValue() : Integer.parseInt((String) obj);
        } catch (Exception e) {
            throw new RuntimeException("JSONObject[" + quote(str) + "] is not an int.");
        }
    }

    public JSONArray getJSONArray(String str) {
        Object obj = get(str);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        throw new RuntimeException("JSONObject[" + quote(str) + "] is not a JSONArray.");
    }

    public JSONObject getJSONObject(String str) {
        Object obj = get(str);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw new RuntimeException("JSONObject[" + quote(str) + "] is not a JSONObject.");
    }

    public long getLong(String str) {
        Object obj = get(str);
        try {
            return obj instanceof Number ? ((Number) obj).longValue() : Long.parseLong((String) obj);
        } catch (Exception e) {
            throw new RuntimeException("JSONObject[" + quote(str) + "] is not a long.", e);
        }
    }

    public String getString(String str) {
        Object obj = get(str);
        if (obj instanceof String) {
            return (String) obj;
        }
        throw new RuntimeException("JSONObject[" + quote(str) + "] not a string.");
    }

    public boolean hasKey(String str) {
        return this.map.containsKey(str);
    }

    /* access modifiers changed from: protected */
    public boolean isNull(String str) {
        return NULL.equals(opt(str));
    }

    public Iterator keyIterator() {
        return this.map.keySet().iterator();
    }

    public Set keys() {
        return this.map.keySet();
    }

    public Object remove(String str) {
        return this.map.remove(str);
    }

    public boolean save(File file, String str) {
        return write(PApplet.createWriter(file));
    }

    public JSONObject setBoolean(String str, boolean z) {
        put(str, z ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONObject setDouble(String str, double d) {
        put(str, new Double(d));
        return this;
    }

    public JSONObject setFloat(String str, float f) {
        put(str, new Double((double) f));
        return this;
    }

    public JSONObject setInt(String str, int i) {
        put(str, new Integer(i));
        return this;
    }

    public JSONObject setJSONArray(String str, JSONArray jSONArray) {
        return put(str, jSONArray);
    }

    public JSONObject setJSONObject(String str, JSONObject jSONObject) {
        return put(str, jSONObject);
    }

    public JSONObject setLong(String str, long j) {
        put(str, new Long(j));
        return this;
    }

    public JSONObject setString(String str, String str2) {
        return put(str, str2);
    }

    public int size() {
        return this.map.size();
    }

    public String toString() {
        try {
            return format(2);
        } catch (Exception e) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public Writer write(Writer writer, int i, int i2) {
        boolean z = false;
        try {
            int size = size();
            Iterator keyIterator = keyIterator();
            writer.write(123);
            int i3 = i == -1 ? 0 : i;
            if (size == 1) {
                Object next = keyIterator.next();
                writer.write(quote(next.toString()));
                writer.write(58);
                if (i3 > 0) {
                    writer.write(32);
                }
                writeValue(writer, this.map.get(next), i3, i2);
            } else if (size != 0) {
                int i4 = i2 + i3;
                while (keyIterator.hasNext()) {
                    Object next2 = keyIterator.next();
                    if (z) {
                        writer.write(44);
                    }
                    if (i != -1) {
                        writer.write(10);
                    }
                    indent(writer, i4);
                    writer.write(quote(next2.toString()));
                    writer.write(58);
                    if (i3 > 0) {
                        writer.write(32);
                    }
                    writeValue(writer, this.map.get(next2), i3, i4);
                    z = true;
                }
                if (i != -1) {
                    writer.write(10);
                }
                indent(writer, i2);
            }
            writer.write(125);
            return writer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean write(PrintWriter printWriter) {
        printWriter.print(format(2));
        printWriter.flush();
        return true;
    }
}

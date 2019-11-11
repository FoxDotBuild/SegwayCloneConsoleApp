package processing.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.opengl.PGL;

public class Table {
    public static final int CATEGORY = 5;
    public static final int DOUBLE = 4;
    public static final int FLOAT = 3;
    public static final int INT = 1;
    public static final int LONG = 2;
    public static final int STRING = 0;
    static final String[] loadExtensions = {"csv", "tsv", "ods", "bin"};
    static final String[] saveExtensions = {"csv", "tsv", "html", "bin"};
    HashMapBlows[] columnCategories;
    HashMap<String, Integer> columnIndices;
    String[] columnTitles;
    int[] columnTypes;
    protected Object[] columns;
    protected int missingCategory;
    protected double missingDouble;
    protected float missingFloat;
    protected int missingInt;
    protected long missingLong;
    protected String missingString;
    protected int rowCount;
    protected RowIterator rowIterator;

    class HashMapBlows {
        HashMap<String, Integer> dataToIndex = new HashMap<>();
        ArrayList<String> indexToData = new ArrayList<>();

        HashMapBlows() {
        }

        HashMapBlows(DataInputStream dataInputStream) throws IOException {
            read(dataInputStream);
        }

        /* access modifiers changed from: private */
        public void writeln(PrintWriter printWriter) throws IOException {
            Iterator it = this.indexToData.iterator();
            while (it.hasNext()) {
                printWriter.println((String) it.next());
            }
            printWriter.flush();
            printWriter.close();
        }

        /* access modifiers changed from: 0000 */
        public int index(String str) {
            Integer num = (Integer) this.dataToIndex.get(str);
            if (num != null) {
                return num.intValue();
            }
            int size = this.dataToIndex.size();
            this.dataToIndex.put(str, Integer.valueOf(size));
            this.indexToData.add(str);
            return size;
        }

        /* access modifiers changed from: 0000 */
        public String key(int i) {
            return (String) this.indexToData.get(i);
        }

        /* access modifiers changed from: 0000 */
        public void read(DataInputStream dataInputStream) throws IOException {
            int readInt = dataInputStream.readInt();
            this.dataToIndex = new HashMap<>(readInt);
            for (int i = 0; i < readInt; i++) {
                String readUTF = dataInputStream.readUTF();
                this.dataToIndex.put(readUTF, Integer.valueOf(i));
                this.indexToData.add(readUTF);
            }
        }

        /* access modifiers changed from: 0000 */
        public int size() {
            return this.dataToIndex.size();
        }

        /* access modifiers changed from: 0000 */
        public void write(DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeInt(size());
            Iterator it = this.indexToData.iterator();
            while (it.hasNext()) {
                dataOutputStream.writeUTF((String) it.next());
            }
        }
    }

    static class RowIndexIterator implements Iterator<TableRow> {
        int index = -1;
        int[] indices;

        /* renamed from: rp */
        RowPointer f71rp;
        Table table;

        public RowIndexIterator(Table table2, int[] iArr) {
            this.table = table2;
            this.indices = iArr;
            this.f71rp = new RowPointer(table2, -1);
        }

        public boolean hasNext() {
            return this.index + 1 < this.indices.length;
        }

        public TableRow next() {
            RowPointer rowPointer = this.f71rp;
            int[] iArr = this.indices;
            int i = this.index + 1;
            this.index = i;
            rowPointer.setRow(iArr[i]);
            return this.f71rp;
        }

        public void remove() {
            this.table.removeRow(this.indices[this.index]);
        }

        public void reset() {
            this.index = -1;
        }
    }

    static class RowIterator implements Iterator<TableRow> {
        int row = -1;

        /* renamed from: rp */
        RowPointer f72rp;
        Table table;

        public RowIterator(Table table2) {
            this.table = table2;
            this.f72rp = new RowPointer(table2, this.row);
        }

        public boolean hasNext() {
            return this.row + 1 < this.table.getRowCount();
        }

        public TableRow next() {
            RowPointer rowPointer = this.f72rp;
            int i = this.row + 1;
            this.row = i;
            rowPointer.setRow(i);
            return this.f72rp;
        }

        public void remove() {
            this.table.removeRow(this.row);
        }

        public void reset() {
            this.row = -1;
        }
    }

    static class RowPointer implements TableRow {
        int row;
        Table table;

        public RowPointer(Table table2, int i) {
            this.table = table2;
            this.row = i;
        }

        public int getColumnCount() {
            return this.table.getColumnCount();
        }

        public int getColumnType(int i) {
            return this.table.getColumnType(i);
        }

        public int getColumnType(String str) {
            return this.table.getColumnType(str);
        }

        public double getDouble(int i) {
            return this.table.getDouble(this.row, i);
        }

        public double getDouble(String str) {
            return this.table.getDouble(this.row, str);
        }

        public float getFloat(int i) {
            return this.table.getFloat(this.row, i);
        }

        public float getFloat(String str) {
            return this.table.getFloat(this.row, str);
        }

        public int getInt(int i) {
            return this.table.getInt(this.row, i);
        }

        public int getInt(String str) {
            return this.table.getInt(this.row, str);
        }

        public long getLong(int i) {
            return this.table.getLong(this.row, i);
        }

        public long getLong(String str) {
            return this.table.getLong(this.row, str);
        }

        public String getString(int i) {
            return this.table.getString(this.row, i);
        }

        public String getString(String str) {
            return this.table.getString(this.row, str);
        }

        public void setDouble(int i, double d) {
            this.table.setDouble(this.row, i, d);
        }

        public void setDouble(String str, double d) {
            this.table.setDouble(this.row, str, d);
        }

        public void setFloat(int i, float f) {
            this.table.setFloat(this.row, i, f);
        }

        public void setFloat(String str, float f) {
            this.table.setFloat(this.row, str, f);
        }

        public void setInt(int i, int i2) {
            this.table.setInt(this.row, i, i2);
        }

        public void setInt(String str, int i) {
            this.table.setInt(this.row, str, i);
        }

        public void setLong(int i, long j) {
            this.table.setLong(this.row, i, j);
        }

        public void setLong(String str, long j) {
            this.table.setLong(this.row, str, j);
        }

        public void setRow(int i) {
            this.row = i;
        }

        public void setString(int i, String str) {
            this.table.setString(this.row, i, str);
        }

        public void setString(String str, String str2) {
            this.table.setString(this.row, str, str2);
        }
    }

    public Table() {
        this.missingString = null;
        this.missingInt = 0;
        this.missingLong = 0;
        this.missingFloat = Float.NaN;
        this.missingDouble = Double.NaN;
        this.missingCategory = -1;
        init();
    }

    public Table(File file) throws IOException {
        this(file, (String) null);
    }

    public Table(File file, String str) throws IOException {
        this.missingString = null;
        this.missingInt = 0;
        this.missingLong = 0;
        this.missingFloat = Float.NaN;
        this.missingDouble = Double.NaN;
        this.missingCategory = -1;
        parse(PApplet.createInput(file), extensionOptions(true, file.getName(), str));
    }

    public Table(InputStream inputStream) throws IOException {
        this(inputStream, (String) null);
    }

    public Table(InputStream inputStream, String str) throws IOException {
        this.missingString = null;
        this.missingInt = 0;
        this.missingLong = 0;
        this.missingFloat = Float.NaN;
        this.missingDouble = Double.NaN;
        this.missingCategory = -1;
        parse(inputStream, str);
    }

    public Table(ResultSet resultSet) {
        this.missingString = null;
        this.missingInt = 0;
        this.missingLong = 0;
        this.missingFloat = Float.NaN;
        this.missingDouble = Double.NaN;
        this.missingCategory = -1;
        init();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            setColumnCount(columnCount);
            for (int i = 0; i < columnCount; i++) {
                setColumnTitle(i, metaData.getColumnName(i + 1));
                switch (metaData.getColumnType(i + 1)) {
                    case PConstants.ENABLE_OPTIMIZED_STROKE /*-6*/:
                    case 4:
                    case 5:
                        setColumnType(i, 1);
                        break;
                    case PConstants.ENABLE_DEPTH_MASK /*-5*/:
                        setColumnType(i, 2);
                        break;
                    case 3:
                    case 7:
                    case 8:
                        setColumnType(i, 4);
                        break;
                    case 6:
                        setColumnType(i, 3);
                        break;
                }
            }
            int i2 = 0;
            while (resultSet.next()) {
                for (int i3 = 0; i3 < columnCount; i3++) {
                    switch (this.columnTypes[i3]) {
                        case 0:
                            setString(i2, i3, resultSet.getString(i3 + 1));
                            break;
                        case 1:
                            setInt(i2, i3, resultSet.getInt(i3 + 1));
                            break;
                        case 2:
                            setLong(i2, i3, resultSet.getLong(i3 + 1));
                            break;
                        case 3:
                            setFloat(i2, i3, resultSet.getFloat(i3 + 1));
                            break;
                        case 4:
                            setDouble(i2, i3, resultSet.getDouble(i3 + 1));
                            break;
                        default:
                            throw new IllegalArgumentException("column type " + this.columnTypes[i3] + " not supported.");
                    }
                }
                i2++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extensionOptions(boolean z, String str, String str2) {
        String checkExtension = PApplet.checkExtension(str);
        if (checkExtension == null) {
            return str2;
        }
        for (String equals : z ? loadExtensions : saveExtensions) {
            if (checkExtension.equals(equals)) {
                return str2 == null ? checkExtension : checkExtension + "," + str2;
            }
        }
        return str2;
    }

    protected static int nextComma(char[] cArr, int i) {
        boolean z = false;
        while (i < cArr.length) {
            if (!z && cArr[i] == ',') {
                return i;
            }
            if (cArr[i] == '\"') {
                z = !z;
            }
            i++;
        }
        return cArr.length;
    }

    private void odsAppendNotNull(XML xml, StringBuffer stringBuffer) {
        String content = xml.getContent();
        if (content != null) {
            stringBuffer.append(content);
        }
    }

    private InputStream odsFindContentXML(InputStream inputStream) {
        ZipEntry nextEntry;
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        do {
            try {
                nextEntry = zipInputStream.getNextEntry();
                if (nextEntry == null) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!nextEntry.getName().equals("content.xml"));
        return zipInputStream;
    }

    private void odsParseSheet(XML xml) {
        XML[] children = xml.getChildren("table:table-row");
        int length = children.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            XML xml2 = children[i];
            int i3 = xml2.getInt("table:number-rows-repeated", 1);
            boolean z = false;
            XML[] children2 = xml2.getChildren();
            int i4 = 0;
            int length2 = children2.length;
            int i5 = 0;
            while (i5 < length2) {
                XML xml3 = children2[i5];
                int i6 = xml3.getInt("table:number-columns-repeated", 1);
                String string = xml3.getString("office:value");
                if (string == null && xml3.getChildCount() != 0) {
                    XML[] children3 = xml3.getChildren("text:p");
                    if (children3.length != 1) {
                        for (XML xml4 : children3) {
                            System.err.println(xml4.toString());
                        }
                        throw new RuntimeException("found more than one text:p element");
                    }
                    XML xml5 = children3[0];
                    string = xml5.getContent();
                    if (string == null) {
                        XML[] children4 = xml5.getChildren();
                        StringBuffer stringBuffer = new StringBuffer();
                        int length3 = children4.length;
                        int i7 = 0;
                        while (true) {
                            int i8 = i7;
                            if (i8 >= length3) {
                                break;
                            }
                            XML xml6 = children4[i8];
                            String name = xml6.getName();
                            if (name == null) {
                                odsAppendNotNull(xml6, stringBuffer);
                            } else if (name.equals("text:s")) {
                                int i9 = xml6.getInt("text:c", 1);
                                for (int i10 = 0; i10 < i9; i10++) {
                                    stringBuffer.append(' ');
                                }
                            } else if (name.equals("text:span")) {
                                odsAppendNotNull(xml6, stringBuffer);
                            } else if (name.equals("text:a")) {
                                stringBuffer.append(xml6.getString("xlink:href"));
                            } else {
                                odsAppendNotNull(xml6, stringBuffer);
                                System.err.println(getClass().getName() + ": don't understand: " + xml6);
                            }
                            i7 = i8 + 1;
                        }
                        string = stringBuffer.toString();
                    }
                }
                boolean z2 = z;
                int i11 = i4;
                for (int i12 = 0; i12 < i6; i12++) {
                    if (string != null) {
                        setString(i2, i11, string);
                    }
                    i11++;
                    if (string != null) {
                        z2 = true;
                    }
                }
                i5++;
                i4 = i11;
                z = z2;
            }
            if (z && i3 > 1) {
                String[] stringRow = getStringRow(i2);
                for (int i13 = 1; i13 < i3; i13++) {
                    addRow((Object[]) stringRow);
                }
            }
            i++;
            i2 += i3;
        }
    }

    protected static String[] splitLineCSV(String str) {
        int i;
        int i2;
        int i3 = 0;
        char[] charArray = str.toCharArray();
        boolean z = false;
        int i4 = 1;
        for (int i5 = 0; i5 < charArray.length; i5++) {
            if (!z && charArray[i5] == ',') {
                i4++;
            } else if (charArray[i5] == '\"') {
                z = !z;
            }
        }
        String[] strArr = new String[i4];
        int i6 = 0;
        while (i3 < charArray.length) {
            int nextComma = nextComma(charArray, i3);
            int i7 = nextComma + 1;
            if (charArray[i3] == '\"' && charArray[nextComma - 1] == '\"') {
                int i8 = i3 + 1;
                i2 = nextComma - 1;
                i = i8;
            } else {
                int i9 = nextComma;
                i = i3;
                i2 = i9;
            }
            int i10 = i;
            int i11 = i;
            while (i11 < i2) {
                if (charArray[i11] == '\"') {
                    i11++;
                }
                if (i11 != i10) {
                    charArray[i10] = charArray[i11];
                }
                i10++;
                i11++;
            }
            int i12 = i6 + 1;
            strArr[i6] = new String(charArray, i, i10 - i);
            i6 = i12;
            i3 = i7;
        }
        while (i6 < strArr.length) {
            strArr[i6] = "";
            i6++;
        }
        return strArr;
    }

    public void addColumn() {
        addColumn(null, 0);
    }

    public void addColumn(String str) {
        addColumn(str, 0);
    }

    public void addColumn(String str, int i) {
        insertColumn(this.columns.length, str, i);
    }

    public TableRow addRow() {
        setRowCount(this.rowCount + 1);
        return new RowPointer(this, this.rowCount - 1);
    }

    public TableRow addRow(TableRow tableRow) {
        int i = this.rowCount;
        ensureBounds(i, tableRow.getColumnCount() - 1);
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            switch (this.columnTypes[i2]) {
                case 0:
                    setString(i, i2, tableRow.getString(i2));
                    break;
                case 1:
                case 5:
                    setInt(i, i2, tableRow.getInt(i2));
                    break;
                case 2:
                    setLong(i, i2, tableRow.getLong(i2));
                    break;
                case 3:
                    setFloat(i, i2, tableRow.getFloat(i2));
                    break;
                case 4:
                    setDouble(i, i2, tableRow.getDouble(i2));
                    break;
                default:
                    throw new RuntimeException("no types");
            }
        }
        return new RowPointer(this, i);
    }

    public TableRow addRow(Object[] objArr) {
        setRow(getRowCount(), objArr);
        return new RowPointer(this, this.rowCount - 1);
    }

    /* access modifiers changed from: protected */
    public void checkBounds(int i, int i2) {
        checkRow(i);
        checkColumn(i2);
    }

    /* access modifiers changed from: protected */
    public void checkColumn(int i) {
        if (i < 0 || i >= this.columns.length) {
            throw new ArrayIndexOutOfBoundsException("Column " + i + " does not exist.");
        }
    }

    public int checkColumnIndex(String str) {
        int columnIndex = getColumnIndex(str, false);
        if (columnIndex != -1) {
            return columnIndex;
        }
        addColumn(str);
        return getColumnCount() - 1;
    }

    /* access modifiers changed from: protected */
    public void checkRow(int i) {
        if (i < 0 || i >= this.rowCount) {
            throw new ArrayIndexOutOfBoundsException("Row " + i + " does not exist.");
        }
    }

    public void clearRows() {
        setRowCount(0);
    }

    /* access modifiers changed from: protected */
    public void convertBasic(BufferedReader bufferedReader, boolean z, File file) throws IOException {
        HashMapBlows[] hashMapBlowsArr;
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file), PGL.COLOR_BUFFER_BIT));
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(getColumnCount());
        if (this.columnTitles != null) {
            dataOutputStream.writeBoolean(true);
            for (String writeUTF : this.columnTitles) {
                dataOutputStream.writeUTF(writeUTF);
            }
        } else {
            dataOutputStream.writeBoolean(false);
        }
        for (int writeInt : this.columnTypes) {
            dataOutputStream.writeInt(writeInt);
        }
        int i = 0;
        int i2 = -1;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            convertRow(dataOutputStream, z ? PApplet.split(readLine, 9) : splitLineCSV(readLine));
            int i3 = i + 1;
            if (i3 % 10000 != 0 || i3 >= this.rowCount) {
                i = i3;
            } else {
                int i4 = (i3 * 100) / this.rowCount;
                if (i4 != i2) {
                    System.out.println(i4 + "%");
                } else {
                    i4 = i2;
                }
                i2 = i4;
                i = i3;
            }
        }
        int i5 = 0;
        for (HashMapBlows hashMapBlows : this.columnCategories) {
            if (hashMapBlows == null) {
                dataOutputStream.writeInt(0);
            } else {
                hashMapBlows.write(dataOutputStream);
                hashMapBlows.writeln(PApplet.createWriter(new File(this.columnTitles[i5] + ".categories")));
            }
            i5++;
        }
        dataOutputStream.flush();
        dataOutputStream.close();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.writeInt(this.rowCount);
        randomAccessFile.close();
    }

    /* access modifiers changed from: protected */
    public void convertRow(DataOutputStream dataOutputStream, String[] strArr) throws IOException {
        if (strArr.length > getColumnCount()) {
            throw new IllegalArgumentException("Row with too many columns: " + PApplet.join(strArr, ","));
        }
        for (int i = 0; i < strArr.length; i++) {
            switch (this.columnTypes[i]) {
                case 0:
                    dataOutputStream.writeUTF(strArr[i]);
                    break;
                case 1:
                    dataOutputStream.writeInt(PApplet.parseInt(strArr[i], this.missingInt));
                    break;
                case 2:
                    try {
                        dataOutputStream.writeLong(Long.parseLong(strArr[i]));
                        break;
                    } catch (NumberFormatException e) {
                        dataOutputStream.writeLong(this.missingLong);
                        break;
                    }
                case 3:
                    dataOutputStream.writeFloat(PApplet.parseFloat(strArr[i], this.missingFloat));
                    break;
                case 4:
                    try {
                        dataOutputStream.writeDouble(Double.parseDouble(strArr[i]));
                        break;
                    } catch (NumberFormatException e2) {
                        dataOutputStream.writeDouble(this.missingDouble);
                        break;
                    }
                case 5:
                    dataOutputStream.writeInt(this.columnCategories[i].index(strArr[i]));
                    break;
            }
        }
        for (int length = strArr.length; length < getColumnCount(); length++) {
            switch (this.columnTypes[length]) {
                case 0:
                    dataOutputStream.writeUTF("");
                    break;
                case 1:
                    dataOutputStream.writeInt(this.missingInt);
                    break;
                case 2:
                    dataOutputStream.writeLong(this.missingLong);
                    break;
                case 3:
                    dataOutputStream.writeFloat(this.missingFloat);
                    break;
                case 4:
                    dataOutputStream.writeDouble(this.missingDouble);
                    break;
                case 5:
                    dataOutputStream.writeInt(this.missingCategory);
                    break;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Table createSubset(int[] iArr) {
        Table table = new Table();
        table.setColumnTitles(this.columnTitles);
        table.columnTypes = this.columnTypes;
        table.setRowCount(iArr.length);
        for (int i = 0; i < iArr.length; i++) {
            int i2 = iArr[i];
            for (int i3 = 0; i3 < this.columns.length; i3++) {
                switch (this.columnTypes[i3]) {
                    case 0:
                        table.setString(i, i3, getString(i2, i3));
                        break;
                    case 1:
                        table.setInt(i, i3, getInt(i2, i3));
                        break;
                    case 2:
                        table.setLong(i, i3, getLong(i2, i3));
                        break;
                    case 3:
                        table.setFloat(i, i3, getFloat(i2, i3));
                        break;
                    case 4:
                        table.setDouble(i, i3, getDouble(i2, i3));
                        break;
                }
            }
        }
        return table;
    }

    /* access modifiers changed from: protected */
    public void ensureBounds(int i, int i2) {
        ensureRow(i);
        ensureColumn(i2);
    }

    /* access modifiers changed from: protected */
    public void ensureColumn(int i) {
        if (i >= this.columns.length) {
            setColumnCount(i + 1);
        }
    }

    /* access modifiers changed from: protected */
    public void ensureRow(int i) {
        if (i >= this.rowCount) {
            setRowCount(i + 1);
        }
    }

    public TableRow findRow(String str, int i) {
        int findRowIndex = findRowIndex(str, i);
        if (findRowIndex == -1) {
            return null;
        }
        return new RowPointer(this, findRowIndex);
    }

    public TableRow findRow(String str, String str2) {
        return findRow(str, getColumnIndex(str2));
    }

    public int findRowIndex(String str, int i) {
        int i2 = 0;
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            if (str == null) {
                while (i2 < this.rowCount) {
                    if (strArr[i2] == null) {
                        return i2;
                    }
                    i2++;
                }
            } else {
                while (i2 < this.rowCount) {
                    if (strArr[i2] != null && strArr[i2].equals(str)) {
                        return i2;
                    }
                    i2++;
                }
            }
        } else {
            while (i2 < this.rowCount) {
                String string = getString(i2, i);
                if (string == null) {
                    if (str == null) {
                        return i2;
                    }
                } else if (string.equals(str)) {
                    return i2;
                }
                i2++;
            }
        }
        return -1;
    }

    public int findRowIndex(String str, String str2) {
        return findRowIndex(str, getColumnIndex(str2));
    }

    public int[] findRowIndices(String str, int i) {
        int i2;
        int[] iArr = new int[this.rowCount];
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            if (str == null) {
                i2 = 0;
                for (int i3 = 0; i3 < this.rowCount; i3++) {
                    if (strArr[i3] == null) {
                        int i4 = i2 + 1;
                        iArr[i2] = i3;
                        i2 = i4;
                    }
                }
            } else {
                i2 = 0;
                for (int i5 = 0; i5 < this.rowCount; i5++) {
                    if (strArr[i5] != null && strArr[i5].equals(str)) {
                        int i6 = i2 + 1;
                        iArr[i2] = i5;
                        i2 = i6;
                    }
                }
            }
        } else {
            int i7 = 0;
            for (int i8 = 0; i8 < this.rowCount; i8++) {
                String string = getString(i8, i);
                if (string == null) {
                    if (str == null) {
                        int i9 = i7 + 1;
                        iArr[i7] = i8;
                        i7 = i9;
                    }
                } else if (string.equals(str)) {
                    int i10 = i7 + 1;
                    iArr[i7] = i8;
                    i7 = i10;
                }
            }
            i2 = i7;
        }
        return PApplet.subset(iArr, 0, i2);
    }

    public int[] findRowIndices(String str, String str2) {
        return findRowIndices(str, getColumnIndex(str2));
    }

    public Iterator<TableRow> findRows(String str, int i) {
        return new RowIndexIterator(this, findRowIndices(str, i));
    }

    public Iterator<TableRow> findRows(String str, String str2) {
        return findRows(str, getColumnIndex(str2));
    }

    public int getColumnCount() {
        return this.columns.length;
    }

    public int getColumnIndex(String str) {
        return getColumnIndex(str, true);
    }

    /* access modifiers changed from: protected */
    public int getColumnIndex(String str, boolean z) {
        if (this.columnTitles != null) {
            if (this.columnIndices == null) {
                this.columnIndices = new HashMap<>();
                for (int i = 0; i < this.columns.length; i++) {
                    this.columnIndices.put(this.columnTitles[i], Integer.valueOf(i));
                }
            }
            Integer num = (Integer) this.columnIndices.get(str);
            if (num != null) {
                return num.intValue();
            }
            if (!z) {
                return -1;
            }
            throw new IllegalArgumentException("This table has no column named '" + str + "'");
        } else if (!z) {
            return -1;
        } else {
            throw new IllegalArgumentException("This table has no header, so no column titles are set.");
        }
    }

    public String getColumnTitle(int i) {
        if (this.columnTitles == null) {
            return null;
        }
        return this.columnTitles[i];
    }

    public String[] getColumnTitles() {
        return this.columnTitles;
    }

    public int getColumnType(int i) {
        return this.columnTypes[i];
    }

    public int getColumnType(String str) {
        return getColumnType(getColumnIndex(str));
    }

    public double getDouble(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 4) {
            return ((double[]) this.columns[i2])[i];
        }
        String string = getString(i, i2);
        if (string == null || string.equals(this.missingString)) {
            return this.missingDouble;
        }
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return this.missingDouble;
        }
    }

    public double getDouble(int i, String str) {
        return getDouble(i, getColumnIndex(str));
    }

    public double[] getDoubleColumn(int i) {
        double[] dArr = new double[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            dArr[i2] = getDouble(i2, i);
        }
        return dArr;
    }

    public double[] getDoubleColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getDoubleColumn(columnIndex);
    }

    public double[] getDoubleRow(int i) {
        double[] dArr = new double[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            dArr[i2] = getDouble(i, i2);
        }
        return dArr;
    }

    public float getFloat(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 3) {
            return ((float[]) this.columns[i2])[i];
        }
        String string = getString(i, i2);
        return (string == null || string.equals(this.missingString)) ? this.missingFloat : PApplet.parseFloat(string, this.missingFloat);
    }

    public float getFloat(int i, String str) {
        return getFloat(i, getColumnIndex(str));
    }

    public float[] getFloatColumn(int i) {
        float[] fArr = new float[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            fArr[i2] = getFloat(i2, i);
        }
        return fArr;
    }

    public float[] getFloatColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getFloatColumn(columnIndex);
    }

    public FloatDict getFloatDict(int i, int i2) {
        return new FloatDict(getStringColumn(i), getFloatColumn(i2));
    }

    public FloatDict getFloatDict(String str, String str2) {
        return new FloatDict(getStringColumn(str), getFloatColumn(str2));
    }

    public FloatList getFloatList(int i) {
        return new FloatList(getFloatColumn(i));
    }

    public FloatList getFloatList(String str) {
        return new FloatList(getFloatColumn(str));
    }

    public float[] getFloatRow(int i) {
        float[] fArr = new float[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            fArr[i2] = getFloat(i, i2);
        }
        return fArr;
    }

    public int getInt(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 1 || this.columnTypes[i2] == 5) {
            return ((int[]) this.columns[i2])[i];
        }
        String string = getString(i, i2);
        return (string == null || string.equals(this.missingString)) ? this.missingInt : PApplet.parseInt(string, this.missingInt);
    }

    public int getInt(int i, String str) {
        return getInt(i, getColumnIndex(str));
    }

    public int[] getIntColumn(int i) {
        int[] iArr = new int[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            iArr[i2] = getInt(i2, i);
        }
        return iArr;
    }

    public int[] getIntColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getIntColumn(columnIndex);
    }

    public IntDict getIntDict(int i, int i2) {
        return new IntDict(getStringColumn(i), getIntColumn(i2));
    }

    public IntDict getIntDict(String str, String str2) {
        return new IntDict(getStringColumn(str), getIntColumn(str2));
    }

    public IntList getIntList(int i) {
        return new IntList(getIntColumn(i));
    }

    public IntList getIntList(String str) {
        return new IntList(getIntColumn(str));
    }

    public int[] getIntRow(int i) {
        int[] iArr = new int[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            iArr[i2] = getInt(i, i2);
        }
        return iArr;
    }

    public long getLong(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 2) {
            return ((long[]) this.columns[i2])[i];
        }
        String string = getString(i, i2);
        if (string == null || string.equals(this.missingString)) {
            return this.missingLong;
        }
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return this.missingLong;
        }
    }

    public long getLong(int i, String str) {
        return getLong(i, getColumnIndex(str));
    }

    public long[] getLongColumn(int i) {
        long[] jArr = new long[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            jArr[i2] = getLong(i2, i);
        }
        return jArr;
    }

    public long[] getLongColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getLongColumn(columnIndex);
    }

    public long[] getLongRow(int i) {
        long[] jArr = new long[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            jArr[i2] = getLong(i, i2);
        }
        return jArr;
    }

    /* access modifiers changed from: protected */
    public float getMaxFloat() {
        boolean z;
        float f;
        float f2 = -3.4028235E38f;
        boolean z2 = false;
        for (int i = 0; i < getRowCount(); i++) {
            int i2 = 0;
            while (i2 < getColumnCount()) {
                float f3 = getFloat(i, i2);
                if (!Float.isNaN(f3)) {
                    if (!z2) {
                        float f4 = f3;
                        z = true;
                        f = f4;
                    } else if (f3 > f2) {
                        float f5 = f3;
                        z = z2;
                        f = f5;
                    }
                    i2++;
                    f2 = f;
                    z2 = z;
                }
                z = z2;
                f = f2;
                i2++;
                f2 = f;
                z2 = z;
            }
        }
        return z2 ? f2 : this.missingFloat;
    }

    public IntDict getOrder(int i) {
        return new StringList(getStringColumn(i)).getOrder();
    }

    public IntDict getOrder(String str) {
        return getOrder(getColumnIndex(str));
    }

    public TableRow getRow(int i) {
        return new RowPointer(this, i);
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public String getString(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 0) {
            return ((String[]) this.columns[i2])[i];
        }
        if (this.columnTypes[i2] != 5) {
            return String.valueOf(Array.get(this.columns[i2], i));
        }
        int i3 = getInt(i, i2);
        return i3 == this.missingCategory ? this.missingString : this.columnCategories[i2].key(i3);
    }

    public String getString(int i, String str) {
        return getString(i, getColumnIndex(str));
    }

    public String[] getStringColumn(int i) {
        String[] strArr = new String[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            strArr[i2] = getString(i2, i);
        }
        return strArr;
    }

    public String[] getStringColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getStringColumn(columnIndex);
    }

    public StringDict getStringDict(int i, int i2) {
        return new StringDict(getStringColumn(i), getStringColumn(i2));
    }

    public StringDict getStringDict(String str, String str2) {
        return new StringDict(getStringColumn(str), getStringColumn(str2));
    }

    public StringList getStringList(int i) {
        return new StringList(getStringColumn(i));
    }

    public StringList getStringList(String str) {
        return new StringList(getStringColumn(str));
    }

    public String[] getStringRow(int i) {
        String[] strArr = new String[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            strArr[i2] = getString(i, i2);
        }
        return strArr;
    }

    public IntDict getTally(int i) {
        return new StringList(getStringColumn(i)).getTally();
    }

    public IntDict getTally(String str) {
        return getTally(getColumnIndex(str));
    }

    public String[] getUnique(int i) {
        return new StringList(getStringColumn(i)).getUnique();
    }

    public String[] getUnique(String str) {
        return getUnique(getColumnIndex(str));
    }

    public boolean hasColumnTitles() {
        return this.columnTitles != null;
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.columns = new Object[0];
        this.columnTypes = new int[0];
        this.columnCategories = new HashMapBlows[0];
    }

    public void insertColumn(int i) {
        insertColumn(i, null, 0);
    }

    public void insertColumn(int i, String str) {
        insertColumn(i, str, 0);
    }

    public void insertColumn(int i, String str, int i2) {
        if (str != null && this.columnTitles == null) {
            this.columnTitles = new String[this.columns.length];
        }
        if (this.columnTitles != null) {
            this.columnTitles = PApplet.splice(this.columnTitles, str, i);
            this.columnIndices = null;
        }
        this.columnTypes = PApplet.splice(this.columnTypes, i2, i);
        HashMapBlows[] hashMapBlowsArr = new HashMapBlows[(this.columns.length + 1)];
        for (int i3 = 0; i3 < i; i3++) {
            hashMapBlowsArr[i3] = this.columnCategories[i3];
        }
        hashMapBlowsArr[i] = new HashMapBlows();
        for (int i4 = i; i4 < this.columns.length; i4++) {
            hashMapBlowsArr[i4 + 1] = this.columnCategories[i4];
        }
        this.columnCategories = hashMapBlowsArr;
        Object[] objArr = new Object[(this.columns.length + 1)];
        System.arraycopy(this.columns, 0, objArr, 0, i);
        System.arraycopy(this.columns, i, objArr, i + 1, this.columns.length - i);
        this.columns = objArr;
        switch (i2) {
            case 0:
                this.columns[i] = new String[this.rowCount];
                return;
            case 1:
                this.columns[i] = new int[this.rowCount];
                return;
            case 2:
                this.columns[i] = new long[this.rowCount];
                return;
            case 3:
                this.columns[i] = new float[this.rowCount];
                return;
            case 4:
                this.columns[i] = new double[this.rowCount];
                return;
            case 5:
                this.columns[i] = new int[this.rowCount];
                return;
            default:
                return;
        }
    }

    public void insertRow(int i, Object[] objArr) {
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            switch (this.columnTypes[i2]) {
                case 0:
                    String[] strArr = new String[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, strArr, 0, i);
                    System.arraycopy(this.columns[i2], i, strArr, i + 1, (this.rowCount - i) + 1);
                    this.columns[i2] = strArr;
                    break;
                case 1:
                case 5:
                    int[] iArr = new int[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, iArr, 0, i);
                    System.arraycopy(this.columns[i2], i, iArr, i + 1, (this.rowCount - i) + 1);
                    this.columns[i2] = iArr;
                    break;
                case 2:
                    long[] jArr = new long[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, jArr, 0, i);
                    System.arraycopy(this.columns[i2], i, jArr, i + 1, (this.rowCount - i) + 1);
                    this.columns[i2] = jArr;
                    break;
                case 3:
                    float[] fArr = new float[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, fArr, 0, i);
                    System.arraycopy(this.columns[i2], i, fArr, i + 1, (this.rowCount - i) + 1);
                    this.columns[i2] = fArr;
                    break;
                case 4:
                    double[] dArr = new double[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, dArr, 0, i);
                    System.arraycopy(this.columns[i2], i, dArr, i + 1, (this.rowCount - i) + 1);
                    this.columns[i2] = dArr;
                    break;
            }
        }
        setRow(i, objArr);
        this.rowCount++;
    }

    public int lastRowIndex() {
        return getRowCount() - 1;
    }

    /* access modifiers changed from: protected */
    public void loadBinary(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
        int readInt = dataInputStream.readInt();
        if (readInt != -1878545634) {
            throw new IOException("Not a compatible binary table (magic was " + PApplet.hex(readInt) + ")");
        }
        int readInt2 = dataInputStream.readInt();
        setRowCount(readInt2);
        int readInt3 = dataInputStream.readInt();
        setColumnCount(readInt3);
        if (dataInputStream.readBoolean()) {
            this.columnTitles = new String[getColumnCount()];
            for (int i = 0; i < readInt3; i++) {
                setColumnTitle(i, dataInputStream.readUTF());
            }
        }
        for (int i2 = 0; i2 < readInt3; i2++) {
            int readInt4 = dataInputStream.readInt();
            this.columnTypes[i2] = readInt4;
            switch (readInt4) {
                case 0:
                    this.columns[i2] = new String[readInt2];
                    break;
                case 1:
                    this.columns[i2] = new int[readInt2];
                    break;
                case 2:
                    this.columns[i2] = new long[readInt2];
                    break;
                case 3:
                    this.columns[i2] = new float[readInt2];
                    break;
                case 4:
                    this.columns[i2] = new double[readInt2];
                    break;
                case 5:
                    this.columns[i2] = new int[readInt2];
                    break;
                default:
                    throw new IllegalArgumentException(readInt4 + " is not a valid column type.");
            }
        }
        for (int i3 = 0; i3 < readInt3; i3++) {
            if (this.columnTypes[i3] == 5) {
                this.columnCategories[i3] = new HashMapBlows(dataInputStream);
            }
        }
        if (dataInputStream.readBoolean()) {
            this.missingString = dataInputStream.readUTF();
        } else {
            this.missingString = null;
        }
        this.missingInt = dataInputStream.readInt();
        this.missingLong = dataInputStream.readLong();
        this.missingFloat = dataInputStream.readFloat();
        this.missingDouble = dataInputStream.readDouble();
        this.missingCategory = dataInputStream.readInt();
        for (int i4 = 0; i4 < readInt2; i4++) {
            for (int i5 = 0; i5 < readInt3; i5++) {
                switch (this.columnTypes[i5]) {
                    case 0:
                        setString(i4, i5, dataInputStream.readUTF());
                        break;
                    case 1:
                        setInt(i4, i5, dataInputStream.readInt());
                        break;
                    case 2:
                        setLong(i4, i5, dataInputStream.readLong());
                        break;
                    case 3:
                        setFloat(i4, i5, dataInputStream.readFloat());
                        break;
                    case 4:
                        setDouble(i4, i5, dataInputStream.readDouble());
                        break;
                    case 5:
                        setInt(i4, i5, dataInputStream.readInt());
                        break;
                }
            }
        }
        dataInputStream.close();
    }

    public TableRow matchRow(String str, int i) {
        int matchRowIndex = matchRowIndex(str, i);
        if (matchRowIndex == -1) {
            return null;
        }
        return new RowPointer(this, matchRowIndex);
    }

    public TableRow matchRow(String str, String str2) {
        return matchRow(str, getColumnIndex(str2));
    }

    public int matchRowIndex(String str, int i) {
        int i2 = 0;
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            while (i2 < this.rowCount) {
                if (strArr[i2] != null && PApplet.match(strArr[i2], str) != null) {
                    return i2;
                }
                i2++;
            }
        } else {
            while (i2 < this.rowCount) {
                String string = getString(i2, i);
                if (string != null && PApplet.match(string, str) != null) {
                    return i2;
                }
                i2++;
            }
        }
        return -1;
    }

    public int matchRowIndex(String str, String str2) {
        return matchRowIndex(str, getColumnIndex(str2));
    }

    public int[] matchRowIndices(String str, int i) {
        int i2;
        int[] iArr = new int[this.rowCount];
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            i2 = 0;
            for (int i3 = 0; i3 < this.rowCount; i3++) {
                if (!(strArr[i3] == null || PApplet.match(strArr[i3], str) == null)) {
                    int i4 = i2 + 1;
                    iArr[i2] = i3;
                    i2 = i4;
                }
            }
        } else {
            int i5 = 0;
            for (int i6 = 0; i6 < this.rowCount; i6++) {
                String string = getString(i6, i);
                if (!(string == null || PApplet.match(string, str) == null)) {
                    int i7 = i5 + 1;
                    iArr[i5] = i6;
                    i5 = i7;
                }
            }
            i2 = i5;
        }
        return PApplet.subset(iArr, 0, i2);
    }

    public int[] matchRowIndices(String str, String str2) {
        return matchRowIndices(str, getColumnIndex(str2));
    }

    public Iterator<TableRow> matchRows(String str, int i) {
        return new RowIndexIterator(this, matchRowIndices(str, i));
    }

    public Iterator<TableRow> matchRows(String str, String str2) {
        return matchRows(str, getColumnIndex(str2));
    }

    /* access modifiers changed from: protected */
    public void odsParse(InputStream inputStream, String str) {
        XML[] children;
        boolean z = false;
        try {
            for (XML xml : new XML(odsFindContentXML(inputStream)).getChildren("office:body/office:spreadsheet/table:table")) {
                if (str == null || str.equals(xml.getString("table:name"))) {
                    odsParseSheet(xml);
                    z = true;
                    if (str == null) {
                        break;
                    }
                }
            }
            if (z) {
                return;
            }
            if (str == null) {
                throw new RuntimeException("No worksheets found in the ODS file.");
            }
            throw new RuntimeException("No worksheet named " + str + " found in the ODS file.");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ParserConfigurationException e3) {
            e3.printStackTrace();
        } catch (SAXException e4) {
            e4.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void parse(InputStream inputStream, String str) throws IOException {
        boolean z;
        boolean z2;
        String str2;
        boolean z3;
        String[] splitTokens;
        String str3 = null;
        init();
        if (str != null) {
            z3 = false;
            str2 = null;
            z2 = false;
            z = false;
            for (String str4 : PApplet.splitTokens(str, " ,")) {
                if (str4.equals("tsv")) {
                    str2 = "tsv";
                } else if (str4.equals("csv")) {
                    str2 = "csv";
                } else if (str4.equals("ods")) {
                    str2 = "ods";
                } else if (str4.equals("newlines")) {
                    str2 = "csv";
                    z = true;
                } else if (str4.equals("bin")) {
                    str2 = "bin";
                    z3 = true;
                } else if (str4.equals("header")) {
                    z2 = true;
                } else if (str4.startsWith("worksheet=")) {
                    str3 = str4.substring("worksheet=".length());
                } else {
                    throw new IllegalArgumentException("'" + str4 + "' is not a valid option for loading a Table");
                }
            }
        } else {
            z3 = false;
            str2 = null;
            z2 = false;
            z = false;
        }
        if (str2 == null) {
            throw new IllegalArgumentException("No extension specified for this Table");
        } else if (z3) {
            loadBinary(inputStream);
        } else if (str2.equals("ods")) {
            odsParse(inputStream, str3);
        } else {
            BufferedReader createReader = PApplet.createReader(inputStream);
            if (z) {
                parseAwfulCSV(createReader, z2);
            } else if ("tsv".equals(str2)) {
                parseBasic(createReader, z2, true);
            } else if ("csv".equals(str2)) {
                parseBasic(createReader, z2, false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void parseAwfulCSV(BufferedReader bufferedReader, boolean z) throws IOException {
        int i = 0;
        boolean z2 = false;
        int i2 = 0;
        char[] cArr = new char[100];
        int i3 = 0;
        while (true) {
            int read = bufferedReader.read();
            if (read == -1) {
                break;
            } else if (z2) {
                if (read == 34) {
                    bufferedReader.mark(1);
                    if (bufferedReader.read() == 34) {
                        char[] cArr2 = i2 == cArr.length ? PApplet.expand(cArr) : cArr;
                        int i4 = i2 + 1;
                        cArr2[i2] = '\"';
                        i2 = i4;
                        cArr = cArr2;
                    } else {
                        bufferedReader.reset();
                        z2 = false;
                    }
                } else {
                    char[] cArr3 = i2 == cArr.length ? PApplet.expand(cArr) : cArr;
                    int i5 = i2 + 1;
                    cArr3[i2] = (char) read;
                    i2 = i5;
                    cArr = cArr3;
                }
            } else if (read == 34) {
                z2 = true;
            } else if (read == 13 || read == 10) {
                if (read == 13) {
                    bufferedReader.mark(1);
                    if (bufferedReader.read() != 10) {
                        bufferedReader.reset();
                    }
                }
                setString(i, i3, new String(cArr, 0, i2));
                if (i == 0 && z) {
                    removeTitleRow();
                    z = false;
                }
                i++;
                i2 = 0;
                i3 = 0;
            } else if (read == 44) {
                setString(i, i3, new String(cArr, 0, i2));
                i3++;
                ensureColumn(i3);
                i2 = 0;
            } else {
                char[] cArr4 = i2 == cArr.length ? PApplet.expand(cArr) : cArr;
                int i6 = i2 + 1;
                cArr4[i2] = (char) read;
                i2 = i6;
                cArr = cArr4;
            }
        }
        if (i2 > 0) {
            setString(i, i3, new String(cArr, 0, i2));
        }
    }

    /* access modifiers changed from: protected */
    public void parseBasic(BufferedReader bufferedReader, boolean z, boolean z2) throws IOException {
        if (this.rowCount == 0) {
            setRowCount(10);
        }
        int i = 0;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            if (i == getRowCount()) {
                setRowCount(i << 1);
            }
            if (i != 0 || !z) {
                setRow(i, z2 ? PApplet.split(readLine, 9) : splitLineCSV(readLine));
                i++;
            } else {
                setColumnTitles(z2 ? PApplet.split(readLine, 9) : splitLineCSV(readLine));
                z = false;
            }
        }
        if (i != getRowCount()) {
            setRowCount(i);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:104:0x019c A[Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002c A[Catch:{ SecurityException -> 0x01aa, NoSuchMethodException -> 0x01a7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00b2 A[Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parseInto(java.lang.Object r17, java.lang.String r18) {
        /*
            r16 = this;
            r2 = 0
            r1 = 0
            r4 = 0
            java.lang.Class r3 = r17.getClass()     // Catch:{ NoSuchFieldException -> 0x006b, SecurityException -> 0x0078 }
            r0 = r18
            java.lang.reflect.Field r3 = r3.getDeclaredField(r0)     // Catch:{ NoSuchFieldException -> 0x006b, SecurityException -> 0x0078 }
            java.lang.Class r4 = r3.getType()     // Catch:{ NoSuchFieldException -> 0x01b9, SecurityException -> 0x01ad }
            boolean r5 = r4.isArray()     // Catch:{ NoSuchFieldException -> 0x01b9, SecurityException -> 0x01ad }
            if (r5 != 0) goto L_0x005e
        L_0x0017:
            r4 = r1
        L_0x0018:
            java.lang.Class r8 = r2.getEnclosingClass()
            r5 = 0
            if (r8 != 0) goto L_0x0085
            r1 = 0
            java.lang.Class[] r1 = new java.lang.Class[r1]     // Catch:{ SecurityException -> 0x0090, NoSuchMethodException -> 0x0099 }
            java.lang.reflect.Constructor r1 = r2.getDeclaredConstructor(r1)     // Catch:{ SecurityException -> 0x0090, NoSuchMethodException -> 0x0099 }
        L_0x0026:
            boolean r5 = r1.isAccessible()     // Catch:{ SecurityException -> 0x01aa, NoSuchMethodException -> 0x01a7 }
            if (r5 != 0) goto L_0x0030
            r5 = 1
            r1.setAccessible(r5)     // Catch:{ SecurityException -> 0x01aa, NoSuchMethodException -> 0x01a7 }
        L_0x0030:
            r5 = r1
        L_0x0031:
            java.lang.reflect.Field[] r2 = r2.getDeclaredFields()
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            int r6 = r2.length
            r1 = 0
        L_0x003c:
            if (r1 >= r6) goto L_0x00a2
            r7 = r2[r1]
            java.lang.String r10 = r7.getName()
            r11 = 0
            r0 = r16
            int r10 = r0.getColumnIndex(r10, r11)
            r11 = -1
            if (r10 == r11) goto L_0x005b
            boolean r10 = r7.isAccessible()
            if (r10 != 0) goto L_0x0058
            r10 = 1
            r7.setAccessible(r10)
        L_0x0058:
            r9.add(r7)
        L_0x005b:
            int r1 = r1 + 1
            goto L_0x003c
        L_0x005e:
            java.lang.Class r2 = r4.getComponentType()     // Catch:{ NoSuchFieldException -> 0x01b9, SecurityException -> 0x01ad }
            int r4 = r16.getRowCount()     // Catch:{ NoSuchFieldException -> 0x01bf, SecurityException -> 0x01b3 }
            java.lang.Object r1 = java.lang.reflect.Array.newInstance(r2, r4)     // Catch:{ NoSuchFieldException -> 0x01bf, SecurityException -> 0x01b3 }
            goto L_0x0017
        L_0x006b:
            r3 = move-exception
            r15 = r3
            r3 = r2
            r2 = r4
            r4 = r15
        L_0x0070:
            r4.printStackTrace()
            r4 = r1
            r15 = r2
            r2 = r3
            r3 = r15
            goto L_0x0018
        L_0x0078:
            r3 = move-exception
            r15 = r3
            r3 = r2
            r2 = r4
            r4 = r15
        L_0x007d:
            r4.printStackTrace()
            r4 = r1
            r15 = r2
            r2 = r3
            r3 = r15
            goto L_0x0018
        L_0x0085:
            r1 = 1
            java.lang.Class[] r1 = new java.lang.Class[r1]     // Catch:{ SecurityException -> 0x0090, NoSuchMethodException -> 0x0099 }
            r6 = 0
            r1[r6] = r8     // Catch:{ SecurityException -> 0x0090, NoSuchMethodException -> 0x0099 }
            java.lang.reflect.Constructor r1 = r2.getDeclaredConstructor(r1)     // Catch:{ SecurityException -> 0x0090, NoSuchMethodException -> 0x0099 }
            goto L_0x0026
        L_0x0090:
            r1 = move-exception
            r15 = r1
            r1 = r5
            r5 = r15
        L_0x0094:
            r5.printStackTrace()
            r5 = r1
            goto L_0x0031
        L_0x0099:
            r1 = move-exception
            r15 = r1
            r1 = r5
            r5 = r15
        L_0x009d:
            r5.printStackTrace()
            r5 = r1
            goto L_0x0031
        L_0x00a2:
            r1 = 0
            java.lang.Iterable r2 = r16.rows()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.util.Iterator r10 = r2.iterator()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r6 = r1
        L_0x00ac:
            boolean r1 = r10.hasNext()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r1 == 0) goto L_0x0196
            java.lang.Object r1 = r10.next()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r0 = r1
            processing.data.TableRow r0 = (processing.data.TableRow) r0     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r2 = r0
            if (r8 != 0) goto L_0x00ed
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Object r1 = r5.newInstance(r1)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r7 = r1
        L_0x00c4:
            java.util.Iterator r11 = r9.iterator()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
        L_0x00c8:
            boolean r1 = r11.hasNext()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r1 == 0) goto L_0x018e
            java.lang.Object r1 = r11.next()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.reflect.Field r1 = (java.lang.reflect.Field) r1     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.String r12 = r1.getName()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Class r13 = r1.getType()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Class<java.lang.String> r14 = java.lang.String.class
            if (r13 != r14) goto L_0x00f9
            java.lang.String r12 = r2.getString(r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r1.set(r7, r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            goto L_0x00c8
        L_0x00e8:
            r1 = move-exception
            r1.printStackTrace()
        L_0x00ec:
            return
        L_0x00ed:
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r7 = 0
            r1[r7] = r17     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Object r1 = r5.newInstance(r1)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r7 = r1
            goto L_0x00c4
        L_0x00f9:
            java.lang.Class r13 = r1.getType()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Class r14 = java.lang.Integer.TYPE     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r13 != r14) goto L_0x010e
            int r12 = r2.getInt(r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r1.setInt(r7, r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            goto L_0x00c8
        L_0x0109:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00ec
        L_0x010e:
            java.lang.Class r13 = r1.getType()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Class r14 = java.lang.Long.TYPE     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r13 != r14) goto L_0x0123
            long r12 = r2.getLong(r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r1.setLong(r7, r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            goto L_0x00c8
        L_0x011e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00ec
        L_0x0123:
            java.lang.Class r13 = r1.getType()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Class r14 = java.lang.Float.TYPE     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r13 != r14) goto L_0x0138
            float r12 = r2.getFloat(r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r1.setFloat(r7, r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            goto L_0x00c8
        L_0x0133:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00ec
        L_0x0138:
            java.lang.Class r13 = r1.getType()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Class r14 = java.lang.Double.TYPE     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r13 != r14) goto L_0x0148
            double r12 = r2.getDouble(r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r1.setDouble(r7, r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            goto L_0x00c8
        L_0x0148:
            java.lang.Class r13 = r1.getType()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Class r14 = java.lang.Boolean.TYPE     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r13 != r14) goto L_0x0170
            java.lang.String r12 = r2.getString(r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r12 == 0) goto L_0x00c8
            java.lang.String r13 = r12.toLowerCase()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.String r14 = "true"
            boolean r13 = r13.equals(r14)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r13 != 0) goto L_0x016a
            java.lang.String r13 = "1"
            boolean r12 = r12.equals(r13)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r12 == 0) goto L_0x00c8
        L_0x016a:
            r12 = 1
            r1.setBoolean(r7, r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            goto L_0x00c8
        L_0x0170:
            java.lang.Class r13 = r1.getType()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            java.lang.Class r14 = java.lang.Character.TYPE     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r13 != r14) goto L_0x00c8
            java.lang.String r12 = r2.getString(r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r12 == 0) goto L_0x00c8
            int r13 = r12.length()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r13 <= 0) goto L_0x00c8
            r13 = 0
            char r12 = r12.charAt(r13)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r1.setChar(r7, r12)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            goto L_0x00c8
        L_0x018e:
            int r1 = r6 + 1
            java.lang.reflect.Array.set(r4, r6, r7)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            r6 = r1
            goto L_0x00ac
        L_0x0196:
            boolean r1 = r3.isAccessible()     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            if (r1 != 0) goto L_0x01a0
            r1 = 1
            r3.setAccessible(r1)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
        L_0x01a0:
            r0 = r17
            r3.set(r0, r4)     // Catch:{ InstantiationException -> 0x00e8, IllegalAccessException -> 0x0109, IllegalArgumentException -> 0x011e, InvocationTargetException -> 0x0133 }
            goto L_0x00ec
        L_0x01a7:
            r5 = move-exception
            goto L_0x009d
        L_0x01aa:
            r5 = move-exception
            goto L_0x0094
        L_0x01ad:
            r4 = move-exception
            r15 = r3
            r3 = r2
            r2 = r15
            goto L_0x007d
        L_0x01b3:
            r4 = move-exception
            r15 = r3
            r3 = r2
            r2 = r15
            goto L_0x007d
        L_0x01b9:
            r4 = move-exception
            r15 = r3
            r3 = r2
            r2 = r15
            goto L_0x0070
        L_0x01bf:
            r4 = move-exception
            r15 = r3
            r3 = r2
            r2 = r15
            goto L_0x0070
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.Table.parseInto(java.lang.Object, java.lang.String):void");
    }

    public void removeColumn(int i) {
        int length = this.columns.length - 1;
        Object[] objArr = new Object[length];
        HashMapBlows[] hashMapBlowsArr = new HashMapBlows[length];
        for (int i2 = 0; i2 < i; i2++) {
            objArr[i2] = this.columns[i2];
            hashMapBlowsArr[i2] = this.columnCategories[i2];
        }
        for (int i3 = i; i3 < length; i3++) {
            objArr[i3] = this.columns[i3 + 1];
            hashMapBlowsArr[i3] = this.columnCategories[i3 + 1];
        }
        this.columns = objArr;
        this.columnCategories = hashMapBlowsArr;
        if (this.columnTitles != null) {
            String[] strArr = new String[length];
            for (int i4 = 0; i4 < i; i4++) {
                strArr[i4] = this.columnTitles[i4];
            }
            while (i < length) {
                strArr[i] = this.columnTitles[i + 1];
                i++;
            }
            this.columnTitles = strArr;
            this.columnIndices = null;
        }
    }

    public void removeColumn(String str) {
        removeColumn(getColumnIndex(str));
    }

    public void removeRow(int i) {
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            switch (this.columnTypes[i2]) {
                case 0:
                    String[] strArr = new String[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, strArr, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, strArr, i, (this.rowCount - i) - 1);
                    this.columns[i2] = strArr;
                    break;
                case 1:
                case 5:
                    int[] iArr = new int[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, iArr, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, iArr, i, (this.rowCount - i) - 1);
                    this.columns[i2] = iArr;
                    break;
                case 2:
                    long[] jArr = new long[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, jArr, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, jArr, i, (this.rowCount - i) - 1);
                    this.columns[i2] = jArr;
                    break;
                case 3:
                    float[] fArr = new float[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, fArr, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, fArr, i, (this.rowCount - i) - 1);
                    this.columns[i2] = fArr;
                    break;
                case 4:
                    double[] dArr = new double[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, dArr, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, dArr, i, (this.rowCount - i) - 1);
                    this.columns[i2] = dArr;
                    break;
            }
        }
        this.rowCount--;
    }

    @Deprecated
    public String[] removeTitleRow() {
        String[] stringRow = getStringRow(0);
        removeRow(0);
        setColumnTitles(stringRow);
        return stringRow;
    }

    public void removeTokens(String str) {
        for (int i = 0; i < getColumnCount(); i++) {
            removeTokens(str, i);
        }
    }

    public void removeTokens(String str, int i) {
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            String string = getString(i2, i);
            if (string != null) {
                char[] charArray = string.toCharArray();
                int i3 = 0;
                for (int i4 = 0; i4 < charArray.length; i4++) {
                    if (str.indexOf(charArray[i4]) == -1) {
                        if (i3 != i4) {
                            charArray[i3] = charArray[i4];
                        }
                        i3++;
                    }
                }
                if (i3 != charArray.length) {
                    setString(i2, i, new String(charArray, 0, i3));
                }
            }
        }
    }

    public void removeTokens(String str, String str2) {
        removeTokens(str, getColumnIndex(str2));
    }

    public void replace(String str, String str2) {
        for (int i = 0; i < this.columns.length; i++) {
            replace(str, str2, i);
        }
    }

    public void replace(String str, String str2, int i) {
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            for (int i2 = 0; i2 < this.rowCount; i2++) {
                if (strArr[i2].equals(str)) {
                    strArr[i2] = str2;
                }
            }
        }
    }

    public void replace(String str, String str2, String str3) {
        replace(str, str2, getColumnIndex(str3));
    }

    public void replaceAll(String str, String str2) {
        for (int i = 0; i < this.columns.length; i++) {
            replaceAll(str, str2, i);
        }
    }

    public void replaceAll(String str, String str2, int i) {
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            for (int i2 = 0; i2 < this.rowCount; i2++) {
                if (strArr[i2] != null) {
                    strArr[i2] = strArr[i2].replaceAll(str, str2);
                }
            }
            return;
        }
        throw new IllegalArgumentException("replaceAll() can only be used on String columns");
    }

    public void replaceAll(String str, String str2, String str3) {
        replaceAll(str, str2, getColumnIndex(str3));
    }

    public Iterable<TableRow> rows() {
        return new Iterable<TableRow>() {
            public Iterator<TableRow> iterator() {
                if (Table.this.rowIterator == null) {
                    Table.this.rowIterator = new RowIterator(Table.this);
                } else {
                    Table.this.rowIterator.reset();
                }
                return Table.this.rowIterator;
            }
        };
    }

    public Iterable<TableRow> rows(final int[] iArr) {
        return new Iterable<TableRow>() {
            public Iterator<TableRow> iterator() {
                return new RowIndexIterator(Table.this, iArr);
            }
        };
    }

    public boolean save(File file, String str) throws IOException {
        return save(PApplet.createOutput(file), extensionOptions(false, file.getName(), str));
    }

    public boolean save(OutputStream outputStream, String str) {
        boolean z;
        PrintWriter createWriter = PApplet.createWriter(outputStream);
        if (str == null) {
            throw new IllegalArgumentException("No extension specified for saving this Table");
        }
        String[] splitTokens = PApplet.splitTokens(str, ", ");
        String str2 = splitTokens[splitTokens.length - 1];
        String[] strArr = saveExtensions;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            } else if (str2.equals(strArr[i])) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (!z) {
            throw new IllegalArgumentException("'" + str2 + "' not available for Table");
        }
        if (str2.equals("csv")) {
            writeCSV(createWriter);
        } else if (str2.equals("tsv")) {
            writeTSV(createWriter);
        } else if (str2.equals("html")) {
            writeHTML(createWriter);
        } else if (str2.equals("bin")) {
            try {
                saveBinary(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        createWriter.flush();
        createWriter.close();
        return true;
    }

    /* access modifiers changed from: protected */
    public void saveBinary(OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));
        dataOutputStream.writeInt(-1878545634);
        dataOutputStream.writeInt(getRowCount());
        dataOutputStream.writeInt(getColumnCount());
        if (this.columnTitles != null) {
            dataOutputStream.writeBoolean(true);
            for (String writeUTF : this.columnTitles) {
                dataOutputStream.writeUTF(writeUTF);
            }
        } else {
            dataOutputStream.writeBoolean(false);
        }
        for (int i = 0; i < getColumnCount(); i++) {
            dataOutputStream.writeInt(this.columnTypes[i]);
        }
        for (int i2 = 0; i2 < getColumnCount(); i2++) {
            if (this.columnTypes[i2] == 5) {
                this.columnCategories[i2].write(dataOutputStream);
            }
        }
        if (this.missingString == null) {
            dataOutputStream.writeBoolean(false);
        } else {
            dataOutputStream.writeBoolean(true);
            dataOutputStream.writeUTF(this.missingString);
        }
        dataOutputStream.writeInt(this.missingInt);
        dataOutputStream.writeLong(this.missingLong);
        dataOutputStream.writeFloat(this.missingFloat);
        dataOutputStream.writeDouble(this.missingDouble);
        dataOutputStream.writeInt(this.missingCategory);
        for (TableRow tableRow : rows()) {
            for (int i3 = 0; i3 < getColumnCount(); i3++) {
                switch (this.columnTypes[i3]) {
                    case 0:
                        dataOutputStream.writeUTF(tableRow.getString(i3));
                        break;
                    case 1:
                        dataOutputStream.writeInt(tableRow.getInt(i3));
                        break;
                    case 2:
                        dataOutputStream.writeLong(tableRow.getLong(i3));
                        break;
                    case 3:
                        dataOutputStream.writeFloat(tableRow.getFloat(i3));
                        break;
                    case 4:
                        dataOutputStream.writeDouble(tableRow.getDouble(i3));
                        break;
                    case 5:
                        dataOutputStream.writeInt(this.columnCategories[i3].index(tableRow.getString(i3)));
                        break;
                }
            }
        }
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    public void setColumnCount(int i) {
        int length = this.columns.length;
        if (length != i) {
            this.columns = (Object[]) PApplet.expand((Object) this.columns, i);
            for (int i2 = length; i2 < i; i2++) {
                this.columns[i2] = new String[this.rowCount];
            }
            if (this.columnTitles != null) {
                this.columnTitles = PApplet.expand(this.columnTitles, i);
            }
            this.columnTypes = PApplet.expand(this.columnTypes, i);
            this.columnCategories = (HashMapBlows[]) PApplet.expand((Object) this.columnCategories, i);
        }
    }

    public void setColumnTitle(int i, String str) {
        ensureColumn(i);
        if (this.columnTitles == null) {
            this.columnTitles = new String[getColumnCount()];
        }
        this.columnTitles[i] = str;
        this.columnIndices = null;
    }

    public void setColumnTitles(String[] strArr) {
        if (strArr != null) {
            ensureColumn(strArr.length - 1);
        }
        this.columnTitles = strArr;
        this.columnIndices = null;
    }

    public void setColumnType(int i, int i2) {
        int i3 = 0;
        switch (i2) {
            case 0:
                if (this.columnTypes[i] != 0) {
                    String[] strArr = new String[this.rowCount];
                    while (i3 < this.rowCount) {
                        strArr[i3] = getString(i3, i);
                        i3++;
                    }
                    this.columns[i] = strArr;
                    break;
                }
                break;
            case 1:
                int[] iArr = new int[this.rowCount];
                while (i3 < this.rowCount) {
                    iArr[i3] = PApplet.parseInt(getString(i3, i), this.missingInt);
                    i3++;
                }
                this.columns[i] = iArr;
                break;
            case 2:
                long[] jArr = new long[this.rowCount];
                while (i3 < this.rowCount) {
                    try {
                        jArr[i3] = Long.parseLong(getString(i3, i));
                    } catch (NumberFormatException e) {
                        jArr[i3] = this.missingLong;
                    }
                    i3++;
                }
                this.columns[i] = jArr;
                break;
            case 3:
                float[] fArr = new float[this.rowCount];
                while (i3 < this.rowCount) {
                    fArr[i3] = PApplet.parseFloat(getString(i3, i), this.missingFloat);
                    i3++;
                }
                this.columns[i] = fArr;
                break;
            case 4:
                double[] dArr = new double[this.rowCount];
                while (i3 < this.rowCount) {
                    try {
                        dArr[i3] = Double.parseDouble(getString(i3, i));
                    } catch (NumberFormatException e2) {
                        dArr[i3] = this.missingDouble;
                    }
                    i3++;
                }
                this.columns[i] = dArr;
                break;
            case 5:
                int[] iArr2 = new int[this.rowCount];
                HashMapBlows hashMapBlows = new HashMapBlows();
                while (i3 < this.rowCount) {
                    iArr2[i3] = hashMapBlows.index(getString(i3, i));
                    i3++;
                }
                this.columnCategories[i] = hashMapBlows;
                this.columns[i] = iArr2;
                break;
            default:
                throw new IllegalArgumentException("That's not a valid column type.");
        }
        this.columnTypes[i] = i2;
    }

    public void setColumnType(int i, String str) {
        int i2;
        if (str.equals("String")) {
            i2 = 0;
        } else if (str.equals("int")) {
            i2 = 1;
        } else if (str.equals("long")) {
            i2 = 2;
        } else if (str.equals("float")) {
            i2 = 3;
        } else if (str.equals("double")) {
            i2 = 4;
        } else if (str.equals("category")) {
            i2 = 5;
        } else {
            throw new IllegalArgumentException("'" + str + "' is not a valid column type.");
        }
        setColumnType(i, i2);
    }

    public void setColumnType(String str, int i) {
        setColumnType(checkColumnIndex(str), i);
    }

    public void setColumnType(String str, String str2) {
        setColumnType(checkColumnIndex(str), str2);
    }

    public void setColumnTypes(Table table) {
        int i;
        int i2;
        final int i3 = 0;
        if (table.hasColumnTitles()) {
            i2 = table.getColumnIndex("title", true);
            i = table.getColumnIndex("type", true);
        } else {
            i = 1;
            i2 = 0;
        }
        setColumnTitles(table.getStringColumn(i2));
        final String[] stringColumn = table.getStringColumn(i);
        if (table.getColumnCount() <= 1) {
            return;
        }
        if (getRowCount() > 1000) {
            ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);
            while (i3 < table.getRowCount()) {
                newFixedThreadPool.execute(new Runnable() {
                    public void run() {
                        Table.this.setColumnType(i3, stringColumn[i3]);
                    }
                });
                i3++;
            }
            newFixedThreadPool.shutdown();
            while (!newFixedThreadPool.isTerminated()) {
                Thread.yield();
            }
            return;
        }
        while (i3 < table.getRowCount()) {
            setColumnType(i3, stringColumn[i3]);
            i3++;
        }
    }

    public void setDouble(int i, int i2, double d) {
        if (this.columnTypes[i2] == 0) {
            setString(i, i2, String.valueOf(d));
            return;
        }
        ensureBounds(i, i2);
        if (this.columnTypes[i2] != 4) {
            throw new IllegalArgumentException("Column " + i2 + " is not a 'double' column.");
        }
        ((double[]) this.columns[i2])[i] = d;
    }

    public void setDouble(int i, String str, double d) {
        setDouble(i, getColumnIndex(str), d);
    }

    public void setFloat(int i, int i2, float f) {
        if (this.columnTypes[i2] == 0) {
            setString(i, i2, String.valueOf(f));
            return;
        }
        ensureBounds(i, i2);
        if (this.columnTypes[i2] != 3) {
            throw new IllegalArgumentException("Column " + i2 + " is not a float column.");
        }
        ((float[]) this.columns[i2])[i] = f;
    }

    public void setFloat(int i, String str, float f) {
        setFloat(i, getColumnIndex(str), f);
    }

    public void setInt(int i, int i2, int i3) {
        if (this.columnTypes[i2] == 0) {
            setString(i, i2, String.valueOf(i3));
            return;
        }
        ensureBounds(i, i2);
        if (this.columnTypes[i2] == 1 || this.columnTypes[i2] == 5) {
            ((int[]) this.columns[i2])[i] = i3;
            return;
        }
        throw new IllegalArgumentException("Column " + i2 + " is not an int column.");
    }

    public void setInt(int i, String str, int i2) {
        setInt(i, getColumnIndex(str), i2);
    }

    public void setLong(int i, int i2, long j) {
        if (this.columnTypes[i2] == 0) {
            setString(i, i2, String.valueOf(j));
            return;
        }
        ensureBounds(i, i2);
        if (this.columnTypes[i2] != 2) {
            throw new IllegalArgumentException("Column " + i2 + " is not a 'long' column.");
        }
        ((long[]) this.columns[i2])[i] = j;
    }

    public void setLong(int i, String str, long j) {
        setLong(i, getColumnIndex(str), j);
    }

    public void setMissingDouble(double d) {
        this.missingDouble = d;
    }

    public void setMissingFloat(float f) {
        this.missingFloat = f;
    }

    public void setMissingInt(int i) {
        this.missingInt = i;
    }

    public void setMissingLong(long j) {
        this.missingLong = j;
    }

    public void setMissingString(String str) {
        this.missingString = str;
    }

    public void setRow(int i, Object[] objArr) {
        ensureBounds(i, objArr.length - 1);
        for (int i2 = 0; i2 < objArr.length; i2++) {
            setRowCol(i, i2, objArr[i2]);
        }
    }

    /* access modifiers changed from: protected */
    public void setRowCol(int i, int i2, Object obj) {
        switch (this.columnTypes[i2]) {
            case 0:
                String[] strArr = (String[]) this.columns[i2];
                if (obj == null) {
                    strArr[i] = null;
                    return;
                } else {
                    strArr[i] = String.valueOf(obj);
                    return;
                }
            case 1:
                int[] iArr = (int[]) this.columns[i2];
                if (obj == null) {
                    iArr[i] = this.missingInt;
                    return;
                } else if (obj instanceof Integer) {
                    iArr[i] = ((Integer) obj).intValue();
                    return;
                } else {
                    iArr[i] = PApplet.parseInt(String.valueOf(obj), this.missingInt);
                    return;
                }
            case 2:
                long[] jArr = (long[]) this.columns[i2];
                if (obj == null) {
                    jArr[i] = this.missingLong;
                    return;
                } else if (obj instanceof Long) {
                    jArr[i] = ((Long) obj).longValue();
                    return;
                } else {
                    try {
                        jArr[i] = Long.parseLong(String.valueOf(obj));
                        return;
                    } catch (NumberFormatException e) {
                        jArr[i] = this.missingLong;
                        return;
                    }
                }
            case 3:
                float[] fArr = (float[]) this.columns[i2];
                if (obj == null) {
                    fArr[i] = this.missingFloat;
                    return;
                } else if (obj instanceof Float) {
                    fArr[i] = ((Float) obj).floatValue();
                    return;
                } else {
                    fArr[i] = PApplet.parseFloat(String.valueOf(obj), this.missingFloat);
                    return;
                }
            case 4:
                double[] dArr = (double[]) this.columns[i2];
                if (obj == null) {
                    dArr[i] = this.missingDouble;
                    return;
                } else if (obj instanceof Double) {
                    dArr[i] = ((Double) obj).doubleValue();
                    return;
                } else {
                    try {
                        dArr[i] = Double.parseDouble(String.valueOf(obj));
                        return;
                    } catch (NumberFormatException e2) {
                        dArr[i] = this.missingDouble;
                        return;
                    }
                }
            case 5:
                int[] iArr2 = (int[]) this.columns[i2];
                if (obj == null) {
                    iArr2[i] = this.missingCategory;
                    return;
                } else {
                    iArr2[i] = this.columnCategories[i2].index(String.valueOf(obj));
                    return;
                }
            default:
                throw new IllegalArgumentException("That's not a valid column type.");
        }
    }

    public void setRowCount(int i) {
        if (i != this.rowCount) {
            if (i > 1000000) {
                System.out.print("Note: setting maximum row count to " + PApplet.nfc(i));
            }
            long currentTimeMillis = System.currentTimeMillis();
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.columns.length) {
                    switch (this.columnTypes[i3]) {
                        case 0:
                            this.columns[i3] = PApplet.expand((String[]) this.columns[i3], i);
                            break;
                        case 1:
                            this.columns[i3] = PApplet.expand((int[]) this.columns[i3], i);
                            break;
                        case 2:
                            this.columns[i3] = PApplet.expand((Object) (long[]) this.columns[i3], i);
                            break;
                        case 3:
                            this.columns[i3] = PApplet.expand((float[]) this.columns[i3], i);
                            break;
                        case 4:
                            this.columns[i3] = PApplet.expand((Object) (double[]) this.columns[i3], i);
                            break;
                        case 5:
                            this.columns[i3] = PApplet.expand((int[]) this.columns[i3], i);
                            break;
                    }
                    if (i > 1000000) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    i2 = i3 + 1;
                } else if (i > 1000000) {
                    System.out.println(" (resize took " + PApplet.nfc((int) (System.currentTimeMillis() - currentTimeMillis)) + " ms)");
                }
            }
        }
        this.rowCount = i;
    }

    public void setString(int i, int i2, String str) {
        ensureBounds(i, i2);
        if (this.columnTypes[i2] != 0) {
            throw new IllegalArgumentException("Column " + i2 + " is not a String column.");
        }
        ((String[]) this.columns[i2])[i] = str;
    }

    public void setString(int i, String str, String str2) {
        setString(i, checkColumnIndex(str), str2);
    }

    public void setTableType(String str) {
        for (int i = 0; i < getColumnCount(); i++) {
            setColumnType(i, str);
        }
    }

    public void trim() {
        for (int i = 0; i < getColumnCount(); i++) {
            trim(i);
        }
    }

    public void trim(int i) {
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            for (int i2 = 0; i2 < this.rowCount; i2++) {
                if (strArr[i2] != null) {
                    strArr[i2] = PApplet.trim(strArr[i2]);
                }
            }
        }
    }

    public void trim(String str) {
        trim(getColumnIndex(str));
    }

    /* access modifiers changed from: protected */
    public void writeCSV(PrintWriter printWriter) {
        if (this.columnTitles != null) {
            for (int i = 0; i < this.columns.length; i++) {
                if (i != 0) {
                    printWriter.print(',');
                }
                if (this.columnTitles[i] != null) {
                    writeEntryCSV(printWriter, this.columnTitles[i]);
                }
            }
            printWriter.println();
        }
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            for (int i3 = 0; i3 < getColumnCount(); i3++) {
                if (i3 != 0) {
                    printWriter.print(',');
                }
                String string = getString(i2, i3);
                if (string != null) {
                    writeEntryCSV(printWriter, string);
                }
            }
            printWriter.println();
        }
        printWriter.flush();
    }

    /* access modifiers changed from: protected */
    public void writeEntryCSV(PrintWriter printWriter, String str) {
        if (str == null) {
            return;
        }
        if (str.indexOf(34) != -1) {
            char[] charArray = str.toCharArray();
            printWriter.print('\"');
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] == '\"') {
                    printWriter.print("\"\"");
                } else {
                    printWriter.print(charArray[i]);
                }
            }
            printWriter.print('\"');
        } else if (str.indexOf(44) != -1 || str.indexOf(10) != -1 || str.indexOf(13) != -1) {
            printWriter.print('\"');
            printWriter.print(str);
            printWriter.print('\"');
        } else if (str.length() <= 0 || !(str.charAt(0) == ' ' || str.charAt(str.length() - 1) == ' ')) {
            printWriter.print(str);
        } else {
            printWriter.print('\"');
            printWriter.print(str);
            printWriter.print('\"');
        }
    }

    /* access modifiers changed from: protected */
    public void writeEntryHTML(PrintWriter printWriter, String str) {
        char[] charArray;
        for (char c : str.toCharArray()) {
            if (c == '<') {
                printWriter.print("&lt;");
            } else if (c == '>') {
                printWriter.print("&gt;");
            } else if (c == '&') {
                printWriter.print("&amp;");
            } else if (c == '\'') {
                printWriter.print("&apos;");
            } else if (c == '\"') {
                printWriter.print("&quot;");
            } else {
                printWriter.print(c);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void writeHTML(PrintWriter printWriter) {
        printWriter.println("<html>");
        printWriter.println("<head>");
        printWriter.println("  <meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />");
        printWriter.println("</head>");
        printWriter.println("<body>");
        printWriter.println("  <table>");
        for (int i = 0; i < getRowCount(); i++) {
            printWriter.println("    <tr>");
            for (int i2 = 0; i2 < getColumnCount(); i2++) {
                String string = getString(i, i2);
                printWriter.print("      <td>");
                writeEntryHTML(printWriter, string);
                printWriter.println("      </td>");
            }
            printWriter.println("    </tr>");
        }
        printWriter.println("  </table>");
        printWriter.println("</body>");
        printWriter.println("</hmtl>");
        printWriter.flush();
    }

    /* access modifiers changed from: protected */
    public void writeTSV(PrintWriter printWriter) {
        if (this.columnTitles != null) {
            for (int i = 0; i < this.columns.length; i++) {
                if (i != 0) {
                    printWriter.print(9);
                }
                if (this.columnTitles[i] != null) {
                    printWriter.print(this.columnTitles[i]);
                }
            }
            printWriter.println();
        }
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            for (int i3 = 0; i3 < getColumnCount(); i3++) {
                if (i3 != 0) {
                    printWriter.print(9);
                }
                String string = getString(i2, i3);
                if (string != null) {
                    printWriter.print(string);
                }
            }
            printWriter.println();
        }
        printWriter.flush();
    }
}

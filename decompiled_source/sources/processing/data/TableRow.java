package processing.data;

public interface TableRow {
    int getColumnCount();

    int getColumnType(int i);

    int getColumnType(String str);

    double getDouble(int i);

    double getDouble(String str);

    float getFloat(int i);

    float getFloat(String str);

    int getInt(int i);

    int getInt(String str);

    long getLong(int i);

    long getLong(String str);

    String getString(int i);

    String getString(String str);

    void setDouble(int i, double d);

    void setDouble(String str, double d);

    void setFloat(int i, float f);

    void setFloat(String str, float f);

    void setInt(int i, int i2);

    void setInt(String str, int i);

    void setLong(int i, long j);

    void setLong(String str, long j);

    void setString(int i, String str);

    void setString(String str, String str2);
}

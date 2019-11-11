package processing.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import processing.core.PConstants;
import processing.core.PGraphics;

class JSONTokener {
    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private Reader reader;
    private boolean usePrevious;

    public JSONTokener(InputStream inputStream) {
        this((Reader) new InputStreamReader(inputStream));
    }

    public JSONTokener(Reader reader2) {
        if (!reader2.markSupported()) {
            reader2 = new BufferedReader(reader2);
        }
        this.reader = reader2;
        this.eof = false;
        this.usePrevious = false;
        this.previous = 0;
        this.index = 0;
        this.character = 1;
        this.line = 1;
    }

    public JSONTokener(String str) {
        this((Reader) new StringReader(str));
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c < 'a' || c > 'f') {
            return -1;
        }
        return c - 'W';
    }

    public void back() {
        if (this.usePrevious || this.index <= 0) {
            throw new RuntimeException("Stepping back two steps is not supported");
        }
        this.index--;
        this.character--;
        this.usePrevious = true;
        this.eof = false;
    }

    public boolean end() {
        return this.eof && !this.usePrevious;
    }

    public boolean more() {
        next();
        if (end()) {
            return false;
        }
        back();
        return true;
    }

    public char next() {
        int i = 0;
        if (this.usePrevious) {
            this.usePrevious = false;
            i = this.previous;
        } else {
            try {
                int read = this.reader.read();
                if (read <= 0) {
                    this.eof = true;
                } else {
                    i = read;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.index++;
        if (this.previous == 13) {
            this.line++;
            this.character = i == 10 ? 0 : 1;
        } else if (i == 10) {
            this.line = 1 + this.line;
            this.character = 0;
        } else {
            this.character++;
        }
        this.previous = (char) i;
        return this.previous;
    }

    public char next(char c) {
        char next = next();
        if (next == c) {
            return next;
        }
        throw new RuntimeException("Expected '" + c + "' and instead saw '" + next + "'");
    }

    public String next(int i) {
        if (i == 0) {
            return "";
        }
        char[] cArr = new char[i];
        for (int i2 = 0; i2 < i; i2++) {
            cArr[i2] = next();
            if (end()) {
                throw new RuntimeException("Substring bounds error");
            }
        }
        return new String(cArr);
    }

    public char nextClean() {
        char next;
        do {
            next = next();
            if (next == 0) {
                break;
            }
        } while (next <= ' ');
        return next;
    }

    public String nextString(char c) {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            char next = next();
            switch (next) {
                case 0:
                case 10:
                case 13:
                    throw new RuntimeException("Unterminated string");
                case '\\':
                    char next2 = next();
                    switch (next2) {
                        case PGraphics.f19EB /*34*/:
                        case '\'':
                        case '/':
                        case '\\':
                            stringBuffer.append(next2);
                            break;
                        case 'b':
                            stringBuffer.append(8);
                            break;
                        case PConstants.BOTTOM /*102*/:
                            stringBuffer.append(12);
                            break;
                        case 'n':
                            stringBuffer.append(10);
                            break;
                        case 'r':
                            stringBuffer.append(PConstants.RETURN);
                            break;
                        case 't':
                            stringBuffer.append(9);
                            break;
                        case 'u':
                            stringBuffer.append((char) Integer.parseInt(next(4), 16));
                            break;
                        default:
                            throw new RuntimeException("Illegal escape.");
                    }
                default:
                    if (next != c) {
                        stringBuffer.append(next);
                        break;
                    } else {
                        return stringBuffer.toString();
                    }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0017  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(char r4) {
        /*
            r3 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
        L_0x0005:
            char r1 = r3.next()
            if (r1 == r4) goto L_0x0015
            if (r1 == 0) goto L_0x0015
            r2 = 10
            if (r1 == r2) goto L_0x0015
            r2 = 13
            if (r1 != r2) goto L_0x0023
        L_0x0015:
            if (r1 == 0) goto L_0x001a
            r3.back()
        L_0x001a:
            java.lang.String r0 = r0.toString()
            java.lang.String r0 = r0.trim()
            return r0
        L_0x0023:
            r0.append(r1)
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.JSONTokener.nextTo(char):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(java.lang.String r4) {
        /*
            r3 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
        L_0x0005:
            char r1 = r3.next()
            int r2 = r4.indexOf(r1)
            if (r2 >= 0) goto L_0x0019
            if (r1 == 0) goto L_0x0019
            r2 = 10
            if (r1 == r2) goto L_0x0019
            r2 = 13
            if (r1 != r2) goto L_0x0027
        L_0x0019:
            if (r1 == 0) goto L_0x001e
            r3.back()
        L_0x001e:
            java.lang.String r0 = r0.toString()
            java.lang.String r0 = r0.trim()
            return r0
        L_0x0027:
            r0.append(r1)
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.JSONTokener.nextTo(java.lang.String):java.lang.String");
    }

    public Object nextValue() {
        char nextClean = nextClean();
        switch (nextClean) {
            case PGraphics.f19EB /*34*/:
            case '\'':
                return nextString(nextClean);
            case '[':
                back();
                return new JSONArray(this);
            case '{':
                back();
                return new JSONObject(this);
            default:
                StringBuffer stringBuffer = new StringBuffer();
                while (nextClean >= ' ' && ",:]}/\\\"[{;=#".indexOf(nextClean) < 0) {
                    stringBuffer.append(nextClean);
                    nextClean = next();
                }
                back();
                String trim = stringBuffer.toString().trim();
                if (!"".equals(trim)) {
                    return JSONObject.stringToValue(trim);
                }
                throw new RuntimeException("Missing value");
        }
    }

    public char skipTo(char c) {
        char next;
        try {
            long j = this.index;
            long j2 = this.character;
            long j3 = this.line;
            this.reader.mark(1000000);
            while (true) {
                next = next();
                if (next != 0) {
                    if (next == c) {
                        back();
                        break;
                    }
                } else {
                    this.reader.reset();
                    this.index = j;
                    this.character = j2;
                    this.line = j3;
                    break;
                }
            }
            return next;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
    }
}

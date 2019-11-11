package processing.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;

public class PFont implements PConstants {
    public static char[] CHARSET = new char[(EXTRA_CHARS.length + 94)];
    static final char[] EXTRA_CHARS = {128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 180, 181, 182, 183, 184, 186, 187, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 255, 258, 259, 260, 261, 262, 263, 268, 269, 270, 271, 272, 273, 280, 281, 282, 283, 305, 313, 314, 317, 318, 321, 322, 323, 324, 327, 328, 336, 337, 338, 339, 340, 341, 344, 345, 346, 347, 350, 351, 352, 353, 354, 355, 356, 357, 366, 367, 368, 369, 376, 377, 378, 379, 380, 381, 382, 402, 710, 711, 728, 729, 730, 731, 732, 733, 937, 960, 8211, 8212, 8216, 8217, 8218, 8220, 8221, 8222, 8224, 8225, 8226, 8230, 8240, 8249, 8250, 8260, 8364, 8482, 8706, 8710, 8719, 8721, 8730, 8734, 8747, 8776, 8800, 8804, 8805, 9674, 63743, 64257, 64258};
    static String[] fontList;
    static HashMap<String, Typeface> typefaceMap;
    protected static Typeface[] typefaces;
    protected int ascent;
    protected int[] ascii;
    protected HashMap<PGraphics, Object> cacheMap;
    protected int descent;
    protected int glyphCount;
    protected Glyph[] glyphs;
    protected boolean lazy;
    Bitmap lazyBitmap;
    Canvas lazyCanvas;
    Paint lazyPaint;
    int[] lazySamples;
    protected String name;
    protected String psname;
    protected int size;
    protected boolean smooth;
    protected boolean subsetting;
    protected Typeface typeface;
    protected boolean typefaceSearched;

    public class Glyph {
        public boolean fromStream = false;
        public int height;
        public PImage image;
        public int index;
        public int leftExtent;
        public int setWidth;
        public int topExtent;
        public int value;
        public int width;

        protected Glyph() {
        }

        protected Glyph(PFont pFont, char c) {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            PFont.this = pFont;
            int i5 = pFont.size * 3;
            pFont.lazyCanvas.drawColor(-1);
            pFont.lazyPaint.setColor(PImage.ALPHA_MASK);
            pFont.lazyCanvas.drawText(String.valueOf(c), (float) pFont.size, (float) (pFont.size * 2), pFont.lazyPaint);
            pFont.lazyBitmap.getPixels(pFont.lazySamples, 0, i5, 0, 0, i5, i5);
            int i6 = 0;
            boolean z = false;
            int i7 = 0;
            int i8 = 1000;
            int i9 = 0;
            int i10 = 1000;
            while (i6 < i5) {
                int i11 = 0;
                boolean z2 = z;
                int i12 = i10;
                int i13 = i9;
                int i14 = i8;
                boolean z3 = z2;
                while (i11 < i5) {
                    if ((pFont.lazySamples[(i6 * i5) + i11] & PImage.BLUE_MASK) != 255) {
                        if (i11 < i12) {
                            i12 = i11;
                        }
                        if (i6 < i14) {
                            i14 = i6;
                        }
                        if (i11 > i13) {
                            i13 = i11;
                        }
                        if (i6 > i7) {
                            i7 = i6;
                        }
                        z3 = true;
                    }
                    boolean z4 = z3;
                    i11++;
                    i7 = i7;
                    z3 = z4;
                }
                i6++;
                boolean z5 = z3;
                i8 = i14;
                i9 = i13;
                i10 = i12;
                z = z5;
            }
            if (!z) {
                i2 = 0;
                i = 0;
                i3 = 0;
            } else {
                i4 = i7;
                i = i9;
                i2 = i8;
                i3 = i10;
            }
            this.value = c;
            this.height = (i4 - i2) + 1;
            this.width = (i - i3) + 1;
            this.setWidth = (int) pFont.lazyPaint.measureText(String.valueOf(c));
            this.topExtent = (pFont.size * 2) - i2;
            this.leftExtent = i3 - pFont.size;
            this.image = new PImage(this.width, this.height, 4);
            int[] iArr = this.image.pixels;
            for (int i15 = i2; i15 <= i4; i15++) {
                for (int i16 = i3; i16 <= i; i16++) {
                    iArr[((i15 - i2) * this.width) + (i16 - i3)] = 255 - (pFont.lazySamples[(i15 * i5) + i16] & PImage.BLUE_MASK);
                }
            }
            if (this.value == 100 && pFont.ascent == 0) {
                pFont.ascent = this.topExtent;
            }
            if (this.value == 112 && pFont.descent == 0) {
                pFont.descent = (-this.topExtent) + this.height;
            }
        }

        protected Glyph(DataInputStream dataInputStream) throws IOException {
            readHeader(dataInputStream);
        }

        /* access modifiers changed from: protected */
        public void readBitmap(DataInputStream dataInputStream) throws IOException {
            this.image = new PImage(this.width, this.height, 4);
            byte[] bArr = new byte[(this.width * this.height)];
            dataInputStream.readFully(bArr);
            int i = this.width;
            int i2 = this.height;
            int[] iArr = this.image.pixels;
            for (int i3 = 0; i3 < i2; i3++) {
                for (int i4 = 0; i4 < i; i4++) {
                    iArr[(this.width * i3) + i4] = bArr[(i3 * i) + i4] & 255;
                }
            }
            this.fromStream = true;
        }

        /* access modifiers changed from: protected */
        public void readHeader(DataInputStream dataInputStream) throws IOException {
            this.value = dataInputStream.readInt();
            this.height = dataInputStream.readInt();
            this.width = dataInputStream.readInt();
            this.setWidth = dataInputStream.readInt();
            this.topExtent = dataInputStream.readInt();
            this.leftExtent = dataInputStream.readInt();
            dataInputStream.readInt();
            if (this.value == 100 && PFont.this.ascent == 0) {
                PFont.this.ascent = this.topExtent;
            }
            if (this.value == 112 && PFont.this.descent == 0) {
                PFont.this.descent = (-this.topExtent) + this.height;
            }
        }

        /* access modifiers changed from: protected */
        public void writeBitmap(DataOutputStream dataOutputStream) throws IOException {
            int[] iArr = this.image.pixels;
            for (int i = 0; i < this.height; i++) {
                for (int i2 = 0; i2 < this.width; i2++) {
                    dataOutputStream.write(iArr[(this.width * i) + i2] & PImage.BLUE_MASK);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void writeHeader(DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeInt(this.value);
            dataOutputStream.writeInt(this.height);
            dataOutputStream.writeInt(this.width);
            dataOutputStream.writeInt(this.setWidth);
            dataOutputStream.writeInt(this.topExtent);
            dataOutputStream.writeInt(this.leftExtent);
            dataOutputStream.writeInt(0);
        }
    }

    static {
        int i = 0;
        int i2 = 33;
        int i3 = 0;
        while (i2 <= 126) {
            int i4 = i3 + 1;
            CHARSET[i3] = (char) i2;
            i2++;
            i3 = i4;
        }
        while (i < EXTRA_CHARS.length) {
            int i5 = i3 + 1;
            CHARSET[i3] = EXTRA_CHARS[i];
            i++;
            i3 = i5;
        }
    }

    public PFont() {
    }

    public PFont(Typeface typeface2, int i, boolean z) {
        this(typeface2, i, z, null);
    }

    public PFont(Typeface typeface2, int i, boolean z, char[] cArr) {
        this.typeface = typeface2;
        this.smooth = z;
        this.name = "";
        this.psname = "";
        this.size = i;
        this.glyphs = new Glyph[10];
        this.ascii = new int[PConstants.MULTIPLY];
        Arrays.fill(this.ascii, -1);
        int i2 = i * 3;
        this.lazyBitmap = Bitmap.createBitmap(i2, i2, Config.ARGB_8888);
        this.lazyCanvas = new Canvas(this.lazyBitmap);
        this.lazyPaint = new Paint();
        this.lazyPaint.setAntiAlias(z);
        this.lazyPaint.setTypeface(typeface2);
        this.lazyPaint.setTextSize((float) i);
        this.lazySamples = new int[(i2 * i2)];
        if (cArr == null) {
            this.lazy = true;
        } else {
            Arrays.sort(cArr);
            this.glyphs = new Glyph[cArr.length];
            this.glyphCount = 0;
            for (char glyph : cArr) {
                Glyph glyph2 = new Glyph(this, glyph);
                if (glyph2.value < 128) {
                    this.ascii[glyph2.value] = this.glyphCount;
                }
                glyph2.index = this.glyphCount;
                Glyph[] glyphArr = this.glyphs;
                int i3 = this.glyphCount;
                this.glyphCount = i3 + 1;
                glyphArr[i3] = glyph2;
            }
        }
        if (this.ascent == 0) {
            new Glyph(this, 'd');
            if (this.ascent == 0) {
                this.ascent = PApplet.round(this.lazyPaint.ascent());
            }
        }
        if (this.descent == 0) {
            new Glyph(this, 'p');
            if (this.descent == 0) {
                this.descent = PApplet.round(this.lazyPaint.descent());
            }
        }
    }

    public PFont(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        this.glyphCount = dataInputStream.readInt();
        int readInt = dataInputStream.readInt();
        this.size = dataInputStream.readInt();
        dataInputStream.readInt();
        this.ascent = dataInputStream.readInt();
        this.descent = dataInputStream.readInt();
        this.glyphs = new Glyph[this.glyphCount];
        this.ascii = new int[PConstants.MULTIPLY];
        Arrays.fill(this.ascii, -1);
        for (int i = 0; i < this.glyphCount; i++) {
            Glyph glyph = new Glyph(dataInputStream);
            if (glyph.value < 128) {
                this.ascii[glyph.value] = i;
            }
            glyph.index = i;
            this.glyphs[i] = glyph;
        }
        if (this.ascent == 0 && this.descent == 0) {
            throw new RuntimeException("Please use \"Create Font\" to re-create this font.");
        }
        for (Glyph readBitmap : this.glyphs) {
            readBitmap.readBitmap(dataInputStream);
        }
        if (readInt >= 10) {
            this.name = dataInputStream.readUTF();
            this.psname = dataInputStream.readUTF();
        }
        if (readInt == 11) {
            this.smooth = dataInputStream.readBoolean();
        }
    }

    public static Object findNative(String str) {
        loadTypefaces();
        return typefaceMap.get(str);
    }

    public static String[] list() {
        loadTypefaces();
        return fontList;
    }

    public static void loadTypefaces() {
        if (typefaceMap == null) {
            typefaceMap = new HashMap<>();
            typefaceMap.put("Serif", Typeface.create(Typeface.SERIF, 0));
            typefaceMap.put("Serif-Bold", Typeface.create(Typeface.SERIF, 1));
            typefaceMap.put("Serif-Italic", Typeface.create(Typeface.SERIF, 2));
            typefaceMap.put("Serif-BoldItalic", Typeface.create(Typeface.SERIF, 3));
            typefaceMap.put("SansSerif", Typeface.create(Typeface.SANS_SERIF, 0));
            typefaceMap.put("SansSerif-Bold", Typeface.create(Typeface.SANS_SERIF, 1));
            typefaceMap.put("SansSerif-Italic", Typeface.create(Typeface.SANS_SERIF, 2));
            typefaceMap.put("SansSerif-BoldItalic", Typeface.create(Typeface.SANS_SERIF, 3));
            typefaceMap.put("Monospaced", Typeface.create(Typeface.MONOSPACE, 0));
            typefaceMap.put("Monospaced-Bold", Typeface.create(Typeface.MONOSPACE, 1));
            typefaceMap.put("Monospaced-Italic", Typeface.create(Typeface.MONOSPACE, 2));
            typefaceMap.put("Monospaced-BoldItalic", Typeface.create(Typeface.MONOSPACE, 3));
            fontList = new String[typefaceMap.size()];
            typefaceMap.keySet().toArray(fontList);
        }
    }

    /* access modifiers changed from: protected */
    public void addGlyph(char c) {
        Glyph glyph = new Glyph(this, c);
        if (this.glyphCount == this.glyphs.length) {
            this.glyphs = (Glyph[]) PApplet.expand((Object) this.glyphs);
        }
        if (this.glyphCount == 0) {
            glyph.index = 0;
            this.glyphs[this.glyphCount] = glyph;
            if (glyph.value < 128) {
                this.ascii[glyph.value] = 0;
            }
        } else if (this.glyphs[this.glyphCount - 1].value < glyph.value) {
            this.glyphs[this.glyphCount] = glyph;
            if (glyph.value < 128) {
                this.ascii[glyph.value] = this.glyphCount;
            }
        } else {
            int i = 0;
            while (true) {
                if (i >= this.glyphCount) {
                    break;
                } else if (this.glyphs[i].value > c) {
                    for (int i2 = this.glyphCount; i2 > i; i2--) {
                        this.glyphs[i2] = this.glyphs[i2 - 1];
                        if (this.glyphs[i2].value < 128) {
                            this.ascii[this.glyphs[i2].value] = i2;
                        }
                    }
                    glyph.index = i;
                    this.glyphs[i] = glyph;
                    if (c < 128) {
                        this.ascii[c] = i;
                    }
                } else {
                    i++;
                }
            }
        }
        this.glyphCount++;
    }

    public float ascent() {
        return ((float) this.ascent) / ((float) this.size);
    }

    public float descent() {
        return ((float) this.descent) / ((float) this.size);
    }

    public Object getCache(PGraphics pGraphics) {
        if (this.cacheMap == null) {
            return null;
        }
        return this.cacheMap.get(pGraphics);
    }

    public Glyph getGlyph(char c) {
        int index = index(c);
        if (index == -1) {
            return null;
        }
        return this.glyphs[index];
    }

    public Glyph getGlyph(int i) {
        return this.glyphs[i];
    }

    public int getGlyphCount() {
        return this.glyphCount;
    }

    public String getName() {
        return this.name;
    }

    public Object getNative() {
        if (this.subsetting) {
            return null;
        }
        return this.typeface;
    }

    public String getPostScriptName() {
        return this.psname;
    }

    public int getSize() {
        return this.size;
    }

    /* access modifiers changed from: protected */
    public int index(char c) {
        if (!this.lazy) {
            return indexActual(c);
        }
        int indexActual = indexActual(c);
        if (indexActual != -1) {
            return indexActual;
        }
        addGlyph(c);
        return indexActual(c);
    }

    /* access modifiers changed from: protected */
    public int indexActual(char c) {
        if (this.glyphCount == 0) {
            return -1;
        }
        return c < 128 ? this.ascii[c] : indexHunt(c, 0, this.glyphCount - 1);
    }

    /* access modifiers changed from: protected */
    public int indexHunt(int i, int i2, int i3) {
        int i4 = (i2 + i3) / 2;
        if (i == this.glyphs[i4].value) {
            return i4;
        }
        if (i2 >= i3) {
            return -1;
        }
        return i < this.glyphs[i4].value ? indexHunt(i, i2, i4 - 1) : indexHunt(i, i4 + 1, i3);
    }

    public float kern(char c, char c2) {
        return 0.0f;
    }

    public void removeCache(PGraphics pGraphics) {
        if (this.cacheMap != null) {
            this.cacheMap.remove(pGraphics);
        }
    }

    public void save(OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(this.glyphCount);
        if (this.name == null || this.psname == null) {
            this.name = "";
            this.psname = "";
        }
        dataOutputStream.writeInt(11);
        dataOutputStream.writeInt(this.size);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(this.ascent);
        dataOutputStream.writeInt(this.descent);
        for (int i = 0; i < this.glyphCount; i++) {
            this.glyphs[i].writeHeader(dataOutputStream);
        }
        for (int i2 = 0; i2 < this.glyphCount; i2++) {
            this.glyphs[i2].writeBitmap(dataOutputStream);
        }
        dataOutputStream.writeUTF(this.name);
        dataOutputStream.writeUTF(this.psname);
        dataOutputStream.writeBoolean(this.smooth);
        dataOutputStream.flush();
    }

    public void setCache(PGraphics pGraphics, Object obj) {
        if (this.cacheMap == null) {
            this.cacheMap = new HashMap<>();
        }
        this.cacheMap.put(pGraphics, obj);
    }

    public void setNative(Object obj) {
        this.typeface = (Typeface) obj;
    }

    public void setSubsetting() {
        this.subsetting = true;
    }

    public float width(char c) {
        if (c == ' ') {
            return width('i');
        }
        int index = index(c);
        if (index == -1) {
            return 0.0f;
        }
        return ((float) this.glyphs[index].setWidth) / ((float) this.size);
    }
}

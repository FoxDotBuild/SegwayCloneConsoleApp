package processing.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import processing.opengl.LinePath;
import processing.opengl.PGL;

public class PImage implements PConstants, Cloneable {
    public static final int ALPHA_MASK = -16777216;
    public static final int BLUE_MASK = 255;
    public static final int GREEN_MASK = 65280;
    static final int PRECISIONB = 15;
    static final int PRECISIONF = 32768;
    static final int PREC_ALPHA_SHIFT = 9;
    static final int PREC_MAXVAL = 32767;
    static final int PREC_RED_SHIFT = 1;
    public static final int RED_MASK = 16711680;
    static final String TIFF_ERROR = "Error: Processing can only read its own TIFF files.";
    static byte[] TIFF_HEADER;

    /* renamed from: a */
    private int f41a;

    /* renamed from: b */
    private int f42b;
    protected Bitmap bitmap;
    private int[] blurKernel;
    private int blurKernelSize;
    private int[][] blurMult;
    private int blurRadius;
    private int cLL;
    private int cLR;
    private int cUL;
    private int cUR;
    protected HashMap<PGraphics, Object> cacheMap;
    public int format;
    private int fracU;
    private int fracV;

    /* renamed from: g */
    private int f43g;
    public int height;
    private int ifU;
    private int ifV;
    private int ih1;

    /* renamed from: iw */
    private int f44iw;
    private int iw1;

    /* renamed from: ll */
    private int f45ll;

    /* renamed from: lr */
    private int f46lr;
    protected boolean modified;
    protected int mx1;
    protected int mx2;
    protected int my1;
    protected int my2;
    protected HashMap<PGraphics, Object> paramMap;
    public PApplet parent;
    public int[] pixels;

    /* renamed from: r */
    private int f47r;

    /* renamed from: sX */
    private int f48sX;

    /* renamed from: sY */
    private int f49sY;
    protected String[] saveImageFormats;
    private int[] srcBuffer;
    private int srcXOffset;
    private int srcYOffset;

    /* renamed from: u1 */
    private int f50u1;

    /* renamed from: u2 */
    private int f51u2;

    /* renamed from: ul */
    private int f52ul;

    /* renamed from: ur */
    private int f53ur;

    /* renamed from: v1 */
    private int f54v1;

    /* renamed from: v2 */
    private int f55v2;
    public int width;

    static {
        byte[] bArr = new byte[PConstants.MULTIPLY];
        // fill-array-data instruction
        bArr[0] = 77;
        bArr[1] = 77;
        bArr[2] = 0;
        bArr[3] = 42;
        bArr[4] = 0;
        bArr[5] = 0;
        bArr[6] = 0;
        bArr[7] = 8;
        bArr[8] = 0;
        bArr[9] = 9;
        bArr[10] = 0;
        bArr[11] = -2;
        bArr[12] = 0;
        bArr[13] = 4;
        bArr[14] = 0;
        bArr[15] = 0;
        bArr[16] = 0;
        bArr[17] = 1;
        bArr[18] = 0;
        bArr[19] = 0;
        bArr[20] = 0;
        bArr[21] = 0;
        bArr[22] = 1;
        bArr[23] = 0;
        bArr[24] = 0;
        bArr[25] = 3;
        bArr[26] = 0;
        bArr[27] = 0;
        bArr[28] = 0;
        bArr[29] = 1;
        bArr[30] = 0;
        bArr[31] = 0;
        bArr[32] = 0;
        bArr[33] = 0;
        bArr[34] = 1;
        bArr[35] = 1;
        bArr[36] = 0;
        bArr[37] = 3;
        bArr[38] = 0;
        bArr[39] = 0;
        bArr[40] = 0;
        bArr[41] = 1;
        bArr[42] = 0;
        bArr[43] = 0;
        bArr[44] = 0;
        bArr[45] = 0;
        bArr[46] = 1;
        bArr[47] = 2;
        bArr[48] = 0;
        bArr[49] = 3;
        bArr[50] = 0;
        bArr[51] = 0;
        bArr[52] = 0;
        bArr[53] = 3;
        bArr[54] = 0;
        bArr[55] = 0;
        bArr[56] = 0;
        bArr[57] = 122;
        bArr[58] = 1;
        bArr[59] = 6;
        bArr[60] = 0;
        bArr[61] = 3;
        bArr[62] = 0;
        bArr[63] = 0;
        bArr[64] = 0;
        bArr[65] = 1;
        bArr[66] = 0;
        bArr[67] = 2;
        bArr[68] = 0;
        bArr[69] = 0;
        bArr[70] = 1;
        bArr[71] = 17;
        bArr[72] = 0;
        bArr[73] = 4;
        bArr[74] = 0;
        bArr[75] = 0;
        bArr[76] = 0;
        bArr[77] = 1;
        bArr[78] = 0;
        bArr[79] = 0;
        bArr[80] = 3;
        bArr[81] = 0;
        bArr[82] = 1;
        bArr[83] = 21;
        bArr[84] = 0;
        bArr[85] = 3;
        bArr[86] = 0;
        bArr[87] = 0;
        bArr[88] = 0;
        bArr[89] = 1;
        bArr[90] = 0;
        bArr[91] = 3;
        bArr[92] = 0;
        bArr[93] = 0;
        bArr[94] = 1;
        bArr[95] = 22;
        bArr[96] = 0;
        bArr[97] = 3;
        bArr[98] = 0;
        bArr[99] = 0;
        bArr[100] = 0;
        bArr[101] = 1;
        bArr[102] = 0;
        bArr[103] = 0;
        bArr[104] = 0;
        bArr[105] = 0;
        bArr[106] = 1;
        bArr[107] = 23;
        bArr[108] = 0;
        bArr[109] = 4;
        bArr[110] = 0;
        bArr[111] = 0;
        bArr[112] = 0;
        bArr[113] = 1;
        bArr[114] = 0;
        bArr[115] = 0;
        bArr[116] = 0;
        bArr[117] = 0;
        bArr[118] = 0;
        bArr[119] = 0;
        bArr[120] = 0;
        bArr[121] = 0;
        bArr[122] = 0;
        bArr[123] = 8;
        bArr[124] = 0;
        bArr[125] = 8;
        bArr[126] = 0;
        bArr[127] = 8;
        TIFF_HEADER = bArr;
    }

    public PImage() {
        this.format = 2;
    }

    public PImage(int i, int i2) {
        init(i, i2, 1);
    }

    public PImage(int i, int i2, int i3) {
        init(i, i2, i3);
    }

    public PImage(Object obj) {
        Bitmap bitmap2 = (Bitmap) obj;
        this.bitmap = bitmap2;
        this.width = bitmap2.getWidth();
        this.height = bitmap2.getHeight();
        this.pixels = null;
        this.format = bitmap2.hasAlpha() ? 2 : 1;
    }

    public static int blendColor(int i, int i2, int i3) {
        switch (i3) {
            case 0:
                return i2;
            case 1:
                return blend_blend(i, i2);
            case 2:
                return blend_add_pin(i, i2);
            case 4:
                return blend_sub_pin(i, i2);
            case 8:
                return blend_lightest(i, i2);
            case 16:
                return blend_darkest(i, i2);
            case 32:
                return blend_difference(i, i2);
            case PConstants.EXCLUSION /*64*/:
                return blend_exclusion(i, i2);
            case PConstants.MULTIPLY /*128*/:
                return blend_multiply(i, i2);
            case 256:
                return blend_screen(i, i2);
            case PConstants.OVERLAY /*512*/:
                return blend_overlay(i, i2);
            case 1024:
                return blend_hard_light(i, i2);
            case PConstants.SOFT_LIGHT /*2048*/:
                return blend_soft_light(i, i2);
            case PConstants.DODGE /*4096*/:
                return blend_dodge(i, i2);
            case PConstants.BURN /*8192*/:
                return blend_burn(i, i2);
            default:
                return 0;
        }
    }

    private static int blend_add_pin(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        return low(((i3 * (i2 & BLUE_MASK)) >> 8) + (i & BLUE_MASK), BLUE_MASK) | (low(((-16777216 & i) >>> 24) + i3, BLUE_MASK) << 24) | (low((i & RED_MASK) + (((i2 & RED_MASK) >> 8) * i3), RED_MASK) & RED_MASK) | (low((i & GREEN_MASK) + (((i2 & GREEN_MASK) >> 8) * i3), GREEN_MASK) & GREEN_MASK);
    }

    private static int blend_blend(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        return mix(i & BLUE_MASK, i2 & BLUE_MASK, i3) | (low(((-16777216 & i) >>> 24) + i3, BLUE_MASK) << 24) | (mix(i & RED_MASK, i2 & RED_MASK, i3) & RED_MASK) | (mix(i & GREEN_MASK, i2 & GREEN_MASK, i3) & GREEN_MASK);
    }

    private static int blend_burn(int i, int i2) {
        int i3 = 0;
        int i4 = (i2 & ALPHA_MASK) >>> 24;
        int i5 = (i & RED_MASK) >> 16;
        int i6 = (i & GREEN_MASK) >> 8;
        int i7 = i & BLUE_MASK;
        int i8 = (i2 & RED_MASK) >> 16;
        int i9 = (65280 & i2) >> 8;
        int i10 = i2 & BLUE_MASK;
        int peg = i8 == 0 ? 0 : 255 - peg(((255 - i5) << 8) / i8);
        int peg2 = i9 == 0 ? 0 : 255 - peg(((255 - i6) << 8) / i9);
        if (i10 != 0) {
            i3 = 255 - peg(((255 - i7) << 8) / i10);
        }
        return peg((((i3 - i7) * i4) >> 8) + i7) | (peg((((peg2 - i6) * i4) >> 8) + i6) << 8) | (peg((((peg - i5) * i4) >> 8) + i5) << 16) | (low(((i & ALPHA_MASK) >>> 24) + i4, BLUE_MASK) << 24);
    }

    private static int blend_darkest(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        return mix(i & BLUE_MASK, low(i & BLUE_MASK, ((i2 & BLUE_MASK) * i3) >> 8), i3) | (low(((-16777216 & i) >>> 24) + i3, BLUE_MASK) << 24) | (mix(i & RED_MASK, low(i & RED_MASK, ((i2 & RED_MASK) >> 8) * i3), i3) & RED_MASK) | (mix(i & GREEN_MASK, low(i & GREEN_MASK, ((i2 & GREEN_MASK) >> 8) * i3), i3) & GREEN_MASK);
    }

    private static int blend_difference(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        int i4 = (i & RED_MASK) >> 16;
        int i5 = (i & GREEN_MASK) >> 8;
        int i6 = i & BLUE_MASK;
        int i7 = (i2 & RED_MASK) >> 16;
        int i8 = (65280 & i2) >> 8;
        int i9 = i2 & BLUE_MASK;
        int i10 = i4 > i7 ? i4 - i7 : i7 - i4;
        return peg(((((i6 > i9 ? i6 - i9 : i9 - i6) - i6) * i3) >> 8) + i6) | (peg(((((i5 > i8 ? i5 - i8 : i8 - i5) - i5) * i3) >> 8) + i5) << 8) | (peg((((i10 - i4) * i3) >> 8) + i4) << 16) | (low(((i & ALPHA_MASK) >>> 24) + i3, BLUE_MASK) << 24);
    }

    private static int blend_dodge(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        int i4 = (i & RED_MASK) >> 16;
        int i5 = (i & GREEN_MASK) >> 8;
        int i6 = i & BLUE_MASK;
        int i7 = (i2 & RED_MASK) >> 16;
        int i8 = (65280 & i2) >> 8;
        int i9 = i2 & BLUE_MASK;
        return peg(((((i9 == 255 ? 255 : peg((i6 << 8) / (255 - i9))) - i6) * i3) >> 8) + i6) | (low(((i & ALPHA_MASK) >>> 24) + i3, BLUE_MASK) << 24) | (peg(((((i7 == 255 ? 255 : peg((i4 << 8) / (255 - i7))) - i4) * i3) >> 8) + i4) << 16) | (peg(((((i8 == 255 ? 255 : peg((i5 << 8) / (255 - i8))) - i5) * i3) >> 8) + i5) << 8);
    }

    private static int blend_exclusion(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        int i4 = (i & RED_MASK) >> 16;
        int i5 = (i & GREEN_MASK) >> 8;
        int i6 = i & BLUE_MASK;
        int i7 = (16711680 & i2) >> 16;
        int i8 = (65280 & i2) >> 8;
        int i9 = i2 & BLUE_MASK;
        int low = low(((i & ALPHA_MASK) >>> 24) + i3, BLUE_MASK) << 24;
        return peg(((i3 * (((i6 + i9) - ((i9 * i6) >> 7)) - i6)) >> 8) + i6) | (peg(i4 + (((((i4 + i7) - ((i7 * i4) >> 7)) - i4) * i3) >> 8)) << 16) | low | (peg(i5 + (((((i5 + i8) - ((i8 * i5) >> 7)) - i5) * i3) >> 8)) << 8);
    }

    private static int blend_hard_light(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        int i4 = (i & RED_MASK) >> 16;
        int i5 = (i & GREEN_MASK) >> 8;
        int i6 = i & BLUE_MASK;
        int i7 = (i2 & RED_MASK) >> 16;
        int i8 = (65280 & i2) >> 8;
        int i9 = i2 & BLUE_MASK;
        int i10 = i7 < 128 ? (i7 * i4) >> 7 : 255 - (((255 - i7) * (255 - i4)) >> 7);
        return peg(((((i9 < 128 ? (i6 * i9) >> 7 : 255 - (((255 - i6) * (255 - i9)) >> 7)) - i6) * i3) >> 8) + i6) | (peg(((((i8 < 128 ? (i5 * i8) >> 7 : 255 - (((255 - i5) * (255 - i8)) >> 7)) - i5) * i3) >> 8) + i5) << 8) | (peg((((i10 - i4) * i3) >> 8) + i4) << 16) | (low(((i & ALPHA_MASK) >>> 24) + i3, BLUE_MASK) << 24);
    }

    private static int blend_lightest(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        return high(i & BLUE_MASK, (i3 * (i2 & BLUE_MASK)) >> 8) | (low(((-16777216 & i) >>> 24) + i3, BLUE_MASK) << 24) | (high(i & RED_MASK, ((i2 & RED_MASK) >> 8) * i3) & RED_MASK) | (high(i & GREEN_MASK, ((i2 & GREEN_MASK) >> 8) * i3) & GREEN_MASK);
    }

    private static int blend_multiply(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        int i4 = (i & RED_MASK) >> 16;
        int i5 = (i & GREEN_MASK) >> 8;
        int i6 = i & BLUE_MASK;
        int low = low(((-16777216 & i) >>> 24) + i3, BLUE_MASK) << 24;
        return peg(((i3 * ((((i2 & BLUE_MASK) * i6) >> 8) - i6)) >> 8) + i6) | (peg(i4 + (((((((16711680 & i2) >> 16) * i4) >> 8) - i4) * i3) >> 8)) << 16) | low | (peg(i5 + (((((((65280 & i2) >> 8) * i5) >> 8) - i5) * i3) >> 8)) << 8);
    }

    private static int blend_overlay(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        int i4 = (i & RED_MASK) >> 16;
        int i5 = (i & GREEN_MASK) >> 8;
        int i6 = i & BLUE_MASK;
        int i7 = (i2 & RED_MASK) >> 16;
        int i8 = (65280 & i2) >> 8;
        int i9 = i2 & BLUE_MASK;
        int i10 = i4 < 128 ? (i7 * i4) >> 7 : 255 - (((255 - i7) * (255 - i4)) >> 7);
        return peg(((((i6 < 128 ? (i6 * i9) >> 7 : 255 - (((255 - i6) * (255 - i9)) >> 7)) - i6) * i3) >> 8) + i6) | (peg(((((i5 < 128 ? (i5 * i8) >> 7 : 255 - (((255 - i5) * (255 - i8)) >> 7)) - i5) * i3) >> 8) + i5) << 8) | (peg((((i10 - i4) * i3) >> 8) + i4) << 16) | (low(((i & ALPHA_MASK) >>> 24) + i3, BLUE_MASK) << 24);
    }

    private static int blend_screen(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        int i4 = (i & RED_MASK) >> 16;
        int i5 = (i & GREEN_MASK) >> 8;
        int i6 = i & BLUE_MASK;
        int low = low(((i & ALPHA_MASK) >>> 24) + i3, BLUE_MASK) << 24;
        return peg(((i3 * ((255 - (((255 - (i2 & BLUE_MASK)) * (255 - i6)) >> 8)) - i6)) >> 8) + i6) | (peg(i4 + ((((255 - (((255 - ((16711680 & i2) >> 16)) * (255 - i4)) >> 8)) - i4) * i3) >> 8)) << 16) | low | (peg(i5 + ((((255 - (((255 - ((65280 & i2) >> 8)) * (255 - i5)) >> 8)) - i5) * i3) >> 8)) << 8);
    }

    private static int blend_soft_light(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        int i4 = (i & RED_MASK) >> 16;
        int i5 = (i & GREEN_MASK) >> 8;
        int i6 = i & BLUE_MASK;
        int i7 = (16711680 & i2) >> 16;
        int i8 = (65280 & i2) >> 8;
        int i9 = i2 & BLUE_MASK;
        int low = low(((i & ALPHA_MASK) >>> 24) + i3, BLUE_MASK) << 24;
        return peg(((i3 * (((((i6 * i9) >> 7) + ((i6 * i6) >> 8)) - ((i9 * (i6 * i6)) >> 15)) - i6)) >> 8) + i6) | (peg(i4 + (((((((i4 * i7) >> 7) + ((i4 * i4) >> 8)) - ((i7 * (i4 * i4)) >> 15)) - i4) * i3) >> 8)) << 16) | low | (peg(i5 + (((((((i5 * i8) >> 7) + ((i5 * i5) >> 8)) - ((i8 * (i5 * i5)) >> 15)) - i5) * i3) >> 8)) << 8);
    }

    private static int blend_sub_pin(int i, int i2) {
        int i3 = (i2 & ALPHA_MASK) >>> 24;
        return high((i & BLUE_MASK) - ((i3 * (i2 & BLUE_MASK)) >> 8), 0) | (low(((-16777216 & i) >>> 24) + i3, BLUE_MASK) << 24) | (high((i & RED_MASK) - (((i2 & RED_MASK) >> 8) * i3), GREEN_MASK) & RED_MASK) | (high((i & GREEN_MASK) - (((i2 & GREEN_MASK) >> 8) * i3), BLUE_MASK) & GREEN_MASK);
    }

    private void blit_resize(PImage pImage, int i, int i2, int i3, int i4, int[] iArr, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
        int i12;
        if (i < 0) {
            i = 0;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        if (i3 > pImage.width) {
            i3 = pImage.width;
        }
        if (i4 > pImage.height) {
            i4 = pImage.height;
        }
        int i13 = i3 - i;
        int i14 = i4 - i2;
        int i15 = i9 - i7;
        int i16 = i10 - i8;
        if (i15 > 0 && i16 > 0 && i13 > 0 && i14 > 0 && i7 < i5 && i8 < i6 && i < pImage.width && i2 < pImage.height) {
            int i17 = (int) ((((float) i13) / ((float) i15)) * 32768.0f);
            int i18 = (int) ((((float) i14) / ((float) i16)) * 32768.0f);
            this.srcXOffset = i7 < 0 ? (-i7) * i17 : PRECISIONF * i;
            this.srcYOffset = i8 < 0 ? (-i8) * i18 : PRECISIONF * i2;
            if (i7 < 0) {
                int i19 = i15 + i7;
                i7 = 0;
                i15 = i19;
            }
            if (i8 < 0) {
                i12 = i16 + i8;
                i8 = 0;
            } else {
                i12 = i16;
            }
            int low = low(i15, i5 - i7);
            int low2 = low(i12, i6 - i8);
            int i20 = (i8 * i5) + i7;
            this.srcBuffer = pImage.pixels;
            this.f44iw = pImage.width;
            this.iw1 = pImage.width - 1;
            this.ih1 = pImage.height - 1;
            switch (i11) {
                case 0:
                    int i21 = i20;
                    for (int i22 = 0; i22 < low2; i22++) {
                        filter_new_scanline();
                        for (int i23 = 0; i23 < low; i23++) {
                            iArr[i21 + i23] = filter_bilinear();
                            this.f48sX += i17;
                        }
                        i21 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case 1:
                    int i24 = i20;
                    for (int i25 = 0; i25 < low2; i25++) {
                        filter_new_scanline();
                        for (int i26 = 0; i26 < low; i26++) {
                            iArr[i24 + i26] = blend_blend(iArr[i24 + i26], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i24 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case 2:
                    int i27 = i20;
                    for (int i28 = 0; i28 < low2; i28++) {
                        filter_new_scanline();
                        for (int i29 = 0; i29 < low; i29++) {
                            iArr[i27 + i29] = blend_add_pin(iArr[i27 + i29], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i27 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case 4:
                    int i30 = i20;
                    for (int i31 = 0; i31 < low2; i31++) {
                        filter_new_scanline();
                        for (int i32 = 0; i32 < low; i32++) {
                            iArr[i30 + i32] = blend_sub_pin(iArr[i30 + i32], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i30 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case 8:
                    int i33 = i20;
                    for (int i34 = 0; i34 < low2; i34++) {
                        filter_new_scanline();
                        for (int i35 = 0; i35 < low; i35++) {
                            iArr[i33 + i35] = blend_lightest(iArr[i33 + i35], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i33 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case 16:
                    int i36 = i20;
                    for (int i37 = 0; i37 < low2; i37++) {
                        filter_new_scanline();
                        for (int i38 = 0; i38 < low; i38++) {
                            iArr[i36 + i38] = blend_darkest(iArr[i36 + i38], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i36 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case 32:
                    int i39 = i20;
                    for (int i40 = 0; i40 < low2; i40++) {
                        filter_new_scanline();
                        for (int i41 = 0; i41 < low; i41++) {
                            iArr[i39 + i41] = blend_difference(iArr[i39 + i41], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i39 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case PConstants.EXCLUSION /*64*/:
                    int i42 = i20;
                    for (int i43 = 0; i43 < low2; i43++) {
                        filter_new_scanline();
                        for (int i44 = 0; i44 < low; i44++) {
                            iArr[i42 + i44] = blend_exclusion(iArr[i42 + i44], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i42 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case PConstants.MULTIPLY /*128*/:
                    int i45 = i20;
                    for (int i46 = 0; i46 < low2; i46++) {
                        filter_new_scanline();
                        for (int i47 = 0; i47 < low; i47++) {
                            iArr[i45 + i47] = blend_multiply(iArr[i45 + i47], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i45 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case 256:
                    int i48 = i20;
                    for (int i49 = 0; i49 < low2; i49++) {
                        filter_new_scanline();
                        for (int i50 = 0; i50 < low; i50++) {
                            iArr[i48 + i50] = blend_screen(iArr[i48 + i50], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i48 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case PConstants.OVERLAY /*512*/:
                    int i51 = i20;
                    for (int i52 = 0; i52 < low2; i52++) {
                        filter_new_scanline();
                        for (int i53 = 0; i53 < low; i53++) {
                            iArr[i51 + i53] = blend_overlay(iArr[i51 + i53], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i51 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case 1024:
                    int i54 = i20;
                    for (int i55 = 0; i55 < low2; i55++) {
                        filter_new_scanline();
                        for (int i56 = 0; i56 < low; i56++) {
                            iArr[i54 + i56] = blend_hard_light(iArr[i54 + i56], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i54 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case PConstants.SOFT_LIGHT /*2048*/:
                    int i57 = i20;
                    for (int i58 = 0; i58 < low2; i58++) {
                        filter_new_scanline();
                        for (int i59 = 0; i59 < low; i59++) {
                            iArr[i57 + i59] = blend_soft_light(iArr[i57 + i59], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i57 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case PConstants.DODGE /*4096*/:
                    int i60 = i20;
                    for (int i61 = 0; i61 < low2; i61++) {
                        filter_new_scanline();
                        for (int i62 = 0; i62 < low; i62++) {
                            iArr[i60 + i62] = blend_dodge(iArr[i60 + i62], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i60 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                case PConstants.BURN /*8192*/:
                    int i63 = i20;
                    for (int i64 = 0; i64 < low2; i64++) {
                        filter_new_scanline();
                        for (int i65 = 0; i65 < low; i65++) {
                            iArr[i63 + i65] = blend_burn(iArr[i63 + i65], filter_bilinear());
                            this.f48sX += i17;
                        }
                        i63 += i5;
                        this.srcYOffset += i18;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private int filter_bilinear() {
        this.fracU = this.f48sX & PREC_MAXVAL;
        this.ifU = 32767 - this.fracU;
        this.f52ul = (this.ifU * this.ifV) >> 15;
        this.f45ll = (this.ifU * this.fracV) >> 15;
        this.f53ur = (this.fracU * this.ifV) >> 15;
        this.f46lr = (this.fracU * this.fracV) >> 15;
        this.f50u1 = this.f48sX >> 15;
        this.f51u2 = low(this.f50u1 + 1, this.iw1);
        this.cUL = this.srcBuffer[this.f54v1 + this.f50u1];
        this.cUR = this.srcBuffer[this.f54v1 + this.f51u2];
        this.cLL = this.srcBuffer[this.f55v2 + this.f50u1];
        this.cLR = this.srcBuffer[this.f55v2 + this.f51u2];
        this.f47r = (((((this.f52ul * ((this.cUL & RED_MASK) >> 16)) + (this.f45ll * ((this.cLL & RED_MASK) >> 16))) + (this.f53ur * ((this.cUR & RED_MASK) >> 16))) + (this.f46lr * ((this.cLR & RED_MASK) >> 16))) << 1) & RED_MASK;
        this.f43g = (((((this.f52ul * (this.cUL & GREEN_MASK)) + (this.f45ll * (this.cLL & GREEN_MASK))) + (this.f53ur * (this.cUR & GREEN_MASK))) + (this.f46lr * (this.cLR & GREEN_MASK))) >>> 15) & GREEN_MASK;
        this.f42b = ((((this.f52ul * (this.cUL & BLUE_MASK)) + (this.f45ll * (this.cLL & BLUE_MASK))) + (this.f53ur * (this.cUR & BLUE_MASK))) + (this.f46lr * (this.cLR & BLUE_MASK))) >>> 15;
        this.f41a = (((((this.f52ul * ((this.cUL & ALPHA_MASK) >>> 24)) + (this.f45ll * ((this.cLL & ALPHA_MASK) >>> 24))) + (this.f53ur * ((this.cUR & ALPHA_MASK) >>> 24))) + (this.f46lr * ((this.cLR & ALPHA_MASK) >>> 24))) << 9) & ALPHA_MASK;
        return this.f41a | this.f47r | this.f43g | this.f42b;
    }

    private void filter_new_scanline() {
        this.f48sX = this.srcXOffset;
        this.fracV = this.srcYOffset & PREC_MAXVAL;
        this.ifV = 32767 - this.fracV;
        this.f54v1 = (this.srcYOffset >> 15) * this.f44iw;
        this.f55v2 = low((this.srcYOffset >> 15) + 1, this.ih1) * this.f44iw;
    }

    private static int high(int i, int i2) {
        return i > i2 ? i : i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0026, code lost:
        if (r3 <= r2) goto L_0x0028;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0028, code lost:
        r2 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        if (r1 <= r0) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        r0 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0015, code lost:
        if (r3 > r2) goto L_0x0017;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001c, code lost:
        if (r1 > r0) goto L_0x001e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean intersect(int r6, int r7, int r8, int r9, int r10, int r11, int r12, int r13) {
        /*
            r5 = this;
            int r0 = r8 - r6
            int r2 = r0 + 1
            int r0 = r9 - r7
            int r0 = r0 + 1
            int r1 = r12 - r10
            int r3 = r1 + 1
            int r1 = r13 - r11
            int r1 = r1 + 1
            if (r10 >= r6) goto L_0x0024
            int r4 = r10 - r6
            int r3 = r3 + r4
            if (r3 <= r2) goto L_0x0028
        L_0x0017:
            if (r11 >= r7) goto L_0x002a
            int r3 = r11 - r7
            int r1 = r1 + r3
            if (r1 <= r0) goto L_0x002e
        L_0x001e:
            if (r2 <= 0) goto L_0x0030
            if (r0 <= 0) goto L_0x0030
            r0 = 1
        L_0x0023:
            return r0
        L_0x0024:
            int r2 = r2 + r6
            int r2 = r2 - r10
            if (r3 > r2) goto L_0x0017
        L_0x0028:
            r2 = r3
            goto L_0x0017
        L_0x002a:
            int r0 = r0 + r7
            int r0 = r0 - r11
            if (r1 > r0) goto L_0x001e
        L_0x002e:
            r0 = r1
            goto L_0x001e
        L_0x0030:
            r0 = 0
            goto L_0x0023
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.core.PImage.intersect(int, int, int, int, int, int, int, int):boolean");
    }

    protected static PImage loadTIFF(byte[] bArr) {
        if (bArr[42] == bArr[102] && bArr[43] == bArr[103]) {
            byte b = (bArr[31] & 255) | ((bArr[30] & 255) << 8);
            byte b2 = (bArr[43] & 255) | ((bArr[42] & 255) << 8);
            byte b3 = (bArr[117] & 255) | ((bArr[114] & 255) << 24) | ((bArr[115] & 255) << 16) | ((bArr[116] & 255) << 8);
            if (b3 != b * b2 * 3) {
                System.err.println("Error: Processing can only read its own TIFF files. (" + b + ", " + b2 + ")");
                return null;
            }
            int i = 0;
            while (i < TIFF_HEADER.length) {
                if (i == 30 || i == 31 || i == 42 || i == 43 || i == 102 || i == 103 || i == 114 || i == 115 || i == 116 || i == 117 || bArr[i] == TIFF_HEADER[i]) {
                    i++;
                } else {
                    System.err.println("Error: Processing can only read its own TIFF files. (" + i + ")");
                    return null;
                }
            }
            PImage pImage = new PImage(b, b2, 1);
            int i2 = PGL.SRC_COLOR;
            int i3 = b3 / 3;
            for (int i4 = 0; i4 < i3; i4++) {
                int i5 = i2 + 1;
                int i6 = i5 + 1;
                byte b4 = ((bArr[i5] & 255) << 8) | ((bArr[i2] & 255) << 16) | LinePath.SEG_MOVETO;
                i2 = i6 + 1;
                pImage.pixels[i4] = (bArr[i6] & 255) | b4;
            }
            return pImage;
        }
        System.err.println(TIFF_ERROR);
        return null;
    }

    private static int low(int i, int i2) {
        return i < i2 ? i : i2;
    }

    private static int mix(int i, int i2, int i3) {
        return (((i2 - i) * i3) >> 8) + i;
    }

    private static int peg(int i) {
        if (i < 0) {
            return 0;
        }
        return i > 255 ? BLUE_MASK : i;
    }

    public void blend(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        blend(this, i, i2, i3, i4, i5, i6, i7, i8, i9);
    }

    public void blend(PImage pImage, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        int i10 = i + i3;
        int i11 = i2 + i4;
        int i12 = i5 + i7;
        int i13 = i6 + i8;
        loadPixels();
        if (pImage != this) {
            pImage.loadPixels();
            blit_resize(pImage, i, i2, i10, i11, this.pixels, this.width, this.height, i5, i6, i12, i13, i9);
        } else if (intersect(i, i2, i10, i11, i5, i6, i12, i13)) {
            blit_resize(get(i, i2, i10 - i, i11 - i2), 0, 0, (i10 - i) - 1, (i11 - i2) - 1, this.pixels, this.width, this.height, i5, i6, i12, i13, i9);
        } else {
            blit_resize(pImage, i, i2, i10, i11, this.pixels, this.width, this.height, i5, i6, i12, i13, i9);
        }
        updatePixels();
    }

    /* access modifiers changed from: protected */
    public void blurARGB(float f) {
        int i;
        int i2;
        int i3;
        int i4;
        int length = this.pixels.length;
        int[] iArr = new int[length];
        int[] iArr2 = new int[length];
        int[] iArr3 = new int[length];
        int[] iArr4 = new int[length];
        int i5 = 0;
        buildBlurKernel(f);
        for (int i6 = 0; i6 < this.height; i6++) {
            for (int i7 = 0; i7 < this.width; i7++) {
                int i8 = 0;
                int i9 = i7 - this.blurRadius;
                if (i9 < 0) {
                    i4 = -i9;
                    i9 = 0;
                } else if (i9 >= this.width) {
                    break;
                } else {
                    i4 = 0;
                }
                int i10 = 0;
                int i11 = 0;
                int i12 = 0;
                int i13 = 0;
                while (i4 < this.blurKernelSize && i9 < this.width) {
                    int i14 = this.pixels[i9 + i5];
                    int[] iArr5 = this.blurMult[i4];
                    i8 += iArr5[(-16777216 & i14) >>> 24];
                    i12 += iArr5[(16711680 & i14) >> 16];
                    i11 += iArr5[(65280 & i14) >> 8];
                    i10 += iArr5[i14 & BLUE_MASK];
                    i13 += this.blurKernel[i4];
                    i9++;
                    i4++;
                }
                int i15 = i5 + i7;
                iArr4[i15] = i8 / i13;
                iArr[i15] = i12 / i13;
                iArr2[i15] = i11 / i13;
                iArr3[i15] = i10 / i13;
            }
            i5 += this.width;
        }
        int i16 = 0;
        int i17 = -this.blurRadius;
        int i18 = i17 * this.width;
        for (int i19 = 0; i19 < this.height; i19++) {
            for (int i20 = 0; i20 < this.width; i20++) {
                int i21 = 0;
                if (i17 < 0) {
                    i = -i17;
                    i3 = i;
                    i2 = i20;
                } else if (i17 >= this.height) {
                    break;
                } else {
                    i = 0;
                    i2 = i20 + i18;
                    i3 = i17;
                }
                int i22 = 0;
                int i23 = 0;
                int i24 = 0;
                int i25 = 0;
                while (i < this.blurKernelSize && i3 < this.height) {
                    int[] iArr6 = this.blurMult[i];
                    i21 += iArr6[iArr4[i2]];
                    i24 += iArr6[iArr[i2]];
                    i23 += iArr6[iArr2[i2]];
                    i22 += iArr6[iArr3[i2]];
                    i25 += this.blurKernel[i];
                    i3++;
                    i2 += this.width;
                    i++;
                }
                this.pixels[i20 + i16] = ((i21 / i25) << 24) | ((i24 / i25) << 16) | ((i23 / i25) << 8) | (i22 / i25);
            }
            i16 += this.width;
            i18 += this.width;
            i17++;
        }
    }

    /* access modifiers changed from: protected */
    public void blurAlpha(float f) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int[] iArr = new int[this.pixels.length];
        buildBlurKernel(f);
        int i6 = 0;
        for (int i7 = 0; i7 < this.height; i7++) {
            for (int i8 = 0; i8 < this.width; i8++) {
                int i9 = i8 - this.blurRadius;
                if (i9 < 0) {
                    i5 = -i9;
                    i4 = 0;
                } else if (i9 >= this.width) {
                    break;
                } else {
                    i4 = i9;
                    i5 = 0;
                }
                int i10 = 0;
                int i11 = 0;
                while (i5 < this.blurKernelSize && i4 < this.width) {
                    i10 += this.blurMult[i5][this.pixels[i4 + i6] & BLUE_MASK];
                    i11 += this.blurKernel[i5];
                    i4++;
                    i5++;
                }
                iArr[i6 + i8] = i10 / i11;
            }
            i6 += this.width;
        }
        int i12 = -this.blurRadius;
        int i13 = this.width * i12;
        int i14 = i12;
        int i15 = 0;
        for (int i16 = 0; i16 < this.height; i16++) {
            for (int i17 = 0; i17 < this.width; i17++) {
                if (i14 < 0) {
                    i3 = -i14;
                    i = i3;
                    i2 = i17;
                } else if (i14 >= this.height) {
                    break;
                } else {
                    i = i14;
                    i2 = i17 + i13;
                    i3 = 0;
                }
                int i18 = 0;
                int i19 = 0;
                while (i3 < this.blurKernelSize && i < this.height) {
                    i18 += this.blurMult[i3][iArr[i2]];
                    i19 += this.blurKernel[i3];
                    i++;
                    i2 += this.width;
                    i3++;
                }
                this.pixels[i17 + i15] = i18 / i19;
            }
            i15 += this.width;
            i13 += this.width;
            i14++;
        }
    }

    /* access modifiers changed from: protected */
    public void blurRGB(float f) {
        int i;
        int i2;
        int i3;
        int i4;
        int[] iArr = new int[this.pixels.length];
        int[] iArr2 = new int[this.pixels.length];
        int[] iArr3 = new int[this.pixels.length];
        int i5 = 0;
        buildBlurKernel(f);
        for (int i6 = 0; i6 < this.height; i6++) {
            for (int i7 = 0; i7 < this.width; i7++) {
                int i8 = 0;
                int i9 = i7 - this.blurRadius;
                if (i9 < 0) {
                    i4 = -i9;
                    i9 = 0;
                } else if (i9 >= this.width) {
                    break;
                } else {
                    i4 = 0;
                }
                int i10 = 0;
                int i11 = 0;
                int i12 = 0;
                while (i4 < this.blurKernelSize && i9 < this.width) {
                    int i13 = this.pixels[i9 + i5];
                    int[] iArr4 = this.blurMult[i4];
                    i11 += iArr4[(16711680 & i13) >> 16];
                    i10 += iArr4[(65280 & i13) >> 8];
                    i8 += iArr4[i13 & BLUE_MASK];
                    i12 += this.blurKernel[i4];
                    i9++;
                    i4++;
                }
                int i14 = i5 + i7;
                iArr[i14] = i11 / i12;
                iArr2[i14] = i10 / i12;
                iArr3[i14] = i8 / i12;
            }
            i5 += this.width;
        }
        int i15 = 0;
        int i16 = -this.blurRadius;
        int i17 = i16 * this.width;
        for (int i18 = 0; i18 < this.height; i18++) {
            for (int i19 = 0; i19 < this.width; i19++) {
                int i20 = 0;
                if (i16 < 0) {
                    i = -i16;
                    i3 = i;
                    i2 = i19;
                } else if (i16 >= this.height) {
                    break;
                } else {
                    i = 0;
                    i2 = i19 + i17;
                    i3 = i16;
                }
                int i21 = 0;
                int i22 = 0;
                int i23 = 0;
                while (i < this.blurKernelSize && i3 < this.height) {
                    int[] iArr5 = this.blurMult[i];
                    i22 += iArr5[iArr[i2]];
                    i21 += iArr5[iArr2[i2]];
                    i20 += iArr5[iArr3[i2]];
                    i23 += this.blurKernel[i];
                    i3++;
                    i2 += this.width;
                    i++;
                }
                this.pixels[i19 + i15] = -16777216 | ((i22 / i23) << 16) | ((i21 / i23) << 8) | (i20 / i23);
            }
            i15 += this.width;
            i17 += this.width;
            i16++;
        }
    }

    /* access modifiers changed from: protected */
    public void buildBlurKernel(float f) {
        int i = 248;
        int i2 = (int) (3.5f * f);
        if (i2 < 1) {
            i = 1;
        } else if (i2 < 248) {
            i = i2;
        }
        if (this.blurRadius != i) {
            this.blurRadius = i;
            this.blurKernelSize = (this.blurRadius + 1) << 1;
            this.blurKernel = new int[this.blurKernelSize];
            this.blurMult = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.blurKernelSize, 256});
            int i3 = i - 1;
            int i4 = 1;
            while (i4 < i) {
                int[] iArr = this.blurKernel;
                int i5 = i + i4;
                int i6 = i3 * i3;
                this.blurKernel[i3] = i6;
                iArr[i5] = i6;
                int[] iArr2 = this.blurMult[i + i4];
                int i7 = i3 - 1;
                int[] iArr3 = this.blurMult[i3];
                for (int i8 = 0; i8 < 256; i8++) {
                    int i9 = i6 * i8;
                    iArr3[i8] = i9;
                    iArr2[i8] = i9;
                }
                i4++;
                i3 = i7;
            }
            int i10 = i * i;
            this.blurKernel[i] = i10;
            int[] iArr4 = this.blurMult[i];
            for (int i11 = 0; i11 < 256; i11++) {
                iArr4[i11] = i10 * i11;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void checkAlpha() {
        if (this.pixels != null) {
            for (int i : this.pixels) {
                if ((i & ALPHA_MASK) != -16777216) {
                    this.format = 2;
                    return;
                }
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return get();
    }

    public void copy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        blend(this, i, i2, i3, i4, i5, i6, i7, i8, 0);
    }

    public void copy(PImage pImage, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        blend(pImage, i, i2, i3, i4, i5, i6, i7, i8, 0);
    }

    /* access modifiers changed from: protected */
    public void dilate(boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = 0;
        int length = this.pixels.length;
        int[] iArr = new int[length];
        if (!z) {
            while (i7 < length) {
                int i8 = i7 + this.width;
                int i9 = i7;
                while (i9 < i8) {
                    int i10 = this.pixels[i9];
                    int i11 = i9 - 1;
                    int i12 = i9 + 1;
                    int i13 = i9 - this.width;
                    int i14 = this.width + i9;
                    int i15 = i11 < i7 ? i9 : i11;
                    if (i12 >= i8) {
                        i12 = i9;
                    }
                    if (i13 < 0) {
                        i13 = 0;
                    }
                    if (i14 >= length) {
                        i14 = i9;
                    }
                    int i16 = this.pixels[i13];
                    int i17 = this.pixels[i15];
                    int i18 = this.pixels[i14];
                    int i19 = this.pixels[i12];
                    int i20 = (((i10 >> 16) & BLUE_MASK) * 77) + (((i10 >> 8) & BLUE_MASK) * 151) + ((i10 & BLUE_MASK) * 28);
                    int i21 = (((i17 >> 16) & BLUE_MASK) * 77) + (((i17 >> 8) & BLUE_MASK) * 151) + ((i17 & BLUE_MASK) * 28);
                    int i22 = ((i19 & BLUE_MASK) * 28) + (((i19 >> 16) & BLUE_MASK) * 77) + (((i19 >> 8) & BLUE_MASK) * 151);
                    int i23 = (((i16 >> 16) & BLUE_MASK) * 77) + (((i16 >> 8) & BLUE_MASK) * 151) + ((i16 & BLUE_MASK) * 28);
                    int i24 = (((i18 >> 16) & BLUE_MASK) * 77) + (((i18 >> 8) & BLUE_MASK) * 151) + ((i18 & BLUE_MASK) * 28);
                    if (i21 > i20) {
                        i4 = i17;
                    } else {
                        i21 = i20;
                        i4 = i10;
                    }
                    if (i22 > i21) {
                        i5 = i19;
                    } else {
                        i22 = i21;
                        i5 = i4;
                    }
                    if (i23 > i22) {
                        i22 = i23;
                        i6 = i16;
                    } else {
                        i6 = i5;
                    }
                    if (i24 <= i22) {
                        i18 = i6;
                    }
                    int i25 = i9 + 1;
                    iArr[i9] = i18;
                    i9 = i25;
                }
                i7 = i9;
            }
        } else {
            while (i7 < length) {
                int i26 = i7 + this.width;
                int i27 = i7;
                while (i27 < i26) {
                    int i28 = this.pixels[i27];
                    int i29 = i27 - 1;
                    int i30 = i27 + 1;
                    int i31 = i27 - this.width;
                    int i32 = this.width + i27;
                    int i33 = i29 < i7 ? i27 : i29;
                    if (i30 >= i26) {
                        i30 = i27;
                    }
                    if (i31 < 0) {
                        i31 = 0;
                    }
                    if (i32 >= length) {
                        i32 = i27;
                    }
                    int i34 = this.pixels[i31];
                    int i35 = this.pixels[i33];
                    int i36 = this.pixels[i32];
                    int i37 = this.pixels[i30];
                    int i38 = (((i28 >> 16) & BLUE_MASK) * 77) + (((i28 >> 8) & BLUE_MASK) * 151) + ((i28 & BLUE_MASK) * 28);
                    int i39 = (((i35 >> 16) & BLUE_MASK) * 77) + (((i35 >> 8) & BLUE_MASK) * 151) + ((i35 & BLUE_MASK) * 28);
                    int i40 = ((i37 & BLUE_MASK) * 28) + (((i37 >> 16) & BLUE_MASK) * 77) + (((i37 >> 8) & BLUE_MASK) * 151);
                    int i41 = (((i34 >> 16) & BLUE_MASK) * 77) + (((i34 >> 8) & BLUE_MASK) * 151) + ((i34 & BLUE_MASK) * 28);
                    int i42 = (((i36 >> 16) & BLUE_MASK) * 77) + (((i36 >> 8) & BLUE_MASK) * 151) + ((i36 & BLUE_MASK) * 28);
                    if (i39 < i38) {
                        i = i35;
                    } else {
                        i39 = i38;
                        i = i28;
                    }
                    if (i40 < i39) {
                        i2 = i37;
                    } else {
                        i40 = i39;
                        i2 = i;
                    }
                    if (i41 < i40) {
                        i40 = i41;
                        i3 = i34;
                    } else {
                        i3 = i2;
                    }
                    if (i42 >= i40) {
                        i36 = i3;
                    }
                    int i43 = i27 + 1;
                    iArr[i27] = i36;
                    i27 = i43;
                }
                i7 = i27;
            }
        }
        System.arraycopy(iArr, 0, this.pixels, 0, length);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x008c, code lost:
        r7.format = 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void filter(int r8) {
        /*
            r7 = this;
            r6 = 1
            r5 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r0 = 0
            r7.loadPixels()
            switch(r8) {
                case 1: goto L_0x007d;
                case 2: goto L_0x000a;
                case 3: goto L_0x000a;
                case 4: goto L_0x000a;
                case 5: goto L_0x000a;
                case 6: goto L_0x000a;
                case 7: goto L_0x000a;
                case 8: goto L_0x000a;
                case 9: goto L_0x000a;
                case 10: goto L_0x000a;
                case 11: goto L_0x000e;
                case 12: goto L_0x0016;
                case 13: goto L_0x0063;
                case 14: goto L_0x000a;
                case 15: goto L_0x0075;
                case 16: goto L_0x0090;
                case 17: goto L_0x0099;
                case 18: goto L_0x009e;
                default: goto L_0x000a;
            }
        L_0x000a:
            r7.updatePixels()
            return
        L_0x000e:
            r0 = 11
            r1 = 1065353216(0x3f800000, float:1.0)
            r7.filter(r0, r1)
            goto L_0x000a
        L_0x0016:
            int r1 = r7.format
            r2 = 4
            if (r1 != r2) goto L_0x0037
        L_0x001b:
            int[] r1 = r7.pixels
            int r1 = r1.length
            if (r0 >= r1) goto L_0x0034
            int[] r1 = r7.pixels
            r1 = r1[r0]
            int r1 = 255 - r1
            int[] r2 = r7.pixels
            int r3 = r1 << 16
            r3 = r3 | r5
            int r4 = r1 << 8
            r3 = r3 | r4
            r1 = r1 | r3
            r2[r0] = r1
            int r0 = r0 + 1
            goto L_0x001b
        L_0x0034:
            r7.format = r6
            goto L_0x000a
        L_0x0037:
            int[] r1 = r7.pixels
            int r1 = r1.length
            if (r0 >= r1) goto L_0x000a
            int[] r1 = r7.pixels
            r1 = r1[r0]
            int r2 = r1 >> 16
            r2 = r2 & 255(0xff, float:3.57E-43)
            int r2 = r2 * 77
            int r3 = r1 >> 8
            r3 = r3 & 255(0xff, float:3.57E-43)
            int r3 = r3 * 151
            int r2 = r2 + r3
            r3 = r1 & 255(0xff, float:3.57E-43)
            int r3 = r3 * 28
            int r2 = r2 + r3
            int r2 = r2 >> 8
            int[] r3 = r7.pixels
            r1 = r1 & r5
            int r4 = r2 << 16
            r1 = r1 | r4
            int r4 = r2 << 8
            r1 = r1 | r4
            r1 = r1 | r2
            r3[r0] = r1
            int r0 = r0 + 1
            goto L_0x0037
        L_0x0063:
            int[] r1 = r7.pixels
            int r1 = r1.length
            if (r0 >= r1) goto L_0x000a
            int[] r1 = r7.pixels
            r2 = r1[r0]
            r3 = 16777215(0xffffff, float:2.3509886E-38)
            r2 = r2 ^ r3
            r1[r0] = r2
            int r0 = r0 + 1
            goto L_0x0063
        L_0x0075:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "Use filter(POSTERIZE, int levels) instead of filter(POSTERIZE)"
            r0.<init>(r1)
            throw r0
        L_0x007d:
            int[] r1 = r7.pixels
            int r1 = r1.length
            if (r0 >= r1) goto L_0x008c
            int[] r1 = r7.pixels
            r2 = r1[r0]
            r2 = r2 | r5
            r1[r0] = r2
            int r0 = r0 + 1
            goto L_0x007d
        L_0x008c:
            r7.format = r6
            goto L_0x000a
        L_0x0090:
            r0 = 16
            r1 = 1056964608(0x3f000000, float:0.5)
            r7.filter(r0, r1)
            goto L_0x000a
        L_0x0099:
            r7.dilate(r6)
            goto L_0x000a
        L_0x009e:
            r7.dilate(r0)
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.core.PImage.filter(int):void");
    }

    public void filter(int i, float f) {
        loadPixels();
        switch (i) {
            case 11:
                if (this.format != 4) {
                    if (this.format != 2) {
                        blurRGB(f);
                        break;
                    } else {
                        blurARGB(f);
                        break;
                    }
                } else {
                    blurAlpha(f);
                    break;
                }
            case 12:
                throw new RuntimeException("Use filter(GRAY) instead of filter(GRAY, param)");
            case 13:
                throw new RuntimeException("Use filter(INVERT) instead of filter(INVERT, param)");
            case 14:
                throw new RuntimeException("Use filter(OPAQUE) instead of filter(OPAQUE, param)");
            case 15:
                int i2 = (int) f;
                if (i2 >= 2 && i2 <= 255) {
                    int i3 = i2 - 1;
                    for (int i4 = 0; i4 < this.pixels.length; i4++) {
                        this.pixels[i4] = (((((((this.pixels[i4] >> 16) & BLUE_MASK) * i2) >> 8) * BLUE_MASK) / i3) << 16) | (this.pixels[i4] & ALPHA_MASK) | (((((((this.pixels[i4] >> 8) & BLUE_MASK) * i2) >> 8) * BLUE_MASK) / i3) << 8) | (((((this.pixels[i4] & BLUE_MASK) * i2) >> 8) * BLUE_MASK) / i3);
                    }
                    break;
                } else {
                    throw new RuntimeException("Levels must be between 2 and 255 for filter(POSTERIZE, levels)");
                }
            case 16:
                int i5 = (int) (255.0f * f);
                for (int i6 = 0; i6 < this.pixels.length; i6++) {
                    this.pixels[i6] = (Math.max((this.pixels[i6] & RED_MASK) >> 16, Math.max((this.pixels[i6] & GREEN_MASK) >> 8, this.pixels[i6] & BLUE_MASK)) < i5 ? 0 : 16777215) | (this.pixels[i6] & ALPHA_MASK);
                }
                break;
            case 17:
                throw new RuntimeException("Use filter(ERODE) instead of filter(ERODE, param)");
            case 18:
                throw new RuntimeException("Use filter(DILATE) instead of filter(DILATE, param)");
        }
        updatePixels();
    }

    public int get(int i, int i2) {
        if (i < 0 || i2 < 0 || i >= this.width || i2 >= this.height) {
            return 0;
        }
        if (this.pixels == null) {
            return this.bitmap.getPixel(i, i2);
        }
        switch (this.format) {
            case 1:
                return this.pixels[(this.width * i2) + i] | ALPHA_MASK;
            case 2:
                return this.pixels[(this.width * i2) + i];
            case 4:
                return (this.pixels[(this.width * i2) + i] << 24) | 16777215;
            default:
                return 0;
        }
    }

    public PImage get() {
        return get(0, 0, this.width, this.height);
    }

    public PImage get(int i, int i2, int i3, int i4) {
        boolean z;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        boolean z2;
        int i11;
        int i12 = 0;
        if (i < 0) {
            i6 = i3 + i;
            i5 = -i;
            z = true;
            i7 = 0;
        } else {
            z = false;
            i5 = 0;
            i6 = i3;
            i7 = i;
        }
        if (i2 < 0) {
            i8 = -i2;
            i9 = i4 + i2;
            i10 = 0;
            z = true;
        } else {
            i8 = 0;
            i9 = i4;
            i10 = i2;
        }
        if (i7 + i6 > this.width) {
            i6 = this.width - i7;
            z = true;
        }
        if (i10 + i9 > this.height) {
            i11 = this.height - i10;
            z2 = true;
        } else {
            z2 = z;
            i11 = i9;
        }
        int i13 = i6 < 0 ? 0 : i6;
        if (i11 >= 0) {
            i12 = i11;
        }
        int i14 = this.format;
        if (z2 && this.format == 1) {
            i14 = 2;
        }
        PImage pImage = new PImage(i3, i4, i14);
        pImage.parent = this.parent;
        if (i13 > 0 && i12 > 0) {
            getImpl(i7, i10, i13, i12, pImage, i5, i8);
        }
        return pImage;
    }

    /* access modifiers changed from: protected */
    public void getImpl(int i, int i2, int i3, int i4, PImage pImage, int i5, int i6) {
        if (this.pixels == null) {
            this.bitmap.getPixels(pImage.pixels, (pImage.width * i6) + i5, pImage.width, i, i2, i3, i4);
            return;
        }
        int i7 = (this.width * i2) + i;
        int i8 = (pImage.width * i6) + i5;
        for (int i9 = 0; i9 < i4; i9++) {
            System.arraycopy(this.pixels, i7, pImage.pixels, i8, i3);
            i7 += this.width;
            i8 += pImage.width;
        }
    }

    public int getModifiedX1() {
        return this.mx1;
    }

    public int getModifiedX2() {
        return this.mx2;
    }

    public int getModifiedY1() {
        return this.my1;
    }

    public int getModifiedY2() {
        return this.my2;
    }

    public Object getNative() {
        return this.bitmap;
    }

    public void init(int i, int i2, int i3) {
        this.width = i;
        this.height = i2;
        this.pixels = new int[(i * i2)];
        this.format = i3;
    }

    public boolean isModified() {
        return this.modified;
    }

    public void loadPixels() {
        if (this.pixels == null || this.pixels.length != this.width * this.height) {
            this.pixels = new int[(this.width * this.height)];
        }
        if (this.bitmap != null) {
            this.bitmap.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
        }
        if (this.parent != null) {
            Object initCache = this.parent.f2g.initCache(this);
            if (initCache != null) {
                Method method = null;
                try {
                    method = initCache.getClass().getMethod("loadPixels", new Class[]{int[].class});
                } catch (Exception e) {
                }
                if (method != null) {
                    try {
                        method.invoke(initCache, new Object[]{this.pixels});
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    public void mask(PImage pImage) {
        if (pImage.pixels == null) {
            pImage.loadPixels();
            mask(pImage.pixels);
            pImage.pixels = null;
            return;
        }
        mask(pImage.pixels);
    }

    public void mask(int[] iArr) {
        loadPixels();
        if (iArr.length != this.pixels.length) {
            throw new RuntimeException("The PImage used with mask() must be the same size as the applet.");
        }
        for (int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = ((iArr[i] & BLUE_MASK) << 24) | (this.pixels[i] & 16777215);
        }
        this.format = 2;
        updatePixels();
    }

    public void resize(int i, int i2) {
        if (i > 0 || i2 > 0) {
            if (i == 0) {
                i = (int) ((((float) i2) / ((float) this.height)) * ((float) this.width));
            } else if (i2 == 0) {
                i2 = (int) ((((float) i) / ((float) this.width)) * ((float) this.height));
            }
            this.bitmap = Bitmap.createScaledBitmap(this.bitmap, i, i2, true);
            this.width = i;
            this.height = i2;
            updatePixels();
            return;
        }
        throw new IllegalArgumentException("width or height must be > 0 for resize");
    }

    public boolean save(String str) {
        boolean z = false;
        loadPixels();
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(this.parent.createOutput(str), PGL.COLOR_BUFFER_BIT);
            String lowerCase = str.toLowerCase();
            String substring = lowerCase.substring(lowerCase.lastIndexOf(46) + 1);
            if (substring.equals("jpg") || substring.equals("jpeg")) {
                z = Bitmap.createBitmap(this.pixels, this.width, this.height, Config.ARGB_8888).compress(CompressFormat.JPEG, 100, bufferedOutputStream);
            } else if (substring.equals("png")) {
                z = Bitmap.createBitmap(this.pixels, this.width, this.height, Config.ARGB_8888).compress(CompressFormat.PNG, 100, bufferedOutputStream);
            } else if (substring.equals("tga")) {
                z = saveTGA(bufferedOutputStream);
            } else {
                if (!substring.equals("tif") && !substring.equals("tiff")) {
                    str = str + ".tif";
                }
                z = saveTIFF(bufferedOutputStream);
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!z) {
            System.err.println("Could not write the image to " + str);
        }
        return z;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0073 A[Catch:{ IOException -> 0x01a4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0101 A[Catch:{ IOException -> 0x01a4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x012d A[Catch:{ IOException -> 0x01a4 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean saveTGA(java.io.OutputStream r12) {
        /*
            r11 = this;
            r5 = 16
            r10 = 128(0x80, float:1.794E-43)
            r9 = 2
            r1 = 1
            r2 = 0
            r0 = 18
            byte[] r0 = new byte[r0]
            int r3 = r11.format
            r4 = 4
            if (r3 != r4) goto L_0x0084
            r3 = 11
            r0[r9] = r3
            r3 = 8
            r0[r5] = r3
            r3 = 17
            r4 = 40
            r0[r3] = r4
        L_0x001e:
            r3 = 12
            int r4 = r11.width
            r4 = r4 & 255(0xff, float:3.57E-43)
            byte r4 = (byte) r4
            r0[r3] = r4
            r3 = 13
            int r4 = r11.width
            int r4 = r4 >> 8
            byte r4 = (byte) r4
            r0[r3] = r4
            r3 = 14
            int r4 = r11.height
            r4 = r4 & 255(0xff, float:3.57E-43)
            byte r4 = (byte) r4
            r0[r3] = r4
            r3 = 15
            int r4 = r11.height
            int r4 = r4 >> 8
            byte r4 = (byte) r4
            r0[r3] = r4
            r12.write(r0)     // Catch:{ IOException -> 0x01a4 }
            int r0 = r11.height     // Catch:{ IOException -> 0x01a4 }
            int r3 = r11.width     // Catch:{ IOException -> 0x01a4 }
            int r6 = r0 * r3
            r0 = 128(0x80, float:1.794E-43)
            int[] r7 = new int[r0]     // Catch:{ IOException -> 0x01a4 }
            int r0 = r11.format     // Catch:{ IOException -> 0x01a4 }
            r3 = 4
            if (r0 != r3) goto L_0x01b3
            r5 = r2
        L_0x0055:
            if (r5 >= r6) goto L_0x01a0
            r0 = 0
            int[] r3 = r11.pixels     // Catch:{ IOException -> 0x01a4 }
            r3 = r3[r5]     // Catch:{ IOException -> 0x01a4 }
            r4 = r3 & 255(0xff, float:3.57E-43)
            r7[r0] = r4     // Catch:{ IOException -> 0x01a4 }
            r3 = r1
        L_0x0061:
            int r0 = r5 + r3
            if (r0 >= r6) goto L_0x01b0
            int[] r0 = r11.pixels     // Catch:{ IOException -> 0x01a4 }
            int r8 = r5 + r3
            r0 = r0[r8]     // Catch:{ IOException -> 0x01a4 }
            r0 = r0 & 255(0xff, float:3.57E-43)
            if (r4 != r0) goto L_0x0071
            if (r3 != r10) goto L_0x00b5
        L_0x0071:
            if (r3 <= r1) goto L_0x00b3
            r0 = r1
        L_0x0074:
            if (r0 == 0) goto L_0x00b8
            int r0 = r3 + -1
            r0 = r0 | 128(0x80, float:1.794E-43)
            r12.write(r0)     // Catch:{ IOException -> 0x01a4 }
            r12.write(r4)     // Catch:{ IOException -> 0x01a4 }
            r0 = r3
        L_0x0081:
            int r0 = r0 + r5
            r5 = r0
            goto L_0x0055
        L_0x0084:
            int r3 = r11.format
            if (r3 != r1) goto L_0x0097
            r3 = 10
            r0[r9] = r3
            r3 = 24
            r0[r5] = r3
            r3 = 17
            r4 = 32
            r0[r3] = r4
            goto L_0x001e
        L_0x0097:
            int r3 = r11.format
            if (r3 != r9) goto L_0x00ab
            r3 = 10
            r0[r9] = r3
            r3 = 32
            r0[r5] = r3
            r3 = 17
            r4 = 40
            r0[r3] = r4
            goto L_0x001e
        L_0x00ab:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "Image format not recognized inside save()"
            r0.<init>(r1)
            throw r0
        L_0x00b3:
            r0 = r2
            goto L_0x0074
        L_0x00b5:
            int r3 = r3 + 1
            goto L_0x0061
        L_0x00b8:
            r0 = r1
            r3 = r4
        L_0x00ba:
            int r4 = r5 + r0
            if (r4 >= r6) goto L_0x00d7
            int[] r4 = r11.pixels     // Catch:{ IOException -> 0x01a4 }
            int r8 = r5 + r0
            r4 = r4[r8]     // Catch:{ IOException -> 0x01a4 }
            r4 = r4 & 255(0xff, float:3.57E-43)
            if (r3 == r4) goto L_0x00ca
            if (r0 < r10) goto L_0x00cd
        L_0x00ca:
            r8 = 3
            if (r0 >= r8) goto L_0x00d3
        L_0x00cd:
            r7[r0] = r4     // Catch:{ IOException -> 0x01a4 }
            int r0 = r0 + 1
            r3 = r4
            goto L_0x00ba
        L_0x00d3:
            if (r3 != r4) goto L_0x00d7
            int r0 = r0 + -2
        L_0x00d7:
            int r3 = r0 + -1
            r12.write(r3)     // Catch:{ IOException -> 0x01a4 }
            r3 = r2
        L_0x00dd:
            if (r3 >= r0) goto L_0x0081
            r4 = r7[r3]     // Catch:{ IOException -> 0x01a4 }
            r12.write(r4)     // Catch:{ IOException -> 0x01a4 }
            int r3 = r3 + 1
            goto L_0x00dd
        L_0x00e7:
            if (r5 >= r6) goto L_0x01a0
            r0 = 0
            int[] r3 = r11.pixels     // Catch:{ IOException -> 0x01a4 }
            r4 = r3[r5]     // Catch:{ IOException -> 0x01a4 }
            r7[r0] = r4     // Catch:{ IOException -> 0x01a4 }
            r3 = r1
        L_0x00f1:
            int r0 = r5 + r3
            if (r0 >= r6) goto L_0x01ad
            int[] r0 = r11.pixels     // Catch:{ IOException -> 0x01a4 }
            int r8 = r5 + r3
            r0 = r0[r8]     // Catch:{ IOException -> 0x01a4 }
            if (r4 != r0) goto L_0x00ff
            if (r3 != r10) goto L_0x012f
        L_0x00ff:
            if (r3 <= r1) goto L_0x012d
            r0 = r1
        L_0x0102:
            if (r0 == 0) goto L_0x0132
            int r0 = r3 + -1
            r0 = r0 | 128(0x80, float:1.794E-43)
            r12.write(r0)     // Catch:{ IOException -> 0x01a4 }
            r0 = r4 & 255(0xff, float:3.57E-43)
            r12.write(r0)     // Catch:{ IOException -> 0x01a4 }
            int r0 = r4 >> 8
            r0 = r0 & 255(0xff, float:3.57E-43)
            r12.write(r0)     // Catch:{ IOException -> 0x01a4 }
            int r0 = r4 >> 16
            r0 = r0 & 255(0xff, float:3.57E-43)
            r12.write(r0)     // Catch:{ IOException -> 0x01a4 }
            int r0 = r11.format     // Catch:{ IOException -> 0x01a4 }
            if (r0 != r9) goto L_0x01aa
            int r0 = r4 >>> 24
            r0 = r0 & 255(0xff, float:3.57E-43)
            r12.write(r0)     // Catch:{ IOException -> 0x01a4 }
            r0 = r3
        L_0x012a:
            int r0 = r0 + r5
            r5 = r0
            goto L_0x00e7
        L_0x012d:
            r0 = r2
            goto L_0x0102
        L_0x012f:
            int r3 = r3 + 1
            goto L_0x00f1
        L_0x0132:
            r0 = r1
            r3 = r4
        L_0x0134:
            int r4 = r5 + r0
            if (r4 >= r6) goto L_0x015a
            int[] r4 = r11.pixels     // Catch:{ IOException -> 0x01a4 }
            int r8 = r5 + r0
            r4 = r4[r8]     // Catch:{ IOException -> 0x01a4 }
            if (r3 == r4) goto L_0x0142
            if (r0 < r10) goto L_0x0145
        L_0x0142:
            r4 = 3
            if (r0 >= r4) goto L_0x0150
        L_0x0145:
            int[] r3 = r11.pixels     // Catch:{ IOException -> 0x01a4 }
            int r4 = r5 + r0
            r3 = r3[r4]     // Catch:{ IOException -> 0x01a4 }
            r7[r0] = r3     // Catch:{ IOException -> 0x01a4 }
            int r0 = r0 + 1
            goto L_0x0134
        L_0x0150:
            int[] r4 = r11.pixels     // Catch:{ IOException -> 0x01a4 }
            int r8 = r5 + r0
            r4 = r4[r8]     // Catch:{ IOException -> 0x01a4 }
            if (r3 != r4) goto L_0x015a
            int r0 = r0 + -2
        L_0x015a:
            int r3 = r0 + -1
            r12.write(r3)     // Catch:{ IOException -> 0x01a4 }
            int r3 = r11.format     // Catch:{ IOException -> 0x01a4 }
            if (r3 != r9) goto L_0x0185
            r3 = r2
        L_0x0164:
            if (r3 >= r0) goto L_0x012a
            r4 = r7[r3]     // Catch:{ IOException -> 0x01a4 }
            r8 = r4 & 255(0xff, float:3.57E-43)
            r12.write(r8)     // Catch:{ IOException -> 0x01a4 }
            int r8 = r4 >> 8
            r8 = r8 & 255(0xff, float:3.57E-43)
            r12.write(r8)     // Catch:{ IOException -> 0x01a4 }
            int r8 = r4 >> 16
            r8 = r8 & 255(0xff, float:3.57E-43)
            r12.write(r8)     // Catch:{ IOException -> 0x01a4 }
            int r4 = r4 >>> 24
            r4 = r4 & 255(0xff, float:3.57E-43)
            r12.write(r4)     // Catch:{ IOException -> 0x01a4 }
            int r3 = r3 + 1
            goto L_0x0164
        L_0x0185:
            r3 = r2
        L_0x0186:
            if (r3 >= r0) goto L_0x012a
            r4 = r7[r3]     // Catch:{ IOException -> 0x01a4 }
            r8 = r4 & 255(0xff, float:3.57E-43)
            r12.write(r8)     // Catch:{ IOException -> 0x01a4 }
            int r8 = r4 >> 8
            r8 = r8 & 255(0xff, float:3.57E-43)
            r12.write(r8)     // Catch:{ IOException -> 0x01a4 }
            int r4 = r4 >> 16
            r4 = r4 & 255(0xff, float:3.57E-43)
            r12.write(r4)     // Catch:{ IOException -> 0x01a4 }
            int r3 = r3 + 1
            goto L_0x0186
        L_0x01a0:
            r12.flush()     // Catch:{ IOException -> 0x01a4 }
        L_0x01a3:
            return r1
        L_0x01a4:
            r0 = move-exception
            r0.printStackTrace()
            r1 = r2
            goto L_0x01a3
        L_0x01aa:
            r0 = r3
            goto L_0x012a
        L_0x01ad:
            r0 = r2
            goto L_0x0102
        L_0x01b0:
            r0 = r2
            goto L_0x0074
        L_0x01b3:
            r5 = r2
            goto L_0x00e7
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.core.PImage.saveTGA(java.io.OutputStream):boolean");
    }

    /* access modifiers changed from: protected */
    public boolean saveTIFF(OutputStream outputStream) {
        try {
            byte[] bArr = new byte[PGL.SRC_COLOR];
            System.arraycopy(TIFF_HEADER, 0, bArr, 0, TIFF_HEADER.length);
            bArr[30] = (byte) ((this.width >> 8) & BLUE_MASK);
            bArr[31] = (byte) (this.width & BLUE_MASK);
            byte b = (byte) ((this.height >> 8) & BLUE_MASK);
            bArr[102] = b;
            bArr[42] = b;
            byte b2 = (byte) (this.height & BLUE_MASK);
            bArr[103] = b2;
            bArr[43] = b2;
            int i = this.width * this.height * 3;
            bArr[114] = (byte) ((i >> 24) & BLUE_MASK);
            bArr[115] = (byte) ((i >> 16) & BLUE_MASK);
            bArr[116] = (byte) ((i >> 8) & BLUE_MASK);
            bArr[117] = (byte) (i & BLUE_MASK);
            outputStream.write(bArr);
            for (int i2 = 0; i2 < this.pixels.length; i2++) {
                outputStream.write((this.pixels[i2] >> 16) & BLUE_MASK);
                outputStream.write((this.pixels[i2] >> 8) & BLUE_MASK);
                outputStream.write(this.pixels[i2] & BLUE_MASK);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void set(int i, int i2, int i3) {
        if (this.pixels == null) {
            this.bitmap.setPixel(i, i2, i3);
        } else if (i >= 0 && i2 >= 0 && i < this.width && i2 < this.height) {
            this.pixels[(this.width * i2) + i] = i3;
            updatePixelsImpl(i, i2, 1, 1);
        }
    }

    public void set(int i, int i2, PImage pImage) {
        int i3;
        int i4;
        int i5;
        int i6;
        if (pImage.format == 4) {
            throw new IllegalArgumentException("set() not available for ALPHA images");
        }
        int i7 = pImage.width;
        int i8 = pImage.height;
        if (i < 0) {
            i3 = 0 - i;
            i7 += i;
            i4 = 0;
        } else {
            i3 = 0;
            i4 = i;
        }
        if (i2 < 0) {
            i5 = 0 - i2;
            i8 += i2;
            i6 = 0;
        } else {
            i5 = 0;
            i6 = i2;
        }
        if (i4 + i7 > this.width) {
            i7 = this.width - i4;
        }
        int i9 = i6 + i8 > this.height ? this.height - i6 : i8;
        if (i7 > 0 && i9 > 0) {
            setImpl(pImage, i3, i5, i7, i9, i4, i6);
        }
    }

    /* access modifiers changed from: protected */
    public void setImpl(PImage pImage, int i, int i2, int i3, int i4, int i5, int i6) {
        if (pImage.pixels == null) {
            pImage.loadPixels();
        }
        if (this.pixels == null) {
            if (!this.bitmap.isMutable()) {
                this.bitmap = this.bitmap.copy(Config.ARGB_8888, true);
            }
            this.bitmap.setPixels(pImage.pixels, (pImage.width * i2) + i, pImage.width, i5, i6, i3, i4);
            return;
        }
        int i7 = (pImage.width * i2) + i;
        int i8 = (this.width * i6) + i5;
        for (int i9 = i2; i9 < i2 + i4; i9++) {
            System.arraycopy(pImage.pixels, i7, this.pixels, i8, i3);
            i7 += pImage.width;
            i8 += this.width;
        }
        updatePixelsImpl(i5, i6, i3, i4);
    }

    public void setModified() {
        this.modified = true;
    }

    public void setModified(boolean z) {
        this.modified = z;
    }

    public void updatePixels() {
        updatePixelsImpl(0, 0, this.width, this.height);
    }

    public void updatePixels(int i, int i2, int i3, int i4) {
        updatePixelsImpl(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void updatePixelsImpl(int i, int i2, int i3, int i4) {
        int i5 = i + i3;
        int i6 = i2 + i4;
        if (!this.modified) {
            this.mx1 = PApplet.max(0, i);
            this.mx2 = PApplet.min(this.width - 1, i5);
            this.my1 = PApplet.max(0, i2);
            this.my2 = PApplet.min(this.height - 1, i6);
            this.modified = true;
            return;
        }
        if (i < this.mx1) {
            this.mx1 = PApplet.max(0, i);
        }
        if (i > this.mx2) {
            this.mx2 = PApplet.min(this.width - 1, i);
        }
        if (i2 < this.my1) {
            this.my1 = PApplet.max(0, i2);
        }
        if (i2 > this.my2) {
            this.my2 = i2;
        }
        if (i5 < this.mx1) {
            this.mx1 = PApplet.max(0, i5);
        }
        if (i5 > this.mx2) {
            this.mx2 = PApplet.min(this.width - 1, i5);
        }
        if (i6 < this.my1) {
            this.my1 = PApplet.max(0, i6);
        }
        if (i6 > this.my2) {
            this.my2 = PApplet.min(this.height - 1, i6);
        }
    }
}

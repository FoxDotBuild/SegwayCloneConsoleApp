package processing.core;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.WeakHashMap;
import processing.core.PFont.Glyph;
import processing.opengl.PGL;
import processing.opengl.PShader;

public class PGraphics extends PImage implements PConstants {

    /* renamed from: A */
    public static final int f10A = 6;

    /* renamed from: AB */
    public static final int f11AB = 27;

    /* renamed from: AG */
    public static final int f12AG = 26;

    /* renamed from: AR */
    public static final int f13AR = 25;

    /* renamed from: B */
    public static final int f14B = 5;
    public static final int BEEN_LIT = 35;

    /* renamed from: DA */
    public static final int f15DA = 6;

    /* renamed from: DB */
    public static final int f16DB = 5;
    protected static final int DEFAULT_STROKE_CAP = 2;
    protected static final int DEFAULT_STROKE_JOIN = 8;
    protected static final float DEFAULT_STROKE_WEIGHT = 1.0f;
    static final int DEFAULT_VERTICES = 512;

    /* renamed from: DG */
    public static final int f17DG = 4;

    /* renamed from: DR */
    public static final int f18DR = 3;

    /* renamed from: EB */
    public static final int f19EB = 34;
    public static final int EDGE = 12;

    /* renamed from: EG */
    public static final int f20EG = 33;

    /* renamed from: ER */
    public static final int f21ER = 32;

    /* renamed from: G */
    public static final int f22G = 4;
    public static final int HAS_NORMAL = 36;
    static final int MATRIX_STACK_DEPTH = 32;
    protected static final int NORMAL_MODE_AUTO = 0;
    protected static final int NORMAL_MODE_SHAPE = 1;
    protected static final int NORMAL_MODE_VERTEX = 2;

    /* renamed from: NX */
    public static final int f23NX = 9;

    /* renamed from: NY */
    public static final int f24NY = 10;

    /* renamed from: NZ */
    public static final int f25NZ = 11;

    /* renamed from: R */
    public static final int f26R = 3;

    /* renamed from: SA */
    public static final int f27SA = 16;

    /* renamed from: SB */
    public static final int f28SB = 15;

    /* renamed from: SG */
    public static final int f29SG = 14;
    public static final int SHINE = 31;
    protected static final int SINCOS_LENGTH = 720;
    protected static final float SINCOS_PRECISION = 0.5f;
    public static final int SPB = 30;
    public static final int SPG = 29;
    public static final int SPR = 28;

    /* renamed from: SR */
    public static final int f30SR = 13;
    static final int STYLE_STACK_DEPTH = 64;

    /* renamed from: SW */
    public static final int f31SW = 17;

    /* renamed from: TX */
    public static final int f32TX = 18;

    /* renamed from: TY */
    public static final int f33TY = 19;

    /* renamed from: TZ */
    public static final int f34TZ = 20;

    /* renamed from: U */
    public static final int f35U = 7;

    /* renamed from: V */
    public static final int f36V = 8;
    public static final int VERTEX_FIELD_COUNT = 37;

    /* renamed from: VW */
    public static final int f37VW = 24;

    /* renamed from: VX */
    public static final int f38VX = 21;

    /* renamed from: VY */
    public static final int f39VY = 22;

    /* renamed from: VZ */
    public static final int f40VZ = 23;
    protected static final float[] cosLUT = new float[SINCOS_LENGTH];
    static float[] lerpColorHSB1;
    static float[] lerpColorHSB2;
    static float[] lerpColorHSB3;
    protected static final float[] sinLUT = new float[SINCOS_LENGTH];
    protected static HashMap<String, Object> warnings;
    public float ambientB;
    public int ambientColor;
    public float ambientG;
    public float ambientR;
    protected boolean autoNormal;
    protected float backgroundA;
    protected int backgroundAi;
    protected boolean backgroundAlpha;
    protected float backgroundB;
    protected int backgroundBi;
    public int backgroundColor = -3355444;
    protected float backgroundG;
    protected int backgroundGi;
    protected float backgroundR;
    protected int backgroundRi;
    protected PMatrix3D bezierBasisInverse;
    protected PMatrix3D bezierBasisMatrix = new PMatrix3D(-1.0f, 3.0f, -3.0f, DEFAULT_STROKE_WEIGHT, 3.0f, -6.0f, 3.0f, 0.0f, -3.0f, 3.0f, 0.0f, 0.0f, DEFAULT_STROKE_WEIGHT, 0.0f, 0.0f, 0.0f);
    public int bezierDetail = 20;
    protected PMatrix3D bezierDrawMatrix;
    protected boolean bezierInited = false;
    public Bitmap bitmap;
    int cacheHsbKey;
    float[] cacheHsbValue = new float[3];
    protected WeakHashMap<PImage, Object> cacheMap = new WeakHashMap<>();
    protected float calcA;
    protected int calcAi;
    protected boolean calcAlpha;
    protected float calcB;
    protected int calcBi;
    protected int calcColor;
    protected float calcG;
    protected int calcGi;
    protected float calcR;
    protected int calcRi;
    public int colorMode;
    public float colorModeA;
    boolean colorModeDefault;
    boolean colorModeScale;
    public float colorModeX;
    public float colorModeY;
    public float colorModeZ;
    protected PMatrix3D curveBasisMatrix;
    protected int curveDetail = 20;
    protected PMatrix3D curveDrawMatrix;
    protected boolean curveInited = false;
    public float curveTightness = 0.0f;
    protected PMatrix3D curveToBezierMatrix;
    protected int curveVertexCount;
    protected float[][] curveVertices;
    public boolean edge = true;
    public int ellipseMode;
    public float emissiveB;
    public int emissiveColor;
    public float emissiveG;
    public float emissiveR;
    public boolean fill;
    protected float fillA;
    protected int fillAi;
    protected boolean fillAlpha;
    protected float fillB;
    protected int fillBi;
    public int fillColor = -1;
    protected float fillG;
    protected int fillGi;
    protected float fillR;
    protected int fillRi;
    protected int height1;
    protected boolean[] hints = new boolean[10];
    public int imageMode = 0;
    protected int normalMode;
    public float normalX;
    public float normalY;
    public float normalZ;
    protected String path;
    public int pixelCount;
    protected boolean primarySurface;
    protected int quality;
    protected PGraphics raw;
    public int rectMode;
    public boolean setAmbient;
    protected boolean settingsInited;
    protected int shape;
    public int shapeMode;
    public float shininess;
    public boolean smooth = false;
    public float specularB;
    public int specularColor;
    public float specularG;
    public float specularR;
    public int sphereDetailU = 0;
    public int sphereDetailV = 0;
    protected float[] sphereX;
    protected float[] sphereY;
    protected float[] sphereZ;
    public boolean stroke;
    protected float strokeA;
    protected int strokeAi;
    protected boolean strokeAlpha;
    protected float strokeB;
    protected int strokeBi;
    public int strokeCap = 2;
    public int strokeColor = PImage.ALPHA_MASK;
    protected float strokeG;
    protected int strokeGi;
    public int strokeJoin = 8;
    protected float strokeR;
    protected int strokeRi;
    public float strokeWeight = DEFAULT_STROKE_WEIGHT;
    PStyle[] styleStack = new PStyle[64];
    int styleStackDepth;
    public int textAlign = 21;
    public int textAlignY = 0;
    protected int textBreakCount;
    protected int[] textBreakStart;
    protected int[] textBreakStop;
    protected char[] textBuffer = new char[PConstants.BURN];
    public PFont textFont;
    public float textLeading;
    public int textMode = 4;
    public float textSize;
    protected char[] textWidthBuffer = new char[PConstants.BURN];
    public PImage textureImage = null;
    public int textureMode = 2;
    public float textureU;
    public float textureV;
    public boolean tint;
    protected float tintA;
    protected int tintAi;
    protected boolean tintAlpha;
    protected float tintB;
    protected int tintBi;
    public int tintColor;
    protected float tintG;
    protected int tintGi;
    protected float tintR;
    protected int tintRi;
    protected int vertexCount;
    protected float[][] vertices = ((float[][]) Array.newInstance(Float.TYPE, new int[]{512, 37}));
    protected int width1;

    static {
        for (int i = 0; i < SINCOS_LENGTH; i++) {
            sinLUT[i] = (float) Math.sin((double) (((float) i) * 0.017453292f * 0.5f));
            cosLUT[i] = (float) Math.cos((double) (((float) i) * 0.017453292f * 0.5f));
        }
    }

    public static int lerpColor(int i, int i2, float f, int i3) {
        if (i3 == 1) {
            float f2 = (float) ((i >> 24) & PImage.BLUE_MASK);
            float f3 = (float) ((i >> 16) & PImage.BLUE_MASK);
            float f4 = (float) ((i >> 8) & PImage.BLUE_MASK);
            float f5 = (float) (i & PImage.BLUE_MASK);
            return (((int) (f2 + ((((float) ((i2 >> 24) & PImage.BLUE_MASK)) - f2) * f))) << 24) | (((int) (f3 + ((((float) ((i2 >> 16) & PImage.BLUE_MASK)) - f3) * f))) << 16) | (((int) (((((float) ((i2 >> 8) & PImage.BLUE_MASK)) - f4) * f) + f4)) << 8) | ((int) (((((float) (i2 & PImage.BLUE_MASK)) - f5) * f) + f5));
        } else if (i3 != 3) {
            return 0;
        } else {
            if (lerpColorHSB1 == null) {
                lerpColorHSB1 = new float[3];
                lerpColorHSB2 = new float[3];
                lerpColorHSB3 = new float[3];
            }
            float f6 = (float) ((i >> 24) & PImage.BLUE_MASK);
            int i4 = ((int) (f6 + ((((float) ((i2 >> 24) & PImage.BLUE_MASK)) - f6) * f))) << 24;
            Color.RGBToHSV((i >> 16) & PImage.BLUE_MASK, (i >> 8) & PImage.BLUE_MASK, i & PImage.BLUE_MASK, lerpColorHSB1);
            Color.RGBToHSV((i2 >> 16) & PImage.BLUE_MASK, (i2 >> 8) & PImage.BLUE_MASK, i2 & PImage.BLUE_MASK, lerpColorHSB2);
            lerpColorHSB3[0] = PApplet.lerp(lerpColorHSB1[0], lerpColorHSB2[0], f);
            lerpColorHSB3[1] = PApplet.lerp(lerpColorHSB1[1], lerpColorHSB2[1], f);
            lerpColorHSB3[2] = PApplet.lerp(lerpColorHSB1[2], lerpColorHSB2[2], f);
            return Color.HSVToColor(i4, lerpColorHSB3);
        }
    }

    public static void showDepthWarning(String str) {
        showWarning(str + "() can only be used with a renderer that " + "supports 3D, such as P3D or OPENGL.");
    }

    public static void showDepthWarningXYZ(String str) {
        showWarning(str + "() with x, y, and z coordinates " + "can only be used with a renderer that " + "supports 3D, such as P3D or OPENGL. " + "Use a version without a z-coordinate instead.");
    }

    public static void showException(String str) {
        throw new RuntimeException(str);
    }

    public static void showMethodWarning(String str) {
        showWarning(str + "() is not available with this renderer.");
    }

    public static void showMissingWarning(String str) {
        showWarning(str + "(), or this particular variation of it, " + "is not available with this renderer.");
    }

    public static void showVariationWarning(String str) {
        showWarning(str + " is not available with this renderer.");
    }

    public static void showWarning(String str) {
        if (warnings == null) {
            warnings = new HashMap<>();
        }
        if (!warnings.containsKey(str)) {
            System.err.println(str);
            warnings.put(str, new Object());
        }
    }

    public static void showWarning(String str, Object... objArr) {
        showWarning(String.format(str, objArr));
    }

    private boolean textSentence(char[] cArr, int i, int i2, float f, float f2) {
        int i3 = i;
        int i4 = i;
        int i5 = i;
        float f3 = 0.0f;
        while (i3 <= i2) {
            if (cArr[i3] == ' ' || i3 == i2) {
                float textWidthImpl = textWidthImpl(cArr, i4, i3);
                if (f3 + textWidthImpl > f) {
                    if (f3 != 0.0f) {
                        textSentenceBreak(i5, i4);
                        i3 = i4;
                        while (i3 < i2 && cArr[i3] == ' ') {
                            i3++;
                        }
                    } else {
                        do {
                            i3--;
                            if (i3 == i4) {
                                return false;
                            }
                        } while (textWidthImpl(cArr, i4, i3) > f);
                        textSentenceBreak(i5, i3);
                    }
                    i4 = i3;
                    i5 = i3;
                    f3 = 0.0f;
                } else if (i3 == i2) {
                    textSentenceBreak(i5, i3);
                    i3++;
                } else {
                    f3 += textWidthImpl + f2;
                    i4 = i3 + 1;
                    i3++;
                }
            } else {
                i3++;
            }
        }
        return true;
    }

    private void textSentenceBreak(int i, int i2) {
        if (this.textBreakCount == this.textBreakStart.length) {
            this.textBreakStart = PApplet.expand(this.textBreakStart);
            this.textBreakStop = PApplet.expand(this.textBreakStop);
        }
        this.textBreakStart[this.textBreakCount] = i;
        this.textBreakStop[this.textBreakCount] = i2;
        this.textBreakCount++;
    }

    /* access modifiers changed from: protected */
    public void allocate() {
    }

    public final float alpha(int i) {
        float f = (float) ((i >> 24) & PImage.BLUE_MASK);
        return this.colorModeA == 255.0f ? f : (f / 255.0f) * this.colorModeA;
    }

    public void ambient(float f) {
        colorCalc(f);
        ambientFromCalc();
    }

    public void ambient(float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        ambientFromCalc();
    }

    public void ambient(int i) {
        colorCalc(i);
        ambientFromCalc();
    }

    /* access modifiers changed from: protected */
    public void ambientFromCalc() {
        this.ambientColor = this.calcColor;
        this.ambientR = this.calcR;
        this.ambientG = this.calcG;
        this.ambientB = this.calcB;
        this.setAmbient = true;
    }

    public void ambientLight(float f, float f2, float f3) {
        showMethodWarning("ambientLight");
    }

    public void ambientLight(float f, float f2, float f3, float f4, float f5, float f6) {
        showMethodWarning("ambientLight");
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6) {
        showMissingWarning("applyMatrix");
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        showMissingWarning("applyMatrix");
    }

    public void applyMatrix(PMatrix2D pMatrix2D) {
        applyMatrix(pMatrix2D.m00, pMatrix2D.m01, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, pMatrix2D.m12);
    }

    public void applyMatrix(PMatrix3D pMatrix3D) {
        applyMatrix(pMatrix3D.m00, pMatrix3D.m01, pMatrix3D.m02, pMatrix3D.m03, pMatrix3D.m10, pMatrix3D.m11, pMatrix3D.m12, pMatrix3D.m13, pMatrix3D.m20, pMatrix3D.m21, pMatrix3D.m22, pMatrix3D.m23, pMatrix3D.m30, pMatrix3D.m31, pMatrix3D.m32, pMatrix3D.m33);
    }

    public void applyMatrix(PMatrix pMatrix) {
        if (pMatrix instanceof PMatrix2D) {
            applyMatrix((PMatrix2D) pMatrix);
        } else if (pMatrix instanceof PMatrix3D) {
            applyMatrix((PMatrix3D) pMatrix);
        }
    }

    public void arc(float f, float f2, float f3, float f4, float f5, float f6) {
        arc(f, f2, f3, f4, f5, f6, 0);
    }

    public void arc(float f, float f2, float f3, float f4, float f5, float f6, int i) {
        float f7;
        float f8;
        float f9;
        float f10;
        if (this.ellipseMode == 1) {
            f7 = f4 - f2;
            f8 = f3 - f;
            f9 = f2;
            f10 = f;
        } else if (this.ellipseMode == 2) {
            f7 = f4 * 2.0f;
            f8 = f3 * 2.0f;
            f9 = f2 - f4;
            f10 = f - f3;
        } else if (this.ellipseMode == 3) {
            f7 = f4;
            f8 = f3;
            f9 = f2 - (f4 / 2.0f);
            f10 = f - (f3 / 2.0f);
        } else {
            f7 = f4;
            f8 = f3;
            f9 = f2;
            f10 = f;
        }
        if (!Float.isInfinite(f5) && !Float.isInfinite(f6) && f6 > f5) {
            float f11 = f6;
            float f12 = f5;
            while (f12 < 0.0f) {
                f12 += 6.2831855f;
                f11 += 6.2831855f;
            }
            if (f11 - f12 > 6.2831855f) {
                f12 = 0.0f;
                f11 = 6.2831855f;
            }
            arcImpl(f10, f9, f8, f7, f12, f11, i);
        }
    }

    /* access modifiers changed from: protected */
    public void arcImpl(float f, float f2, float f3, float f4, float f5, float f6, int i) {
        showMissingWarning("arc");
    }

    public void background(float f) {
        colorCalc(f);
        backgroundFromCalc();
    }

    public void background(float f, float f2) {
        if (this.format == 1) {
            background(f);
            return;
        }
        colorCalc(f, f2);
        backgroundFromCalc();
    }

    public void background(float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        backgroundFromCalc();
    }

    public void background(float f, float f2, float f3, float f4) {
        colorCalc(f, f2, f3, f4);
        backgroundFromCalc();
    }

    public void background(int i) {
        colorCalc(i);
        backgroundFromCalc();
    }

    public void background(int i, float f) {
        colorCalc(i, f);
        backgroundFromCalc();
    }

    public void background(PImage pImage) {
        if (pImage.width != this.width || pImage.height != this.height) {
            throw new RuntimeException(PConstants.ERROR_BACKGROUND_IMAGE_SIZE);
        } else if (pImage.format == 1 || pImage.format == 2) {
            this.backgroundColor = 0;
            backgroundImpl(pImage);
        } else {
            throw new RuntimeException(PConstants.ERROR_BACKGROUND_IMAGE_FORMAT);
        }
    }

    /* access modifiers changed from: protected */
    public void backgroundFromCalc() {
        this.backgroundR = this.calcR;
        this.backgroundG = this.calcG;
        this.backgroundB = this.calcB;
        this.backgroundA = this.format == 1 ? this.colorModeA : this.calcA;
        this.backgroundRi = this.calcRi;
        this.backgroundGi = this.calcGi;
        this.backgroundBi = this.calcBi;
        this.backgroundAi = this.format == 1 ? PImage.BLUE_MASK : this.calcAi;
        this.backgroundAlpha = this.format == 1 ? false : this.calcAlpha;
        this.backgroundColor = this.calcColor;
        backgroundImpl();
    }

    /* access modifiers changed from: protected */
    public void backgroundImpl() {
        pushStyle();
        pushMatrix();
        resetMatrix();
        fill(this.backgroundColor);
        rect(0.0f, 0.0f, (float) this.width, (float) this.height);
        popMatrix();
        popStyle();
    }

    /* access modifiers changed from: protected */
    public void backgroundImpl(PImage pImage) {
        set(0, 0, pImage);
    }

    public void beginCamera() {
        showMethodWarning("beginCamera");
    }

    public void beginContour() {
        showMissingWarning("beginContour");
    }

    public void beginDraw() {
    }

    public PGL beginPGL() {
        showMethodWarning("beginPGL");
        return null;
    }

    public void beginRaw(PGraphics pGraphics) {
        this.raw = pGraphics;
        pGraphics.beginDraw();
    }

    public void beginShape() {
        beginShape(20);
    }

    public void beginShape(int i) {
        this.shape = i;
    }

    public void bezier(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        beginShape();
        vertex(f, f2);
        bezierVertex(f3, f4, f5, f6, f7, f8);
        endShape();
    }

    public void bezier(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        beginShape();
        vertex(f, f2, f3);
        bezierVertex(f4, f5, f6, f7, f8, f9, f10, f11, f12);
        endShape();
    }

    public void bezierDetail(int i) {
        this.bezierDetail = i;
        if (this.bezierDrawMatrix == null) {
            this.bezierDrawMatrix = new PMatrix3D();
        }
        splineForward(i, this.bezierDrawMatrix);
        this.bezierDrawMatrix.apply(this.bezierBasisMatrix);
    }

    /* access modifiers changed from: protected */
    public void bezierInit() {
        bezierDetail(this.bezierDetail);
        this.bezierInited = true;
    }

    /* access modifiers changed from: protected */
    public void bezierInitCheck() {
        if (!this.bezierInited) {
            bezierInit();
        }
    }

    public float bezierPoint(float f, float f2, float f3, float f4, float f5) {
        float f6 = DEFAULT_STROKE_WEIGHT - f5;
        return (f6 * 3.0f * f3 * f5 * f5) + (f * f6 * f6 * f6) + (3.0f * f2 * f5 * f6 * f6) + (f4 * f5 * f5 * f5);
    }

    public float bezierTangent(float f, float f2, float f3, float f4, float f5) {
        return (3.0f * f5 * f5 * ((((-f) + (3.0f * f2)) - (3.0f * f3)) + f4)) + (6.0f * f5 * ((f - (2.0f * f2)) + f3)) + (((-f) + f2) * 3.0f);
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        bezierInitCheck();
        bezierVertexCheck();
        PMatrix3D pMatrix3D = this.bezierDrawMatrix;
        float[] fArr = this.vertices[this.vertexCount - 1];
        float f7 = fArr[0];
        float f8 = fArr[1];
        float f9 = (pMatrix3D.m10 * f7) + (pMatrix3D.m11 * f) + (pMatrix3D.m12 * f3) + (pMatrix3D.m13 * f5);
        float f10 = (pMatrix3D.m20 * f7) + (pMatrix3D.m21 * f) + (pMatrix3D.m22 * f3) + (pMatrix3D.m23 * f5);
        float f11 = (pMatrix3D.m30 * f7) + (pMatrix3D.m31 * f) + (pMatrix3D.m32 * f3) + (pMatrix3D.m33 * f5);
        float f12 = (pMatrix3D.m13 * f6) + (pMatrix3D.m10 * f8) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f4);
        float f13 = (pMatrix3D.m20 * f8) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f4) + (pMatrix3D.m23 * f6);
        float f14 = (pMatrix3D.m30 * f8) + (pMatrix3D.m31 * f2) + (pMatrix3D.m32 * f4) + (pMatrix3D.m33 * f6);
        for (int i = 0; i < this.bezierDetail; i++) {
            f7 += f9;
            f9 += f10;
            f10 += f11;
            f8 += f12;
            f12 += f13;
            f13 += f14;
            vertex(f7, f8);
        }
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        bezierInitCheck();
        bezierVertexCheck();
        PMatrix3D pMatrix3D = this.bezierDrawMatrix;
        float[] fArr = this.vertices[this.vertexCount - 1];
        float f10 = fArr[0];
        float f11 = fArr[1];
        float f12 = fArr[2];
        float f13 = (pMatrix3D.m10 * f10) + (pMatrix3D.m11 * f) + (pMatrix3D.m12 * f4) + (pMatrix3D.m13 * f7);
        float f14 = (pMatrix3D.m20 * f10) + (pMatrix3D.m21 * f) + (pMatrix3D.m22 * f4) + (pMatrix3D.m23 * f7);
        float f15 = (pMatrix3D.m30 * f10) + (pMatrix3D.m31 * f) + (pMatrix3D.m32 * f4) + (pMatrix3D.m33 * f7);
        float f16 = (pMatrix3D.m10 * f11) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f5) + (pMatrix3D.m13 * f8);
        float f17 = (pMatrix3D.m20 * f11) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f5) + (pMatrix3D.m23 * f8);
        float f18 = (pMatrix3D.m30 * f11) + (pMatrix3D.m31 * f2) + (pMatrix3D.m32 * f5) + (pMatrix3D.m33 * f8);
        float f19 = (pMatrix3D.m13 * f9) + (pMatrix3D.m10 * f12) + (pMatrix3D.m11 * f3) + (pMatrix3D.m12 * f6);
        float f20 = (pMatrix3D.m20 * f12) + (pMatrix3D.m21 * f3) + (pMatrix3D.m22 * f6) + (pMatrix3D.m23 * f9);
        float f21 = (pMatrix3D.m30 * f12) + (pMatrix3D.m31 * f3) + (pMatrix3D.m32 * f6) + (pMatrix3D.m33 * f9);
        for (int i = 0; i < this.bezierDetail; i++) {
            f10 += f13;
            f13 += f14;
            f14 += f15;
            f11 += f16;
            f16 += f17;
            f17 += f18;
            f12 += f19;
            f19 += f20;
            f20 += f21;
            vertex(f10, f11, f12);
        }
    }

    /* access modifiers changed from: protected */
    public void bezierVertexCheck() {
        bezierVertexCheck(this.shape, this.vertexCount);
    }

    /* access modifiers changed from: protected */
    public void bezierVertexCheck(int i, int i2) {
        if (i == 0 || i != 20) {
            throw new RuntimeException("beginShape() or beginShape(POLYGON) must be used before bezierVertex() or quadraticVertex()");
        } else if (i2 == 0) {
            throw new RuntimeException("vertex() must be used at least oncebefore bezierVertex() or quadraticVertex()");
        }
    }

    public void blendMode(int i) {
        showMissingWarning("blendMode");
    }

    public final float blue(int i) {
        float f = (float) (i & PImage.BLUE_MASK);
        return this.colorModeDefault ? f : (f / 255.0f) * this.colorModeZ;
    }

    public void box(float f) {
        box(f, f, f);
    }

    public void box(float f, float f2, float f3) {
        float f4 = (-f) / 2.0f;
        float f5 = f / 2.0f;
        float f6 = (-f2) / 2.0f;
        float f7 = f2 / 2.0f;
        float f8 = (-f3) / 2.0f;
        float f9 = f3 / 2.0f;
        beginShape(17);
        normal(0.0f, 0.0f, DEFAULT_STROKE_WEIGHT);
        vertex(f4, f6, f8);
        vertex(f5, f6, f8);
        vertex(f5, f7, f8);
        vertex(f4, f7, f8);
        normal(DEFAULT_STROKE_WEIGHT, 0.0f, 0.0f);
        vertex(f5, f6, f8);
        vertex(f5, f6, f9);
        vertex(f5, f7, f9);
        vertex(f5, f7, f8);
        normal(0.0f, 0.0f, -1.0f);
        vertex(f5, f6, f9);
        vertex(f4, f6, f9);
        vertex(f4, f7, f9);
        vertex(f5, f7, f9);
        normal(-1.0f, 0.0f, 0.0f);
        vertex(f4, f6, f9);
        vertex(f4, f6, f8);
        vertex(f4, f7, f8);
        vertex(f4, f7, f9);
        normal(0.0f, DEFAULT_STROKE_WEIGHT, 0.0f);
        vertex(f4, f6, f9);
        vertex(f5, f6, f9);
        vertex(f5, f6, f8);
        vertex(f4, f6, f8);
        normal(0.0f, -1.0f, 0.0f);
        vertex(f4, f7, f8);
        vertex(f5, f7, f8);
        vertex(f5, f7, f9);
        vertex(f4, f7, f9);
        endShape();
    }

    public void breakShape() {
        showWarning("This renderer cannot currently handle concave shapes, or shapes with holes.");
    }

    public final float brightness(int i) {
        if (i != this.cacheHsbKey) {
            Color.RGBToHSV((i >> 16) & PImage.BLUE_MASK, (i >> 8) & PImage.BLUE_MASK, i & PImage.BLUE_MASK, this.cacheHsbValue);
            this.cacheHsbKey = i;
        }
        return this.cacheHsbValue[2] * this.colorModeZ;
    }

    public void camera() {
        showMissingWarning("camera");
    }

    public void camera(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        showMissingWarning("camera");
    }

    public boolean canDraw() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void checkSettings() {
        if (!this.settingsInited) {
            defaultSettings();
        }
    }

    public void clear() {
        background(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void clip(float f, float f2, float f3, float f4) {
        if (this.imageMode == 0) {
            if (f3 < 0.0f) {
                f += f3;
                f3 = -f3;
            }
            if (f4 < 0.0f) {
                f2 += f4;
                f4 = -f4;
            }
            clipImpl(f, f2, f + f3, f2 + f4);
        } else if (this.imageMode == 1) {
            if (f3 >= f) {
                float f5 = f3;
                f3 = f;
                f = f5;
            }
            if (f4 >= f2) {
                float f6 = f4;
                f4 = f2;
                f2 = f6;
            }
            clipImpl(f3, f4, f, f2);
        } else if (this.imageMode == 3) {
            if (f3 < 0.0f) {
                f3 = -f3;
            }
            if (f4 < 0.0f) {
                f4 = -f4;
            }
            float f7 = f - (f3 / 2.0f);
            float f8 = f2 - (f4 / 2.0f);
            clipImpl(f7, f8, f7 + f3, f8 + f4);
        }
    }

    /* access modifiers changed from: protected */
    public void clipImpl(float f, float f2, float f3, float f4) {
        showMissingWarning("clip");
    }

    public final int color(float f) {
        colorCalc(f);
        return this.calcColor;
    }

    public final int color(float f, float f2) {
        colorCalc(f, f2);
        return this.calcColor;
    }

    public final int color(float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        return this.calcColor;
    }

    public final int color(float f, float f2, float f3, float f4) {
        colorCalc(f, f2, f3, f4);
        return this.calcColor;
    }

    public final int color(int i) {
        if ((i & PImage.ALPHA_MASK) != 0 || ((float) i) > this.colorModeX) {
            colorCalcARGB(i, this.colorModeA);
        } else if (this.colorModeDefault) {
            if (i > 255) {
                i = 255;
            } else if (i < 0) {
                i = 0;
            }
            return (i << 16) | PImage.ALPHA_MASK | (i << 8) | i;
        } else {
            colorCalc(i);
        }
        return this.calcColor;
    }

    public final int color(int i, float f) {
        if ((-16777216 & i) != 0 || ((float) i) > this.colorModeX) {
            colorCalcARGB(i, f);
        } else {
            colorCalc(i, f);
        }
        return this.calcColor;
    }

    public final int color(int i, int i2) {
        if (this.colorModeDefault) {
            if (i > 255) {
                i = 255;
            } else if (i < 0) {
                i = 0;
            }
            if (i2 > 255) {
                i2 = 255;
            } else if (i2 < 0) {
                i2 = 0;
            }
            return ((i2 & PImage.BLUE_MASK) << 24) | (i << 16) | (i << 8) | i;
        }
        colorCalc(i, (float) i2);
        return this.calcColor;
    }

    public final int color(int i, int i2, int i3) {
        if (this.colorModeDefault) {
            if (i > 255) {
                i = 255;
            } else if (i < 0) {
                i = 0;
            }
            if (i2 > 255) {
                i2 = 255;
            } else if (i2 < 0) {
                i2 = 0;
            }
            if (i3 > 255) {
                i3 = 255;
            } else if (i3 < 0) {
                i3 = 0;
            }
            return -16777216 | (i << 16) | (i2 << 8) | i3;
        }
        colorCalc((float) i, (float) i2, (float) i3);
        return this.calcColor;
    }

    public final int color(int i, int i2, int i3, int i4) {
        if (this.colorModeDefault) {
            if (i4 > 255) {
                i4 = 255;
            } else if (i4 < 0) {
                i4 = 0;
            }
            if (i > 255) {
                i = 255;
            } else if (i < 0) {
                i = 0;
            }
            if (i2 > 255) {
                i2 = 255;
            } else if (i2 < 0) {
                i2 = 0;
            }
            if (i3 > 255) {
                i3 = 255;
            } else if (i3 < 0) {
                i3 = 0;
            }
            return (i4 << 24) | (i << 16) | (i2 << 8) | i3;
        }
        colorCalc((float) i, (float) i2, (float) i3, (float) i4);
        return this.calcColor;
    }

    /* access modifiers changed from: protected */
    public void colorCalc(float f) {
        colorCalc(f, this.colorModeA);
    }

    /* access modifiers changed from: protected */
    public void colorCalc(float f, float f2) {
        float f3 = 0.0f;
        if (f > this.colorModeX) {
            f = this.colorModeX;
        }
        float f4 = f2 > this.colorModeA ? this.colorModeA : f2;
        float f5 = f < 0.0f ? 0.0f : f;
        if (f4 >= 0.0f) {
            f3 = f4;
        }
        this.calcR = this.colorModeScale ? f5 / this.colorModeX : f5;
        this.calcG = this.calcR;
        this.calcB = this.calcR;
        if (this.colorModeScale) {
            f3 /= this.colorModeA;
        }
        this.calcA = f3;
        this.calcRi = (int) (this.calcR * 255.0f);
        this.calcGi = (int) (this.calcG * 255.0f);
        this.calcBi = (int) (this.calcB * 255.0f);
        this.calcAi = (int) (this.calcA * 255.0f);
        this.calcColor = (this.calcAi << 24) | (this.calcRi << 16) | (this.calcGi << 8) | this.calcBi;
        this.calcAlpha = this.calcAi != 255;
    }

    /* access modifiers changed from: protected */
    public void colorCalc(float f, float f2, float f3) {
        colorCalc(f, f2, f3, this.colorModeA);
    }

    /* access modifiers changed from: protected */
    public void colorCalc(float f, float f2, float f3, float f4) {
        if (f > this.colorModeX) {
            f = this.colorModeX;
        }
        if (f2 > this.colorModeY) {
            f2 = this.colorModeY;
        }
        if (f3 > this.colorModeZ) {
            f3 = this.colorModeZ;
        }
        float f5 = f4 > this.colorModeA ? this.colorModeA : f4;
        if (f < 0.0f) {
            f = 0.0f;
        }
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        switch (this.colorMode) {
            case 1:
                if (!this.colorModeScale) {
                    this.calcR = f;
                    this.calcG = f2;
                    this.calcB = f3;
                    this.calcA = f5;
                    break;
                } else {
                    this.calcR = f / this.colorModeX;
                    this.calcG = f2 / this.colorModeY;
                    this.calcB = f3 / this.colorModeZ;
                    this.calcA = f5 / this.colorModeA;
                    break;
                }
            case 3:
                float f6 = f / this.colorModeX;
                float f7 = f2 / this.colorModeY;
                float f8 = f3 / this.colorModeZ;
                if (this.colorModeScale) {
                    f5 /= this.colorModeA;
                }
                this.calcA = f5;
                if (f7 != 0.0f) {
                    float f9 = (f6 - ((float) ((int) f6))) * 6.0f;
                    float f10 = f9 - ((float) ((int) f9));
                    float f11 = (DEFAULT_STROKE_WEIGHT - f7) * f8;
                    float f12 = (DEFAULT_STROKE_WEIGHT - (f7 * f10)) * f8;
                    float f13 = (DEFAULT_STROKE_WEIGHT - ((DEFAULT_STROKE_WEIGHT - f10) * f7)) * f8;
                    switch ((int) f9) {
                        case 0:
                            this.calcR = f8;
                            this.calcG = f13;
                            this.calcB = f11;
                            break;
                        case 1:
                            this.calcR = f12;
                            this.calcG = f8;
                            this.calcB = f11;
                            break;
                        case 2:
                            this.calcR = f11;
                            this.calcG = f8;
                            this.calcB = f13;
                            break;
                        case 3:
                            this.calcR = f11;
                            this.calcG = f12;
                            this.calcB = f8;
                            break;
                        case 4:
                            this.calcR = f13;
                            this.calcG = f11;
                            this.calcB = f8;
                            break;
                        case 5:
                            this.calcR = f8;
                            this.calcG = f11;
                            this.calcB = f12;
                            break;
                    }
                } else {
                    this.calcB = f8;
                    this.calcG = f8;
                    this.calcR = f8;
                    break;
                }
        }
        this.calcRi = (int) (this.calcR * 255.0f);
        this.calcGi = (int) (this.calcG * 255.0f);
        this.calcBi = (int) (this.calcB * 255.0f);
        this.calcAi = (int) (this.calcA * 255.0f);
        this.calcColor = (this.calcAi << 24) | (this.calcRi << 16) | (this.calcGi << 8) | this.calcBi;
        this.calcAlpha = this.calcAi != 255;
    }

    /* access modifiers changed from: protected */
    public void colorCalc(int i) {
        if ((-16777216 & i) != 0 || ((float) i) > this.colorModeX) {
            colorCalcARGB(i, this.colorModeA);
        } else {
            colorCalc((float) i);
        }
    }

    /* access modifiers changed from: protected */
    public void colorCalc(int i, float f) {
        if ((-16777216 & i) != 0 || ((float) i) > this.colorModeX) {
            colorCalcARGB(i, f);
        } else {
            colorCalc((float) i, f);
        }
    }

    /* access modifiers changed from: protected */
    public void colorCalcARGB(int i, float f) {
        if (f == this.colorModeA) {
            this.calcAi = (i >> 24) & PImage.BLUE_MASK;
            this.calcColor = i;
        } else {
            this.calcAi = (int) (((float) ((i >> 24) & PImage.BLUE_MASK)) * (f / this.colorModeA));
            this.calcColor = (this.calcAi << 24) | (16777215 & i);
        }
        this.calcRi = (i >> 16) & PImage.BLUE_MASK;
        this.calcGi = (i >> 8) & PImage.BLUE_MASK;
        this.calcBi = i & PImage.BLUE_MASK;
        this.calcA = ((float) this.calcAi) / 255.0f;
        this.calcR = ((float) this.calcRi) / 255.0f;
        this.calcG = ((float) this.calcGi) / 255.0f;
        this.calcB = ((float) this.calcBi) / 255.0f;
        this.calcAlpha = this.calcAi != 255;
    }

    public void colorMode(int i) {
        colorMode(i, this.colorModeX, this.colorModeY, this.colorModeZ, this.colorModeA);
    }

    public void colorMode(int i, float f) {
        colorMode(i, f, f, f, f);
    }

    public void colorMode(int i, float f, float f2, float f3) {
        colorMode(i, f, f2, f3, this.colorModeA);
    }

    public void colorMode(int i, float f, float f2, float f3, float f4) {
        boolean z = true;
        this.colorMode = i;
        this.colorModeX = f;
        this.colorModeY = f2;
        this.colorModeZ = f3;
        this.colorModeA = f4;
        this.colorModeScale = (f4 == DEFAULT_STROKE_WEIGHT && f == f2 && f2 == f3 && f3 == f4) ? false : true;
        if (!(this.colorMode == 1 && this.colorModeA == 255.0f && this.colorModeX == 255.0f && this.colorModeY == 255.0f && this.colorModeZ == 255.0f)) {
            z = false;
        }
        this.colorModeDefault = z;
    }

    public PShape createShape() {
        showMissingWarning("createShape");
        return null;
    }

    public PShape createShape(int i) {
        showMissingWarning("createShape");
        return null;
    }

    public PShape createShape(int i, float... fArr) {
        showMissingWarning("createShape");
        return null;
    }

    public PShape createShape(PShape pShape) {
        showMissingWarning("createShape");
        return null;
    }

    public void curve(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        beginShape();
        curveVertex(f, f2);
        curveVertex(f3, f4);
        curveVertex(f5, f6);
        curveVertex(f7, f8);
        endShape();
    }

    public void curve(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        beginShape();
        curveVertex(f, f2, f3);
        curveVertex(f4, f5, f6);
        curveVertex(f7, f8, f9);
        curveVertex(f10, f11, f12);
        endShape();
    }

    public void curveDetail(int i) {
        this.curveDetail = i;
        curveInit();
    }

    /* access modifiers changed from: protected */
    public void curveInit() {
        if (this.curveDrawMatrix == null) {
            this.curveBasisMatrix = new PMatrix3D();
            this.curveDrawMatrix = new PMatrix3D();
            this.curveInited = true;
        }
        float f = this.curveTightness;
        this.curveBasisMatrix.set((f - DEFAULT_STROKE_WEIGHT) / 2.0f, (3.0f + f) / 2.0f, (-3.0f - f) / 2.0f, (DEFAULT_STROKE_WEIGHT - f) / 2.0f, DEFAULT_STROKE_WEIGHT - f, (-5.0f - f) / 2.0f, 2.0f + f, (f - DEFAULT_STROKE_WEIGHT) / 2.0f, (f - DEFAULT_STROKE_WEIGHT) / 2.0f, 0.0f, (DEFAULT_STROKE_WEIGHT - f) / 2.0f, 0.0f, 0.0f, DEFAULT_STROKE_WEIGHT, 0.0f, 0.0f);
        splineForward(this.curveDetail, this.curveDrawMatrix);
        if (this.bezierBasisInverse == null) {
            this.bezierBasisInverse = this.bezierBasisMatrix.get();
            this.bezierBasisInverse.invert();
            this.curveToBezierMatrix = new PMatrix3D();
        }
        this.curveToBezierMatrix.set((PMatrix) this.curveBasisMatrix);
        this.curveToBezierMatrix.preApply(this.bezierBasisInverse);
        this.curveDrawMatrix.apply(this.curveBasisMatrix);
    }

    /* access modifiers changed from: protected */
    public void curveInitCheck() {
        if (!this.curveInited) {
            curveInit();
        }
    }

    public float curvePoint(float f, float f2, float f3, float f4, float f5) {
        curveInitCheck();
        float f6 = f5 * f5;
        float f7 = f5 * f6;
        PMatrix3D pMatrix3D = this.curveBasisMatrix;
        return (((f6 * pMatrix3D.m13) + (f7 * pMatrix3D.m03) + (pMatrix3D.m23 * f5) + pMatrix3D.m33) * f4) + (((pMatrix3D.m00 * f7) + (pMatrix3D.m10 * f6) + (pMatrix3D.m20 * f5) + pMatrix3D.m30) * f) + (((pMatrix3D.m01 * f7) + (pMatrix3D.m11 * f6) + (pMatrix3D.m21 * f5) + pMatrix3D.m31) * f2) + (((pMatrix3D.m02 * f7) + (pMatrix3D.m12 * f6) + (pMatrix3D.m22 * f5) + pMatrix3D.m32) * f3);
    }

    public float curveTangent(float f, float f2, float f3, float f4, float f5) {
        curveInitCheck();
        float f6 = f5 * f5 * 3.0f;
        float f7 = 2.0f * f5;
        PMatrix3D pMatrix3D = this.curveBasisMatrix;
        return (((f6 * pMatrix3D.m03) + (f7 * pMatrix3D.m13) + pMatrix3D.m23) * f4) + (((pMatrix3D.m00 * f6) + (pMatrix3D.m10 * f7) + pMatrix3D.m20) * f) + (((pMatrix3D.m01 * f6) + (pMatrix3D.m11 * f7) + pMatrix3D.m21) * f2) + (((pMatrix3D.m02 * f6) + (pMatrix3D.m12 * f7) + pMatrix3D.m22) * f3);
    }

    public void curveTightness(float f) {
        this.curveTightness = f;
        curveInit();
    }

    public void curveVertex(float f, float f2) {
        curveVertexCheck();
        float[] fArr = this.curveVertices[this.curveVertexCount];
        fArr[0] = f;
        fArr[1] = f2;
        this.curveVertexCount++;
        if (this.curveVertexCount > 3) {
            curveVertexSegment(this.curveVertices[this.curveVertexCount - 4][0], this.curveVertices[this.curveVertexCount - 4][1], this.curveVertices[this.curveVertexCount - 3][0], this.curveVertices[this.curveVertexCount - 3][1], this.curveVertices[this.curveVertexCount - 2][0], this.curveVertices[this.curveVertexCount - 2][1], this.curveVertices[this.curveVertexCount - 1][0], this.curveVertices[this.curveVertexCount - 1][1]);
        }
    }

    public void curveVertex(float f, float f2, float f3) {
        curveVertexCheck();
        float[] fArr = this.curveVertices[this.curveVertexCount];
        fArr[0] = f;
        fArr[1] = f2;
        fArr[2] = f3;
        this.curveVertexCount++;
        if (this.curveVertexCount > 3) {
            curveVertexSegment(this.curveVertices[this.curveVertexCount - 4][0], this.curveVertices[this.curveVertexCount - 4][1], this.curveVertices[this.curveVertexCount - 4][2], this.curveVertices[this.curveVertexCount - 3][0], this.curveVertices[this.curveVertexCount - 3][1], this.curveVertices[this.curveVertexCount - 3][2], this.curveVertices[this.curveVertexCount - 2][0], this.curveVertices[this.curveVertexCount - 2][1], this.curveVertices[this.curveVertexCount - 2][2], this.curveVertices[this.curveVertexCount - 1][0], this.curveVertices[this.curveVertexCount - 1][1], this.curveVertices[this.curveVertexCount - 1][2]);
        }
    }

    /* access modifiers changed from: protected */
    public void curveVertexCheck() {
        curveVertexCheck(this.shape);
    }

    /* access modifiers changed from: protected */
    public void curveVertexCheck(int i) {
        if (i != 20) {
            throw new RuntimeException("You must use beginShape() or beginShape(POLYGON) before curveVertex()");
        }
        if (this.curveVertices == null) {
            this.curveVertices = (float[][]) Array.newInstance(Float.TYPE, new int[]{PConstants.MULTIPLY, 3});
        }
        if (this.curveVertexCount == this.curveVertices.length) {
            float[][] fArr = (float[][]) Array.newInstance(Float.TYPE, new int[]{this.curveVertexCount << 1, 3});
            System.arraycopy(this.curveVertices, 0, fArr, 0, this.curveVertexCount);
            this.curveVertices = fArr;
        }
        curveInitCheck();
    }

    /* access modifiers changed from: protected */
    public void curveVertexSegment(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        PMatrix3D pMatrix3D = this.curveDrawMatrix;
        float f9 = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f3) + (pMatrix3D.m12 * f5) + (pMatrix3D.m13 * f7);
        float f10 = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f3) + (pMatrix3D.m22 * f5) + (pMatrix3D.m23 * f7);
        float f11 = (pMatrix3D.m30 * f) + (pMatrix3D.m31 * f3) + (pMatrix3D.m32 * f5) + (pMatrix3D.m33 * f7);
        float f12 = (pMatrix3D.m13 * f8) + (pMatrix3D.m10 * f2) + (pMatrix3D.m11 * f4) + (pMatrix3D.m12 * f6);
        float f13 = (pMatrix3D.m20 * f2) + (pMatrix3D.m21 * f4) + (pMatrix3D.m22 * f6) + (pMatrix3D.m23 * f8);
        float f14 = (pMatrix3D.m30 * f2) + (pMatrix3D.m31 * f4) + (pMatrix3D.m32 * f6) + (pMatrix3D.m33 * f8);
        int i = this.curveVertexCount;
        vertex(f3, f4);
        for (int i2 = 0; i2 < this.curveDetail; i2++) {
            f3 += f9;
            f9 += f10;
            f10 += f11;
            f4 += f12;
            f12 += f13;
            f13 += f14;
            vertex(f3, f4);
        }
        this.curveVertexCount = i;
    }

    /* access modifiers changed from: protected */
    public void curveVertexSegment(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        PMatrix3D pMatrix3D = this.curveDrawMatrix;
        float f13 = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f4) + (pMatrix3D.m12 * f7) + (pMatrix3D.m13 * f10);
        float f14 = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f4) + (pMatrix3D.m22 * f7) + (pMatrix3D.m23 * f10);
        float f15 = (pMatrix3D.m30 * f) + (pMatrix3D.m31 * f4) + (pMatrix3D.m32 * f7) + (pMatrix3D.m33 * f10);
        float f16 = (pMatrix3D.m10 * f2) + (pMatrix3D.m11 * f5) + (pMatrix3D.m12 * f8) + (pMatrix3D.m13 * f11);
        float f17 = (pMatrix3D.m20 * f2) + (pMatrix3D.m21 * f5) + (pMatrix3D.m22 * f8) + (pMatrix3D.m23 * f11);
        float f18 = (pMatrix3D.m30 * f2) + (pMatrix3D.m31 * f5) + (pMatrix3D.m32 * f8) + (pMatrix3D.m33 * f11);
        int i = this.curveVertexCount;
        float f19 = (pMatrix3D.m13 * f12) + (pMatrix3D.m10 * f3) + (pMatrix3D.m11 * f6) + (pMatrix3D.m12 * f9);
        float f20 = (pMatrix3D.m20 * f3) + (pMatrix3D.m21 * f6) + (pMatrix3D.m22 * f9) + (pMatrix3D.m23 * f12);
        float f21 = (pMatrix3D.m30 * f3) + (pMatrix3D.m31 * f6) + (pMatrix3D.m32 * f9) + (pMatrix3D.m33 * f12);
        vertex(f4, f5, f6);
        for (int i2 = 0; i2 < this.curveDetail; i2++) {
            f4 += f13;
            f13 += f14;
            f14 += f15;
            f5 += f16;
            f16 += f17;
            f17 += f18;
            f6 += f19;
            f19 += f20;
            f20 += f21;
            vertex(f4, f5, f6);
        }
        this.curveVertexCount = i;
    }

    /* access modifiers changed from: protected */
    public void defaultFontOrDeath(String str) {
        defaultFontOrDeath(str, 12.0f);
    }

    /* access modifiers changed from: protected */
    public void defaultFontOrDeath(String str, float f) {
        if (this.parent != null) {
            this.textFont = this.parent.createDefaultFont(f);
            return;
        }
        throw new RuntimeException("Use textFont() before " + str + "()");
    }

    /* access modifiers changed from: protected */
    public void defaultSettings() {
        smooth();
        colorMode(1, 255.0f);
        fill((int) PImage.BLUE_MASK);
        stroke(0);
        strokeWeight(DEFAULT_STROKE_WEIGHT);
        strokeJoin(8);
        strokeCap(2);
        this.shape = 0;
        rectMode(0);
        ellipseMode(3);
        this.autoNormal = true;
        this.textFont = null;
        this.textSize = 12.0f;
        this.textLeading = 14.0f;
        this.textAlign = 21;
        this.textMode = 4;
        if (this.primarySurface) {
            background(this.backgroundColor);
        }
        this.settingsInited = true;
    }

    public void directionalLight(float f, float f2, float f3, float f4, float f5, float f6) {
        showMethodWarning("directionalLight");
    }

    public boolean displayable() {
        return true;
    }

    public void dispose() {
    }

    public void edge(boolean z) {
        this.edge = z;
    }

    public void ellipse(float f, float f2, float f3, float f4) {
        float f5;
        float f6;
        float f7;
        float f8;
        if (this.ellipseMode == 1) {
            f5 = f4 - f2;
            f6 = f3 - f;
            f7 = f2;
            f8 = f;
        } else if (this.ellipseMode == 2) {
            f5 = f4 * 2.0f;
            f6 = f3 * 2.0f;
            f7 = f2 - f4;
            f8 = f - f3;
        } else if (this.ellipseMode == 3) {
            f5 = f4;
            f6 = f3;
            f7 = f2 - (f4 / 2.0f);
            f8 = f - (f3 / 2.0f);
        } else {
            f5 = f4;
            f6 = f3;
            f7 = f2;
            f8 = f;
        }
        if (f6 < 0.0f) {
            f8 += f6;
            f6 = -f6;
        }
        if (f5 < 0.0f) {
            f7 += f5;
            f5 = -f5;
        }
        ellipseImpl(f8, f7, f6, f5);
    }

    /* access modifiers changed from: protected */
    public void ellipseImpl(float f, float f2, float f3, float f4) {
    }

    public void ellipseMode(int i) {
        this.ellipseMode = i;
    }

    public void emissive(float f) {
        colorCalc(f);
        emissiveFromCalc();
    }

    public void emissive(float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        emissiveFromCalc();
    }

    public void emissive(int i) {
        colorCalc(i);
        emissiveFromCalc();
    }

    /* access modifiers changed from: protected */
    public void emissiveFromCalc() {
        this.emissiveColor = this.calcColor;
        this.emissiveR = this.calcR;
        this.emissiveG = this.calcG;
        this.emissiveB = this.calcB;
    }

    public void endCamera() {
        showMethodWarning("endCamera");
    }

    public void endContour() {
        showMissingWarning("endContour");
    }

    public void endDraw() {
    }

    public void endPGL() {
        showMethodWarning("endPGL");
    }

    public void endRaw() {
        if (this.raw != null) {
            flush();
            this.raw.endDraw();
            this.raw.dispose();
            this.raw = null;
        }
    }

    public void endShape() {
        endShape(1);
    }

    public void endShape(int i) {
    }

    public void fill(float f) {
        colorCalc(f);
        fillFromCalc();
    }

    public void fill(float f, float f2) {
        colorCalc(f, f2);
        fillFromCalc();
    }

    public void fill(float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        fillFromCalc();
    }

    public void fill(float f, float f2, float f3, float f4) {
        colorCalc(f, f2, f3, f4);
        fillFromCalc();
    }

    public void fill(int i) {
        colorCalc(i);
        fillFromCalc();
    }

    public void fill(int i, float f) {
        colorCalc(i, f);
        fillFromCalc();
    }

    /* access modifiers changed from: protected */
    public void fillFromCalc() {
        this.fill = true;
        this.fillR = this.calcR;
        this.fillG = this.calcG;
        this.fillB = this.calcB;
        this.fillA = this.calcA;
        this.fillRi = this.calcRi;
        this.fillGi = this.calcGi;
        this.fillBi = this.calcBi;
        this.fillAi = this.calcAi;
        this.fillColor = this.calcColor;
        this.fillAlpha = this.calcAlpha;
    }

    public void filter(PShader pShader) {
        showMissingWarning("filter");
    }

    public void flush() {
    }

    public void frustum(float f, float f2, float f3, float f4, float f5, float f6) {
        showMethodWarning("frustum");
    }

    public Object getCache(PImage pImage) {
        return this.cacheMap.get(pImage);
    }

    public PMatrix2D getMatrix(PMatrix2D pMatrix2D) {
        showMissingWarning("getMatrix");
        return null;
    }

    public PMatrix3D getMatrix(PMatrix3D pMatrix3D) {
        showMissingWarning("getMatrix");
        return null;
    }

    public PMatrix getMatrix() {
        showMissingWarning("getMatrix");
        return null;
    }

    public PGraphics getRaw() {
        return this.raw;
    }

    public PShader getShader(int i) {
        showMissingWarning("getShader");
        return null;
    }

    public PStyle getStyle() {
        return getStyle(null);
    }

    public PStyle getStyle(PStyle pStyle) {
        if (pStyle == null) {
            pStyle = new PStyle();
        }
        pStyle.imageMode = this.imageMode;
        pStyle.rectMode = this.rectMode;
        pStyle.ellipseMode = this.ellipseMode;
        pStyle.shapeMode = this.shapeMode;
        pStyle.colorMode = this.colorMode;
        pStyle.colorModeX = this.colorModeX;
        pStyle.colorModeY = this.colorModeY;
        pStyle.colorModeZ = this.colorModeZ;
        pStyle.colorModeA = this.colorModeA;
        pStyle.tint = this.tint;
        pStyle.tintColor = this.tintColor;
        pStyle.fill = this.fill;
        pStyle.fillColor = this.fillColor;
        pStyle.stroke = this.stroke;
        pStyle.strokeColor = this.strokeColor;
        pStyle.strokeWeight = this.strokeWeight;
        pStyle.strokeCap = this.strokeCap;
        pStyle.strokeJoin = this.strokeJoin;
        pStyle.ambientR = this.ambientR;
        pStyle.ambientG = this.ambientG;
        pStyle.ambientB = this.ambientB;
        pStyle.specularR = this.specularR;
        pStyle.specularG = this.specularG;
        pStyle.specularB = this.specularB;
        pStyle.emissiveR = this.emissiveR;
        pStyle.emissiveG = this.emissiveG;
        pStyle.emissiveB = this.emissiveB;
        pStyle.shininess = this.shininess;
        pStyle.textFont = this.textFont;
        pStyle.textAlign = this.textAlign;
        pStyle.textAlignY = this.textAlignY;
        pStyle.textMode = this.textMode;
        pStyle.textSize = this.textSize;
        pStyle.textLeading = this.textLeading;
        return pStyle;
    }

    public final float green(int i) {
        float f = (float) ((i >> 8) & PImage.BLUE_MASK);
        return this.colorModeDefault ? f : (f / 255.0f) * this.colorModeY;
    }

    public boolean haveRaw() {
        return this.raw != null;
    }

    public void hint(int i) {
        if (i > 0) {
            this.hints[i] = true;
        } else {
            this.hints[-i] = false;
        }
    }

    public final float hue(int i) {
        if (i != this.cacheHsbKey) {
            Color.RGBToHSV((i >> 16) & PImage.BLUE_MASK, (i >> 8) & PImage.BLUE_MASK, i & PImage.BLUE_MASK, this.cacheHsbValue);
            this.cacheHsbKey = i;
        }
        return (this.cacheHsbValue[0] / 360.0f) * this.colorModeX;
    }

    public void image(PImage pImage, float f, float f2) {
        if (pImage.width != -1 && pImage.height != -1 && pImage.width != 0 && pImage.height != 0) {
            if (this.imageMode == 0 || this.imageMode == 1) {
                imageImpl(pImage, f, f2, f + ((float) pImage.width), f2 + ((float) pImage.height), 0, 0, pImage.width, pImage.height);
            } else if (this.imageMode == 3) {
                float f3 = f - ((float) (pImage.width / 2));
                float f4 = f2 - ((float) (pImage.height / 2));
                imageImpl(pImage, f3, f4, f3 + ((float) pImage.width), f4 + ((float) pImage.height), 0, 0, pImage.width, pImage.height);
            }
        }
    }

    public void image(PImage pImage, float f, float f2, float f3, float f4) {
        image(pImage, f, f2, f3, f4, 0, 0, pImage.width, pImage.height);
    }

    public void image(PImage pImage, float f, float f2, float f3, float f4, int i, int i2, int i3, int i4) {
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        if (pImage.width != -1 && pImage.height != -1) {
            if (this.imageMode == 0) {
                if (f3 < 0.0f) {
                    f9 = f + f3;
                    f3 = -f3;
                } else {
                    f9 = f;
                }
                if (f4 < 0.0f) {
                    f10 = f2 + f4;
                    f4 = -f4;
                } else {
                    f10 = f2;
                }
                imageImpl(pImage, f9, f10, f9 + f3, f10 + f4, i, i2, i3, i4);
            } else if (this.imageMode == 1) {
                if (f3 < f) {
                    f5 = f;
                    f6 = f3;
                } else {
                    f5 = f3;
                    f6 = f;
                }
                if (f4 < f2) {
                    f7 = f2;
                    f8 = f4;
                } else {
                    f7 = f4;
                    f8 = f2;
                }
                imageImpl(pImage, f6, f8, f5, f7, i, i2, i3, i4);
            } else if (this.imageMode == 3) {
                if (f3 < 0.0f) {
                    f3 = -f3;
                }
                if (f4 < 0.0f) {
                    f4 = -f4;
                }
                float f11 = f - (f3 / 2.0f);
                float f12 = f2 - (f4 / 2.0f);
                imageImpl(pImage, f11, f12, f11 + f3, f12 + f4, i, i2, i3, i4);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void imageImpl(PImage pImage, float f, float f2, float f3, float f4, int i, int i2, int i3, int i4) {
        boolean z = this.stroke;
        boolean z2 = this.fill;
        int i5 = this.textureMode;
        this.stroke = false;
        this.fill = true;
        this.textureMode = 2;
        float f5 = this.fillR;
        float f6 = this.fillG;
        float f7 = this.fillB;
        float f8 = this.fillA;
        if (this.tint) {
            this.fillR = this.tintR;
            this.fillG = this.tintG;
            this.fillB = this.tintB;
            this.fillA = this.tintA;
        } else {
            this.fillR = DEFAULT_STROKE_WEIGHT;
            this.fillG = DEFAULT_STROKE_WEIGHT;
            this.fillB = DEFAULT_STROKE_WEIGHT;
            this.fillA = DEFAULT_STROKE_WEIGHT;
        }
        beginShape(17);
        texture(pImage);
        vertex(f, f2, (float) i, (float) i2);
        vertex(f, f4, (float) i, (float) i4);
        vertex(f3, f4, (float) i3, (float) i4);
        vertex(f3, f2, (float) i3, (float) i2);
        endShape();
        this.stroke = z;
        this.fill = z2;
        this.textureMode = i5;
        this.fillR = f5;
        this.fillG = f6;
        this.fillB = f7;
        this.fillA = f8;
    }

    public void imageMode(int i) {
        if (i == 0 || i == 1 || i == 3) {
            this.imageMode = i;
            return;
        }
        throw new RuntimeException("imageMode() only works with CORNER, CORNERS, or CENTER");
    }

    public Object initCache(PImage pImage) {
        return null;
    }

    public boolean is2D() {
        return true;
    }

    public boolean is3D() {
        return false;
    }

    public boolean isGL() {
        return false;
    }

    public int lerpColor(int i, int i2, float f) {
        return lerpColor(i, i2, f, this.colorMode);
    }

    public void lightFalloff(float f, float f2, float f3) {
        showMethodWarning("lightFalloff");
    }

    public void lightSpecular(float f, float f2, float f3) {
        showMethodWarning("lightSpecular");
    }

    public void lights() {
        showMethodWarning("lights");
    }

    public void line(float f, float f2, float f3, float f4) {
        beginShape(5);
        vertex(f, f2);
        vertex(f3, f4);
        endShape();
    }

    public void line(float f, float f2, float f3, float f4, float f5, float f6) {
        beginShape(5);
        vertex(f, f2, f3);
        vertex(f4, f5, f6);
        endShape();
    }

    public PShader loadShader(String str) {
        showMissingWarning("loadShader");
        return null;
    }

    public PShader loadShader(String str, String str2) {
        showMissingWarning("loadShader");
        return null;
    }

    public PShape loadShape(String str) {
        showMissingWarning("loadShape");
        return null;
    }

    public float modelX(float f, float f2, float f3) {
        showMissingWarning("modelX");
        return 0.0f;
    }

    public float modelY(float f, float f2, float f3) {
        showMissingWarning("modelY");
        return 0.0f;
    }

    public float modelZ(float f, float f2, float f3) {
        showMissingWarning("modelZ");
        return 0.0f;
    }

    public void noClip() {
        showMissingWarning("noClip");
    }

    public void noFill() {
        this.fill = false;
    }

    public void noLights() {
        showMethodWarning("noLights");
    }

    public void noSmooth() {
        this.smooth = false;
    }

    public void noStroke() {
        this.stroke = false;
    }

    public void noTexture() {
        this.textureImage = null;
    }

    public void noTint() {
        this.tint = false;
    }

    public void normal(float f, float f2, float f3) {
        this.normalX = f;
        this.normalY = f2;
        this.normalZ = f3;
        if (this.shape == 0) {
            return;
        }
        if (this.normalMode == 0) {
            this.normalMode = 1;
        } else if (this.normalMode == 1) {
            this.normalMode = 2;
        }
    }

    public void ortho() {
        showMissingWarning("ortho");
    }

    public void ortho(float f, float f2, float f3, float f4) {
        showMissingWarning("ortho");
    }

    public void ortho(float f, float f2, float f3, float f4, float f5, float f6) {
        showMissingWarning("ortho");
    }

    public void perspective() {
        showMissingWarning("perspective");
    }

    public void perspective(float f, float f2, float f3, float f4) {
        showMissingWarning("perspective");
    }

    public void point(float f, float f2) {
        beginShape(3);
        vertex(f, f2);
        endShape();
    }

    public void point(float f, float f2, float f3) {
        beginShape(3);
        vertex(f, f2, f3);
        endShape();
    }

    public void pointLight(float f, float f2, float f3, float f4, float f5, float f6) {
        showMethodWarning("pointLight");
    }

    public void popMatrix() {
        showMethodWarning("popMatrix");
    }

    public void popStyle() {
        if (this.styleStackDepth == 0) {
            throw new RuntimeException("Too many popStyle() without enough pushStyle()");
        }
        this.styleStackDepth--;
        style(this.styleStack[this.styleStackDepth]);
    }

    public void printCamera() {
        showMethodWarning("printCamera");
    }

    public void printMatrix() {
        showMethodWarning("printMatrix");
    }

    public void printProjection() {
        showMethodWarning("printCamera");
    }

    public void pushMatrix() {
        showMethodWarning("pushMatrix");
    }

    public void pushStyle() {
        if (this.styleStackDepth == this.styleStack.length) {
            this.styleStack = (PStyle[]) PApplet.expand((Object) this.styleStack);
        }
        if (this.styleStack[this.styleStackDepth] == null) {
            this.styleStack[this.styleStackDepth] = new PStyle();
        }
        PStyle[] pStyleArr = this.styleStack;
        int i = this.styleStackDepth;
        this.styleStackDepth = i + 1;
        getStyle(pStyleArr[i]);
    }

    public void quad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        beginShape(17);
        vertex(f, f2);
        vertex(f3, f4);
        vertex(f5, f6);
        vertex(f7, f8);
        endShape();
    }

    public void quadraticVertex(float f, float f2, float f3, float f4) {
        bezierVertexCheck();
        float[] fArr = this.vertices[this.vertexCount - 1];
        float f5 = fArr[0];
        float f6 = fArr[1];
        bezierVertex(f5 + (((f - f5) * 2.0f) / 3.0f), (((f2 - f6) * 2.0f) / 3.0f) + f6, f3 + (((f - f3) * 2.0f) / 3.0f), f4 + (((f2 - f4) * 2.0f) / 3.0f), f3, f4);
    }

    public void quadraticVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        bezierVertexCheck();
        float[] fArr = this.vertices[this.vertexCount - 1];
        float f7 = fArr[0];
        float f8 = fArr[1];
        float f9 = fArr[2];
        bezierVertex(f7 + (((f - f7) * 2.0f) / 3.0f), f8 + (((f2 - f8) * 2.0f) / 3.0f), (((f3 - f9) * 2.0f) / 3.0f) + f9, f4 + (((f - f4) * 2.0f) / 3.0f), f5 + (((f2 - f5) * 2.0f) / 3.0f), f6 + (((f3 - f6) * 2.0f) / 3.0f), f4, f5, f6);
    }

    /* access modifiers changed from: protected */
    public void reapplySettings() {
        if (this.settingsInited) {
            colorMode(this.colorMode, this.colorModeX, this.colorModeY, this.colorModeZ);
            if (this.fill) {
                fill(this.fillColor);
            } else {
                noFill();
            }
            if (this.stroke) {
                stroke(this.strokeColor);
                strokeWeight(this.strokeWeight);
                strokeCap(this.strokeCap);
                strokeJoin(this.strokeJoin);
            } else {
                noStroke();
            }
            if (this.tint) {
                tint(this.tintColor);
            } else {
                noTint();
            }
            if (this.smooth) {
                smooth();
            } else {
                noSmooth();
            }
            if (this.textFont != null) {
                float f = this.textLeading;
                textFont(this.textFont, this.textSize);
                textLeading(f);
            }
            textMode(this.textMode);
            textAlign(this.textAlign, this.textAlignY);
            background(this.backgroundColor);
        }
    }

    public void rect(float f, float f2, float f3, float f4) {
        float f5;
        float f6;
        float f7;
        float f8;
        switch (this.rectMode) {
            case 0:
                f5 = f4 + f2;
                f6 = f3 + f;
                f7 = f2;
                f8 = f;
                break;
            case 1:
                f5 = f4;
                f6 = f3;
                f7 = f2;
                f8 = f;
                break;
            case 2:
                f6 = f + f3;
                f5 = f2 + f4;
                f7 = f2 - f4;
                f8 = f - f3;
                break;
            case 3:
                float f9 = f3 / 2.0f;
                float f10 = f4 / 2.0f;
                float f11 = f + f9;
                float f12 = f2 + f10;
                float f13 = f - f9;
                float f14 = f2 - f10;
                f5 = f12;
                f6 = f11;
                f7 = f14;
                f8 = f13;
                break;
            default:
                f5 = f4;
                f6 = f3;
                f7 = f2;
                f8 = f;
                break;
        }
        if (f8 <= f6) {
            float f15 = f6;
            f6 = f8;
            f8 = f15;
        }
        if (f7 <= f5) {
            float f16 = f5;
            f5 = f7;
            f7 = f16;
        }
        rectImpl(f6, f5, f8, f7);
    }

    public void rect(float f, float f2, float f3, float f4, float f5) {
        rect(f, f2, f3, f4, f5, f5, f5, f5);
    }

    public void rect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        switch (this.rectMode) {
            case 0:
                f9 = f4 + f2;
                f10 = f3 + f;
                f11 = f2;
                f12 = f;
                break;
            case 1:
                f9 = f4;
                f10 = f3;
                f11 = f2;
                f12 = f;
                break;
            case 2:
                f10 = f + f3;
                f9 = f2 + f4;
                f11 = f2 - f4;
                f12 = f - f3;
                break;
            case 3:
                float f14 = f3 / 2.0f;
                float f15 = f4 / 2.0f;
                float f16 = f + f14;
                float f17 = f2 + f15;
                float f18 = f - f14;
                float f19 = f2 - f15;
                f9 = f17;
                f10 = f16;
                f11 = f19;
                f12 = f18;
                break;
            default:
                f9 = f4;
                f10 = f3;
                f11 = f2;
                f12 = f;
                break;
        }
        if (f12 <= f10) {
            float f20 = f10;
            f10 = f12;
            f12 = f20;
        }
        if (f11 > f9) {
            f13 = f9;
        } else {
            f13 = f11;
            f11 = f9;
        }
        float min = PApplet.min((f12 - f10) / 2.0f, (f11 - f13) / 2.0f);
        float f21 = f5 > min ? min : f5;
        float f22 = f6 > min ? min : f6;
        float f23 = f7 > min ? min : f7;
        if (f8 <= min) {
            min = f8;
        }
        rectImpl(f10, f13, f12, f11, f21, f22, f23, min);
    }

    /* access modifiers changed from: protected */
    public void rectImpl(float f, float f2, float f3, float f4) {
        quad(f, f2, f3, f2, f3, f4, f, f4);
    }

    /* access modifiers changed from: protected */
    public void rectImpl(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        beginShape();
        if (f6 != 0.0f) {
            vertex(f3 - f6, f2);
            quadraticVertex(f3, f2, f3, f2 + f6);
        } else {
            vertex(f3, f2);
        }
        if (f7 != 0.0f) {
            vertex(f3, f4 - f7);
            quadraticVertex(f3, f4, f3 - f7, f4);
        } else {
            vertex(f3, f4);
        }
        if (f8 != 0.0f) {
            vertex(f + f8, f4);
            quadraticVertex(f, f4, f, f4 - f8);
        } else {
            vertex(f, f4);
        }
        if (f5 != 0.0f) {
            vertex(f, f2 + f5);
            quadraticVertex(f, f2, f + f5, f2);
        } else {
            vertex(f, f2);
        }
        endShape(2);
    }

    public void rectMode(int i) {
        this.rectMode = i;
    }

    public final float red(int i) {
        float f = (float) ((i >> 16) & PImage.BLUE_MASK);
        return this.colorModeDefault ? f : (f / 255.0f) * this.colorModeX;
    }

    public void removeCache(PImage pImage) {
        this.cacheMap.remove(pImage);
    }

    public void requestDraw() {
    }

    public void resetMatrix() {
        showMethodWarning("resetMatrix");
    }

    public void resetShader() {
        showMissingWarning("resetShader");
    }

    public void resetShader(int i) {
        showMissingWarning("resetShader");
    }

    public void rotate(float f) {
        showMissingWarning("rotate");
    }

    public void rotate(float f, float f2, float f3, float f4) {
        showMissingWarning("rotate");
    }

    public void rotateX(float f) {
        showMethodWarning("rotateX");
    }

    public void rotateY(float f) {
        showMethodWarning("rotateY");
    }

    public void rotateZ(float f) {
        showMethodWarning("rotateZ");
    }

    public final float saturation(int i) {
        if (i != this.cacheHsbKey) {
            Color.RGBToHSV((i >> 16) & PImage.BLUE_MASK, (i >> 8) & PImage.BLUE_MASK, i & PImage.BLUE_MASK, this.cacheHsbValue);
            this.cacheHsbKey = i;
        }
        return this.cacheHsbValue[1] * this.colorModeY;
    }

    public void scale(float f) {
        showMissingWarning("scale");
    }

    public void scale(float f, float f2) {
        showMissingWarning("scale");
    }

    public void scale(float f, float f2, float f3) {
        showMissingWarning("scale");
    }

    public float screenX(float f, float f2) {
        showMissingWarning("screenX");
        return 0.0f;
    }

    public float screenX(float f, float f2, float f3) {
        showMissingWarning("screenX");
        return 0.0f;
    }

    public float screenY(float f, float f2) {
        showMissingWarning("screenY");
        return 0.0f;
    }

    public float screenY(float f, float f2, float f3) {
        showMissingWarning("screenY");
        return 0.0f;
    }

    public float screenZ(float f, float f2, float f3) {
        showMissingWarning("screenZ");
        return 0.0f;
    }

    public void setCache(PImage pImage, Object obj) {
        this.cacheMap.put(pImage, obj);
    }

    public void setFrameRate(float f) {
    }

    public void setMatrix(PMatrix2D pMatrix2D) {
        showMissingWarning("setMatrix");
    }

    public void setMatrix(PMatrix3D pMatrix3D) {
        showMissingWarning("setMatrix");
    }

    public void setMatrix(PMatrix pMatrix) {
        if (pMatrix instanceof PMatrix2D) {
            setMatrix((PMatrix2D) pMatrix);
        } else if (pMatrix instanceof PMatrix3D) {
            setMatrix((PMatrix3D) pMatrix);
        }
    }

    public void setParent(PApplet pApplet) {
        this.parent = pApplet;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public void setPrimary(boolean z) {
        this.primarySurface = z;
        if (this.primarySurface) {
            this.format = 1;
        }
    }

    public void setSize(int i, int i2) {
        this.width = i;
        this.height = i2;
        this.width1 = this.width - 1;
        this.height1 = this.height - 1;
        allocate();
        reapplySettings();
    }

    public void shader(PShader pShader) {
        showMissingWarning("shader");
    }

    public void shader(PShader pShader, int i) {
        showMissingWarning("shader");
    }

    public void shape(PShape pShape) {
        if (pShape.isVisible()) {
            if (this.shapeMode == 3) {
                pushMatrix();
                translate((-pShape.getWidth()) / 2.0f, (-pShape.getHeight()) / 2.0f);
            }
            pShape.draw(this);
            if (this.shapeMode == 3) {
                popMatrix();
            }
        }
    }

    public void shape(PShape pShape, float f, float f2) {
        if (pShape.isVisible()) {
            pushMatrix();
            if (this.shapeMode == 3) {
                translate(f - (pShape.getWidth() / 2.0f), f2 - (pShape.getHeight() / 2.0f));
            } else if (this.shapeMode == 0 || this.shapeMode == 1) {
                translate(f, f2);
            }
            pShape.draw(this);
            popMatrix();
        }
    }

    /* access modifiers changed from: protected */
    public void shape(PShape pShape, float f, float f2, float f3) {
        showMissingWarning("shape");
    }

    public void shape(PShape pShape, float f, float f2, float f3, float f4) {
        if (pShape.isVisible()) {
            pushMatrix();
            if (this.shapeMode == 3) {
                translate(f - (f3 / 2.0f), f2 - (f4 / 2.0f));
                scale(f3 / pShape.getWidth(), f4 / pShape.getHeight());
            } else if (this.shapeMode == 0) {
                translate(f, f2);
                scale(f3 / pShape.getWidth(), f4 / pShape.getHeight());
            } else if (this.shapeMode == 1) {
                float f5 = f3 - f;
                float f6 = f4 - f2;
                translate(f, f2);
                scale(f5 / pShape.getWidth(), f6 / pShape.getHeight());
            }
            pShape.draw(this);
            popMatrix();
        }
    }

    /* access modifiers changed from: protected */
    public void shape(PShape pShape, float f, float f2, float f3, float f4, float f5, float f6) {
        showMissingWarning("shape");
    }

    public void shapeMode(int i) {
        this.shapeMode = i;
    }

    public void shearX(float f) {
        showMissingWarning("shearX");
    }

    public void shearY(float f) {
        showMissingWarning("shearY");
    }

    public void shininess(float f) {
        this.shininess = f;
    }

    public void smooth() {
        this.smooth = true;
    }

    public void smooth(int i) {
        this.smooth = true;
    }

    public void specular(float f) {
        colorCalc(f);
        specularFromCalc();
    }

    public void specular(float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        specularFromCalc();
    }

    public void specular(int i) {
        colorCalc(i);
        specularFromCalc();
    }

    /* access modifiers changed from: protected */
    public void specularFromCalc() {
        this.specularColor = this.calcColor;
        this.specularR = this.calcR;
        this.specularG = this.calcG;
        this.specularB = this.calcB;
    }

    public void sphere(float f) {
        if (this.sphereDetailU < 3 || this.sphereDetailV < 2) {
            sphereDetail(30);
        }
        edge(false);
        beginShape(10);
        for (int i = 0; i < this.sphereDetailU; i++) {
            normal(0.0f, -1.0f, 0.0f);
            vertex(0.0f, -f, 0.0f);
            normal(this.sphereX[i], this.sphereY[i], this.sphereZ[i]);
            vertex(this.sphereX[i] * f, this.sphereY[i] * f, this.sphereZ[i] * f);
        }
        normal(0.0f, -f, 0.0f);
        vertex(0.0f, -f, 0.0f);
        normal(this.sphereX[0], this.sphereY[0], this.sphereZ[0]);
        vertex(this.sphereX[0] * f, this.sphereY[0] * f, this.sphereZ[0] * f);
        endShape();
        int i2 = 2;
        int i3 = 0;
        while (i2 < this.sphereDetailV) {
            int i4 = this.sphereDetailU + i3;
            beginShape(10);
            int i5 = 0;
            int i6 = i4;
            int i7 = i3;
            while (i5 < this.sphereDetailU) {
                normal(this.sphereX[i7], this.sphereY[i7], this.sphereZ[i7]);
                int i8 = i7 + 1;
                vertex(f * this.sphereX[i7], f * this.sphereY[i7], this.sphereZ[i7] * f);
                normal(this.sphereX[i6], this.sphereY[i6], this.sphereZ[i6]);
                int i9 = i6 + 1;
                vertex(f * this.sphereX[i6], f * this.sphereY[i6], this.sphereZ[i6] * f);
                i5++;
                i6 = i9;
                i7 = i8;
            }
            normal(this.sphereX[i3], this.sphereY[i3], this.sphereZ[i3]);
            vertex(this.sphereX[i3] * f, this.sphereY[i3] * f, this.sphereZ[i3] * f);
            normal(this.sphereX[i4], this.sphereY[i4], this.sphereZ[i4]);
            vertex(this.sphereX[i4] * f, this.sphereY[i4] * f, this.sphereZ[i4] * f);
            endShape();
            i2++;
            i3 = i4;
        }
        beginShape(10);
        for (int i10 = 0; i10 < this.sphereDetailU; i10++) {
            int i11 = i3 + i10;
            normal(this.sphereX[i11], this.sphereY[i11], this.sphereZ[i11]);
            vertex(this.sphereX[i11] * f, this.sphereY[i11] * f, this.sphereZ[i11] * f);
            normal(0.0f, DEFAULT_STROKE_WEIGHT, 0.0f);
            vertex(0.0f, f, 0.0f);
        }
        normal(this.sphereX[i3], this.sphereY[i3], this.sphereZ[i3]);
        vertex(this.sphereX[i3] * f, this.sphereY[i3] * f, this.sphereZ[i3] * f);
        normal(0.0f, DEFAULT_STROKE_WEIGHT, 0.0f);
        vertex(0.0f, f, 0.0f);
        endShape();
        edge(true);
    }

    public void sphereDetail(int i) {
        sphereDetail(i, i);
    }

    public void sphereDetail(int i, int i2) {
        if (i < 3) {
            i = 3;
        }
        if (i2 < 2) {
            i2 = 2;
        }
        if (i != this.sphereDetailU || i2 != this.sphereDetailV) {
            float f = 720.0f / ((float) i);
            float[] fArr = new float[i];
            float[] fArr2 = new float[i];
            for (int i3 = 0; i3 < i; i3++) {
                fArr[i3] = cosLUT[((int) (((float) i3) * f)) % SINCOS_LENGTH];
                fArr2[i3] = sinLUT[((int) (((float) i3) * f)) % SINCOS_LENGTH];
            }
            int i4 = ((i2 - 1) * i) + 2;
            this.sphereX = new float[i4];
            this.sphereY = new float[i4];
            this.sphereZ = new float[i4];
            float f2 = 360.0f / ((float) i2);
            int i5 = 1;
            float f3 = f2;
            int i6 = 0;
            while (i5 < i2) {
                float f4 = sinLUT[((int) f3) % SINCOS_LENGTH];
                float f5 = cosLUT[((int) f3) % SINCOS_LENGTH];
                int i7 = i6;
                int i8 = 0;
                while (i8 < i) {
                    this.sphereX[i7] = fArr[i8] * f4;
                    this.sphereY[i7] = f5;
                    int i9 = i7 + 1;
                    this.sphereZ[i7] = fArr2[i8] * f4;
                    i8++;
                    i7 = i9;
                }
                i5++;
                f3 += f2;
                i6 = i7;
            }
            this.sphereDetailU = i;
            this.sphereDetailV = i2;
        }
    }

    /* access modifiers changed from: protected */
    public void splineForward(int i, PMatrix3D pMatrix3D) {
        float f = DEFAULT_STROKE_WEIGHT / ((float) i);
        float f2 = f * f;
        float f3 = f2 * f;
        pMatrix3D.set(0.0f, 0.0f, 0.0f, DEFAULT_STROKE_WEIGHT, f3, f2, f, 0.0f, 6.0f * f3, 2.0f * f2, 0.0f, 0.0f, 6.0f * f3, 0.0f, 0.0f, 0.0f);
    }

    public void spotLight(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        showMethodWarning("spotLight");
    }

    public void stroke(float f) {
        colorCalc(f);
        strokeFromCalc();
    }

    public void stroke(float f, float f2) {
        colorCalc(f, f2);
        strokeFromCalc();
    }

    public void stroke(float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        strokeFromCalc();
    }

    public void stroke(float f, float f2, float f3, float f4) {
        colorCalc(f, f2, f3, f4);
        strokeFromCalc();
    }

    public void stroke(int i) {
        colorCalc(i);
        strokeFromCalc();
    }

    public void stroke(int i, float f) {
        colorCalc(i, f);
        strokeFromCalc();
    }

    public void strokeCap(int i) {
        this.strokeCap = i;
    }

    /* access modifiers changed from: protected */
    public void strokeFromCalc() {
        this.stroke = true;
        this.strokeR = this.calcR;
        this.strokeG = this.calcG;
        this.strokeB = this.calcB;
        this.strokeA = this.calcA;
        this.strokeRi = this.calcRi;
        this.strokeGi = this.calcGi;
        this.strokeBi = this.calcBi;
        this.strokeAi = this.calcAi;
        this.strokeColor = this.calcColor;
        this.strokeAlpha = this.calcAlpha;
    }

    public void strokeJoin(int i) {
        this.strokeJoin = i;
    }

    public void strokeWeight(float f) {
        this.strokeWeight = f;
    }

    public void style(PStyle pStyle) {
        imageMode(pStyle.imageMode);
        rectMode(pStyle.rectMode);
        ellipseMode(pStyle.ellipseMode);
        shapeMode(pStyle.shapeMode);
        if (pStyle.tint) {
            tint(pStyle.tintColor);
        } else {
            noTint();
        }
        if (pStyle.fill) {
            fill(pStyle.fillColor);
        } else {
            noFill();
        }
        if (pStyle.stroke) {
            stroke(pStyle.strokeColor);
        } else {
            noStroke();
        }
        strokeWeight(pStyle.strokeWeight);
        strokeCap(pStyle.strokeCap);
        strokeJoin(pStyle.strokeJoin);
        colorMode(1, DEFAULT_STROKE_WEIGHT);
        ambient(pStyle.ambientR, pStyle.ambientG, pStyle.ambientB);
        emissive(pStyle.emissiveR, pStyle.emissiveG, pStyle.emissiveB);
        specular(pStyle.specularR, pStyle.specularG, pStyle.specularB);
        shininess(pStyle.shininess);
        colorMode(pStyle.colorMode, pStyle.colorModeX, pStyle.colorModeY, pStyle.colorModeZ, pStyle.colorModeA);
        if (pStyle.textFont != null) {
            textFont(pStyle.textFont, pStyle.textSize);
            textLeading(pStyle.textLeading);
        }
        textAlign(pStyle.textAlign, pStyle.textAlignY);
        textMode(pStyle.textMode);
    }

    public void text(char c, float f, float f2) {
        if (this.textFont == null) {
            defaultFontOrDeath("text");
        }
        float f3 = this.textAlignY == 3 ? f2 + (textAscent() / 2.0f) : this.textAlignY == 101 ? f2 + textAscent() : this.textAlignY == 102 ? f2 - textDescent() : f2;
        this.textBuffer[0] = c;
        textLineAlignImpl(this.textBuffer, 0, 1, f, f3);
    }

    public void text(char c, float f, float f2, float f3) {
        if (f3 != 0.0f) {
            translate(0.0f, 0.0f, f3);
        }
        text(c, f, f2);
        if (f3 != 0.0f) {
            translate(0.0f, 0.0f, -f3);
        }
    }

    public void text(float f, float f2, float f3) {
        text(PApplet.nfs(f, 0, 3), f2, f3);
    }

    public void text(float f, float f2, float f3, float f4) {
        text(PApplet.nfs(f, 0, 3), f2, f3, f4);
    }

    public void text(int i, float f, float f2) {
        text(String.valueOf(i), f, f2);
    }

    public void text(int i, float f, float f2, float f3) {
        text(String.valueOf(i), f, f2, f3);
    }

    public void text(String str, float f, float f2) {
        int i = 0;
        if (this.textFont == null) {
            defaultFontOrDeath("text");
        }
        int length = str.length();
        if (length > this.textBuffer.length) {
            this.textBuffer = new char[(length + 10)];
        }
        str.getChars(0, length, this.textBuffer, 0);
        float f3 = 0.0f;
        for (int i2 = 0; i2 < length; i2++) {
            if (this.textBuffer[i2] == 10) {
                f3 += this.textLeading;
            }
        }
        if (this.textAlignY == 3) {
            f2 += (textAscent() - f3) / 2.0f;
        } else if (this.textAlignY == 101) {
            f2 += textAscent();
        } else if (this.textAlignY == 102) {
            f2 -= f3 + textDescent();
        }
        int i3 = 0;
        float f4 = f2;
        while (i < length) {
            if (this.textBuffer[i] == 10) {
                textLineAlignImpl(this.textBuffer, i3, i, f, f4);
                i3 = i + 1;
                f4 += this.textLeading;
            }
            i++;
        }
        if (i3 < length) {
            textLineAlignImpl(this.textBuffer, i3, i, f, f4);
        }
    }

    public void text(String str, float f, float f2, float f3) {
        if (f3 != 0.0f) {
            translate(0.0f, 0.0f, f3);
        }
        text(str, f, f2);
        if (f3 != 0.0f) {
            translate(0.0f, 0.0f, -f3);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00f4  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00fd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void text(java.lang.String r12, float r13, float r14, float r15, float r16) {
        /*
            r11 = this;
            processing.core.PFont r0 = r11.textFont
            if (r0 != 0) goto L_0x0009
            java.lang.String r0 = "text"
            r11.defaultFontOrDeath(r0)
        L_0x0009:
            int r0 = r11.rectMode
            switch(r0) {
                case 0: goto L_0x00c9;
                case 1: goto L_0x000e;
                case 2: goto L_0x00d1;
                case 3: goto L_0x00dc;
                default: goto L_0x000e;
            }
        L_0x000e:
            r1 = r16
            r2 = r15
            r0 = r14
            r8 = r13
        L_0x0013:
            int r3 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r3 >= 0) goto L_0x0151
            r9 = r2
        L_0x0018:
            int r2 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1))
            if (r2 >= 0) goto L_0x014d
            r6 = r0
            r7 = r1
        L_0x001e:
            float r4 = r8 - r9
            r0 = 32
            float r5 = r11.textWidth(r0)
            int[] r0 = r11.textBreakStart
            if (r0 != 0) goto L_0x0036
            r0 = 20
            int[] r0 = new int[r0]
            r11.textBreakStart = r0
            r0 = 20
            int[] r0 = new int[r0]
            r11.textBreakStop = r0
        L_0x0036:
            r0 = 0
            r11.textBreakCount = r0
            int r0 = r12.length()
            int r1 = r0 + 1
            char[] r2 = r11.textBuffer
            int r2 = r2.length
            if (r1 <= r2) goto L_0x004a
            int r1 = r0 + 1
            char[] r1 = new char[r1]
            r11.textBuffer = r1
        L_0x004a:
            r1 = 0
            char[] r2 = r11.textBuffer
            r3 = 0
            r12.getChars(r1, r0, r2, r3)
            char[] r1 = r11.textBuffer
            int r10 = r0 + 1
            r2 = 10
            r1[r0] = r2
            r2 = 0
            r3 = 0
        L_0x005b:
            if (r3 >= r10) goto L_0x006e
            char[] r0 = r11.textBuffer
            char r0 = r0[r3]
            r1 = 10
            if (r0 != r1) goto L_0x00f0
            char[] r1 = r11.textBuffer
            r0 = r11
            boolean r0 = r0.textSentence(r1, r2, r3, r4, r5)
            if (r0 != 0) goto L_0x00ee
        L_0x006e:
            int r0 = r11.textAlign
            r1 = 3
            if (r0 != r1) goto L_0x00f4
            r0 = 1073741824(0x40000000, float:2.0)
            float r0 = r4 / r0
            float r4 = r9 + r0
        L_0x0079:
            float r0 = r6 - r7
            float r1 = r11.textAscent()
            float r2 = r11.textDescent()
            float r1 = r1 + r2
            float r1 = r0 - r1
            float r2 = r11.textLeading
            float r1 = r1 / r2
            int r1 = processing.core.PApplet.floor(r1)
            int r1 = r1 + 1
            int r2 = r11.textBreakCount
            int r8 = java.lang.Math.min(r2, r1)
            int r1 = r11.textAlignY
            r2 = 3
            if (r1 != r2) goto L_0x00fd
            float r1 = r11.textAscent()
            float r2 = r11.textLeading
            int r3 = r8 + -1
            float r3 = (float) r3
            float r2 = r2 * r3
            float r1 = r1 + r2
            float r2 = r11.textAscent()
            float r2 = r2 + r7
            float r0 = r0 - r1
            r1 = 1073741824(0x40000000, float:2.0)
            float r0 = r0 / r1
            float r5 = r2 + r0
            r0 = 0
            r6 = r0
        L_0x00b2:
            if (r6 >= r8) goto L_0x0149
            char[] r1 = r11.textBuffer
            int[] r0 = r11.textBreakStart
            r2 = r0[r6]
            int[] r0 = r11.textBreakStop
            r3 = r0[r6]
            r0 = r11
            r0.textLineAlignImpl(r1, r2, r3, r4, r5)
            float r0 = r11.textLeading
            float r5 = r5 + r0
            int r0 = r6 + 1
            r6 = r0
            goto L_0x00b2
        L_0x00c9:
            float r2 = r15 + r13
            float r1 = r16 + r14
            r0 = r14
            r8 = r13
            goto L_0x0013
        L_0x00d1:
            float r2 = r13 + r15
            float r1 = r14 + r16
            float r13 = r13 - r15
            float r14 = r14 - r16
            r0 = r14
            r8 = r13
            goto L_0x0013
        L_0x00dc:
            r0 = 1073741824(0x40000000, float:2.0)
            float r0 = r15 / r0
            r1 = 1073741824(0x40000000, float:2.0)
            float r3 = r16 / r1
            float r2 = r13 + r0
            float r1 = r14 + r3
            float r13 = r13 - r0
            float r14 = r14 - r3
            r0 = r14
            r8 = r13
            goto L_0x0013
        L_0x00ee:
            int r2 = r3 + 1
        L_0x00f0:
            int r3 = r3 + 1
            goto L_0x005b
        L_0x00f4:
            int r0 = r11.textAlign
            r1 = 22
            if (r0 != r1) goto L_0x014a
            r4 = r8
            goto L_0x0079
        L_0x00fd:
            int r0 = r11.textAlignY
            r1 = 102(0x66, float:1.43E-43)
            if (r0 != r1) goto L_0x012a
            float r0 = r11.textDescent()
            float r0 = r6 - r0
            float r1 = r11.textLeading
            int r2 = r8 + -1
            float r2 = (float) r2
            float r1 = r1 * r2
            float r5 = r0 - r1
            r0 = 0
            r6 = r0
        L_0x0113:
            if (r6 >= r8) goto L_0x0149
            char[] r1 = r11.textBuffer
            int[] r0 = r11.textBreakStart
            r2 = r0[r6]
            int[] r0 = r11.textBreakStop
            r3 = r0[r6]
            r0 = r11
            r0.textLineAlignImpl(r1, r2, r3, r4, r5)
            float r0 = r11.textLeading
            float r5 = r5 + r0
            int r0 = r6 + 1
            r6 = r0
            goto L_0x0113
        L_0x012a:
            float r0 = r11.textAscent()
            float r5 = r7 + r0
            r0 = 0
            r6 = r0
        L_0x0132:
            if (r6 >= r8) goto L_0x0149
            char[] r1 = r11.textBuffer
            int[] r0 = r11.textBreakStart
            r2 = r0[r6]
            int[] r0 = r11.textBreakStop
            r3 = r0[r6]
            r0 = r11
            r0.textLineAlignImpl(r1, r2, r3, r4, r5)
            float r0 = r11.textLeading
            float r5 = r5 + r0
            int r0 = r6 + 1
            r6 = r0
            goto L_0x0132
        L_0x0149:
            return
        L_0x014a:
            r4 = r9
            goto L_0x0079
        L_0x014d:
            r6 = r1
            r7 = r0
            goto L_0x001e
        L_0x0151:
            r9 = r8
            r8 = r2
            goto L_0x0018
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.core.PGraphics.text(java.lang.String, float, float, float, float):void");
    }

    public void textAlign(int i) {
        textAlign(i, 0);
    }

    public void textAlign(int i, int i2) {
        this.textAlign = i;
        this.textAlignY = i2;
    }

    public float textAscent() {
        if (this.textFont == null) {
            defaultFontOrDeath("textAscent");
        }
        return this.textFont.ascent() * this.textSize;
    }

    /* access modifiers changed from: protected */
    public void textCharImpl(char c, float f, float f2) {
        Glyph glyph = this.textFont.getGlyph(c);
        if (glyph != null && this.textMode == 4) {
            float f3 = ((((float) glyph.leftExtent) / ((float) this.textFont.size)) * this.textSize) + f;
            float f4 = f2 - ((((float) glyph.topExtent) / ((float) this.textFont.size)) * this.textSize);
            float f5 = ((((float) glyph.width) / ((float) this.textFont.size)) * this.textSize) + f3;
            float f6 = f4 + ((((float) glyph.height) / ((float) this.textFont.size)) * this.textSize);
            textCharModelImpl(glyph.image, f3, f4, f5, f6, glyph.width, glyph.height);
        }
    }

    /* access modifiers changed from: protected */
    public void textCharModelImpl(PImage pImage, float f, float f2, float f3, float f4, int i, int i2) {
        boolean z = this.tint;
        int i3 = this.tintColor;
        tint(this.fillColor);
        imageImpl(pImage, f, f2, f3, f4, 0, 0, i, i2);
        if (z) {
            tint(i3);
        } else {
            noTint();
        }
    }

    public float textDescent() {
        if (this.textFont == null) {
            defaultFontOrDeath("textDescent");
        }
        return this.textFont.descent() * this.textSize;
    }

    public void textFont(PFont pFont) {
        if (pFont != null) {
            this.textFont = pFont;
            textSize((float) pFont.size);
            return;
        }
        throw new RuntimeException(PConstants.ERROR_TEXTFONT_NULL_PFONT);
    }

    public void textFont(PFont pFont, float f) {
        textFont(pFont);
        textSize(f);
    }

    public void textLeading(float f) {
        this.textLeading = f;
    }

    /* access modifiers changed from: protected */
    public void textLineAlignImpl(char[] cArr, int i, int i2, float f, float f2) {
        float f3 = this.textAlign == 3 ? f - (textWidthImpl(cArr, i, i2) / 2.0f) : this.textAlign == 22 ? f - textWidthImpl(cArr, i, i2) : f;
        textLineImpl(cArr, i, i2, f3, f2);
    }

    /* access modifiers changed from: protected */
    public void textLineImpl(char[] cArr, int i, int i2, float f, float f2) {
        while (i < i2) {
            textCharImpl(cArr[i], f, f2);
            f += textWidth(cArr[i]);
            i++;
        }
    }

    public void textMode(int i) {
        if (i == 21 || i == 22) {
            showWarning("Since Processing beta, textMode() is now textAlign().");
            return;
        }
        if (i == 256) {
            showWarning("textMode(SCREEN) has been removed from Processing 2.0.");
        }
        if (textModeCheck(i)) {
            this.textMode = i;
            return;
        }
        String valueOf = String.valueOf(i);
        switch (i) {
            case 4:
                valueOf = "MODEL";
                break;
            case 5:
                valueOf = "SHAPE";
                break;
        }
        showWarning("textMode(" + valueOf + ") is not supported by this renderer.");
    }

    /* access modifiers changed from: protected */
    public boolean textModeCheck(int i) {
        return true;
    }

    public void textSize(float f) {
        if (this.textFont == null) {
            defaultFontOrDeath("textSize", f);
        }
        this.textSize = f;
        this.textLeading = (textAscent() + textDescent()) * 1.275f;
    }

    public float textWidth(char c) {
        this.textWidthBuffer[0] = c;
        return textWidthImpl(this.textWidthBuffer, 0, 1);
    }

    public float textWidth(String str) {
        int i = 0;
        if (this.textFont == null) {
            defaultFontOrDeath("textWidth");
        }
        int length = str.length();
        if (length > this.textWidthBuffer.length) {
            this.textWidthBuffer = new char[(length + 10)];
        }
        str.getChars(0, length, this.textWidthBuffer, 0);
        float f = 0.0f;
        int i2 = 0;
        while (i2 < length) {
            if (this.textWidthBuffer[i2] == 10) {
                f = Math.max(f, textWidthImpl(this.textWidthBuffer, i, i2));
                i = i2 + 1;
            }
            i2++;
        }
        return i < length ? Math.max(f, textWidthImpl(this.textWidthBuffer, i, i2)) : f;
    }

    /* access modifiers changed from: protected */
    public float textWidthImpl(char[] cArr, int i, int i2) {
        float f = 0.0f;
        while (i < i2) {
            f += this.textFont.width(cArr[i]) * this.textSize;
            i++;
        }
        return f;
    }

    public void texture(PImage pImage) {
        this.textureImage = pImage;
    }

    public void textureMode(int i) {
        this.textureMode = i;
    }

    public void textureWrap(int i) {
        showMissingWarning("textureWrap");
    }

    public void tint(float f) {
        colorCalc(f);
        tintFromCalc();
    }

    public void tint(float f, float f2) {
        colorCalc(f, f2);
        tintFromCalc();
    }

    public void tint(float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        tintFromCalc();
    }

    public void tint(float f, float f2, float f3, float f4) {
        colorCalc(f, f2, f3, f4);
        tintFromCalc();
    }

    public void tint(int i) {
        colorCalc(i);
        tintFromCalc();
    }

    public void tint(int i, float f) {
        colorCalc(i, f);
        tintFromCalc();
    }

    /* access modifiers changed from: protected */
    public void tintFromCalc() {
        this.tint = true;
        this.tintR = this.calcR;
        this.tintG = this.calcG;
        this.tintB = this.calcB;
        this.tintA = this.calcA;
        this.tintRi = this.calcRi;
        this.tintGi = this.calcGi;
        this.tintBi = this.calcBi;
        this.tintAi = this.calcAi;
        this.tintColor = this.calcColor;
        this.tintAlpha = this.calcAlpha;
    }

    public void translate(float f, float f2) {
        showMissingWarning("translate");
    }

    public void translate(float f, float f2, float f3) {
        showMissingWarning("translate");
    }

    public void triangle(float f, float f2, float f3, float f4, float f5, float f6) {
        beginShape(9);
        vertex(f, f2);
        vertex(f3, f4);
        vertex(f5, f6);
        endShape();
    }

    public void vertex(float f, float f2) {
        vertexCheck();
        float[] fArr = this.vertices[this.vertexCount];
        this.curveVertexCount = 0;
        fArr[0] = f;
        fArr[1] = f2;
        fArr[2] = 0.0f;
        fArr[12] = this.edge ? 1.0f : 0.0f;
        boolean z = this.textureImage != null;
        if (this.fill || z) {
            if (!z) {
                fArr[3] = this.fillR;
                fArr[4] = this.fillG;
                fArr[5] = this.fillB;
                fArr[6] = this.fillA;
            } else if (this.tint) {
                fArr[3] = this.tintR;
                fArr[4] = this.tintG;
                fArr[5] = this.tintB;
                fArr[6] = this.tintA;
            } else {
                fArr[3] = 1.0f;
                fArr[4] = 1.0f;
                fArr[5] = 1.0f;
                fArr[6] = 1.0f;
            }
        }
        if (this.stroke) {
            fArr[13] = this.strokeR;
            fArr[14] = this.strokeG;
            fArr[15] = this.strokeB;
            fArr[16] = this.strokeA;
            fArr[17] = this.strokeWeight;
        }
        fArr[7] = this.textureU;
        fArr[8] = this.textureV;
        if (this.autoNormal) {
            float f3 = (this.normalX * this.normalX) + (this.normalY * this.normalY) + (this.normalZ * this.normalZ);
            if (f3 < 1.0E-4f) {
                fArr[36] = 0.0f;
            } else {
                if (Math.abs(f3 - DEFAULT_STROKE_WEIGHT) > 1.0E-4f) {
                    float sqrt = PApplet.sqrt(f3);
                    this.normalX /= sqrt;
                    this.normalY /= sqrt;
                    this.normalZ /= sqrt;
                }
                fArr[36] = 1.0f;
            }
        } else {
            fArr[36] = 1.0f;
        }
        fArr[9] = this.normalX;
        fArr[10] = this.normalY;
        fArr[11] = this.normalZ;
        this.vertexCount++;
    }

    public void vertex(float f, float f2, float f3) {
        vertexCheck();
        float[] fArr = this.vertices[this.vertexCount];
        if (this.shape == 20 && this.vertexCount > 0) {
            float[] fArr2 = this.vertices[this.vertexCount - 1];
            if (Math.abs(fArr2[0] - f) < 1.0E-4f && Math.abs(fArr2[1] - f2) < 1.0E-4f && Math.abs(fArr2[2] - f3) < 1.0E-4f) {
                return;
            }
        }
        this.curveVertexCount = 0;
        fArr[0] = f;
        fArr[1] = f2;
        fArr[2] = f3;
        fArr[12] = this.edge ? 1.0f : 0.0f;
        boolean z = this.textureImage != null;
        if (this.fill || z) {
            if (!z) {
                fArr[3] = this.fillR;
                fArr[4] = this.fillG;
                fArr[5] = this.fillB;
                fArr[6] = this.fillA;
            } else if (this.tint) {
                fArr[3] = this.tintR;
                fArr[4] = this.tintG;
                fArr[5] = this.tintB;
                fArr[6] = this.tintA;
            } else {
                fArr[3] = 1.0f;
                fArr[4] = 1.0f;
                fArr[5] = 1.0f;
                fArr[6] = 1.0f;
            }
        }
        if (this.stroke) {
            fArr[13] = this.strokeR;
            fArr[14] = this.strokeG;
            fArr[15] = this.strokeB;
            fArr[16] = this.strokeA;
            fArr[17] = this.strokeWeight;
        }
        fArr[7] = this.textureU;
        fArr[8] = this.textureV;
        if (this.autoNormal) {
            float f4 = (this.normalX * this.normalX) + (this.normalY * this.normalY) + (this.normalZ * this.normalZ);
            if (f4 < 1.0E-4f) {
                fArr[36] = 0.0f;
            } else {
                if (Math.abs(f4 - DEFAULT_STROKE_WEIGHT) > 1.0E-4f) {
                    float sqrt = PApplet.sqrt(f4);
                    this.normalX /= sqrt;
                    this.normalY /= sqrt;
                    this.normalZ /= sqrt;
                }
                fArr[36] = 1.0f;
            }
        } else {
            fArr[36] = 1.0f;
        }
        fArr[9] = this.normalX;
        fArr[10] = this.normalY;
        fArr[11] = this.normalZ;
        this.vertexCount++;
    }

    public void vertex(float f, float f2, float f3, float f4) {
        vertexTexture(f3, f4);
        vertex(f, f2);
    }

    public void vertex(float f, float f2, float f3, float f4, float f5) {
        vertexTexture(f4, f5);
        vertex(f, f2, f3);
    }

    public void vertex(float[] fArr) {
        vertexCheck();
        this.curveVertexCount = 0;
        System.arraycopy(fArr, 0, this.vertices[this.vertexCount], 0, 37);
        this.vertexCount++;
    }

    /* access modifiers changed from: protected */
    public void vertexCheck() {
        if (this.vertexCount == this.vertices.length) {
            float[][] fArr = (float[][]) Array.newInstance(Float.TYPE, new int[]{this.vertexCount << 1, 37});
            System.arraycopy(this.vertices, 0, fArr, 0, this.vertexCount);
            this.vertices = fArr;
        }
    }

    /* access modifiers changed from: protected */
    public void vertexTexture(float f, float f2) {
        if (this.textureImage == null) {
            throw new RuntimeException("You must first call texture() before using u and v coordinates with vertex()");
        }
        if (this.textureMode == 2) {
            f /= (float) this.textureImage.width;
            f2 /= (float) this.textureImage.height;
        }
        this.textureU = f;
        this.textureV = f2;
    }
}

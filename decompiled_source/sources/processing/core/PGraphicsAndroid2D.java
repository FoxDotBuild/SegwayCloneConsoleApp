package processing.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.zip.GZIPInputStream;
import processing.data.XML;

public class PGraphicsAndroid2D extends PGraphics {
    static int[] getset = new int[1];
    boolean breakShape;
    public Canvas canvas;
    float[] curveCoordX;
    float[] curveCoordY;
    float[] curveDrawX;
    float[] curveDrawY;
    Paint fillPaint = new Paint();
    RectF imageImplDstRect;
    Rect imageImplSrcRect;
    Path path = new Path();
    RectF rect = new RectF();
    float[] screenPoint;
    Paint strokePaint;
    Paint tintPaint;
    float[] transform = new float[9];

    public PGraphicsAndroid2D() {
        this.fillPaint.setStyle(Style.FILL);
        this.strokePaint = new Paint();
        this.strokePaint.setStyle(Style.STROKE);
        this.tintPaint = new Paint(2);
    }

    /* access modifiers changed from: protected */
    public void allocate() {
        this.bitmap = Bitmap.createBitmap(this.width, this.height, Config.ARGB_8888);
        this.canvas = new Canvas(this.bitmap);
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6) {
        Matrix matrix = new Matrix();
        matrix.setValues(new float[]{f, f2, f3, f4, f5, f6, 0.0f, 0.0f, 1.0f});
        this.canvas.concat(matrix);
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        showVariationWarning("applyMatrix");
    }

    /* access modifiers changed from: protected */
    public void arcImpl(float f, float f2, float f3, float f4, float f5, float f6, int i) {
        float f7;
        if (f6 - f5 >= 6.2831855f) {
            ellipseImpl(f, f2, f3, f4);
            return;
        }
        float f8 = f5 * 57.295776f;
        float f9 = f6 * 57.295776f;
        while (f8 < 0.0f) {
            f8 += 360.0f;
            f9 += 360.0f;
        }
        if (f8 > f9) {
            f7 = f9;
        } else {
            f7 = f8;
            f8 = f9;
        }
        float f10 = f8 - f7;
        this.rect.set(f, f2, f + f3, f2 + f4);
        if (i == 0) {
            if (this.fill) {
                this.canvas.drawArc(this.rect, f7, f10, true, this.fillPaint);
            }
            if (this.stroke) {
                this.canvas.drawArc(this.rect, f7, f10, false, this.strokePaint);
            }
        } else if (i == 1) {
            if (this.fill) {
                showMissingWarning("arc");
            }
            if (this.stroke) {
                this.canvas.drawArc(this.rect, f7, f10, false, this.strokePaint);
            }
        } else if (i == 2) {
            showMissingWarning("arc");
        } else if (i == 3) {
            if (this.fill) {
                this.canvas.drawArc(this.rect, f7, f10, true, this.fillPaint);
            }
            if (this.stroke) {
                this.canvas.drawArc(this.rect, f7, f10, true, this.strokePaint);
            }
        }
    }

    public void backgroundImpl() {
        this.canvas.drawColor(this.backgroundColor);
    }

    public void beginDraw() {
        checkSettings();
        resetMatrix();
        this.vertexCount = 0;
    }

    public void beginRaw(PGraphics pGraphics) {
        showMethodWarning("beginRaw");
    }

    public void beginShape(int i) {
        this.shape = i;
        this.vertexCount = 0;
        this.curveVertexCount = 0;
    }

    /* access modifiers changed from: protected */
    public void beginTextScreenMode() {
        loadPixels();
    }

    public void bezierDetail(int i) {
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        bezierVertexCheck();
        this.path.cubicTo(f, f2, f3, f4, f5, f6);
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        showDepthWarningXYZ("bezierVertex");
    }

    public void box(float f, float f2, float f3) {
        showMethodWarning("box");
    }

    public void breakShape() {
        this.breakShape = true;
    }

    public void copy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.rect.set((float) i, (float) i2, (float) (i + i3), (float) (i2 + i4));
        this.canvas.drawBitmap(this.bitmap, new Rect(i5, i6, i5 + i7, i6 + i8), this.rect, null);
    }

    public void curveDetail(int i) {
    }

    public void curveVertex(float f, float f2, float f3) {
        showDepthWarningXYZ("curveVertex");
    }

    /* access modifiers changed from: protected */
    public void curveVertexCheck() {
        super.curveVertexCheck();
        if (this.curveCoordX == null) {
            this.curveCoordX = new float[4];
            this.curveCoordY = new float[4];
            this.curveDrawX = new float[4];
            this.curveDrawY = new float[4];
        }
    }

    /* access modifiers changed from: protected */
    public void curveVertexSegment(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.curveCoordX[0] = f;
        this.curveCoordY[0] = f2;
        this.curveCoordX[1] = f3;
        this.curveCoordY[1] = f4;
        this.curveCoordX[2] = f5;
        this.curveCoordY[2] = f6;
        this.curveCoordX[3] = f7;
        this.curveCoordY[3] = f8;
        this.curveToBezierMatrix.mult(this.curveCoordX, this.curveDrawX);
        this.curveToBezierMatrix.mult(this.curveCoordY, this.curveDrawY);
        if (this.vertexCount == 0) {
            this.path.moveTo(this.curveDrawX[0], this.curveDrawY[0]);
            this.vertexCount = 1;
        }
        this.path.cubicTo(this.curveDrawX[1], this.curveDrawY[1], this.curveDrawX[2], this.curveDrawY[2], this.curveDrawX[3], this.curveDrawY[3]);
    }

    public void dispose() {
        this.bitmap.recycle();
    }

    /* access modifiers changed from: protected */
    public void drawPath() {
        if (this.fill) {
            this.canvas.drawPath(this.path, this.fillPaint);
        }
        if (this.stroke) {
            this.canvas.drawPath(this.path, this.strokePaint);
        }
    }

    /* access modifiers changed from: protected */
    public void ellipseImpl(float f, float f2, float f3, float f4) {
        this.rect.set(f, f2, f + f3, f2 + f4);
        if (this.fill) {
            this.canvas.drawOval(this.rect, this.fillPaint);
        }
        if (this.stroke) {
            this.canvas.drawOval(this.rect, this.strokePaint);
        }
    }

    /* JADX INFO: finally extract failed */
    public void endDraw() {
        if (this.primarySurface) {
            try {
                Canvas lockCanvas = this.parent.getSurfaceHolder().lockCanvas(null);
                if (lockCanvas != null) {
                    lockCanvas.drawBitmap(this.bitmap, new Matrix(), null);
                }
                if (lockCanvas != null) {
                    this.parent.getSurfaceHolder().unlockCanvasAndPost(lockCanvas);
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    this.parent.getSurfaceHolder().unlockCanvasAndPost(null);
                }
                throw th;
            }
        } else {
            loadPixels();
        }
        setModified();
        super.updatePixels();
    }

    public void endRaw() {
        showMethodWarning("endRaw");
    }

    public void endShape(int i) {
        if (this.shape == 3 && this.stroke && this.vertexCount > 0) {
            Matrix matrix = this.canvas.getMatrix();
            if (this.strokeWeight != 1.0f || !matrix.isIdentity()) {
                float f = this.strokeWeight / 2.0f;
                this.strokePaint.setStyle(Style.FILL);
                for (int i2 = 0; i2 < this.vertexCount; i2++) {
                    float f2 = this.vertices[i2][0];
                    float f3 = this.vertices[i2][1];
                    this.rect.set(f2 - f, f3 - f, f2 + f, f3 + f);
                    this.canvas.drawOval(this.rect, this.strokePaint);
                }
                this.strokePaint.setStyle(Style.STROKE);
            } else {
                if (this.screenPoint == null) {
                    this.screenPoint = new float[2];
                }
                for (int i3 = 0; i3 < this.vertexCount; i3++) {
                    this.screenPoint[0] = this.vertices[i3][0];
                    this.screenPoint[1] = this.vertices[i3][1];
                    matrix.mapPoints(this.screenPoint);
                    set(PApplet.round(this.screenPoint[0]), PApplet.round(this.screenPoint[1]), this.strokeColor);
                    float f4 = this.vertices[i3][0];
                    float f5 = this.vertices[i3][1];
                    set(PApplet.round(screenX(f4, f5)), PApplet.round(screenY(f4, f5)), this.strokeColor);
                }
            }
        } else if (this.shape == 20 && !this.path.isEmpty()) {
            if (i == 2) {
                this.path.close();
            }
            drawPath();
        }
        this.shape = 0;
    }

    /* access modifiers changed from: protected */
    public void endTextScreenMode() {
        updatePixels();
    }

    /* access modifiers changed from: protected */
    public void fillFromCalc() {
        super.fillFromCalc();
        this.fillPaint.setColor(this.fillColor);
        this.fillPaint.setShader(null);
    }

    public int get(int i, int i2) {
        if (i < 0 || i2 < 0 || i >= this.width || i2 >= this.height) {
            return 0;
        }
        return this.bitmap.getPixel(i, i2);
    }

    public PImage get() {
        return get(0, 0, this.width, this.height);
    }

    public PMatrix2D getMatrix(PMatrix2D pMatrix2D) {
        PMatrix2D pMatrix2D2 = pMatrix2D == null ? new PMatrix2D() : pMatrix2D;
        Matrix matrix = new Matrix();
        this.canvas.getMatrix(matrix);
        matrix.getValues(this.transform);
        pMatrix2D2.set(this.transform[0], this.transform[1], this.transform[2], this.transform[3], this.transform[4], this.transform[5]);
        return pMatrix2D2;
    }

    public PMatrix3D getMatrix(PMatrix3D pMatrix3D) {
        showVariationWarning("getMatrix");
        return pMatrix3D;
    }

    public PMatrix getMatrix() {
        return getMatrix((PMatrix2D) null);
    }

    /* access modifiers changed from: protected */
    public void imageImpl(PImage pImage, float f, float f2, float f3, float f4, int i, int i2, int i3, int i4) {
        if (pImage.bitmap == null && pImage.format == 4) {
            pImage.bitmap = Bitmap.createBitmap(pImage.width, pImage.height, Config.ARGB_8888);
            int[] iArr = new int[pImage.pixels.length];
            for (int i5 = 0; i5 < iArr.length; i5++) {
                iArr[i5] = (pImage.pixels[i5] << 24) | 16777215;
            }
            pImage.bitmap.setPixels(iArr, 0, pImage.width, 0, 0, pImage.width, pImage.height);
            pImage.modified = false;
        }
        if (!(pImage.bitmap != null && pImage.width == pImage.bitmap.getWidth() && pImage.height == pImage.bitmap.getHeight())) {
            pImage.bitmap = Bitmap.createBitmap(pImage.width, pImage.height, Config.ARGB_8888);
            pImage.modified = true;
        }
        if (pImage.modified) {
            if (!pImage.bitmap.isMutable()) {
                pImage.bitmap = Bitmap.createBitmap(pImage.width, pImage.height, Config.ARGB_8888);
            }
            pImage.bitmap.setPixels(pImage.pixels, 0, pImage.width, 0, 0, pImage.width, pImage.height);
            pImage.modified = false;
        }
        if (this.imageImplSrcRect == null) {
            this.imageImplSrcRect = new Rect(i, i2, i3, i4);
            this.imageImplDstRect = new RectF(f, f2, f3, f4);
        } else {
            this.imageImplSrcRect.set(i, i2, i3, i4);
            this.imageImplDstRect.set(f, f2, f3, f4);
        }
        this.canvas.drawBitmap(pImage.bitmap, this.imageImplSrcRect, this.imageImplDstRect, this.tint ? this.tintPaint : null);
    }

    public void line(float f, float f2, float f3, float f4) {
        if (this.stroke) {
            this.canvas.drawLine(f, f2, f3, f4, this.strokePaint);
        }
    }

    public void loadPixels() {
        if (this.pixels == null || this.pixels.length != this.width * this.height) {
            this.pixels = new int[(this.width * this.height)];
        }
        this.bitmap.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
    }

    public PShape loadShape(String str) {
        String extension = PApplet.getExtension(str);
        if (extension.equals("svg")) {
            return new PShapeSVG(this.parent.loadXML(str));
        }
        if (extension.equals("svgz")) {
            try {
                return new PShapeSVG(new XML((InputStream) new GZIPInputStream(this.parent.createInput(str))));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            PGraphics.showWarning("Unsupported format");
            return null;
        }
    }

    public void mask(PImage pImage) {
        showMethodWarning("mask");
    }

    public void mask(int[] iArr) {
        showMethodWarning("mask");
    }

    public void noSmooth() {
        this.smooth = false;
        this.strokePaint.setAntiAlias(false);
        this.fillPaint.setAntiAlias(false);
    }

    public void point(float f, float f2) {
        beginShape(3);
        vertex(f, f2);
        endShape();
    }

    public void popMatrix() {
        this.canvas.restore();
    }

    public void printMatrix() {
        getMatrix((PMatrix2D) null).print();
    }

    public void pushMatrix() {
        this.canvas.save(1);
    }

    public void quad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.path.reset();
        this.path.moveTo(f, f2);
        this.path.lineTo(f3, f4);
        this.path.lineTo(f5, f6);
        this.path.lineTo(f7, f8);
        this.path.close();
        drawPath();
    }

    public void quadraticVertex(float f, float f2, float f3, float f4) {
        bezierVertexCheck();
        this.path.quadTo(f, f2, f3, f4);
    }

    public void quadraticVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        showDepthWarningXYZ("quadVertex");
    }

    /* access modifiers changed from: protected */
    public void rectImpl(float f, float f2, float f3, float f4) {
        if (this.fill) {
            this.canvas.drawRect(f, f2, f3, f4, this.fillPaint);
        }
        if (this.stroke) {
            this.canvas.drawRect(f, f2, f3, f4, this.strokePaint);
        }
    }

    public void requestDraw() {
        this.parent.handleDraw();
    }

    public void resetMatrix() {
        this.canvas.setMatrix(new Matrix());
    }

    public void resize(int i, int i2) {
        showMethodWarning("resize");
    }

    public void rotate(float f) {
        this.canvas.rotate(57.295776f * f);
    }

    public void rotate(float f, float f2, float f3, float f4) {
        showVariationWarning("rotate");
    }

    public void rotateX(float f) {
        showDepthWarning("rotateX");
    }

    public void rotateY(float f) {
        showDepthWarning("rotateY");
    }

    public void rotateZ(float f) {
        showDepthWarning("rotateZ");
    }

    public void scale(float f) {
        this.canvas.scale(f, f);
    }

    public void scale(float f, float f2) {
        this.canvas.scale(f, f2);
    }

    public void scale(float f, float f2, float f3) {
        showDepthWarningXYZ("scale");
    }

    public float screenX(float f, float f2) {
        if (this.screenPoint == null) {
            this.screenPoint = new float[2];
        }
        this.screenPoint[0] = f;
        this.screenPoint[1] = f2;
        this.canvas.getMatrix().mapPoints(this.screenPoint);
        return this.screenPoint[0];
    }

    public float screenX(float f, float f2, float f3) {
        showDepthWarningXYZ("screenX");
        return 0.0f;
    }

    public float screenY(float f, float f2) {
        if (this.screenPoint == null) {
            this.screenPoint = new float[2];
        }
        this.screenPoint[0] = f;
        this.screenPoint[1] = f2;
        this.canvas.getMatrix().mapPoints(this.screenPoint);
        return this.screenPoint[1];
    }

    public float screenY(float f, float f2, float f3) {
        showDepthWarningXYZ("screenY");
        return 0.0f;
    }

    public float screenZ(float f, float f2, float f3) {
        showDepthWarningXYZ("screenZ");
        return 0.0f;
    }

    public void set(int i, int i2, int i3) {
        if (i >= 0 && i2 >= 0 && i < this.width && i2 < this.height) {
            this.bitmap.setPixel(i, i2, i3);
        }
    }

    public void set(int i, int i2, PImage pImage) {
        if (pImage.format == 4) {
            throw new RuntimeException("set() not available for ALPHA images");
        } else if (pImage.bitmap == null) {
            this.canvas.drawBitmap(pImage.pixels, 0, pImage.width, i, i2, pImage.width, pImage.height, false, null);
        } else {
            if (!(pImage.width == pImage.bitmap.getWidth() && pImage.height == pImage.bitmap.getHeight())) {
                pImage.bitmap = Bitmap.createBitmap(pImage.width, pImage.height, Config.ARGB_8888);
                pImage.modified = true;
            }
            if (pImage.modified) {
                if (!pImage.bitmap.isMutable()) {
                    pImage.bitmap = Bitmap.createBitmap(pImage.width, pImage.height, Config.ARGB_8888);
                }
                pImage.bitmap.setPixels(pImage.pixels, 0, pImage.width, 0, 0, pImage.width, pImage.height);
                pImage.modified = false;
            }
            this.canvas.save(1);
            this.canvas.setMatrix(null);
            this.canvas.drawBitmap(pImage.bitmap, (float) i, (float) i2, null);
            this.canvas.restore();
        }
    }

    public void setMatrix(PMatrix2D pMatrix2D) {
        Matrix matrix = new Matrix();
        matrix.setValues(new float[]{pMatrix2D.m00, pMatrix2D.m01, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, pMatrix2D.m12, 0.0f, 0.0f, 1.0f});
        this.canvas.setMatrix(matrix);
    }

    public void setMatrix(PMatrix3D pMatrix3D) {
        showVariationWarning("setMatrix");
    }

    public void setSize(int i, int i2) {
        this.width = i;
        this.height = i2;
        this.width1 = this.width - 1;
        this.height1 = this.height - 1;
        allocate();
        reapplySettings();
    }

    public void shearX(float f) {
        this.canvas.skew((float) Math.tan((double) f), 0.0f);
    }

    public void shearY(float f) {
        this.canvas.skew(0.0f, (float) Math.tan((double) f));
    }

    public void smooth() {
        this.smooth = true;
        this.strokePaint.setAntiAlias(true);
        this.fillPaint.setAntiAlias(true);
    }

    public void sphere(float f) {
        showMethodWarning("sphere");
    }

    public void strokeCap(int i) {
        super.strokeCap(i);
        if (this.strokeCap == 2) {
            this.strokePaint.setStrokeCap(Cap.ROUND);
        } else if (this.strokeCap == 4) {
            this.strokePaint.setStrokeCap(Cap.SQUARE);
        } else {
            this.strokePaint.setStrokeCap(Cap.BUTT);
        }
    }

    /* access modifiers changed from: protected */
    public void strokeFromCalc() {
        super.strokeFromCalc();
        this.strokePaint.setColor(this.strokeColor);
        this.strokePaint.setShader(null);
    }

    public void strokeJoin(int i) {
        super.strokeJoin(i);
        if (this.strokeJoin == 8) {
            this.strokePaint.setStrokeJoin(Join.MITER);
        } else if (this.strokeJoin == 2) {
            this.strokePaint.setStrokeJoin(Join.ROUND);
        } else {
            this.strokePaint.setStrokeJoin(Join.BEVEL);
        }
    }

    public void strokeWeight(float f) {
        super.strokeWeight(f);
        this.strokePaint.setStrokeWidth(f);
    }

    public void textFont(PFont pFont) {
        super.textFont(pFont);
        this.fillPaint.setTypeface((Typeface) pFont.getNative());
    }

    /* access modifiers changed from: protected */
    public void textLineImpl(char[] cArr, int i, int i2, float f, float f2) {
        if (((Typeface) this.textFont.getNative()) == null) {
            showWarning("Inefficient font rendering: use createFont() with a TTF/OTF instead of loadFont().");
            super.textLineImpl(cArr, i, i2, f, f2);
            return;
        }
        this.fillPaint.setAntiAlias(this.textFont.smooth);
        char[] cArr2 = cArr;
        int i3 = i;
        this.canvas.drawText(cArr2, i3, i2 - i, f, f2, this.fillPaint);
        this.fillPaint.setAntiAlias(this.smooth);
    }

    /* access modifiers changed from: protected */
    public boolean textModeCheck(int i) {
        return i == 4;
    }

    public void textSize(float f) {
        if (this.textFont == null) {
            defaultFontOrDeath("textSize", f);
        }
        if (((Typeface) this.textFont.getNative()) != null) {
            this.fillPaint.setTextSize(f);
        }
        this.textSize = f;
        this.textLeading = (textAscent() + textDescent()) * 1.275f;
    }

    /* access modifiers changed from: protected */
    public float textWidthImpl(char[] cArr, int i, int i2) {
        if (((Typeface) this.textFont.getNative()) == null) {
            return super.textWidthImpl(cArr, i, i2);
        }
        return this.fillPaint.measureText(cArr, i, i2 - i);
    }

    public void texture(PImage pImage) {
        showMethodWarning("texture");
    }

    /* access modifiers changed from: protected */
    public void tintFromCalc() {
        super.tintFromCalc();
        this.tintPaint.setColorFilter(new PorterDuffColorFilter(this.tintColor, Mode.MULTIPLY));
    }

    public void translate(float f, float f2) {
        this.canvas.translate(f, f2);
    }

    public void triangle(float f, float f2, float f3, float f4, float f5, float f6) {
        this.path.reset();
        this.path.moveTo(f, f2);
        this.path.lineTo(f3, f4);
        this.path.lineTo(f5, f6);
        this.path.close();
        drawPath();
    }

    public void updatePixels() {
        this.bitmap.setPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
    }

    public void updatePixels(int i, int i2, int i3, int i4) {
        if (!(i == 0 && i2 == 0 && i3 == this.width && i4 == this.height)) {
            showVariationWarning("updatePixels(x, y, w, h)");
        }
        updatePixels();
    }

    public void vertex(float f, float f2) {
        if (this.shape != 20) {
            this.curveVertexCount = 0;
            if (this.vertexCount == this.vertices.length) {
                float[][] fArr = (float[][]) Array.newInstance(Float.TYPE, new int[]{this.vertexCount << 1, 37});
                System.arraycopy(this.vertices, 0, fArr, 0, this.vertexCount);
                this.vertices = fArr;
            }
            this.vertices[this.vertexCount][0] = f;
            this.vertices[this.vertexCount][1] = f2;
            this.vertexCount++;
            switch (this.shape) {
                case 5:
                    if (this.vertexCount % 2 == 0) {
                        line(this.vertices[this.vertexCount - 2][0], this.vertices[this.vertexCount - 2][1], f, f2);
                        this.vertexCount = 0;
                        return;
                    }
                    return;
                case 9:
                    if (this.vertexCount % 3 == 0) {
                        triangle(this.vertices[this.vertexCount - 3][0], this.vertices[this.vertexCount - 3][1], this.vertices[this.vertexCount - 2][0], this.vertices[this.vertexCount - 2][1], f, f2);
                        this.vertexCount = 0;
                        return;
                    }
                    return;
                case 10:
                    if (this.vertexCount >= 3) {
                        triangle(this.vertices[this.vertexCount - 2][0], this.vertices[this.vertexCount - 2][1], f, f2, this.vertices[this.vertexCount - 3][0], this.vertices[this.vertexCount - 3][1]);
                        return;
                    }
                    return;
                case 11:
                    if (this.vertexCount >= 3) {
                        triangle(this.vertices[0][0], this.vertices[0][1], this.vertices[this.vertexCount - 2][0], this.vertices[this.vertexCount - 2][1], f, f2);
                        return;
                    }
                    return;
                case 16:
                case 17:
                    if (this.vertexCount % 4 == 0) {
                        quad(this.vertices[this.vertexCount - 4][0], this.vertices[this.vertexCount - 4][1], this.vertices[this.vertexCount - 3][0], this.vertices[this.vertexCount - 3][1], this.vertices[this.vertexCount - 2][0], this.vertices[this.vertexCount - 2][1], f, f2);
                        this.vertexCount = 0;
                        return;
                    }
                    return;
                case 18:
                    if (this.vertexCount >= 4 && this.vertexCount % 2 == 0) {
                        quad(this.vertices[this.vertexCount - 4][0], this.vertices[this.vertexCount - 4][1], this.vertices[this.vertexCount - 2][0], this.vertices[this.vertexCount - 2][1], f, f2, this.vertices[this.vertexCount - 3][0], this.vertices[this.vertexCount - 3][1]);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else if (this.vertexCount == 0) {
            this.path.reset();
            this.path.moveTo(f, f2);
            this.vertexCount = 1;
        } else if (this.breakShape) {
            this.path.moveTo(f, f2);
            this.breakShape = false;
        } else {
            this.path.lineTo(f, f2);
        }
    }

    public void vertex(float f, float f2, float f3) {
        showDepthWarningXYZ("vertex");
    }

    public void vertex(float f, float f2, float f3, float f4) {
        showVariationWarning("vertex(x, y, u, v)");
    }

    public void vertex(float f, float f2, float f3, float f4, float f5) {
        showDepthWarningXYZ("vertex");
    }
}

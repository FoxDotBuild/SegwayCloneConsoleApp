package processing.core;

public class PMatrix2D implements PMatrix {
    public float m00;
    public float m01;
    public float m02;
    public float m10;
    public float m11;
    public float m12;

    public PMatrix2D() {
        reset();
    }

    public PMatrix2D(float f, float f2, float f3, float f4, float f5, float f6) {
        set(f, f2, f3, f4, f5, f6);
    }

    public PMatrix2D(PMatrix pMatrix) {
        set(pMatrix);
    }

    private final float abs(float f) {
        return f < 0.0f ? -f : f;
    }

    private final float cos(float f) {
        return (float) Math.cos((double) f);
    }

    private final float max(float f, float f2) {
        return f > f2 ? f : f2;
    }

    private final float sin(float f) {
        return (float) Math.sin((double) f);
    }

    private final float tan(float f) {
        return (float) Math.tan((double) f);
    }

    public void apply(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = this.m00;
        float f8 = this.m01;
        this.m00 = (f * f7) + (f4 * f8);
        this.m01 = (f2 * f7) + (f5 * f8);
        this.m02 = (f7 * f3) + (f8 * f6) + this.m02;
        float f9 = this.m10;
        float f10 = this.m11;
        this.m10 = (f * f9) + (f4 * f10);
        this.m11 = (f2 * f9) + (f5 * f10);
        this.m12 = (f9 * f3) + (f10 * f6) + this.m12;
    }

    public void apply(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        throw new IllegalArgumentException("Cannot use this version of apply() on a PMatrix2D.");
    }

    public void apply(PMatrix2D pMatrix2D) {
        apply(pMatrix2D.m00, pMatrix2D.m01, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, pMatrix2D.m12);
    }

    public void apply(PMatrix3D pMatrix3D) {
        throw new IllegalArgumentException("Cannot use apply(PMatrix3D) on a PMatrix2D.");
    }

    public void apply(PMatrix pMatrix) {
        if (pMatrix instanceof PMatrix2D) {
            apply((PMatrix2D) pMatrix);
        } else if (pMatrix instanceof PMatrix3D) {
            apply((PMatrix3D) pMatrix);
        }
    }

    public float determinant() {
        return (this.m00 * this.m11) - (this.m01 * this.m10);
    }

    public PMatrix2D get() {
        PMatrix2D pMatrix2D = new PMatrix2D();
        pMatrix2D.set((PMatrix) this);
        return pMatrix2D;
    }

    public float[] get(float[] fArr) {
        if (fArr == null || fArr.length != 6) {
            fArr = new float[6];
        }
        fArr[0] = this.m00;
        fArr[1] = this.m01;
        fArr[2] = this.m02;
        fArr[3] = this.m10;
        fArr[4] = this.m11;
        fArr[5] = this.m12;
        return fArr;
    }

    public boolean invert() {
        float determinant = determinant();
        if (Math.abs(determinant) <= Float.MIN_VALUE) {
            return false;
        }
        float f = this.m00;
        float f2 = this.m01;
        float f3 = this.m02;
        float f4 = this.m10;
        float f5 = this.m11;
        float f6 = this.m12;
        this.m00 = f5 / determinant;
        this.m10 = (-f4) / determinant;
        this.m01 = (-f2) / determinant;
        this.m11 = f / determinant;
        this.m02 = ((f2 * f6) - (f5 * f3)) / determinant;
        this.m12 = ((f4 * f3) - (f * f6)) / determinant;
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isIdentity() {
        return this.m00 == 1.0f && this.m01 == 0.0f && this.m02 == 0.0f && this.m10 == 0.0f && this.m11 == 1.0f && this.m12 == 0.0f;
    }

    /* access modifiers changed from: protected */
    public boolean isWarped() {
        return (this.m00 == 1.0f && this.m01 == 0.0f && this.m10 == 0.0f && this.m11 == 1.0f) ? false : true;
    }

    public PVector mult(PVector pVector, PVector pVector2) {
        if (pVector2 == null) {
            pVector2 = new PVector();
        }
        pVector2.f68x = (this.m00 * pVector.f68x) + (this.m01 * pVector.f69y) + this.m02;
        pVector2.f69y = (this.m10 * pVector.f68x) + (this.m11 * pVector.f69y) + this.m12;
        return pVector2;
    }

    public float[] mult(float[] fArr, float[] fArr2) {
        if (fArr2 == null || fArr2.length != 2) {
            fArr2 = new float[2];
        }
        if (fArr == fArr2) {
            float f = (this.m10 * fArr[0]) + (this.m11 * fArr[1]) + this.m12;
            fArr2[0] = (this.m00 * fArr[0]) + (this.m01 * fArr[1]) + this.m02;
            fArr2[1] = f;
        } else {
            fArr2[0] = (this.m00 * fArr[0]) + (this.m01 * fArr[1]) + this.m02;
            fArr2[1] = (this.m10 * fArr[0]) + (this.m11 * fArr[1]) + this.m12;
        }
        return fArr2;
    }

    public float multX(float f, float f2) {
        return (this.m00 * f) + (this.m01 * f2) + this.m02;
    }

    public float multY(float f, float f2) {
        return (this.m10 * f) + (this.m11 * f2) + this.m12;
    }

    public void preApply(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = this.m02;
        float f8 = this.m12;
        float f9 = (f7 * f4) + (f8 * f5) + f6;
        this.m02 = (f7 * f) + (f8 * f2) + f3;
        this.m12 = f9;
        float f10 = this.m00;
        float f11 = this.m10;
        this.m00 = (f10 * f) + (f11 * f2);
        this.m10 = (f10 * f4) + (f11 * f5);
        float f12 = this.m01;
        float f13 = this.m11;
        this.m01 = (f12 * f) + (f13 * f2);
        this.m11 = (f12 * f4) + (f13 * f5);
    }

    public void preApply(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        throw new IllegalArgumentException("Cannot use this version of preApply() on a PMatrix2D.");
    }

    public void preApply(PMatrix2D pMatrix2D) {
        preApply(pMatrix2D.m00, pMatrix2D.m01, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, pMatrix2D.m12);
    }

    public void preApply(PMatrix3D pMatrix3D) {
        throw new IllegalArgumentException("Cannot use preApply(PMatrix3D) on a PMatrix2D.");
    }

    public void print() {
        int abs = (int) abs(max(PApplet.max(abs(this.m00), abs(this.m01), abs(this.m02)), PApplet.max(abs(this.m10), abs(this.m11), abs(this.m12))));
        int i = 1;
        if (!Float.isNaN((float) abs) && !Float.isInfinite((float) abs)) {
            while (true) {
                abs /= 10;
                if (abs == 0) {
                    break;
                }
                i++;
            }
        } else {
            i = 5;
        }
        System.out.println(PApplet.nfs(this.m00, i, 4) + " " + PApplet.nfs(this.m01, i, 4) + " " + PApplet.nfs(this.m02, i, 4));
        System.out.println(PApplet.nfs(this.m10, i, 4) + " " + PApplet.nfs(this.m11, i, 4) + " " + PApplet.nfs(this.m12, i, 4));
        System.out.println();
    }

    public void reset() {
        set(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    public void rotate(float f) {
        float sin = sin(f);
        float cos = cos(f);
        float f2 = this.m00;
        float f3 = this.m01;
        this.m00 = (cos * f2) + (sin * f3);
        this.m01 = (f2 * (-sin)) + (f3 * cos);
        float f4 = this.m10;
        float f5 = this.m11;
        this.m10 = (cos * f4) + (sin * f5);
        this.m11 = ((-sin) * f4) + (cos * f5);
    }

    public void rotate(float f, float f2, float f3, float f4) {
        throw new IllegalArgumentException("Cannot use this version of rotate() on a PMatrix2D.");
    }

    public void rotateX(float f) {
        throw new IllegalArgumentException("Cannot use rotateX() on a PMatrix2D.");
    }

    public void rotateY(float f) {
        throw new IllegalArgumentException("Cannot use rotateY() on a PMatrix2D.");
    }

    public void rotateZ(float f) {
        rotate(f);
    }

    public void scale(float f) {
        scale(f, f);
    }

    public void scale(float f, float f2) {
        this.m00 *= f;
        this.m01 *= f2;
        this.m10 *= f;
        this.m11 *= f2;
    }

    public void scale(float f, float f2, float f3) {
        throw new IllegalArgumentException("Cannot use this version of scale() on a PMatrix2D.");
    }

    public void set(float f, float f2, float f3, float f4, float f5, float f6) {
        this.m00 = f;
        this.m01 = f2;
        this.m02 = f3;
        this.m10 = f4;
        this.m11 = f5;
        this.m12 = f6;
    }

    public void set(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
    }

    public void set(PMatrix3D pMatrix3D) {
    }

    public void set(PMatrix pMatrix) {
        if (pMatrix instanceof PMatrix2D) {
            PMatrix2D pMatrix2D = (PMatrix2D) pMatrix;
            set(pMatrix2D.m00, pMatrix2D.m01, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, pMatrix2D.m12);
            return;
        }
        throw new IllegalArgumentException("PMatrix2D.set() only accepts PMatrix2D objects.");
    }

    public void set(float[] fArr) {
        this.m00 = fArr[0];
        this.m01 = fArr[1];
        this.m02 = fArr[2];
        this.m10 = fArr[3];
        this.m11 = fArr[4];
        this.m12 = fArr[5];
    }

    public void shearX(float f) {
        apply(1.0f, 0.0f, 1.0f, tan(f), 0.0f, 0.0f);
    }

    public void shearY(float f) {
        apply(1.0f, 0.0f, 1.0f, 0.0f, tan(f), 0.0f);
    }

    public void translate(float f, float f2) {
        this.m02 = (this.m00 * f) + (this.m01 * f2) + this.m02;
        this.m12 = (this.m10 * f) + (this.m11 * f2) + this.m12;
    }

    public void translate(float f, float f2, float f3) {
        throw new IllegalArgumentException("Cannot use translate(x, y, z) on a PMatrix2D.");
    }

    public void transpose() {
    }
}

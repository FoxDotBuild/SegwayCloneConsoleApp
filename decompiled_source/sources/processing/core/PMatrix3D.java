package processing.core;

public final class PMatrix3D implements PMatrix {
    protected PMatrix3D inverseCopy;
    public float m00;
    public float m01;
    public float m02;
    public float m03;
    public float m10;
    public float m11;
    public float m12;
    public float m13;
    public float m20;
    public float m21;
    public float m22;
    public float m23;
    public float m30;
    public float m31;
    public float m32;
    public float m33;

    public PMatrix3D() {
        reset();
    }

    public PMatrix3D(float f, float f2, float f3, float f4, float f5, float f6) {
        set(f, f2, f3, 0.0f, f4, f5, f6, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public PMatrix3D(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        set(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
    }

    public PMatrix3D(PMatrix pMatrix) {
        set(pMatrix);
    }

    private static final float abs(float f) {
        return f < 0.0f ? -f : f;
    }

    private static final float cos(float f) {
        return (float) Math.cos((double) f);
    }

    private float determinant3x3(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        return (((f5 * f9) - (f6 * f8)) * f) + (((f6 * f7) - (f4 * f9)) * f2) + (((f4 * f8) - (f5 * f7)) * f3);
    }

    private static final float max(float f, float f2) {
        return f > f2 ? f : f2;
    }

    private static final float sin(float f) {
        return (float) Math.sin((double) f);
    }

    public void apply(float f, float f2, float f3, float f4, float f5, float f6) {
        apply(f, f2, 0.0f, f3, f4, f5, 0.0f, f6, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void apply(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        float f17 = (this.m00 * f2) + (this.m01 * f6) + (this.m02 * f10) + (this.m03 * f14);
        float f18 = (this.m00 * f3) + (this.m01 * f7) + (this.m02 * f11) + (this.m03 * f15);
        float f19 = (this.m00 * f4) + (this.m01 * f8) + (this.m02 * f12) + (this.m03 * f16);
        float f20 = (this.m10 * f) + (this.m11 * f5) + (this.m12 * f9) + (this.m13 * f13);
        float f21 = (this.m10 * f2) + (this.m11 * f6) + (this.m12 * f10) + (this.m13 * f14);
        float f22 = (this.m10 * f3) + (this.m11 * f7) + (this.m12 * f11) + (this.m13 * f15);
        float f23 = (this.m10 * f4) + (this.m11 * f8) + (this.m12 * f12) + (this.m13 * f16);
        float f24 = (this.m20 * f) + (this.m21 * f5) + (this.m22 * f9) + (this.m23 * f13);
        float f25 = (this.m20 * f2) + (this.m21 * f6) + (this.m22 * f10) + (this.m23 * f14);
        float f26 = (this.m20 * f3) + (this.m21 * f7) + (this.m22 * f11) + (this.m23 * f15);
        float f27 = (this.m20 * f4) + (this.m21 * f8) + (this.m22 * f12) + (this.m23 * f16);
        float f28 = (this.m30 * f) + (this.m31 * f5) + (this.m32 * f9) + (this.m33 * f13);
        float f29 = (this.m30 * f2) + (this.m31 * f6) + (this.m32 * f10) + (this.m33 * f14);
        float f30 = (this.m30 * f3) + (this.m31 * f7) + (this.m32 * f11) + (this.m33 * f15);
        float f31 = (this.m30 * f4) + (this.m31 * f8) + (this.m32 * f12) + (this.m33 * f16);
        this.m00 = (this.m00 * f) + (this.m01 * f5) + (this.m02 * f9) + (this.m03 * f13);
        this.m01 = f17;
        this.m02 = f18;
        this.m03 = f19;
        this.m10 = f20;
        this.m11 = f21;
        this.m12 = f22;
        this.m13 = f23;
        this.m20 = f24;
        this.m21 = f25;
        this.m22 = f26;
        this.m23 = f27;
        this.m30 = f28;
        this.m31 = f29;
        this.m32 = f30;
        this.m33 = f31;
    }

    public void apply(PMatrix2D pMatrix2D) {
        apply(pMatrix2D.m00, pMatrix2D.m01, 0.0f, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, 0.0f, pMatrix2D.m12, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void apply(PMatrix3D pMatrix3D) {
        apply(pMatrix3D.m00, pMatrix3D.m01, pMatrix3D.m02, pMatrix3D.m03, pMatrix3D.m10, pMatrix3D.m11, pMatrix3D.m12, pMatrix3D.m13, pMatrix3D.m20, pMatrix3D.m21, pMatrix3D.m22, pMatrix3D.m23, pMatrix3D.m30, pMatrix3D.m31, pMatrix3D.m32, pMatrix3D.m33);
    }

    public void apply(PMatrix pMatrix) {
        if (pMatrix instanceof PMatrix2D) {
            apply((PMatrix2D) pMatrix);
        } else if (pMatrix instanceof PMatrix3D) {
            apply((PMatrix3D) pMatrix);
        }
    }

    public float determinant() {
        return (((this.m00 * (((((((this.m11 * this.m22) * this.m33) + ((this.m12 * this.m23) * this.m31)) + ((this.m13 * this.m21) * this.m32)) - ((this.m13 * this.m22) * this.m31)) - ((this.m11 * this.m23) * this.m32)) - ((this.m12 * this.m21) * this.m33))) - (this.m01 * (((((((this.m10 * this.m22) * this.m33) + ((this.m12 * this.m23) * this.m30)) + ((this.m13 * this.m20) * this.m32)) - ((this.m13 * this.m22) * this.m30)) - ((this.m10 * this.m23) * this.m32)) - ((this.m12 * this.m20) * this.m33)))) + (this.m02 * (((((((this.m10 * this.m21) * this.m33) + ((this.m11 * this.m23) * this.m30)) + ((this.m13 * this.m20) * this.m31)) - ((this.m13 * this.m21) * this.m30)) - ((this.m10 * this.m23) * this.m31)) - ((this.m11 * this.m20) * this.m33)))) - (this.m03 * (((((((this.m10 * this.m21) * this.m32) + ((this.m11 * this.m22) * this.m30)) + ((this.m12 * this.m20) * this.m31)) - ((this.m12 * this.m21) * this.m30)) - ((this.m10 * this.m22) * this.m31)) - ((this.m11 * this.m20) * this.m32)));
    }

    public PMatrix3D get() {
        PMatrix3D pMatrix3D = new PMatrix3D();
        pMatrix3D.set((PMatrix) this);
        return pMatrix3D;
    }

    public float[] get(float[] fArr) {
        if (fArr == null || fArr.length != 16) {
            fArr = new float[16];
        }
        fArr[0] = this.m00;
        fArr[1] = this.m01;
        fArr[2] = this.m02;
        fArr[3] = this.m03;
        fArr[4] = this.m10;
        fArr[5] = this.m11;
        fArr[6] = this.m12;
        fArr[7] = this.m13;
        fArr[8] = this.m20;
        fArr[9] = this.m21;
        fArr[10] = this.m22;
        fArr[11] = this.m23;
        fArr[12] = this.m30;
        fArr[13] = this.m31;
        fArr[14] = this.m32;
        fArr[15] = this.m33;
        return fArr;
    }

    /* access modifiers changed from: protected */
    public boolean invApply(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        if (this.inverseCopy == null) {
            this.inverseCopy = new PMatrix3D();
        }
        this.inverseCopy.set(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
        if (!this.inverseCopy.invert()) {
            return false;
        }
        preApply(this.inverseCopy);
        return true;
    }

    /* access modifiers changed from: protected */
    public void invRotate(float f, float f2, float f3, float f4) {
        float cos = cos(-f);
        float sin = sin(-f);
        float f5 = 1.0f - cos;
        float f6 = (f5 * f4 * f4) + cos;
        preApply((f5 * f2 * f2) + cos, ((f5 * f2) * f3) - (sin * f4), (f5 * f2 * f4) + (sin * f3), 0.0f, (f5 * f2 * f3) + (sin * f4), (f5 * f3 * f3) + cos, ((f5 * f3) * f4) - (sin * f2), 0.0f, ((f5 * f2) * f4) - (sin * f3), (sin * f2) + (f5 * f3 * f4), f6, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /* access modifiers changed from: protected */
    public void invRotateX(float f) {
        float cos = cos(-f);
        float sin = sin(-f);
        preApply(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, cos, -sin, 0.0f, 0.0f, sin, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /* access modifiers changed from: protected */
    public void invRotateY(float f) {
        float cos = cos(-f);
        float sin = sin(-f);
        preApply(cos, 0.0f, sin, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -sin, 0.0f, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /* access modifiers changed from: protected */
    public void invRotateZ(float f) {
        float cos = cos(-f);
        float sin = sin(-f);
        preApply(cos, -sin, 0.0f, 0.0f, sin, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /* access modifiers changed from: protected */
    public void invScale(float f, float f2, float f3) {
        preApply(1.0f / f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f / f2, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f / f3, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /* access modifiers changed from: protected */
    public void invTranslate(float f, float f2, float f3) {
        preApply(1.0f, 0.0f, 0.0f, -f, 0.0f, 1.0f, 0.0f, -f2, 0.0f, 0.0f, 1.0f, -f3, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public boolean invert() {
        float determinant = determinant();
        if (determinant == 0.0f) {
            return false;
        }
        float determinant3x3 = determinant3x3(this.m11, this.m12, this.m13, this.m21, this.m22, this.m23, this.m31, this.m32, this.m33);
        float f = -determinant3x3(this.m10, this.m12, this.m13, this.m20, this.m22, this.m23, this.m30, this.m32, this.m33);
        float determinant3x32 = determinant3x3(this.m10, this.m11, this.m13, this.m20, this.m21, this.m23, this.m30, this.m31, this.m33);
        float f2 = -determinant3x3(this.m10, this.m11, this.m12, this.m20, this.m21, this.m22, this.m30, this.m31, this.m32);
        float f3 = -determinant3x3(this.m01, this.m02, this.m03, this.m21, this.m22, this.m23, this.m31, this.m32, this.m33);
        float determinant3x33 = determinant3x3(this.m00, this.m02, this.m03, this.m20, this.m22, this.m23, this.m30, this.m32, this.m33);
        float f4 = -determinant3x3(this.m00, this.m01, this.m03, this.m20, this.m21, this.m23, this.m30, this.m31, this.m33);
        float determinant3x34 = determinant3x3(this.m00, this.m01, this.m02, this.m20, this.m21, this.m22, this.m30, this.m31, this.m32);
        float determinant3x35 = determinant3x3(this.m01, this.m02, this.m03, this.m11, this.m12, this.m13, this.m31, this.m32, this.m33);
        float f5 = -determinant3x3(this.m00, this.m02, this.m03, this.m10, this.m12, this.m13, this.m30, this.m32, this.m33);
        float determinant3x36 = determinant3x3(this.m00, this.m01, this.m03, this.m10, this.m11, this.m13, this.m30, this.m31, this.m33);
        float f6 = -determinant3x3(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m30, this.m31, this.m32);
        float f7 = -determinant3x3(this.m01, this.m02, this.m03, this.m11, this.m12, this.m13, this.m21, this.m22, this.m23);
        float determinant3x37 = determinant3x3(this.m00, this.m02, this.m03, this.m10, this.m12, this.m13, this.m20, this.m22, this.m23);
        float f8 = -determinant3x3(this.m00, this.m01, this.m03, this.m10, this.m11, this.m13, this.m20, this.m21, this.m23);
        float determinant3x38 = determinant3x3(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22);
        this.m00 = determinant3x3 / determinant;
        this.m01 = f3 / determinant;
        this.m02 = determinant3x35 / determinant;
        this.m03 = f7 / determinant;
        this.m10 = f / determinant;
        this.m11 = determinant3x33 / determinant;
        this.m12 = f5 / determinant;
        this.m13 = determinant3x37 / determinant;
        this.m20 = determinant3x32 / determinant;
        this.m21 = f4 / determinant;
        this.m22 = determinant3x36 / determinant;
        this.m23 = f8 / determinant;
        this.m30 = f2 / determinant;
        this.m31 = determinant3x34 / determinant;
        this.m32 = f6 / determinant;
        this.m33 = determinant3x38 / determinant;
        return true;
    }

    public PVector mult(PVector pVector, PVector pVector2) {
        if (pVector2 == null) {
            pVector2 = new PVector();
        }
        pVector2.set((this.m00 * pVector.f68x) + (this.m01 * pVector.f69y) + (this.m02 * pVector.f70z) + this.m03, (this.m10 * pVector.f68x) + (this.m11 * pVector.f69y) + (this.m12 * pVector.f70z) + this.m13, (this.m20 * pVector.f68x) + (this.m21 * pVector.f69y) + (this.m22 * pVector.f70z) + this.m23);
        return pVector2;
    }

    public float[] mult(float[] fArr, float[] fArr2) {
        if (fArr2 == null || fArr2.length < 3) {
            fArr2 = new float[3];
        }
        if (fArr == fArr2) {
            throw new RuntimeException("The source and target vectors used in PMatrix3D.mult() cannot be identical.");
        }
        if (fArr2.length == 3) {
            fArr2[0] = (this.m00 * fArr[0]) + (this.m01 * fArr[1]) + (this.m02 * fArr[2]) + this.m03;
            fArr2[1] = (this.m10 * fArr[0]) + (this.m11 * fArr[1]) + (this.m12 * fArr[2]) + this.m13;
            fArr2[2] = (this.m20 * fArr[0]) + (this.m21 * fArr[1]) + (this.m22 * fArr[2]) + this.m23;
        } else if (fArr2.length > 3) {
            fArr2[0] = (this.m00 * fArr[0]) + (this.m01 * fArr[1]) + (this.m02 * fArr[2]) + (this.m03 * fArr[3]);
            fArr2[1] = (this.m10 * fArr[0]) + (this.m11 * fArr[1]) + (this.m12 * fArr[2]) + (this.m13 * fArr[3]);
            fArr2[2] = (this.m20 * fArr[0]) + (this.m21 * fArr[1]) + (this.m22 * fArr[2]) + (this.m23 * fArr[3]);
            fArr2[3] = (this.m30 * fArr[0]) + (this.m31 * fArr[1]) + (this.m32 * fArr[2]) + (this.m33 * fArr[3]);
        }
        return fArr2;
    }

    public float multW(float f, float f2, float f3) {
        return (this.m30 * f) + (this.m31 * f2) + (this.m32 * f3) + this.m33;
    }

    public float multW(float f, float f2, float f3, float f4) {
        return (this.m30 * f) + (this.m31 * f2) + (this.m32 * f3) + (this.m33 * f4);
    }

    public float multX(float f, float f2) {
        return (this.m00 * f) + (this.m01 * f2) + this.m03;
    }

    public float multX(float f, float f2, float f3) {
        return (this.m00 * f) + (this.m01 * f2) + (this.m02 * f3) + this.m03;
    }

    public float multX(float f, float f2, float f3, float f4) {
        return (this.m00 * f) + (this.m01 * f2) + (this.m02 * f3) + (this.m03 * f4);
    }

    public float multY(float f, float f2) {
        return (this.m10 * f) + (this.m11 * f2) + this.m13;
    }

    public float multY(float f, float f2, float f3) {
        return (this.m10 * f) + (this.m11 * f2) + (this.m12 * f3) + this.m13;
    }

    public float multY(float f, float f2, float f3, float f4) {
        return (this.m10 * f) + (this.m11 * f2) + (this.m12 * f3) + (this.m13 * f4);
    }

    public float multZ(float f, float f2, float f3) {
        return (this.m20 * f) + (this.m21 * f2) + (this.m22 * f3) + this.m23;
    }

    public float multZ(float f, float f2, float f3, float f4) {
        return (this.m20 * f) + (this.m21 * f2) + (this.m22 * f3) + (this.m23 * f4);
    }

    public void preApply(float f, float f2, float f3, float f4, float f5, float f6) {
        preApply(f, f2, 0.0f, f3, f4, f5, 0.0f, f6, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void preApply(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        float f17 = (this.m01 * f) + (this.m11 * f2) + (this.m21 * f3) + (this.m31 * f4);
        float f18 = (this.m02 * f) + (this.m12 * f2) + (this.m22 * f3) + (this.m32 * f4);
        float f19 = (this.m03 * f) + (this.m13 * f2) + (this.m23 * f3) + (this.m33 * f4);
        float f20 = (this.m00 * f5) + (this.m10 * f6) + (this.m20 * f7) + (this.m30 * f8);
        float f21 = (this.m01 * f5) + (this.m11 * f6) + (this.m21 * f7) + (this.m31 * f8);
        float f22 = (this.m02 * f5) + (this.m12 * f6) + (this.m22 * f7) + (this.m32 * f8);
        float f23 = (this.m03 * f5) + (this.m13 * f6) + (this.m23 * f7) + (this.m33 * f8);
        float f24 = (this.m00 * f9) + (this.m10 * f10) + (this.m20 * f11) + (this.m30 * f12);
        float f25 = (this.m01 * f9) + (this.m11 * f10) + (this.m21 * f11) + (this.m31 * f12);
        float f26 = (this.m02 * f9) + (this.m12 * f10) + (this.m22 * f11) + (this.m32 * f12);
        float f27 = (this.m03 * f9) + (this.m13 * f10) + (this.m23 * f11) + (this.m33 * f12);
        float f28 = (this.m00 * f13) + (this.m10 * f14) + (this.m20 * f15) + (this.m30 * f16);
        float f29 = (this.m01 * f13) + (this.m11 * f14) + (this.m21 * f15) + (this.m31 * f16);
        float f30 = (this.m02 * f13) + (this.m12 * f14) + (this.m22 * f15) + (this.m32 * f16);
        float f31 = (this.m03 * f13) + (this.m13 * f14) + (this.m23 * f15) + (this.m33 * f16);
        this.m00 = (this.m00 * f) + (this.m10 * f2) + (this.m20 * f3) + (this.m30 * f4);
        this.m01 = f17;
        this.m02 = f18;
        this.m03 = f19;
        this.m10 = f20;
        this.m11 = f21;
        this.m12 = f22;
        this.m13 = f23;
        this.m20 = f24;
        this.m21 = f25;
        this.m22 = f26;
        this.m23 = f27;
        this.m30 = f28;
        this.m31 = f29;
        this.m32 = f30;
        this.m33 = f31;
    }

    public void preApply(PMatrix2D pMatrix2D) {
        preApply(pMatrix2D.m00, pMatrix2D.m01, 0.0f, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, 0.0f, pMatrix2D.m12, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void preApply(PMatrix3D pMatrix3D) {
        preApply(pMatrix3D.m00, pMatrix3D.m01, pMatrix3D.m02, pMatrix3D.m03, pMatrix3D.m10, pMatrix3D.m11, pMatrix3D.m12, pMatrix3D.m13, pMatrix3D.m20, pMatrix3D.m21, pMatrix3D.m22, pMatrix3D.m23, pMatrix3D.m30, pMatrix3D.m31, pMatrix3D.m32, pMatrix3D.m33);
    }

    public void print() {
        int abs = (int) Math.abs(max(max(max(max(abs(this.m00), abs(this.m01)), max(abs(this.m02), abs(this.m03))), max(max(abs(this.m10), abs(this.m11)), max(abs(this.m12), abs(this.m13)))), max(max(max(abs(this.m20), abs(this.m21)), max(abs(this.m22), abs(this.m23))), max(max(abs(this.m30), abs(this.m31)), max(abs(this.m32), abs(this.m33))))));
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
        System.out.println(PApplet.nfs(this.m00, i, 4) + " " + PApplet.nfs(this.m01, i, 4) + " " + PApplet.nfs(this.m02, i, 4) + " " + PApplet.nfs(this.m03, i, 4));
        System.out.println(PApplet.nfs(this.m10, i, 4) + " " + PApplet.nfs(this.m11, i, 4) + " " + PApplet.nfs(this.m12, i, 4) + " " + PApplet.nfs(this.m13, i, 4));
        System.out.println(PApplet.nfs(this.m20, i, 4) + " " + PApplet.nfs(this.m21, i, 4) + " " + PApplet.nfs(this.m22, i, 4) + " " + PApplet.nfs(this.m23, i, 4));
        System.out.println(PApplet.nfs(this.m30, i, 4) + " " + PApplet.nfs(this.m31, i, 4) + " " + PApplet.nfs(this.m32, i, 4) + " " + PApplet.nfs(this.m33, i, 4));
        System.out.println();
    }

    public void reset() {
        set(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void rotate(float f) {
        rotateZ(f);
    }

    public void rotate(float f, float f2, float f3, float f4) {
        float f5 = (f2 * f2) + (f3 * f3) + (f4 * f4);
        if (f5 >= 1.0E-4f) {
            if (Math.abs(f5 - 1.0f) > 1.0E-4f) {
                float sqrt = PApplet.sqrt(f5);
                f2 /= sqrt;
                f3 /= sqrt;
                f4 /= sqrt;
            }
            float cos = cos(f);
            float sin = sin(f);
            float f6 = 1.0f - cos;
            float f7 = (f6 * f4 * f4) + cos;
            apply((f6 * f2 * f2) + cos, ((f6 * f2) * f3) - (sin * f4), (f6 * f2 * f4) + (sin * f3), 0.0f, (f6 * f2 * f3) + (sin * f4), (f6 * f3 * f3) + cos, ((f6 * f3) * f4) - (sin * f2), 0.0f, ((f6 * f2) * f4) - (sin * f3), (sin * f2) + (f6 * f3 * f4), f7, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
        }
    }

    public void rotateX(float f) {
        float cos = cos(f);
        float sin = sin(f);
        apply(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, cos, -sin, 0.0f, 0.0f, sin, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void rotateY(float f) {
        float cos = cos(f);
        float sin = sin(f);
        apply(cos, 0.0f, sin, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -sin, 0.0f, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void rotateZ(float f) {
        float cos = cos(f);
        float sin = sin(f);
        apply(cos, -sin, 0.0f, 0.0f, sin, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void scale(float f) {
        scale(f, f, f);
    }

    public void scale(float f, float f2) {
        scale(f, f2, 1.0f);
    }

    public void scale(float f, float f2, float f3) {
        this.m00 *= f;
        this.m01 *= f2;
        this.m02 *= f3;
        this.m10 *= f;
        this.m11 *= f2;
        this.m12 *= f3;
        this.m20 *= f;
        this.m21 *= f2;
        this.m22 *= f3;
        this.m30 *= f;
        this.m31 *= f2;
        this.m32 *= f3;
    }

    public void set(float f, float f2, float f3, float f4, float f5, float f6) {
        set(f, f2, 0.0f, f3, f4, f5, 0.0f, f6, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void set(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        this.m00 = f;
        this.m01 = f2;
        this.m02 = f3;
        this.m03 = f4;
        this.m10 = f5;
        this.m11 = f6;
        this.m12 = f7;
        this.m13 = f8;
        this.m20 = f9;
        this.m21 = f10;
        this.m22 = f11;
        this.m23 = f12;
        this.m30 = f13;
        this.m31 = f14;
        this.m32 = f15;
        this.m33 = f16;
    }

    public void set(PMatrix pMatrix) {
        if (pMatrix instanceof PMatrix3D) {
            PMatrix3D pMatrix3D = (PMatrix3D) pMatrix;
            set(pMatrix3D.m00, pMatrix3D.m01, pMatrix3D.m02, pMatrix3D.m03, pMatrix3D.m10, pMatrix3D.m11, pMatrix3D.m12, pMatrix3D.m13, pMatrix3D.m20, pMatrix3D.m21, pMatrix3D.m22, pMatrix3D.m23, pMatrix3D.m30, pMatrix3D.m31, pMatrix3D.m32, pMatrix3D.m33);
            return;
        }
        PMatrix2D pMatrix2D = (PMatrix2D) pMatrix;
        set(pMatrix2D.m00, pMatrix2D.m01, 0.0f, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, 0.0f, pMatrix2D.m12, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void set(float[] fArr) {
        if (fArr.length == 6) {
            set(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5]);
        } else if (fArr.length == 16) {
            this.m00 = fArr[0];
            this.m01 = fArr[1];
            this.m02 = fArr[2];
            this.m03 = fArr[3];
            this.m10 = fArr[4];
            this.m11 = fArr[5];
            this.m12 = fArr[6];
            this.m13 = fArr[7];
            this.m20 = fArr[8];
            this.m21 = fArr[9];
            this.m22 = fArr[10];
            this.m23 = fArr[11];
            this.m30 = fArr[12];
            this.m31 = fArr[13];
            this.m32 = fArr[14];
            this.m33 = fArr[15];
        }
    }

    public void shearX(float f) {
        apply(1.0f, (float) Math.tan((double) f), 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void shearY(float f) {
        apply(1.0f, 0.0f, 0.0f, 0.0f, (float) Math.tan((double) f), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void translate(float f, float f2) {
        translate(f, f2, 0.0f);
    }

    public void translate(float f, float f2, float f3) {
        this.m03 += (this.m00 * f) + (this.m01 * f2) + (this.m02 * f3);
        this.m13 += (this.m10 * f) + (this.m11 * f2) + (this.m12 * f3);
        this.m23 += (this.m20 * f) + (this.m21 * f2) + (this.m22 * f3);
        this.m33 += (this.m30 * f) + (this.m31 * f2) + (this.m32 * f3);
    }

    public void transpose() {
        float f = this.m01;
        this.m01 = this.m10;
        this.m10 = f;
        float f2 = this.m02;
        this.m02 = this.m20;
        this.m20 = f2;
        float f3 = this.m03;
        this.m03 = this.m30;
        this.m30 = f3;
        float f4 = this.m12;
        this.m12 = this.m21;
        this.m21 = f4;
        float f5 = this.m13;
        this.m13 = this.m31;
        this.m31 = f5;
        float f6 = this.m23;
        this.m23 = this.m32;
        this.m32 = f6;
    }
}

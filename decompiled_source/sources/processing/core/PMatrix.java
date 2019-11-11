package processing.core;

public interface PMatrix {
    void apply(float f, float f2, float f3, float f4, float f5, float f6);

    void apply(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16);

    void apply(PMatrix2D pMatrix2D);

    void apply(PMatrix3D pMatrix3D);

    void apply(PMatrix pMatrix);

    float determinant();

    PMatrix get();

    float[] get(float[] fArr);

    boolean invert();

    PVector mult(PVector pVector, PVector pVector2);

    float[] mult(float[] fArr, float[] fArr2);

    void preApply(float f, float f2, float f3, float f4, float f5, float f6);

    void preApply(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16);

    void preApply(PMatrix2D pMatrix2D);

    void preApply(PMatrix3D pMatrix3D);

    void reset();

    void rotate(float f);

    void rotate(float f, float f2, float f3, float f4);

    void rotateX(float f);

    void rotateY(float f);

    void rotateZ(float f);

    void scale(float f);

    void scale(float f, float f2);

    void scale(float f, float f2, float f3);

    void set(float f, float f2, float f3, float f4, float f5, float f6);

    void set(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16);

    void set(PMatrix pMatrix);

    void set(float[] fArr);

    void shearX(float f);

    void shearY(float f);

    void translate(float f, float f2);

    void translate(float f, float f2, float f3);

    void transpose();
}

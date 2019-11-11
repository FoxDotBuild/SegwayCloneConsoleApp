package processing.core;

import java.io.Serializable;

public class PVector implements Serializable {
    private static final long serialVersionUID = -6717872085945400694L;
    protected transient float[] array;

    /* renamed from: x */
    public float f68x;

    /* renamed from: y */
    public float f69y;

    /* renamed from: z */
    public float f70z;

    public PVector() {
    }

    public PVector(float f, float f2) {
        this.f68x = f;
        this.f69y = f2;
        this.f70z = 0.0f;
    }

    public PVector(float f, float f2, float f3) {
        this.f68x = f;
        this.f69y = f2;
        this.f70z = f3;
    }

    public static PVector add(PVector pVector, PVector pVector2) {
        return add(pVector, pVector2, (PVector) null);
    }

    public static PVector add(PVector pVector, PVector pVector2, PVector pVector3) {
        if (pVector3 == null) {
            return new PVector(pVector.f68x + pVector2.f68x, pVector.f69y + pVector2.f69y, pVector.f70z + pVector2.f70z);
        }
        pVector3.set(pVector.f68x + pVector2.f68x, pVector.f69y + pVector2.f69y, pVector.f70z + pVector2.f70z);
        return pVector3;
    }

    public static float angleBetween(PVector pVector, PVector pVector2) {
        double sqrt = ((double) (((pVector.f68x * pVector2.f68x) + (pVector.f69y * pVector2.f69y)) + (pVector.f70z * pVector2.f70z))) / (Math.sqrt((double) (((pVector.f68x * pVector.f68x) + (pVector.f69y * pVector.f69y)) + (pVector.f70z * pVector.f70z))) * Math.sqrt((double) (((pVector2.f68x * pVector2.f68x) + (pVector2.f69y * pVector2.f69y)) + (pVector2.f70z * pVector2.f70z))));
        if (sqrt <= -1.0d) {
            return 3.1415927f;
        }
        if (sqrt >= 1.0d) {
            return 0.0f;
        }
        return (float) Math.acos(sqrt);
    }

    public static PVector cross(PVector pVector, PVector pVector2, PVector pVector3) {
        float f = (pVector.f69y * pVector2.f70z) - (pVector2.f69y * pVector.f70z);
        float f2 = (pVector.f70z * pVector2.f68x) - (pVector2.f70z * pVector.f68x);
        float f3 = (pVector.f68x * pVector2.f69y) - (pVector2.f68x * pVector.f69y);
        if (pVector3 == null) {
            return new PVector(f, f2, f3);
        }
        pVector3.set(f, f2, f3);
        return pVector3;
    }

    public static float dist(PVector pVector, PVector pVector2) {
        float f = pVector.f68x - pVector2.f68x;
        float f2 = pVector.f69y - pVector2.f69y;
        float f3 = pVector.f70z - pVector2.f70z;
        return (float) Math.sqrt((double) ((f * f) + (f2 * f2) + (f3 * f3)));
    }

    public static PVector div(PVector pVector, float f) {
        return div(pVector, f, (PVector) null);
    }

    public static PVector div(PVector pVector, float f, PVector pVector2) {
        if (pVector2 == null) {
            return new PVector(pVector.f68x / f, pVector.f69y / f, pVector.f70z / f);
        }
        pVector2.set(pVector.f68x / f, pVector.f69y / f, pVector.f70z / f);
        return pVector2;
    }

    public static PVector div(PVector pVector, PVector pVector2) {
        return div(pVector, pVector2, (PVector) null);
    }

    public static PVector div(PVector pVector, PVector pVector2, PVector pVector3) {
        if (pVector3 == null) {
            return new PVector(pVector.f68x / pVector2.f68x, pVector.f69y / pVector2.f69y, pVector.f70z / pVector2.f70z);
        }
        pVector3.set(pVector.f68x / pVector2.f68x, pVector.f69y / pVector2.f69y, pVector.f70z / pVector2.f70z);
        return pVector3;
    }

    public static float dot(PVector pVector, PVector pVector2) {
        return (pVector.f68x * pVector2.f68x) + (pVector.f69y * pVector2.f69y) + (pVector.f70z * pVector2.f70z);
    }

    public static PVector fromAngle(float f) {
        return fromAngle(f, null);
    }

    public static PVector fromAngle(float f, PVector pVector) {
        if (pVector == null) {
            return new PVector((float) Math.cos((double) f), (float) Math.sin((double) f), 0.0f);
        }
        pVector.set((float) Math.cos((double) f), (float) Math.sin((double) f), 0.0f);
        return pVector;
    }

    public static PVector lerp(PVector pVector, PVector pVector2, float f) {
        PVector pVector3 = pVector.get();
        pVector3.lerp(pVector2, f);
        return pVector3;
    }

    public static PVector mult(PVector pVector, float f) {
        return mult(pVector, f, (PVector) null);
    }

    public static PVector mult(PVector pVector, float f, PVector pVector2) {
        if (pVector2 == null) {
            return new PVector(pVector.f68x * f, pVector.f69y * f, pVector.f70z * f);
        }
        pVector2.set(pVector.f68x * f, pVector.f69y * f, pVector.f70z * f);
        return pVector2;
    }

    public static PVector mult(PVector pVector, PVector pVector2) {
        return mult(pVector, pVector2, (PVector) null);
    }

    public static PVector mult(PVector pVector, PVector pVector2, PVector pVector3) {
        if (pVector3 == null) {
            return new PVector(pVector.f68x * pVector2.f68x, pVector.f69y * pVector2.f69y, pVector.f70z * pVector2.f70z);
        }
        pVector3.set(pVector.f68x * pVector2.f68x, pVector.f69y * pVector2.f69y, pVector.f70z * pVector2.f70z);
        return pVector3;
    }

    public static PVector random2D() {
        return random2D(null, null);
    }

    public static PVector random2D(PApplet pApplet) {
        return random2D(null, pApplet);
    }

    public static PVector random2D(PVector pVector) {
        return random2D(pVector, null);
    }

    public static PVector random2D(PVector pVector, PApplet pApplet) {
        return pApplet == null ? fromAngle((float) (Math.random() * 3.141592653589793d * 2.0d), pVector) : fromAngle(pApplet.random(6.2831855f), pVector);
    }

    public static PVector random3D() {
        return random3D(null, null);
    }

    public static PVector random3D(PApplet pApplet) {
        return random3D(null, pApplet);
    }

    public static PVector random3D(PVector pVector) {
        return random3D(pVector, null);
    }

    public static PVector random3D(PVector pVector, PApplet pApplet) {
        float random;
        float random2;
        if (pApplet == null) {
            random = (float) (Math.random() * 3.141592653589793d * 2.0d);
            random2 = (float) ((Math.random() * 2.0d) - 1.0d);
        } else {
            random = pApplet.random(6.2831855f);
            random2 = pApplet.random(-1.0f, 1.0f);
        }
        float sqrt = (float) (Math.sqrt((double) (1.0f - (random2 * random2))) * Math.cos((double) random));
        float sqrt2 = (float) (Math.sqrt((double) (1.0f - (random2 * random2))) * Math.sin((double) random));
        if (pVector == null) {
            return new PVector(sqrt, sqrt2, random2);
        }
        pVector.set(sqrt, sqrt2, random2);
        return pVector;
    }

    public static PVector sub(PVector pVector, PVector pVector2) {
        return sub(pVector, pVector2, (PVector) null);
    }

    public static PVector sub(PVector pVector, PVector pVector2, PVector pVector3) {
        if (pVector3 == null) {
            return new PVector(pVector.f68x - pVector2.f68x, pVector.f69y - pVector2.f69y, pVector.f70z - pVector2.f70z);
        }
        pVector3.set(pVector.f68x - pVector2.f68x, pVector.f69y - pVector2.f69y, pVector.f70z - pVector2.f70z);
        return pVector3;
    }

    public void add(float f, float f2, float f3) {
        this.f68x += f;
        this.f69y += f2;
        this.f70z += f3;
    }

    public void add(PVector pVector) {
        this.f68x += pVector.f68x;
        this.f69y += pVector.f69y;
        this.f70z += pVector.f70z;
    }

    public float[] array() {
        if (this.array == null) {
            this.array = new float[3];
        }
        this.array[0] = this.f68x;
        this.array[1] = this.f69y;
        this.array[2] = this.f70z;
        return this.array;
    }

    public PVector cross(PVector pVector) {
        return cross(pVector, null);
    }

    public PVector cross(PVector pVector, PVector pVector2) {
        float f = (this.f69y * pVector.f70z) - (pVector.f69y * this.f70z);
        float f2 = (this.f70z * pVector.f68x) - (pVector.f70z * this.f68x);
        float f3 = (this.f68x * pVector.f69y) - (pVector.f68x * this.f69y);
        if (pVector2 == null) {
            return new PVector(f, f2, f3);
        }
        pVector2.set(f, f2, f3);
        return pVector2;
    }

    public float dist(PVector pVector) {
        float f = this.f68x - pVector.f68x;
        float f2 = this.f69y - pVector.f69y;
        float f3 = this.f70z - pVector.f70z;
        return (float) Math.sqrt((double) ((f * f) + (f2 * f2) + (f3 * f3)));
    }

    public void div(float f) {
        this.f68x /= f;
        this.f69y /= f;
        this.f70z /= f;
    }

    public void div(PVector pVector) {
        this.f68x /= pVector.f68x;
        this.f69y /= pVector.f69y;
        this.f70z /= pVector.f70z;
    }

    public float dot(float f, float f2, float f3) {
        return (this.f68x * f) + (this.f69y * f2) + (this.f70z * f3);
    }

    public float dot(PVector pVector) {
        return (this.f68x * pVector.f68x) + (this.f69y * pVector.f69y) + (this.f70z * pVector.f70z);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PVector)) {
            return false;
        }
        PVector pVector = (PVector) obj;
        return this.f68x == pVector.f68x && this.f69y == pVector.f69y && this.f70z == pVector.f70z;
    }

    public PVector get() {
        return new PVector(this.f68x, this.f69y, this.f70z);
    }

    public float[] get(float[] fArr) {
        if (fArr == null) {
            return new float[]{this.f68x, this.f69y, this.f70z};
        }
        if (fArr.length >= 2) {
            fArr[0] = this.f68x;
            fArr[1] = this.f69y;
        }
        if (fArr.length < 3) {
            return fArr;
        }
        fArr[2] = this.f70z;
        return fArr;
    }

    public int hashCode() {
        return ((((Float.floatToIntBits(this.f68x) + 31) * 31) + Float.floatToIntBits(this.f69y)) * 31) + Float.floatToIntBits(this.f70z);
    }

    public float heading() {
        return ((float) Math.atan2((double) (-this.f69y), (double) this.f68x)) * -1.0f;
    }

    @Deprecated
    public float heading2D() {
        return heading();
    }

    public void lerp(float f, float f2, float f3, float f4) {
        this.f68x = PApplet.lerp(this.f68x, f, f4);
        this.f69y = PApplet.lerp(this.f69y, f2, f4);
        this.f70z = PApplet.lerp(this.f70z, f3, f4);
    }

    public void lerp(PVector pVector, float f) {
        this.f68x = PApplet.lerp(this.f68x, pVector.f68x, f);
        this.f69y = PApplet.lerp(this.f69y, pVector.f69y, f);
        this.f70z = PApplet.lerp(this.f70z, pVector.f70z, f);
    }

    public void limit(float f) {
        if (magSq() > f * f) {
            normalize();
            mult(f);
        }
    }

    public float mag() {
        return (float) Math.sqrt((double) ((this.f68x * this.f68x) + (this.f69y * this.f69y) + (this.f70z * this.f70z)));
    }

    public float magSq() {
        return (this.f68x * this.f68x) + (this.f69y * this.f69y) + (this.f70z * this.f70z);
    }

    public void mult(float f) {
        this.f68x *= f;
        this.f69y *= f;
        this.f70z *= f;
    }

    public void mult(PVector pVector) {
        this.f68x *= pVector.f68x;
        this.f69y *= pVector.f69y;
        this.f70z *= pVector.f70z;
    }

    public PVector normalize(PVector pVector) {
        if (pVector == null) {
            pVector = new PVector();
        }
        float mag = mag();
        if (mag > 0.0f) {
            pVector.set(this.f68x / mag, this.f69y / mag, this.f70z / mag);
        } else {
            pVector.set(this.f68x, this.f69y, this.f70z);
        }
        return pVector;
    }

    public void normalize() {
        float mag = mag();
        if (mag != 0.0f && mag != 1.0f) {
            div(mag);
        }
    }

    public void rotate(float f) {
        float f2 = this.f68x;
        this.f68x = (this.f68x * PApplet.cos(f)) - (this.f69y * PApplet.sin(f));
        this.f69y = (f2 * PApplet.sin(f)) + (this.f69y * PApplet.cos(f));
    }

    public void set(float f, float f2, float f3) {
        this.f68x = f;
        this.f69y = f2;
        this.f70z = f3;
    }

    public void set(PVector pVector) {
        this.f68x = pVector.f68x;
        this.f69y = pVector.f69y;
        this.f70z = pVector.f70z;
    }

    public void set(float[] fArr) {
        if (fArr.length >= 2) {
            this.f68x = fArr[0];
            this.f69y = fArr[1];
        }
        if (fArr.length >= 3) {
            this.f70z = fArr[2];
        }
    }

    public PVector setMag(PVector pVector, float f) {
        PVector normalize = normalize(pVector);
        normalize.mult(f);
        return normalize;
    }

    public void setMag(float f) {
        normalize();
        mult(f);
    }

    public void sub(float f, float f2, float f3) {
        this.f68x -= f;
        this.f69y -= f2;
        this.f70z -= f3;
    }

    public void sub(PVector pVector) {
        this.f68x -= pVector.f68x;
        this.f69y -= pVector.f69y;
        this.f70z -= pVector.f70z;
    }

    public String toString() {
        return "[ " + this.f68x + ", " + this.f69y + ", " + this.f70z + " ]";
    }
}

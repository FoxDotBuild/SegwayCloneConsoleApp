package processing.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import processing.data.Table;
import processing.data.XML;
import processing.event.Event;
import processing.event.MouseEvent;
import processing.event.TouchEvent;
import processing.opengl.PGL;
import processing.opengl.PGraphics2D;
import processing.opengl.PGraphics3D;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PShader;

public class PApplet extends Activity implements PConstants, Runnable {
    public static final String ARGS_BGCOLOR = "--bgcolor";
    public static final String ARGS_DISPLAY = "--display";
    public static final String ARGS_EDITOR_LOCATION = "--editor-location";
    public static final String ARGS_EXCLUSIVE = "--exclusive";
    public static final String ARGS_EXTERNAL = "--external";
    public static final String ARGS_HIDE_STOP = "--hide-stop";
    public static final String ARGS_LOCATION = "--location";
    public static final String ARGS_PRESENT = "--present";
    public static final String ARGS_SKETCH_FOLDER = "--sketch-path";
    public static final String ARGS_STOP_COLOR = "--stop-color";
    public static final boolean DEBUG = false;
    static final String ERROR_MIN_MAX = "Cannot use min() or max() on an empty array.";
    public static final String EXTERNAL_MOVE = "__MOVE__";
    public static final String EXTERNAL_STOP = "__STOP__";
    public static final byte[] ICON_IMAGE = {71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -77, 0, 0, 0, 0, 0, -1, -1, -1, 12, 12, 13, -15, -15, -14, 45, 57, 74, 54, 80, 111, 47, 71, 97, 62, 88, 117, 1, 14, 27, 7, 41, 73, 15, 52, 85, 2, 31, 55, 4, 54, 94, 18, 69, 109, 37, 87, 126, -1, -1, -1, 33, -7, 4, 1, 0, 0, 15, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 4, 122, -16, -107, 114, -86, -67, 83, 30, -42, 26, -17, -100, -45, 56, -57, -108, 48, 40, 122, -90, 104, 67, -91, -51, 32, -53, 77, -78, -100, 47, -86, 12, 76, -110, -20, -74, -101, 97, -93, 27, 40, 20, -65, 65, 48, -111, 99, -20, -112, -117, -123, -47, -105, 24, 114, -112, 74, 69, 84, 25, 93, 88, -75, 9, 46, 2, 49, 88, -116, -67, 7, -19, -83, 60, 38, 3, -34, 2, 66, -95, 27, -98, 13, 4, -17, 55, 33, 109, 11, 11, -2, Byte.MIN_VALUE, 121, 123, 62, 91, 120, Byte.MIN_VALUE, Byte.MAX_VALUE, 122, 115, 102, 2, 119, 0, -116, -113, -119, 6, 102, 121, -108, -126, 5, 18, 6, 4, -102, -101, -100, 114, 15, 17, 0, 59};
    static final int META_CTRL_ON = 4096;
    static final int META_META_ON = 65536;
    static final int PERLIN_SIZE = 4095;
    static final int PERLIN_YWRAP = 16;
    static final int PERLIN_YWRAPB = 4;
    static final int PERLIN_ZWRAP = 256;
    static final int PERLIN_ZWRAPB = 8;
    private static NumberFormat float_nf;
    private static boolean float_nf_commas;
    private static int float_nf_left;
    private static int float_nf_right;
    private static NumberFormat int_nf;
    private static boolean int_nf_commas;
    private static int int_nf_digits;
    protected static HashMap<String, Pattern> matchPatterns;
    protected static Time time = new Time();
    public int displayHeight;
    public int displayWidth;
    protected int dmouseX;
    protected int dmouseY;
    protected int emouseX;
    protected int emouseY;
    InternalEventQueue eventQueue = new InternalEventQueue();
    protected boolean exitCalled;
    boolean external = false;
    public boolean finished;
    public boolean focused = false;
    public int frameCount;
    public float frameRate = 10.0f;
    protected long frameRateLastNanos = 0;
    protected long frameRatePeriod = 16666666;
    protected float frameRateTarget = 60.0f;

    /* renamed from: g */
    public PGraphics f2g;
    Handler handler;
    public int height;
    Random internalRandom;
    public char key;
    public int keyCode;
    public boolean keyPressed;
    protected boolean looping;
    long millisOffset = System.currentTimeMillis();
    int motionPointerId;
    public boolean mousePressed;
    public int mouseX;
    public int mouseY;
    protected boolean paused;
    float[] perlin;
    Random perlinRandom;
    int perlin_PI;
    int perlin_TWOPI;
    float perlin_amp_falloff = 0.5f;
    float[] perlin_cosTable;
    int perlin_octaves = 4;
    public int[] pixels;
    public int pmouseX;
    public int pmouseY;
    protected boolean redraw;
    HashMap<String, RegisteredMethods> registerMap = new HashMap<>();
    volatile int requestImageCount;
    public int requestImageMax = 4;
    public String sketchPath;
    protected boolean surfaceChanged;
    protected boolean surfaceReady;
    protected SurfaceView surfaceView;
    Thread thread;
    protected boolean viewFocused = false;
    public int width;
    protected boolean windowFocused = false;

    class AsyncImageLoader extends Thread {
        String filename;
        PImage vessel;

        public AsyncImageLoader(String str, PImage pImage) {
            this.filename = str;
            this.vessel = pImage;
        }

        public void run() {
            while (PApplet.this.requestImageCount == PApplet.this.requestImageMax) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
            PApplet.this.requestImageCount++;
            PImage loadImage = PApplet.this.loadImage(this.filename);
            if (loadImage == null) {
                this.vessel.width = -1;
                this.vessel.height = -1;
            } else {
                this.vessel.width = loadImage.width;
                this.vessel.height = loadImage.height;
                this.vessel.format = loadImage.format;
                this.vessel.pixels = loadImage.pixels;
                this.vessel.bitmap = loadImage.bitmap;
            }
            PApplet.this.requestImageCount--;
        }
    }

    class InternalEventQueue {
        protected int count;
        protected int offset;
        protected Event[] queue = new Event[10];

        InternalEventQueue() {
        }

        /* access modifiers changed from: 0000 */
        public synchronized void add(Event event) {
            if (this.count == this.queue.length) {
                this.queue = (Event[]) PApplet.expand((Object) this.queue);
            }
            Event[] eventArr = this.queue;
            int i = this.count;
            this.count = i + 1;
            eventArr[i] = event;
        }

        /* access modifiers changed from: 0000 */
        public synchronized boolean available() {
            return this.count != 0;
        }

        /* access modifiers changed from: 0000 */
        public synchronized Event remove() {
            Event event;
            if (this.offset == this.count) {
                throw new RuntimeException("Nothing left on the event queue.");
            }
            Event[] eventArr = this.queue;
            int i = this.offset;
            this.offset = i + 1;
            event = eventArr[i];
            if (this.offset == this.count) {
                this.offset = 0;
                this.count = 0;
            }
            return event;
        }
    }

    class RegisteredMethods {
        int count;
        Object[] emptyArgs = new Object[0];
        Method[] methods;
        Object[] objects;

        RegisteredMethods() {
        }

        /* access modifiers changed from: 0000 */
        public void add(Object obj, Method method) {
            if (findIndex(obj) == -1) {
                if (this.objects == null) {
                    this.objects = new Object[5];
                    this.methods = new Method[5];
                } else if (this.count == this.objects.length) {
                    this.objects = (Object[]) PApplet.expand((Object) this.objects);
                    this.methods = (Method[]) PApplet.expand((Object) this.methods);
                }
                this.objects[this.count] = obj;
                this.methods[this.count] = method;
                this.count++;
                return;
            }
            PApplet.this.die(method.getName() + "() already added for this instance of " + obj.getClass().getName());
        }

        /* access modifiers changed from: protected */
        public int findIndex(Object obj) {
            for (int i = 0; i < this.count; i++) {
                if (this.objects[i] == obj) {
                    return i;
                }
            }
            return -1;
        }

        /* access modifiers changed from: 0000 */
        public void handle() {
            handle(this.emptyArgs);
        }

        /* access modifiers changed from: 0000 */
        public void handle(Object[] objArr) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.count) {
                    try {
                        this.methods[i2].invoke(this.objects[i2], objArr);
                    } catch (Exception e) {
                        e = e;
                        if (e instanceof InvocationTargetException) {
                            e = ((InvocationTargetException) e).getCause();
                        }
                        if (e instanceof RuntimeException) {
                            throw ((RuntimeException) e);
                        }
                        e.printStackTrace();
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }

        public void remove(Object obj) {
            int findIndex = findIndex(obj);
            if (findIndex != -1) {
                this.count--;
                while (findIndex < this.count) {
                    this.objects[findIndex] = this.objects[findIndex + 1];
                    this.methods[findIndex] = this.methods[findIndex + 1];
                    findIndex++;
                }
                this.objects[this.count] = null;
                this.methods[this.count] = null;
            }
        }
    }

    public static class RendererChangeException extends RuntimeException {
    }

    public class SketchSurfaceView extends SurfaceView implements Callback {

        /* renamed from: g2 */
        PGraphicsAndroid2D f3g2;
        SurfaceHolder surfaceHolder = getHolder();

        public SketchSurfaceView(Context context, int i, int i2) {
            super(context);
            this.surfaceHolder.addCallback(this);
            this.surfaceHolder.setType(2);
            this.f3g2 = new PGraphicsAndroid2D();
            this.f3g2.setSize(i, i2);
            this.f3g2.setParent(PApplet.this);
            this.f3g2.setPrimary(true);
            PApplet.this.f2g = this.f3g2;
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
        }

        public boolean onKeyDown(int i, KeyEvent keyEvent) {
            return PApplet.this.surfaceKeyDown(i, keyEvent);
        }

        public boolean onKeyUp(int i, KeyEvent keyEvent) {
            return PApplet.this.surfaceKeyUp(i, keyEvent);
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            return PApplet.this.surfaceTouchEvent(motionEvent);
        }

        public void onWindowFocusChanged(boolean z) {
            PApplet.this.surfaceWindowFocusChanged(z);
        }

        public void surfaceChanged(SurfaceHolder surfaceHolder2, int i, int i2, int i3) {
            PApplet.this.surfaceChanged = true;
        }

        public void surfaceCreated(SurfaceHolder surfaceHolder2) {
        }

        public void surfaceDestroyed(SurfaceHolder surfaceHolder2) {
        }
    }

    public class SketchSurfaceViewGL extends GLSurfaceView {

        /* renamed from: g3 */
        PGraphicsOpenGL f4g3;
        SurfaceHolder surfaceHolder;

        public SketchSurfaceViewGL(Context context, int i, int i2, boolean z) {
            super(context);
            if (!(((ActivityManager) PApplet.this.getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= 131072)) {
                throw new RuntimeException("OpenGL ES 2.0 is not supported by this device.");
            }
            this.surfaceHolder = getHolder();
            this.surfaceHolder.addCallback(this);
            if (z) {
                this.f4g3 = new PGraphics3D();
            } else {
                this.f4g3 = new PGraphics2D();
            }
            this.f4g3.setParent(PApplet.this);
            this.f4g3.setPrimary(true);
            this.f4g3.setSize(i, i2);
            setEGLContextClientVersion(2);
            setRenderer(PGraphicsOpenGL.pgl.getRenderer());
            setRenderMode(0);
            PApplet.this.f2g = this.f4g3;
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
        }

        public PGraphics getGraphics() {
            return this.f4g3;
        }

        public boolean onKeyDown(int i, KeyEvent keyEvent) {
            return PApplet.this.surfaceKeyDown(i, keyEvent);
        }

        public boolean onKeyUp(int i, KeyEvent keyEvent) {
            return PApplet.this.surfaceKeyUp(i, keyEvent);
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            return PApplet.this.surfaceTouchEvent(motionEvent);
        }

        public void onWindowFocusChanged(boolean z) {
            PApplet.this.surfaceWindowFocusChanged(z);
        }

        public void surfaceChanged(SurfaceHolder surfaceHolder2, int i, int i2, int i3) {
            super.surfaceChanged(surfaceHolder2, i, i2, i3);
            PApplet.this.surfaceChanged = true;
        }

        public void surfaceCreated(SurfaceHolder surfaceHolder2) {
            super.surfaceCreated(surfaceHolder2);
        }

        public void surfaceDestroyed(SurfaceHolder surfaceHolder2) {
            super.surfaceDestroyed(surfaceHolder2);
        }
    }

    public static final float abs(float f) {
        return f < 0.0f ? -f : f;
    }

    public static final int abs(int i) {
        return i < 0 ? -i : i;
    }

    public static final float acos(float f) {
        return (float) Math.acos((double) f);
    }

    public static Object append(Object obj, Object obj2) {
        int length = Array.getLength(obj);
        Object expand = expand(obj, length + 1);
        Array.set(expand, length, obj2);
        return expand;
    }

    public static byte[] append(byte[] bArr, byte b) {
        byte[] expand = expand(bArr, bArr.length + 1);
        expand[expand.length - 1] = b;
        return expand;
    }

    public static char[] append(char[] cArr, char c) {
        char[] expand = expand(cArr, cArr.length + 1);
        expand[expand.length - 1] = c;
        return expand;
    }

    public static float[] append(float[] fArr, float f) {
        float[] expand = expand(fArr, fArr.length + 1);
        expand[expand.length - 1] = f;
        return expand;
    }

    public static int[] append(int[] iArr, int i) {
        int[] expand = expand(iArr, iArr.length + 1);
        expand[expand.length - 1] = i;
        return expand;
    }

    public static String[] append(String[] strArr, String str) {
        String[] expand = expand(strArr, strArr.length + 1);
        expand[expand.length - 1] = str;
        return expand;
    }

    public static void arrayCopy(Object obj, int i, Object obj2, int i2, int i3) {
        System.arraycopy(obj, i, obj2, i2, i3);
    }

    public static void arrayCopy(Object obj, Object obj2) {
        System.arraycopy(obj, 0, obj2, 0, Array.getLength(obj));
    }

    public static void arrayCopy(Object obj, Object obj2, int i) {
        System.arraycopy(obj, 0, obj2, 0, i);
    }

    public static final float asin(float f) {
        return (float) Math.asin((double) f);
    }

    public static final float atan(float f) {
        return (float) Math.atan((double) f);
    }

    public static final float atan2(float f, float f2) {
        return (float) Math.atan2((double) f, (double) f2);
    }

    public static final String binary(byte b) {
        return binary(b, 8);
    }

    public static final String binary(char c) {
        return binary(c, 16);
    }

    public static final String binary(int i) {
        return binary(i, 32);
    }

    public static final String binary(int i, int i2) {
        String binaryString = Integer.toBinaryString(i);
        if (i2 > 32) {
            i2 = 32;
        }
        int length = binaryString.length();
        if (length > i2) {
            return binaryString.substring(length - i2);
        }
        if (length >= i2) {
            return binaryString;
        }
        return "00000000000000000000000000000000".substring(32 - (i2 - length)) + binaryString;
    }

    public static int blendColor(int i, int i2, int i3) {
        return PImage.blendColor(i, i2, i3);
    }

    public static final int ceil(float f) {
        return (int) Math.ceil((double) f);
    }

    public static String checkExtension(String str) {
        if (str.toLowerCase().endsWith(".gz")) {
            str = str.substring(0, str.length() - 3);
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf != -1) {
            return str.substring(lastIndexOf + 1).toLowerCase();
        }
        return null;
    }

    public static Object concat(Object obj, Object obj2) {
        Class componentType = obj.getClass().getComponentType();
        int length = Array.getLength(obj);
        int length2 = Array.getLength(obj2);
        Object newInstance = Array.newInstance(componentType, length + length2);
        System.arraycopy(obj, 0, newInstance, 0, length);
        System.arraycopy(obj2, 0, newInstance, length, length2);
        return newInstance;
    }

    public static byte[] concat(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public static char[] concat(char[] cArr, char[] cArr2) {
        char[] cArr3 = new char[(cArr.length + cArr2.length)];
        System.arraycopy(cArr, 0, cArr3, 0, cArr.length);
        System.arraycopy(cArr2, 0, cArr3, cArr.length, cArr2.length);
        return cArr3;
    }

    public static float[] concat(float[] fArr, float[] fArr2) {
        float[] fArr3 = new float[(fArr.length + fArr2.length)];
        System.arraycopy(fArr, 0, fArr3, 0, fArr.length);
        System.arraycopy(fArr2, 0, fArr3, fArr.length, fArr2.length);
        return fArr3;
    }

    public static int[] concat(int[] iArr, int[] iArr2) {
        int[] iArr3 = new int[(iArr.length + iArr2.length)];
        System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
        System.arraycopy(iArr2, 0, iArr3, iArr.length, iArr2.length);
        return iArr3;
    }

    public static String[] concat(String[] strArr, String[] strArr2) {
        String[] strArr3 = new String[(strArr.length + strArr2.length)];
        System.arraycopy(strArr, 0, strArr3, 0, strArr.length);
        System.arraycopy(strArr2, 0, strArr3, strArr.length, strArr2.length);
        return strArr3;
    }

    public static boolean[] concat(boolean[] zArr, boolean[] zArr2) {
        boolean[] zArr3 = new boolean[(zArr.length + zArr2.length)];
        System.arraycopy(zArr, 0, zArr3, 0, zArr.length);
        System.arraycopy(zArr2, 0, zArr3, zArr.length, zArr2.length);
        return zArr3;
    }

    public static final float constrain(float f, float f2, float f3) {
        return f < f2 ? f2 : f > f3 ? f3 : f;
    }

    public static final int constrain(int i, int i2, int i3) {
        return i < i2 ? i2 : i > i3 ? i3 : i;
    }

    public static final float cos(float f) {
        return (float) Math.cos((double) f);
    }

    public static InputStream createInput(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File passed to createInput() was null");
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return file.getName().toLowerCase().endsWith(".gz") ? new GZIPInputStream(fileInputStream) : fileInputStream;
        } catch (IOException e) {
            System.err.println("Could not createInput() for " + file);
            e.printStackTrace();
            return null;
        }
    }

    public static OutputStream createOutput(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            return file.getName().toLowerCase().endsWith(".gz") ? new GZIPOutputStream(fileOutputStream) : fileOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createPath(File file) {
        try {
            String parent = file.getParent();
            if (parent != null) {
                File file2 = new File(parent);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
            }
        } catch (SecurityException e) {
            System.err.println("You don't have permissions to create " + file.getAbsolutePath());
        }
    }

    public static void createPath(String str) {
        createPath(new File(str));
    }

    public static BufferedReader createReader(File file) {
        try {
            InputStream fileInputStream = new FileInputStream(file);
            return createReader(file.getName().toLowerCase().endsWith(".gz") ? new GZIPInputStream(fileInputStream) : fileInputStream);
        } catch (Exception e) {
            if (file == null) {
                throw new RuntimeException("File passed to createReader() was null");
            }
            e.printStackTrace();
            throw new RuntimeException("Couldn't create a reader for " + file.getAbsolutePath());
        }
    }

    public static BufferedReader createReader(InputStream inputStream) {
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            inputStreamReader = null;
        }
        return new BufferedReader(inputStreamReader);
    }

    public static PrintWriter createWriter(File file) {
        try {
            OutputStream fileOutputStream = new FileOutputStream(file);
            return createWriter(file.getName().toLowerCase().endsWith(".gz") ? new GZIPOutputStream(fileOutputStream) : fileOutputStream);
        } catch (Exception e) {
            if (file == null) {
                throw new RuntimeException("File passed to createWriter() was null");
            }
            e.printStackTrace();
            throw new RuntimeException("Couldn't create a writer for " + file.getAbsolutePath());
        }
    }

    public static PrintWriter createWriter(OutputStream outputStream) {
        try {
            return new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(outputStream, PConstants.BURN), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static int day() {
        time.setToNow();
        return time.monthDay;
    }

    public static final float degrees(float f) {
        return 57.295776f * f;
    }

    public static final float dist(float f, float f2, float f3, float f4) {
        return sqrt(m4sq(f3 - f) + m4sq(f4 - f2));
    }

    public static final float dist(float f, float f2, float f3, float f4, float f5, float f6) {
        return sqrt(m4sq(f4 - f) + m4sq(f5 - f2) + m4sq(f6 - f3));
    }

    public static Process exec(String[] strArr) {
        try {
            return Runtime.getRuntime().exec(strArr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open " + join(strArr, ' '));
        }
    }

    public static final float exp(float f) {
        return (float) Math.exp((double) f);
    }

    public static Object expand(Object obj) {
        return expand(obj, Array.getLength(obj) << 1);
    }

    public static Object expand(Object obj, int i) {
        Object newInstance = Array.newInstance(obj.getClass().getComponentType(), i);
        System.arraycopy(obj, 0, newInstance, 0, Math.min(Array.getLength(obj), i));
        return newInstance;
    }

    public static byte[] expand(byte[] bArr) {
        return expand(bArr, bArr.length << 1);
    }

    public static byte[] expand(byte[] bArr, int i) {
        byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, Math.min(i, bArr.length));
        return bArr2;
    }

    public static char[] expand(char[] cArr) {
        return expand(cArr, cArr.length << 1);
    }

    public static char[] expand(char[] cArr, int i) {
        char[] cArr2 = new char[i];
        System.arraycopy(cArr, 0, cArr2, 0, Math.min(i, cArr.length));
        return cArr2;
    }

    public static float[] expand(float[] fArr) {
        return expand(fArr, fArr.length << 1);
    }

    public static float[] expand(float[] fArr, int i) {
        float[] fArr2 = new float[i];
        System.arraycopy(fArr, 0, fArr2, 0, Math.min(i, fArr.length));
        return fArr2;
    }

    public static int[] expand(int[] iArr) {
        return expand(iArr, iArr.length << 1);
    }

    public static int[] expand(int[] iArr, int i) {
        int[] iArr2 = new int[i];
        System.arraycopy(iArr, 0, iArr2, 0, Math.min(i, iArr.length));
        return iArr2;
    }

    public static String[] expand(String[] strArr) {
        return expand(strArr, strArr.length << 1);
    }

    public static String[] expand(String[] strArr, int i) {
        String[] strArr2 = new String[i];
        System.arraycopy(strArr, 0, strArr2, 0, Math.min(i, strArr.length));
        return strArr2;
    }

    public static PImage[] expand(PImage[] pImageArr) {
        return expand(pImageArr, pImageArr.length << 1);
    }

    public static PImage[] expand(PImage[] pImageArr, int i) {
        PImage[] pImageArr2 = new PImage[i];
        System.arraycopy(pImageArr, 0, pImageArr2, 0, Math.min(i, pImageArr.length));
        return pImageArr2;
    }

    public static boolean[] expand(boolean[] zArr) {
        return expand(zArr, zArr.length << 1);
    }

    public static boolean[] expand(boolean[] zArr, int i) {
        boolean[] zArr2 = new boolean[i];
        System.arraycopy(zArr, 0, zArr2, 0, Math.min(i, zArr.length));
        return zArr2;
    }

    public static final int floor(float f) {
        return (int) Math.floor((double) f);
    }

    public static String getExtension(String str) {
        String lowerCase = str.toLowerCase();
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf == -1) {
            String str2 = "unknown";
        }
        String substring = lowerCase.substring(lastIndexOf + 1);
        int indexOf = substring.indexOf(63);
        return indexOf != -1 ? substring.substring(0, indexOf) : substring;
    }

    public static final String hex(byte b) {
        return hex(b, 2);
    }

    public static final String hex(char c) {
        return hex(c, 4);
    }

    public static final String hex(int i) {
        return hex(i, 8);
    }

    public static final String hex(int i, int i2) {
        String upperCase = Integer.toHexString(i).toUpperCase();
        if (i2 > 8) {
            i2 = 8;
        }
        int length = upperCase.length();
        return length > i2 ? upperCase.substring(length - i2) : length < i2 ? "00000000".substring(8 - (i2 - length)) + upperCase : upperCase;
    }

    public static int hour() {
        time.setToNow();
        return time.hour;
    }

    public static String join(String[] strArr, char c) {
        return join(strArr, String.valueOf(c));
    }

    public static String join(String[] strArr, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strArr.length; i++) {
            if (i != 0) {
                stringBuffer.append(str);
            }
            stringBuffer.append(strArr[i]);
        }
        return stringBuffer.toString();
    }

    public static final float lerp(float f, float f2, float f3) {
        return ((f2 - f) * f3) + f;
    }

    public static int lerpColor(int i, int i2, float f, int i3) {
        return PGraphics.lerpColor(i, i2, f, i3);
    }

    public static byte[] loadBytes(File file) {
        return loadBytes(createInput(file));
    }

    public static byte[] loadBytes(InputStream inputStream) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int read = bufferedInputStream.read(); read != -1; read = bufferedInputStream.read()) {
                byteArrayOutputStream.write(read);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] loadStrings(BufferedReader bufferedReader) {
        String[] strArr;
        try {
            int i = 0;
            String[] strArr2 = new String[100];
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                if (i == strArr2.length) {
                    strArr = new String[(i << 1)];
                    System.arraycopy(strArr2, 0, strArr, 0, i);
                } else {
                    strArr = strArr2;
                }
                int i2 = i + 1;
                strArr[i] = readLine;
                i = i2;
                strArr2 = strArr;
            }
            bufferedReader.close();
            if (i == strArr2.length) {
                return strArr2;
            }
            String[] strArr3 = new String[i];
            System.arraycopy(strArr2, 0, strArr3, 0, i);
            return strArr3;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] loadStrings(File file) {
        InputStream createInput = createInput(file);
        if (createInput != null) {
            return loadStrings(createInput);
        }
        return null;
    }

    public static String[] loadStrings(InputStream inputStream) {
        String[] strArr;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int i = 0;
            String[] strArr2 = new String[100];
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                if (i == strArr2.length) {
                    strArr = new String[(i << 1)];
                    System.arraycopy(strArr2, 0, strArr, 0, i);
                } else {
                    strArr = strArr2;
                }
                int i2 = i + 1;
                strArr[i] = readLine;
                i = i2;
                strArr2 = strArr;
            }
            bufferedReader.close();
            if (i == strArr2.length) {
                return strArr2;
            }
            String[] strArr3 = new String[i];
            System.arraycopy(strArr2, 0, strArr3, 0, i);
            return strArr3;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final float log(float f) {
        return (float) Math.log((double) f);
    }

    public static final float mag(float f, float f2) {
        return (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
    }

    public static final float mag(float f, float f2, float f3) {
        return (float) Math.sqrt((double) ((f * f) + (f2 * f2) + (f3 * f3)));
    }

    public static void main(String[] strArr) {
    }

    public static final float map(float f, float f2, float f3, float f4, float f5) {
        return ((f5 - f4) * ((f - f2) / (f3 - f2))) + f4;
    }

    public static String[] match(String str, String str2) {
        Matcher matcher = matchPattern(str2).matcher(str);
        if (!matcher.find()) {
            return null;
        }
        int groupCount = matcher.groupCount() + 1;
        String[] strArr = new String[groupCount];
        for (int i = 0; i < groupCount; i++) {
            strArr[i] = matcher.group(i);
        }
        return strArr;
    }

    public static String[][] matchAll(String str, String str2) {
        int i = 0;
        Matcher matcher = matchPattern(str2).matcher(str);
        ArrayList arrayList = new ArrayList();
        int groupCount = matcher.groupCount() + 1;
        while (matcher.find()) {
            String[] strArr = new String[groupCount];
            for (int i2 = 0; i2 < groupCount; i2++) {
                strArr[i2] = matcher.group(i2);
            }
            arrayList.add(strArr);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        String[][] strArr2 = (String[][]) Array.newInstance(String.class, new int[]{arrayList.size(), groupCount});
        while (true) {
            int i3 = i;
            if (i3 >= strArr2.length) {
                return strArr2;
            }
            strArr2[i3] = (String[]) arrayList.get(i3);
            i = i3 + 1;
        }
    }

    static Pattern matchPattern(String str) {
        Pattern pattern = null;
        if (matchPatterns == null) {
            matchPatterns = new HashMap<>();
        } else {
            pattern = (Pattern) matchPatterns.get(str);
        }
        if (pattern != null) {
            return pattern;
        }
        if (matchPatterns.size() == 10) {
            matchPatterns.clear();
        }
        Pattern compile = Pattern.compile(str, 40);
        matchPatterns.put(str, compile);
        return compile;
    }

    public static final float max(float f, float f2) {
        return f > f2 ? f : f2;
    }

    public static final float max(float f, float f2, float f3) {
        return f > f2 ? f > f3 ? f : f3 : f2 > f3 ? f2 : f3;
    }

    public static final float max(float[] fArr) {
        if (fArr.length == 0) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MIN_MAX);
        }
        float f = fArr[0];
        for (int i = 1; i < fArr.length; i++) {
            if (fArr[i] > f) {
                f = fArr[i];
            }
        }
        return f;
    }

    public static final int max(int i, int i2) {
        return i > i2 ? i : i2;
    }

    public static final int max(int i, int i2, int i3) {
        return i > i2 ? i > i3 ? i : i3 : i2 > i3 ? i2 : i3;
    }

    public static final int max(int[] iArr) {
        if (iArr.length == 0) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MIN_MAX);
        }
        int i = iArr[0];
        for (int i2 = 1; i2 < iArr.length; i2++) {
            if (iArr[i2] > i) {
                i = iArr[i2];
            }
        }
        return i;
    }

    public static final float min(float f, float f2) {
        return f < f2 ? f : f2;
    }

    public static final float min(float f, float f2, float f3) {
        return f < f2 ? f < f3 ? f : f3 : f2 < f3 ? f2 : f3;
    }

    public static final float min(float[] fArr) {
        if (fArr.length == 0) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MIN_MAX);
        }
        float f = fArr[0];
        for (int i = 1; i < fArr.length; i++) {
            if (fArr[i] < f) {
                f = fArr[i];
            }
        }
        return f;
    }

    public static final int min(int i, int i2) {
        return i < i2 ? i : i2;
    }

    public static final int min(int i, int i2, int i3) {
        return i < i2 ? i < i3 ? i : i3 : i2 < i3 ? i2 : i3;
    }

    public static final int min(int[] iArr) {
        if (iArr.length == 0) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MIN_MAX);
        }
        int i = iArr[0];
        for (int i2 = 1; i2 < iArr.length; i2++) {
            if (iArr[i2] < i) {
                i = iArr[i2];
            }
        }
        return i;
    }

    public static int minute() {
        time.setToNow();
        return time.minute;
    }

    public static int month() {
        time.setToNow();
        return time.month + 1;
    }

    /* renamed from: nf */
    public static String m0nf(float f, int i, int i2) {
        if (float_nf != null && float_nf_left == i && float_nf_right == i2 && !float_nf_commas) {
            return float_nf.format((double) f);
        }
        float_nf = NumberFormat.getInstance();
        float_nf.setGroupingUsed(false);
        float_nf_commas = false;
        if (i != 0) {
            float_nf.setMinimumIntegerDigits(i);
        }
        if (i2 != 0) {
            float_nf.setMinimumFractionDigits(i2);
            float_nf.setMaximumFractionDigits(i2);
        }
        float_nf_left = i;
        float_nf_right = i2;
        return float_nf.format((double) f);
    }

    /* renamed from: nf */
    public static String m1nf(int i, int i2) {
        if (int_nf != null && int_nf_digits == i2 && !int_nf_commas) {
            return int_nf.format((long) i);
        }
        int_nf = NumberFormat.getInstance();
        int_nf.setGroupingUsed(false);
        int_nf_commas = false;
        int_nf.setMinimumIntegerDigits(i2);
        int_nf_digits = i2;
        return int_nf.format((long) i);
    }

    /* renamed from: nf */
    public static String[] m2nf(float[] fArr, int i, int i2) {
        String[] strArr = new String[fArr.length];
        for (int i3 = 0; i3 < strArr.length; i3++) {
            strArr[i3] = m0nf(fArr[i3], i, i2);
        }
        return strArr;
    }

    /* renamed from: nf */
    public static String[] m3nf(int[] iArr, int i) {
        String[] strArr = new String[iArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = m1nf(iArr[i2], i);
        }
        return strArr;
    }

    public static String nfc(float f, int i) {
        if (float_nf != null && float_nf_left == 0 && float_nf_right == i && float_nf_commas) {
            return float_nf.format((double) f);
        }
        float_nf = NumberFormat.getInstance();
        float_nf.setGroupingUsed(true);
        float_nf_commas = true;
        if (i != 0) {
            float_nf.setMinimumFractionDigits(i);
            float_nf.setMaximumFractionDigits(i);
        }
        float_nf_left = 0;
        float_nf_right = i;
        return float_nf.format((double) f);
    }

    public static String nfc(int i) {
        if (int_nf != null && int_nf_digits == 0 && int_nf_commas) {
            return int_nf.format((long) i);
        }
        int_nf = NumberFormat.getInstance();
        int_nf.setGroupingUsed(true);
        int_nf_commas = true;
        int_nf.setMinimumIntegerDigits(0);
        int_nf_digits = 0;
        return int_nf.format((long) i);
    }

    public static String[] nfc(float[] fArr, int i) {
        String[] strArr = new String[fArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = nfc(fArr[i2], i);
        }
        return strArr;
    }

    public static String[] nfc(int[] iArr) {
        String[] strArr = new String[iArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = nfc(iArr[i]);
        }
        return strArr;
    }

    public static String nfp(float f, int i, int i2) {
        return f < 0.0f ? m0nf(f, i, i2) : '+' + m0nf(f, i, i2);
    }

    public static String nfp(int i, int i2) {
        return i < 0 ? m1nf(i, i2) : '+' + m1nf(i, i2);
    }

    public static String[] nfp(float[] fArr, int i, int i2) {
        String[] strArr = new String[fArr.length];
        for (int i3 = 0; i3 < strArr.length; i3++) {
            strArr[i3] = nfp(fArr[i3], i, i2);
        }
        return strArr;
    }

    public static String[] nfp(int[] iArr, int i) {
        String[] strArr = new String[iArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = nfp(iArr[i2], i);
        }
        return strArr;
    }

    public static String nfs(float f, int i, int i2) {
        return f < 0.0f ? m0nf(f, i, i2) : ' ' + m0nf(f, i, i2);
    }

    public static String nfs(int i, int i2) {
        return i < 0 ? m1nf(i, i2) : ' ' + m1nf(i, i2);
    }

    public static String[] nfs(float[] fArr, int i, int i2) {
        String[] strArr = new String[fArr.length];
        for (int i3 = 0; i3 < strArr.length; i3++) {
            strArr[i3] = nfs(fArr[i3], i, i2);
        }
        return strArr;
    }

    public static String[] nfs(int[] iArr, int i) {
        String[] strArr = new String[iArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = nfs(iArr[i2], i);
        }
        return strArr;
    }

    private float noise_fsc(float f) {
        return 0.5f * (1.0f - this.perlin_cosTable[((int) (((float) this.perlin_PI) * f)) % this.perlin_TWOPI]);
    }

    public static final float norm(float f, float f2, float f3) {
        return (f - f2) / (f3 - f2);
    }

    public static Process open(String[] strArr) {
        return exec(strArr);
    }

    public static void open(String str) {
        open(new String[]{str});
    }

    public static final boolean parseBoolean(int i) {
        return i != 0;
    }

    public static final boolean parseBoolean(String str) {
        return new Boolean(str).booleanValue();
    }

    public static final boolean[] parseBoolean(byte[] bArr) {
        boolean[] zArr = new boolean[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            zArr[i] = bArr[i] != 0;
        }
        return zArr;
    }

    public static final boolean[] parseBoolean(int[] iArr) {
        boolean[] zArr = new boolean[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            zArr[i] = iArr[i] != 0;
        }
        return zArr;
    }

    public static final boolean[] parseBoolean(String[] strArr) {
        boolean[] zArr = new boolean[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            zArr[i] = new Boolean(strArr[i]).booleanValue();
        }
        return zArr;
    }

    public static final byte parseByte(char c) {
        return (byte) c;
    }

    public static final byte parseByte(float f) {
        return (byte) ((int) f);
    }

    public static final byte parseByte(int i) {
        return (byte) i;
    }

    public static final byte parseByte(boolean z) {
        return z ? (byte) 1 : 0;
    }

    public static final byte[] parseByte(char[] cArr) {
        byte[] bArr = new byte[cArr.length];
        for (int i = 0; i < cArr.length; i++) {
            bArr[i] = (byte) cArr[i];
        }
        return bArr;
    }

    public static final byte[] parseByte(float[] fArr) {
        byte[] bArr = new byte[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            bArr[i] = (byte) ((int) fArr[i]);
        }
        return bArr;
    }

    public static final byte[] parseByte(int[] iArr) {
        byte[] bArr = new byte[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            bArr[i] = (byte) iArr[i];
        }
        return bArr;
    }

    public static final byte[] parseByte(boolean[] zArr) {
        byte[] bArr = new byte[zArr.length];
        for (int i = 0; i < zArr.length; i++) {
            bArr[i] = zArr[i] ? (byte) 1 : 0;
        }
        return bArr;
    }

    public static final float[] parseByte(byte[] bArr) {
        float[] fArr = new float[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            fArr[i] = (float) bArr[i];
        }
        return fArr;
    }

    public static final char parseChar(byte b) {
        return (char) (b & 255);
    }

    public static final char parseChar(int i) {
        return (char) i;
    }

    public static final char[] parseChar(byte[] bArr) {
        char[] cArr = new char[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            cArr[i] = (char) (bArr[i] & 255);
        }
        return cArr;
    }

    public static final char[] parseChar(int[] iArr) {
        char[] cArr = new char[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            cArr[i] = (char) iArr[i];
        }
        return cArr;
    }

    public static final float parseFloat(int i) {
        return (float) i;
    }

    public static final float parseFloat(String str) {
        return parseFloat(str, Float.NaN);
    }

    public static final float parseFloat(String str, float f) {
        try {
            return new Float(str).floatValue();
        } catch (NumberFormatException e) {
            return f;
        }
    }

    public static final float[] parseFloat(int[] iArr) {
        float[] fArr = new float[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            fArr[i] = (float) iArr[i];
        }
        return fArr;
    }

    public static final float[] parseFloat(String[] strArr) {
        return parseFloat(strArr, Float.NaN);
    }

    public static final float[] parseFloat(String[] strArr, float f) {
        float[] fArr = new float[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                fArr[i] = new Float(strArr[i]).floatValue();
            } catch (NumberFormatException e) {
                fArr[i] = f;
            }
        }
        return fArr;
    }

    public static final int parseInt(byte b) {
        return b & 255;
    }

    public static final int parseInt(char c) {
        return c;
    }

    public static final int parseInt(float f) {
        return (int) f;
    }

    public static final int parseInt(String str) {
        return parseInt(str, 0);
    }

    public static final int parseInt(String str, int i) {
        try {
            int indexOf = str.indexOf(46);
            return indexOf == -1 ? Integer.parseInt(str) : Integer.parseInt(str.substring(0, indexOf));
        } catch (NumberFormatException e) {
            return i;
        }
    }

    public static final int parseInt(boolean z) {
        return z ? 1 : 0;
    }

    public static final int[] parseInt(byte[] bArr) {
        int[] iArr = new int[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            iArr[i] = bArr[i] & 255;
        }
        return iArr;
    }

    public static final int[] parseInt(char[] cArr) {
        int[] iArr = new int[cArr.length];
        for (int i = 0; i < cArr.length; i++) {
            iArr[i] = cArr[i];
        }
        return iArr;
    }

    public static int[] parseInt(float[] fArr) {
        int[] iArr = new int[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            iArr[i] = (int) fArr[i];
        }
        return iArr;
    }

    public static int[] parseInt(String[] strArr) {
        return parseInt(strArr, 0);
    }

    public static int[] parseInt(String[] strArr, int i) {
        int[] iArr = new int[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            try {
                iArr[i2] = Integer.parseInt(strArr[i2]);
            } catch (NumberFormatException e) {
                iArr[i2] = i;
            }
        }
        return iArr;
    }

    public static final int[] parseInt(boolean[] zArr) {
        int[] iArr = new int[zArr.length];
        for (int i = 0; i < zArr.length; i++) {
            iArr[i] = zArr[i] ? 1 : 0;
        }
        return iArr;
    }

    public static final float pow(float f, float f2) {
        return (float) Math.pow((double) f, (double) f2);
    }

    public static void print(byte b) {
        System.out.print(b);
        System.out.flush();
    }

    public static void print(char c) {
        System.out.print(c);
        System.out.flush();
    }

    public static void print(float f) {
        System.out.print(f);
        System.out.flush();
    }

    public static void print(int i) {
        System.out.print(i);
        System.out.flush();
    }

    public static void print(Object obj) {
        if (obj == null) {
            System.out.print("null");
        } else {
            System.out.println(obj.toString());
        }
    }

    public static void print(String str) {
        System.out.print(str);
        System.out.flush();
    }

    public static void print(boolean z) {
        System.out.print(z);
        System.out.flush();
    }

    public static void println() {
        System.out.println();
    }

    public static void println(byte b) {
        print(b);
        System.out.println();
    }

    public static void println(char c) {
        print(c);
        System.out.println();
    }

    public static void println(float f) {
        print(f);
        System.out.println();
    }

    public static void println(int i) {
        print(i);
        System.out.println();
    }

    public static void println(Object obj) {
        int i = 0;
        if (obj == null) {
            System.out.println("null");
            return;
        }
        String name = obj.getClass().getName();
        if (name.charAt(0) == '[') {
            switch (name.charAt(1)) {
                case 'B':
                    byte[] bArr = (byte[]) obj;
                    while (i < bArr.length) {
                        System.out.println("[" + i + "] " + bArr[i]);
                        i++;
                    }
                    return;
                case 'C':
                    char[] cArr = (char[]) obj;
                    while (i < cArr.length) {
                        System.out.println("[" + i + "] '" + cArr[i] + "'");
                        i++;
                    }
                    return;
                case 'F':
                    float[] fArr = (float[]) obj;
                    while (i < fArr.length) {
                        System.out.println("[" + i + "] " + fArr[i]);
                        i++;
                    }
                    return;
                case 'I':
                    int[] iArr = (int[]) obj;
                    while (i < iArr.length) {
                        System.out.println("[" + i + "] " + iArr[i]);
                        i++;
                    }
                    return;
                case 'L':
                    Object[] objArr = (Object[]) obj;
                    while (i < objArr.length) {
                        if (objArr[i] instanceof String) {
                            System.out.println("[" + i + "] \"" + objArr[i] + "\"");
                        } else {
                            System.out.println("[" + i + "] " + objArr[i]);
                        }
                        i++;
                    }
                    return;
                case 'Z':
                    boolean[] zArr = (boolean[]) obj;
                    while (i < zArr.length) {
                        System.out.println("[" + i + "] " + zArr[i]);
                        i++;
                    }
                    return;
                case '[':
                    System.out.println(obj);
                    return;
                default:
                    System.out.println(obj);
                    return;
            }
        } else {
            System.out.println(obj);
        }
    }

    public static void println(String str) {
        print(str);
        System.out.println();
    }

    public static void println(boolean z) {
        print(z);
        System.out.println();
    }

    public static final float radians(float f) {
        return 0.017453292f * f;
    }

    private void registerNoArgs(String str, Object obj) {
        RegisteredMethods registeredMethods = (RegisteredMethods) this.registerMap.get(str);
        if (registeredMethods == null) {
            registeredMethods = new RegisteredMethods();
            this.registerMap.put(str, registeredMethods);
        }
        try {
            registeredMethods.add(obj, obj.getClass().getMethod(str, new Class[0]));
        } catch (NoSuchMethodException e) {
            die("There is no public " + str + "() method in the class " + obj.getClass().getName());
        } catch (Exception e2) {
            die("Could not register " + str + " + () for " + obj, e2);
        }
    }

    private void registerWithArgs(String str, Object obj, Class<?>[] clsArr) {
        RegisteredMethods registeredMethods = (RegisteredMethods) this.registerMap.get(str);
        if (registeredMethods == null) {
            registeredMethods = new RegisteredMethods();
            this.registerMap.put(str, registeredMethods);
        }
        try {
            registeredMethods.add(obj, obj.getClass().getMethod(str, clsArr));
        } catch (NoSuchMethodException e) {
            die("There is no public " + str + "() method in the class " + obj.getClass().getName());
        } catch (Exception e2) {
            die("Could not register " + str + " + () for " + obj, e2);
        }
    }

    public static Object reverse(Object obj) {
        Class componentType = obj.getClass().getComponentType();
        int length = Array.getLength(obj);
        Object newInstance = Array.newInstance(componentType, length);
        for (int i = 0; i < length; i++) {
            Array.set(newInstance, i, Array.get(obj, (length - 1) - i));
        }
        return newInstance;
    }

    public static byte[] reverse(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        int length = bArr.length - 1;
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[length - i];
        }
        return bArr2;
    }

    public static char[] reverse(char[] cArr) {
        char[] cArr2 = new char[cArr.length];
        int length = cArr.length - 1;
        for (int i = 0; i < cArr.length; i++) {
            cArr2[i] = cArr[length - i];
        }
        return cArr2;
    }

    public static float[] reverse(float[] fArr) {
        float[] fArr2 = new float[fArr.length];
        int length = fArr.length - 1;
        for (int i = 0; i < fArr.length; i++) {
            fArr2[i] = fArr[length - i];
        }
        return fArr2;
    }

    public static int[] reverse(int[] iArr) {
        int[] iArr2 = new int[iArr.length];
        int length = iArr.length - 1;
        for (int i = 0; i < iArr.length; i++) {
            iArr2[i] = iArr[length - i];
        }
        return iArr2;
    }

    public static String[] reverse(String[] strArr) {
        String[] strArr2 = new String[strArr.length];
        int length = strArr.length - 1;
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = strArr[length - i];
        }
        return strArr2;
    }

    public static boolean[] reverse(boolean[] zArr) {
        boolean[] zArr2 = new boolean[zArr.length];
        int length = zArr.length - 1;
        for (int i = 0; i < zArr.length; i++) {
            zArr2[i] = zArr[length - i];
        }
        return zArr2;
    }

    public static final int round(float f) {
        return Math.round(f);
    }

    public static void saveBytes(File file, byte[] bArr) {
        try {
            createPath(file.getAbsolutePath());
            OutputStream fileOutputStream = new FileOutputStream(file);
            OutputStream outputStream = file.getName().toLowerCase().endsWith(".gz") ? new GZIPOutputStream(fileOutputStream) : fileOutputStream;
            saveBytes(outputStream, bArr);
            outputStream.close();
        } catch (IOException e) {
            System.err.println("error saving bytes to " + file);
            e.printStackTrace();
        }
    }

    public static void saveBytes(OutputStream outputStream, byte[] bArr) {
        try {
            outputStream.write(bArr);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveStream(File file, InputStream inputStream) {
        File file2 = null;
        try {
            File parentFile = file.getParentFile();
            createPath(file);
            file2 = File.createTempFile(file.getName(), null, parentFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, PGL.COLOR_BUFFER_BIT);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            byte[] bArr = new byte[PConstants.BURN];
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                bufferedOutputStream.write(bArr, 0, read);
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            if (file.exists() && !file.delete()) {
                System.err.println("Could not replace " + file.getAbsolutePath() + ".");
            }
            if (file2.renameTo(file)) {
                return true;
            }
            System.err.println("Could not rename temporary file " + file2.getAbsolutePath());
            return false;
        } catch (IOException e) {
            if (file2 != null) {
                file2.delete();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static void saveStrings(File file, String[] strArr) {
        try {
            String absolutePath = file.getAbsolutePath();
            createPath(absolutePath);
            OutputStream fileOutputStream = new FileOutputStream(absolutePath);
            OutputStream outputStream = file.getName().toLowerCase().endsWith(".gz") ? new GZIPOutputStream(fileOutputStream) : fileOutputStream;
            saveStrings(outputStream, strArr);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveStrings(OutputStream outputStream, String[] strArr) {
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            for (String println : strArr) {
                printWriter.println(println);
            }
            printWriter.flush();
        } catch (UnsupportedEncodingException e) {
        }
    }

    public static int second() {
        time.setToNow();
        return time.second;
    }

    public static Object shorten(Object obj) {
        return subset(obj, 0, Array.getLength(obj) - 1);
    }

    public static byte[] shorten(byte[] bArr) {
        return subset(bArr, 0, bArr.length - 1);
    }

    public static char[] shorten(char[] cArr) {
        return subset(cArr, 0, cArr.length - 1);
    }

    public static float[] shorten(float[] fArr) {
        return subset(fArr, 0, fArr.length - 1);
    }

    public static int[] shorten(int[] iArr) {
        return subset(iArr, 0, iArr.length - 1);
    }

    public static String[] shorten(String[] strArr) {
        return subset(strArr, 0, strArr.length - 1);
    }

    public static boolean[] shorten(boolean[] zArr) {
        return subset(zArr, 0, zArr.length - 1);
    }

    public static void showDepthWarning(String str) {
        PGraphics.showDepthWarning(str);
    }

    public static void showDepthWarningXYZ(String str) {
        PGraphics.showDepthWarningXYZ(str);
    }

    public static void showMethodWarning(String str) {
        PGraphics.showMethodWarning(str);
    }

    public static void showMissingWarning(String str) {
        PGraphics.showMissingWarning(str);
    }

    public static void showVariationWarning(String str) {
        PGraphics.showVariationWarning(str);
    }

    public static final float sin(float f) {
        return (float) Math.sin((double) f);
    }

    public static byte[] sort(byte[] bArr) {
        return sort(bArr, bArr.length);
    }

    public static byte[] sort(byte[] bArr, int i) {
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        Arrays.sort(bArr2, 0, i);
        return bArr2;
    }

    public static char[] sort(char[] cArr) {
        return sort(cArr, cArr.length);
    }

    public static char[] sort(char[] cArr, int i) {
        char[] cArr2 = new char[cArr.length];
        System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
        Arrays.sort(cArr2, 0, i);
        return cArr2;
    }

    public static float[] sort(float[] fArr) {
        return sort(fArr, fArr.length);
    }

    public static float[] sort(float[] fArr, int i) {
        float[] fArr2 = new float[fArr.length];
        System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
        Arrays.sort(fArr2, 0, i);
        return fArr2;
    }

    public static int[] sort(int[] iArr) {
        return sort(iArr, iArr.length);
    }

    public static int[] sort(int[] iArr, int i) {
        int[] iArr2 = new int[iArr.length];
        System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
        Arrays.sort(iArr2, 0, i);
        return iArr2;
    }

    public static String[] sort(String[] strArr) {
        return sort(strArr, strArr.length);
    }

    public static String[] sort(String[] strArr, int i) {
        String[] strArr2 = new String[strArr.length];
        System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        Arrays.sort(strArr2, 0, i);
        return strArr2;
    }

    public static final Object splice(Object obj, Object obj2, int i) {
        int length = Array.getLength(obj);
        if (obj2.getClass().getName().charAt(0) == '[') {
            int length2 = Array.getLength(obj2);
            Object[] objArr = new Object[(length + length2)];
            System.arraycopy(obj, 0, objArr, 0, i);
            System.arraycopy(obj2, 0, objArr, i, length2);
            System.arraycopy(obj, i, objArr, length2 + i, length - i);
            return objArr;
        }
        Object[] objArr2 = new Object[(length + 1)];
        System.arraycopy(obj, 0, objArr2, 0, i);
        Array.set(objArr2, i, obj2);
        System.arraycopy(obj, i, objArr2, i + 1, length - i);
        return objArr2;
    }

    public static final byte[] splice(byte[] bArr, byte b, int i) {
        byte[] bArr2 = new byte[(bArr.length + 1)];
        System.arraycopy(bArr, 0, bArr2, 0, i);
        bArr2[i] = b;
        System.arraycopy(bArr, i, bArr2, i + 1, bArr.length - i);
        return bArr2;
    }

    public static final byte[] splice(byte[] bArr, byte[] bArr2, int i) {
        byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, bArr3, 0, i);
        System.arraycopy(bArr2, 0, bArr3, i, bArr2.length);
        System.arraycopy(bArr, i, bArr3, bArr2.length + i, bArr.length - i);
        return bArr3;
    }

    public static final char[] splice(char[] cArr, char c, int i) {
        char[] cArr2 = new char[(cArr.length + 1)];
        System.arraycopy(cArr, 0, cArr2, 0, i);
        cArr2[i] = c;
        System.arraycopy(cArr, i, cArr2, i + 1, cArr.length - i);
        return cArr2;
    }

    public static final char[] splice(char[] cArr, char[] cArr2, int i) {
        char[] cArr3 = new char[(cArr.length + cArr2.length)];
        System.arraycopy(cArr, 0, cArr3, 0, i);
        System.arraycopy(cArr2, 0, cArr3, i, cArr2.length);
        System.arraycopy(cArr, i, cArr3, cArr2.length + i, cArr.length - i);
        return cArr3;
    }

    public static final float[] splice(float[] fArr, float f, int i) {
        float[] fArr2 = new float[(fArr.length + 1)];
        System.arraycopy(fArr, 0, fArr2, 0, i);
        fArr2[i] = f;
        System.arraycopy(fArr, i, fArr2, i + 1, fArr.length - i);
        return fArr2;
    }

    public static final float[] splice(float[] fArr, float[] fArr2, int i) {
        float[] fArr3 = new float[(fArr.length + fArr2.length)];
        System.arraycopy(fArr, 0, fArr3, 0, i);
        System.arraycopy(fArr2, 0, fArr3, i, fArr2.length);
        System.arraycopy(fArr, i, fArr3, fArr2.length + i, fArr.length - i);
        return fArr3;
    }

    public static final int[] splice(int[] iArr, int i, int i2) {
        int[] iArr2 = new int[(iArr.length + 1)];
        System.arraycopy(iArr, 0, iArr2, 0, i2);
        iArr2[i2] = i;
        System.arraycopy(iArr, i2, iArr2, i2 + 1, iArr.length - i2);
        return iArr2;
    }

    public static final int[] splice(int[] iArr, int[] iArr2, int i) {
        int[] iArr3 = new int[(iArr.length + iArr2.length)];
        System.arraycopy(iArr, 0, iArr3, 0, i);
        System.arraycopy(iArr2, 0, iArr3, i, iArr2.length);
        System.arraycopy(iArr, i, iArr3, iArr2.length + i, iArr.length - i);
        return iArr3;
    }

    public static final String[] splice(String[] strArr, String str, int i) {
        String[] strArr2 = new String[(strArr.length + 1)];
        System.arraycopy(strArr, 0, strArr2, 0, i);
        strArr2[i] = str;
        System.arraycopy(strArr, i, strArr2, i + 1, strArr.length - i);
        return strArr2;
    }

    public static final String[] splice(String[] strArr, String[] strArr2, int i) {
        String[] strArr3 = new String[(strArr.length + strArr2.length)];
        System.arraycopy(strArr, 0, strArr3, 0, i);
        System.arraycopy(strArr2, 0, strArr3, i, strArr2.length);
        System.arraycopy(strArr, i, strArr3, strArr2.length + i, strArr.length - i);
        return strArr3;
    }

    public static final boolean[] splice(boolean[] zArr, boolean z, int i) {
        boolean[] zArr2 = new boolean[(zArr.length + 1)];
        System.arraycopy(zArr, 0, zArr2, 0, i);
        zArr2[i] = z;
        System.arraycopy(zArr, i, zArr2, i + 1, zArr.length - i);
        return zArr2;
    }

    public static final boolean[] splice(boolean[] zArr, boolean[] zArr2, int i) {
        boolean[] zArr3 = new boolean[(zArr.length + zArr2.length)];
        System.arraycopy(zArr, 0, zArr3, 0, i);
        System.arraycopy(zArr2, 0, zArr3, i, zArr2.length);
        System.arraycopy(zArr, i, zArr3, zArr2.length + i, zArr.length - i);
        return zArr3;
    }

    public static String[] split(String str, char c) {
        if (str == null) {
            return null;
        }
        char[] charArray = str.toCharArray();
        int i = 0;
        for (char c2 : charArray) {
            if (c2 == c) {
                i++;
            }
        }
        if (i == 0) {
            return new String[]{new String(str)};
        }
        String[] strArr = new String[(i + 1)];
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < charArray.length; i4++) {
            if (charArray[i4] == c) {
                int i5 = i3 + 1;
                strArr[i3] = new String(charArray, i2, i4 - i2);
                i2 = i4 + 1;
                i3 = i5;
            }
        }
        strArr[i3] = new String(charArray, i2, charArray.length - i2);
        return strArr;
    }

    public static String[] split(String str, String str2) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int indexOf = str.indexOf(str2, i);
            if (indexOf != -1) {
                arrayList.add(str.substring(i, indexOf));
                i = str2.length() + indexOf;
            } else {
                arrayList.add(str.substring(i));
                String[] strArr = new String[arrayList.size()];
                arrayList.toArray(strArr);
                return strArr;
            }
        }
    }

    public static String[] splitTokens(String str) {
        return splitTokens(str, PConstants.WHITESPACE);
    }

    public static String[] splitTokens(String str, String str2) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, str2);
        String[] strArr = new String[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            int i2 = i + 1;
            strArr[i] = stringTokenizer.nextToken();
            i = i2;
        }
        return strArr;
    }

    /* renamed from: sq */
    public static final float m4sq(float f) {
        return f * f;
    }

    public static final float sqrt(float f) {
        return (float) Math.sqrt((double) f);
    }

    public static final String str(byte b) {
        return String.valueOf(b);
    }

    public static final String str(char c) {
        return String.valueOf(c);
    }

    public static final String str(float f) {
        return String.valueOf(f);
    }

    public static final String str(int i) {
        return String.valueOf(i);
    }

    public static final String str(boolean z) {
        return String.valueOf(z);
    }

    public static final String[] str(byte[] bArr) {
        String[] strArr = new String[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            strArr[i] = String.valueOf(bArr[i]);
        }
        return strArr;
    }

    public static final String[] str(char[] cArr) {
        String[] strArr = new String[cArr.length];
        for (int i = 0; i < cArr.length; i++) {
            strArr[i] = String.valueOf(cArr[i]);
        }
        return strArr;
    }

    public static final String[] str(float[] fArr) {
        String[] strArr = new String[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            strArr[i] = String.valueOf(fArr[i]);
        }
        return strArr;
    }

    public static final String[] str(int[] iArr) {
        String[] strArr = new String[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            strArr[i] = String.valueOf(iArr[i]);
        }
        return strArr;
    }

    public static final String[] str(boolean[] zArr) {
        String[] strArr = new String[zArr.length];
        for (int i = 0; i < zArr.length; i++) {
            strArr[i] = String.valueOf(zArr[i]);
        }
        return strArr;
    }

    public static Object subset(Object obj, int i) {
        return subset(obj, i, Array.getLength(obj) - i);
    }

    public static Object subset(Object obj, int i, int i2) {
        Object newInstance = Array.newInstance(obj.getClass().getComponentType(), i2);
        System.arraycopy(obj, i, newInstance, 0, i2);
        return newInstance;
    }

    public static byte[] subset(byte[] bArr, int i) {
        return subset(bArr, i, bArr.length - i);
    }

    public static byte[] subset(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }

    public static char[] subset(char[] cArr, int i) {
        return subset(cArr, i, cArr.length - i);
    }

    public static char[] subset(char[] cArr, int i, int i2) {
        char[] cArr2 = new char[i2];
        System.arraycopy(cArr, i, cArr2, 0, i2);
        return cArr2;
    }

    public static float[] subset(float[] fArr, int i) {
        return subset(fArr, i, fArr.length - i);
    }

    public static float[] subset(float[] fArr, int i, int i2) {
        float[] fArr2 = new float[i2];
        System.arraycopy(fArr, i, fArr2, 0, i2);
        return fArr2;
    }

    public static int[] subset(int[] iArr, int i) {
        return subset(iArr, i, iArr.length - i);
    }

    public static int[] subset(int[] iArr, int i, int i2) {
        int[] iArr2 = new int[i2];
        System.arraycopy(iArr, i, iArr2, 0, i2);
        return iArr2;
    }

    public static String[] subset(String[] strArr, int i) {
        return subset(strArr, i, strArr.length - i);
    }

    public static String[] subset(String[] strArr, int i, int i2) {
        String[] strArr2 = new String[i2];
        System.arraycopy(strArr, i, strArr2, 0, i2);
        return strArr2;
    }

    public static boolean[] subset(boolean[] zArr, int i) {
        return subset(zArr, i, zArr.length - i);
    }

    public static boolean[] subset(boolean[] zArr, int i, int i2) {
        boolean[] zArr2 = new boolean[i2];
        System.arraycopy(zArr, i, zArr2, 0, i2);
        return zArr2;
    }

    public static final float tan(float f) {
        return (float) Math.tan((double) f);
    }

    private void tellPDE(String str) {
        Log.i(getComponentName().getPackageName(), "PROCESSING " + str);
    }

    public static String trim(String str) {
        return str.replace(160, ' ').trim();
    }

    public static String[] trim(String[] strArr) {
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] != null) {
                strArr2[i] = strArr[i].replace(160, ' ').trim();
            }
        }
        return strArr2;
    }

    public static final int unbinary(String str) {
        return Integer.parseInt(str, 2);
    }

    public static final int unhex(String str) {
        return (int) Long.parseLong(str, 16);
    }

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static int year() {
        time.setToNow();
        return time.year;
    }

    public final float alpha(int i) {
        return this.f2g.alpha(i);
    }

    public void ambient(float f) {
        this.f2g.ambient(f);
    }

    public void ambient(float f, float f2, float f3) {
        this.f2g.ambient(f, f2, f3);
    }

    public void ambient(int i) {
        this.f2g.ambient(i);
    }

    public void ambientLight(float f, float f2, float f3) {
        this.f2g.ambientLight(f, f2, f3);
    }

    public void ambientLight(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.ambientLight(f, f2, f3, f4, f5, f6);
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.applyMatrix(f, f2, f3, f4, f5, f6);
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        this.f2g.applyMatrix(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
    }

    public void applyMatrix(PMatrix2D pMatrix2D) {
        this.f2g.applyMatrix(pMatrix2D);
    }

    public void applyMatrix(PMatrix3D pMatrix3D) {
        this.f2g.applyMatrix(pMatrix3D);
    }

    public void applyMatrix(PMatrix pMatrix) {
        this.f2g.applyMatrix(pMatrix);
    }

    public void arc(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.arc(f, f2, f3, f4, f5, f6);
    }

    public void arc(float f, float f2, float f3, float f4, float f5, float f6, int i) {
        this.f2g.arc(f, f2, f3, f4, f5, f6, i);
    }

    public void background(float f) {
        this.f2g.background(f);
    }

    public void background(float f, float f2) {
        this.f2g.background(f, f2);
    }

    public void background(float f, float f2, float f3) {
        this.f2g.background(f, f2, f3);
    }

    public void background(float f, float f2, float f3, float f4) {
        this.f2g.background(f, f2, f3, f4);
    }

    public void background(int i) {
        this.f2g.background(i);
    }

    public void background(int i, float f) {
        this.f2g.background(i, f);
    }

    public void background(PImage pImage) {
        this.f2g.background(pImage);
    }

    public void beginCamera() {
        this.f2g.beginCamera();
    }

    public void beginContour() {
        this.f2g.beginContour();
    }

    public PGL beginPGL() {
        return this.f2g.beginPGL();
    }

    public void beginShape() {
        this.f2g.beginShape();
    }

    public void beginShape(int i) {
        this.f2g.beginShape(i);
    }

    public void bezier(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.f2g.bezier(f, f2, f3, f4, f5, f6, f7, f8);
    }

    public void bezier(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        this.f2g.bezier(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12);
    }

    public void bezierDetail(int i) {
        this.f2g.bezierDetail(i);
    }

    public float bezierPoint(float f, float f2, float f3, float f4, float f5) {
        return this.f2g.bezierPoint(f, f2, f3, f4, f5);
    }

    public float bezierTangent(float f, float f2, float f3, float f4, float f5) {
        return this.f2g.bezierTangent(f, f2, f3, f4, f5);
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.bezierVertex(f, f2, f3, f4, f5, f6);
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.f2g.bezierVertex(f, f2, f3, f4, f5, f6, f7, f8, f9);
    }

    public void blend(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        this.f2g.blend(i, i2, i3, i4, i5, i6, i7, i8, i9);
    }

    public void blend(PImage pImage, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        this.f2g.blend(pImage, i, i2, i3, i4, i5, i6, i7, i8, i9);
    }

    public void blendMode(int i) {
        this.f2g.blendMode(i);
    }

    public final float blue(int i) {
        return this.f2g.blue(i);
    }

    public void box(float f) {
        this.f2g.box(f);
    }

    public void box(float f, float f2, float f3) {
        this.f2g.box(f, f2, f3);
    }

    public void breakShape() {
        this.f2g.breakShape();
    }

    public final float brightness(int i) {
        return this.f2g.brightness(i);
    }

    public void camera() {
        this.f2g.camera();
    }

    public void camera(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.f2g.camera(f, f2, f3, f4, f5, f6, f7, f8, f9);
    }

    public boolean canDraw() {
        return this.f2g != null && this.surfaceReady && !this.paused && (this.looping || this.redraw);
    }

    public void clear() {
        this.f2g.clear();
    }

    public void clip(float f, float f2, float f3, float f4) {
        this.f2g.clip(f, f2, f3, f4);
    }

    public final int color(float f) {
        int i = PImage.BLUE_MASK;
        if (this.f2g != null) {
            return this.f2g.color(f);
        }
        int i2 = (int) f;
        if (i2 <= 255) {
            i = i2 < 0 ? 0 : i2;
        }
        return i | -16777216 | (i << 16) | (i << 8);
    }

    public final int color(float f, float f2) {
        if (this.f2g != null) {
            return this.f2g.color(f, f2);
        }
        int i = (int) f;
        int i2 = (int) f2;
        if (i > 255) {
            i = 255;
        } else if (i < 0) {
            i = 0;
        }
        if (i2 <= 255 && i2 < 0) {
        }
        return i | -16777216 | (i << 16) | (i << 8);
    }

    public final int color(float f, float f2, float f3) {
        if (this.f2g != null) {
            return this.f2g.color(f, f2, f3);
        }
        if (f > 255.0f) {
            f = 255.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        if (f2 > 255.0f) {
            f2 = 255.0f;
        } else if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f3 > 255.0f) {
            f3 = 255.0f;
        } else if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        return -16777216 | (((int) f) << 16) | (((int) f2) << 8) | ((int) f3);
    }

    public final int color(float f, float f2, float f3, float f4) {
        if (this.f2g != null) {
            return this.f2g.color(f, f2, f3, f4);
        }
        if (f4 > 255.0f) {
            f4 = 255.0f;
        } else if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f > 255.0f) {
            f = 255.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        if (f2 > 255.0f) {
            f2 = 255.0f;
        } else if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f3 > 255.0f) {
            f3 = 255.0f;
        } else if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        return (((int) f4) << 24) | (((int) f) << 16) | (((int) f2) << 8) | ((int) f3);
    }

    public final int color(int i) {
        if (this.f2g != null) {
            return this.f2g.color(i);
        }
        if (i > 255) {
            i = 255;
        } else if (i < 0) {
            i = 0;
        }
        return -16777216 | (i << 16) | (i << 8) | i;
    }

    public final int color(int i, int i2) {
        if (this.f2g != null) {
            return this.f2g.color(i, i2);
        }
        if (i2 > 255) {
            i2 = 255;
        } else if (i2 < 0) {
            i2 = 0;
        }
        return i > 255 ? (i2 << 24) | (16777215 & i) : (i2 << 24) | (i << 16) | (i << 8) | i;
    }

    public final int color(int i, int i2, int i3) {
        if (this.f2g != null) {
            return this.f2g.color(i, i2, i3);
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
        return -16777216 | (i << 16) | (i2 << 8) | i3;
    }

    public final int color(int i, int i2, int i3, int i4) {
        if (this.f2g != null) {
            return this.f2g.color(i, i2, i3, i4);
        }
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

    public void colorMode(int i) {
        this.f2g.colorMode(i);
    }

    public void colorMode(int i, float f) {
        this.f2g.colorMode(i, f);
    }

    public void colorMode(int i, float f, float f2, float f3) {
        this.f2g.colorMode(i, f, f2, f3);
    }

    public void colorMode(int i, float f, float f2, float f3, float f4) {
        this.f2g.colorMode(i, f, f2, f3, f4);
    }

    public void copy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.f2g.copy(i, i2, i3, i4, i5, i6, i7, i8);
    }

    public void copy(PImage pImage, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.f2g.copy(pImage, i, i2, i3, i4, i5, i6, i7, i8);
    }

    /* access modifiers changed from: protected */
    public PFont createDefaultFont(float f) {
        return createFont("SansSerif", f, true, null);
    }

    public PFont createFont(String str, float f) {
        return createFont(str, f, true, null);
    }

    public PFont createFont(String str, float f, boolean z) {
        return createFont(str, f, z, null);
    }

    public PFont createFont(String str, float f, boolean z, char[] cArr) {
        String lowerCase = str.toLowerCase();
        return new PFont((lowerCase.endsWith(".otf") || lowerCase.endsWith(".ttf")) ? Typeface.createFromAsset(getBaseContext().getAssets(), str) : (Typeface) PFont.findNative(str), round(f), z, cArr);
    }

    public PGraphics createGraphics(int i, int i2) {
        return createGraphics(i, i2, PConstants.JAVA2D);
    }

    public PGraphics createGraphics(int i, int i2, String str) {
        PGraphics pGraphics = null;
        if (str.equals(PConstants.JAVA2D)) {
            pGraphics = new PGraphicsAndroid2D();
        } else if (str.equals(PConstants.P2D)) {
            if (!this.f2g.isGL()) {
                throw new RuntimeException("createGraphics() with P2D requires size() to use P2D or P3D");
            }
            pGraphics = new PGraphics2D();
        } else if (!str.equals("processing.opengl.PGraphics3D")) {
            try {
                Class loadClass = getClass().getClassLoader().loadClass(str);
                if (loadClass != null) {
                    try {
                        Constructor constructor = loadClass.getConstructor(new Class[0]);
                        if (constructor != null) {
                            try {
                                pGraphics = (PGraphics) constructor.newInstance(new Object[0]);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e.getMessage());
                            } catch (IllegalAccessException e2) {
                                e2.printStackTrace();
                                throw new RuntimeException(e2.getMessage());
                            } catch (InstantiationException e3) {
                                e3.printStackTrace();
                                throw new RuntimeException(e3.getMessage());
                            }
                        }
                    } catch (NoSuchMethodException e4) {
                        throw new RuntimeException("Missing renderer constructor");
                    }
                }
            } catch (ClassNotFoundException e5) {
                throw new RuntimeException("Missing renderer class");
            }
        } else if (!this.f2g.isGL()) {
            throw new RuntimeException("createGraphics() with P3D or OPENGL requires size() to use P2D or P3D");
        } else {
            pGraphics = new PGraphics3D();
        }
        pGraphics.setParent(this);
        pGraphics.setPrimary(false);
        pGraphics.setSize(i, i2);
        return pGraphics;
    }

    public PImage createImage(int i, int i2, int i3) {
        PImage pImage = new PImage(i, i2, i3);
        pImage.parent = this;
        return pImage;
    }

    public InputStream createInput(String str) {
        InputStream createInputRaw = createInputRaw(str);
        if (createInputRaw == null || !str.toLowerCase().endsWith(".gz")) {
            return createInputRaw;
        }
        try {
            return new GZIPInputStream(createInputRaw);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream createInputRaw(String str) {
        InputStream inputStream = null;
        if (str == null || str.length() == 0) {
            return inputStream;
        }
        if (str.indexOf(":") != -1) {
            try {
                return new DefaultHttpClient().execute(new HttpGet(URI.create(str))).getEntity().getContent();
            } catch (FileNotFoundException | MalformedURLException e) {
            } catch (IOException e2) {
                e2.printStackTrace();
                return inputStream;
            }
        }
        try {
            InputStream open = getAssets().open(str);
            if (open != null) {
                return open;
            }
        } catch (IOException e3) {
        }
        File file = new File(str);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                if (fileInputStream != null) {
                    return fileInputStream;
                }
            } catch (FileNotFoundException e4) {
            }
        }
        File file2 = new File(sketchPath(str));
        if (file2.exists()) {
            try {
                FileInputStream fileInputStream2 = new FileInputStream(file2);
                if (fileInputStream2 != null) {
                    return fileInputStream2;
                }
            } catch (FileNotFoundException e5) {
            }
        }
        try {
            FileInputStream openFileInput = getApplicationContext().openFileInput(str);
            return openFileInput != null ? openFileInput : inputStream;
        } catch (FileNotFoundException e6) {
            return inputStream;
        }
    }

    public OutputStream createOutput(String str) {
        try {
            File file = new File(str);
            if (!file.isAbsolute()) {
                file = new File(sketchPath(str));
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            return file.getName().toLowerCase().endsWith(".gz") ? new GZIPOutputStream(fileOutputStream) : fileOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BufferedReader createReader(String str) {
        try {
            InputStream createInput = createInput(str);
            if (createInput != null) {
                return createReader(createInput);
            }
            System.err.println(str + " does not exist or could not be read");
            return null;
        } catch (Exception e) {
            if (str == null) {
                System.err.println("Filename passed to reader() was null");
                return null;
            }
            System.err.println("Couldn't create a reader for " + str);
            return null;
        }
    }

    public PShape createShape() {
        return this.f2g.createShape();
    }

    public PShape createShape(int i) {
        return this.f2g.createShape(i);
    }

    public PShape createShape(int i, float... fArr) {
        return this.f2g.createShape(i, fArr);
    }

    public PShape createShape(PShape pShape) {
        return this.f2g.createShape(pShape);
    }

    public Table createTable() {
        return new Table();
    }

    public PrintWriter createWriter(String str) {
        return createWriter(saveFile(str));
    }

    public XML createXML(String str) {
        try {
            return new XML(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void curve(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.f2g.curve(f, f2, f3, f4, f5, f6, f7, f8);
    }

    public void curve(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        this.f2g.curve(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12);
    }

    public void curveDetail(int i) {
        this.f2g.curveDetail(i);
    }

    public float curvePoint(float f, float f2, float f3, float f4, float f5) {
        return this.f2g.curvePoint(f, f2, f3, f4, f5);
    }

    public float curveTangent(float f, float f2, float f3, float f4, float f5) {
        return this.f2g.curveTangent(f, f2, f3, f4, f5);
    }

    public void curveTightness(float f) {
        this.f2g.curveTightness(f);
    }

    public void curveVertex(float f, float f2) {
        this.f2g.curveVertex(f, f2);
    }

    public void curveVertex(float f, float f2, float f3) {
        this.f2g.curveVertex(f, f2, f3);
    }

    public File dataFile(String str) {
        return new File(dataPath(str));
    }

    public String dataPath(String str) {
        return new File(str).isAbsolute() ? str : this.sketchPath + File.separator + "data" + File.separator + str;
    }

    public void delay(int i) {
        try {
            Thread.sleep((long) i);
        } catch (InterruptedException e) {
        }
    }

    /* access modifiers changed from: protected */
    public void dequeueEvents() {
        while (this.eventQueue.available()) {
            Event remove = this.eventQueue.remove();
            switch (remove.getFlavor()) {
                case 1:
                    handleKeyEvent((processing.event.KeyEvent) remove);
                    break;
                case 2:
                    handleMouseEvent((MouseEvent) remove);
                    break;
            }
        }
    }

    public void destroy() {
        exit();
    }

    public void die(String str) {
        stop();
        throw new RuntimeException(str);
    }

    public void die(String str, Exception exc) {
        if (exc != null) {
            exc.printStackTrace();
        }
        die(str);
    }

    public void directionalLight(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.directionalLight(f, f2, f3, f4, f5, f6);
    }

    public boolean displayable() {
        return this.f2g.displayable();
    }

    public final void dispose() {
        this.finished = true;
        if (this.thread != null) {
            this.thread = null;
            if (this.f2g != null) {
                this.f2g.dispose();
            }
            handleMethods("dispose");
        }
    }

    public void draw() {
        this.finished = true;
    }

    public void edge(boolean z) {
        this.f2g.edge(z);
    }

    public void ellipse(float f, float f2, float f3, float f4) {
        this.f2g.ellipse(f, f2, f3, f4);
    }

    public void ellipseMode(int i) {
        this.f2g.ellipseMode(i);
    }

    public void emissive(float f) {
        this.f2g.emissive(f);
    }

    public void emissive(float f, float f2, float f3) {
        this.f2g.emissive(f, f2, f3);
    }

    public void emissive(int i) {
        this.f2g.emissive(i);
    }

    public void endCamera() {
        this.f2g.endCamera();
    }

    public void endContour() {
        this.f2g.endContour();
    }

    public void endPGL() {
        this.f2g.endPGL();
    }

    public void endShape() {
        this.f2g.endShape();
    }

    public void endShape(int i) {
        this.f2g.endShape(i);
    }

    public void exit() {
        if (this.thread == null) {
            exit2();
        } else if (this.looping) {
            this.finished = true;
            this.exitCalled = true;
        } else if (!this.looping) {
            dispose();
            exit2();
        }
    }

    /* access modifiers changed from: 0000 */
    public void exit2() {
        try {
            System.exit(0);
        } catch (SecurityException e) {
        }
    }

    public void fill(float f) {
        this.f2g.fill(f);
    }

    public void fill(float f, float f2) {
        this.f2g.fill(f, f2);
    }

    public void fill(float f, float f2, float f3) {
        this.f2g.fill(f, f2, f3);
    }

    public void fill(float f, float f2, float f3, float f4) {
        this.f2g.fill(f, f2, f3, f4);
    }

    public void fill(int i) {
        this.f2g.fill(i);
    }

    public void fill(int i, float f) {
        this.f2g.fill(i, f);
    }

    public void filter(int i) {
        this.f2g.filter(i);
    }

    public void filter(int i, float f) {
        this.f2g.filter(i, f);
    }

    public void filter(PShader pShader) {
        this.f2g.filter(pShader);
    }

    public void flush() {
        this.f2g.flush();
    }

    public void focusGained() {
    }

    public void focusLost() {
    }

    public void frameRate(float f) {
        this.frameRateTarget = f;
        this.frameRatePeriod = (long) (1.0E9d / ((double) this.frameRateTarget));
        this.f2g.setFrameRate(f);
    }

    public void frustum(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.frustum(f, f2, f3, f4, f5, f6);
    }

    public int get(int i, int i2) {
        return this.f2g.get(i, i2);
    }

    public PImage get() {
        return this.f2g.get();
    }

    public PImage get(int i, int i2, int i3, int i4) {
        return this.f2g.get(i, i2, i3, i4);
    }

    public Object getCache(PImage pImage) {
        return this.f2g.getCache(pImage);
    }

    public PMatrix2D getMatrix(PMatrix2D pMatrix2D) {
        return this.f2g.getMatrix(pMatrix2D);
    }

    public PMatrix3D getMatrix(PMatrix3D pMatrix3D) {
        return this.f2g.getMatrix(pMatrix3D);
    }

    public PMatrix getMatrix() {
        return this.f2g.getMatrix();
    }

    public Object getNative() {
        return this.f2g.getNative();
    }

    public PShader getShader(int i) {
        return this.f2g.getShader(i);
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.surfaceView.getHolder();
    }

    public SurfaceView getSurfaceView() {
        return this.surfaceView;
    }

    public final float green(int i) {
        return this.f2g.green(i);
    }

    public void handleDraw() {
        if (this.surfaceChanged) {
            int width2 = this.surfaceView.getWidth();
            int height2 = this.surfaceView.getHeight();
            if (!(width2 == this.width && height2 == this.height)) {
                this.width = width2;
                this.height = height2;
                this.f2g.setSize(this.width, this.height);
            }
            this.surfaceChanged = false;
            this.surfaceReady = true;
        }
        if (canDraw()) {
            this.f2g.beginDraw();
            long nanoTime = System.nanoTime();
            if (this.frameCount == 0) {
                try {
                    setup();
                } catch (RendererChangeException e) {
                    return;
                }
            } else {
                this.frameRate = ((((float) (1000000.0d / (((double) (nanoTime - this.frameRateLastNanos)) / 1000000.0d))) / 1000.0f) * 0.1f) + (this.frameRate * 0.9f);
                if (this.frameCount != 0) {
                    handleMethods("pre");
                }
                this.pmouseX = this.dmouseX;
                this.pmouseY = this.dmouseY;
                draw();
                this.dmouseX = this.mouseX;
                this.dmouseY = this.mouseY;
                dequeueEvents();
                handleMethods("draw");
                this.redraw = false;
            }
            this.f2g.endDraw();
            if (this.frameCount != 0) {
                handleMethods("post");
            }
            this.frameRateLastNanos = nanoTime;
            this.frameCount++;
        }
    }

    /* access modifiers changed from: protected */
    public void handleKeyEvent(processing.event.KeyEvent keyEvent) {
        this.key = keyEvent.getKey();
        this.keyCode = keyEvent.getKeyCode();
        switch (keyEvent.getAction()) {
            case 1:
                this.keyPressed = true;
                keyPressed(keyEvent);
                break;
            case 2:
                this.keyPressed = false;
                keyReleased(keyEvent);
                break;
        }
        handleMethods("keyEvent", new Object[]{keyEvent});
        if (keyEvent.getAction() == 1 && keyEvent.getKeyCode() == 4) {
            exit();
        }
    }

    /* access modifiers changed from: protected */
    public void handleMethods(String str) {
        RegisteredMethods registeredMethods = (RegisteredMethods) this.registerMap.get(str);
        if (registeredMethods != null) {
            registeredMethods.handle();
        }
    }

    /* access modifiers changed from: protected */
    public void handleMethods(String str, Object[] objArr) {
        RegisteredMethods registeredMethods = (RegisteredMethods) this.registerMap.get(str);
        if (registeredMethods != null) {
            registeredMethods.handle(objArr);
        }
    }

    /* access modifiers changed from: protected */
    public void handleMouseEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getAction() == 4 || mouseEvent.getAction() == 5) {
            this.pmouseX = this.emouseX;
            this.pmouseY = this.emouseY;
            this.mouseX = mouseEvent.getX();
            this.mouseY = mouseEvent.getY();
        }
        if (mouseEvent.getAction() == 1) {
            this.mouseX = mouseEvent.getX();
            this.mouseY = mouseEvent.getY();
            this.pmouseX = this.mouseX;
            this.pmouseY = this.mouseY;
            this.dmouseX = this.mouseX;
            this.dmouseY = this.mouseY;
        }
        switch (mouseEvent.getAction()) {
            case 1:
                this.mousePressed = true;
                break;
            case 2:
                this.mousePressed = false;
                break;
        }
        handleMethods("mouseEvent", new Object[]{mouseEvent});
        switch (mouseEvent.getAction()) {
            case 1:
                mousePressed(mouseEvent);
                break;
            case 2:
                mouseReleased(mouseEvent);
                break;
            case 3:
                mouseClicked(mouseEvent);
                break;
            case 4:
                mouseDragged(mouseEvent);
                break;
            case 5:
                mouseMoved(mouseEvent);
                break;
            case 6:
                mouseEntered(mouseEvent);
                break;
            case 7:
                mouseExited(mouseEvent);
                break;
        }
        if (mouseEvent.getAction() == 4 || mouseEvent.getAction() == 5) {
            this.emouseX = this.mouseX;
            this.emouseY = this.mouseY;
        }
        if (mouseEvent.getAction() == 1) {
            this.emouseX = this.mouseX;
            this.emouseY = this.mouseY;
        }
    }

    public void hint(int i) {
        this.f2g.hint(i);
    }

    public final float hue(int i) {
        return this.f2g.hue(i);
    }

    public void image(PImage pImage, float f, float f2) {
        this.f2g.image(pImage, f, f2);
    }

    public void image(PImage pImage, float f, float f2, float f3, float f4) {
        this.f2g.image(pImage, f, f2, f3, f4);
    }

    public void image(PImage pImage, float f, float f2, float f3, float f4, int i, int i2, int i3, int i4) {
        this.f2g.image(pImage, f, f2, f3, f4, i, i2, i3, i4);
    }

    public void imageMode(int i) {
        this.f2g.imageMode(i);
    }

    /* access modifiers changed from: protected */
    public String insertFrame(String str) {
        int indexOf = str.indexOf(35);
        int lastIndexOf = str.lastIndexOf(35);
        if (indexOf == -1 || lastIndexOf - indexOf <= 0) {
            return str;
        }
        String substring = str.substring(0, indexOf);
        int i = (lastIndexOf - indexOf) + 1;
        return substring + m1nf(this.frameCount, i) + str.substring(lastIndexOf + 1);
    }

    public boolean isGL() {
        return this.f2g.isGL();
    }

    public void keyPressed() {
    }

    public void keyPressed(processing.event.KeyEvent keyEvent) {
        keyPressed();
    }

    public void keyReleased() {
    }

    public void keyReleased(processing.event.KeyEvent keyEvent) {
        keyReleased();
    }

    public void keyTyped() {
    }

    public void keyTyped(processing.event.KeyEvent keyEvent) {
        keyTyped();
    }

    public int lerpColor(int i, int i2, float f) {
        return this.f2g.lerpColor(i, i2, f);
    }

    public void lightFalloff(float f, float f2, float f3) {
        this.f2g.lightFalloff(f, f2, f3);
    }

    public void lightSpecular(float f, float f2, float f3) {
        this.f2g.lightSpecular(f, f2, f3);
    }

    public void lights() {
        this.f2g.lights();
    }

    public void line(float f, float f2, float f3, float f4) {
        this.f2g.line(f, f2, f3, f4);
    }

    public void line(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.line(f, f2, f3, f4, f5, f6);
    }

    public void link(String str) {
        link(str, null);
    }

    public void link(String str, String str2) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }

    public byte[] loadBytes(String str) {
        InputStream createInput = createInput(str);
        if (createInput != null) {
            return loadBytes(createInput);
        }
        System.err.println("The file \"" + str + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
        return null;
    }

    public PFont loadFont(String str) {
        try {
            return new PFont(createInput(str));
        } catch (Exception e) {
            die("Could not load font " + str + ". " + "Make sure that the font has been copied " + "to the data folder of your sketch.", e);
            return null;
        }
    }

    public PImage loadImage(String str) {
        InputStream createInput = createInput(str);
        if (createInput == null) {
            System.err.println("Could not find the image " + str + ".");
            return null;
        }
        try {
            PImage pImage = new PImage(BitmapFactory.decodeStream(createInput));
            pImage.parent = this;
            return pImage;
        } finally {
            try {
                createInput.close();
            } catch (IOException e) {
            }
        }
    }

    public void loadPixels() {
        this.f2g.loadPixels();
        this.pixels = this.f2g.pixels;
    }

    public PShader loadShader(String str) {
        return this.f2g.loadShader(str);
    }

    public PShader loadShader(String str, String str2) {
        return this.f2g.loadShader(str, str2);
    }

    public PShape loadShape(String str) {
        return this.f2g.loadShape(str);
    }

    public String[] loadStrings(String str) {
        InputStream createInput = createInput(str);
        if (createInput != null) {
            return loadStrings(createInput);
        }
        System.err.println("The file \"" + str + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
        return null;
    }

    public Table loadTable(String str) {
        return loadTable(str, null);
    }

    public Table loadTable(String str, String str2) {
        try {
            String checkExtension = checkExtension(str);
            if (checkExtension != null && (checkExtension.equals("csv") || checkExtension.equals("tsv"))) {
                str2 = str2 == null ? checkExtension : checkExtension + "," + str2;
            }
            return new Table(createInput(str), str2);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public XML loadXML(String str) {
        return loadXML(str, null);
    }

    public XML loadXML(String str, String str2) {
        try {
            return new XML(createInput(str), str2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void loop() {
        if (!this.looping) {
            this.looping = true;
        }
    }

    public void mask(PImage pImage) {
        this.f2g.mask(pImage);
    }

    public void mask(int[] iArr) {
        this.f2g.mask(iArr);
    }

    public void method(String str) {
        try {
            getClass().getMethod(str, new Class[0]).invoke(this, new Object[0]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.getTargetException().printStackTrace();
        } catch (NoSuchMethodException e4) {
            System.err.println("There is no public " + str + "() method " + "in the class " + getClass().getName());
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }

    public int millis() {
        return (int) (System.currentTimeMillis() - this.millisOffset);
    }

    public float modelX(float f, float f2, float f3) {
        return this.f2g.modelX(f, f2, f3);
    }

    public float modelY(float f, float f2, float f3) {
        return this.f2g.modelY(f, f2, f3);
    }

    public float modelZ(float f, float f2, float f3) {
        return this.f2g.modelZ(f, f2, f3);
    }

    public void mouseClicked() {
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mouseClicked();
    }

    public void mouseDragged() {
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        mouseDragged();
    }

    public void mouseEntered() {
    }

    public void mouseEntered(MouseEvent mouseEvent) {
        mouseEntered();
    }

    public void mouseExited() {
    }

    public void mouseExited(MouseEvent mouseEvent) {
        mouseExited();
    }

    public void mouseMoved() {
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        mouseMoved();
    }

    public void mousePressed() {
    }

    public void mousePressed(MouseEvent mouseEvent) {
        mousePressed();
    }

    public void mouseReleased() {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        mouseReleased();
    }

    /* access modifiers changed from: protected */
    public void nativeKeyEvent(KeyEvent keyEvent) {
        int i = 1;
        char unicodeChar = (char) keyEvent.getUnicodeChar();
        if (unicodeChar == 0 || unicodeChar == 65535) {
            unicodeChar = 65535;
        }
        int keyCode2 = keyEvent.getKeyCode();
        int action = keyEvent.getAction();
        if (action != 0) {
            i = action == 1 ? 2 : 0;
        }
        processing.event.KeyEvent keyEvent2 = new processing.event.KeyEvent(keyEvent, keyEvent.getEventTime(), i, 0, unicodeChar, keyCode2);
        if (action == 0 && keyCode2 == 4) {
            exit();
        }
        postEvent(keyEvent2);
    }

    /* access modifiers changed from: protected */
    public void nativeMotionEvent(MotionEvent motionEvent) {
        int metaState = motionEvent.getMetaState();
        int i = 0;
        if ((metaState & 1) != 0) {
            i = 1;
        }
        if ((metaState & 4096) != 0) {
            i |= 2;
        }
        if ((META_META_ON & metaState) != 0) {
            i |= 4;
        }
        if ((metaState & 2) != 0) {
            i |= 8;
        }
        switch (motionEvent.getAction()) {
            case 0:
                this.motionPointerId = motionEvent.getPointerId(0);
                postEvent(new MouseEvent(motionEvent, motionEvent.getEventTime(), 1, i, (int) motionEvent.getX(), (int) motionEvent.getY(), 21, 1));
                return;
            case 1:
                int findPointerIndex = motionEvent.findPointerIndex(this.motionPointerId);
                if (findPointerIndex != -1) {
                    postEvent(new MouseEvent(motionEvent, motionEvent.getEventTime(), 2, i, (int) motionEvent.getX(findPointerIndex), (int) motionEvent.getY(findPointerIndex), 21, 1));
                    return;
                }
                return;
            case 2:
                int findPointerIndex2 = motionEvent.findPointerIndex(this.motionPointerId);
                if (findPointerIndex2 != -1) {
                    postEvent(new MouseEvent(motionEvent, motionEvent.getEventTime(), 4, i, (int) motionEvent.getX(findPointerIndex2), (int) motionEvent.getY(findPointerIndex2), 21, 1));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void noClip() {
        this.f2g.noClip();
    }

    public void noFill() {
        this.f2g.noFill();
    }

    public void noLights() {
        this.f2g.noLights();
    }

    public synchronized void noLoop() {
        if (this.looping) {
            this.looping = false;
        }
    }

    public void noSmooth() {
        this.f2g.noSmooth();
    }

    public void noStroke() {
        this.f2g.noStroke();
    }

    public void noTexture() {
        this.f2g.noTexture();
    }

    public void noTint() {
        this.f2g.noTint();
    }

    public float noise(float f) {
        return noise(f, 0.0f, 0.0f);
    }

    public float noise(float f, float f2) {
        return noise(f, f2, 0.0f);
    }

    public float noise(float f, float f2, float f3) {
        if (this.perlin == null) {
            if (this.perlinRandom == null) {
                this.perlinRandom = new Random();
            }
            this.perlin = new float[4096];
            for (int i = 0; i < 4096; i++) {
                this.perlin[i] = this.perlinRandom.nextFloat();
            }
            this.perlin_cosTable = PGraphics.cosLUT;
            this.perlin_PI = 720;
            this.perlin_TWOPI = 720;
            this.perlin_PI >>= 1;
        }
        if (f < 0.0f) {
            f = -f;
        }
        if (f2 < 0.0f) {
            f2 = -f2;
        }
        if (f3 < 0.0f) {
            f3 = -f3;
        }
        int i2 = (int) f;
        int i3 = (int) f2;
        int i4 = (int) f3;
        float f4 = f - ((float) i2);
        float f5 = f2 - ((float) i3);
        float f6 = f3 - ((float) i4);
        float f7 = 0.0f;
        float f8 = 0.5f;
        for (int i5 = 0; i5 < this.perlin_octaves; i5++) {
            int i6 = (i3 << 4) + i2 + (i4 << 8);
            float noise_fsc = noise_fsc(f4);
            float noise_fsc2 = noise_fsc(f5);
            float f9 = this.perlin[i6 & PERLIN_SIZE];
            float f10 = f9 + ((this.perlin[(i6 + 1) & PERLIN_SIZE] - f9) * noise_fsc);
            float f11 = this.perlin[(i6 + 16) & PERLIN_SIZE];
            float f12 = f10 + (((f11 + ((this.perlin[((i6 + 16) + 1) & PERLIN_SIZE] - f11) * noise_fsc)) - f10) * noise_fsc2);
            int i7 = i6 + 256;
            float f13 = this.perlin[i7 & PERLIN_SIZE];
            float f14 = f13 + ((this.perlin[(i7 + 1) & PERLIN_SIZE] - f13) * noise_fsc);
            float f15 = this.perlin[(i7 + 16) & PERLIN_SIZE];
            f7 += (((((((((this.perlin[((i7 + 16) + 1) & PERLIN_SIZE] - f15) * noise_fsc) + f15) - f14) * noise_fsc2) + f14) - f12) * noise_fsc(f6)) + f12) * f8;
            f8 *= this.perlin_amp_falloff;
            i2 <<= 1;
            f4 *= 2.0f;
            i3 <<= 1;
            f5 *= 2.0f;
            i4 <<= 1;
            f6 *= 2.0f;
            if (f4 >= 1.0f) {
                i2++;
                f4 -= 1.0f;
            }
            if (f5 >= 1.0f) {
                i3++;
                f5 -= 1.0f;
            }
            if (f6 >= 1.0f) {
                i4++;
                f6 -= 1.0f;
            }
        }
        return f7;
    }

    public void noiseDetail(int i) {
        if (i > 0) {
            this.perlin_octaves = i;
        }
    }

    public void noiseDetail(int i, float f) {
        if (i > 0) {
            this.perlin_octaves = i;
        }
        if (f > 0.0f) {
            this.perlin_amp_falloff = f;
        }
    }

    public void noiseSeed(long j) {
        if (this.perlinRandom == null) {
            this.perlinRandom = new Random();
        }
        this.perlinRandom.setSeed(j);
        this.perlin = null;
    }

    public void normal(float f, float f2, float f3) {
        this.f2g.normal(f, f2, f3);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        requestWindowFeature(1);
        window.setFlags(256, 256);
        window.setFlags(1024, 1024);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.displayWidth = displayMetrics.widthPixels;
        this.displayHeight = displayMetrics.heightPixels;
        int sketchWidth = sketchWidth();
        int sketchHeight = sketchHeight();
        if (sketchRenderer().equals(PConstants.JAVA2D)) {
            this.surfaceView = new SketchSurfaceView(this, sketchWidth, sketchHeight);
        } else if (sketchRenderer().equals(PConstants.P2D) || sketchRenderer().equals("processing.opengl.PGraphics3D")) {
            this.surfaceView = new SketchSurfaceViewGL(this, sketchWidth, sketchHeight, sketchRenderer().equals("processing.opengl.PGraphics3D"));
        }
        if (sketchWidth == this.displayWidth && sketchHeight == this.displayHeight) {
            window.setContentView(this.surfaceView);
        } else {
            RelativeLayout relativeLayout = new RelativeLayout(this);
            LayoutParams layoutParams = new LayoutParams(-2, -2);
            layoutParams.addRule(13);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.addView(this.surfaceView, sketchWidth(), sketchHeight());
            relativeLayout.addView(linearLayout, layoutParams);
            window.setContentView(relativeLayout);
        }
        this.finished = false;
        this.looping = true;
        this.redraw = true;
        this.sketchPath = getApplicationContext().getFilesDir().getAbsolutePath();
        this.handler = new Handler();
        start();
    }

    public void onDestroy() {
        dispose();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.paused = true;
        handleMethods("pause");
        pause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.paused = false;
        handleMethods("resume");
        resume();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        tellPDE("onStart");
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        tellPDE("onStop");
        super.onStop();
    }

    public void orientation(int i) {
        if (i == 1) {
            setRequestedOrientation(1);
        } else if (i == 2) {
            setRequestedOrientation(0);
        }
    }

    public void ortho() {
        this.f2g.ortho();
    }

    public void ortho(float f, float f2, float f3, float f4) {
        this.f2g.ortho(f, f2, f3, f4);
    }

    public void ortho(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.ortho(f, f2, f3, f4, f5, f6);
    }

    public XML parseXML(String str) {
        return parseXML(str, null);
    }

    public XML parseXML(String str, String str2) {
        try {
            return XML.parse(str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void pause() {
    }

    public void perspective() {
        this.f2g.perspective();
    }

    public void perspective(float f, float f2, float f3, float f4) {
        this.f2g.perspective(f, f2, f3, f4);
    }

    public void point(float f, float f2) {
        this.f2g.point(f, f2);
    }

    public void point(float f, float f2, float f3) {
        this.f2g.point(f, f2, f3);
    }

    public void pointLight(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.pointLight(f, f2, f3, f4, f5, f6);
    }

    public void popMatrix() {
        this.f2g.popMatrix();
    }

    public void popStyle() {
        this.f2g.popStyle();
    }

    public void postEvent(Event event) {
        this.eventQueue.add(event);
        if (!this.looping) {
            dequeueEvents();
        }
    }

    public void printCamera() {
        this.f2g.printCamera();
    }

    public void printMatrix() {
        this.f2g.printMatrix();
    }

    public void printProjection() {
        this.f2g.printProjection();
    }

    public void pushMatrix() {
        this.f2g.pushMatrix();
    }

    public void pushStyle() {
        this.f2g.pushStyle();
    }

    public void quad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.f2g.quad(f, f2, f3, f4, f5, f6, f7, f8);
    }

    public void quadraticVertex(float f, float f2, float f3, float f4) {
        this.f2g.quadraticVertex(f, f2, f3, f4);
    }

    public void quadraticVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.quadraticVertex(f, f2, f3, f4, f5, f6);
    }

    public final float random(float f) {
        float f2 = 0.0f;
        if (f != 0.0f) {
            if (this.internalRandom == null) {
                this.internalRandom = new Random();
            }
            do {
                f2 = this.internalRandom.nextFloat() * f;
            } while (f2 == f);
        }
        return f2;
    }

    public final float random(float f, float f2) {
        return f >= f2 ? f : f + random(f2 - f);
    }

    public final void randomSeed(long j) {
        if (this.internalRandom == null) {
            this.internalRandom = new Random();
        }
        this.internalRandom.setSeed(j);
    }

    public void rect(float f, float f2, float f3, float f4) {
        this.f2g.rect(f, f2, f3, f4);
    }

    public void rect(float f, float f2, float f3, float f4, float f5) {
        this.f2g.rect(f, f2, f3, f4, f5);
    }

    public void rect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.f2g.rect(f, f2, f3, f4, f5, f6, f7, f8);
    }

    public void rectMode(int i) {
        this.f2g.rectMode(i);
    }

    public final float red(int i) {
        return this.f2g.red(i);
    }

    public synchronized void redraw() {
        if (!this.looping) {
            this.redraw = true;
        }
    }

    @Deprecated
    public void registerDispose(Object obj) {
        registerNoArgs("dispose", obj);
    }

    @Deprecated
    public void registerDraw(Object obj) {
        registerNoArgs("draw", obj);
    }

    public void registerMethod(String str, Object obj) {
        if (str.equals("mouseEvent")) {
            registerWithArgs("mouseEvent", obj, new Class[]{MouseEvent.class});
        } else if (str.equals("keyEvent")) {
            registerWithArgs("keyEvent", obj, new Class[]{processing.event.KeyEvent.class});
        } else if (str.equals("touchEvent")) {
            registerWithArgs("touchEvent", obj, new Class[]{TouchEvent.class});
        } else {
            registerNoArgs(str, obj);
        }
    }

    @Deprecated
    public void registerPost(Object obj) {
        registerNoArgs("post", obj);
    }

    @Deprecated
    public void registerPre(Object obj) {
        registerNoArgs("pre", obj);
    }

    @Deprecated
    public void registerSize(Object obj) {
        System.err.println("The registerSize() command is no longer supported.");
    }

    public void removeCache(PImage pImage) {
        this.f2g.removeCache(pImage);
    }

    public PImage requestImage(String str) {
        PImage createImage = createImage(0, 0, 2);
        new AsyncImageLoader(str, createImage).start();
        return createImage;
    }

    public void resetMatrix() {
        this.f2g.resetMatrix();
    }

    public void resetShader() {
        this.f2g.resetShader();
    }

    public void resetShader(int i) {
        this.f2g.resetShader(i);
    }

    public void resume() {
    }

    public void rotate(float f) {
        this.f2g.rotate(f);
    }

    public void rotate(float f, float f2, float f3, float f4) {
        this.f2g.rotate(f, f2, f3, f4);
    }

    public void rotateX(float f) {
        this.f2g.rotateX(f);
    }

    public void rotateY(float f) {
        this.f2g.rotateY(f);
    }

    public void rotateZ(float f) {
        this.f2g.rotateZ(f);
    }

    public void run() {
        long nanoTime = System.nanoTime();
        long j = 0;
        while (Thread.currentThread() == this.thread && !this.finished) {
            while (this.paused) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            if (this.f2g != null) {
                this.f2g.requestDraw();
            }
            long nanoTime2 = System.nanoTime();
            long j2 = (this.frameRatePeriod - (nanoTime2 - nanoTime)) - j;
            if (j2 > 0) {
                try {
                    Thread.sleep(j2 / 1000000, (int) (j2 % 1000000));
                } catch (InterruptedException e2) {
                }
                j = (System.nanoTime() - nanoTime2) - j2;
            } else {
                j = 0;
            }
            nanoTime = System.nanoTime();
        }
        if (!this.paused) {
            stop();
            if (this.exitCalled) {
                exit2();
            }
        }
    }

    public final float saturation(int i) {
        return this.f2g.saturation(i);
    }

    public void save(String str) {
        this.f2g.save(savePath(str));
    }

    public void saveBytes(String str, byte[] bArr) {
        saveBytes(saveFile(str), bArr);
    }

    public File saveFile(String str) {
        return new File(savePath(str));
    }

    public void saveFrame() {
        try {
            this.f2g.save(savePath("screen-" + m1nf(this.frameCount, 4) + ".tif"));
        } catch (SecurityException e) {
            System.err.println("Can't use saveFrame() when running in a browser, unless using a signed applet.");
        }
    }

    public void saveFrame(String str) {
        try {
            this.f2g.save(savePath(insertFrame(str)));
        } catch (SecurityException e) {
            System.err.println("Can't use saveFrame() when running in a browser, unless using a signed applet.");
        }
    }

    public String savePath(String str) {
        if (str == null) {
            return null;
        }
        String sketchPath2 = sketchPath(str);
        createPath(sketchPath2);
        return sketchPath2;
    }

    public boolean saveStream(File file, String str) {
        return saveStream(file, createInputRaw(str));
    }

    public boolean saveStream(String str, InputStream inputStream) {
        return saveStream(saveFile(str), inputStream);
    }

    public boolean saveStream(String str, String str2) {
        return saveStream(saveFile(str), str2);
    }

    public void saveStrings(String str, String[] strArr) {
        saveStrings(saveFile(str), strArr);
    }

    public boolean saveTable(Table table, String str) {
        return saveTable(table, str, null);
    }

    public boolean saveTable(Table table, String str, String str2) {
        try {
            table.save(saveFile(str), str2);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveXML(XML xml, String str) {
        return saveXML(xml, str, null);
    }

    public boolean saveXML(XML xml, String str, String str2) {
        return xml.save(saveFile(str), str2);
    }

    public void scale(float f) {
        this.f2g.scale(f);
    }

    public void scale(float f, float f2) {
        this.f2g.scale(f, f2);
    }

    public void scale(float f, float f2, float f3) {
        this.f2g.scale(f, f2, f3);
    }

    public float screenX(float f, float f2) {
        return this.f2g.screenX(f, f2);
    }

    public float screenX(float f, float f2, float f3) {
        return this.f2g.screenX(f, f2, f3);
    }

    public float screenY(float f, float f2) {
        return this.f2g.screenY(f, f2);
    }

    public float screenY(float f, float f2, float f3) {
        return this.f2g.screenY(f, f2, f3);
    }

    public float screenZ(float f, float f2, float f3) {
        return this.f2g.screenZ(f, f2, f3);
    }

    public void set(int i, int i2, int i3) {
        this.f2g.set(i, i2, i3);
    }

    public void set(int i, int i2, PImage pImage) {
        this.f2g.set(i, i2, pImage);
    }

    public void setCache(PImage pImage, Object obj) {
        this.f2g.setCache(pImage, obj);
    }

    public void setMatrix(PMatrix2D pMatrix2D) {
        this.f2g.setMatrix(pMatrix2D);
    }

    public void setMatrix(PMatrix3D pMatrix3D) {
        this.f2g.setMatrix(pMatrix3D);
    }

    public void setMatrix(PMatrix pMatrix) {
        this.f2g.setMatrix(pMatrix);
    }

    public void setup() {
    }

    public void shader(PShader pShader) {
        this.f2g.shader(pShader);
    }

    public void shader(PShader pShader, int i) {
        this.f2g.shader(pShader, i);
    }

    public void shape(PShape pShape) {
        this.f2g.shape(pShape);
    }

    public void shape(PShape pShape, float f, float f2) {
        this.f2g.shape(pShape, f, f2);
    }

    public void shape(PShape pShape, float f, float f2, float f3, float f4) {
        this.f2g.shape(pShape, f, f2, f3, f4);
    }

    public void shapeMode(int i) {
        this.f2g.shapeMode(i);
    }

    public void shearX(float f) {
        this.f2g.shearX(f);
    }

    public void shearY(float f) {
        this.f2g.shearY(f);
    }

    public void shininess(float f) {
        this.f2g.shininess(f);
    }

    public void size(int i, int i2) {
        size(i, i2, PConstants.P2D, null);
    }

    public void size(int i, int i2, String str) {
        size(i, i2, str, null);
    }

    public void size(int i, int i2, String str, String str2) {
        System.out.println("This size() method is ignored on Android.");
        System.out.println("See http://wiki.processing.org/w/Android for more information.");
    }

    public File sketchFile(String str) {
        return new File(sketchPath(str));
    }

    public int sketchHeight() {
        return this.displayHeight;
    }

    public String sketchPath(String str) {
        if (this.sketchPath == null) {
            return str;
        }
        try {
            if (new File(str).isAbsolute()) {
                return str;
            }
        } catch (Exception e) {
        }
        return getApplicationContext().getFileStreamPath(str).getAbsolutePath();
    }

    public int sketchQuality() {
        return 1;
    }

    public String sketchRenderer() {
        return PConstants.JAVA2D;
    }

    public int sketchWidth() {
        return this.displayWidth;
    }

    public void smooth() {
        this.f2g.smooth();
    }

    public void smooth(int i) {
        this.f2g.smooth(i);
    }

    public void specular(float f) {
        this.f2g.specular(f);
    }

    public void specular(float f, float f2, float f3) {
        this.f2g.specular(f, f2, f3);
    }

    public void specular(int i) {
        this.f2g.specular(i);
    }

    public void sphere(float f) {
        this.f2g.sphere(f);
    }

    public void sphereDetail(int i) {
        this.f2g.sphereDetail(i);
    }

    public void sphereDetail(int i, int i2) {
        this.f2g.sphereDetail(i, i2);
    }

    public void spotLight(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        this.f2g.spotLight(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11);
    }

    public void start() {
        this.finished = false;
        this.paused = false;
        if (this.thread == null) {
            this.thread = new Thread(this, "Animation Thread");
            this.thread.start();
        }
    }

    public void stop() {
        this.paused = true;
    }

    public void stroke(float f) {
        this.f2g.stroke(f);
    }

    public void stroke(float f, float f2) {
        this.f2g.stroke(f, f2);
    }

    public void stroke(float f, float f2, float f3) {
        this.f2g.stroke(f, f2, f3);
    }

    public void stroke(float f, float f2, float f3, float f4) {
        this.f2g.stroke(f, f2, f3, f4);
    }

    public void stroke(int i) {
        this.f2g.stroke(i);
    }

    public void stroke(int i, float f) {
        this.f2g.stroke(i, f);
    }

    public void strokeCap(int i) {
        this.f2g.strokeCap(i);
    }

    public void strokeJoin(int i) {
        this.f2g.strokeJoin(i);
    }

    public void strokeWeight(float f) {
        this.f2g.strokeWeight(f);
    }

    public void style(PStyle pStyle) {
        this.f2g.style(pStyle);
    }

    public boolean surfaceKeyDown(int i, KeyEvent keyEvent) {
        nativeKeyEvent(keyEvent);
        return super.onKeyDown(i, keyEvent);
    }

    public boolean surfaceKeyUp(int i, KeyEvent keyEvent) {
        nativeKeyEvent(keyEvent);
        return super.onKeyUp(i, keyEvent);
    }

    public boolean surfaceTouchEvent(MotionEvent motionEvent) {
        nativeMotionEvent(motionEvent);
        return true;
    }

    public void surfaceWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        this.focused = z;
        if (this.focused) {
            focusGained();
        } else {
            focusLost();
        }
    }

    public void text(char c, float f, float f2) {
        this.f2g.text(c, f, f2);
    }

    public void text(char c, float f, float f2, float f3) {
        this.f2g.text(c, f, f2, f3);
    }

    public void text(float f, float f2, float f3) {
        this.f2g.text(f, f2, f3);
    }

    public void text(float f, float f2, float f3, float f4) {
        this.f2g.text(f, f2, f3, f4);
    }

    public void text(int i, float f, float f2) {
        this.f2g.text(i, f, f2);
    }

    public void text(int i, float f, float f2, float f3) {
        this.f2g.text(i, f, f2, f3);
    }

    public void text(String str, float f, float f2) {
        this.f2g.text(str, f, f2);
    }

    public void text(String str, float f, float f2, float f3) {
        this.f2g.text(str, f, f2, f3);
    }

    public void text(String str, float f, float f2, float f3, float f4) {
        this.f2g.text(str, f, f2, f3, f4);
    }

    public void textAlign(int i) {
        this.f2g.textAlign(i);
    }

    public void textAlign(int i, int i2) {
        this.f2g.textAlign(i, i2);
    }

    public float textAscent() {
        return this.f2g.textAscent();
    }

    public float textDescent() {
        return this.f2g.textDescent();
    }

    public void textFont(PFont pFont) {
        this.f2g.textFont(pFont);
    }

    public void textFont(PFont pFont, float f) {
        this.f2g.textFont(pFont, f);
    }

    public void textLeading(float f) {
        this.f2g.textLeading(f);
    }

    public void textMode(int i) {
        this.f2g.textMode(i);
    }

    public void textSize(float f) {
        this.f2g.textSize(f);
    }

    public float textWidth(char c) {
        return this.f2g.textWidth(c);
    }

    public float textWidth(String str) {
        return this.f2g.textWidth(str);
    }

    public void texture(PImage pImage) {
        this.f2g.texture(pImage);
    }

    public void textureMode(int i) {
        this.f2g.textureMode(i);
    }

    public void textureWrap(int i) {
        this.f2g.textureWrap(i);
    }

    public void thread(final String str) {
        new Thread() {
            public void run() {
                PApplet.this.method(str);
            }
        }.start();
    }

    public void tint(float f) {
        this.f2g.tint(f);
    }

    public void tint(float f, float f2) {
        this.f2g.tint(f, f2);
    }

    public void tint(float f, float f2, float f3) {
        this.f2g.tint(f, f2, f3);
    }

    public void tint(float f, float f2, float f3, float f4) {
        this.f2g.tint(f, f2, f3, f4);
    }

    public void tint(int i) {
        this.f2g.tint(i);
    }

    public void tint(int i, float f) {
        this.f2g.tint(i, f);
    }

    public void translate(float f, float f2) {
        this.f2g.translate(f, f2);
    }

    public void translate(float f, float f2, float f3) {
        this.f2g.translate(f, f2, f3);
    }

    public void triangle(float f, float f2, float f3, float f4, float f5, float f6) {
        this.f2g.triangle(f, f2, f3, f4, f5, f6);
    }

    @Deprecated
    public void unregisterDispose(Object obj) {
        unregisterMethod("dispose", obj);
    }

    @Deprecated
    public void unregisterDraw(Object obj) {
        unregisterMethod("draw", obj);
    }

    public void unregisterMethod(String str, Object obj) {
        RegisteredMethods registeredMethods = (RegisteredMethods) this.registerMap.get(str);
        if (registeredMethods == null) {
            die("No registered methods with the name " + str + "() were found.");
        }
        try {
            registeredMethods.remove(obj);
        } catch (Exception e) {
            die("Could not unregister " + str + "() for " + obj, e);
        }
    }

    @Deprecated
    public void unregisterPost(Object obj) {
        unregisterMethod("post", obj);
    }

    @Deprecated
    public void unregisterPre(Object obj) {
        unregisterMethod("pre", obj);
    }

    @Deprecated
    public void unregisterSize(Object obj) {
        System.err.println("The unregisterSize() command is no longer supported.");
    }

    public void updatePixels() {
        this.f2g.updatePixels();
    }

    public void updatePixels(int i, int i2, int i3, int i4) {
        this.f2g.updatePixels(i, i2, i3, i4);
    }

    public void vertex(float f, float f2) {
        this.f2g.vertex(f, f2);
    }

    public void vertex(float f, float f2, float f3) {
        this.f2g.vertex(f, f2, f3);
    }

    public void vertex(float f, float f2, float f3, float f4) {
        this.f2g.vertex(f, f2, f3, f4);
    }

    public void vertex(float f, float f2, float f3, float f4, float f5) {
        this.f2g.vertex(f, f2, f3, f4, f5);
    }

    public void vertex(float[] fArr) {
        this.f2g.vertex(fArr);
    }
}

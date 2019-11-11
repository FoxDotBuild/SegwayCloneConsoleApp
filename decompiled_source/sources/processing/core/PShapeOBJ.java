package processing.core;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class PShapeOBJ extends PShape {

    protected static class OBJFace {
        int matIdx = -1;
        String name = "";
        ArrayList<Integer> normIdx = new ArrayList<>();
        ArrayList<Integer> texIdx = new ArrayList<>();
        ArrayList<Integer> vertIdx = new ArrayList<>();

        OBJFace() {
        }
    }

    protected static class OBJMaterial {

        /* renamed from: d */
        float f56d;

        /* renamed from: ka */
        PVector f57ka;

        /* renamed from: kd */
        PVector f58kd;
        PImage kdMap;

        /* renamed from: ks */
        PVector f59ks;
        String name;

        /* renamed from: ns */
        float f60ns;

        OBJMaterial() {
            this("default");
        }

        OBJMaterial(String str) {
            this.name = str;
            this.f57ka = new PVector(0.5f, 0.5f, 0.5f);
            this.f58kd = new PVector(0.5f, 0.5f, 0.5f);
            this.f59ks = new PVector(0.5f, 0.5f, 0.5f);
            this.f56d = 1.0f;
            this.f60ns = 0.0f;
            this.kdMap = null;
        }
    }

    public PShapeOBJ(PApplet pApplet, BufferedReader bufferedReader) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        parseOBJ(pApplet, bufferedReader, arrayList, arrayList2, arrayList3, arrayList4, arrayList5);
        this.family = 0;
        addChildren(arrayList, arrayList2, arrayList3, arrayList4, arrayList5);
    }

    public PShapeOBJ(PApplet pApplet, String str) {
        this(pApplet, pApplet.createReader(str));
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x010a  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0126  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0139 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected PShapeOBJ(processing.core.PShapeOBJ.OBJFace r11, processing.core.PShapeOBJ.OBJMaterial r12, java.util.ArrayList<processing.core.PVector> r13, java.util.ArrayList<processing.core.PVector> r14, java.util.ArrayList<processing.core.PVector> r15) {
        /*
            r10 = this;
            r9 = 4
            r8 = 1
            r7 = -1
            r6 = 3
            r3 = 0
            r10.<init>()
            r10.family = r6
            java.util.ArrayList<java.lang.Integer> r0 = r11.vertIdx
            int r0 = r0.size()
            if (r0 != r6) goto L_0x013e
            r0 = 9
            r10.kind = r0
        L_0x0016:
            r10.stroke = r3
            r10.fill = r8
            processing.core.PVector r0 = r12.f58kd
            int r0 = rgbaValue(r0)
            r10.fillColor = r0
            processing.core.PVector r0 = r12.f57ka
            int r0 = rgbaValue(r0)
            r10.ambientColor = r0
            processing.core.PVector r0 = r12.f59ks
            int r0 = rgbaValue(r0)
            r10.specularColor = r0
            float r0 = r12.f60ns
            r10.shininess = r0
            processing.core.PImage r0 = r12.kdMap
            if (r0 == 0) goto L_0x0044
            processing.core.PVector r0 = r12.f58kd
            float r1 = r12.f56d
            int r0 = rgbaValue(r0, r1)
            r10.tintColor = r0
        L_0x0044:
            java.util.ArrayList<java.lang.Integer> r0 = r11.vertIdx
            int r0 = r0.size()
            r10.vertexCount = r0
            int r0 = r10.vertexCount
            r1 = 12
            int[] r0 = new int[]{r0, r1}
            java.lang.Class r1 = java.lang.Float.TYPE
            java.lang.Object r0 = java.lang.reflect.Array.newInstance(r1, r0)
            float[][] r0 = (float[][]) r0
            r10.vertices = r0
            r2 = r3
        L_0x005f:
            java.util.ArrayList<java.lang.Integer> r0 = r11.vertIdx
            int r0 = r0.size()
            if (r2 >= r0) goto L_0x0152
            r4 = 0
            java.util.ArrayList<java.lang.Integer> r0 = r11.vertIdx
            java.lang.Object r0 = r0.get(r2)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            int r0 = r0 + -1
            java.lang.Object r0 = r13.get(r0)
            processing.core.PVector r0 = (processing.core.PVector) r0
            java.util.ArrayList<java.lang.Integer> r1 = r11.normIdx
            int r1 = r1.size()
            if (r2 >= r1) goto L_0x0155
            java.util.ArrayList<java.lang.Integer> r1 = r11.normIdx
            java.lang.Object r1 = r1.get(r2)
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            int r1 = r1 + -1
            if (r7 >= r1) goto L_0x0155
            java.lang.Object r1 = r14.get(r1)
            processing.core.PVector r1 = (processing.core.PVector) r1
        L_0x009a:
            float[][] r4 = r10.vertices
            r4 = r4[r2]
            float r5 = r0.f68x
            r4[r3] = r5
            float[][] r4 = r10.vertices
            r4 = r4[r2]
            float r5 = r0.f69y
            r4[r8] = r5
            float[][] r4 = r10.vertices
            r4 = r4[r2]
            r5 = 2
            float r0 = r0.f70z
            r4[r5] = r0
            float[][] r0 = r10.vertices
            r0 = r0[r2]
            processing.core.PVector r4 = r12.f58kd
            float r4 = r4.f68x
            r0[r6] = r4
            float[][] r0 = r10.vertices
            r0 = r0[r2]
            processing.core.PVector r4 = r12.f58kd
            float r4 = r4.f69y
            r0[r9] = r4
            float[][] r0 = r10.vertices
            r0 = r0[r2]
            r4 = 5
            processing.core.PVector r5 = r12.f58kd
            float r5 = r5.f70z
            r0[r4] = r5
            float[][] r0 = r10.vertices
            r0 = r0[r2]
            r4 = 6
            r5 = 1065353216(0x3f800000, float:1.0)
            r0[r4] = r5
            if (r1 == 0) goto L_0x00fb
            float[][] r0 = r10.vertices
            r0 = r0[r2]
            r4 = 9
            float r5 = r1.f68x
            r0[r4] = r5
            float[][] r0 = r10.vertices
            r0 = r0[r2]
            r4 = 10
            float r5 = r1.f69y
            r0[r4] = r5
            float[][] r0 = r10.vertices
            r0 = r0[r2]
            r4 = 11
            float r1 = r1.f70z
            r0[r4] = r1
        L_0x00fb:
            if (r12 == 0) goto L_0x0139
            processing.core.PImage r0 = r12.kdMap
            if (r0 == 0) goto L_0x0139
            r1 = 0
            java.util.ArrayList<java.lang.Integer> r0 = r11.texIdx
            int r0 = r0.size()
            if (r2 >= r0) goto L_0x0153
            java.util.ArrayList<java.lang.Integer> r0 = r11.texIdx
            java.lang.Object r0 = r0.get(r2)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            int r0 = r0 + -1
            if (r7 >= r0) goto L_0x0153
            java.lang.Object r0 = r15.get(r0)
            processing.core.PVector r0 = (processing.core.PVector) r0
        L_0x0120:
            processing.core.PImage r1 = r12.kdMap
            r10.image = r1
            if (r0 == 0) goto L_0x0139
            float[][] r1 = r10.vertices
            r1 = r1[r2]
            r4 = 7
            float r5 = r0.f68x
            r1[r4] = r5
            float[][] r1 = r10.vertices
            r1 = r1[r2]
            r4 = 8
            float r0 = r0.f69y
            r1[r4] = r0
        L_0x0139:
            int r0 = r2 + 1
            r2 = r0
            goto L_0x005f
        L_0x013e:
            java.util.ArrayList<java.lang.Integer> r0 = r11.vertIdx
            int r0 = r0.size()
            if (r0 != r9) goto L_0x014c
            r0 = 17
            r10.kind = r0
            goto L_0x0016
        L_0x014c:
            r0 = 20
            r10.kind = r0
            goto L_0x0016
        L_0x0152:
            return
        L_0x0153:
            r0 = r1
            goto L_0x0120
        L_0x0155:
            r1 = r4
            goto L_0x009a
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.core.PShapeOBJ.<init>(processing.core.PShapeOBJ$OBJFace, processing.core.PShapeOBJ$OBJMaterial, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList):void");
    }

    protected static void parseMTL(PApplet pApplet, BufferedReader bufferedReader, ArrayList<OBJMaterial> arrayList, Hashtable<String, Integer> hashtable) {
        OBJMaterial oBJMaterial = null;
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String[] split = readLine.trim().split("\\s+");
                    if (split.length > 0) {
                        if (split[0].equals("newmtl")) {
                            String str = split[1];
                            oBJMaterial = new OBJMaterial(str);
                            hashtable.put(str, new Integer(arrayList.size()));
                            arrayList.add(oBJMaterial);
                        } else if (split[0].equals("map_Kd") && split.length > 1) {
                            oBJMaterial.kdMap = pApplet.loadImage(split[1]);
                        } else if (split[0].equals("Ka") && split.length > 3) {
                            oBJMaterial.f57ka.f68x = Float.valueOf(split[1]).floatValue();
                            oBJMaterial.f57ka.f69y = Float.valueOf(split[2]).floatValue();
                            oBJMaterial.f57ka.f70z = Float.valueOf(split[3]).floatValue();
                        } else if (split[0].equals("Kd") && split.length > 3) {
                            oBJMaterial.f58kd.f68x = Float.valueOf(split[1]).floatValue();
                            oBJMaterial.f58kd.f69y = Float.valueOf(split[2]).floatValue();
                            oBJMaterial.f58kd.f70z = Float.valueOf(split[3]).floatValue();
                        } else if (split[0].equals("Ks") && split.length > 3) {
                            oBJMaterial.f59ks.f68x = Float.valueOf(split[1]).floatValue();
                            oBJMaterial.f59ks.f69y = Float.valueOf(split[2]).floatValue();
                            oBJMaterial.f59ks.f70z = Float.valueOf(split[3]).floatValue();
                        } else if ((split[0].equals("d") || split[0].equals("Tr")) && split.length > 1) {
                            oBJMaterial.f56d = Float.valueOf(split[1]).floatValue();
                        } else if (split[0].equals("Ns") && split.length > 1) {
                            oBJMaterial.f60ns = Float.valueOf(split[1]).floatValue();
                        }
                    }
                } else {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    protected static void parseOBJ(PApplet pApplet, BufferedReader bufferedReader, ArrayList<OBJFace> arrayList, ArrayList<OBJMaterial> arrayList2, ArrayList<PVector> arrayList3, ArrayList<PVector> arrayList4, ArrayList<PVector> arrayList5) {
        String str;
        boolean z;
        boolean z2;
        boolean z3;
        int i;
        Hashtable hashtable = new Hashtable();
        int i2 = -1;
        boolean z4 = false;
        String str2 = "object";
        boolean z5 = false;
        boolean z6 = false;
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String trim = readLine.trim();
                if (!trim.equals("") && trim.indexOf(35) != 0) {
                    while (trim.contains("\\")) {
                        trim = trim.split("\\\\")[0];
                        String readLine2 = bufferedReader.readLine();
                        if (readLine2 != null) {
                            trim = trim + readLine2;
                        }
                    }
                    String[] split = trim.split("\\s+");
                    if (split.length > 0) {
                        if (split[0].equals("v")) {
                            arrayList3.add(new PVector(Float.valueOf(split[1]).floatValue(), Float.valueOf(split[2]).floatValue(), Float.valueOf(split[3]).floatValue()));
                            i = i2;
                            boolean z7 = z4;
                            z2 = z5;
                            z3 = true;
                            str = str2;
                            z = z7;
                        } else if (split[0].equals("vn")) {
                            arrayList4.add(new PVector(Float.valueOf(split[1]).floatValue(), Float.valueOf(split[2]).floatValue(), Float.valueOf(split[3]).floatValue()));
                            z3 = z6;
                            i = i2;
                            str = str2;
                            z = z4;
                            z2 = true;
                        } else if (split[0].equals("vt")) {
                            arrayList5.add(new PVector(Float.valueOf(split[1]).floatValue(), 1.0f - Float.valueOf(split[2]).floatValue()));
                            z2 = z5;
                            z3 = z6;
                            i = i2;
                            str = str2;
                            z = true;
                        } else if (split[0].equals("o")) {
                            str = str2;
                            z = z4;
                            z2 = z5;
                            z3 = z6;
                            i = i2;
                        } else if (split[0].equals("mtllib")) {
                            if (split[1] != null) {
                                BufferedReader createReader = pApplet.createReader(split[1]);
                                if (createReader != null) {
                                    parseMTL(pApplet, createReader, arrayList2, hashtable);
                                }
                                createReader.close();
                                str = str2;
                                z = z4;
                                z2 = z5;
                                z3 = z6;
                                i = i2;
                            }
                        } else if (split[0].equals("g")) {
                            str = 1 < split.length ? split[1] : "";
                            z = z4;
                            z2 = z5;
                            z3 = z6;
                            i = i2;
                        } else if (split[0].equals("usemtl")) {
                            if (split[1] != null) {
                                String str3 = split[1];
                                String str4 = str2;
                                z = z4;
                                z2 = z5;
                                z3 = z6;
                                i = hashtable.containsKey(str3) ? ((Integer) hashtable.get(str3)).intValue() : -1;
                                str = str4;
                            }
                        } else if (split[0].equals("f")) {
                            OBJFace oBJFace = new OBJFace();
                            oBJFace.matIdx = i2;
                            oBJFace.name = str2;
                            for (int i3 = 1; i3 < split.length; i3++) {
                                String str5 = split[i3];
                                if (str5.indexOf("/") > 0) {
                                    String[] split2 = str5.split("/");
                                    if (split2.length > 2) {
                                        if (split2[0].length() > 0 && z6) {
                                            oBJFace.vertIdx.add(Integer.valueOf(split2[0]));
                                        }
                                        if (split2[1].length() > 0 && z4) {
                                            oBJFace.texIdx.add(Integer.valueOf(split2[1]));
                                        }
                                        if (split2[2].length() > 0 && z5) {
                                            oBJFace.normIdx.add(Integer.valueOf(split2[2]));
                                        }
                                    } else if (split2.length > 1) {
                                        if (split2[0].length() > 0 && z6) {
                                            oBJFace.vertIdx.add(Integer.valueOf(split2[0]));
                                        }
                                        if (split2[1].length() > 0) {
                                            if (z4) {
                                                oBJFace.texIdx.add(Integer.valueOf(split2[1]));
                                            } else if (z5) {
                                                oBJFace.normIdx.add(Integer.valueOf(split2[1]));
                                            }
                                        }
                                    } else if (split2.length > 0 && split2[0].length() > 0 && z6) {
                                        oBJFace.vertIdx.add(Integer.valueOf(split2[0]));
                                    }
                                } else if (str5.length() > 0 && z6) {
                                    oBJFace.vertIdx.add(Integer.valueOf(str5));
                                }
                            }
                            arrayList.add(oBJFace);
                        }
                        i2 = i;
                        z6 = z3;
                        z5 = z2;
                        z4 = z;
                        str2 = str;
                    }
                    str = str2;
                    z = z4;
                    z2 = z5;
                    z3 = z6;
                    i = i2;
                    i2 = i;
                    z6 = z3;
                    z5 = z2;
                    z4 = z;
                    str2 = str;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (arrayList2.size() == 0) {
            arrayList2.add(new OBJMaterial());
        }
    }

    protected static int rgbaValue(PVector pVector) {
        return -16777216 | (((int) (pVector.f68x * 255.0f)) << 16) | (((int) (pVector.f69y * 255.0f)) << 8) | ((int) (pVector.f70z * 255.0f));
    }

    protected static int rgbaValue(PVector pVector, float f) {
        return (((int) (f * 255.0f)) << 24) | (((int) (pVector.f68x * 255.0f)) << 16) | (((int) (pVector.f69y * 255.0f)) << 8) | ((int) (pVector.f70z * 255.0f));
    }

    /* access modifiers changed from: protected */
    public void addChildren(ArrayList<OBJFace> arrayList, ArrayList<OBJMaterial> arrayList2, ArrayList<PVector> arrayList3, ArrayList<PVector> arrayList4, ArrayList<PVector> arrayList5) {
        int i;
        OBJMaterial oBJMaterial = null;
        int i2 = 0;
        int i3 = -1;
        while (i2 < arrayList.size()) {
            OBJFace oBJFace = (OBJFace) arrayList.get(i2);
            if (i3 != oBJFace.matIdx || oBJFace.matIdx == -1) {
                int max = PApplet.max(0, oBJFace.matIdx);
                i = max;
                oBJMaterial = (OBJMaterial) arrayList2.get(max);
            } else {
                i = i3;
            }
            addChild(new PShapeOBJ(oBJFace, oBJMaterial, arrayList3, arrayList4, arrayList5));
            i2++;
            i3 = i;
        }
    }
}

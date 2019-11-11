package processing.core;

import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import java.lang.reflect.Array;
import java.util.HashMap;
import processing.data.XML;

public class PShapeSVG extends PShape {
    XML element;
    Gradient fillGradient;
    Shader fillGradientPaint;
    String fillName;
    float fillOpacity;
    float opacity;
    Gradient strokeGradient;
    Shader strokeGradientPaint;
    String strokeName;
    float strokeOpacity;

    public class Font extends PShapeSVG {
        public FontFace face;
        public int glyphCount = 0;
        public FontGlyph[] glyphs;
        int horizAdvX;
        public FontGlyph missingGlyph;
        public HashMap<String, FontGlyph> namedGlyphs = new HashMap<>();
        final /* synthetic */ PShapeSVG this$0;
        public HashMap<Character, FontGlyph> unicodeGlyphs = new HashMap<>();

        public Font(PShapeSVG pShapeSVG, PShapeSVG pShapeSVG2, XML xml) {
            this.this$0 = pShapeSVG;
            super(pShapeSVG2, xml, false);
            XML[] children = xml.getChildren();
            this.horizAdvX = xml.getInt("horiz-adv-x", 0);
            this.glyphs = new FontGlyph[children.length];
            for (int i = 0; i < children.length; i++) {
                String name = children[i].getName();
                XML xml2 = children[i];
                if (name != null) {
                    if (name.equals("glyph")) {
                        FontGlyph fontGlyph = new FontGlyph(this, xml2, this);
                        if (fontGlyph.isLegit()) {
                            if (fontGlyph.name != null) {
                                this.namedGlyphs.put(fontGlyph.name, fontGlyph);
                            }
                            if (fontGlyph.unicode != 0) {
                                this.unicodeGlyphs.put(new Character(fontGlyph.unicode), fontGlyph);
                            }
                        }
                        FontGlyph[] fontGlyphArr = this.glyphs;
                        int i2 = this.glyphCount;
                        this.glyphCount = i2 + 1;
                        fontGlyphArr[i2] = fontGlyph;
                    } else if (name.equals("missing-glyph")) {
                        this.missingGlyph = new FontGlyph(this, xml2, this);
                    } else if (name.equals("font-face")) {
                        this.face = new FontFace(this, xml2);
                    } else {
                        System.err.println("Ignoring " + name + " inside <font>");
                    }
                }
            }
        }

        public void drawChar(PGraphics pGraphics, char c, float f, float f2, float f3) {
            pGraphics.pushMatrix();
            float f4 = f3 / ((float) this.face.unitsPerEm);
            pGraphics.translate(f, f2);
            pGraphics.scale(f4, -f4);
            FontGlyph fontGlyph = (FontGlyph) this.unicodeGlyphs.get(new Character(c));
            if (fontGlyph != null) {
                pGraphics.shape(fontGlyph);
            }
            pGraphics.popMatrix();
        }

        /* access modifiers changed from: protected */
        public void drawShape() {
        }

        public void drawString(PGraphics pGraphics, String str, float f, float f2, float f3) {
            pGraphics.pushMatrix();
            float f4 = f3 / ((float) this.face.unitsPerEm);
            pGraphics.translate(f, f2);
            pGraphics.scale(f4, -f4);
            char[] charArray = str.toCharArray();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < charArray.length) {
                    FontGlyph fontGlyph = (FontGlyph) this.unicodeGlyphs.get(new Character(charArray[i2]));
                    if (fontGlyph != null) {
                        fontGlyph.draw(pGraphics);
                        pGraphics.translate((float) fontGlyph.horizAdvX, 0.0f);
                    } else {
                        System.err.println("'" + charArray[i2] + "' not available.");
                    }
                    i = i2 + 1;
                } else {
                    pGraphics.popMatrix();
                    return;
                }
            }
        }

        public float textWidth(String str, float f) {
            char[] charArray = str.toCharArray();
            int i = 0;
            float f2 = 0.0f;
            while (true) {
                int i2 = i;
                if (i2 >= charArray.length) {
                    return f2 * f;
                }
                FontGlyph fontGlyph = (FontGlyph) this.unicodeGlyphs.get(new Character(charArray[i2]));
                if (fontGlyph != null) {
                    f2 += ((float) fontGlyph.horizAdvX) / ((float) this.face.unitsPerEm);
                }
                i = i2 + 1;
            }
        }
    }

    class FontFace extends PShapeSVG {
        int ascent;
        int[] bbox;
        int descent;
        String fontFamily;
        String fontStretch;
        int fontWeight;
        int horizOriginX;
        int horizOriginY;
        int[] panose1;
        int underlinePosition;
        int underlineThickness;
        int unitsPerEm;
        int vertAdvY;
        int vertOriginX;
        int vertOriginY;

        public FontFace(PShapeSVG pShapeSVG, XML xml) {
            super(pShapeSVG, xml, true);
            this.unitsPerEm = xml.getInt("units-per-em", 1000);
        }

        /* access modifiers changed from: protected */
        public void drawShape() {
        }
    }

    public class FontGlyph extends PShapeSVG {
        int horizAdvX;
        public String name;
        char unicode = 0;

        public FontGlyph(PShapeSVG pShapeSVG, XML xml, Font font) {
            super(pShapeSVG, xml, true);
            PShapeSVG.super.parsePath();
            this.name = xml.getString("glyph-name");
            String string = xml.getString("unicode");
            if (string != null) {
                if (string.length() == 1) {
                    this.unicode = string.charAt(0);
                } else {
                    System.err.println("unicode for " + this.name + " is more than one char: " + string);
                }
            }
            if (xml.hasAttribute("horiz-adv-x")) {
                this.horizAdvX = xml.getInt("horiz-adv-x");
            } else {
                this.horizAdvX = font.horizAdvX;
            }
        }

        /* access modifiers changed from: protected */
        public boolean isLegit() {
            return this.vertexCount != 0;
        }
    }

    static class Gradient extends PShapeSVG {
        int[] color;
        int count;
        float[] offset;
        Matrix transform;

        public Gradient(PShapeSVG pShapeSVG, XML xml) {
            super(pShapeSVG, xml, true);
            XML[] children = xml.getChildren();
            this.offset = new float[children.length];
            this.color = new int[children.length];
            for (XML xml2 : children) {
                if (xml2.getName().equals("stop")) {
                    String string = xml2.getString("offset");
                    float f = 1.0f;
                    if (string.endsWith("%")) {
                        f = 100.0f;
                        string = string.substring(0, string.length() - 1);
                    }
                    this.offset[this.count] = PApplet.parseFloat(string) / f;
                    HashMap parseStyleAttributes = parseStyleAttributes(xml2.getString("style"));
                    String str = (String) parseStyleAttributes.get("stop-color");
                    String str2 = str == null ? "#000000" : str;
                    String str3 = (String) parseStyleAttributes.get("stop-opacity");
                    if (str3 == null) {
                        str3 = "1";
                    }
                    this.color[this.count] = (((int) (PApplet.parseFloat(str3) * 255.0f)) << 24) | Integer.parseInt(str2.substring(1), 16);
                    this.count++;
                }
            }
            this.offset = PApplet.subset(this.offset, 0, this.count);
            this.color = PApplet.subset(this.color, 0, this.count);
        }
    }

    class LinearGradient extends Gradient {

        /* renamed from: x1 */
        float f61x1;

        /* renamed from: x2 */
        float f62x2;

        /* renamed from: y1 */
        float f63y1;

        /* renamed from: y2 */
        float f64y2;

        public LinearGradient(PShapeSVG pShapeSVG, XML xml) {
            super(pShapeSVG, xml);
            this.f61x1 = getFloatWithUnit(xml, "x1");
            this.f63y1 = getFloatWithUnit(xml, "y1");
            this.f62x2 = getFloatWithUnit(xml, "x2");
            this.f64y2 = getFloatWithUnit(xml, "y2");
            String string = xml.getString("gradientTransform");
            if (string != null) {
                float[] fArr = parseTransform(string).get(null);
                this.transform = new Matrix();
                this.transform.setValues(new float[]{fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5], 0.0f, 0.0f, 1.0f});
                float[] fArr2 = {this.f61x1, this.f63y1};
                float[] fArr3 = {this.f62x2, this.f64y2};
                this.transform.mapPoints(fArr2);
                this.transform.mapPoints(fArr3);
                this.f61x1 = fArr2[0];
                this.f63y1 = fArr2[1];
                this.f62x2 = fArr3[0];
                this.f64y2 = fArr3[1];
            }
        }
    }

    class RadialGradient extends Gradient {

        /* renamed from: cx */
        float f65cx;

        /* renamed from: cy */
        float f66cy;

        /* renamed from: r */
        float f67r;

        public RadialGradient(PShapeSVG pShapeSVG, XML xml) {
            super(pShapeSVG, xml);
            this.f65cx = getFloatWithUnit(xml, "cx");
            this.f66cy = getFloatWithUnit(xml, "cy");
            this.f67r = getFloatWithUnit(xml, "r");
            String string = xml.getString("gradientTransform");
            if (string != null) {
                float[] fArr = parseTransform(string).get(null);
                this.transform = new Matrix();
                this.transform.setValues(new float[]{fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5], 0.0f, 0.0f, 1.0f});
                float[] fArr2 = {this.f65cx, this.f66cy};
                float[] fArr3 = {this.f65cx + this.f67r, this.f66cy};
                this.transform.mapPoints(fArr2);
                this.transform.mapPoints(fArr3);
                this.f65cx = fArr2[0];
                this.f66cy = fArr2[1];
                this.f67r = fArr3[0] - fArr2[0];
            }
        }
    }

    protected PShapeSVG(PShapeSVG pShapeSVG, XML xml, boolean z) {
        boolean z2 = true;
        this.parent = pShapeSVG;
        if (pShapeSVG == null) {
            this.stroke = false;
            this.strokeColor = PImage.ALPHA_MASK;
            this.strokeWeight = 1.0f;
            this.strokeCap = 1;
            this.strokeJoin = 8;
            this.strokeGradient = null;
            this.strokeGradientPaint = null;
            this.strokeName = null;
            this.fill = true;
            this.fillColor = PImage.ALPHA_MASK;
            this.fillGradient = null;
            this.fillGradientPaint = null;
            this.fillName = null;
            this.strokeOpacity = 1.0f;
            this.fillOpacity = 1.0f;
            this.opacity = 1.0f;
        } else {
            this.stroke = pShapeSVG.stroke;
            this.strokeColor = pShapeSVG.strokeColor;
            this.strokeWeight = pShapeSVG.strokeWeight;
            this.strokeCap = pShapeSVG.strokeCap;
            this.strokeJoin = pShapeSVG.strokeJoin;
            this.strokeGradient = pShapeSVG.strokeGradient;
            this.strokeGradientPaint = pShapeSVG.strokeGradientPaint;
            this.strokeName = pShapeSVG.strokeName;
            this.fill = pShapeSVG.fill;
            this.fillColor = pShapeSVG.fillColor;
            this.fillGradient = pShapeSVG.fillGradient;
            this.fillGradientPaint = pShapeSVG.fillGradientPaint;
            this.fillName = pShapeSVG.fillName;
            this.opacity = pShapeSVG.opacity;
        }
        this.element = xml;
        this.name = xml.getString("id");
        if (this.name != null) {
            while (true) {
                String[] match = PApplet.match(this.name, "_x([A-Za-z0-9]{2})_");
                if (match == null) {
                    break;
                }
                this.name = this.name.replace(match[0], "" + ((char) PApplet.unhex(match[1])));
            }
        }
        if (xml.getString("display", "inline").equals("none")) {
            z2 = false;
        }
        this.visible = z2;
        String string = xml.getString("transform");
        if (string != null) {
            this.matrix = parseTransform(string);
        }
        if (z) {
            parseColors(xml);
            parseChildren(xml);
        }
    }

    public PShapeSVG(XML xml) {
        this(null, xml, true);
        if (!xml.getName().equals("svg")) {
            throw new RuntimeException("root is not <svg>, it's <" + xml.getName() + ">");
        }
        String string = xml.getString("viewBox");
        if (string != null) {
            int[] parseInt = PApplet.parseInt(PApplet.splitTokens(string));
            this.width = (float) parseInt[2];
            this.height = (float) parseInt[3];
        }
        String string2 = xml.getString("width");
        String string3 = xml.getString("height");
        if (string2 != null) {
            this.width = parseUnitSize(string2);
            this.height = parseUnitSize(string3);
        } else if (this.width == 0.0f || this.height == 0.0f) {
            PGraphics.showWarning("The width and/or height is not readable in the <svg> tag of this file.");
            this.width = 1.0f;
            this.height = 1.0f;
        }
    }

    protected static float getFloatWithUnit(XML xml, String str) {
        String string = xml.getString(str);
        if (string == null) {
            return 0.0f;
        }
        return parseUnitSize(string);
    }

    private void parsePathCode(int i) {
        if (this.vertexCodeCount == this.vertexCodes.length) {
            this.vertexCodes = PApplet.expand(this.vertexCodes);
        }
        int[] iArr = this.vertexCodes;
        int i2 = this.vertexCodeCount;
        this.vertexCodeCount = i2 + 1;
        iArr[i2] = i;
    }

    private void parsePathCurveto(float f, float f2, float f3, float f4, float f5, float f6) {
        parsePathCode(1);
        parsePathVertex(f, f2);
        parsePathVertex(f3, f4);
        parsePathVertex(f5, f6);
    }

    private void parsePathLineto(float f, float f2) {
        parsePathCode(0);
        parsePathVertex(f, f2);
    }

    private void parsePathMoveto(float f, float f2) {
        if (this.vertexCount > 0) {
            parsePathCode(4);
        }
        parsePathCode(0);
        parsePathVertex(f, f2);
    }

    private void parsePathQuadto(float f, float f2, float f3, float f4) {
        parsePathCode(2);
        parsePathVertex(f, f2);
        parsePathVertex(f3, f4);
    }

    private void parsePathVertex(float f, float f2) {
        if (this.vertexCount == this.vertices.length) {
            float[][] fArr = (float[][]) Array.newInstance(Float.TYPE, new int[]{this.vertexCount << 1, 2});
            System.arraycopy(this.vertices, 0, fArr, 0, this.vertexCount);
            this.vertices = fArr;
        }
        this.vertices[this.vertexCount][0] = f;
        this.vertices[this.vertexCount][1] = f2;
        this.vertexCount++;
    }

    protected static int parseRGB(String str) {
        int[] parseInt = PApplet.parseInt(PApplet.splitTokens(str.substring(str.indexOf(40) + 1, str.indexOf(41)), ", "));
        return parseInt[2] | (parseInt[0] << 16) | (parseInt[1] << 8);
    }

    protected static PMatrix2D parseSingleTransform(String str) {
        String[] match = PApplet.match(str, "[,\\s]*(\\w+)\\((.*)\\)");
        if (match == null) {
            System.err.println("Could not parse transform " + str);
            return null;
        }
        float[] parseFloat = PApplet.parseFloat(PApplet.splitTokens(match[2], ", "));
        if (match[1].equals("matrix")) {
            return new PMatrix2D(parseFloat[0], parseFloat[2], parseFloat[4], parseFloat[1], parseFloat[3], parseFloat[5]);
        }
        if (match[1].equals("translate")) {
            return new PMatrix2D(1.0f, 0.0f, parseFloat[0], 0.0f, 1.0f, parseFloat.length == 2 ? parseFloat[1] : parseFloat[0]);
        } else if (match[1].equals("scale")) {
            return new PMatrix2D(parseFloat[0], 0.0f, 0.0f, 0.0f, parseFloat.length == 2 ? parseFloat[1] : parseFloat[0], 0.0f);
        } else {
            if (match[1].equals("rotate")) {
                float f = parseFloat[0];
                if (parseFloat.length == 1) {
                    float cos = PApplet.cos(f);
                    float sin = PApplet.sin(f);
                    return new PMatrix2D(cos, -sin, 0.0f, sin, cos, 0.0f);
                } else if (parseFloat.length == 3) {
                    PMatrix2D pMatrix2D = new PMatrix2D(0.0f, 1.0f, parseFloat[1], 1.0f, 0.0f, parseFloat[2]);
                    pMatrix2D.rotate(parseFloat[0]);
                    pMatrix2D.translate(-parseFloat[1], -parseFloat[2]);
                    return pMatrix2D;
                }
            } else if (match[1].equals("skewX")) {
                return new PMatrix2D(1.0f, 0.0f, 1.0f, PApplet.tan(parseFloat[0]), 0.0f, 0.0f);
            } else {
                if (match[1].equals("skewY")) {
                    return new PMatrix2D(1.0f, 0.0f, 1.0f, 0.0f, PApplet.tan(parseFloat[0]), 0.0f);
                }
            }
            return null;
        }
    }

    protected static HashMap<String, String> parseStyleAttributes(String str) {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] split = str.split(";");
        for (String split2 : split) {
            String[] split3 = split2.split(":");
            hashMap.put(split3[0], split3[1]);
        }
        return hashMap;
    }

    protected static PMatrix2D parseTransform(String str) {
        String trim = str.trim();
        PMatrix2D pMatrix2D = null;
        int i = 0;
        while (true) {
            int indexOf = trim.indexOf(41, i);
            if (indexOf == -1) {
                return pMatrix2D;
            }
            PMatrix2D parseSingleTransform = parseSingleTransform(trim.substring(i, indexOf + 1));
            if (pMatrix2D != null) {
                pMatrix2D.apply(parseSingleTransform);
                parseSingleTransform = pMatrix2D;
            }
            pMatrix2D = parseSingleTransform;
            i = indexOf + 1;
        }
    }

    protected static float parseUnitSize(String str) {
        int length = str.length() - 2;
        return str.endsWith("pt") ? PApplet.parseFloat(str.substring(0, length)) * 1.25f : str.endsWith("pc") ? PApplet.parseFloat(str.substring(0, length)) * 15.0f : str.endsWith("mm") ? PApplet.parseFloat(str.substring(0, length)) * 3.543307f : str.endsWith("cm") ? PApplet.parseFloat(str.substring(0, length)) * 35.43307f : str.endsWith("in") ? PApplet.parseFloat(str.substring(0, length)) * 90.0f : str.endsWith("px") ? PApplet.parseFloat(str.substring(0, length)) : PApplet.parseFloat(str);
    }

    /* access modifiers changed from: protected */
    public Shader calcGradientPaint(Gradient gradient) {
        int[] iArr = new int[gradient.count];
        int i = ((int) (this.opacity * 255.0f)) << 24;
        for (int i2 = 0; i2 < gradient.count; i2++) {
            iArr[i2] = (gradient.color[i2] & 16777215) | i;
        }
        if (gradient instanceof LinearGradient) {
            LinearGradient linearGradient = (LinearGradient) gradient;
            return new android.graphics.LinearGradient(linearGradient.f61x1, linearGradient.f63y1, linearGradient.f62x2, linearGradient.f64y2, iArr, linearGradient.offset, TileMode.CLAMP);
        } else if (!(gradient instanceof RadialGradient)) {
            return null;
        } else {
            RadialGradient radialGradient = (RadialGradient) gradient;
            return new android.graphics.RadialGradient(radialGradient.f65cx, radialGradient.f66cy, radialGradient.f67r, iArr, radialGradient.offset, TileMode.CLAMP);
        }
    }

    public PShape getChild(String str) {
        PShape child = super.getChild(str);
        if (child == null) {
            child = super.getChild(str.replace(' ', '_'));
        }
        if (child != null) {
            child.width = this.width;
            child.height = this.height;
        }
        return child;
    }

    /* access modifiers changed from: protected */
    public PShape parseChild(XML xml) {
        String name = xml.getName();
        if (name == null) {
            return null;
        }
        if (name.equals("g")) {
            return new PShapeSVG(this, xml, true);
        }
        if (name.equals("defs")) {
            return new PShapeSVG(this, xml, true);
        }
        if (name.equals("line")) {
            PShapeSVG pShapeSVG = new PShapeSVG(this, xml, true);
            pShapeSVG.parseLine();
            return pShapeSVG;
        } else if (name.equals("circle")) {
            PShapeSVG pShapeSVG2 = new PShapeSVG(this, xml, true);
            pShapeSVG2.parseEllipse(true);
            return pShapeSVG2;
        } else if (name.equals("ellipse")) {
            PShapeSVG pShapeSVG3 = new PShapeSVG(this, xml, true);
            pShapeSVG3.parseEllipse(false);
            return pShapeSVG3;
        } else if (name.equals("rect")) {
            PShapeSVG pShapeSVG4 = new PShapeSVG(this, xml, true);
            pShapeSVG4.parseRect();
            return pShapeSVG4;
        } else if (name.equals("polygon")) {
            PShapeSVG pShapeSVG5 = new PShapeSVG(this, xml, true);
            pShapeSVG5.parsePoly(true);
            return pShapeSVG5;
        } else if (name.equals("polyline")) {
            PShapeSVG pShapeSVG6 = new PShapeSVG(this, xml, true);
            pShapeSVG6.parsePoly(false);
            return pShapeSVG6;
        } else if (name.equals("path")) {
            PShapeSVG pShapeSVG7 = new PShapeSVG(this, xml, true);
            pShapeSVG7.parsePath();
            return pShapeSVG7;
        } else if (name.equals("radialGradient")) {
            return new RadialGradient(this, xml);
        } else {
            if (name.equals("linearGradient")) {
                return new LinearGradient(this, xml);
            }
            if (name.equals("font")) {
                return new Font(this, this, xml);
            }
            if (name.equals("metadata")) {
                return null;
            }
            if (name.equals("text")) {
                PGraphics.showWarning("Text and fonts in SVG files are not currently supported, convert text to outlines instead.");
                return null;
            } else if (name.equals("filter")) {
                PGraphics.showWarning("Filters are not supported.");
                return null;
            } else if (name.equals("mask")) {
                PGraphics.showWarning("Masks are not supported.");
                return null;
            } else if (name.equals("pattern")) {
                PGraphics.showWarning("Patterns are not supported.");
                return null;
            } else if (name.equals("stop") || name.equals("sodipodi:namedview")) {
                return null;
            } else {
                PGraphics.showWarning("Ignoring <" + name + "> tag.");
                return null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void parseChildren(XML xml) {
        XML[] children = xml.getChildren();
        this.children = new PShape[children.length];
        this.childCount = 0;
        for (XML parseChild : children) {
            PShape parseChild2 = parseChild(parseChild);
            if (parseChild2 != null) {
                addChild(parseChild2);
            }
        }
        this.children = (PShape[]) PApplet.subset((Object) this.children, 0, this.childCount);
    }

    /* access modifiers changed from: protected */
    public void parseColors(XML xml) {
        if (xml.hasAttribute("opacity")) {
            setOpacity(xml.getString("opacity"));
        }
        if (xml.hasAttribute("stroke")) {
            setColor(xml.getString("stroke"), false);
        }
        if (xml.hasAttribute("stroke-opacity")) {
            setStrokeOpacity(xml.getString("stroke-opacity"));
        }
        if (xml.hasAttribute("stroke-width")) {
            setStrokeWeight(xml.getString("stroke-width"));
        }
        if (xml.hasAttribute("stroke-linejoin")) {
            setStrokeJoin(xml.getString("stroke-linejoin"));
        }
        if (xml.hasAttribute("stroke-linecap")) {
            setStrokeCap(xml.getString("stroke-linecap"));
        }
        if (xml.hasAttribute("fill")) {
            setColor(xml.getString("fill"), true);
        }
        if (xml.hasAttribute("fill-opacity")) {
            setFillOpacity(xml.getString("fill-opacity"));
        }
        if (xml.hasAttribute("style")) {
            String[] splitTokens = PApplet.splitTokens(xml.getString("style"), ";");
            for (String splitTokens2 : splitTokens) {
                String[] splitTokens3 = PApplet.splitTokens(splitTokens2, ":");
                splitTokens3[0] = PApplet.trim(splitTokens3[0]);
                if (splitTokens3[0].equals("fill")) {
                    setColor(splitTokens3[1], true);
                } else if (splitTokens3[0].equals("fill-opacity")) {
                    setFillOpacity(splitTokens3[1]);
                } else if (splitTokens3[0].equals("stroke")) {
                    setColor(splitTokens3[1], false);
                } else if (splitTokens3[0].equals("stroke-width")) {
                    setStrokeWeight(splitTokens3[1]);
                } else if (splitTokens3[0].equals("stroke-linecap")) {
                    setStrokeCap(splitTokens3[1]);
                } else if (splitTokens3[0].equals("stroke-linejoin")) {
                    setStrokeJoin(splitTokens3[1]);
                } else if (splitTokens3[0].equals("stroke-opacity")) {
                    setStrokeOpacity(splitTokens3[1]);
                } else if (splitTokens3[0].equals("opacity")) {
                    setOpacity(splitTokens3[1]);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void parseEllipse(boolean z) {
        float floatWithUnit;
        float floatWithUnit2;
        this.kind = 31;
        this.family = 1;
        this.params = new float[4];
        this.params[0] = getFloatWithUnit(this.element, "cx");
        this.params[1] = getFloatWithUnit(this.element, "cy");
        if (z) {
            floatWithUnit2 = getFloatWithUnit(this.element, "r");
            floatWithUnit = floatWithUnit2;
        } else {
            floatWithUnit = getFloatWithUnit(this.element, "rx");
            floatWithUnit2 = getFloatWithUnit(this.element, "ry");
        }
        float[] fArr = this.params;
        fArr[0] = fArr[0] - floatWithUnit;
        float[] fArr2 = this.params;
        fArr2[1] = fArr2[1] - floatWithUnit2;
        this.params[2] = floatWithUnit * 2.0f;
        this.params[3] = floatWithUnit2 * 2.0f;
    }

    /* access modifiers changed from: protected */
    public void parseLine() {
        this.kind = 4;
        this.family = 1;
        this.params = new float[]{getFloatWithUnit(this.element, "x1"), getFloatWithUnit(this.element, "y1"), getFloatWithUnit(this.element, "x2"), getFloatWithUnit(this.element, "y2")};
    }

    /* access modifiers changed from: protected */
    public void parsePath() {
        char c;
        int i;
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        this.family = 2;
        this.kind = 0;
        String string = this.element.getString("d");
        if (string != null && PApplet.trim(string).length() != 0) {
            char[] charArray = string.toCharArray();
            StringBuffer stringBuffer = new StringBuffer();
            boolean z = false;
            int i2 = 0;
            while (i2 < charArray.length) {
                char c2 = charArray[i2];
                boolean z2 = false;
                if (c2 == 'M' || c2 == 'm' || c2 == 'L' || c2 == 'l' || c2 == 'H' || c2 == 'h' || c2 == 'V' || c2 == 'v' || c2 == 'C' || c2 == 'c' || c2 == 'S' || c2 == 's' || c2 == 'Q' || c2 == 'q' || c2 == 'T' || c2 == 't' || c2 == 'Z' || c2 == 'z' || c2 == ',') {
                    z2 = true;
                    if (i2 != 0) {
                        stringBuffer.append("|");
                    }
                }
                if (c2 == 'Z' || c2 == 'z') {
                    z2 = false;
                }
                if (c2 == '-' && !z && (i2 == 0 || charArray[i2 - 1] != 'e')) {
                    stringBuffer.append("|");
                }
                if (c2 != ',') {
                    stringBuffer.append(c2);
                }
                if (!(!z2 || c2 == ',' || c2 == '-')) {
                    stringBuffer.append("|");
                }
                i2++;
                z = z2;
            }
            String[] splitTokens = PApplet.splitTokens(stringBuffer.toString(), "| \t\n\r\f ");
            this.vertices = (float[][]) Array.newInstance(Float.TYPE, new int[]{splitTokens.length, 2});
            this.vertexCodes = new int[splitTokens.length];
            float f7 = 0.0f;
            float f8 = 0.0f;
            int i3 = 0;
            char c3 = 0;
            boolean z3 = false;
            float f9 = 0.0f;
            float f10 = 0.0f;
            while (true) {
                float f11 = f10;
                float f12 = f9;
                boolean z4 = z3;
                char c4 = c3;
                int i4 = i3;
                float f13 = f8;
                float f14 = f7;
                float f15 = f13;
                if (i4 < splitTokens.length) {
                    char charAt = splitTokens[i4].charAt(0);
                    if (((charAt < '0' || charAt > '9') && charAt != '-') || c4 == 0) {
                        c4 = charAt;
                        c = charAt;
                        i = i4;
                    } else {
                        c = c4;
                        i = i4 - 1;
                    }
                    switch (c4) {
                        case 'C':
                            float parseFloat = PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat2 = PApplet.parseFloat(splitTokens[i + 2]);
                            float parseFloat3 = PApplet.parseFloat(splitTokens[i + 3]);
                            float parseFloat4 = PApplet.parseFloat(splitTokens[i + 4]);
                            float parseFloat5 = PApplet.parseFloat(splitTokens[i + 5]);
                            float parseFloat6 = PApplet.parseFloat(splitTokens[i + 6]);
                            parsePathCurveto(parseFloat, parseFloat2, parseFloat3, parseFloat4, parseFloat5, parseFloat6);
                            i3 = i + 7;
                            f9 = f12;
                            z3 = true;
                            c3 = c;
                            f10 = f11;
                            float f16 = parseFloat6;
                            f7 = parseFloat5;
                            f8 = f16;
                            break;
                        case 'H':
                            float parseFloat7 = PApplet.parseFloat(splitTokens[i + 1]);
                            parsePathLineto(parseFloat7, f15);
                            i3 = i + 2;
                            f9 = f12;
                            z3 = z4;
                            c3 = c;
                            f10 = f11;
                            float f17 = f15;
                            f7 = parseFloat7;
                            f8 = f17;
                            break;
                        case 'L':
                            float parseFloat8 = PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat9 = PApplet.parseFloat(splitTokens[i + 2]);
                            parsePathLineto(parseFloat8, parseFloat9);
                            i3 = i + 3;
                            f9 = f12;
                            z3 = z4;
                            c3 = c;
                            f10 = f11;
                            float f18 = parseFloat9;
                            f7 = parseFloat8;
                            f8 = f18;
                            break;
                        case 'M':
                            float parseFloat10 = PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat11 = PApplet.parseFloat(splitTokens[i + 2]);
                            parsePathMoveto(parseFloat10, parseFloat11);
                            c3 = 'L';
                            i3 = i + 3;
                            f9 = parseFloat10;
                            z3 = z4;
                            f8 = parseFloat11;
                            f7 = parseFloat10;
                            f10 = parseFloat11;
                            break;
                        case 'Q':
                            float parseFloat12 = PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat13 = PApplet.parseFloat(splitTokens[i + 2]);
                            float parseFloat14 = PApplet.parseFloat(splitTokens[i + 3]);
                            float parseFloat15 = PApplet.parseFloat(splitTokens[i + 4]);
                            parsePathQuadto(parseFloat12, parseFloat13, parseFloat14, parseFloat15);
                            i3 = i + 5;
                            f9 = f12;
                            z3 = true;
                            c3 = c;
                            f10 = f11;
                            float f19 = parseFloat15;
                            f7 = parseFloat14;
                            f8 = f19;
                            break;
                        case 'S':
                            if (!z4) {
                                f6 = f15;
                                f5 = f14;
                            } else {
                                float f20 = this.vertices[this.vertexCount - 2][0];
                                float f21 = this.vertices[this.vertexCount - 2][1];
                                float f22 = this.vertices[this.vertexCount - 1][0];
                                float f23 = this.vertices[this.vertexCount - 1][1];
                                f5 = f22 + (f22 - f20);
                                f6 = f23 + (f23 - f21);
                            }
                            float parseFloat16 = PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat17 = PApplet.parseFloat(splitTokens[i + 2]);
                            float parseFloat18 = PApplet.parseFloat(splitTokens[i + 3]);
                            float parseFloat19 = PApplet.parseFloat(splitTokens[i + 4]);
                            parsePathCurveto(f5, f6, parseFloat16, parseFloat17, parseFloat18, parseFloat19);
                            i3 = i + 5;
                            f9 = f12;
                            z3 = true;
                            c3 = c;
                            f10 = f11;
                            float f24 = parseFloat19;
                            f7 = parseFloat18;
                            f8 = f24;
                            break;
                        case 'T':
                            if (z4) {
                                float f25 = this.vertices[this.vertexCount - 2][0];
                                float f26 = this.vertices[this.vertexCount - 2][1];
                                float f27 = this.vertices[this.vertexCount - 1][0];
                                float f28 = this.vertices[this.vertexCount - 1][1];
                                f14 = f27 + (f27 - f25);
                                f15 = f28 + (f28 - f26);
                            }
                            float parseFloat20 = PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat21 = PApplet.parseFloat(splitTokens[i + 2]);
                            parsePathQuadto(f14, f15, parseFloat20, parseFloat21);
                            i3 = i + 3;
                            c3 = c;
                            f8 = parseFloat21;
                            f7 = parseFloat20;
                            f9 = f12;
                            z3 = true;
                            f10 = f11;
                            break;
                        case 'V':
                            float parseFloat22 = PApplet.parseFloat(splitTokens[i + 1]);
                            parsePathLineto(f14, parseFloat22);
                            i3 = i + 2;
                            f9 = f12;
                            z3 = z4;
                            c3 = c;
                            f10 = f11;
                            float f29 = parseFloat22;
                            f7 = f14;
                            f8 = f29;
                            break;
                        case 'Z':
                        case 'z':
                            this.close = true;
                            i3 = i + 1;
                            f9 = f12;
                            z3 = z4;
                            c3 = c;
                            f8 = f11;
                            f7 = f12;
                            f10 = f11;
                            break;
                        case 'c':
                            float parseFloat23 = f14 + PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat24 = f15 + PApplet.parseFloat(splitTokens[i + 2]);
                            float parseFloat25 = f14 + PApplet.parseFloat(splitTokens[i + 3]);
                            float parseFloat26 = f15 + PApplet.parseFloat(splitTokens[i + 4]);
                            float parseFloat27 = f14 + PApplet.parseFloat(splitTokens[i + 5]);
                            float parseFloat28 = f15 + PApplet.parseFloat(splitTokens[i + 6]);
                            parsePathCurveto(parseFloat23, parseFloat24, parseFloat25, parseFloat26, parseFloat27, parseFloat28);
                            i3 = i + 7;
                            f9 = f12;
                            z3 = true;
                            c3 = c;
                            f10 = f11;
                            float f30 = parseFloat28;
                            f7 = parseFloat27;
                            f8 = f30;
                            break;
                        case 'h':
                            float parseFloat29 = f14 + PApplet.parseFloat(splitTokens[i + 1]);
                            parsePathLineto(parseFloat29, f15);
                            i3 = i + 2;
                            f9 = f12;
                            z3 = z4;
                            c3 = c;
                            f10 = f11;
                            float f31 = f15;
                            f7 = parseFloat29;
                            f8 = f31;
                            break;
                        case 'l':
                            float parseFloat30 = f14 + PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat31 = f15 + PApplet.parseFloat(splitTokens[i + 2]);
                            parsePathLineto(parseFloat30, parseFloat31);
                            i3 = i + 3;
                            f9 = f12;
                            z3 = z4;
                            c3 = c;
                            f10 = f11;
                            float f32 = parseFloat31;
                            f7 = parseFloat30;
                            f8 = f32;
                            break;
                        case 'm':
                            float parseFloat32 = f14 + PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat33 = f15 + PApplet.parseFloat(splitTokens[i + 2]);
                            parsePathMoveto(parseFloat32, parseFloat33);
                            c3 = 'l';
                            i3 = i + 3;
                            f9 = f12;
                            z3 = z4;
                            f10 = f11;
                            float f33 = parseFloat33;
                            f7 = parseFloat32;
                            f8 = f33;
                            break;
                        case 'q':
                            float parseFloat34 = PApplet.parseFloat(splitTokens[i + 1]) + f14;
                            float parseFloat35 = PApplet.parseFloat(splitTokens[i + 2]) + f15;
                            float parseFloat36 = f14 + PApplet.parseFloat(splitTokens[i + 3]);
                            float parseFloat37 = f15 + PApplet.parseFloat(splitTokens[i + 4]);
                            parsePathQuadto(parseFloat34, parseFloat35, parseFloat36, parseFloat37);
                            i3 = i + 5;
                            f9 = f12;
                            z3 = true;
                            c3 = c;
                            f10 = f11;
                            float f34 = parseFloat37;
                            f7 = parseFloat36;
                            f8 = f34;
                            break;
                        case 's':
                            if (!z4) {
                                f4 = f15;
                                f3 = f14;
                            } else {
                                float f35 = this.vertices[this.vertexCount - 2][0];
                                float f36 = this.vertices[this.vertexCount - 2][1];
                                float f37 = this.vertices[this.vertexCount - 1][0];
                                float f38 = this.vertices[this.vertexCount - 1][1];
                                f3 = f37 + (f37 - f35);
                                f4 = f38 + (f38 - f36);
                            }
                            float parseFloat38 = f14 + PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat39 = f15 + PApplet.parseFloat(splitTokens[i + 2]);
                            float parseFloat40 = f14 + PApplet.parseFloat(splitTokens[i + 3]);
                            float parseFloat41 = f15 + PApplet.parseFloat(splitTokens[i + 4]);
                            parsePathCurveto(f3, f4, parseFloat38, parseFloat39, parseFloat40, parseFloat41);
                            i3 = i + 5;
                            f9 = f12;
                            z3 = true;
                            c3 = c;
                            f10 = f11;
                            float f39 = parseFloat41;
                            f7 = parseFloat40;
                            f8 = f39;
                            break;
                        case 't':
                            if (!z4) {
                                f2 = f15;
                                f = f14;
                            } else {
                                float f40 = this.vertices[this.vertexCount - 2][0];
                                float f41 = this.vertices[this.vertexCount - 2][1];
                                float f42 = this.vertices[this.vertexCount - 1][0];
                                float f43 = this.vertices[this.vertexCount - 1][1];
                                f = f42 + (f42 - f40);
                                f2 = (f43 - f41) + f43;
                            }
                            float parseFloat42 = f14 + PApplet.parseFloat(splitTokens[i + 1]);
                            float parseFloat43 = f15 + PApplet.parseFloat(splitTokens[i + 2]);
                            parsePathQuadto(f, f2, parseFloat42, parseFloat43);
                            i3 = i + 3;
                            f9 = f12;
                            z3 = true;
                            c3 = c;
                            f10 = f11;
                            float f44 = parseFloat43;
                            f7 = parseFloat42;
                            f8 = f44;
                            break;
                        case 'v':
                            float parseFloat44 = f15 + PApplet.parseFloat(splitTokens[i + 1]);
                            parsePathLineto(f14, parseFloat44);
                            i3 = i + 2;
                            f9 = f12;
                            z3 = z4;
                            c3 = c;
                            f10 = f11;
                            float f45 = parseFloat44;
                            f7 = f14;
                            f8 = f45;
                            break;
                        default:
                            String join = PApplet.join(PApplet.subset(splitTokens, 0, i), ",");
                            String join2 = PApplet.join(PApplet.subset(splitTokens, i), ",");
                            System.err.println("parsed: " + join);
                            System.err.println("unparsed: " + join2);
                            if (splitTokens[i].equals("a") || splitTokens[i].equals("A")) {
                                throw new RuntimeException("Sorry, elliptical arc support for SVG files is not yet implemented (See issue 130 for updates)");
                            }
                            throw new RuntimeException("shape command not handled: " + splitTokens[i]);
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void parsePoly(boolean z) {
        this.family = 2;
        this.close = z;
        String string = this.element.getString("points");
        if (string != null) {
            String[] splitTokens = PApplet.splitTokens(string);
            this.vertexCount = splitTokens.length;
            this.vertices = (float[][]) Array.newInstance(Float.TYPE, new int[]{this.vertexCount, 2});
            for (int i = 0; i < this.vertexCount; i++) {
                String[] split = PApplet.split(splitTokens[i], ',');
                this.vertices[i][0] = Float.valueOf(split[0]).floatValue();
                this.vertices[i][1] = Float.valueOf(split[1]).floatValue();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void parseRect() {
        this.kind = 30;
        this.family = 1;
        this.params = new float[]{getFloatWithUnit(this.element, "x"), getFloatWithUnit(this.element, "y"), getFloatWithUnit(this.element, "width"), getFloatWithUnit(this.element, "height")};
    }

    public void print() {
        PApplet.println(this.element.toString());
    }

    /* access modifiers changed from: 0000 */
    public void setColor(String str, boolean z) {
        String str2;
        Shader shader;
        Gradient gradient = null;
        boolean z2 = true;
        int i = 0;
        int i2 = -16777216 & this.fillColor;
        String str3 = "";
        if (str.equals("none")) {
            str2 = str3;
            z2 = false;
            shader = null;
        } else if (str.equals("black")) {
            i = i2;
            str2 = str3;
            shader = null;
        } else if (str.equals("white")) {
            i = i2 | 16777215;
            str2 = str3;
            shader = null;
        } else if (str.startsWith("#")) {
            if (str.length() == 4) {
                str = str.replaceAll("^#(.)(.)(.)$", "#$1$1$2$2$3$3");
            }
            i = i2 | (Integer.parseInt(str.substring(1), 16) & 16777215);
            str2 = str3;
            shader = null;
        } else if (str.startsWith("rgb")) {
            i = i2 | parseRGB(str);
            str2 = str3;
            shader = null;
        } else if (str.startsWith("url(#")) {
            str2 = str.substring(5, str.length() - 1);
            PShape findChild = findChild(str2);
            if (findChild instanceof Gradient) {
                Gradient gradient2 = (Gradient) findChild;
                gradient = gradient2;
                shader = calcGradientPaint(gradient2);
            } else {
                System.err.println("url " + str2 + " refers to unexpected data: " + findChild);
                shader = null;
            }
        } else {
            str2 = str3;
            shader = null;
        }
        if (z) {
            this.fill = z2;
            this.fillColor = i;
            this.fillName = str2;
            this.fillGradient = gradient;
            this.fillGradientPaint = shader;
            return;
        }
        this.stroke = z2;
        this.strokeColor = i;
        this.strokeName = str2;
        this.strokeGradient = gradient;
        this.strokeGradientPaint = shader;
    }

    /* access modifiers changed from: 0000 */
    public void setFillOpacity(String str) {
        this.fillOpacity = PApplet.parseFloat(str);
        this.fillColor = (((int) (this.fillOpacity * 255.0f)) << 24) | (this.fillColor & 16777215);
    }

    /* access modifiers changed from: 0000 */
    public void setOpacity(String str) {
        this.opacity = PApplet.parseFloat(str);
        this.strokeColor = (((int) (this.opacity * 255.0f)) << 24) | (this.strokeColor & 16777215);
        this.fillColor = (((int) (this.opacity * 255.0f)) << 24) | (this.fillColor & 16777215);
    }

    /* access modifiers changed from: 0000 */
    public void setStrokeCap(String str) {
        if (!str.equals("inherit")) {
            if (str.equals("butt")) {
                this.strokeCap = 1;
            } else if (str.equals("round")) {
                this.strokeCap = 2;
            } else if (str.equals("square")) {
                this.strokeCap = 4;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setStrokeJoin(String str) {
        if (!str.equals("inherit")) {
            if (str.equals("miter")) {
                this.strokeJoin = 8;
            } else if (str.equals("round")) {
                this.strokeJoin = 2;
            } else if (str.equals("bevel")) {
                this.strokeJoin = 32;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setStrokeOpacity(String str) {
        this.strokeOpacity = PApplet.parseFloat(str);
        this.strokeColor = (((int) (this.strokeOpacity * 255.0f)) << 24) | (this.strokeColor & 16777215);
    }

    /* access modifiers changed from: 0000 */
    public void setStrokeWeight(String str) {
        this.strokeWeight = parseUnitSize(str);
    }

    /* access modifiers changed from: protected */
    public void styles(PGraphics pGraphics) {
        super.styles(pGraphics);
        if (pGraphics instanceof PGraphicsAndroid2D) {
            PGraphicsAndroid2D pGraphicsAndroid2D = (PGraphicsAndroid2D) pGraphics;
            if (this.strokeGradient != null) {
                pGraphicsAndroid2D.strokePaint.setShader(this.strokeGradientPaint);
            }
            if (this.fillGradient != null) {
                pGraphicsAndroid2D.fillPaint.setShader(this.fillGradientPaint);
            }
        }
    }
}

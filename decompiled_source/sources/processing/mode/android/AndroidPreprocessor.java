package processing.mode.android;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import processing.app.Sketch;
import processing.app.SketchException;
import processing.mode.java.preproc.PdePreprocessor;
import processing.mode.java.preproc.PdePreprocessor.Mode;
import processing.mode.java.preproc.PreprocessorResult;

public class AndroidPreprocessor extends PdePreprocessor {
    String packageName;
    Sketch sketch;

    public AndroidPreprocessor(Sketch sketch2, String packageName2) throws IOException {
        super(sketch2.getName());
        this.sketch = sketch2;
        this.packageName = packageName2;
    }

    public String[] initSketchSize(String code) throws SketchException {
        String[] info = parseSketchSize(code, true);
        if (info == null) {
            System.err.println("More about the size() command on Android can be");
            System.err.println("found here: http://wiki.processing.org/w/Android");
            throw new SketchException("Could not parse the size() command.");
        }
        this.sizeStatement = info[0];
        this.sketchWidth = info[1];
        this.sketchHeight = info[2];
        this.sketchRenderer = info[3];
        return info;
    }

    public PreprocessorResult write(Writer out, String program, String[] codeFolderPackages) throws SketchException, RecognitionException, TokenStreamException {
        if (this.sizeStatement != null) {
            int start = program.indexOf(this.sizeStatement);
            program = program.substring(0, start) + program.substring(this.sizeStatement.length() + start);
        }
        return AndroidPreprocessor.super.write(out, program, codeFolderPackages);
    }

    /* access modifiers changed from: protected */
    public int writeImports(PrintWriter out, List<String> programImports, List<String> codeFolderImports) {
        out.println("package " + this.packageName + ";");
        out.println();
        return AndroidPreprocessor.super.writeImports(out, programImports, codeFolderImports) + 2;
    }

    /* access modifiers changed from: protected */
    public void writeFooter(PrintWriter out, String className) {
        if (this.mode == Mode.STATIC) {
            out.println("noLoop();");
            out.println(this.indent + "}");
        }
        if (this.mode == Mode.STATIC || this.mode == Mode.ACTIVE) {
            out.println();
            if (this.sketchWidth != null) {
                out.println(this.indent + "public int sketchWidth() { return " + this.sketchWidth + "; }");
            }
            if (this.sketchHeight != null) {
                out.println(this.indent + "public int sketchHeight() { return " + this.sketchHeight + "; }");
            }
            if (this.sketchRenderer != null) {
                out.println(this.indent + "public String sketchRenderer() { return " + this.sketchRenderer + "; }");
            }
            out.println("}");
        }
    }
}

package processing.mode.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LogEntry {
    private static final Pattern PARSER = Pattern.compile("^([VDIWEF])/([^\\(\\s]+)\\s*\\(\\s*(\\d+)\\): (.+)$");
    public final String message;
    public final int pid;
    public final Severity severity;
    public final String source;

    public enum Severity {
        Verbose(false),
        Debug(false),
        Info(false),
        Warning(true),
        Error(true),
        Fatal(true);
        
        public final boolean useErrorStream;

        private Severity(boolean useErrorStream2) {
            this.useErrorStream = useErrorStream2;
        }

        /* access modifiers changed from: private */
        public static Severity fromChar(char c) {
            if (c == 'V') {
                return Verbose;
            }
            if (c == 'D') {
                return Debug;
            }
            if (c == 'I') {
                return Info;
            }
            if (c == 'W') {
                return Warning;
            }
            if (c == 'E') {
                return Error;
            }
            if (c == 'F') {
                return Fatal;
            }
            throw new IllegalArgumentException("I don't know how to interpret '" + c + "' as a log severity");
        }
    }

    public LogEntry(String line) {
        Matcher m = PARSER.matcher(line);
        if (!m.matches()) {
            throw new RuntimeException("I can't understand log entry\n" + line);
        }
        this.severity = Severity.fromChar(m.group(1).charAt(0));
        this.source = m.group(2);
        this.pid = Integer.parseInt(m.group(3));
        this.message = m.group(4);
    }

    public String toString() {
        return this.severity + "/" + this.source + "(" + this.pid + "): " + this.message;
    }
}

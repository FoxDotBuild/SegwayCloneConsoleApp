package processing.mode.android;

import java.util.List;

public interface DeviceListener {
    void sketchStopped();

    void stackTrace(List<String> list);
}

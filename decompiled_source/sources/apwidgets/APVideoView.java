package apwidgets;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.VideoView;
import java.util.Vector;
import processing.core.PApplet;

public class APVideoView extends APWidget implements OnCompletionListener, OnPreparedListener, OnErrorListener {
    private static final String TAG = "PVideoView";
    private boolean hasMediaController;
    private boolean looping;
    private boolean prepared;
    private Vector<MediaPlayerTask> tasks;
    private String videoPath;

    class GUIThreadSeekToTask implements Runnable {
        int msec;

        public GUIThreadSeekToTask(int argmsec) {
            this.msec = argmsec;
        }

        public void run() {
            if ((this.msec > APVideoView.this.getCurrentPosition() && ((VideoView) APVideoView.this.view).canSeekForward()) || (this.msec < APVideoView.this.getCurrentPosition() && ((VideoView) APVideoView.this.view).canSeekBackward())) {
                ((VideoView) APVideoView.this.view).seekTo(this.msec);
            }
        }
    }

    interface MediaPlayerTask {
        void doTask();
    }

    class MyVideoView extends VideoView {
        public MyVideoView(Context context) {
            super(context);
        }

        public boolean onKeyDown(int key, KeyEvent keyEvent) {
            APVideoView.this.pApplet.surfaceKeyDown(key, keyEvent);
            return super.onKeyDown(key, keyEvent);
        }
    }

    class SeekToTask implements MediaPlayerTask {
        int msec;

        public SeekToTask(int msec2) {
            this.msec = msec2;
        }

        public void doTask() {
            ((VideoView) APVideoView.this.view).seekTo(this.msec);
        }
    }

    public void setLooping(boolean looping2) {
        this.looping = looping2;
    }

    public APVideoView() {
        this(0, 0, -2, -2, true);
    }

    public APVideoView(boolean hasMediaController2) {
        this(0, 0, -2, -2, hasMediaController2);
    }

    public APVideoView(int x, int y, int width, int height, boolean hasMediaController2) {
        super(x, y, width, height);
        this.hasMediaController = false;
        this.videoPath = null;
        this.looping = false;
        this.prepared = false;
        this.tasks = new Vector<>();
        this.hasMediaController = hasMediaController2;
    }

    public void setVideoPath(String videoPath2) {
        this.videoPath = videoPath2;
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((VideoView) APVideoView.this.view).setVideoPath(APVideoView.this.getVideoPath());
                }
            });
        }
    }

    public void start() {
        if (this.prepared) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((VideoView) APVideoView.this.view).start();
                }
            });
        } else {
            this.tasks.addElement(new MediaPlayerTask() {
                public void doTask() {
                    APVideoView.this.start();
                }
            });
        }
    }

    public void stopPlayBack() {
        if (this.initialized) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((VideoView) APVideoView.this.view).stopPlayback();
                }
            });
        }
    }

    public void pause() {
        if (((VideoView) this.view).isPlaying()) {
            this.pApplet.runOnUiThread(new Runnable() {
                public void run() {
                    ((VideoView) APVideoView.this.view).pause();
                }
            });
        }
    }

    public void seekTo(int msec) {
        if (this.prepared) {
            this.pApplet.runOnUiThread(new GUIThreadSeekToTask(msec));
        } else {
            this.tasks.addElement(new SeekToTask(msec));
        }
    }

    public void init(PApplet pApplet) {
        this.pApplet = pApplet;
        if (this.view == null) {
            this.view = new MyVideoView(pApplet);
        }
        ((VideoView) this.view).setZOrderMediaOverlay(true);
        if (this.hasMediaController) {
            MediaController mediaController = new MediaController(pApplet);
            mediaController.setAnchorView((VideoView) this.view);
            ((VideoView) this.view).setMediaController(mediaController);
        }
        if (this.videoPath != null) {
            ((VideoView) this.view).setVideoPath(this.videoPath);
        }
        ((VideoView) this.view).setOnCompletionListener(this);
        ((VideoView) this.view).setOnPreparedListener(this);
        ((VideoView) this.view).setOnErrorListener(this);
        super.init(pApplet);
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public int getDuration() {
        if (!this.initialized || this.videoPath == null) {
            return 0;
        }
        return ((VideoView) this.view).getDuration();
    }

    public int getCurrentPosition() {
        if (!this.initialized || this.videoPath == null) {
            return 0;
        }
        return ((VideoView) this.view).getCurrentPosition();
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        if (this.looping) {
            ((VideoView) this.view).start();
        }
    }

    public boolean onError(MediaPlayer mediaPlayer, int a, int b) {
        Log.e(TAG, new StringBuilder(String.valueOf(a)).append(" ").append(b).toString());
        return false;
    }

    public void onPrepared(MediaPlayer mp) {
        this.prepared = true;
        for (int i = 0; i < this.tasks.size(); i++) {
            ((MediaPlayerTask) this.tasks.elementAt(i)).doTask();
        }
        this.tasks.removeAllElements();
    }
}

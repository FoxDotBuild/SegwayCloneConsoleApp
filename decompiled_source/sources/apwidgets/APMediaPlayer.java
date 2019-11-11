package apwidgets;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import java.io.IOException;
import java.util.Vector;
import processing.core.PApplet;

public class APMediaPlayer implements OnPreparedListener, OnErrorListener {
    private static final String TAG = "PMediaPlayer";
    private String file;
    private float left;
    private boolean looping;
    /* access modifiers changed from: private */
    public MediaPlayer mediaPlayer;
    private int msec;
    private PApplet pApplet;
    private boolean prepared = false;
    private float right;
    private Vector<MediaPlayerTask> tasks = new Vector<>();

    interface MediaPlayerTask {
        void doTask();
    }

    class SeekToTask implements MediaPlayerTask {
        int msec;

        public SeekToTask(int msec2) {
            this.msec = msec2;
        }

        public void doTask() {
            APMediaPlayer.this.mediaPlayer.seekTo(this.msec);
        }
    }

    class SetLoopingTask implements MediaPlayerTask {
        boolean looping;

        public SetLoopingTask(boolean looping2) {
            this.looping = looping2;
        }

        public void doTask() {
            APMediaPlayer.this.mediaPlayer.setLooping(this.looping);
        }
    }

    class SetVolumeTask implements MediaPlayerTask {
        float left;
        float right;

        public SetVolumeTask(float left2, float right2) {
            this.left = left2;
            this.right = right2;
        }

        public void doTask() {
            APMediaPlayer.this.mediaPlayer.setVolume(this.left, this.right);
        }
    }

    private String getFile() {
        return this.file;
    }

    private APMediaPlayer getThis() {
        return this;
    }

    public APMediaPlayer(PApplet pApplet2) {
        this.pApplet = pApplet2;
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setOnPreparedListener(getThis());
        this.mediaPlayer.setOnErrorListener(getThis());
    }

    public void setMediaFile(String file2) {
        this.file = file2;
        this.mediaPlayer.reset();
        this.prepared = false;
        try {
            AssetFileDescriptor afd = this.pApplet.getAssets().openFd(getFile());
            this.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            this.mediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void start() {
        if (this.prepared) {
            this.mediaPlayer.start();
        } else {
            this.tasks.addElement(new MediaPlayerTask() {
                public void doTask() {
                    APMediaPlayer.this.mediaPlayer.start();
                }
            });
        }
    }

    public void pause() {
        if (!this.prepared) {
            this.tasks.addElement(new MediaPlayerTask() {
                public void doTask() {
                    APMediaPlayer.this.mediaPlayer.pause();
                }
            });
        } else if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
        }
    }

    private int getMsec() {
        return this.msec;
    }

    public void seekTo(int msec2) {
        this.msec = msec2;
        if (this.prepared) {
            this.mediaPlayer.seekTo(getMsec());
        } else {
            this.tasks.addElement(new SeekToTask(msec2));
        }
    }

    public void release() {
        this.mediaPlayer.release();
    }

    private boolean getLooping() {
        return this.looping;
    }

    public void setLooping(boolean looping2) {
        this.looping = looping2;
        if (this.prepared) {
            this.mediaPlayer.setLooping(getLooping());
        } else {
            this.tasks.addElement(new SetLoopingTask(looping2));
        }
    }

    private float getLeft() {
        return this.left;
    }

    private float getRight() {
        return this.right;
    }

    public void setVolume(float left2, float right2) {
        this.left = left2;
        this.right = right2;
        if (this.prepared) {
            this.mediaPlayer.setVolume(getLeft(), getRight());
        } else {
            this.tasks.addElement(new SetVolumeTask(left2, right2));
        }
    }

    public int getCurrentPosition() {
        if (this.prepared) {
            return this.mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (this.prepared) {
            return this.mediaPlayer.getDuration();
        }
        return 0;
    }

    public void onPrepared(MediaPlayer mp) {
        this.prepared = true;
        for (int i = 0; i < this.tasks.size(); i++) {
            ((MediaPlayerTask) this.tasks.elementAt(i)).doTask();
        }
        this.tasks.removeAllElements();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        Log.i(TAG, "Finilized");
        this.mediaPlayer.release();
    }

    public boolean onError(MediaPlayer mediaPlayer2, int a, int b) {
        Log.e(TAG, new StringBuilder(String.valueOf(a)).append(" ").append(b).toString());
        return false;
    }
}

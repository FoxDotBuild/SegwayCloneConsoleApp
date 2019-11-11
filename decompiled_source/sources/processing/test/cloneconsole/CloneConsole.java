package processing.test.cloneconsole;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import apwidgets.APButton;
import apwidgets.APWidget;
import apwidgets.APWidgetContainer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class CloneConsole extends PApplet {
    public static int ALIGN_LEFT = 10;
    public static final float ALPHA = 0.5f;
    public static int ALPHA_X = 180;
    public static int ALPHA_Y = 990;
    public static int BATTERY_MIN = 13;
    public static int BOARD_ANGLE = 1;
    public static int BOARD_ANGLE_X = 360;
    public static int BOARD_ANGLE_Y = 990;
    public static int BUTTON_CALIBRATE_WIDTH = 300;
    public static int BUTTON_CALIBRATE_Y = 440;
    public static int BUTTON_HEIGHT = 80;
    public static int BUTTON_HEIGHT2 = 160;
    public static int BUTTON_LOCK_WIDTH = 300;
    public static int BUTTON_LOCK_Y = 690;
    public static int BUTTON_SAVE_WIDTH = 150;
    public static int BUTTON_SAVE_Y = 1100;
    public static int BUTTON_SWAP_HEIGHT = 100;
    public static int BUTTON_SWAP_WIDTH = 100;
    public static int BUTTON_SWAP_Y = 1080;
    public static int CALIBRATE_VIEW = 2;
    public static int CALIBRATE_X = 360;
    public static int CALIBRATE_Y = 540;
    public static int CURRENT1 = 10;
    public static int CURRENT1_X = 180;
    public static int CURRENT1_Y = 870;
    public static int CURRENT2 = 11;
    public static int CURRENT2_X = 180;
    public static int CURRENT2_Y = 990;
    public static float CURRENT_STAGE_1 = 500.0f;
    public static float CURRENT_STAGE_2 = 1000.0f;
    public static float CURRENT_STAGE_3 = 2000.0f;
    public static int DATA_VIEW = 0;
    public static int JOYSTICK_VIEW = 1;
    public static int LENGTH_GRAPH = PConstants.OVERLAY;
    public static int LMOTOR = 5;
    public static int LMOTOR_X = 180;
    public static int LMOTOR_Y = 390;
    public static int MARGIN_X = 60;
    public static int MARGIN_Y = 60;
    public static int MAX_CURRENT = 4000;
    public static int MAX_MOTOR = PImage.BLUE_MASK;
    public static int MAX_TEMP = 80;
    public static int MAX_VOLTAGE = 30;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 1;
    public static float MIN_BATTERY_1 = 22.0f;
    public static float MIN_BATTERY_2 = 21.5f;
    public static float MIN_BATTERY_3 = 21.0f;
    public static float MOTOR_STAGE_1 = 150.0f;
    public static float MOTOR_STAGE_2 = 200.0f;
    public static float MOTOR_STAGE_3 = 230.0f;
    public static int OFFSET_GRAPH = 50;
    public static int OFFSET_VALUE = 50;
    public static int PARAM1 = 14;
    public static int PARAM2 = 15;
    public static int PIDD = 8;
    public static int PIDD_X = 180;
    public static int PIDD_Y = 750;
    public static int PIDP = 7;
    public static int PIDP_X = 180;
    public static int PIDP_Y = 630;
    public static int RMOTOR = 6;
    public static int RMOTOR_X = 180;
    public static int RMOTOR_Y = 510;
    public static final int SAMPLES = 10;
    public static int SPEED_VIEW = 3;
    public static int STATUS = 9;
    public static int STATUS_CALIBRATE_FAILED = 8;
    public static int STATUS_LOCK = 5;
    public static int STATUS_LOCK_FAILED = 7;
    public static int STATUS_LOCK_WAIT = 6;
    public static int STATUS_NEW = 0;
    public static int STATUS_NEXT = 3;
    public static int STATUS_OLD = 4;
    public static int STATUS_SAVE = 1;
    public static int STATUS_WAIT = 2;
    public static int STEER_ANGLE = 2;
    public static int STEER_ANGLE_X = 260;
    public static int STEER_ANGLE_Y = 870;
    public static int STROKE = 20;
    public static int TEMP1 = 3;
    public static int TEMP1_X = 180;
    public static int TEMP1_Y = 150;
    public static int TEMP2 = 4;
    public static int TEMP2_X = 180;
    public static int TEMP2_Y = 270;
    public static int TEMP_MAX = 12;
    public static float TEMP_STAGE_1 = 25.0f;
    public static float TEMP_STAGE_2 = 35.0f;
    public static float TEMP_STAGE_3 = 45.0f;
    public static int VOLTAGE = 0;
    public static int VOLTAGE_X = 180;
    public static int VOLTAGE_Y = 30;
    static int calibrate_status = STATUS_OLD;
    static boolean joystick_ack = true;
    static boolean keyReleased = true;
    static int s_mouseX;
    static int s_mouseY;
    static float scaleX;
    static float scaleY;
    static boolean sent = false;
    static int view = DATA_VIEW;
    boolean BTisConnected = false;
    float alpha = 0.5f;
    float aveSpeed = 0.0f;

    /* renamed from: bg */
    public int[] f95bg = {PImage.BLUE_MASK, PImage.BLUE_MASK, 0};
    APButton blueButton;
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    int buttonHeight = 0;
    String buttonText = "";
    int buttonWidth = 0;
    BroadcastReceiver checkIsConnected = new myOwnBroadcastReceiver();
    short[] data = {2550, 0, -1100, 35, 35, -30, 30, 70, 30, 0, 0, 0, 0, 0, -1, -1};
    int data_pos = 0;
    float fineCurrentAccuracy = 0.0f;
    float fineCurrentLatitude = 0.0f;
    float fineCurrentLongitude = 0.0f;
    String fineCurrentProvider = "";
    float fineCurrentSpeed = 0.0f;
    MyFineLocationListener fineLocationListener;
    boolean foundDevice = false;
    int gap = 10;
    APButton greenButton;
    short joystick_x = 0;
    short joystick_y = 0;
    boolean lastRiderFlag = true;
    LocationManager locationManager;
    boolean locked = false;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    byte[] readBuf = (byte[]) msg.obj;
                    CloneConsole.this.readMessage = new String(readBuf, 0, msg.arg1);
                    return;
                default:
                    return;
            }
        }
    };
    BroadcastReceiver myDiscoverer = new myOwnBroadcastReceiver();

    /* renamed from: n */
    int f96n = 4;
    APButton offButton;
    int pid_status = STATUS_WAIT;
    short pidd_new = 0;
    short pidp_new = 0;
    String readMessage = "";
    APButton redButton;
    APButton saveButton;
    public BluetoothSocket scSocket;
    APButton screenButton;
    SendReceiveBytes sendReceiveBT = null;
    String serverName = "ArduinoBasicsServer";
    APWidgetContainer widgetContainer;

    public class ConnectToBluetooth implements Runnable {
        private BluetoothDevice btShield;
        private BluetoothSocket mySocket = null;
        private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        public ConnectToBluetooth(BluetoothDevice bluetoothShield) {
            this.btShield = bluetoothShield;
            try {
                this.mySocket = this.btShield.createRfcommSocketToServiceRecord(this.uuid);
            } catch (IOException e) {
                Log.e("ConnectToBluetooth", "Error with Socket");
            }
        }

        public void run() {
            CloneConsole.this.bluetooth.cancelDiscovery();
            try {
                this.mySocket.connect();
                CloneConsole.this.scSocket = this.mySocket;
            } catch (IOException e) {
                Log.e("ConnectToBluetooth", "Error with Socket Connection");
                try {
                    this.mySocket.close();
                } catch (IOException e2) {
                }
            }
        }

        public BluetoothSocket getSocket() {
            return this.mySocket;
        }

        public void cancel() {
            try {
                this.mySocket.close();
            } catch (IOException e) {
            }
        }
    }

    class MyFineLocationListener implements LocationListener {
        MyFineLocationListener() {
        }

        public void onLocationChanged(Location location) {
            CloneConsole.this.fineCurrentLatitude = (float) location.getLatitude();
            CloneConsole.this.fineCurrentLongitude = (float) location.getLongitude();
            CloneConsole.this.fineCurrentAccuracy = location.getAccuracy();
            CloneConsole.this.fineCurrentSpeed = location.getSpeed() * 3.6f;
            CloneConsole.this.fineCurrentProvider = location.getProvider();
            CloneConsole.this.aveSpeed += CloneConsole.this.alpha * (CloneConsole.this.fineCurrentSpeed - CloneConsole.this.aveSpeed);
        }

        public void onProviderDisabled(String provider) {
            CloneConsole.this.fineCurrentProvider = "";
        }

        public void onProviderEnabled(String provider) {
            CloneConsole.this.fineCurrentProvider = provider;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private class SendReceiveBytes implements Runnable {
        String TAG = "SendReceiveBytes";
        public DataInputStream btDataInputStream = null;
        public DataOutputStream btDataOutputStream = null;
        private InputStream btInputStream = null;
        private OutputStream btOutputStream = null;
        private BluetoothSocket btSocket;

        public SendReceiveBytes(BluetoothSocket socket) {
            this.btSocket = socket;
            try {
                this.btInputStream = this.btSocket.getInputStream();
                this.btOutputStream = this.btSocket.getOutputStream();
                this.btDataInputStream = new DataInputStream(this.btInputStream);
                this.btDataOutputStream = new DataOutputStream(this.btOutputStream);
                CloneConsole.this.BTisConnected = true;
                CloneConsole.this.changeBackground(PImage.BLUE_MASK, PImage.BLUE_MASK, PImage.BLUE_MASK);
            } catch (IOException e) {
                Log.e(this.TAG, "Error when getting input or output Stream");
            }
        }

        public void run() {
            while (true) {
                if (CloneConsole.this.BTisConnected) {
                    CloneConsole.this.read_bluetooth_data();
                    CloneConsole.this.write_bluetooth_data();
                }
            }
        }

        public int read() {
            try {
                return this.btDataInputStream.read();
            } catch (IOException e) {
                Log.e(this.TAG, "Error when reading from btDataInputStream");
                return -1;
            }
        }

        public int available() {
            try {
                return this.btDataInputStream.available();
            } catch (IOException e) {
                Log.e(this.TAG, "Error when check available");
                return -1;
            }
        }

        public short readShort() {
            try {
                return this.btDataInputStream.readShort();
            } catch (IOException e) {
                Log.e(this.TAG, "Error when reading short from btDataInputStream");
                return -1;
            }
        }

        public void write(int value) {
            try {
                this.btDataOutputStream.write(value);
            } catch (IOException e) {
                Log.e(this.TAG, "Error when writing to btOutputStream");
            }
        }

        public void writeShort(short data) {
            try {
                this.btDataOutputStream.writeShort(data);
            } catch (IOException e) {
                Log.e(this.TAG, "Error when writing to btOutputStream");
            }
        }

        public void cancel() {
            try {
                this.btSocket.close();
            } catch (IOException e) {
                Log.e(this.TAG, "Error when closing the btSocket");
            }
        }
    }

    public class myOwnBroadcastReceiver extends BroadcastReceiver {
        ConnectToBluetooth connectBT;

        public myOwnBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                String discoveredDeviceName = intent.getStringExtra("android.bluetooth.device.extra.NAME");
                CloneConsole.this.ToastMaster("Discovered: " + discoveredDeviceName);
                BluetoothDevice discoveredDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                switch (discoveredDevice.getBondState()) {
                    case 10:
                        String str = "BOND_NONE";
                        break;
                    case 11:
                        String mybondState = "BOND_BONDING";
                        break;
                    case 12:
                        String mybondState2 = "BOND_BONDED";
                        break;
                    default:
                        String str2 = "INVALID BOND STATE";
                        break;
                }
                CloneConsole.this.changeBackground(0, PImage.BLUE_MASK, PImage.BLUE_MASK);
                if (discoveredDeviceName.equals("SEGWAY") || discoveredDeviceName.equals("linvor") || discoveredDeviceName.equals("HC-06")) {
                    CloneConsole.this.unregisterReceiver(CloneConsole.this.myDiscoverer);
                    this.connectBT = new ConnectToBluetooth(discoveredDevice);
                    new Thread(this.connectBT).start();
                }
            }
            if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
                CloneConsole.this.changeBackground(0, PImage.BLUE_MASK, 0);
                CloneConsole.this.ToastMaster("CONNECTED!");
                do {
                } while (CloneConsole.this.scSocket == null);
                if (CloneConsole.this.scSocket != null) {
                    CloneConsole.this.sendReceiveBT = new SendReceiveBytes(CloneConsole.this.scSocket);
                    new Thread(CloneConsole.this.sendReceiveBT).start();
                }
            }
        }
    }

    public void changeView() {
        if (calibrate_status != STATUS_WAIT) {
            if (view == SPEED_VIEW) {
                view = JOYSTICK_VIEW;
            } else if (view == JOYSTICK_VIEW) {
                view = CALIBRATE_VIEW;
            } else if (view == CALIBRATE_VIEW) {
                view = DATA_VIEW;
            } else if (view == DATA_VIEW) {
                view = SPEED_VIEW;
            }
            calibrate_status = STATUS_OLD;
        }
    }

    public void keyPressed() {
        if (this.key == 65535 && this.keyCode != 4 && this.keyCode == 82) {
            changeView();
        }
    }

    public void moving_horizon(float steer_angle, float board_angle) {
        float steer_delta = sin((3.1415927f * steer_angle) / 180.0f) * 50.0f;
        float board_delta = sin((3.1415927f * board_angle) / 180.0f) * 50.0f;
        noStroke();
        fill(30.0f, 143.0f, 198.0f);
        quad((float) (STEER_ANGLE_X + 200), (float) STEER_ANGLE_Y, (float) STEER_ANGLE_X, (float) STEER_ANGLE_Y, (float) STEER_ANGLE_X, (((float) (STEER_ANGLE_Y + 100)) - steer_delta) + board_delta, (float) (STEER_ANGLE_X + 200), ((float) (STEER_ANGLE_Y + 100)) + steer_delta + board_delta);
        fill(183.0f, 113.0f, 28.0f);
        quad((float) (STEER_ANGLE_X + 200), (float) (STEER_ANGLE_Y + 200), (float) STEER_ANGLE_X, (float) (STEER_ANGLE_Y + 200), (float) STEER_ANGLE_X, (((float) (STEER_ANGLE_Y + 100)) - steer_delta) + board_delta, (float) (STEER_ANGLE_X + 200), ((float) (STEER_ANGLE_Y + 100)) + steer_delta + board_delta);
        stroke(250);
        strokeWeight(2.0f);
        line((float) STEER_ANGLE_X, (((float) (STEER_ANGLE_Y + 100)) - steer_delta) + board_delta, (float) (STEER_ANGLE_X + 200), ((float) (STEER_ANGLE_Y + 100)) + steer_delta + board_delta);
        line((float) ((STEER_ANGLE_X + 100) - 35), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 78), (float) (STEER_ANGLE_X + 100 + 35), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 78));
        line((float) ((STEER_ANGLE_X + 100) - 10), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 65), (float) (STEER_ANGLE_X + 100 + 10), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 65));
        line((float) ((STEER_ANGLE_X + 100) - 30), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 52), (float) (STEER_ANGLE_X + 100 + 30), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 52));
        line((float) ((STEER_ANGLE_X + 100) - 10), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 39), (float) (STEER_ANGLE_X + 100 + 10), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 39));
        line((float) ((STEER_ANGLE_X + 100) - 25), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 26), (float) (STEER_ANGLE_X + 100 + 25), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 26));
        line((float) ((STEER_ANGLE_X + 100) - 10), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 13), (float) (STEER_ANGLE_X + 100 + 10), ((float) (STEER_ANGLE_Y + 100)) + board_delta + ((float) 13));
        line((float) ((STEER_ANGLE_X + 100) - 10), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 13), (float) (STEER_ANGLE_X + 100 + 10), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 13));
        line((float) ((STEER_ANGLE_X + 100) - 25), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 26), (float) (STEER_ANGLE_X + 100 + 25), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 26));
        line((float) ((STEER_ANGLE_X + 100) - 10), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 39), (float) (STEER_ANGLE_X + 100 + 10), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 39));
        line((float) ((STEER_ANGLE_X + 100) - 30), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 52), (float) (STEER_ANGLE_X + 100 + 30), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 52));
        line((float) ((STEER_ANGLE_X + 100) - 10), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 65), (float) (STEER_ANGLE_X + 100 + 10), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 65));
        line((float) ((STEER_ANGLE_X + 100) - 35), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 78), (float) (STEER_ANGLE_X + 100 + 35), (((float) (STEER_ANGLE_Y + 100)) + board_delta) - ((float) 78));
    }

    public void frame() {
        fill(183.0f, 113.0f, 28.0f);
        strokeWeight(1.0f);
        stroke(25);
        beginShape();
        vertex((float) (STEER_ANGLE_X - 25), (float) (STEER_ANGLE_Y + 100));
        vertex((float) (STEER_ANGLE_X - 1), (float) (STEER_ANGLE_Y + 100));
        bezierVertex((float) (STEER_ANGLE_X - 1), (float) (STEER_ANGLE_Y + 130), (float) (STEER_ANGLE_X + 20), (float) (STEER_ANGLE_Y + 200), (float) (STEER_ANGLE_X + 100), (float) (STEER_ANGLE_Y + 200));
        vertex((float) (STEER_ANGLE_X + 100), (float) (STEER_ANGLE_Y + 220));
        vertex((float) (STEER_ANGLE_X - 25), (float) (STEER_ANGLE_Y + 220));
        endShape();
        beginShape();
        vertex((float) (STEER_ANGLE_X + 225), (float) (STEER_ANGLE_Y + 100));
        vertex((float) (STEER_ANGLE_X + 200), (float) (STEER_ANGLE_Y + 100));
        bezierVertex((float) (STEER_ANGLE_X + 201), (float) (STEER_ANGLE_Y + 130), (float) (STEER_ANGLE_X + 180), (float) (STEER_ANGLE_Y + 200), (float) (STEER_ANGLE_X + 100), (float) (STEER_ANGLE_Y + 200));
        vertex((float) (STEER_ANGLE_X + 100), (float) (STEER_ANGLE_Y + 220));
        vertex((float) (STEER_ANGLE_X + 225), (float) (STEER_ANGLE_Y + 220));
        endShape();
        fill(30.0f, 143.0f, 198.0f);
        beginShape();
        vertex((float) (STEER_ANGLE_X - 25), (float) (STEER_ANGLE_Y + 100));
        vertex((float) (STEER_ANGLE_X - 1), (float) (STEER_ANGLE_Y + 100));
        bezierVertex((float) (STEER_ANGLE_X - 1), (float) (STEER_ANGLE_Y + 70), (float) (STEER_ANGLE_X + 20), (float) STEER_ANGLE_Y, (float) (STEER_ANGLE_X + 100), (float) STEER_ANGLE_Y);
        vertex((float) (STEER_ANGLE_X + 100), (float) (STEER_ANGLE_Y - 20));
        vertex((float) (STEER_ANGLE_X - 25), (float) (STEER_ANGLE_Y - 20));
        endShape();
        beginShape();
        vertex((float) (STEER_ANGLE_X + 225), (float) (STEER_ANGLE_Y + 100));
        vertex((float) (STEER_ANGLE_X + 200), (float) (STEER_ANGLE_Y + 100));
        bezierVertex((float) (STEER_ANGLE_X + 201), (float) (STEER_ANGLE_Y + 70), (float) (STEER_ANGLE_X + 180), (float) STEER_ANGLE_Y, (float) (STEER_ANGLE_X + 100), (float) STEER_ANGLE_Y);
        vertex((float) (STEER_ANGLE_X + 100), (float) (STEER_ANGLE_Y - 20));
        vertex((float) (STEER_ANGLE_X + 225), (float) (STEER_ANGLE_Y - 20));
        endShape();
        fill(250);
        noStroke();
        triangle((float) (STEER_ANGLE_X + 100), (float) STEER_ANGLE_Y, (float) (STEER_ANGLE_X + 90), (float) (STEER_ANGLE_Y - 20), (float) (STEER_ANGLE_X + 110), (float) (STEER_ANGLE_Y - 20));
        stroke(250);
        strokeWeight(4.0f);
        line((float) (STEER_ANGLE_X - 25), (float) (STEER_ANGLE_Y + 100), (float) (STEER_ANGLE_X - 1), (float) (STEER_ANGLE_Y + 100));
        line((float) (STEER_ANGLE_X + 201), (float) (STEER_ANGLE_Y + 100), (float) (STEER_ANGLE_X + 225), (float) (STEER_ANGLE_Y + 100));
    }

    public void data_view_with_attitude() {
        int pidp_scaled;
        int pidd_scaled;
        int pidd;
        int pidp;
        float voltage = ((float) this.data[VOLTAGE]) / 100.0f;
        float board_angle = ((float) this.data[BOARD_ANGLE]) / 100.0f;
        float steer_angle = ((float) this.data[STEER_ANGLE]) / 100.0f;
        short temp1 = this.data[TEMP1];
        short temp2 = this.data[TEMP2];
        short lmotor = this.data[LMOTOR];
        short rmotor = this.data[RMOTOR];
        boolean riderFlag = (this.data[PARAM1] & 32768) > 0;
        scaleX = ((float) this.width) / 720.0f;
        scaleY = ((float) this.height) / 1184.0f;
        scale(scaleX, scaleY);
        textAlign(21);
        if (this.pid_status == STATUS_NEW || riderFlag != this.lastRiderFlag) {
            this.pidp_new = this.data[PIDP];
            this.pidd_new = this.data[PIDD];
            this.pid_status = STATUS_OLD;
        }
        int pidp2 = this.pidp_new;
        int pidd2 = this.pidd_new;
        float pidp_f = ((float) pidp2) / 100.0f;
        float pidd_f = ((float) pidd2) / 100.0f;
        if (pidp2 < 100) {
            pidp_scaled = pidp2 * 2;
        } else {
            pidp_scaled = ((int) sqrt((float) ((pidp2 * 20) - 2000))) + 200;
        }
        if (pidd2 < 100) {
            pidd_scaled = pidd2 * 2;
        } else {
            pidd_scaled = ((int) sqrt((float) ((pidd2 * 20) - 2000))) + 200;
        }
        textSize(25.0f);
        fill(64.0f, 64.0f, 64.0f);
        text("BATTERY VOLTAGE", (float) ALIGN_LEFT, (float) VOLTAGE_Y);
        text("TILT", (float) ALIGN_LEFT, (float) BOARD_ANGLE_Y);
        text("TURN", (float) ALIGN_LEFT, (float) STEER_ANGLE_Y);
        text("TEMPERATURE 1", (float) ALIGN_LEFT, (float) TEMP1_Y);
        text("TEMPERATURE 2", (float) ALIGN_LEFT, (float) TEMP2_Y);
        text("LEFT MOTOR", (float) ALIGN_LEFT, (float) LMOTOR_Y);
        text("RIGHT MOTOR", (float) ALIGN_LEFT, (float) RMOTOR_Y);
        text("P", (float) ALIGN_LEFT, (float) PIDP_Y);
        text("D", (float) ALIGN_LEFT, (float) PIDD_Y);
        textSize(50.0f);
        fill(0.0f, 0.0f, 0.0f);
        text(str(voltage), (float) ALIGN_LEFT, (float) (VOLTAGE_Y + OFFSET_VALUE));
        text(str(board_angle), (float) ALIGN_LEFT, (float) (BOARD_ANGLE_Y + OFFSET_VALUE));
        text(str(steer_angle), (float) ALIGN_LEFT, (float) (STEER_ANGLE_Y + OFFSET_VALUE));
        text(str((int) temp1), (float) ALIGN_LEFT, (float) (TEMP1_Y + OFFSET_VALUE));
        text(str((int) temp2), (float) ALIGN_LEFT, (float) (TEMP2_Y + OFFSET_VALUE));
        text(str((int) lmotor), (float) ALIGN_LEFT, (float) (LMOTOR_Y + OFFSET_VALUE));
        text(str((int) rmotor), (float) ALIGN_LEFT, (float) (RMOTOR_Y + OFFSET_VALUE));
        text(str(pidp_f), (float) ALIGN_LEFT, (float) (PIDP_Y + OFFSET_VALUE));
        text(str(pidd_f), (float) ALIGN_LEFT, (float) (PIDD_Y + OFFSET_VALUE));
        moving_horizon(steer_angle, board_angle);
        frame();
        strokeWeight((float) STROKE);
        stroke(64.0f, 64.0f, 64.0f);
        line((float) VOLTAGE_X, (float) ((VOLTAGE_Y + OFFSET_GRAPH) - STROKE), (float) (VOLTAGE_X + LENGTH_GRAPH), (float) ((VOLTAGE_Y + OFFSET_GRAPH) - STROKE));
        if (voltage < MIN_BATTERY_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (voltage < MIN_BATTERY_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (voltage < MIN_BATTERY_1) {
            stroke(255.0f, 255.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) VOLTAGE_X, (float) ((VOLTAGE_Y + OFFSET_GRAPH) - STROKE), ((float) VOLTAGE_X) + ((((float) LENGTH_GRAPH) * voltage) / ((float) MAX_VOLTAGE)), (float) ((VOLTAGE_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) TEMP1_X, (float) ((TEMP1_Y + OFFSET_GRAPH) - STROKE), (float) (TEMP1_X + LENGTH_GRAPH), (float) ((TEMP1_Y + OFFSET_GRAPH) - STROKE));
        if (((float) temp1) > TEMP_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) temp1) > TEMP_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) temp1) > TEMP_STAGE_1) {
            stroke(255.0f, 228.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) TEMP1_X, (float) ((TEMP1_Y + OFFSET_GRAPH) - STROKE), (float) (TEMP1_X + ((LENGTH_GRAPH * temp1) / MAX_TEMP)), (float) ((TEMP1_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) TEMP2_X, (float) ((TEMP2_Y + OFFSET_GRAPH) - STROKE), (float) (TEMP2_X + LENGTH_GRAPH), (float) ((TEMP2_Y + OFFSET_GRAPH) - STROKE));
        if (((float) temp2) > TEMP_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) temp2) > TEMP_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) temp2) > TEMP_STAGE_1) {
            stroke(255.0f, 228.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) TEMP2_X, (float) ((TEMP2_Y + OFFSET_GRAPH) - STROKE), (float) (TEMP2_X + ((LENGTH_GRAPH * temp2) / MAX_TEMP)), (float) ((TEMP2_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) LMOTOR_X, (float) ((LMOTOR_Y + OFFSET_GRAPH) - STROKE), (float) (LMOTOR_X + LENGTH_GRAPH), (float) ((LMOTOR_Y + OFFSET_GRAPH) - STROKE));
        strokeWeight(1.0f);
        line((float) (LMOTOR_X + (LENGTH_GRAPH / 2)), (float) ((LMOTOR_Y + OFFSET_GRAPH) - (STROKE * 2)), (float) (LMOTOR_X + (LENGTH_GRAPH / 2)), (float) (LMOTOR_Y + OFFSET_GRAPH));
        strokeWeight((float) STROKE);
        if (((float) abs((int) lmotor)) > MOTOR_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) abs((int) lmotor)) > MOTOR_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) abs((int) lmotor)) > MOTOR_STAGE_1) {
            stroke(255.0f, 255.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) (LMOTOR_X + (LENGTH_GRAPH / 2)), (float) ((LMOTOR_Y + OFFSET_GRAPH) - STROKE), (float) (LMOTOR_X + (LENGTH_GRAPH / 2) + (((LENGTH_GRAPH * lmotor) / MAX_MOTOR) / 2)), (float) ((LMOTOR_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) RMOTOR_X, (float) ((RMOTOR_Y + OFFSET_GRAPH) - STROKE), (float) (RMOTOR_X + LENGTH_GRAPH), (float) ((RMOTOR_Y + OFFSET_GRAPH) - STROKE));
        strokeWeight(1.0f);
        line((float) (RMOTOR_X + (LENGTH_GRAPH / 2)), (float) ((RMOTOR_Y + OFFSET_GRAPH) - (STROKE * 2)), (float) (RMOTOR_X + (LENGTH_GRAPH / 2)), (float) (RMOTOR_Y + OFFSET_GRAPH));
        strokeWeight((float) STROKE);
        if (((float) abs((int) rmotor)) > MOTOR_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) abs((int) rmotor)) > MOTOR_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) abs((int) rmotor)) > MOTOR_STAGE_1) {
            stroke(255.0f, 255.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) (RMOTOR_X + (LENGTH_GRAPH / 2)), (float) ((RMOTOR_Y + OFFSET_GRAPH) - STROKE), (float) (RMOTOR_X + (LENGTH_GRAPH / 2) + (((LENGTH_GRAPH * rmotor) / MAX_MOTOR) / 2)), (float) ((RMOTOR_Y + OFFSET_GRAPH) - STROKE));
        if (this.mousePressed) {
            s_mouseX = (int) (((float) this.mouseX) / scaleX);
            s_mouseY = (int) (((float) this.mouseY) / scaleY);
            if (s_mouseX >= (PIDP_X + pidp_scaled) - MARGIN_X && s_mouseX <= PIDP_X + pidp_scaled + MARGIN_X && s_mouseY >= ((PIDP_Y + OFFSET_GRAPH) - STROKE) - MARGIN_Y && s_mouseY <= ((PIDP_Y + OFFSET_GRAPH) - STROKE) + MARGIN_Y) {
                int pidp3 = s_mouseX - PIDP_X;
                if (pidp3 < 0) {
                    pidp = 0;
                } else if (pidp3 < 200) {
                    pidp = pidp3 / 2;
                } else {
                    if (pidp3 > LENGTH_GRAPH) {
                        pidp3 = LENGTH_GRAPH;
                    }
                    pidp = (((pidp3 - 200) * (pidp3 - 200)) / 20) + 100;
                }
                this.pidp_new = (short) pidp;
            }
            if (s_mouseX >= (PIDD_X + pidd_scaled) - MARGIN_X && s_mouseX <= PIDD_X + pidd_scaled + MARGIN_X && s_mouseY >= ((PIDD_Y + OFFSET_GRAPH) - STROKE) - MARGIN_Y && s_mouseY <= ((PIDD_Y + OFFSET_GRAPH) - STROKE) + MARGIN_Y) {
                int pidd3 = s_mouseX - PIDD_X;
                if (pidd3 < 0) {
                    pidd = 0;
                } else if (pidd3 < 200) {
                    pidd = pidd3 / 2;
                } else {
                    if (pidd3 > LENGTH_GRAPH) {
                        pidd3 = LENGTH_GRAPH;
                    }
                    pidd = (((pidd3 - 200) * (pidd3 - 200)) / 20) + 100;
                }
                this.pidd_new = (short) pidd;
            }
        }
        stroke(0.0f, 255.0f, 0.0f);
        line((float) PIDP_X, (float) ((PIDP_Y + OFFSET_GRAPH) - STROKE), (float) (PIDP_X + LENGTH_GRAPH), (float) ((PIDP_Y + OFFSET_GRAPH) - STROKE));
        stroke(255.0f, 165.0f, 0.0f);
        line((float) (PIDP_X + pidp_scaled), (float) ((PIDP_Y + OFFSET_GRAPH) - (STROKE * 2)), (float) (PIDP_X + pidp_scaled), (float) (PIDP_Y + OFFSET_GRAPH));
        stroke(0.0f, 255.0f, 0.0f);
        line((float) PIDD_X, (float) ((PIDD_Y + OFFSET_GRAPH) - STROKE), (float) (PIDD_X + LENGTH_GRAPH), (float) ((PIDD_Y + OFFSET_GRAPH) - STROKE));
        stroke(255.0f, 165.0f, 0.0f);
        line((float) (PIDD_X + pidd_scaled), (float) ((PIDD_Y + OFFSET_GRAPH) - (STROKE * 2)), (float) (PIDD_X + pidd_scaled), (float) (PIDD_Y + OFFSET_GRAPH));
        strokeWeight(1.0f);
        fill((int) PImage.BLUE_MASK);
        if (this.mousePressed) {
            s_mouseX = (int) (((float) this.mouseX) / scaleX);
            s_mouseY = (int) (((float) this.mouseY) / scaleY);
            if (s_mouseX > (720 - BUTTON_SAVE_WIDTH) / 2 && s_mouseX < (BUTTON_SAVE_WIDTH + 720) / 2 && s_mouseY > BUTTON_SAVE_Y && s_mouseY < BUTTON_SAVE_Y + BUTTON_HEIGHT && keyReleased) {
                keyReleased = false;
                fill(0);
                this.pid_status = STATUS_SAVE;
            }
        } else {
            keyReleased = true;
        }
        rect((float) ((720 - BUTTON_SAVE_WIDTH) / 2), (float) BUTTON_SAVE_Y, (float) BUTTON_SAVE_WIDTH, (float) BUTTON_HEIGHT);
        fill(0);
        textAlign(3, 3);
        text("SAVE", 360.0f, (float) (BUTTON_SAVE_Y + (BUTTON_HEIGHT / 2)));
        this.lastRiderFlag = riderFlag;
    }

    public void data_view_with_current() {
        int pidp_scaled;
        int pidd_scaled;
        int pidd;
        int pidp;
        float voltage = ((float) this.data[VOLTAGE]) / 100.0f;
        float f = ((float) this.data[BOARD_ANGLE]) / 100.0f;
        float f2 = ((float) this.data[STEER_ANGLE]) / 100.0f;
        short temp1 = this.data[TEMP1];
        short temp2 = this.data[TEMP2];
        short lmotor = this.data[LMOTOR];
        short rmotor = this.data[RMOTOR];
        short current1 = this.data[CURRENT1];
        short current2 = this.data[CURRENT2];
        boolean riderFlag = (this.data[PARAM1] & 32768) > 0;
        scaleX = ((float) this.width) / 720.0f;
        scaleY = ((float) this.height) / 1184.0f;
        scale(scaleX, scaleY);
        textAlign(21);
        if (this.pid_status == STATUS_NEW || riderFlag != this.lastRiderFlag) {
            this.pidp_new = this.data[PIDP];
            this.pidd_new = this.data[PIDD];
            this.pid_status = STATUS_OLD;
        }
        int pidp2 = this.pidp_new;
        int pidd2 = this.pidd_new;
        float pidp_f = ((float) pidp2) / 100.0f;
        float pidd_f = ((float) pidd2) / 100.0f;
        if (pidp2 < 100) {
            pidp_scaled = pidp2 * 2;
        } else {
            pidp_scaled = ((int) sqrt((float) ((pidp2 * 20) - 2000))) + 200;
        }
        if (pidd2 < 100) {
            pidd_scaled = pidd2 * 2;
        } else {
            pidd_scaled = ((int) sqrt((float) ((pidd2 * 20) - 2000))) + 200;
        }
        textSize(25.0f);
        fill(64.0f, 64.0f, 64.0f);
        text("BATTERY VOLTAGE", (float) ALIGN_LEFT, (float) VOLTAGE_Y);
        text("CURRENT 1", (float) ALIGN_LEFT, (float) CURRENT1_Y);
        text("CURRENT 2", (float) ALIGN_LEFT, (float) CURRENT2_Y);
        text("TEMPERATURE 1", (float) ALIGN_LEFT, (float) TEMP1_Y);
        text("TEMPERATURE 2", (float) ALIGN_LEFT, (float) TEMP2_Y);
        text("LEFT MOTOR", (float) ALIGN_LEFT, (float) LMOTOR_Y);
        text("RIGHT MOTOR", (float) ALIGN_LEFT, (float) RMOTOR_Y);
        text("P", (float) ALIGN_LEFT, (float) PIDP_Y);
        text("D", (float) ALIGN_LEFT, (float) PIDD_Y);
        textSize(50.0f);
        fill(0.0f, 0.0f, 0.0f);
        text(str(voltage), (float) ALIGN_LEFT, (float) (VOLTAGE_Y + OFFSET_VALUE));
        text(str((int) temp1), (float) ALIGN_LEFT, (float) (TEMP1_Y + OFFSET_VALUE));
        text(str((int) temp2), (float) ALIGN_LEFT, (float) (TEMP2_Y + OFFSET_VALUE));
        text(str((int) lmotor), (float) ALIGN_LEFT, (float) (LMOTOR_Y + OFFSET_VALUE));
        text(str((int) rmotor), (float) ALIGN_LEFT, (float) (RMOTOR_Y + OFFSET_VALUE));
        text(str((int) current1), (float) ALIGN_LEFT, (float) (CURRENT1_Y + OFFSET_VALUE));
        text(str((int) current2), (float) ALIGN_LEFT, (float) (CURRENT2_Y + OFFSET_VALUE));
        text(str(pidp_f), (float) ALIGN_LEFT, (float) (PIDP_Y + OFFSET_VALUE));
        text(str(pidd_f), (float) ALIGN_LEFT, (float) (PIDD_Y + OFFSET_VALUE));
        strokeWeight((float) STROKE);
        stroke(64.0f, 64.0f, 64.0f);
        line((float) VOLTAGE_X, (float) ((VOLTAGE_Y + OFFSET_GRAPH) - STROKE), (float) (VOLTAGE_X + LENGTH_GRAPH), (float) ((VOLTAGE_Y + OFFSET_GRAPH) - STROKE));
        if (voltage < MIN_BATTERY_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (voltage < MIN_BATTERY_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (voltage < MIN_BATTERY_1) {
            stroke(255.0f, 255.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) VOLTAGE_X, (float) ((VOLTAGE_Y + OFFSET_GRAPH) - STROKE), ((float) VOLTAGE_X) + ((((float) LENGTH_GRAPH) * voltage) / ((float) MAX_VOLTAGE)), (float) ((VOLTAGE_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) TEMP1_X, (float) ((TEMP1_Y + OFFSET_GRAPH) - STROKE), (float) (TEMP1_X + LENGTH_GRAPH), (float) ((TEMP1_Y + OFFSET_GRAPH) - STROKE));
        if (((float) temp1) > TEMP_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) temp1) > TEMP_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) temp1) > TEMP_STAGE_1) {
            stroke(255.0f, 228.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) TEMP1_X, (float) ((TEMP1_Y + OFFSET_GRAPH) - STROKE), (float) (TEMP1_X + ((LENGTH_GRAPH * temp1) / MAX_TEMP)), (float) ((TEMP1_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) TEMP2_X, (float) ((TEMP2_Y + OFFSET_GRAPH) - STROKE), (float) (TEMP2_X + LENGTH_GRAPH), (float) ((TEMP2_Y + OFFSET_GRAPH) - STROKE));
        if (((float) temp2) > TEMP_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) temp2) > TEMP_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) temp2) > TEMP_STAGE_1) {
            stroke(255.0f, 228.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) TEMP2_X, (float) ((TEMP2_Y + OFFSET_GRAPH) - STROKE), (float) (TEMP2_X + ((LENGTH_GRAPH * temp2) / MAX_TEMP)), (float) ((TEMP2_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) LMOTOR_X, (float) ((LMOTOR_Y + OFFSET_GRAPH) - STROKE), (float) (LMOTOR_X + LENGTH_GRAPH), (float) ((LMOTOR_Y + OFFSET_GRAPH) - STROKE));
        strokeWeight(1.0f);
        line((float) (LMOTOR_X + (LENGTH_GRAPH / 2)), (float) ((LMOTOR_Y + OFFSET_GRAPH) - (STROKE * 2)), (float) (LMOTOR_X + (LENGTH_GRAPH / 2)), (float) (LMOTOR_Y + OFFSET_GRAPH));
        strokeWeight((float) STROKE);
        if (((float) abs((int) lmotor)) > MOTOR_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) abs((int) lmotor)) > MOTOR_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) abs((int) lmotor)) > MOTOR_STAGE_1) {
            stroke(255.0f, 255.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) (LMOTOR_X + (LENGTH_GRAPH / 2)), (float) ((LMOTOR_Y + OFFSET_GRAPH) - STROKE), (float) (LMOTOR_X + (LENGTH_GRAPH / 2) + (((LENGTH_GRAPH * lmotor) / MAX_MOTOR) / 2)), (float) ((LMOTOR_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) RMOTOR_X, (float) ((RMOTOR_Y + OFFSET_GRAPH) - STROKE), (float) (RMOTOR_X + LENGTH_GRAPH), (float) ((RMOTOR_Y + OFFSET_GRAPH) - STROKE));
        strokeWeight(1.0f);
        line((float) (RMOTOR_X + (LENGTH_GRAPH / 2)), (float) ((RMOTOR_Y + OFFSET_GRAPH) - (STROKE * 2)), (float) (RMOTOR_X + (LENGTH_GRAPH / 2)), (float) (RMOTOR_Y + OFFSET_GRAPH));
        strokeWeight((float) STROKE);
        if (((float) abs((int) rmotor)) > MOTOR_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) abs((int) rmotor)) > MOTOR_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) abs((int) rmotor)) > MOTOR_STAGE_1) {
            stroke(255.0f, 255.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) (RMOTOR_X + (LENGTH_GRAPH / 2)), (float) ((RMOTOR_Y + OFFSET_GRAPH) - STROKE), (float) (RMOTOR_X + (LENGTH_GRAPH / 2) + (((LENGTH_GRAPH * rmotor) / MAX_MOTOR) / 2)), (float) ((RMOTOR_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) CURRENT1_X, (float) ((CURRENT1_Y + OFFSET_GRAPH) - STROKE), (float) (CURRENT1_X + LENGTH_GRAPH), (float) ((CURRENT1_Y + OFFSET_GRAPH) - STROKE));
        if (((float) current1) > CURRENT_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) current1) > CURRENT_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) current1) > CURRENT_STAGE_1) {
            stroke(255.0f, 228.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) CURRENT1_X, (float) ((CURRENT1_Y + OFFSET_GRAPH) - STROKE), (float) (CURRENT1_X + ((LENGTH_GRAPH * current1) / MAX_CURRENT)), (float) ((CURRENT1_Y + OFFSET_GRAPH) - STROKE));
        stroke(64.0f, 64.0f, 64.0f);
        line((float) CURRENT2_X, (float) ((CURRENT2_Y + OFFSET_GRAPH) - STROKE), (float) (CURRENT2_X + LENGTH_GRAPH), (float) ((CURRENT2_Y + OFFSET_GRAPH) - STROKE));
        if (((float) current2) > CURRENT_STAGE_3) {
            stroke(255.0f, 0.0f, 0.0f);
        } else if (((float) current2) > CURRENT_STAGE_2) {
            stroke(255.0f, 165.0f, 0.0f);
        } else if (((float) current2) > CURRENT_STAGE_1) {
            stroke(255.0f, 228.0f, 0.0f);
        } else {
            stroke(0.0f, 255.0f, 0.0f);
        }
        line((float) CURRENT2_X, (float) ((CURRENT2_Y + OFFSET_GRAPH) - STROKE), (float) (CURRENT2_X + ((LENGTH_GRAPH * current2) / MAX_CURRENT)), (float) ((CURRENT2_Y + OFFSET_GRAPH) - STROKE));
        if (this.mousePressed) {
            s_mouseX = (int) (((float) this.mouseX) / scaleX);
            s_mouseY = (int) (((float) this.mouseY) / scaleY);
            if (s_mouseX >= (PIDP_X + pidp_scaled) - MARGIN_X && s_mouseX <= PIDP_X + pidp_scaled + MARGIN_X && s_mouseY >= ((PIDP_Y + OFFSET_GRAPH) - STROKE) - MARGIN_Y && s_mouseY <= ((PIDP_Y + OFFSET_GRAPH) - STROKE) + MARGIN_Y) {
                int pidp3 = s_mouseX - PIDP_X;
                if (pidp3 < 0) {
                    pidp = 0;
                } else if (pidp3 < 200) {
                    pidp = pidp3 / 2;
                } else {
                    if (pidp3 > LENGTH_GRAPH) {
                        pidp3 = LENGTH_GRAPH;
                    }
                    pidp = (((pidp3 - 200) * (pidp3 - 200)) / 20) + 100;
                }
                this.pidp_new = (short) pidp;
            }
            if (s_mouseX >= (PIDD_X + pidd_scaled) - MARGIN_X && s_mouseX <= PIDD_X + pidd_scaled + MARGIN_X && s_mouseY >= ((PIDD_Y + OFFSET_GRAPH) - STROKE) - MARGIN_Y && s_mouseY <= ((PIDD_Y + OFFSET_GRAPH) - STROKE) + MARGIN_Y) {
                int pidd3 = s_mouseX - PIDD_X;
                if (pidd3 < 0) {
                    pidd = 0;
                } else if (pidd3 < 200) {
                    pidd = pidd3 / 2;
                } else {
                    if (pidd3 > LENGTH_GRAPH) {
                        pidd3 = LENGTH_GRAPH;
                    }
                    pidd = (((pidd3 - 200) * (pidd3 - 200)) / 20) + 100;
                }
                this.pidd_new = (short) pidd;
            }
        }
        stroke(0.0f, 255.0f, 0.0f);
        line((float) PIDP_X, (float) ((PIDP_Y + OFFSET_GRAPH) - STROKE), (float) (PIDP_X + LENGTH_GRAPH), (float) ((PIDP_Y + OFFSET_GRAPH) - STROKE));
        stroke(255.0f, 165.0f, 0.0f);
        line((float) (PIDP_X + pidp_scaled), (float) ((PIDP_Y + OFFSET_GRAPH) - (STROKE * 2)), (float) (PIDP_X + pidp_scaled), (float) (PIDP_Y + OFFSET_GRAPH));
        stroke(0.0f, 255.0f, 0.0f);
        line((float) PIDD_X, (float) ((PIDD_Y + OFFSET_GRAPH) - STROKE), (float) (PIDD_X + LENGTH_GRAPH), (float) ((PIDD_Y + OFFSET_GRAPH) - STROKE));
        stroke(255.0f, 165.0f, 0.0f);
        line((float) (PIDD_X + pidd_scaled), (float) ((PIDD_Y + OFFSET_GRAPH) - (STROKE * 2)), (float) (PIDD_X + pidd_scaled), (float) (PIDD_Y + OFFSET_GRAPH));
        strokeWeight(1.0f);
        fill((int) PImage.BLUE_MASK);
        if (this.mousePressed) {
            s_mouseX = (int) (((float) this.mouseX) / scaleX);
            s_mouseY = (int) (((float) this.mouseY) / scaleY);
            if (s_mouseX > (720 - BUTTON_SAVE_WIDTH) / 2 && s_mouseX < (BUTTON_SAVE_WIDTH + 720) / 2 && s_mouseY > BUTTON_SAVE_Y && s_mouseY < BUTTON_SAVE_Y + BUTTON_HEIGHT && keyReleased) {
                keyReleased = false;
                fill(0);
                this.pid_status = STATUS_SAVE;
            }
        } else {
            keyReleased = true;
        }
        rect((float) ((720 - BUTTON_SAVE_WIDTH) / 2), (float) BUTTON_SAVE_Y, (float) BUTTON_SAVE_WIDTH, (float) BUTTON_HEIGHT);
        fill(0);
        textAlign(3, 3);
        text("SAVE", 360.0f, (float) (BUTTON_SAVE_Y + (BUTTON_HEIGHT / 2)));
        this.lastRiderFlag = riderFlag;
    }

    public void calibrate_view() {
        scaleX = ((float) this.width) / 720.0f;
        scaleY = ((float) this.height) / 1184.0f;
        scale(scaleX, scaleY);
        textAlign(3, 3);
        if (calibrate_status == STATUS_OLD || calibrate_status == STATUS_LOCK_FAILED || calibrate_status == STATUS_CALIBRATE_FAILED) {
            int calibrate_fill = PImage.BLUE_MASK;
            int lock_fill = PImage.BLUE_MASK;
            strokeWeight(1.0f);
            fill((int) PImage.BLUE_MASK);
            if (this.mousePressed) {
                s_mouseX = (int) (((float) this.mouseX) / scaleX);
                s_mouseY = (int) (((float) this.mouseY) / scaleY);
                if (s_mouseX > (720 - BUTTON_CALIBRATE_WIDTH) / 2 && s_mouseX < (BUTTON_CALIBRATE_WIDTH + 720) / 2 && s_mouseY > BUTTON_CALIBRATE_Y && s_mouseY < BUTTON_CALIBRATE_Y + BUTTON_HEIGHT2 && keyReleased) {
                    keyReleased = false;
                    calibrate_fill = 0;
                    calibrate_status = STATUS_SAVE;
                }
                if (s_mouseX > (720 - BUTTON_LOCK_WIDTH) / 2 && s_mouseX < (BUTTON_LOCK_WIDTH + 720) / 2 && s_mouseY > BUTTON_LOCK_Y && s_mouseY < BUTTON_LOCK_Y + BUTTON_HEIGHT2 && keyReleased) {
                    keyReleased = false;
                    lock_fill = 0;
                    calibrate_status = STATUS_LOCK;
                }
            } else {
                keyReleased = true;
            }
            if (calibrate_status == STATUS_CALIBRATE_FAILED) {
                fill(255.0f, 0.0f, 0.0f);
            } else {
                fill(calibrate_fill);
            }
            rect((float) ((720 - BUTTON_CALIBRATE_WIDTH) / 2), (float) BUTTON_CALIBRATE_Y, (float) BUTTON_CALIBRATE_WIDTH, (float) BUTTON_HEIGHT2);
            fill(0);
            text("CALIBRATE", 360.0f, (float) (BUTTON_CALIBRATE_Y + (BUTTON_HEIGHT2 / 2)));
            if (calibrate_status == STATUS_LOCK_FAILED) {
                fill(255.0f, 0.0f, 0.0f);
            } else {
                fill(lock_fill);
            }
            rect((float) ((720 - BUTTON_LOCK_WIDTH) / 2), (float) BUTTON_LOCK_Y, (float) BUTTON_LOCK_WIDTH, (float) BUTTON_HEIGHT2);
            fill(0);
            if (this.locked) {
                text("UNLOCK", 360.0f, (float) (BUTTON_LOCK_Y + (BUTTON_HEIGHT2 / 2)));
            } else {
                text("LOCK", 360.0f, (float) (BUTTON_LOCK_Y + (BUTTON_HEIGHT2 / 2)));
            }
        } else if (calibrate_status == STATUS_WAIT) {
            fill(0);
            text("Calibration in progress...", (float) CALIBRATE_X, (float) CALIBRATE_Y);
        } else if (calibrate_status == STATUS_NEXT) {
            fill(0);
            text("Calibration completed", (float) CALIBRATE_X, (float) CALIBRATE_Y);
        }
    }

    public void joystick_view() {
        strokeWeight(1.0f);
        line(0.0f, (float) (this.height / 2), (float) this.width, (float) (this.height / 2));
        line((float) (this.width / 2), 0.0f, (float) (this.width / 2), (float) this.height);
        if (this.mousePressed) {
            strokeWeight(10.0f);
            line((float) (this.width / 2), (float) (this.height / 2), (float) this.mouseX, (float) this.mouseY);
            this.joystick_x = (short) (this.mouseX - (this.width / 2));
            this.joystick_y = (short) ((this.height / 2) - this.mouseY);
        } else {
            this.joystick_x = 0;
            this.joystick_y = 0;
        }
        textAlign(21);
        textSize(50.0f);
        text("X=" + str((int) this.joystick_x), (float) ALIGN_LEFT, 50.0f);
        text("Y=" + str((int) this.joystick_y), (float) ALIGN_LEFT, 110.0f);
    }

    public void gps_view() {
        background(0);
        textSize(50.0f);
        text("Latitude: " + this.fineCurrentLatitude, 20.0f, 40.0f);
        text("Longitude: " + this.fineCurrentLongitude, 20.0f, 100.0f);
        text("Accuracy: " + this.fineCurrentAccuracy, 20.0f, 160.0f);
        text("Provider: " + this.fineCurrentProvider, 20.0f, 220.0f);
        text("Speed: " + this.fineCurrentSpeed, 20.0f, 280.0f);
    }

    public void speed_view() {
        scaleX = ((float) this.width) / 720.0f;
        scaleY = ((float) this.height) / 1184.0f;
        scale(scaleX, scaleY);
        background((int) PImage.BLUE_MASK);
        textSize(350.0f);
        fill((int) PImage.BLUE_MASK);
        stroke(255.0f, 0.0f, 0.0f);
        ellipse(360.0f, 592.0f, 680.0f, 680.0f);
        fill(0);
        textAlign(3, 3);
        text(m0nf(this.aveSpeed, 1, 1), 360.0f, 592.0f);
        strokeWeight((float) STROKE);
        int alpha_scaled = (int) (this.alpha * ((float) LENGTH_GRAPH));
        if (this.mousePressed) {
            s_mouseX = (int) (((float) this.mouseX) / scaleX);
            s_mouseY = (int) (((float) this.mouseY) / scaleY);
            if (s_mouseX >= (ALPHA_X + alpha_scaled) - MARGIN_X && s_mouseX <= ALPHA_X + alpha_scaled + MARGIN_X && s_mouseY >= ((ALPHA_Y + OFFSET_GRAPH) - STROKE) - MARGIN_Y && s_mouseY <= ((ALPHA_Y + OFFSET_GRAPH) - STROKE) + MARGIN_Y) {
                alpha_scaled = s_mouseX - ALPHA_X;
                if (alpha_scaled < 0) {
                    alpha_scaled = 0;
                } else if (alpha_scaled > LENGTH_GRAPH) {
                    alpha_scaled = LENGTH_GRAPH;
                }
                this.alpha = (float) alpha_scaled;
                this.alpha /= (float) LENGTH_GRAPH;
            }
        }
        textAlign(21);
        textSize(50.0f);
        text(m0nf(this.alpha, 0, 2), (float) ALIGN_LEFT, (float) (ALPHA_Y + OFFSET_VALUE));
        stroke(0.0f, 255.0f, 0.0f);
        line((float) ALPHA_X, (float) ((ALPHA_Y + OFFSET_GRAPH) - STROKE), (float) (ALPHA_X + LENGTH_GRAPH), (float) ((ALPHA_Y + OFFSET_GRAPH) - STROKE));
        stroke(255.0f, 165.0f, 0.0f);
        line((float) (ALPHA_X + alpha_scaled), (float) ((ALPHA_Y + OFFSET_GRAPH) - (STROKE * 2)), (float) (ALPHA_X + alpha_scaled), (float) (ALPHA_Y + OFFSET_GRAPH));
    }

    public void draw() {
        if (this.lastRiderFlag) {
            background((float) this.f95bg[0], (float) this.f95bg[1], (float) this.f95bg[2]);
        } else {
            background(100);
        }
        if (this.locked) {
            view = CALIBRATE_VIEW;
        }
        if (view == DATA_VIEW) {
            data_view_with_attitude();
        } else if (view == JOYSTICK_VIEW) {
            joystick_view();
        } else if (view == CALIBRATE_VIEW) {
            calibrate_view();
        } else {
            speed_view();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data2) {
        if (requestCode != 0) {
            return;
        }
        if (resultCode == -1) {
            ToastMaster("Bluetooth has been switched ON");
        } else {
            ToastMaster("You need to turn Bluetooth ON !!!");
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(PConstants.MULTIPLY);
    }

    public void setup() {
        orientation(1);
        this.widgetContainer = new APWidgetContainer(this);
        this.screenButton = new APButton(this.width - BUTTON_SWAP_WIDTH, this.height - BUTTON_SWAP_HEIGHT, BUTTON_SWAP_WIDTH, BUTTON_SWAP_HEIGHT, "*");
        this.widgetContainer.addWidget(this.screenButton);
        if (!this.bluetooth.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 0);
        }
        if (this.bluetooth.isEnabled()) {
            registerReceiver(this.myDiscoverer, new IntentFilter("android.bluetooth.device.action.FOUND"));
            registerReceiver(this.checkIsConnected, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
            if (!this.bluetooth.isDiscovering()) {
                this.bluetooth.startDiscovery();
            }
        }
    }

    public void changeBackground(int bg0, int bg1, int bg2) {
        this.f95bg[0] = bg0;
        this.f95bg[1] = bg1;
        this.f95bg[2] = bg2;
    }

    public static byte[] stringToBytesUTFCustom(String str) {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[(buffer.length << 1)];
        for (int i = 0; i < buffer.length; i++) {
            int bpos = i << 1;
            b[bpos] = (byte) ((buffer[i] & 65280) >> 8);
            b[bpos + 1] = (byte) (buffer[i] & 255);
        }
        return b;
    }

    public void read_bluetooth_data() {
        while (this.sendReceiveBT.available() > 0) {
            short c = this.sendReceiveBT.readShort();
            if (!joystick_ack) {
                joystick_ack = true;
            } else if (calibrate_status == STATUS_WAIT) {
                if (c != 0) {
                    calibrate_status = STATUS_NEXT;
                } else {
                    calibrate_status = STATUS_CALIBRATE_FAILED;
                }
                changeBackground(PImage.BLUE_MASK, PImage.BLUE_MASK, PImage.BLUE_MASK);
            } else if (calibrate_status == STATUS_LOCK_WAIT) {
                if (c != 0) {
                    this.locked = !this.locked;
                    calibrate_status = STATUS_OLD;
                    if (!this.locked) {
                        view = DATA_VIEW;
                    }
                } else {
                    calibrate_status = STATUS_LOCK_FAILED;
                }
                changeBackground(PImage.BLUE_MASK, PImage.BLUE_MASK, PImage.BLUE_MASK);
            } else {
                this.data[this.data_pos] = c;
                if (this.data_pos == this.data.length - 1) {
                    sent = false;
                    if (this.pid_status == STATUS_NEXT) {
                        this.pid_status = STATUS_NEW;
                    }
                    this.data_pos = 0;
                    this.locked = (this.data[STATUS] & 1) != 0;
                } else {
                    this.data_pos++;
                }
            }
        }
        if (!sent && view == DATA_VIEW) {
            this.sendReceiveBT.write(100);
            sent = true;
            if (this.pid_status == STATUS_WAIT) {
                this.pid_status = STATUS_NEXT;
            }
        }
    }

    public void write_bluetooth_data() {
        if (this.pid_status == STATUS_SAVE) {
            this.sendReceiveBT.write(80);
            this.sendReceiveBT.writeShort(this.pidp_new);
            this.sendReceiveBT.writeShort(this.pidd_new);
            this.pid_status = STATUS_WAIT;
        }
        if (calibrate_status == STATUS_SAVE) {
            this.sendReceiveBT.write(99);
            calibrate_status = STATUS_WAIT;
            changeBackground(0, PImage.BLUE_MASK, PImage.BLUE_MASK);
        } else if (calibrate_status == STATUS_LOCK) {
            if (this.locked) {
                this.sendReceiveBT.write(117);
            } else {
                this.sendReceiveBT.write(108);
            }
            changeBackground(0, PImage.BLUE_MASK, PImage.BLUE_MASK);
            calibrate_status = STATUS_LOCK_WAIT;
        }
        if (!sent && view == JOYSTICK_VIEW && joystick_ack) {
            this.sendReceiveBT.write(120);
            this.sendReceiveBT.writeShort(this.joystick_x);
            this.sendReceiveBT.writeShort(this.joystick_y);
            joystick_ack = false;
        }
    }

    public void ToastMaster(String textToDisplay) {
        Toast myMessage = Toast.makeText(getApplicationContext(), textToDisplay, 0);
        myMessage.setGravity(17, 0, 0);
        myMessage.show();
    }

    public void onClickWidget(APWidget widget) {
        if (widget == this.redButton) {
            this.buttonText = "RED";
            background(255.0f, 0.0f, 0.0f);
        } else if (widget == this.greenButton) {
            this.buttonText = "GREEN";
            background(0.0f, 255.0f, 0.0f);
        } else if (widget == this.blueButton) {
            this.buttonText = "BLUE";
            background(0.0f, 0.0f, 255.0f);
        } else if (widget == this.offButton) {
            this.buttonText = "OFF";
            background(0);
        } else if (widget == this.saveButton) {
            this.buttonText = "SAVE";
            this.pid_status = STATUS_SAVE;
        } else if (widget == this.screenButton) {
            changeView();
        }
    }

    public void onResume() {
        super.onResume();
        this.fineLocationListener = new MyFineLocationListener();
        this.locationManager = (LocationManager) getSystemService("location");
        this.locationManager.requestLocationUpdates("gps", 0, 0.0f, this.fineLocationListener);
    }

    public void onPause() {
        super.onPause();
    }
}

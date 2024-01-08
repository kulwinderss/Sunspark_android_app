package com.example.kulwinder.sunspark;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.MotionEvent;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class mainpg extends AppCompatActivity {
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    Handler bluetoothIn;
    final int handlerState = 0;//used to identify handler message
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String message, phoneNo1, phoneNo2;
    LocationManager locationManager;
    double longitudeGPS, latitudeGPS;

    Button horn,ok;
    ToggleButton hl;
    TextView speed, seatbelt,front,back;
    RadioButton manual, auto;
    MediaPlayer player,player1,player2,player3;
    TextView vol, bat;
    int co1=0,co2=0,co3=0,co=0;
    String address = null;

    private ProgressDialog progress;//Progress bars are used to show progress of a task.
    BluetoothAdapter myBt = null;//Represents the local device Bluetooth adapter
    BluetoothSocket btSocket = null;//socket is an endpoint for communication between two machine

    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

        }
    };

    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };

    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };


    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };


    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpg);

        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        horn = (Button) findViewById(R.id.button5);
        //ok= (Button) findViewById(R.id.button8);
        hl = (ToggleButton) findViewById(R.id.toggleButton);
        manual = (RadioButton) findViewById(R.id.radioButton);
        auto = (RadioButton) findViewById(R.id.radioButton2);
        speed = (TextView) findViewById(R.id.textView5);
        vol = (TextView) findViewById(R.id.textView14);
        bat= (TextView) findViewById(R.id.textView15);
        seatbelt = (TextView) findViewById(R.id.textView7);
        front= (TextView) findViewById(R.id.textView9);
       // back = (TextView) findViewById(R.id.textView10);
        Intent newint = getIntent();
        address = newint.getStringExtra(bluetooth.EXTRAADD);
        new connectbt().execute();

        bluetoothIn = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == handlerState) {                                        //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                    //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        if (recDataString.charAt(0) == '#')                                //if it starts with # we know it is what we are looking for
                        {
                           Float speeds = Float.parseFloat(recDataString.substring(1, 6));    //speed      //get sensor value from string between indices 1-5
                            Float bvol =Float.parseFloat(recDataString.substring(7, 12));          //same again...
                            Float btemp = Float.parseFloat(recDataString.substring(13, 18));
                            Float sbelt = Float.parseFloat(recDataString.substring(19, 24));
                            Float fcol=Float.parseFloat(recDataString.substring(25, 30));
                            Float xx=Float.parseFloat(recDataString.substring(31, 36));

                           speed.setText(""+speeds);
                            bat.setText(""+btemp);
                            vol.setText(""+bvol);


                           // back.setText(""+bcol);

                            player = MediaPlayer.create(mainpg.this, R.raw.btw);
                            if (btemp>=45&&co==0)
                            {
                            bat.setBackgroundColor(Color.parseColor("#fd0404"));
                            player.start();
                            //player.setLooping(true);
                            co=1;
                        }
                        else if(btemp<45) {
                                bat.setBackgroundResource(R.drawable.speed);
                                co= 0;
                                if (player.isPlaying()==true)
                                player.stop();
                                player.release();
                            }

                            if(sbelt==33) {
                                seatbelt.setBackgroundResource(R.drawable.collision);
                            }
                            else
                                seatbelt.setBackgroundColor(Color.parseColor("#fd0404"));

                            if(speeds>3) {
                                player1 = MediaPlayer.create(mainpg.this, R.raw.seat);
                                player2 = MediaPlayer.create(mainpg.this, R.raw.fcw);

                                if (sbelt == 33) {

                                    seatbelt.setBackgroundResource(R.drawable.collision);
                                    co1 = 0;
                                    //  player1 = MediaPlayer.create(mainpg.this, R.raw.fcw);//seatbelt
                                    if (player1.isPlaying() == true)
                                        player1.stop();
                                    // player1.setLooping(true);

                                } else {
                                    seatbelt.setBackgroundColor(Color.parseColor("#fd0404"));
                                    player1.start();
                                    co1 = 1;
                                    //player.reset();
                                }
                            }
                            if(speeds>25) {
                                if (fcol == 44 && co2 == 0) {
                                    front.setBackgroundColor(Color.parseColor("#fd0404"));
                                    //player2 = MediaPlayer.create(mainpg.this, R.raw.fcw);//front collision
                                    player2.start();
                                    // player2.setLooping(true);
                                    co2 = 1;
                                } else {
                                    front.setBackgroundResource(R.drawable.collision);
                                    co2 = 0;
                                    if (player2.isPlaying() == true)
                                        player2.stop();
                                    //player.reset();
                                }
                            }
                            if(xx==11)
                            {
                                if (ActivityCompat.checkSelfPermission(mainpg.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainpg.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                Location location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitudeGPS = location.getLatitude();
                                    longitudeGPS = location.getLongitude();

                                    SharedPreferences set1=getSharedPreferences("mno1",0);
                                    phoneNo1=set1.getString("m1","");
                                    SharedPreferences set2=getSharedPreferences("mno2",0);
                                    phoneNo2=set1.getString("m2","");

                                    message = "Emergency...!!\n  A relative in problem...\n Location:Latitude.="+latitudeGPS+" \n Longitude="+longitudeGPS ;

                                    SmsManager.getDefault().sendTextMessage(phoneNo1, null, message, null,null);
                                    SmsManager.getDefault().sendTextMessage(phoneNo2, null, message, null,null);

                                    Toast.makeText(mainpg.this,
                                            "SMS Send", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        // strIncom =" ";
                        //dataInPrint = " ";
                    }
                }
            }
        };



        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hl.setEnabled(true);
                Toast.makeText(mainpg.this, manual.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hl.setEnabled(false);
                try {
                    btSocket.getOutputStream().write("A".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
                Toast.makeText(mainpg.this, auto.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        hl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hl1 = hl.getText().toString();
                if (hl1.equals("OFF")) {
                    try {
                        btSocket.getOutputStream().write("F".toString().getBytes());
                        Toast.makeText(mainpg.this, "Headlights off", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        msg("Error");
                    }
                } else if (hl1.equals("ON")) {
                    try {
                        btSocket.getOutputStream().write("N".toString().getBytes());
                        Toast.makeText(mainpg.this, "Headlights on", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
            }
        });

        horn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("H".toString().getBytes());
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
                return false;
            }
        });

//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        mVisible = true;
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("mainpg Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void stop(View view) {
            player.release();
    }

    //mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;
            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    //new1(readMessage);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private class connectbt extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(mainpg.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }
        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBt = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBt.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                    mConnectedThread = new ConnectedThread(btSocket);
                    mConnectedThread.start();
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    //private boolean isLocationEnabled() {
      //  return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
      //          locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
   // }

   // private final LocationListener locationListenerGPS = new LocationListener() {

     //   public void onLocationChanged(Location location) {
     //       longitudeGPS =location.getLongitude();
      //      latitudeGPS = location.getLatitude();

       // }
}

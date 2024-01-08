package com.example.kulwinder.sunspark;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class connect extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    Button team, con, mes, pl;
    String message, phoneNo,phoneNo1;
    LocationManager locationManager;
    double longitudeGPS, latitudeGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        team = (Button) findViewById(R.id.button2);
        con = (Button) findViewById(R.id.button3);
        mes = (Button) findViewById(R.id.button6);
        pl = (Button) findViewById(R.id.button7);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (!isLocationEnabled())
            showAlert();



        /*mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences set1=getSharedPreferences("mno1",0);
                phoneNo=set1.getString("m1","");
                Log.i("Send SMS", "");
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);

                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address"  , new String (phoneNo));
                smsIntent.putExtra("sms_body"  , "Testing");

                try {
                    startActivity(smsIntent);
                    finish();
                    Log.i("Finished sending SMS...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(connect.this,
                            "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        */
        pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(connect.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(connect.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    SharedPreferences set2=getSharedPreferences("mno2",0);
                    phoneNo=set1.getString("m1","");
                    phoneNo1=set2.getString("m2","");

                    message = "Relative in problem check it... \n longitude="+longitudeGPS+"\n   lantitude="+latitudeGPS;

                    SmsManager.getDefault().sendTextMessage(phoneNo, null, message, null,null);
                    SmsManager.getDefault().sendTextMessage(phoneNo1, null, message, null,null);
                    Toast.makeText(connect.this,
                            "SMS Send", Toast.LENGTH_SHORT).show();
            }}
        });


        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(connect.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(connect.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerGPS);

                SharedPreferences set1=getSharedPreferences("mno1",0);
                 phoneNo=set1.getString("m1","");

                 message = "Relative need help.. check it... \n longitude"+longitudeGPS;

                SmsManager.getDefault().sendTextMessage(phoneNo, null, message, null,null);
                Toast.makeText(connect.this,
                        "SMS Send", Toast.LENGTH_SHORT).show();


            }
        });

        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(connect.this, team.class);
                startActivity(b);
            }
        });

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(connect.this, bluetooth.class);
                finish();
                startActivity(c);
            }
        });
    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private final LocationListener locationListenerGPS = new LocationListener() {

        public void onLocationChanged(Location location) {
            longitudeGPS =location.getLongitude();
            latitudeGPS = location.getLatitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater infalte=getMenuInflater();
        infalte.inflate(R.menu.message,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.item1)
        {
      Intent em=new Intent(connect.this, emergenymess.class);
            startActivity(em);
        }
        return true;
    }
}

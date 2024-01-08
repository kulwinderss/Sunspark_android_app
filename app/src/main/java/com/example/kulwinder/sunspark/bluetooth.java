package com.example.kulwinder.sunspark;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class bluetooth extends AppCompatActivity {

    ListView list;
    Button paired;

    private BluetoothAdapter adapter = null;//Represents the local device Bluetooth adapter
    private Set<BluetoothDevice> paireddevices;
    public static String EXTRAADD = "device_address";//use to store MAC address of connected device


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        //Calling the widgets
        paired = (Button) findViewById(R.id.button4);
        list = (ListView) findViewById(R.id.listView);

        //if the device has bluetooth
        adapter = BluetoothAdapter.getDefaultAdapter();

        if (adapter == null) {
            //if no adapter/device availabe
            Toast.makeText(getApplicationContext(), "No Bluetooth Device available", Toast.LENGTH_LONG).show();
            finish();
        } else if (!adapter.isEnabled())//if adapter is available
        {
            //Ask to the user for turning on the bluetooth
            Intent i1 = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);//intent use for to atrat new activity
            startActivityForResult(i1, 1);
        }

        paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();//call function
            }
        });

    }

    //function
    private void pairedDevicesList() {
        /*Once you have the local adapter, you can get a set of BluetoothDevice objects
        representing all paired devices with getBondedDevices() */
        paireddevices = adapter.getBondedDevices();
        ArrayList alist = new ArrayList();

        if (paireddevices.size() > 0) //if paired devices are there
        {
            for (BluetoothDevice bt : paireddevices)
            {
                alist.add(bt.getName() + "\n" + bt.getAddress()); //Get the device name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
// to show a vertical list of scrollable items we will use a ListView which has data populated using an Adapter
        /*
        The ArrayAdapter requires a declaration of the type of the item to be converted to a
        View (a String in this case) and then accepts three arguments: context (activity instance),
        XML item layout, and the array of data. Note that we've chosen simple_list_item_1.xml
        which is a simple TextView as the layout for each of the items.
         */
        final ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, alist);
        list.setAdapter(adapter1);
        list.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent i = new Intent(bluetooth.this, mainpg.class);

            //Change the activity.
            i.putExtra(EXTRAADD, address); //this will be received at LED(class) Activity
            startActivity(i);
        }
    };

}

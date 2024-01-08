package com.example.kulwinder.sunspark;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

public class emergenymess extends AppCompatActivity {

    ToggleButton ch;
    EditText m1,m2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergenymess);

        ch= (ToggleButton) findViewById(R.id.toggleButton2);
        m1= (EditText) findViewById(R.id.editText2);
        m2= (EditText) findViewById(R.id.editText3);

        SharedPreferences set1=getSharedPreferences("mno1",0);
        m1.setText(set1.getString("m1",""));
        SharedPreferences set2=getSharedPreferences("mno2",0);
        m2.setText(set2.getString("m2",""));

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String hl1=ch.getText().toString();
                if(hl1.equals("OK"))
                {
                    m1.setEnabled(true);
                    m2.setEnabled(true);
                }
                else if(hl1.equals("CHANGE"))
                {

                        Toast.makeText(emergenymess.this, "Done", Toast.LENGTH_SHORT).show();
                    m1.setEnabled(false);
                    m2.setEnabled(false);
                    SharedPreferences set1=getSharedPreferences("mno1",0);
                    SharedPreferences set2=getSharedPreferences("mno2",0);
                    SharedPreferences.Editor ed1=set1.edit();
                    SharedPreferences.Editor ed2=set2.edit();
                    ed1.putString("m1",m1.getText().toString());
                    ed2.putString("m2",m2.getText().toString());
                    ed1.commit();
                    ed2.commit();


                }

            }
        });
    }
}

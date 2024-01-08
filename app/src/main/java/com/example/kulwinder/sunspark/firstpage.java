package com.example.kulwinder.sunspark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class firstpage extends AppCompatActivity {
    int counter=3;
    Button enter;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        enter= (Button) findViewById(R.id.button);
        pass= (EditText) findViewById(R.id.editText);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( pass.getText().toString().equals("sp"))
                {
                    Toast.makeText(getApplicationContext(),"Redirecting...",Toast.LENGTH_SHORT).show();
                    Intent b = new Intent(firstpage.this, connect.class);
                    finish();
                    startActivity(b);


                }
                else if(pass.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pass.setText("");
                    counter--;
                    if(counter==2)
                        Toast.makeText(getApplicationContext(),"Wrong password and you left with 2 more attempts",Toast.LENGTH_SHORT).show();
                    else if(counter==1)
                        Toast.makeText(getApplicationContext(),"Wrong password and you left with last attempt",Toast.LENGTH_SHORT).show();
                    else if(counter==0) {
                        Toast.makeText(getBaseContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                        System.exit(0);
                    }
                }
            }
        });

    }

}

package com.example.kulwinder.sunspark;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class team extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        String[] itemname = {"Prakher Srivastava","Manpreet Singh","Maninder Sharma","Piyush Bhardawaj",//4
                "Hardeep Singh","Preetinder Singh","Vyomkesh Vyas","Jayesh Mishra","Shubham Prakash","Deepak Sharma",//10
                "Kulwinder Singh","Ashok Kumar","Akash Singh","Saurav Suman","Naman Jaswani","Princy Manwani","Palak Mahajan"};
        String[] itemdes = {"Steering & Marketing","Steering & Fabrication","Fabrication","Design & Sponsorship",//4
                "Design & Fabrication","Sponsorship","Steering & Fabrication","Fabrication","Brakes","Brakes & Fabrication",//10
                "Electronics & Control","Electrical","Electrical","Electronics","Electrical","Electrical","Transmission & Sponsorship"};
      Integer[] imgid={
                R.drawable.prakher,//1
                R.drawable.manpreet,//2
                R.drawable.maninder,
                R.drawable.piyush,
                R.drawable.hardeep1,//5
                R.drawable.preet,
                R.drawable.vyom,//7
                R.drawable.jayesh,
              R.drawable.shubham,
              R.drawable.deepak,//10
              R.drawable.kulwinder,//11
              R.drawable.ashok,
              R.drawable.akash,
              R.drawable.saurav,
              R.drawable.naman,//15
              R.drawable.princy,
              R.drawable.palak
        };
        ListAdapter adapter=new customadapterL(this, itemname, itemdes, imgid);
        ListView listView = (ListView) findViewById(R.id.bucky);
        listView.setAdapter(adapter);
    }
}
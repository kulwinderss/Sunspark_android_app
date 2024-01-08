package com.example.kulwinder.sunspark;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kulwinder.sunspark.R;

import static com.example.kulwinder.sunspark.R.id.custom;

/**
 * Created by Kulwinder on 1/17/2017.
 */

class customadapterL extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] itemname;
    private final String[] itemdes;
    private final Integer[] imgid;

    public customadapterL(Activity context, String[] itemname,String[] itemdes, Integer[] imgid) {
        super(context, R.layout.newlist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.itemdes=itemdes;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.newlist, null, true);

        //TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        TextView text = (TextView) rowView.findViewById(R.id.textView16);
        TextView text2 = (TextView) rowView.findViewById(R.id.textView18);
        ImageView image = (ImageView) rowView.findViewById(R.id.imageView2);

        text.setText(itemname[position]);
        text2.setText(itemdes[position]);
        image.setImageResource(imgid[position]);
        //extratxt.setText("Description "+itemname[position]);
        return rowView;
    }
}
        /*
customadapterL(Context context,String[] food){
    super(context, R.layout.newlist, food);
}

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate=LayoutInflater.from(getContext());
        View custom=inflate.inflate(R.layout.newlist, parent,false);

        String item=getItem(position);
        TextView text= (TextView) custom.findViewById(R.id.textView16);
        ImageView image= (ImageView) custom.findViewById(R.id.imageView2);

        text.setText(item);
        image.setImageResource(R.drawable.images);

        return custom;


    }*/



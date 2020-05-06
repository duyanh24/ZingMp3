package com.leduyanh.zingmp3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    ArrayList<Mp3> mp3 = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public CustomAdapter(Context context,ArrayList<Mp3> mp3) {
        this.mp3 = mp3;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mp3.size();
    }

    @Override
    public Object getItem(int position) {
        return mp3.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.activity_item,parent,false);
        }
        ImageView hinh = (ImageView)convertView.findViewById(R.id.imgHinh);
        TextView txtBaiHat = (TextView)convertView.findViewById(R.id.txtBauHat);
        TextView txtCaSi = (TextView)convertView.findViewById(R.id.txtCaSi);

        hinh.setImageResource(mp3.get(position).hinh);
        txtBaiHat.setText(mp3.get(position).baiHat);
        txtCaSi.setText(mp3.get(position).caSi);

        return convertView;
    }
}

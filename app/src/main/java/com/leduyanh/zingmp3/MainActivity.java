package com.leduyanh.zingmp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvDanhSach;
    ArrayList<Mp3> listMp3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        lvDanhSach = (ListView)findViewById(R.id.lvDanhSach);

        Data data = new Data();
        Collections.addAll(listMp3,data.mp3);
        CustomAdapter adapter = new CustomAdapter(MainActivity.this,listMp3);
        lvDanhSach.setAdapter(adapter);

        lvDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,Play.class);
                intent.putExtra("id",position);
                startActivity(intent);
            }
        });
    }
}

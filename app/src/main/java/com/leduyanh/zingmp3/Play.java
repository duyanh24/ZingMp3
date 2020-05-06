package com.leduyanh.zingmp3;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Play extends AppCompatActivity implements View.OnClickListener{

    ImageButton btnPlay,btnNext,btnPrevious;
    ImageView imgDiaNhac;
    TextView txtBaiHat,txtTimeEnd,txtTimeStart;
    SeekBar skTime;

    ArrayList<Mp3> listMp3 = new ArrayList<>();
    int position;
    MediaPlayer mediaPlayer;
    Boolean flag;
    Timer timer;
    RunTimer runTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        init();
        XuLy(position);
        rotateDiaNhac();
    }

    public void init(){
        btnPlay = (ImageButton)findViewById(R.id.btnPlay);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        imgDiaNhac = (ImageView)findViewById(R.id.imgDiaNhac);
        txtBaiHat = (TextView)findViewById(R.id.txtTenBai);
        txtTimeStart = (TextView)findViewById(R.id.txtTimeStart);
        txtTimeEnd = (TextView)findViewById(R.id.txtTimeEnd);
        skTime = (SeekBar)findViewById(R.id.skTime);

        Data data = new Data();
        Collections.addAll(listMp3,data.mp3);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            position = bundle.getInt("id");
        }

        flag = false;

        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
    }

    public void XuLy(int pos){
        if(flag){
            mediaPlayer.stop();
            timer.cancel();
        }
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        String bh = listMp3.get(pos).baiHat;
        int link = listMp3.get(pos).url;
        txtBaiHat.setText(bh);

        getSong(link);
        btnPlay.setImageResource(R.drawable.pause);
        flag = true;
    }

    public void getSong(int link){
        mediaPlayer = MediaPlayer.create(Play.this,link);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                int timeDuration = mediaPlayer.getDuration();
                convertTime(txtTimeEnd,timeDuration);
                skTime.setMax(timeDuration);
                skTime.setProgress(0);
                skTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mediaPlayer.seekTo(skTime.getProgress());
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mediaPlayer.seekTo(skTime.getProgress());
                    }
                });
                timer = new Timer();
                runTimer = new RunTimer();
                timer.scheduleAtFixedRate(runTimer,0,1);
            }
        });
    }

    public class RunTimer extends TimerTask{

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    skTime.incrementProgressBy(1);
                    convertTime(txtTimeStart,skTime.getProgress());
                    if(skTime.getProgress() == mediaPlayer.getDuration()){
                        if(position == listMp3.size() - 1){
                            position = 0;
                        }else{
                            position++;
                        }
                        XuLy(position);
                    }
                }
            });
        }
    }

    public void convertTime(TextView tv,int time){
        NumberFormat f= new DecimalFormat("00");
        long timeMiniutes = TimeUnit.MILLISECONDS.toMinutes((long)time);
        long timeSecond = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(timeMiniutes);
        String result = f.format(timeMiniutes)+" : "+f.format(timeSecond);
        tv.setText(result);
    }

    public void rotateDiaNhac(){
        RotateAnimation rotate = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5F,RotateAnimation.RELATIVE_TO_SELF,0.5F);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(5000);
        imgDiaNhac.startAnimation(rotate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlay:
                if(flag){
                    btnPlay.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                    runTimer.cancel();
                    flag = false;
                }else{
                    btnPlay.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    runTimer = new RunTimer();
                    timer.scheduleAtFixedRate(runTimer,0,1);
                    flag = true;
                }
                break;
            case R.id.btnNext:
                if(position == listMp3.size() - 1){
                    position = 0;
                }else{
                    position++;
                }
                XuLy(position);
                break;
            case R.id.btnPrevious:
                if(position == 0){
                    position = listMp3.size() - 1;
                }else{
                    position--;
                }
                XuLy(position);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        flag = false;
    }
}

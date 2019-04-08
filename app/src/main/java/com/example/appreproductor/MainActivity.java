package com.example.appreproductor;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button btnReproducir;
    private Button btnAnterior;
    private Button btnSiguiente;
    private SeekBar seekbarCancion;
    private SeekBar seekbarVolumen;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private boolean isActivePlaying = false;
    private Runnable runnable;
    private Handler handler;

    public void pauseClick(View view){
        mediaPlayer.pause();
    }

    public void playClick(View view){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            btnReproducir.setText("Reproducir");
        }else{
            mediaPlayer.start();
            btnReproducir.setText("Pausar");
            modificarSeekbarCanción();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReproducir = (Button) findViewById(R.id.btnPlay);
        btnSiguiente = (Button) findViewById(R.id.btnFollowing);
        btnAnterior = (Button) findViewById(R.id.btnPrevious);
        handler = new Handler();
        seekbarCancion = findViewById(R.id.seekbarDuracion);
        //Aqui
        mediaPlayer = MediaPlayer.create(this, R.raw.sail_awolnation);
        iniciarCanción();

        // Manejo de volumen

        //audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        controlarVolumen();


    }
    public void iniciarCanción(){
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekbarCancion.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                modificarSeekbarCanción();
            }
        });
        seekbarCancion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void modificarSeekbarCanción(){
        seekbarCancion.setProgress(mediaPlayer.getCurrentPosition());
        if(mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    modificarSeekbarCanción();
                }
            };
            handler.postDelayed(runnable,1000);
        }
    }
    public void controlarVolumen(){
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        SeekBar volumeSeekBar = findViewById(R.id.seekbarVolumen);

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.d("volume:", Integer.toString(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}

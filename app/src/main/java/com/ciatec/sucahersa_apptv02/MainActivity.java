package com.ciatec.sucahersa_apptv02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener {

    String claveYoutube = "AIzaSyBOogQ7p8FGSTfqqyf54j3itv4LuPqr0L0";
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youTubePlayerView=(YouTubePlayerView)findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(claveYoutube, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean fueRestaurado) {
        if (!fueRestaurado)
        {
            //youTubePlayer.cueVideo("kPa9YoPZALs&list=PLty-EzYotmfSRMC1jNdbP6yygILz2wHAG");
            youTubePlayer.loadPlaylist("PLty-EzYotmfSRMC1jNdbP6yygILz2wHAG");
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this, 1).show();

        }else {
            String error = "Error al inicializar Youtube " + youTubeInitializationResult.toString();
            Toast.makeText(this, error,Toast.LENGTH_LONG).show();
        }

    }
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if(resultCode == 1){
            getYoutubePLayerProvider().initialize(claveYoutube, this);
        }
    }

    protected YouTubePlayer.Provider getYoutubePLayerProvider(){
        return youTubePlayerView;
    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }
}

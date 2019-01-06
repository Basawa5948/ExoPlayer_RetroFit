package com.droid.mobiotics.mobiotics;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class PlayVideo extends AppCompatActivity {

    String title;
    String description;
    String url;
    int touch;

    TextView titleset;
    TextView descriptionset;

    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Videos> videosList;
    ProgressDialog dialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        dialog = new ProgressDialog(PlayVideo.this);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();

        title = bundle.getString("Title");
        description = bundle.getString("Description");
        url = bundle.getString("URL");
        touch = bundle.getInt("Touch");

        titleset = (TextView) findViewById(R.id.titleset);
        descriptionset = (TextView) findViewById(R.id.descriptionset);

        titleset.setText(title);
        descriptionset.setText(description);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayerview);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
        Uri uri = Uri.parse(url);
        DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("mobiotics");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(uri,defaultHttpDataSourceFactory,extractorsFactory,null,null);

        simpleExoPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(false);

        //RecyclerView For related Videos
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewtwo);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog.show();
        dialog.setMessage("Loading Related Videos");
        getDataFromVideos();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 3000);

    }

    private void getDataFromVideos() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRequest.Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRequest apiRequest = retrofit.create(ApiRequest.class);

        Call<List<Videos>> call = apiRequest.getDataFromVideos();

        call.enqueue(new Callback<List<Videos>>() {
            @Override
            public void onResponse(Call<List<Videos>> call, Response<List<Videos>> response) {
                int count = 0;
                List<Videos> v = response.body();
                videosList = new ArrayList<>();

                for(int i =0;i<v.size();i++){
                    if(v.get(i).getTitle().equals(title)){

                    }else{
                        Videos videos = new Videos(v.get(i).getDescription(),v.get(i).getId(),
                                v.get(i).getThumb(),v.get(i).getTitle(),v.get(i).getUrl());

                        videosList.add(videos);
                    }

                }

                adapter = new RelatedVideoAdapter(videosList,PlayVideo.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Videos>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

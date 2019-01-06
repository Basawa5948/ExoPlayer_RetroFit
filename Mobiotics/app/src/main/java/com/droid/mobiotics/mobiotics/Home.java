package com.droid.mobiotics.mobiotics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Videos> videosList;
    Bundle bundle;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        dialog = new ProgressDialog(Home.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog.show();
        dialog.setMessage("Fetching Details!!");
        getDataFromVideos();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 3000); // 3000 milliseconds delay

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                dialog.setMessage("Logging You Out");
                firebaseAuth.signOut();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                }, 3000);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                        if(bundle.getInt("flag")==1){
                            dialog.dismiss();
                            finish();
                        }else{
                            Intent intent = new Intent(Home.this , Login.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }
                }
            }
        };
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
                List<Videos> v = response.body();
                videosList = new ArrayList<>();

                for(int i =0;i<v.size();i++){
                    Videos videos = new Videos(v.get(i).getDescription(),v.get(i).getId(),
                            v.get(i).getThumb(),v.get(i).getTitle(),v.get(i).getUrl());

                    videosList.add(videos);
                }

                adapter = new VideoAdapter(videosList,Home.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Videos>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

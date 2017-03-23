package codes.platform.flickerstreaming;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.i2p.android.ext.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import codes.platform.flickerstreaming.DataStorage.ImageDatabase;
import codes.platform.flickerstreaming.InternetDataRequest.RequestService;

public class HomeActivity extends AppCompatActivity {


    private FloatingActionButton button_grid;


    private BroadcastReceiver imageReceiver;


    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private GridLayoutManager mGridLayoutManager;

    private RecyclerAdapter recyclerAdapter ;



    private SwipeRefreshLayout swipeRefresh ;



    private int Display_MODE = 0; // recycleView display ,, 0=linear , 1=grid



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        setUIproperties();

        setImageReceiver(); // set Custom broadcast reciever

        updateRecycleView(); // update recycleview with cache images

        sendUpdateRequest();  // send request to IntentService


    }


    private void setUIproperties(){


        button_grid = (FloatingActionButton) findViewById(R.id.GridButton) ;

        button_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /* Flip float button to the other mode*/

                if (Display_MODE ==0){

                    button_grid.setIcon(R.drawable.ic_grid_off_white_36dp);
                    Display_MODE=1;
                }



                else{
                    button_grid.setIcon(R.drawable.ic_grid_on_white_36dp);
                    Display_MODE=0;
                }



                updateDisplayMODE();  //update to linear or grid



            }
        });


        mLinearLayoutManager = new LinearLayoutManager(this);
        mGridLayoutManager = new GridLayoutManager(this, 2);  // set grid two columns

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        updateDisplayMODE();



        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { /*on pull request*/

                sendUpdateRequest();
            }
        });


        if(isServiceRunning()){  /* to display swiprefersh on rotation as intentService running */
            swipeRefresh.setRefreshing(true);
        }


    }



    private void updateRecycleView(){

        /* retrieve image list form database*/
        ImageDatabase database = new ImageDatabase(HomeActivity.this);
        ArrayList<Image> list = database.getData();

        /*set custom adapter*/
        recyclerAdapter = new RecyclerAdapter(HomeActivity.this,list);


        recyclerView.setAdapter(recyclerAdapter);
    }




    private void updateDisplayMODE(){


        if (Display_MODE ==0){

            recyclerView.setLayoutManager(mLinearLayoutManager);
        }
        else{

            recyclerView.setLayoutManager(mGridLayoutManager);
        }

    }




    private void setImageReceiver(){ /*broadcast fire once the IntentService finish download*/


        imageReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                swipeRefresh.setRefreshing(false);


                updateRecycleView();

            }
        };

        /* registering the receiver*/
        IntentFilter intentFilter = new IntentFilter("com.FlickerStream.IMAGE_RECEIVE");
        this.registerReceiver(imageReceiver, intentFilter);

    }



    public void sendUpdateRequest(){


        if ( ! isServiceRunning() ){

            swipeRefresh.setRefreshing(true);

            Intent intent = new Intent(HomeActivity.this, RequestService.class);
            startService(intent);
        }

    }


    private boolean isServiceRunning() { /* check if Intent filter is running*/

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {


            if ("codes.platform.flickerstreaming.InternetDataRequest.RequestService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}

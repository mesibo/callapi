package com.mesibo.calls.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mesibo.api.Mesibo;
import com.mesibo.calls.api.MesiboCall;



public class MainActivity extends AppCompatActivity implements Mesibo.ConnectionListener, MesiboCall.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        File root =android.os.Environment.getExternalStorageDirectory();

        File Dir = new File(root.getAbsolutePath()+"/DCIM/Camera/");

        File files[] = Dir.listFiles();

        for(File f: files) {
            if(f.getName().startsWith(".")) {
                Log.e("", "Found: " + f.getName());
            }
        }
        */

        Mesibo mesibo = Mesibo.getInstance();
        mesibo.init(getApplicationContext());
        mesibo.setAccessToken("ccd9d1066a47f07f43b17a1b7778f1095f0a4d54b62967ecc146d75"); //919999900104
        boolean res = mesibo.setDatabase("mesibocalls.db", 0);
        mesibo.addListener(this);
        Mesibo.start();


        //MesiboWebrtc.init(this, true);
        //long i = MesiboWebrtc.nativeGetInterface();
        //Mesibo.setCallInterface(0, 0);


        MesiboCall.getInstance().init(this);

        Mesibo.UserProfile u = new Mesibo.UserProfile();
        u.name = "Name Set by App";
        //u.address = "919113203545";

        u.address = "919999900105";
        //u.address = "919113203545";
        u.address = "919901172890";
        //u.address = "919740305019";
        //u.address = "919999900103";
        //u.address = "919113203545";
        //u.address = "919999900150";


        Mesibo.setUserProfile(u, false);


        FloatingActionButton fab_v = (FloatingActionButton) findViewById(R.id.fab_videocall);
        fab_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean videoCall = true;
                //CallManager.getInstance().call(MainActivity.this, 1, u, videoCall, false);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                MesiboCall.getInstance().launchCallActivity(MainActivity.this, CallActivity.class, "919901172890", true);

            }
        });

        FloatingActionButton fab_a = (FloatingActionButton) findViewById(R.id.fab_audiocall);
        fab_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MesiboCall.getInstance().launchCallActivity(MainActivity.this, CallActivity.class, "919901172890", false);

                boolean videoCall = true;
                //CallManager.getInstance().call(MainActivity.this, 1, u, false, false);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();

            }
        });

        FloatingActionButton fab_m = (FloatingActionButton) findViewById(R.id.fab_message);
        fab_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mesibo.MessageParams params = new Mesibo.MessageParams();
                params.peer = u.address;
                //Mesibo.sendMessage(params, Mesibo.random(), "Hello Friend");

                MesiboCall.CallContext cc = new MesiboCall.CallContext(true);
                cc.parent = getApplicationContext();
                cc.className = CallActivity.class;
                cc.user = new Mesibo.UserProfile();
                cc.user.name = "test";
                cc.user.address = "testaddr";

                cc.notify.message = "message";
                cc.notify.title = "title";
                cc.notify.icon = android.R.drawable.ic_dialog_info;
                MesiboCall.getInstance().testNotify(getApplicationContext(), cc);
            }
        });
    }


    @Override
    public void Mesibo_onConnectionStatus(int status) {
        Log.d("Mesibo", "Connection status: " + status);
    }

    @Override
    public MesiboCall.CallContext MesiboCall_OnIncoming(MesiboCall.Call call, Mesibo.UserProfile profile, boolean video) {
        MesiboCall.CallContext cc;
        if(call != null) {
            /* launch activity - will be called only if activity was passed*/
        }
        cc = new MesiboCall.CallContext(video);
        cc.parent = getApplicationContext();
        cc.user = profile;
        return cc;
    }

    @Override
    public boolean MesiboCall_OnError(MesiboCall.CallContext ctx, int error) {
        return false;
    }

    @Override
    public boolean MesiboCall_onNotify(int type, Mesibo.UserProfile profile, boolean video) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

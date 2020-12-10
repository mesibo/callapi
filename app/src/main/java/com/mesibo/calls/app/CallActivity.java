package com.mesibo.calls.app;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.mesibo.api.Mesibo;
import com.mesibo.calls.api.MesiboCall;
import com.mesibo.calls.api.MesiboCallActivity;

public class CallActivity extends MesiboCallActivity {


    FrameLayout mFrameContainer;
    private boolean mFragLoaded = false;

    private boolean mInit = false;

    MesiboCall.Call mCall = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TBD. different layout for voice and video
        setContentView(R.layout.activity_call);

        int res = checkPermissions(mVideo);

        /* permissions were declined */
        if(res < 0) {
            finish();
            return;
        }

        /* all permissions were already granted */
        if(0 == res) {
            initCall();
        } else {
            /* permission requested - wait for results */
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initCall();
        }
        else
            finish();
    }

    private void initCall() {
        if(mInit) return;
        mInit = true;

        mCall = MesiboCall.getInstance().getActiveCall();

        if(null == mCall) {

            Mesibo.UserProfile profile = Mesibo.getUserProfile(mAddress);
            if(null == profile) {
                profile = new Mesibo.UserProfile();
                profile.address = mAddress;
                profile.name = mAddress;
            }

            MesiboCall.CallContext cc = new MesiboCall.CallContext(mVideo);
            cc.parent = this;
            cc.activity = this;
            cc.user = profile;
            cc.useScreencapture = mScreenCapture;

            mCall = MesiboCall.getInstance().call(cc);

            if(null == mCall || !mCall.isCallInProgress()) {
                finish();
                return;
            }
        }

        super.initCall(mCall);

        CallFragment fragment = null;
        fragment = new CallFragment();

        /* OPTIONAL - you can different fragments for different kind of calls if you prefer */
        if(mVideo) {
            // show video fragment
        } else if(mIncoming && mCall.isCallInProgress() && !mCall.isAnswered()) {
            //show incoming audio fragment
        } else {
            //show outgoing audio fragemnt
        }

        fragment.MesiboCall_OnSetCall(this, mCall);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.top_fragment_container, fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        /*
        if(mDtmfVisible) {
            showDialer(); //hide dialer
            return;
        }
        */
        //moveTaskToBack(true);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCall = MesiboCall.getInstance().getActiveCall();
        if(null == mCall) {
            finish();
            return;
        }

        //setValues();
    }


}

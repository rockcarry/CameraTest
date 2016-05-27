package com.fanfan.cameratest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;

public class CameraTestActivity extends Activity
{
    private static final String TAG = "CameraTestActivity";

    private SurfaceView mPreview;

    private RecordService.RecordBinder mRecServ = null;
    private ServiceConnection mRecServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serv) {
            mRecServ = (RecordService.RecordBinder)serv;
            mRecServ.selectCamera(0);
            mRecServ.setPreviewSurfaceHolder(mPreview.getHolder());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRecServ = null;
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mPreview = (SurfaceView)findViewById(R.id.camera_preview_view);
        mPreview.getHolder().addCallback(mPreviewSurfaceHolderCallback);

        // start record service
        Intent i = new Intent(CameraTestActivity.this, RecordService.class);
        startService(i);

        // bind record service
        bindService(i, mRecServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        // unbind record service
        unbindService(mRecServiceConn);

        // stop record service
        Intent i = new Intent(CameraTestActivity.this, RecordService.class);
        stopService(i);

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private SurfaceHolder.Callback mPreviewSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "surfaceCreated");
            if (mRecServ != null) {
                mRecServ.selectCamera(0);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed");
            if (mRecServ != null) {
                mRecServ.selectCamera(-1);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            Log.d(TAG, "surfaceChanged");
        }
    };
}





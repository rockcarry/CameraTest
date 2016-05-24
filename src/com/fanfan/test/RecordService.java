package com.fanfan.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RecordService extends Service
{
    private static final String TAG = "RecordService";
    private static final int CAMERA_VIDEO_WIDTH  = 1280;
    private static final int CAMERA_VIDEO_HEIGHT = 720;

    private RecordBinder    mBinder      = null;
    private Camera          mCamDev      = null;
    private SurfaceHolder   mHolder      = null;
    private SurfaceView     mSurViewNull = null;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        mBinder      = new RecordBinder();
        mSurViewNull = new SurfaceView(this);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        if (mCamDev != null) {
            mCamDev.stopPreview();
            mCamDev.release();
            mCamDev = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public class RecordBinder extends Binder {
        public void setPreviewSurfaceHolder(SurfaceHolder holder) {
            if (holder == null) {
                holder = mSurViewNull.getHolder();
            }
            else {
                mHolder = holder;
            }
            try {
                mCamDev.setPreviewDisplay(holder);
                mCamDev.setPreviewCallback(mPreviewCallback);
                mCamDev.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void selectCamera(int n) {
            if (mCamDev != null) {
                mCamDev.stopPreview();
                mCamDev.setPreviewCallback(null);
                mCamDev.release();
            }

            switch (n) {
            case 0:
                mCamDev = Camera.open(0);
                break;
            case 1:
                mCamDev = Camera.open(1);
                break;
            case -1:
                mCamDev = null;
                return;
            }

            Camera.Parameters params = mCamDev.getParameters();
            params .setPreviewSize(CAMERA_VIDEO_WIDTH, CAMERA_VIDEO_HEIGHT);
            mCamDev.setParameters(params);
            setPreviewSurfaceHolder(mHolder);
        }
    }

    Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.d(TAG, "onPreviewFrame..");
        }
    };
}



package com.circle8.circleOne.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.circle8.circleOne.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AddQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    Button btnRescan ;
    ImageView imgBack ;
    ZXingScannerView mScannerView ;
    ViewGroup contentFrame ;
    String scanQr="",scanFormat="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qr);

        contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        btnRescan = (Button)findViewById(R.id.btnRescan);
        imgBack = (ImageView)findViewById(R.id.imgBack);

       /* mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        CameraScann();*/

        btnRescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mScannerView.stopCamera();
                CameraScann();
            }
        });

        mScannerView = new ZXingScannerView(this)
        {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
        CameraScann();

       /* Boolean aBoolean = Utility.checkCameraPermission(AddQRActivity.this);
        if (aBoolean == true) {*/

//            mScannerView.setResultHandler(this);
//            mScannerView.startCamera();
//      /*  }*/

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void handleResult(Result rawResult)
    {
//        Toast.makeText(this, "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString()+
//                ", Points = "+rawResult.getResultPoints(), Toast.LENGTH_SHORT).show();

        scanQr = rawResult.getText();
        scanFormat = rawResult.getBarcodeFormat().toString();
        AlertDisplay();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.resumeCameraPreview(QrActivity.this);
//            }
//        }, 2000);
    }


    public void AlertDisplay()
    {
//        String verify = sharedPreferences.getString("verify","");
        AlertDialog.Builder alert = new AlertDialog.Builder(AddQRActivity.this);
        alert.setTitle("Add QR");
        alert.setMessage("Scan Item: " +scanQr);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface adialog, int which)
            {
                adialog.cancel();
                mScannerView.stopCamera();
                CameraScann();
            }
        });
//        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface adialog, int which)
//            {
//
//            }
//        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
//        CameraScreen();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        mScannerView.stopCamera();
//    }

    public void CameraScann()
    {
//        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},1);
        }
        else
        {
            mScannerView.startCamera();
        }        // Start camera<br />
    }

    private static class CustomViewFinderView extends ViewFinderView
    {
        public static final String TRADE_MARK_TEXT = "";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();
        public Paint mLaserPaint;
        public Paint mFinderMaskPaint;
        public Paint mBorderPaint;

        private final int mDefaultLaserColor = getResources().getColor(R.color.viewfinder_laser);
        private final int mDefaultMaskColor = getResources().getColor(R.color.viewfinder_mask);
        private final int mDefaultBorderColor = getResources().getColor(R.color.viewfinder_border);
        private final int mDefaultBorderStrokeWidth = getResources().getInteger(R.integer.viewfinder_border_width);
        private final int mDefaultBorderLineLength = getResources().getInteger(R.integer.viewfinder_border_length);

        private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
        private int scannerAlpha;
        private static final int POINT_SIZE = 10;
        private static final long ANIMATION_DELAY = 80l;

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init()
        {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
//            setSquareViewFinder(true);

            //set up laser paint
            mLaserPaint = new Paint();
            mLaserPaint.setColor(mDefaultLaserColor);
            mLaserPaint.setStyle(Paint.Style.FILL);

            //finder mask paint
            mFinderMaskPaint = new Paint();
            mFinderMaskPaint.setColor(mDefaultMaskColor);

            //border paint
            mBorderPaint = new Paint();
            mBorderPaint.setColor(mDefaultBorderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mDefaultBorderStrokeWidth);
            mBorderPaint.setAntiAlias(true);
        }

        @Override
        public void setLaserColor(int laserColor) {
            mLaserPaint.setColor(laserColor);
        }

        @Override
        public void setMaskColor(int maskColor) {
            mFinderMaskPaint.setColor(maskColor);
        }

        @Override
        public void setBorderColor(int borderColor) {
            mBorderPaint.setColor(borderColor);
        }

        @Override
        public void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            drawTradeMark(canvas);
            drawViewFinderMask(canvas);
            drawViewFinderBorder(canvas);
            drawLaser(canvas);
        }

        private void drawTradeMark(Canvas canvas)
        {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }

        public void drawViewFinderMask(Canvas canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            Rect framingRect = getFramingRect();

            canvas.drawRect(0, 0, width, framingRect.top, mFinderMaskPaint);
            canvas.drawRect(0, framingRect.top, framingRect.left, framingRect.bottom + 1, mFinderMaskPaint);
            canvas.drawRect(framingRect.right + 1, framingRect.top, width, framingRect.bottom + 1, mFinderMaskPaint);
            canvas.drawRect(0, framingRect.bottom + 1, width, height, mFinderMaskPaint);
        }

        public void drawViewFinderBorder(Canvas canvas) {
            Rect framingRect = getFramingRect();

            // Top-left corner
            Path path = new Path();
            path.moveTo(framingRect.left, framingRect.top + mBorderLineLength);
            path.lineTo(framingRect.left, framingRect.top);
            path.lineTo(framingRect.left + mBorderLineLength, framingRect.top);
            canvas.drawPath(path, mBorderPaint);

            // Top-right corner
            path.moveTo(framingRect.right, framingRect.top + mBorderLineLength);
            path.lineTo(framingRect.right, framingRect.top);
            path.lineTo(framingRect.right - mBorderLineLength, framingRect.top);
            canvas.drawPath(path, mBorderPaint);

            // Bottom-right corner
            path.moveTo(framingRect.right, framingRect.bottom - mBorderLineLength);
            path.lineTo(framingRect.right, framingRect.bottom);
            path.lineTo(framingRect.right - mBorderLineLength, framingRect.bottom);
            canvas.drawPath(path, mBorderPaint);

            // Bottom-left corner
            path.moveTo(framingRect.left, framingRect.bottom - mBorderLineLength);
            path.lineTo(framingRect.left, framingRect.bottom);
            path.lineTo(framingRect.left + mBorderLineLength, framingRect.bottom);
            canvas.drawPath(path, mBorderPaint);
        }

        public void drawLaser(Canvas canvas) {
            Rect framingRect = getFramingRect();

            // Draw a red "laser scanner" line through the middle to show decoding is active
            mLaserPaint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            int middle = framingRect.height() / 2 + framingRect.top;
            canvas.drawRect(framingRect.left + 2, middle - 1, framingRect.right - 1, middle + 2, mLaserPaint);

            postInvalidateDelayed(ANIMATION_DELAY,
                    framingRect.left - POINT_SIZE,
                    framingRect.top - POINT_SIZE,
                    framingRect.right + POINT_SIZE,
                    framingRect.bottom + POINT_SIZE);
        }
    }
}

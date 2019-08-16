package com.example.trucker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnTouchListener {
    public ImageView logoView;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        showDialog();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoView = (ImageView) findViewById(R.id.imageView);
        logoView.setOnTouchListener(this);

       RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(5000);

        logoView.startAnimation(animation);

  /*      final Handler handler = new Handler();

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(new Runnable()
                {
                    public void run()
                    {
                        showDialog();
                    }
                });
            }
        }, 5000); // 1sec
*/
    }

    public void showDialog(){
        final AlertDialog updateDialog = new AlertDialog
                .Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.now_version))
                .setMessage(getResources().getString(R.string.number_version_text))
                .setPositiveButton(getResources().getString(R.string.update),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goToPlayStore();
                            }
                        }).setNegativeButton(getResources().getString(R.string.no_thanks),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                               Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

      // Toast.makeText(getApplicationContext(), " ???? : -"+getSimNumber, Toast.LENGTH_LONG).show();





         /*                       TelephonyManager mngr = (TelephonyManager)getSystemService(Context.Telephony_service);
                                mngr.getDeviceId();
       */                     }
                        })
                .create();
        updateDialog.show();
    }

    public void goToPlayStore(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }



}

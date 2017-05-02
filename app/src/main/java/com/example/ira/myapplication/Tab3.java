package com.example.ira.myapplication;

import android.app.Notification;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.R.drawable;


public class Tab3 extends Fragment {

    Button startBtn, stopBtn, resumeBtn;
    ProgressBar progressBar, progressBar2;
    CountDownTimer countDownTimer;
    MyCountDownTimer myCountDownTimer;
    TextView text;
    int workCounter = 0;
    boolean workFlag;
    Intent notificationIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationIntent = new Intent(getActivity(), Tab3.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final View rootView = inflater.inflate(R.layout.tab3, container, false);

        startBtn = (Button) rootView.findViewById(R.id.startBtn);
        stopBtn = (Button) rootView.findViewById(R.id.stopBtn);
        resumeBtn = (Button) rootView.findViewById(R.id.resumeBtn);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        text = (TextView) rootView.findViewById(R.id.text);

        stopBtn.setVisibility(View.INVISIBLE);
        resumeBtn.setVisibility(View.INVISIBLE);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBtn.setVisibility(View.INVISIBLE);
                stopBtn.setVisibility(View.VISIBLE);

                startWork(10, false);
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeBtn.setVisibility(View.VISIBLE);
                stopBtn.setVisibility(View.INVISIBLE);
                myCountDownTimer.cancel();

            }
        });

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopBtn.setVisibility(View.VISIBLE);
                resumeBtn.setVisibility(View.INVISIBLE);

                String[] timeStr = text.getText().toString().split(":");

                int seconds = Integer.valueOf(timeStr[1]);
                int minutes = Integer.valueOf(timeStr[0]);

                int time = seconds + minutes * 60;

                if (workFlag == true) {
                    startWork(time, true);
                }
                else {
                    startBreak(time, true);
                }

            }
        });

        return rootView;
    }


    public void startWork(int time, boolean restart) {
        workFlag = true;
        workCounter++;
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);

        if (!restart) {
            progressBar.setMax(time);
        }

        NotificationManager notification = ( NotificationManager ) getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
        notification.cancelAll();

        myCountDownTimer = new MyCountDownTimer(time * 1000, 1000);
        myCountDownTimer.start();


    }

    public void startBreak(int time, boolean restart) {
        workFlag = false;
        progressBar.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        if (!restart) {
            progressBar2.setMax(time);
        }
        NotificationManager notification = ( NotificationManager ) getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
        notification.cancelAll();

        myCountDownTimer = new MyCountDownTimer(time * 1000, 1000);
        myCountDownTimer.start();

    }


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished / 1000);

            if(workFlag == true){
                progressBar.setProgress(progress);
            }
            else if(workFlag == false){
                progressBar2.setProgress(progress);
            }

            int seconds = (int) ((millisUntilFinished / 1000) % 60);
            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);

            text.setText(String.format("%02d:%02d", minutes, seconds));

            Intent notificationIntent = new Intent(getActivity(), Tab3.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT| PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notification = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

            if(workFlag == true) {


                Notification noti = new Notification.Builder(getActivity())
                        .setContentTitle("Working time: stay focused!")
                        .setContentText(minutes + " minutes left.")
                        .setContentIntent(contentIntent)
                        .setSmallIcon(drawable.ic_dialog_alert).build();

                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.notify(1, noti);
            }



            else{
                Notification noti = new Notification.Builder(getActivity())
                        .setContentTitle("Break time: you can relax!")
                        .setContentText(minutes + " minutes left.")
                        .setContentIntent(contentIntent).setSmallIcon(drawable.ic_dialog_alert).build();

                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.notify(0, noti);
            }
        }

        @Override
        public void onFinish() {
            NotificationManager notification = ( NotificationManager ) getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
            notification.cancelAll();

            Intent notificationIntent = new Intent();
            PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);

            if(workFlag == true) {
                Notification noti = new Notification.Builder(getActivity())
                        .setContentTitle("Working time finished!")
                        .setContentText("What next?")
                        .setContentIntent(contentIntent).setSmallIcon(drawable.ic_dialog_alert).build();

                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.notify(0, noti);
            }
            else{
                Notification noti = new Notification.Builder(getActivity())
                        .setContentTitle("Break time finished!")
                        .setContentText("What next?")
                        .setContentIntent(contentIntent).setSmallIcon(drawable.ic_dialog_alert).build();

                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.notify(0, noti);
            }

            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
            if(workFlag == true){
                progressBar.setProgress(0);
            }
            else if(workFlag == false){
                progressBar2.setProgress(0);
            }

            text.setText("00:00");

            if (workFlag == true){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                if (workCounter < 4){
                    builder1.setMessage("Take break now?");
                }
                else if (workCounter == 4){
                    builder1.setMessage("Take long break now?");
                }

                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (workCounter < 4) {
                                    startBreak(10, false);
                                }
                                else if (workCounter == 4) {
                                    workCounter = 0;
                                    startBreak(20, false);
                                }
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                text.setText("00:10");
                                stopBtn.setVisibility(View.INVISIBLE);

                                startBtn.setVisibility(View.VISIBLE);

                                NotificationManager notification = ( NotificationManager ) getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
                                notification.cancelAll();
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder1.create();
                alert.show();
            }
            else if(workFlag == false){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Start work now?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startWork(10, false);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startBtn.setVisibility(View.VISIBLE);
                                stopBtn.setVisibility(View.INVISIBLE);
                                text.setText("00:10");
                                NotificationManager notification = ( NotificationManager ) getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
                                notification.cancelAll();
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder1.create();
                alert.show();
            }
        }
    }
}
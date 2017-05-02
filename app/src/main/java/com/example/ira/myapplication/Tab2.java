package com.example.ira.myapplication;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Tab2 extends Fragment  {

    EditText editHrs, editMins, editSecs;
    Button startBtn, stopBtn, resetBtn, resumeBtn, resetBtn2;
    TextView text, text3, text4, text5, text6, text7;

    CountDownTimer countDownTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2, container, false);

        editHrs = (EditText) rootView.findViewById(R.id.editHours);
        editMins = (EditText) rootView.findViewById(R.id.editMinutes);
        editSecs = (EditText) rootView.findViewById(R.id.editSeconds);


        startBtn = (Button) rootView.findViewById(R.id.startCount);
        stopBtn = (Button) rootView.findViewById(R.id.stopCount);
        resetBtn = (Button) rootView.findViewById(R.id.resetCount);
        resetBtn2 = (Button) rootView.findViewById(R.id.reset2);
        resumeBtn = (Button) rootView.findViewById(R.id.resumeCount);

        text = (TextView) rootView.findViewById((R.id.textView));

        text3 = (TextView) rootView.findViewById((R.id.textView3));
        text4 = (TextView) rootView.findViewById((R.id.textView4));
        text5 = (TextView) rootView.findViewById((R.id.textView5));
        text6 = (TextView) rootView.findViewById((R.id.textView6));
        text7 = (TextView) rootView.findViewById((R.id.textView7));

        editHrs.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});

        editHrs.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        editMins.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
        editSecs.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});

        stopBtn.setVisibility(View.INVISIBLE);
        resetBtn.setVisibility(View.INVISIBLE);
        resetBtn2.setVisibility(View.INVISIBLE);
        resumeBtn.setVisibility(View.INVISIBLE);

        text.setVisibility(View.INVISIBLE);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text3.setVisibility(View.INVISIBLE);
                text4.setVisibility(View.INVISIBLE);
                text5.setVisibility(View.INVISIBLE);
                text6.setVisibility(View.INVISIBLE);
                text7.setVisibility(View.INVISIBLE);

                stopBtn.setVisibility(View.VISIBLE);
                resetBtn.setVisibility(View.VISIBLE);
                startBtn.setVisibility(View.INVISIBLE);

                editHrs.setVisibility(View.INVISIBLE);
                editMins.setVisibility(View.INVISIBLE);
                editSecs.setVisibility(View.INVISIBLE);


                text.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                    int seconds = Integer.valueOf(editSecs.getText().toString());
                    int minutes =  Integer.valueOf(editMins.getText().toString());
                    int hours =  Integer.valueOf(editHrs.getText().toString());

                    int time = seconds * 1000 + minutes * 60 * 1000 + hours * 3600 * 1000;


                    countDownTimer = new CountDownTimer(time , 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            int seconds = (int) ((millisUntilFinished / 1000) % 60);
                            int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                            int hours = (int) ((millisUntilFinished / (1000*60*60)) % 24);

                            text.setText(String.format("%02d:%02d:%02d",hours, minutes, seconds));
                        }

                        @Override
                        public void onFinish() {

                            text.setText("00:00:00");

                            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(2000);
                            stopBtn.setVisibility(View.INVISIBLE);
                            resetBtn.setVisibility(View.INVISIBLE);
                            resetBtn2.setVisibility(View.VISIBLE);
                            onStop();
                        }
                    }.start();
                //}

            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeBtn.setVisibility(View.VISIBLE);
                stopBtn.setVisibility(View.INVISIBLE);
                countDownTimer.cancel();

            }
        });

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopBtn.setVisibility(View.VISIBLE);
                resumeBtn.setVisibility(View.INVISIBLE);

                String[] timeStr = text.getText().toString().split(":");

                int seconds = Integer.valueOf(timeStr[2]);
                int minutes =  Integer.valueOf(timeStr[1]);
                int hours =  Integer.valueOf(timeStr[0]);


                int time = seconds * 1000 + minutes * 60 * 1000 + hours * 3600 * 1000;

                countDownTimer = new CountDownTimer(time , 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                        int seconds = (int) ((millisUntilFinished / 1000) % 60);
                        int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                        int hours = (int) ((millisUntilFinished / (1000*60*60)) % 24);


                        text.setText(String.format("%02d:%02d:%02d",hours, minutes, seconds));
                    }
                    @Override
                    public void onFinish() {

                        text.setText("00:00:00");

                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(2000);
                        //onStop();
                        stopBtn.setVisibility(View.INVISIBLE);
                        resetBtn.setVisibility(View.INVISIBLE);
                        resetBtn2.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();

                text3.setVisibility(View.VISIBLE);
                text4.setVisibility(View.VISIBLE);
                text5.setVisibility(View.VISIBLE);
                text6.setVisibility(View.VISIBLE);
                text7.setVisibility(View.VISIBLE);


                stopBtn.setVisibility(View.INVISIBLE);
                resetBtn.setVisibility(View.INVISIBLE);
                resumeBtn.setVisibility(View.INVISIBLE);
                resetBtn2.setVisibility(View.INVISIBLE);
                startBtn.setVisibility(View.VISIBLE);

                text.setVisibility(View.INVISIBLE);

                editHrs.setVisibility(View.VISIBLE);
                editMins.setVisibility(View.VISIBLE);
                editSecs.setVisibility(View.VISIBLE);

                editHrs.setText("00");
                editMins.setText("00");
                editSecs.setText("00");

            }
        });

        resetBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();

                text3.setVisibility(View.VISIBLE);
                text4.setVisibility(View.VISIBLE);
                text5.setVisibility(View.VISIBLE);
                text6.setVisibility(View.VISIBLE);
                text7.setVisibility(View.VISIBLE);

                stopBtn.setVisibility(View.INVISIBLE);
                resetBtn.setVisibility(View.INVISIBLE);
                resumeBtn.setVisibility(View.INVISIBLE);
                resetBtn2.setVisibility(View.INVISIBLE);
                startBtn.setVisibility(View.VISIBLE);

                text.setVisibility(View.INVISIBLE);

                editHrs.setVisibility(View.VISIBLE);
                editMins.setVisibility(View.VISIBLE);
                editSecs.setVisibility(View.VISIBLE);

                editHrs.setText("00");
                editMins.setText("00");
                editSecs.setText("00");

            }
        });
        return rootView;
    }

    /*@Override
    public void onDestroyView(){
        countDownTimer.cancel();
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        countDownTimer.cancel();
        super.onDestroy();
    }*/
}

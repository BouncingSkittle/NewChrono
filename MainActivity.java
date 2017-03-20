package uk.ac.napier.newchrono;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private TextView maGoalTime;
    private TextView maTimeDifference;
    private TextView maChronometer;
    private Button maStartButton;
    private Button maStopButton;

    private Context cContext;

    //variables for Chronometer class
    private Chronometer cChronometer;
    private Thread cThreadChronometer;

    String stringMaGoalTime;
    int intMaGoalTime;

    String stringMaChronometer;
    int intMaChronometer;

    String stringMaTimeDifference;
    String sub1MaTimeDifference;
    String sub2MaTimeDifference;
    int intMaTimeDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maGoalTime          = (TextView) findViewById(R.id.GoalTime);
        maTimeDifference    = (TextView) findViewById(R.id.TimeDifference);
        maChronometer       = (TextView) findViewById(R.id.Chronometer);

        maStartButton       = (Button) findViewById(R.id.StartButton);
        maStopButton        = (Button) findViewById(R.id.StopButton);

        cContext            = this;

        maStartButton.setVisibility(View.VISIBLE);
        maStopButton.setVisibility(View.GONE);


        maStartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Random random   = new Random();
                int second      = random.nextInt(60 - 0) + 0;
                int millisecond = random.nextInt(999 - 0) + 0;

                //start chronometer if object is null
                if(cChronometer == null)
                {
                    cChronometer        = new Chronometer(cContext);
                    cThreadChronometer  = new Thread(cChronometer); //thread expects something implemented in runnable interface

                    cThreadChronometer.start();
                    cChronometer.start();

                    maStartButton.setVisibility(View.GONE);
                    maStopButton.setVisibility(View.VISIBLE);

                    final Handler handler = new Handler();

                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            maChronometer.setVisibility(View.INVISIBLE);
                        }
                    },3000);

                    if(millisecond <= 9 && second <= 9)
                    {
                        maGoalTime.setText(0 + ":0" + second + ":00" + millisecond);
                    }

                    else if(millisecond <= 99 && millisecond >= 10 && second <= 9)
                    {
                        maGoalTime.setText(0 + ":0" + second + ":0" + millisecond);
                    }

                    else if(millisecond <= 9)
                    {
                        maGoalTime.setText(0 + ":" + second + ":00" + millisecond);
                    }

                    else if(millisecond <= 99 && millisecond >= 10)
                    {
                        maGoalTime.setText(0 + ":" + second + ":0" + millisecond);
                    }

                    else if(second <= 9)
                    {
                        maGoalTime.setText(0 + ":0" + second + ":" + millisecond);
                    }

                    else
                    {
                        maGoalTime.setText(0 + ":" + second + ":" + millisecond);
                    }
                }
            }
        });

        maStopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cChronometer != null)
                {
                    cChronometer.stop();
                    cThreadChronometer.interrupt();

                    cThreadChronometer  = null;
                    cChronometer        = null;

                    maStartButton.setVisibility(View.VISIBLE);
                    maStopButton.setVisibility(View.GONE);

                    stringMaGoalTime    = maGoalTime.getText().toString();
                    stringMaGoalTime    = stringMaGoalTime.replaceAll(":","");
                    intMaGoalTime       = Integer.parseInt(stringMaGoalTime);

                    stringMaChronometer = maChronometer.getText().toString();
                    stringMaChronometer = stringMaChronometer.replaceAll(":","");
                    intMaChronometer    = Integer.parseInt(stringMaChronometer);

                    System.out.println("Goaltime: " + intMaGoalTime + " / Chronometertime: " + intMaChronometer);



                    if(intMaChronometer > intMaGoalTime)
                    {
                        intMaTimeDifference = intMaChronometer - intMaGoalTime;
                    }

                    else if(intMaChronometer < intMaGoalTime)
                    {
                        intMaTimeDifference = intMaGoalTime - intMaChronometer;
                    }

                    else
                    {
                        intMaTimeDifference = 0;
                    }



                    if (intMaTimeDifference <= 9)
                    {
                        maTimeDifference.setText("0:00:00" + intMaTimeDifference);
                    }

                    else if (intMaTimeDifference <= 99 && intMaTimeDifference > 9)
                    {
                        maTimeDifference.setText("0:00:0" + intMaTimeDifference);
                    }

                    else if (intMaTimeDifference <= 999 && intMaTimeDifference > 99)
                    {
                        maTimeDifference.setText("0:00:" + intMaTimeDifference);
                    }

                    else if (intMaTimeDifference <= 9999 && intMaTimeDifference > 999)
                    {
                        stringMaTimeDifference  = Integer.toString(intMaTimeDifference);
                        sub1MaTimeDifference    = String.valueOf(stringMaTimeDifference).substring(0,1);
                        sub2MaTimeDifference    = String.valueOf(stringMaTimeDifference).substring(1,4);

                        maTimeDifference.setText("0:0" + sub1MaTimeDifference + ":" + sub2MaTimeDifference);
                    }

                    else if (intMaTimeDifference <= 99999 && intMaTimeDifference > 9999)
                    {
                        stringMaTimeDifference  = Integer.toString(intMaTimeDifference);
                        sub1MaTimeDifference    = String.valueOf(stringMaTimeDifference).substring(0,2);
                        sub2MaTimeDifference    = String.valueOf(stringMaTimeDifference).substring(2,5);

                        maTimeDifference.setText("0:" + sub1MaTimeDifference + ":" + sub2MaTimeDifference);
                    }
                }
                maChronometer.setVisibility(View.VISIBLE);
            }
        });
    }

    public void updateChronometer (final String time)
    {
        try
        {
            Thread.sleep(1);
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                maChronometer.setText(time);
            }
        });
    }
}
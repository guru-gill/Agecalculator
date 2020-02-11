package com.age.agecalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ImageView bgapp, clover;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;
    DatePicker picker;
    String dayName;
    Button selectbtn,ok,cl;
    private Button button;

    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);



        selectbtn=(Button)findViewById(R.id.selectbtn);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);

        bgapp.animate().translationY(-1900).setDuration(1000).setStartDelay(500);
        clover.animate().alpha(0).setDuration(900).setStartDelay(600);
        textsplash.animate().translationY(140).alpha(0).setDuration(1500).setStartDelay(400);

        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);
        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog, null);
                picker=(DatePicker)mView.findViewById(R.id.datePicker1);
                ok=(Button)mView.findViewById(R.id.ok);
                cl=(Button)mView.findViewById(R.id.cl);
                mBuilder.setView(mView);
//Set max date to be 3 years prior
                final int minAge = 1; //min age is 3 years old
                final Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -minAge);
                picker.setMaxDate(cal.getTimeInMillis());
                final AlertDialog dialog = mBuilder.create();

                dialog.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int dayPicked = picker.getDayOfMonth();
                        int monthPicked = picker.getMonth() + 1; //0 indexed
                        int yearPicked = picker.getYear();
                        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date myDate = inFormat.parse(dayPicked+"-"+monthPicked+"-"+yearPicked);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                            dayName=simpleDateFormat.format(myDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        final int selectedDates[] = {dayPicked, monthPicked, yearPicked};

                        Log.d(TAG, "Day: " + dayPicked + ", Month: " + monthPicked + ", Year: " + yearPicked);
                        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

                        if (currentYear - yearPicked <= minAge) {
                            showErrorMsg(view);
                        } else {
                            startCalc(selectedDates);
                        }
//                        Calendar calendar= Calendar.getInstance();
//                        int month =(picker.getMonth()+1);
//                        int day=picker.getDayOfMonth();
//                        int year=picker.getYear();
//                        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
//                        try {
//                            Date myDate = inFormat.parse(day+"-"+month+"-"+year);
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
//                            dayName=simpleDateFormat.format(myDate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        Intent intent = new Intent(MainActivity.this, MainPage.class);
//                        intent.putExtra("day", day);
//                        intent.putExtra("month", month);
//                        intent.putExtra("year", year);
//                        intent.putExtra("dayName",dayName);
//                        startActivity(intent);
//                        //Toast.makeText(getApplicationContext(),String.valueOf(dayName+" "+picker.getYear()+"/ "+month),Toast.LENGTH_LONG).show();
                     }
                });
                cl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
    private void showErrorMsg(View view) {
        Snackbar snackbar = Snackbar.make(view, "Sorry, you are too young " +
                "to use this app", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void startCalc(int[] selectedDates) {
        Intent intent = new Intent(this, MainPage.class);
        intent.putExtra("dates", selectedDates);
        intent.putExtra("day",dayName);
        startActivity(intent);
    }
}

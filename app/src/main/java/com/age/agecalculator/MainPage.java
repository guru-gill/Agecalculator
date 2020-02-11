package com.age.agecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainPage extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    TextView today,bdate,age,months,days,min,weeks,rdays;
    public static final String TAG = MainPage.class.getSimpleName();
    Calendar mNow=Calendar.getInstance(),endCalendar = Calendar.getInstance();
    private int[] mDates;
    Button home;
    private static final int DAY_INDEX = 0;
    private static final int MONTH_INDEX = 1;
    private static final int YEAR_INDEX = 2;
    public int startYear, startMonth, startDay;
    public static int endYear, endMonth, endDay;
    private int resultYear, resultMonth, resultDay;
    private String currentDay, dayOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        today = (TextView) findViewById(R.id.today);
        bdate=(TextView)findViewById(R.id.bdate);
        min=(TextView)findViewById(R.id.mins);
        weeks=(TextView)findViewById(R.id.weeks);
        age=(TextView)findViewById(R.id.age);
        months=(TextView)findViewById(R.id.months);
        rdays=(TextView)findViewById(R.id.rdays);
        days=(TextView)findViewById(R.id.days);
        home=(Button)findViewById(R.id.home);
today.setText(getCurrentDay());
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        final Intent intent = getIntent();
        mDates = intent.getIntArrayExtra("dates");
        String day=intent.getStringExtra("day");

  home.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          Intent intent = new Intent(MainPage.this, MainActivity.class);
          startActivity(intent);
      }
  });
//        startYear=intent.getIntExtra("year",2020);
//        startMonth=intent.getIntExtra("month",2);
//        startDay=intent.getIntExtra("day",1);
        bdate.setText(String.valueOf(day+"-"+ mDates[DAY_INDEX]+"/"+ mDates[MONTH_INDEX]+"/"+mDates[YEAR_INDEX]));
       age.setText(String.valueOf(getDiff(DifferenceIn.YEARS))+" Years");
       months.setText(String.valueOf(getDiff(DifferenceIn.MONTHS)+" Months"));
       days.setText(String.valueOf(getDiff(DifferenceIn.DAYS)+" Days"));
       weeks.setText(String.valueOf(getDiff(DifferenceIn.WEEKS)+" Weeks"));
       min.setText(String.valueOf(getDiff(DifferenceIn.SECONDS)+" Seconds"));
      int rday=  calculateRemainingDays(mDates[MONTH_INDEX],mDates[DAY_INDEX],mDates[YEAR_INDEX]);
        //Toast.makeText(getApplicationContext(),""+String.valueOf(rday),Toast.LENGTH_LONG).show();

      //  getDiff(DifferenceIn.MONTHS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentDay() {
        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH);

        //month starts from 0
        endMonth++;
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        currentDay = endCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        return currentDay + "- " + endDay + "/" + endMonth + "/" + endYear;
    }

    public long getDiff(DifferenceIn duration) {
        int currentMonth = mNow.get(Calendar.MONTH)+1; //+1 since months are 0 indexed
//        Log.d(TAG, "current month: " + currentMonth);

        int currentDayOfMonth = mNow.get(Calendar.DAY_OF_MONTH);
//        Log.d(TAG, "current day: " + currentDayOfYear);

        int currentYear = mNow.get(Calendar.YEAR);
//        Log.d(TAG, "current year: " + currentYear);

        LocalDate startDate = LocalDate.of(mDates[YEAR_INDEX], mDates[MONTH_INDEX], mDates[DAY_INDEX]);
        LocalDate endDate = LocalDate.of(currentYear, currentMonth, currentDayOfMonth);

        long diff = 0; //default value

        switch (duration) {
            case SECONDS:
                LocalDateTime startDateSec = LocalDateTime.of(mDates[YEAR_INDEX], mDates[MONTH_INDEX],
                        mDates[DAY_INDEX], 0, 0, 0);

                LocalDateTime endDateSec = LocalDateTime.of(currentYear, currentMonth,
                        currentDayOfMonth,
                        mNow.get(Calendar.HOUR_OF_DAY),
                        mNow.get(Calendar.MINUTE),
                        mNow.get(Calendar.SECOND));

                LocalDateTime temp = LocalDateTime.from(startDateSec);

                diff = temp.until(endDateSec, ChronoUnit.SECONDS);
                Log.d(TAG, "seconds diff: " + diff);
                break;

            case DAYS:
                diff = ChronoUnit.DAYS.between(startDate, endDate);
                Log.d(TAG, "days diff: " + diff);
                break;
            case YEARS:
                diff=(currentYear-mDates[YEAR_INDEX]);
                break;
            case WEEKS:
                diff = ChronoUnit.WEEKS.between(startDate, endDate);
                Log.d(TAG, "weeks diff: " + diff);
                break;

            case MONTHS:
                diff = ChronoUnit.MONTHS.between(startDate, endDate);
                Log.d(TAG, "months diff: " + diff);
                break;
        }

        return diff;
    }

    public int calculateRemainingDays(int selectedMonth, int selectedDay,int Years) {
//        int years = Calendar.YEAR;

        Date date1 = new Date();
        date1.setDate(selectedDay);
        date1.setMonth(selectedMonth);
//        date1.setYear(years);

//        years = years - 1; // Focus on the day between 0 - 365
        Date date2 = new Date();
        date2.setDate(endDay);
        date2.setMonth(endMonth);
//        date2.setYear(years);

        // find the difference between two dates
        long difference = date1.getTime() - date2.getTime();

        int remainingDays = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
//        remainingDays = Math.abs(remainingDays);
        Log.d("TAG", String.valueOf(remainingDays));
        if (remainingDays >= 365) {
            remainingDays = remainingDays - 365;
        } else if (remainingDays < 0)
            remainingDays = 365 - Math.abs(remainingDays);
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date myDate = inFormat.parse(selectedDay+"-"+selectedMonth+"-"+Years);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
          String  dayName=simpleDateFormat.format(myDate);
            rdays.setText(String.valueOf(remainingDays)+" Days"+" - Day of Birth is "+dayName);
         // Toast.makeText(getApplicationContext(),dayName,Toast.LENGTH_LONG).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return remainingDays;
    }
}
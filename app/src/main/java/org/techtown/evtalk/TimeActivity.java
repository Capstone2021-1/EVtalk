package org.techtown.evtalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeActivity extends AppCompatActivity {

    SimpleDateFormat simpleDateFormat;
    DoubleDateAndTimePickerDialog.Builder doubleBuilder;

    TextView startText;
    TextView endText;
    TextView totalText;

    public static String start_time="";
    public static String end_time="";
    public static String total_time="";

    public static final int TIMERESULTCODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_time);


        startText = (TextView) findViewById(R.id.startText);
        endText = (TextView) findViewById(R.id.endText);
        totalText = (TextView) findViewById(R.id.totalText);

        this.simpleDateFormat = new SimpleDateFormat("M월 d일 EEE HH:mm", Locale.getDefault());


        // 초기에 시간이 설정이 안되어 있다면
        if(start_time.equals("")) {
            Date currentTime = Calendar.getInstance().getTime();
            start_time = new SimpleDateFormat("M월 d일 EEE HH:mm", Locale.getDefault()).format(currentTime);
            end_time = new SimpleDateFormat("M월 d일 EEE HH:mm", Locale.getDefault()).format(currentTime);
            total_time = "0시간";
        }
        startText.setText(start_time);
        endText.setText(end_time);
        totalText.setText(total_time);



        // Time Setting Button
        Button setting = (Button) findViewById(R.id.double_button);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doubleClicked();
            }
        });
        // OK Button
        TextView buttonOK = (TextView) findViewById(R.id.buttonOk);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("start_time", start_time);  // 선택된 값 Intent로 전달
                intent.putExtra("end_time", end_time);  // 선택된 값 Intent로 전달
                intent.putExtra("total_time", total_time);  // 선택된 값 Intent로 전달

                setResult(TIMERESULTCODE, intent);
                finish();
            }
        });


    }

    public void doubleClicked() {

        final Date now = new Date();
        final Calendar calendarMin = Calendar.getInstance();
        final Calendar calendarMax = Calendar.getInstance();

        calendarMin.setTime(now); // Set min now
        calendarMax.setTime(new Date(now.getTime() + TimeUnit.DAYS.toMillis(150))); // Set max now + 150 days

        final Date minDate = calendarMin.getTime();
        final Date maxDate = calendarMax.getTime();

        doubleBuilder = new DoubleDateAndTimePickerDialog.Builder(this)
                .setTimeZone(TimeZone.getDefault())
                //.curved()

                .minutesStep(5)
                .mustBeOnFuture()

                .minDateRange(minDate)
                .maxDateRange(maxDate)

                .secondDateAfterFirst(true)

                //.defaultDate(now)
                .tab0Date(now)
                .tab1Date(new Date(now.getTime() + TimeUnit.HOURS.toMillis(1)))

                .title("날짜 및 시간 설정")
                .tab0Text("시작 시간")
                .tab1Text("종료 시간")

                .listener(new DoubleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(List<Date> dates) {
                        StringBuilder stringBuilder = new StringBuilder();
                        String[] results = new String[2];

                        int i = 0;
                        for (Date date : dates) {
                            stringBuilder.append(simpleDateFormat.format(date)).append("\n");
                        }
                        int middle = stringBuilder.indexOf("\n");

                        start_time = stringBuilder.substring(0, middle);
                        end_time = stringBuilder.substring(middle + 1);

                        startText.setText(start_time);
                        endText.setText(end_time);

                        long calDate = dates.get(1).getTime() - dates.get(0).getTime();
                        long result = calDate / (60 * 60 * 1000);
                        long result_min = (calDate - result * (60 * 60 * 1000)) / (60 * 1000);
                        total_time = Long.toString(result)+"시간" + Long.toString(result_min)+"분";

                        totalText.setText(total_time);


                    }
                });
        doubleBuilder.display();
    }


}
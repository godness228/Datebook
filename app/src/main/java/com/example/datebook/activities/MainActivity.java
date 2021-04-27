package com.example.datebook.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.datebook.R;
import com.example.datebook.models.Event;
import com.example.datebook.services.RealmHelper;
import com.example.datebook.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ArrayList<Event> events = new ArrayList<>();
    Calendar clickedDayCalendar;
    Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialData();

        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDayClickListener(eventDay -> {
            clickedDayCalendar = eventDay.getCalendar();

            RecyclerView recyclerView = findViewById(R.id.recyclerView);

            RealmHelper realmHelper = new RealmHelper(this);

            for (int i = 0; i < 24; i ++){

                Event event = realmHelper.getEventByTime(clickedDayCalendar.getTimeInMillis() + events.get(i).getEventStart());

                if (event != null) {
                    events.get(i).setEventName(event.getEventName());
                } else {
                    events.get(i).setEventName("");
                }
            }

            EventAdapter.OnEventClickListener eventClickListener = (event, position) -> {
                Intent intentActivity = new Intent(getApplicationContext(), EventMakerActivity.class);

                TextView textView = findViewById(R.id.eventStart);

                long getClickedTimeInMillis = getTimeInMillis(textView.getText().toString()) * TimeUtils.hourInMillis;

                Log.d("clickedTime", String.valueOf(getClickedTimeInMillis));

                Log.d("clickedTime123", String.valueOf(getTimeInMillis(textView.getText().toString())));

                long eventTimeStart = clickedDayCalendar.getTimeInMillis() + getClickedTimeInMillis;

                Log.d("calendarTime", String.valueOf(clickedDayCalendar.getTimeInMillis()));

                intentActivity.putExtra("eventStartInMilliseconds", events.get(position).getEventStart());
                intentActivity.putExtra("eventTimeInMilliseconds", eventTimeStart);

                startActivity(intentActivity);
            };

            EventAdapter adapter = new EventAdapter(getApplicationContext(), events, eventClickListener);
            recyclerView.setAdapter(adapter);
        });
    }

    private void setInitialData() {
        for (int i = 0; i < 24; i++) {

            Event event = new Event();

            event.setEventStart(i * TimeUtils.hourInMillis);
            event.setEventFinish((i + 1) * TimeUtils.hourInMillis);

            events.add(event);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        stopService(intentService);
        Log.d("Main Activity", "Realm closed");
    }

    private long getTimeInMillis(String time) {
        switch (time) {
            case "00:00":
                return 0;
            case "01:00":
                return 1;
            case "02:00":
                return 2;
            case "03:00":
                return 3;
            case "04:00":
                return 4;
            case "05:00":
                return 5;
            case "06:00":
                return 6;
            case "07:00":
                return 7;
            case "08:00":
                return 8;
            case "09:00":
                return 9;
            case "10:00":
                return 10;
            case "11:00":
                return 11;
            case "12:00":
                return 12;
            case "13:00":
                return 13;
            case "14:00":
                return 14;
            case "15:00":
                return 15;
            case "16:00":
                return 16;
            case "17:00":
                return 17;
            case "18:00":
                return 18;
            case "19:00":
                return 19;
            case "20:00":
                return 20;
            case "21:00":
                return 21;
            case "22:00":
                return 22;
            case "23:00":
                return 23;
        }
        return 1;
    }
}
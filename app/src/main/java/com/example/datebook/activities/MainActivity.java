package com.example.datebook.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.datebook.R;
import com.example.datebook.models.Event;
import com.example.datebook.services.EventsService;
import com.example.datebook.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    ArrayList<Event> events = new ArrayList<>();
    Calendar clickedDayCalendar;

    private EventsService eventsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventsService = new EventsService(this);

        setInitialData();

        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                clickedDayCalendar = eventDay.getCalendar();

                RecyclerView recyclerView = findViewById(R.id.recyclerView);

                for (int i = 0; i < 24; i ++){

                    Event event = eventsService.getEventByTime(clickedDayCalendar.getTimeInMillis() + events.get(i).getEventStart());

                    if (event != null) {
                        events.get(i).setEventName(event.getEventName());
                    } else {
                        events.get(i).setEventName("");
                    }
                }

                EventAdapter.OnEventClickListener eventClickListener = new EventAdapter.OnEventClickListener() {
                    @Override
                    public void onEventClick(Event event, int position) {
                        Intent intent = new Intent(getApplicationContext(), EventMakerActivity.class);

                        TextView textView = findViewById(R.id.eventStart);

                        long getClickedTimeInMill = getTimeInMill(textView.getText().toString()) * TimeUtils.hourInMill;

                        long eventTimeStart = clickedDayCalendar.getTimeInMillis() + getClickedTimeInMill;

                        intent.putExtra("eventStartInMillis", events.get(position).getEventStart());
                        intent.putExtra("eventTimeInMill", eventTimeStart);

                        startActivity(intent);
                    }
                };

                EventAdapter adapter = new EventAdapter(getApplicationContext(), events, eventClickListener);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void setInitialData() {
        for (int i = 0; i < 24; i++) {

            Event event = new Event();

            event.setEventStart(i * TimeUtils.hourInMill);
            event.setEventFinish((i + 1) * TimeUtils.hourInMill);

            events.add(event);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        eventsService.destroy();
        Log.d("Main Activity", "Realm closed");
    }

    private long getTimeInMill(String time) {
            if(time.charAt(1) != ':'){
                return time.charAt(0);
            }
            if (time.charAt(0) == 1) {
                return (10 + time.charAt(1));
            }
        return (20 + time.charAt(1));
    }

}
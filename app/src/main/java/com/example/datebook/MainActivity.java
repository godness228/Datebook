package com.example.datebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import java.util.zip.Inflater;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ArrayList<Event> events = new ArrayList<>();
    Calendar clickedDayCalendar;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        realm = Realm.getInstance(new RealmConfiguration.Builder().build());
        try {
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }


        setInitialData();

        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                clickedDayCalendar = eventDay.getCalendar();

                RecyclerView recyclerView = findViewById(R.id.recyclerView);

                for (int i = 0; i < 24; i ++){

                    Event time = realm.where(Event.class).equalTo("eventStart", clickedDayCalendar.getTimeInMillis() + events.get(i).getEventStart()).findFirst();

                    if (time != null) {
                        events.get(i).setEventName(time.getEventName());
                    } else {
                        events.get(i).setEventName("");
                    }
                }

                EventAdapter.OnEventClickListener eventClickListener = new EventAdapter.OnEventClickListener() {
                    @Override
                    public void onEventClick(Event event, int position) {
                        Intent intent = new Intent(getApplicationContext(), EventMaker.class);

                        TextView textView = findViewById(R.id.eventStart);

                        long getClickedTimeInMill = getTimeInMill(textView.getText().toString()) * Convert.hourInMill;

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

            event.setEventStart(i * Convert.hourInMill);
            event.setEventFinish((i + 1) * Convert.hourInMill);

            events.add(event);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        realm.close();
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
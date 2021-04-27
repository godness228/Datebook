package com.example.datebook.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.datebook.R;
import com.example.datebook.models.Event;
import com.example.datebook.services.RealmHelper;
import com.example.datebook.utils.JSONHelper;
import com.example.datebook.utils.TimeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class EventMakerActivity extends AppCompatActivity {
    private EditText editName;
    private EditText editDescription;

    private long eventTime;
    private long eventStart;

    private List<Event> events;

    private RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        Bundle arguments = getIntent().getExtras();
        eventStart = arguments.getLong("eventStartInMilliseconds");
        eventTime = arguments.getLong("eventTimeInMilliseconds");

        TextView startView = findViewById(R.id.eventStart);
        TextView finishView = findViewById(R.id.eventFinish);
        editName = findViewById(R.id.editEventName);
        editDescription = findViewById(R.id.editEventDescription);

        startView.setText(TimeUtils.getHoursString(eventStart));
        finishView.setText(TimeUtils.getHoursString(eventStart + (TimeUtils.hourInMillis)));

        events = new ArrayList<>();
        realmHelper = new RealmHelper(this);

        FloatingActionButton fab = findViewById(R.id.fabSaveEvent);

        fab.setOnClickListener(v -> {
            if(!editName.getText().toString().equals("")) {
                addEvent();
                JSONHelper.exportToJSON(getApplicationContext(), events);
                realmHelper.saveToRealm(getApplicationContext());

                finish();
            }
        });

    }

    private void addEvent(){
        RealmResults<Event> realmEvents = realmHelper.getAllEventsFromRealm();

        int id = 0;
        if(!realmEvents.isEmpty()){
            id =  realmEvents.get(realmEvents.size() - 1).getId();
        }
        String name = editName.getText().toString();
        String description = editDescription.getText().toString();

        Event event = new Event();

        event.setId(id);
        event.setEventName(name);
        event.setEventStart(eventTime + eventStart);
        event.setEventFinish(eventTime + (TimeUtils.hourInMillis) + eventStart);
        event.setDescription(description);

        events.add(event);
    }

}
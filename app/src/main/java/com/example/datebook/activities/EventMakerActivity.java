package com.example.datebook.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datebook.R;
import com.example.datebook.models.Event;
import com.example.datebook.services.EventsService;
import com.example.datebook.utils.JSONHelper;
import com.example.datebook.utils.TimeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EventMakerActivity extends AppCompatActivity {

    private final static String TAG = "LOG";

    private long eventTime;
    private long eventStart;

    TextView startView;
    TextView finishView;
    EditText editName;
    EditText editDescription;

    private List<Event> events;

    EventsService eventsService;

    Realm realm;

    EventMakerActivity(){
    }

    EventMakerActivity(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        Realm.init(this);
        realm = Realm.getInstance(new RealmConfiguration.Builder().build());

        Bundle arguments = getIntent().getExtras();
        eventStart = arguments.getLong("eventStartInMillis");
        eventTime = arguments.getLong("eventTimeInMill");

        startView = findViewById(R.id.eventStart);
        finishView = findViewById(R.id.eventFinish);
        editName = findViewById(R.id.editEventName);
        editDescription = findViewById(R.id.editEventDescription);

        events = new ArrayList<>();

        startView.setText(TimeUtils.getHoursString(eventStart));
        finishView.setText(TimeUtils.getHoursString(eventStart + (1000 * 60 * 60)));

        FloatingActionButton fab = findViewById(R.id.fabSaveEvent);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editName.getText().toString().equals("") && !editDescription.getText().toString().equals("")) {
                    addEvent();
                    Log.d(TAG, "Event added");
                    saveToJSON();
                    Log.d(TAG, "Saved to Json");
                    saveToRealm();
                    finish();
                }
            }
        });

    }

    private void addEvent(){
        int id = (int) (Math.random() * Integer.MAX_VALUE);
        String name = editName.getText().toString();
        String description = editDescription.getText().toString();

        Event event = new Event();

        event.setId(id);
        event.setEventName(name);
        event.setEventStart(eventTime + eventStart);
        event.setEventFinish(eventTime + (TimeUtils.hourInMill) + eventStart);
        event.setDescription(description);

        events.add(event);
    }

    private void saveToJSON(){

        boolean result = JSONHelper.exportToJSON(this, events);
    }

    private void saveToRealm() {
        Gson gson = new Gson();

        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(Event.class, gson.toJson(JSONHelper.importFromJSON(getApplicationContext())));
        realm.commitTransaction();
        Toast.makeText(getApplicationContext(), "Saved to realm", Toast.LENGTH_LONG).show();
    }




}
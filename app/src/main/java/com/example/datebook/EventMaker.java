package com.example.datebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmConfiguration.Builder;

public class EventMaker extends AppCompatActivity {

    private final static String TAG = "LOG";

    long eventTime;
    long eventStart;
    private List<Event> events;
    TextView startView;
    TextView finishView;
    EditText editName;
    EditText editDescription;
    Realm realm;



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

        startView.setText(Convert.convertMilToString(eventStart));
        finishView.setText(Convert.convertMilToString(eventStart + (1000 * 60 * 60)));

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
        event.setEventFinish(eventTime + (1000 * 60 * 60) + eventStart);
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
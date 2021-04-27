package com.example.datebook.services;

import android.content.Context;

import com.example.datebook.activities.MainActivity;
import com.example.datebook.models.Event;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EventsService {

    private Realm realm;

    public EventsService(Context context) {
        Realm.init(context);

        realm = Realm.getInstance(new RealmConfiguration.Builder().build());
    }

    public void destroy() {
        realm.close();
    }

    @Nullable
    public Event getEventByTime(long timestamp) {
        return realm.where(Event.class).equalTo("eventStart", timestamp).findFirst();
    }
}

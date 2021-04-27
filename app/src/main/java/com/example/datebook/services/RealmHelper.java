package com.example.datebook.services;

import android.content.Context;

import com.example.datebook.activities.MainActivity;
import com.example.datebook.models.Event;
import com.example.datebook.utils.JSONHelper;
import com.google.gson.Gson;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    private final Realm realm;

    public RealmHelper(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public void saveToRealm(Context context) {
        Gson gson = new Gson();

        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(Event.class, gson.toJson(JSONHelper.importFromJSON(context)));
        realm.commitTransaction();
    }

    public RealmResults<Event> getAllEventsFromRealm() {
        return realm.where(Event.class).findAll();
    }

    @Nullable
    public Event getEventByTime(long timestamp) {
        return realm.where(Event.class).equalTo("eventStart", timestamp).findFirst();
    }

}

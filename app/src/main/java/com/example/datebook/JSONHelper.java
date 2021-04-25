package com.example.datebook;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

public class JSONHelper {
    private static final String FILE_NAME = "eventDetails.json";

    static boolean exportToJSON(Context context, List<Event> events) {
        Gson gson = new Gson();
        EventInfo eventInfo = new EventInfo();
        eventInfo.setEventDetails(events);
        String jsonString = gson.toJson(eventInfo);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    static List<Event> importFromJSON(Context context) {

        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = context.openFileInput(FILE_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            EventInfo eventInfo = gson.fromJson(streamReader, EventInfo.class);
            Log.d("JSONHelper", gson.toJson(eventInfo.getEventDetails()));
            return eventInfo.getEventDetails();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static class EventInfo {
        private List<Event> eventDetails;

        List<Event> getEventDetails() {
            return eventDetails;
        }

        void setEventDetails(List<Event> eventDetails) {
            this.eventDetails = eventDetails;
        }
    }
}
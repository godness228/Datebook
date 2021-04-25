package com.example.datebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    interface OnEventClickListener{
        void onEventClick(Event event, int position);
    }

    private final OnEventClickListener onClickListener;

    private final LayoutInflater inflater;
    private final List<Event> events;

    EventAdapter(Context context, List<Event> events, OnEventClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.events = events;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.table_of_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventStart.setText(Convert.convertMilToString(event.getEventStart()));
        holder.eventFinish.setText(Convert.convertMilToString(event.getEventFinish()));
        holder.eventName.setText(event.getEventName());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onEventClick(event, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView eventName;
        final TextView eventStart;
        final TextView eventFinish;

        ViewHolder(View view){
            super(view);
            eventName = view.findViewById(R.id.eventName);
            eventStart = view.findViewById(R.id.eventStart);
            eventFinish = view.findViewById(R.id.eventFinish);
        }
    }

}

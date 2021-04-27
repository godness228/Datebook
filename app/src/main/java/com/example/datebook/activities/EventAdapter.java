package com.example.datebook.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.models.Event;
import com.example.datebook.utils.TimeUtils;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    public interface OnEventClickListener{
        void onEventClick(Event event, int position);
    }

    private final OnEventClickListener onClickListener;

    private final LayoutInflater inflater;
    private final List<Event> events;

    public EventAdapter(Context context, List<Event> events, OnEventClickListener onClickListener) {
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
        holder.eventStart.setText(TimeUtils.getHoursString(event.getEventStart()));
        holder.eventFinish.setText(TimeUtils.getHoursString(event.getEventFinish()));
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

package com.example.busik.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.other.Trip;

import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder>  {

    private List<Trip> mTrips;

    public HistoryListAdapter(List<Trip> trips) {
        mTrips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = mTrips.get(position);
        holder.bind(trip);
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView routeStart;
        private TextView routeEnd;
        private TextView startTime;
        private TextView endTime;
        private TextView status;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routeStart = itemView.findViewById(R.id.route_start);
            routeEnd = itemView.findViewById(R.id.route_end);
            startTime = itemView.findViewById(R.id.time_start);
            endTime = itemView.findViewById(R.id.time_end);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date_text);
        }

        public void bind(Trip trip) {
            routeStart.setText(trip.getRoute().split("-")[0]);
            routeEnd.setText(trip.getRoute().split("-")[1]);
            startTime.setText(trip.getStartTime());
            endTime.setText(trip.getEndTime());
            date.setText("Дата поездки: 14.05.2023");
            status.setText("Статус заказа: Активен");
        }
    }

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }
}

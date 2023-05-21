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

    private List<Trip> trips;
    private List<Integer> arrives;
    private OnTripClickListener onTripClickListener;

    public HistoryListAdapter(List<Trip> trips, List<Integer> arrives, OnTripClickListener onTripClickListener) {
        this.trips = trips;
        this.onTripClickListener = onTripClickListener;
        this.arrives = arrives;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);
        int arrived = arrives.get(position);
        holder.bind(trip, arrived, onTripClickListener);
    }

    @Override
    public int getItemCount() {
        return trips.size();
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

        public void bind(Trip trip, int arrived, OnTripClickListener onTripClickListener) {
            routeStart.setText(trip.getRoute().split("-")[0]);
            routeEnd.setText(trip.getRoute().split("-")[1]);
            startTime.setText(trip.getStartTime());
            endTime.setText(trip.getEndTime());
            date.setText("Дата поездки: " + trip.getDate());
            if(arrived == 2)
                status.setText("Статус заказа: Не явился");
            else if(trip.getFinished() == 1)
                status.setText("Статус заказа: Завершен");
            else if(arrived == 1)
                status.setText("Статус заказа: В процессе");
            else
                status.setText("Статус заказа: Активен");
            itemView.setOnClickListener(v -> onTripClickListener.onTripClick(trip));
        }
    }

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }
}

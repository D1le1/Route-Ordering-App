package com.example.busik.operator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.other.Trip;

import org.json.JSONException;

import java.util.List;

public class OperatorTripListAdapter extends RecyclerView.Adapter<OperatorTripListAdapter.ViewHolder>  {

    private List<Trip> mTrips;
    private OnTripClickListener onTripClickListener;

    public OperatorTripListAdapter(List<Trip> trips, OnTripClickListener onTripClickListener) {
        mTrips = trips;
        this.onTripClickListener = onTripClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.operator_trip_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = mTrips.get(position);
        holder.bind(trip, onTripClickListener);
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
            status = itemView.findViewById(R.id.driver_name);
            date = itemView.findViewById(R.id.date_text);
        }

        public void bind(Trip trip, OnTripClickListener onTripClickListener) {
            routeStart.setText(trip.getRoute().split("-")[0]);
            routeEnd.setText(trip.getRoute().split("-")[1]);
            startTime.setText(trip.getStartTime());
            endTime.setText(trip.getEndTime());
            date.setText("Дата поездки: " + trip.getDate());
            status.setText("Водитель: " + trip.getDriverName());
            itemView.setOnClickListener(v -> onTripClickListener.onTripClick(trip));
        }
    }

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }
}

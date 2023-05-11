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

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder>  {

    private List<Trip> mTrips;
    private OnTripClickListener mClickListener;

    public TripListAdapter(List<Trip> trips, OnTripClickListener clickListener) {
        mTrips = trips;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = mTrips.get(position);
        holder.bind(trip, mClickListener);
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
        private TextView seats;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routeStart = itemView.findViewById(R.id.route_start);
            routeEnd = itemView.findViewById(R.id.route_end);
            startTime = itemView.findViewById(R.id.time_start);
            endTime = itemView.findViewById(R.id.time_end);
            seats = itemView.findViewById(R.id.seat_numbers);
        }

        public void bind(Trip trip, OnTripClickListener clickListener) {
            routeStart.setText(trip.getRoute().split("-")[0]);
            routeEnd.setText(trip.getRoute().split("-")[1]);
            startTime.setText(trip.getStartTime());
            endTime.setText(trip.getEndTime());
            seats.setText("Кол-во свободных мест: " + trip.getSeats());
            itemView.setOnClickListener(v -> clickListener.onTripClick(trip));
        }
    }

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }
}

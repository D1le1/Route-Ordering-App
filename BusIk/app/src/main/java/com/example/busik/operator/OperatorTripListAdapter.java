package com.example.busik.operator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.other.Trip;

import java.util.List;

public class OperatorTripListAdapter extends RecyclerView.Adapter<OperatorTripListAdapter.ViewHolder> {

    private List<Trip> mTrips;
    private OnTripClickListener mClickListener;

    public OperatorTripListAdapter(List<Trip> trips, OnTripClickListener clickListener) {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextRoute;
        private TextView mTextTime;
        private TextView mTextPassengersCount;
        private TextView mTextDriver;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextRoute = itemView.findViewById(R.id.text_route);
            mTextTime = itemView.findViewById(R.id.text_time);
            mTextPassengersCount = itemView.findViewById(R.id.text_passengers_count);
            mTextDriver = itemView.findViewById(R.id.text_driver);
        }

        public void bind(Trip trip, OnTripClickListener clickListener) {
            mTextRoute.setText(trip.getRoute());
            itemView.setOnClickListener(v -> clickListener.onTripClick(trip));
        }
    }

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }
}

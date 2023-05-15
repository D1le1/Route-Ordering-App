package com.example.busik.operator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;

import java.util.List;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.ViewHolder>  {

    private List<Client> clients;
    private OnBusClickListener onDriverClickListener;

    public BusListAdapter(List<Client> clients, OnBusClickListener clickListener) {
        this.clients = clients;
        onDriverClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.bind(client, onDriverClickListener);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mark;
        private TextView number;
        private TextView color;
        private TextView driver;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mark = itemView.findViewById(R.id.bus_mark);
            number = itemView.findViewById(R.id.bus_number);
            color = itemView.findViewById(R.id.bus_color);
            driver = itemView.findViewById(R.id.driver_name);
        }

        public void bind(Client client, OnBusClickListener clickListener) {
            mark.setText("Марка маршрутного такси: " + client.getBus().split(" ")[1]);
            number.setText("Номер маршрутного такси: " + client.getBus().split(" ")[2] + " " + client.getBus().split(" ")[3]);
            color.setText("Цвет маршрутного такси: " + client.getBus().split(" ")[0]);
            driver.setText("Водитель: " + client.getName());
            itemView.setOnClickListener(v -> clickListener.onBusClick(client));
        }
    }

    public interface OnBusClickListener {
        void onBusClick(Client client);
    }
}

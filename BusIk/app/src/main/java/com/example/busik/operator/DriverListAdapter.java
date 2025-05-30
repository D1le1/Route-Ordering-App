package com.example.busik.operator;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.other.CryptoUtils;
import com.example.busik.other.Trip;

import java.util.List;

public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.ViewHolder>  {

    private List<Client> clients;
    private OnDriverClickListener onDriverClickListener;

    public DriverListAdapter(List<Client> clients, OnDriverClickListener clickListener) {
        this.clients = clients;
        onDriverClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_item, parent, false);
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

        private TextView name;
        private TextView phone;
        private TextView bus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.driver_name);
            phone = itemView.findViewById(R.id.phone_number);
            bus = itemView.findViewById(R.id.bus_name);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Client client, OnDriverClickListener clickListener) {
            name.setText("Фамилия Имя: " + client.getName());
            phone.setText("Номер телефона: " + CryptoUtils.decrypt(client.getPhone()));
            if(client.getBus().get(0).equals("Нет закрепленного авто"))
                bus.setText("Маршрутка: " + client.getBus().get(0));
            else
                bus.setText("Маршрутка: " + client.getBus().get(0) + " " + client.getBus().get(1) + " " + client.getBus().get(2));
            itemView.setOnClickListener(v -> clickListener.onDriverClick(client));
        }
    }

    public interface OnDriverClickListener {
        void onDriverClick(Client client);
    }
}

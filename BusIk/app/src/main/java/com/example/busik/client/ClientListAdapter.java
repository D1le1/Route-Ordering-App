package com.example.busik.client;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.other.CryptoUtils;
import com.example.busik.other.Trip;
import com.example.busik.servertasks.MarkClientTask;

import java.util.Collections;
import java.util.List;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ClientViewHolder> {

    private List<Client> clients;
    private Trip trip;
    private Context context;

    public ClientListAdapter(List<Client> clients, Trip trip, Context context) {
        this.clients = clients;
        this.trip = trip;
        this.context = context;
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_item, parent, false);
        return new ClientViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.mNameTextView.setText("Имя пассажира: " + client.getName());
        holder.mAddressTextView.setText("Адрес: " + client.getAddress());
        holder.mPhoneTextView.setText("Телефон: " + CryptoUtils.decrypt(client.getPhone()));
        if(client.getArrived() != 0)
            holder.mClientCard.setCardBackgroundColor(client.getArrived() == 1 ? 0xCC1AF876 : 0xCCFF0000);

        holder.mArrivedButton.setOnClickListener(view -> {
            new MarkClientTask(client, this, trip, position, context).execute(1);
        });

        holder.mNotArrivedButton.setOnClickListener(view -> {
            new MarkClientTask(client, this, trip, position, context).execute(2);
        });
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder {

        public TextView mNameTextView;
        public TextView mAddressTextView;
        public TextView mPhoneTextView;
        public Button mArrivedButton;
        public Button mNotArrivedButton;
        public CardView mClientCard;

        public ClientViewHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.client_name);
            mAddressTextView = itemView.findViewById(R.id.client_address);
            mPhoneTextView = itemView.findViewById(R.id.client_phone);
            mArrivedButton = itemView.findViewById(R.id.arrived_button);
            mNotArrivedButton = itemView.findViewById(R.id.not_arrived_button);
            mClientCard = itemView.findViewById(R.id.client_card);
        }
    }
}
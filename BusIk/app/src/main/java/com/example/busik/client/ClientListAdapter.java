package com.example.busik.client;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.servertasks.MarkClientTask;

import java.util.List;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ClientViewHolder> {

    private List<Client> mClientList;
    private int mTripId;

    public ClientListAdapter(List<Client> clientList, int tripId) {
        mClientList = clientList;
        mTripId = tripId;
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_item, parent, false);
        return new ClientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        Client client = mClientList.get(position);
        holder.mNameTextView.setText("Имя клиента: " + client.getName());
        holder.mAddressTextView.setText("Адрес: " + client.getAddress());
        holder.mPhoneTextView.setText("Телефон: " + client.getPhone());
        if(client.getArrived() != 0)
            holder.mClientCard.setCardBackgroundColor(client.getArrived() == 1 ? 0xCC1AF876 : 0xCCFF0000);

        holder.mArrivedButton.setOnClickListener(view -> {
            new MarkClientTask(client, this, position).execute(mTripId, client.getId(), 1, position);
        });

        holder.mNotArrivedButton.setOnClickListener(view -> {
            new MarkClientTask(client, this, position).execute(mTripId, client.getId(), 2, position);
        });
    }

    @Override
    public int getItemCount() {
        return mClientList.size();
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
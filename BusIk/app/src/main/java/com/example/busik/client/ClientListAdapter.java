package com.example.busik.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;

import java.util.List;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ClientViewHolder> {

    private List<Client> mClientList;

    public ClientListAdapter(List<Client> clientList) {
        mClientList = clientList;
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

        holder.mArrivedButton.setOnClickListener(view -> {
            // Обработчик нажатия на кнопку "Пришел"
            client.setArrived(true);
            // Вызов метода для обновления отображения элемента списка
            notifyItemChanged(position);
        });

        holder.mNotArrivedButton.setOnClickListener(view -> {
            // Обработчик нажатия на кнопку "Не пришел"
            client.setArrived(false);
            // Вызов метода для обновления отображения элемента списка
            notifyItemChanged(position);
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

        public ClientViewHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.client_name);
            mAddressTextView = itemView.findViewById(R.id.client_address);
            mPhoneTextView = itemView.findViewById(R.id.client_phone);
            mArrivedButton = itemView.findViewById(R.id.arrived_button);
            mNotArrivedButton = itemView.findViewById(R.id.not_arrived_button);
        }
    }
}
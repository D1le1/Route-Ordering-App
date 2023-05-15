package com.example.busik.operator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.other.Trip;
import com.example.busik.servertasks.MarkClientTask;

import java.util.List;

public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.ClientViewHolder> {

    private List<Client> clients;

    public ApplicationListAdapter(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.application_item, parent, false);
        return new ClientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.bind(client);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView phone;
        private TextView role;

        public ClientViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.client_name);
            phone = itemView.findViewById(R.id.client_phone);
            role = itemView.findViewById(R.id.client_role);
        }

        public void bind(Client client)
        {
            name.setText("Фамилия Имя: " + client.getName());
            if(client.getRole() == 2)
                role.setText("Должность: Водитель");
            else
                role.setText("Должность: Оператор");
            phone.setText("Номер телефона: " + client.getPhone());
        }
    }
}
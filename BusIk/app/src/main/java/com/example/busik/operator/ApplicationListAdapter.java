package com.example.busik.operator;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;
import com.example.busik.other.CryptoUtils;
import com.example.busik.servertasks.ApplicationTask;

import java.util.List;

public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.ApplicationViewHolder> {

    private List<Client> clients;

    public ApplicationListAdapter(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.application_item, parent, false);
        return new ApplicationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.bind(client, position, this);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ApplicationViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView phone;
        private TextView role;

        private Button accept;
        private Button decline;

        public ApplicationViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.client_name);
            phone = itemView.findViewById(R.id.client_phone);
            role = itemView.findViewById(R.id.client_role);
            accept = itemView.findViewById(R.id.accept_button);
            decline = itemView.findViewById(R.id.decline_button);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Client client, int position, ApplicationListAdapter adapter)
        {
            name.setText("Фамилия Имя: " + client.getName());
            if(client.getRole() == 2)
                role.setText("Должность: Водитель");
            else
                role.setText("Должность: Оператор");
            phone.setText("Номер телефона: " + CryptoUtils.decrypt(client.getPhone()));

            accept.setOnClickListener(v -> new ApplicationTask(client, adapter, clients, position).execute(1));
            decline.setOnClickListener(v -> new ApplicationTask(client, adapter, clients, position).execute(0));
        }
    }
}
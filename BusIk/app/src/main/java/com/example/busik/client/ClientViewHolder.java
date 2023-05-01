package com.example.busik.client;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;

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
package com.example.busik.operator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busik.R;
import com.example.busik.client.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.ViewHolder>  {

    private List<JSONObject> objects;
    private OnBusClickListener onDriverClickListener;

    public BusListAdapter(List<JSONObject> objects, OnBusClickListener clickListener) {
        this.objects = objects;
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
        JSONObject object = objects.get(position);
        holder.bind(object, onDriverClickListener);
    }

    @Override
    public int getItemCount() {
        return objects.size();
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

        public void bind(JSONObject object, OnBusClickListener clickListener) {
            try {
                mark.setText("Марка маршрутного такси: " + object.getString("mark"));
                number.setText("Номер маршрутного такси: " + object.getString("number"));
                color.setText("Цвет маршрутного такси: " + object.getString("color"));
                String driverName = object.has("name") ? object.getString("name") : "Нет закрепленного водителя";
                driver.setText("Водитель: " + driverName);
                itemView.setOnClickListener(v -> clickListener.onBusClick(object));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public interface OnBusClickListener {
        void onBusClick(JSONObject object);
    }
}

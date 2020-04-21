package com.example.itpreminder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itpreminder.R;
import com.example.itpreminder.interfaces.OnItemClickListener;
import com.example.itpreminder.model.Car;
import com.example.itpreminder.utils.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CancellationException;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {

    private List<Car> carList;
    private Context context;
    private OnItemClickListener listener;

    public CarAdapter(List<Car> carList, Context context) {
        this.carList = carList;
        this.context = context;
    }

    @NonNull
    @Override
    public CarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_list_elements, parent,false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CarAdapter.MyViewHolder holder, int position) {
        holder.tvPhone.setText(carList.get(position).getPhoneNumber());
        holder.tvDoneDate.setText(carList.get(position).getDoneDate());
        holder.tvCarType.setText(carList.get(position).getCarType());
        holder.tvDuration.setText(String.valueOf(carList.get(position).getItpPeriod()));
        holder.tvPlateNumber.setText(carList.get(position).getPlateNumber());

        Date today = Calendar.getInstance().getTime();
        Date dateUntil = new Date();
        try {
            dateUntil = new SimpleDateFormat(Constant.DATE_PATTERN)
                    .parse(carList.get(position).getExpireDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (today.compareTo(dateUntil) > 0){
            holder.tvExpireDate.setTextColor(Color.RED);
        }

        holder.tvExpireDate.setText(carList.get(position).getExpireDate());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvPlateNumber, tvDuration, tvCarType, tvPhone, tvDoneDate, tvExpireDate;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener){
            super(itemView);
            tvPlateNumber = itemView.findViewById(R.id.plate_number_edit);
            tvDuration = itemView.findViewById(R.id.itp_duration_edit);
            tvCarType = itemView.findViewById(R.id.car_type_edit);
            tvDoneDate = itemView.findViewById(R.id.done_edit);
            tvExpireDate = itemView.findViewById(R.id.expire_edit);
            tvPhone = itemView.findViewById(R.id.phone_edit);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

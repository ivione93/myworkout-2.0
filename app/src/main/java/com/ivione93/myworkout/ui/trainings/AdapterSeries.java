package com.ivione93.myworkout.ui.trainings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ivione93.myworkout.R;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Series;

import java.util.List;

public class AdapterSeries extends RecyclerView.Adapter<AdapterSeries.ViewHolderSeries> {

    List<Series> listSeries;

    public AdapterSeries(List<Series> listSeries) {
        this.listSeries = listSeries;
    }

    @NonNull
    @Override
    public ViewHolderSeries onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_series, parent, false);
        return new ViewHolderSeries(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSeries holder, int position) {
        holder.showDistanceSerie.setText(listSeries.get(position).distance + " m");
        holder.showTimeSerie.setText(listSeries.get(position).time);
    }

    @Override
    public int getItemCount() {
        return listSeries.size();
    }

    public class ViewHolderSeries extends RecyclerView.ViewHolder {
        TextView showDistanceSerie, showTimeSerie;

        public ViewHolderSeries(@NonNull View itemView) {
            super(itemView);
            showDistanceSerie = itemView.findViewById(R.id.showDistanceSerie);
            showTimeSerie = itemView.findViewById(R.id.showTimeSerie);
        }
    }
}

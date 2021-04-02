package com.ivione93.myworkout.ui.trainings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.Training;

import java.util.List;

public class AdapterTraining extends RecyclerView.Adapter<AdapterTraining.ViewHolderTrainings> {

    List<Training> listTrainings;

    public AdapterTraining(List<Training> listTrainings) { this.listTrainings = listTrainings; }

    @NonNull
    @Override
    public ViewHolderTrainings onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training, parent, false);
        return new ViewHolderTrainings(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTrainings holder, int position) {
        holder.itemTrainingDate.setText(Utils.toString(listTrainings.get(position).date));
        holder.itemTrainingTime.setText(listTrainings.get(position).warmup.time + " min");
        holder.itemTrainingDistance.setText(listTrainings.get(position).warmup.distance + " km");
        holder.itemTrainingPartial.setText(listTrainings.get(position).warmup.partial + " /km");
    }

    @Override
    public int getItemCount() {
        return listTrainings.size();
    }

    public class ViewHolderTrainings extends RecyclerView.ViewHolder {
        TextView itemTrainingDate, itemTrainingTime, itemTrainingDistance, itemTrainingPartial;

        public ViewHolderTrainings(@NonNull View itemView) {
            super(itemView);
            itemTrainingDate = itemView.findViewById(R.id.itemTrainingDate);
            itemTrainingTime = itemView.findViewById(R.id.itemTrainingTime);
            itemTrainingDistance = itemView.findViewById(R.id.itemTrainingDistance);
            itemTrainingPartial = itemView.findViewById(R.id.itemTrainingPartial);
        }
    }
}

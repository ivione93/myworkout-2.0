package com.ivione93.myworkout.ui.competitions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.Competition;

import java.util.List;

public class AdapterCompetition extends RecyclerView.Adapter<AdapterCompetition.ViewHolderCompetitions> {

    List<Competition> listCompetitions;

    public AdapterCompetition(List<Competition> listCompetitions) { this.listCompetitions = listCompetitions; }

    @NonNull
    @Override
    public ViewHolderCompetitions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_competition, parent, false);
        return new ViewHolderCompetitions(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCompetitions holder, int position) {
        holder.name.setText(listCompetitions.get(position).name);
        holder.place.setText(listCompetitions.get(position).place);
        holder.track.setText(listCompetitions.get(position).track);
        holder.result.setText(listCompetitions.get(position).result);
        holder.date.setText(Utils.toString(listCompetitions.get(position).date));
    }

    @Override
    public int getItemCount() {
        return listCompetitions.size();
    }

    public class ViewHolderCompetitions extends RecyclerView.ViewHolder {
        TextView name, place, track, result, date;

        public ViewHolderCompetitions(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.competitionNameText);
            place = itemView.findViewById(R.id.placeText);
            track = itemView.findViewById(R.id.trackText);
            result = itemView.findViewById(R.id.resultText);
            date = itemView.findViewById(R.id.dateText);
        }
    }
}
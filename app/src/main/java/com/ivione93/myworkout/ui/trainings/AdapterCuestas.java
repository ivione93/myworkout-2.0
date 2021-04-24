package com.ivione93.myworkout.ui.trainings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ivione93.myworkout.R;
import com.ivione93.myworkout.db.Cuestas;

import java.util.List;

public class AdapterCuestas extends RecyclerView.Adapter<AdapterCuestas.ViewHolderCuestas> {

    List<Cuestas> listCuestas;

    public AdapterCuestas(List<Cuestas> listCuestas) {
        this.listCuestas = listCuestas;
    }

    @NonNull
    @Override
    public ViewHolderCuestas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuestas, parent, false);
        return new ViewHolderCuestas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCuestas holder, int position) {
        holder.showTypeCuestas.setText(listCuestas.get(position).type);
        holder.showTimesCuestas.setText(String.valueOf(listCuestas.get(position).times));
    }

    @Override
    public int getItemCount() {
        return listCuestas.size();
    }

    public class ViewHolderCuestas extends RecyclerView.ViewHolder {
        TextView showTypeCuestas, showTimesCuestas;

        public ViewHolderCuestas(@NonNull View itemView) {
            super(itemView);
            showTypeCuestas = itemView.findViewById(R.id.showTypeCuestas);
            showTimesCuestas = itemView.findViewById(R.id.showTimesCuestas);
        }
    }
}

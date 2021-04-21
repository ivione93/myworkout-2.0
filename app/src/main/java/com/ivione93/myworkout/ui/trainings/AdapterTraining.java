package com.ivione93.myworkout.ui.trainings;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.ivione93.myworkout.R;
import com.ivione93.myworkout.Utils;
import com.ivione93.myworkout.db.AppDatabase;
import com.ivione93.myworkout.db.Training;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterTraining extends RecyclerView.Adapter<AdapterTraining.ViewHolderTrainings> implements Filterable {

    List<Training> listTrainings;
    List<Training> listTrainingsFull;

    public AdapterTraining(List<Training> listTrainings) {
        this.listTrainings = listTrainings;
        this.listTrainingsFull = new ArrayList<>(listTrainings);
    }

    @NonNull
    @Override
    public ViewHolderTrainings onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training, parent, false);
        return new ViewHolderTrainings(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTrainings holder, int position) {
        AppDatabase db = Room.databaseBuilder(holder.itemView.getContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        holder.itemTrainingDate.setText(Utils.toString(listTrainings.get(position).date));
        holder.itemTrainingTime.setText(listTrainings.get(position).warmup.time + " min");
        holder.itemTrainingDistance.setText(listTrainings.get(position).warmup.distance + " km");
        holder.itemTrainingPartial.setText(listTrainings.get(position).warmup.partial + " /km");

        holder.ibOptionsTraining.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.ibOptionsTraining);
            popup.inflate(R.menu.item_training_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_edit_training:
                        Intent newTraining = new Intent(holder.itemView.getContext(), NewTrainingActivity.class);
                        newTraining.putExtra("isNew", false);
                        newTraining.putExtra("license", listTrainings.get(position).license);
                        newTraining.putExtra("id", listTrainings.get(position).idTraining);
                        holder.itemView.getContext().startActivity(newTraining);
                        return true;
                    case R.id.menu_delete_training:
                        db.trainingDao().deleteTrainingByLicense(listTrainings.get(position).license, listTrainings.get(position).idTraining);
                        listTrainings.remove(position);
                        notifyItemRemoved(position);
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return listTrainings.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Training> filteredTrainings = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredTrainings.addAll(filteredTrainings);
            } else {
                String filterPattern = constraint.toString();
                for (Training training : listTrainingsFull) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String actualTrainingDate = sdf.format(training.date);
                    if (actualTrainingDate.equals(filterPattern)) {
                        filteredTrainings.add(training);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredTrainings;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listTrainings.clear();
            listTrainings.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };

    public class ViewHolderTrainings extends RecyclerView.ViewHolder {
        TextView itemTrainingDate, itemTrainingTime, itemTrainingDistance, itemTrainingPartial;
        ImageButton ibOptionsTraining;

        public ViewHolderTrainings(@NonNull View itemView) {
            super(itemView);
            itemTrainingDate = itemView.findViewById(R.id.itemTrainingDate);
            itemTrainingTime = itemView.findViewById(R.id.itemTrainingTime);
            itemTrainingDistance = itemView.findViewById(R.id.itemTrainingDistance);
            itemTrainingPartial = itemView.findViewById(R.id.itemTrainingPartial);
            ibOptionsTraining = itemView.findViewById(R.id.ibOptionsTraining);
        }
    }
}

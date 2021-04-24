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

public class AdapterTraining extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    List<Training> listTrainings;
    List<Training> listTrainingsFull;

    AppDatabase db;

    public AdapterTraining(List<Training> listTrainings) {
        this.listTrainings = listTrainings;
        this.listTrainingsFull = new ArrayList<>(listTrainings);
    }

    @Override
    public int getItemViewType(int position) {

        if (listTrainings.get(position).hasSeries == 0) {
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.item_training, parent, false);
            return new ViewHolderOne(view);
        }
        view = layoutInflater.inflate(R.layout.item_training_series, parent, false);
        return new ViewHolderTwo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        db = Room.databaseBuilder(holder.itemView.getContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        if (listTrainings.get(position).hasSeries == 0) {
            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            viewHolderOne.itemTrainingDate.setText(Utils.toString(listTrainings.get(position).date));
            viewHolderOne.itemTrainingTime.setText(listTrainings.get(position).warmup.time + " min");
            viewHolderOne.itemTrainingDistance.setText(listTrainings.get(position).warmup.distance + " km");
            viewHolderOne.itemTrainingPartial.setText(listTrainings.get(position).warmup.partial + " /km");

            viewHolderOne.ibOptionsTraining.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(holder.itemView.getContext(), viewHolderOne.ibOptionsTraining);
                popup.inflate(R.menu.item_training_menu);
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_edit_training:
                            Intent newTraining = new Intent(holder.itemView.getContext(), ViewTrainingActivity.class);
                            newTraining.putExtra("isNew", false);
                            newTraining.putExtra("license", listTrainings.get(position).license);
                            newTraining.putExtra("id", listTrainings.get(position).idTraining);
                            holder.itemView.getContext().startActivity(newTraining);
                            return true;
                        case R.id.menu_delete_training:
                            db.trainingDao().deleteTrainingByLicense(listTrainings.get(position).license, listTrainings.get(position).idTraining);
                            db.seriesDao().deleteSeries(listTrainings.get(position).license, listTrainings.get(position).idTraining);
                            db.cuestasDao().deleteCuestas(listTrainings.get(position).license, listTrainings.get(position).idTraining);
                            listTrainings.remove(position);
                            notifyItemRemoved(position);
                            return true;
                        default:
                            return false;
                    }
                });
                popup.show();
            });
        } else {
            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            viewHolderTwo.itemTrainingDate.setText(Utils.toString(listTrainings.get(position).date));
            viewHolderTwo.itemTrainingTime.setText(listTrainings.get(position).warmup.time + " min");
            viewHolderTwo.itemTrainingDistance.setText(listTrainings.get(position).warmup.distance + " km");
            viewHolderTwo.itemTrainingPartial.setText(listTrainings.get(position).warmup.partial + " /km");

            viewHolderTwo.ibOptionsTraining.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(holder.itemView.getContext(), viewHolderTwo.ibOptionsTraining);
                popup.inflate(R.menu.item_training_menu);
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_edit_training:
                            Intent newTraining = new Intent(holder.itemView.getContext(), ViewTrainingActivity.class);
                            newTraining.putExtra("isNew", false);
                            newTraining.putExtra("license", listTrainings.get(position).license);
                            newTraining.putExtra("id", listTrainings.get(position).idTraining);
                            holder.itemView.getContext().startActivity(newTraining);
                            return true;
                        case R.id.menu_delete_training:
                            db.trainingDao().deleteTrainingByLicense(listTrainings.get(position).license, listTrainings.get(position).idTraining);
                            db.seriesDao().deleteSeries(listTrainings.get(position).license, listTrainings.get(position).idTraining);
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
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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

    class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView itemTrainingDate, itemTrainingTime, itemTrainingDistance, itemTrainingPartial;
        ImageButton ibOptionsTraining;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            itemTrainingDate = itemView.findViewById(R.id.itemTrainingDate);
            itemTrainingTime = itemView.findViewById(R.id.itemTrainingTime);
            itemTrainingDistance = itemView.findViewById(R.id.itemTrainingDistance);
            itemTrainingPartial = itemView.findViewById(R.id.itemTrainingPartial);
            ibOptionsTraining = itemView.findViewById(R.id.ibOptionsTraining);
        }
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder {

        TextView itemTrainingDate, itemTrainingTime, itemTrainingDistance, itemTrainingPartial;
        ImageButton ibOptionsTraining;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            itemTrainingDate = itemView.findViewById(R.id.itemTrainingDate);
            itemTrainingTime = itemView.findViewById(R.id.itemTrainingTime);
            itemTrainingDistance = itemView.findViewById(R.id.itemTrainingDistance);
            itemTrainingPartial = itemView.findViewById(R.id.itemTrainingPartial);
            ibOptionsTraining = itemView.findViewById(R.id.ibOptionsTraining);
        }
    }

}

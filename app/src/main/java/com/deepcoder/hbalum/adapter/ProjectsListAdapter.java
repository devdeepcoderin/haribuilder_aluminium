package com.deepcoder.hbalum.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.deepcoder.hbalum.databinding.ItemProjectBinding;
import com.deepcoder.hbalum.datalayers.model.ProjectData;

import java.util.ArrayList;
import java.util.Objects;

public abstract class ProjectsListAdapter extends RecyclerView.Adapter<ProjectsListAdapter.MyViewHolder> {
    protected Activity context;
    private ArrayList<ProjectData> lstData;

    public abstract void onItemSelected(int position, ProjectData projectData);

    public abstract void onLongItemSelected(int position, ProjectData projectData);

    protected ProjectsListAdapter(Activity context, ArrayList<ProjectData> lstData) {
        this.context = context;
        this.lstData = new ArrayList<>(lstData);
    }

    @NonNull
    @Override
    public ProjectsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProjectBinding binding = ItemProjectBinding.inflate(android.view.LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsListAdapter.MyViewHolder holder, int position) {
        ProjectData projectData = lstData.get(position);
        holder.binding.tvTitle.setText(projectData.projectName);
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }
            onItemSelected(adapterPosition, lstData.get(adapterPosition));
        });

        holder.itemView.setOnLongClickListener(v -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return true;
            }
            onLongItemSelected(adapterPosition, lstData.get(adapterPosition));
            return true;
        });
    }

    public void updateData(ArrayList<ProjectData> lstProjectData) {
        ArrayList<ProjectData> updatedData = new ArrayList<>(lstProjectData);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return lstData.size();
            }

            @Override
            public int getNewListSize() {
                return updatedData.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return Objects.equals(
                        lstData.get(oldItemPosition).uuid,
                        updatedData.get(newItemPosition).uuid
                );
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ProjectData oldItem = lstData.get(oldItemPosition);
                ProjectData newItem = updatedData.get(newItemPosition);
                return Objects.equals(oldItem.projectName, newItem.projectName)
                        && Objects.equals(oldItem.projectNumber, newItem.projectNumber)
                        && Objects.equals(oldItem.fileName, newItem.fileName);
            }
        });
        lstData = updatedData;
        result.dispatchUpdatesTo(this);

    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }


    public static final class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemProjectBinding binding;

        MyViewHolder(ItemProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

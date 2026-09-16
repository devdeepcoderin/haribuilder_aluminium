package com.deepcoder.hbalum.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepcoder.hbalum.databinding.ItemProjectBinding;
import com.deepcoder.hbalum.datalayers.model.ProjectData;

import java.util.ArrayList;

public abstract class ProjectsListAdapter extends RecyclerView.Adapter<ProjectsListAdapter.MyViewHolder> {
    protected Activity context;
    private ArrayList<ProjectData> lstData;

    public abstract void onItemSelected(int position, ProjectData projectData);

    public abstract void onLongItemSelected(int position, ProjectData projectData);

    protected ProjectsListAdapter(Activity context, ArrayList<ProjectData> lstData) {
        this.context = context;
        this.lstData = lstData;
    }

    @NonNull
    @Override
    public ProjectsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProjectBinding binding = ItemProjectBinding.inflate(android.view.LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsListAdapter.MyViewHolder holder, int position) {
        holder.binding.tvTitle.setText(lstData.get(position).projectName);
        holder.itemView.setOnClickListener(v ->
                onItemSelected(position, lstData.get(position))

        );

        holder.itemView.setOnLongClickListener(v -> {
                    onLongItemSelected(position, lstData.get(position));
                    return true;
                }
        );
    }

    public void updateData(ArrayList<ProjectData> lstProjectData){
        this.lstData = lstProjectData;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemProjectBinding binding;

        MyViewHolder(ItemProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package com.deepcoder.hbalum.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepcoder.hbalum.databinding.ItemProjectItemBinding;
import com.deepcoder.hbalum.manager.ProjectItemManager;

import java.util.ArrayList;
import java.util.Locale;

public abstract class ProjectsItemListAdapter extends RecyclerView.Adapter<ProjectsItemListAdapter.MyViewHolder> {
    protected Activity context;
    private ArrayList<ProjectItemManager> lstData;

    public abstract void onItemSelected(int position, ProjectItemManager projectData);

    public abstract void onLongItemSelected(int position, ProjectItemManager projectData);

    protected ProjectsItemListAdapter(Activity context, ArrayList<ProjectItemManager> lstData) {
        this.context = context;
        this.lstData = lstData;
    }

    public void updateList(ArrayList<ProjectItemManager> mList) {
        this.lstData = mList;
    }

    public ArrayList<ProjectItemManager> getData() {
        return lstData;
    }

    @NonNull
    @Override
    public ProjectsItemListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProjectItemBinding binding = ItemProjectItemBinding.inflate(android.view.LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsItemListAdapter.MyViewHolder holder, int position) {

        ProjectItemManager designDataDetail = lstData.get(position);
        holder.binding.tvItemNumber.setText(String.format(Locale.ENGLISH, "Item  :  %d", designDataDetail.itemNumber));
        holder.binding.tvQty.setText(String.format(Locale.ENGLISH, "Qty  :  %d", designDataDetail.itemQuantity));

        holder.binding.tvImageSize.setText(String.format(Locale.ENGLISH, "%.0f", designDataDetail.m1290c())
                .concat(" * ")
                .concat(String.format(Locale.UK, "%.0f", designDataDetail.m1291d())));


        holder.binding.ivItem.setImageBitmap(designDataDetail.f776c.bitmap);
        holder.itemView.setOnClickListener(v ->
                onItemSelected(position, lstData.get(position))

        );

        holder.itemView.setOnLongClickListener(v -> {
                    onLongItemSelected(position, lstData.get(position));
                    return true;
                }
        );
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }


    public static final class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemProjectItemBinding binding;

        MyViewHolder(ItemProjectItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

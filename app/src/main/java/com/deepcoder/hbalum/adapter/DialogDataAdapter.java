package com.deepcoder.hbalum.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deepcoder.hbalum.databinding.ItemDialogDataBinding;
import com.deepcoder.hbalum.datalayers.model.BottomSheetData;

import java.util.ArrayList;

public abstract class DialogDataAdapter extends RecyclerView.Adapter<DialogDataAdapter.MyViewHolder> {
    protected Activity context;
    private final ArrayList<BottomSheetData> lstData;

    public abstract void onItemClick(int position, String item);


    protected DialogDataAdapter(Activity context, ArrayList<BottomSheetData> lstData) {
        this.context = context;
        this.lstData = lstData;
    }

    @NonNull
    @Override
    public DialogDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDialogDataBinding binding = ItemDialogDataBinding.inflate(android.view.LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogDataAdapter.MyViewHolder holder, int position) {
        holder.binding.tvItemTitle.setText(lstData.get(position).title);
        Drawable res = ResourcesCompat.getDrawable(
                holder.itemView.getResources(),
                lstData.get(position).imageId,
                holder.itemView.getContext().getTheme()
        );
        holder.binding.ivIcon.setImageDrawable(res);
        holder.itemView.setOnClickListener(v -> onItemClick(position, lstData.get(position).title));

    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }


    public static final class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemDialogDataBinding binding;

        MyViewHolder(ItemDialogDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

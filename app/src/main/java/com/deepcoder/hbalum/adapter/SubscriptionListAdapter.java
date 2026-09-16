package com.deepcoder.hbalum.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ItemSubscriptionProjectBinding;
import com.deepcoder.hbalum.datalayers.model.SubscriptionDataModel;

import java.util.ArrayList;

public abstract class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.MyViewHolder> {
    protected Activity context;
    private int selectedPosition = -1;
    private int subscriptionId = 0;
    private ArrayList<SubscriptionDataModel> lstData;


    protected SubscriptionListAdapter(Activity context, int subscriptionId, ArrayList<SubscriptionDataModel> lstData) {
        this.context = context;
        this.lstData = lstData;
        this.subscriptionId = subscriptionId;

    }

    public abstract void onItemSelected(int position, SubscriptionDataModel projectData);

    public abstract void onLongItemSelected(int position, SubscriptionDataModel projectData);

    @NonNull
    @Override
    public SubscriptionListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscriptionProjectBinding binding = ItemSubscriptionProjectBinding.inflate(android.view.LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionListAdapter.MyViewHolder holder, int position) {
        holder.binding.tvPlan.setText(lstData.get(position).getTitle());
        holder.binding.tvAmount.setText(lstData.get(position).getAmount() + " $");
        holder.binding.tvDuration.setText("For " + lstData.get(position).getDuration() + " " + lstData.get(position).getDuration_type());
        holder.itemView.setTag(position);
        //for default check in first item
        if (position == selectedPosition) {
            holder.binding.cvMain.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
            holder.binding.tvPlan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.binding.tvDuration.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            //  holder.tvDec.setTextColor(ContextCompat.getColor(  holder.itemView.getContext(),R.color.white));
            holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        } else {
            holder.binding.cvMain.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.binding.tvPlan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            holder.binding.tvDuration.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            //  holder.tvDec.setTextColor(ContextCompat.getColor(  holder.itemView.getContext(),R.color.black));
            holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
        }

        if (subscriptionId == 0) {
            if (position == selectedPosition) {
                holder.binding.cvMain.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
                holder.binding.tvPlan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                holder.binding.tvDuration.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                //  holder.tvDec.setTextColor(ContextCompat.getColor(  holder.itemView.getContext(),R.color.white));
                holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            } else {
                holder.binding.cvMain.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                holder.binding.tvPlan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                holder.binding.tvDuration.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                //  holder.tvDec.setTextColor(ContextCompat.getColor(  holder.itemView.getContext(),R.color.black));
                holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
            }
            holder.itemView.setOnClickListener(view -> {
                // int clickedPos = (int) holder.itemView.getTag();
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAbsoluteAdapterPosition();
                notifyItemChanged(selectedPosition);
                onItemSelected(selectedPosition, lstData.get(selectedPosition));
                // lstData.get(clickedPos).setSelected(true);
            });
        } else {
            if (lstData.get(position).getId() == subscriptionId) {
                holder.binding.cvMain.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
                holder.binding.tvPlan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                holder.binding.tvDuration.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                //  holder.tvDec.setTextColor(ContextCompat.getColor(  holder.itemView.getContext(),R.color.white));
                holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            } else {
                holder.binding.cvMain.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                holder.binding.tvPlan.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                holder.binding.tvDuration.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                //  holder.tvDec.setTextColor(ContextCompat.getColor(  holder.itemView.getContext(),R.color.black));
                holder.binding.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
            }

        }
    }

    public void updateData(ArrayList<SubscriptionDataModel> lstProjectData) {
        this.lstData = lstProjectData;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemSubscriptionProjectBinding binding;

        MyViewHolder(ItemSubscriptionProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

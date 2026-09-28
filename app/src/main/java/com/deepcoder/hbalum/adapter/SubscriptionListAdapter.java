package com.deepcoder.hbalum.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.databinding.ItemSubscriptionProjectBinding;
import com.deepcoder.hbalum.datalayers.model.SubscriptionDataModel;

import java.util.ArrayList;
import java.util.Objects;

public abstract class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.MyViewHolder> {
    protected Activity context;
    private int selectedPosition = -1;
    private final int subscriptionId;
    private ArrayList<SubscriptionDataModel> lstData;


    protected SubscriptionListAdapter(Activity context, int subscriptionId, ArrayList<SubscriptionDataModel> lstData) {
        this.context = context;
        this.lstData = new ArrayList<>(lstData);
        this.subscriptionId = subscriptionId;

    }

    public abstract void onItemSelected(int position, SubscriptionDataModel projectData);

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscriptionProjectBinding binding = ItemSubscriptionProjectBinding.inflate(android.view.LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubscriptionDataModel subscription = lstData.get(position);
        holder.binding.tvPlan.setText(subscription.getTitle());
        holder.binding.tvAmount.setText(holder.itemView.getContext().getString(
                R.string.subscription_amount,
                String.valueOf(subscription.getAmount())
        ));
        holder.binding.tvDuration.setText(holder.itemView.getContext().getString(
                R.string.subscription_duration,
                subscription.getDuration(),
                subscription.getDuration_type()
        ));
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
                int clickedPosition = holder.getAbsoluteAdapterPosition();
                if (clickedPosition == RecyclerView.NO_POSITION) {
                    return;
                }
                if (selectedPosition >= 0) {
                    notifyItemChanged(selectedPosition);
                }
                selectedPosition = clickedPosition;
                notifyItemChanged(selectedPosition);
                onItemSelected(selectedPosition, lstData.get(selectedPosition));
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
        ArrayList<SubscriptionDataModel> updatedData = new ArrayList<>(lstProjectData);
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
                return lstData.get(oldItemPosition).getId()
                        == updatedData.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                SubscriptionDataModel oldItem = lstData.get(oldItemPosition);
                SubscriptionDataModel newItem = updatedData.get(newItemPosition);
                return Objects.equals(oldItem.getTitle(), newItem.getTitle())
                        && Double.compare(oldItem.getAmount(), newItem.getAmount()) == 0
                        && oldItem.getDuration() == newItem.getDuration()
                        && Objects.equals(oldItem.getDuration_type(), newItem.getDuration_type());
            }
        });

        lstData = updatedData;
        if (selectedPosition >= lstData.size()) {
            selectedPosition = RecyclerView.NO_POSITION;
        }
        result.dispatchUpdatesTo(this);

    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }


    public static final class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemSubscriptionProjectBinding binding;

        public MyViewHolder(ItemSubscriptionProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

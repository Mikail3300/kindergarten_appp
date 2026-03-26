package com.example.kindergarten.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kindergarten.R;
import com.example.kindergarten.databinding.ItemChildBinding;
import com.example.kindergarten.model.Child;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    public interface OnChildActionListener {
        void onChildClick(Child child);
        void onEditClick(Child child);
        void onDeleteClick(Child child);
    }

    private final List<Child> children = new ArrayList<>();
    private final OnChildActionListener listener;

    public ChildAdapter(OnChildActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Child> updatedChildren) {
        children.clear();
        children.addAll(updatedChildren);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChildBinding binding = ItemChildBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChildViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        holder.bind(children.get(position));
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_scale_in));
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {
        private final ItemChildBinding binding;

        ChildViewHolder(ItemChildBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Child child) {
            binding.tvChildName.setText(child.getChildName());
            binding.tvGroup.setText(binding.getRoot().getContext().getString(R.string.group_template, child.getGroupName()));
            binding.tvParent.setText(binding.getRoot().getContext().getString(R.string.parent_template, child.getParentName()));
            binding.tvTeacher.setText(binding.getRoot().getContext().getString(R.string.teacher_template, child.getTeacherName()));

            int imageRes = "Девочка".equalsIgnoreCase(child.getGender()) ? R.drawable.ic_child_female : R.drawable.ic_child_male;
            binding.ivAvatar.setImageResource(imageRes);

            binding.getRoot().setOnClickListener(v -> listener.onChildClick(child));
            binding.btnEdit.setOnClickListener(v -> listener.onEditClick(child));
            binding.btnDelete.setOnClickListener(v -> listener.onDeleteClick(child));
        }
    }
}

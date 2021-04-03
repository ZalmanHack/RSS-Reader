package com.zalman_hack.mvvmrss.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.zalman_hack.mvvmrss.BR;
import com.zalman_hack.mvvmrss.R;
import com.zalman_hack.mvvmrss.databases.entities.Category;

import java.util.List;
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> categories;
    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CategoryViewHolder.create(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bindTo(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_category;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        static CategoryViewHolder create(LayoutInflater inflater, ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
            return new CategoryViewHolder(binding);
        }

        public CategoryViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Category categoryModel) {
            binding.setVariable(BR.categoryModel, categoryModel);
            binding.executePendingBindings();
        }
    }
}

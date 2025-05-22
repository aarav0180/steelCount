package com.example.practise.ui.form;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practise.R;

import java.util.List;

public class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselAdapter.ImageViewHolder> {

    private List<Uri> imageUris;
    private final OnRemoveImageListener removeListener;

    public interface OnRemoveImageListener {
        void onRemove(Uri uri);
    }

    public ImageCarouselAdapter(List<Uri> imageUris, OnRemoveImageListener listener) {
        this.imageUris = imageUris;
        this.removeListener = listener;
    }

    public void updateImages(List<Uri> newUris) {
        this.imageUris = newUris;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_carousel, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = imageUris.get(position);
        holder.imageView.setImageURI(uri);

        holder.removeBtn.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemove(uri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris != null ? imageUris.size() : 0;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView removeBtn;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_carousel_image);
            removeBtn = itemView.findViewById(R.id.image_remove_btn);
        }
    }
}


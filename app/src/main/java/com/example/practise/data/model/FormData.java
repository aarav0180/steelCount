package com.example.practise.data.model;

import android.net.Uri;
import java.util.List;

public class FormData {
    private String itemName;
    private String itemCategory;
    private String title;
    private String description;
    private int itemCount;
    private List<Uri> imageUris;

    public FormData(String itemName, String itemCategory, String title, String description, int itemCount, List<Uri> imageUris) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.title = title;
        this.description = description;
        this.itemCount = itemCount;
        this.imageUris = imageUris;
    }

    // Getters and Setters
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemCategory() { return itemCategory; }
    public void setItemCategory(String itemCategory) { this.itemCategory = itemCategory; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }

    public List<Uri> getImageUris() { return imageUris; }
    public void setImageUris(List<Uri> imageUris) { this.imageUris = imageUris; }
}

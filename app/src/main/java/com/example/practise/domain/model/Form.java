package com.example.practise.domain.model;

import java.util.List;

public class Form {
    private final String itemName;
    private final String itemCategory;
    private final String title;
    private final String description;
    private final List<String> imagePaths; // Simple file paths
    private final int itemCount;

    public Form(String itemName, String itemCategory, String title, String description, List<String> imagePaths, int itemCount) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.title = title;
        this.description = description;
        this.imagePaths = imagePaths;
        this.itemCount = itemCount;
    }

    public String getItemName() { return itemName; }
    public String getItemCategory() { return itemCategory; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getImagePaths() { return imagePaths; }
    public int getItemCount() { return itemCount; }

    public Form withItemCount(int newCount) {
        return new Form(itemName, itemCategory, title, description, imagePaths, newCount);
    }
}


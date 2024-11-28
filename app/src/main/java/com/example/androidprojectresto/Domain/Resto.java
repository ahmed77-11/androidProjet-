package com.example.androidprojectresto.Domain;

import java.io.Serializable;

public class Resto implements Serializable {
    private int CategoryId;
    private String Description;
    private boolean Recomanded;
    private int id;
    private int LocationId;

    private String Budget;
    private String ImagePath;
    private double Star;
    private int TimeId;
    private String Title;

    public Resto() {
    }

    @Override
    public String toString() {
        return  Title;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.CategoryId = categoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isRecomanded() {
        return Recomanded;
    }

    public void setRecomanded(boolean recomanded) {
        Recomanded = recomanded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return LocationId;
    }

    public void setLocationId(int locationId) {
        LocationId = locationId;
    }

    public String getBudget() {
        return Budget;
    }

    public void setBudget(String budget) {
        Budget = budget;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }

    public int getTimeId() {
        return TimeId;
    }

    public void setTimeId(int timeId) {
        TimeId = timeId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}

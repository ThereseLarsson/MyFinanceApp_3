package com.example.thereselarsson.da401a_assignment_1;

/**
 * Class for creating an object from the item stored in the SQLite database
 * Provides easier processing of the data
 */

public class Item {
    private int icon; //the icon for the data (based on category)
    private String title;
    private String date;
    private int amount; //in the currency kr
    private String category;

    public Item(int icon, String title, String date, int amount, String category) {
        this.icon = icon;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.category = category;
    }

    /*public Item(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }*/

    /**
     * set-methods
     * ------------------------------------------------------------------------
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setTitle(String title)  {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * get-methods
     * ------------------------------------------------------------------------
     */
    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }
}

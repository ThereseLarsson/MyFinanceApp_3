package com.example.thereselarsson.MyFinanceApp_3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for creating an object from the item stored in the SQLite database
 * Provides easier processing of the data
 */

public class Item implements Parcelable {
    private int icon; //icon based on category
    private String title;
    private String date;
    private double amount; //currency kr
    private String category;

    public Item(int icon, String title, String date, double amount, String category) {
        this.icon = icon;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.category = category;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setTitle(String title)  {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    /**
     * methods for making the Item-object parcelable (used in ViewTransactionFragment)
     */
    public Item(Parcel in) {
        this.icon = in.readInt();
        this.title = in.readString();
        this.date = in.readString();
        this.amount = in.readDouble();
        this.category = in.readString();
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeDouble(amount);
        dest.writeString(category);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}

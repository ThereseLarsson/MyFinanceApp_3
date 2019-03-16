package com.example.thereselarsson.da401a_assignment_1;

import android.widget.ImageView;

public class Item {
    private int icon;
    private String title;

    public Item(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}

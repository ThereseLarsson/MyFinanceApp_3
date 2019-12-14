package com.example.thereselarsson.da401a_assignment_1_v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * custom adapter for the listview used in the ViewTransactionFragment
 */
public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Item> items;

    public CustomListAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    /**
     * returns total of items in the list
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * returns list item at the specified position
     */
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        }

        // get current item to be displayed
        Item currentItem = (Item) getItem(position);

        // get the TextView for item name and item description
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.item_icon);
        TextView itemTitle = (TextView) convertView.findViewById(R.id.item_title);

        //sets the text for item name and item description from the current item object
        itemIcon.setImageResource(currentItem.getIcon());
        itemTitle.setText(currentItem.getTitle() + "\n" + currentItem.getDate()); //displays both the title and the date of the current item

        // returns the view for the current row
        return convertView;
    }
}

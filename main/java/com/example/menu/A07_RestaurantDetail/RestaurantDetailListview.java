package com.example.menu.A07_RestaurantDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.menu.R;

public class RestaurantDetailListview extends BaseAdapter {

    // Declare Variables
    Context context;
    String[] name,message;
    int[] image;
    LayoutInflater inflater;

    public RestaurantDetailListview(Context context, String[] name,String[] message,int[] image) {
        this.context = context;
        this.name = name;
        this.message = message;
        this.image = image;
    }

    @Override
    public int getCount() {
        return name.length;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtname,txtmessage;
        ImageView imagename;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.a07_restaurantdetail_listitem1, parent, false);

        // Locate the TextViews in listview_item.xml
        txtname = (TextView) itemView.findViewById(R.id.title_content);

        txtmessage = (TextView) itemView.findViewById(R.id.subtitle_content);

        // Locate the ImageView in listview_item.xml
        imagename = (ImageView) itemView.findViewById(R.id.image_content);

        // Capture position and set to the TextViews
        txtname.setText(name[position]);

        txtmessage.setText(message[position]);


        // Capture position and set to the ImageView
        imagename.setImageResource(image[position]);

        return itemView;
    }
}

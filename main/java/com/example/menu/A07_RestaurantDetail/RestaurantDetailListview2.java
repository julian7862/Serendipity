package com.example.menu.A07_RestaurantDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.menu.R;
import com.mikhaellopez.circularimageview.CircularImageView;

public class RestaurantDetailListview2 extends BaseAdapter {

    // Declare Variables
    Context context;
    String[] title,time,content;
    int[] images;
    LayoutInflater inflater;

    public RestaurantDetailListview2(Context context, String[] title,String[] time,String[] content,int[] images) {
        this.context = context;
        this.title = title;
        this.time = time;
        this.content = content;
        this.images = images;
    }

    @Override
    public int getCount() {
        return title.length;
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
        TextView txtname,txttime,textcontent;
        CircularImageView imagename;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.a07_restaurantdetail_listitem2, parent, false);

        // Locate the TextViews in listview_item.xml
        txtname = (TextView) itemView.findViewById(R.id.title_content);

        txttime = (TextView) itemView.findViewById(R.id.subtitle_content);

        textcontent = (TextView) itemView.findViewById(R.id.content);

        // Locate the ImageView in listview_item.xml
        imagename = (CircularImageView) itemView.findViewById(R.id.image_data);

        // Capture position and set to the TextViews
        txtname.setText(title[position]);

        txttime.setText(time[position]);

        textcontent.setText(content[position]);


        // Capture position and set to the ImageView
        imagename.setImageResource(images[position]);

        return itemView;
    }
}

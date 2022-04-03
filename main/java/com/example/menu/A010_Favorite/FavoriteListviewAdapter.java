package com.example.menu.A010_Favorite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.menu.A03_TabMenu.A04_FavoriteTab;
import com.example.menu.R;

import java.util.ArrayList;

import xyz.hanks.library.bang.SmallBangView;

//

public class FavoriteListviewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    //String[] name,message;
    ArrayList<String>name,message;
    ArrayList<Integer> image;
    LayoutInflater inflater;

    public FavoriteListviewAdapter() {
        // Required empty public constructor
    }

    public FavoriteListviewAdapter(Context context, ArrayList<String>name,ArrayList<String>message,ArrayList<Integer>image) {
        this.context = context;
        this.name = name;
        this.message = message;
        this.image = image;
    }

    @Override
    public int getCount() {
        return name.size();
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

        View itemView = inflater.inflate(R.layout.a10_fav_listitem, parent, false);

        // Locate the TextViews in listview_item.xml
        txtname = (TextView) itemView.findViewById(R.id.title_content);

        txtmessage = (TextView) itemView.findViewById(R.id.subtitle_content);

        // Locate the ImageView in listview_item.xml
        imagename = (ImageView) itemView.findViewById(R.id.image_content);

        // Capture position and set to the TextViews
        txtname.setText(A04_FavoriteTab.name.get(position));

        txtmessage.setText(A04_FavoriteTab.message.get(position));

        final SmallBangView like_heart = itemView.findViewById(R.id.like_heart);
        like_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like_heart.isSelected()) {
                    like_heart.setSelected(false);
                    FavoriteListviewAdapter.this.notifyDataSetChanged();
                } else {
                    like_heart.setSelected(true);
                    like_heart.likeAnimation(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //toast("heart+1");
                        }
                    });
                }
            }
        });


        // Capture position and set to the ImageView
        imagename.setImageResource(A04_FavoriteTab.image.get(position));
        
        return itemView;
    }
}

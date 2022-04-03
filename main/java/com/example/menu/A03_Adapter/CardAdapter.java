package com.example.menu.A03_Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.menu.A03_Model.Model;
import com.example.menu.R;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

//import com.example.menu.A03_TabMenu.A01_HomeTab;

public class CardAdapter extends BaseCardAdapter {

    public  List<Model> modelList;
    private Context context;

    public CardAdapter(List<Model> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.a03_card_item;
    }

    @Override
    public void onBindData(int position, View cardview) {

        if (modelList == null || modelList.size() == 0) {
            return;
        }
        ImageView imageView = (ImageView) cardview.findViewById(R.id.imageView);
        TextView textView1 = (TextView) cardview.findViewById(R.id.name);
        //TextView textView2 = (TextView) cardview.findViewById(R.id.distance);
        Model model = modelList.get(position);
        textView1.setText(model.getTitle());
        //textView2.setText(model.getDistance());
        if(model.getImage() != ""){
            Picasso.get().load(model.getImage()).into(imageView);
        }else{
            imageView.setImageResource(R.drawable.ic_home_nocolor);
        }

    }


}

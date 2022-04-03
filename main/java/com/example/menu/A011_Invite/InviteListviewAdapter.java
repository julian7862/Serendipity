package com.example.menu.A011_Invite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.menu.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import xyz.hanks.library.bang.SmallBangView;

//
//import static android.app.PendingIntent.getActivity;
//import static android.support.v4.content.ContextCompat.startActivity;

public class InviteListviewAdapter extends BaseAdapter {

    // Declare Variables
    Invite context;
    ArrayList<String> name;
    //int[] image;
    LayoutInflater inflater;
    ArrayList <String> UID,img;

    public InviteListviewAdapter() {
        // Required empty public constructor
    }

    public InviteListviewAdapter(Invite context, ArrayList name,ArrayList uid ,ArrayList img) {
        this.context = context;

        this.name = name;
        this.UID = uid;
        this.img = img;
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
        TextView txtname,uidview;
        ImageView imagename;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View itemView = inflater.inflate(R.layout.a11_invite_listitem, parent, false);

        // Locate the TextViews in listview_item.xml
        txtname = (TextView) itemView.findViewById(R.id.title_content);
        uidview = itemView.findViewById(R.id.inviteeUID);

        // Locate the ImageView in listview_item.xml
        imagename = (ImageView) itemView.findViewById(R.id.image_data);

        // Capture position and set to the TextViews
        txtname.setText(name.get(position));
        uidview.setText(UID.get(position));

        final SmallBangView check = itemView.findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.isSelected()) {
                    check.setSelected(false);
                    context.InviteeUID.remove(UID.get(position));
                    Log.d("remove","remove"+UID.get(position)+"total:"+context.InviteeUID.size());
                } else {
                    check.setSelected(true);
                    context.InviteeUID.add(UID.get(position));
                    Log.d("remove","add"+UID.get(position)+"total:"+context.InviteeUID.size());
                    check.likeAnimation(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //toast("member+1");
                        }
                    });
                }
            }
        });



        // Capture position and set to the ImageView
        //imagename.setImageResource(image[position]);
        StorageReference r= FirebaseStorage.getInstance().getReference().child("images");
        if (img.get(position)!=null){
            r.child(img.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(imagename);
                }
            });
        }




        return itemView;
    }
}

package com.example.menu.A011_Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.menu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Notification_Listview_Adapter extends BaseAdapter {

    // Declare Variables
    Context context;
    ArrayList<String> title;
    //int[] images;
    LayoutInflater inflater;

    public Notification_Listview_Adapter(Context context, ArrayList title) {
        this.context = context;
        this.title = title;
    }

    @Override
    public int getCount() {
        return title.size();
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
        ImageView imagename;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.a11_notification_litsitem, parent, false);

        // Locate the TextViews in listview_item.xml
        txtname = (TextView) itemView.findViewById(R.id.title_content);

        txttime = (TextView) itemView.findViewById(R.id.subtitle_content);


        // Locate the ImageView in listview_item.xml
        imagename = (ImageView) itemView.findViewById(R.id.image_content);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invitations").child(title.get(position));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("PlaceImg")){
                    Picasso.get().load(snapshot.child("PlaceImg").getValue().toString()).into(imagename);
                }
                txttime.setText(snapshot.child("Time").getValue().toString());
                String uidd = snapshot.child("Inviter").getValue().toString();
                if (uidd.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    txtname.setText("您建立了一個飯局");
                }else{
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Locations").child(uidd);
                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild("name")){
                                String name = snapshot.child("name").getValue().toString();
                                txtname.setText(name+"邀請您");
                            }else{
                                txtname.setText(snapshot.child("email").getValue().toString()+"邀請您");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return itemView;
    }
}

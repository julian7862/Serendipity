package com.example.menu.A011_Event;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.menu.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class EventList extends BaseAdapter{
    // Declare Variables
    Context context;
    ArrayList<String> name,reply;
    //ArrayList<String>images;
    LayoutInflater inflater;
    DatabaseReference location;

    public EventList(Context context, ArrayList name,ArrayList reply) {
        this.context = context;
        this.name = name;
        this.reply = reply;
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
        TextView txtname,textreply;
        CircularImageView imagename;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.a11_event_list, parent, false);

        // Locate the TextViews in listview_item.xml
        txtname = (TextView) itemView.findViewById(R.id.title_content);

        textreply = (TextView) itemView.findViewById(R.id.reply_content);

        // Locate the ImageView in listview_item.xml
        imagename = (CircularImageView) itemView.findViewById(R.id.image_data);

        location = FirebaseDatabase.getInstance().getReference().child("Locations").child(name.get(position));

        location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtname.setText(snapshot.child("email").getValue().toString());
                if (snapshot.hasChild("name")){
                    txtname.setText(snapshot.child("name").getValue().toString());
                }
                if (snapshot.hasChild("img")){
                    //image.add(String.valueOf(d.child("img").getValue()));
                    StorageReference r= FirebaseStorage.getInstance().getReference().child("images");
                    r.child(snapshot.child("img").getValue().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(imagename);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Capture position and set to the TextViews


        textreply.setText(reply.get(position));


        // Capture position and set to the ImageView
        //imagename.setImageResource(images.get(position));

        return itemView;
    }
}

//public class RestaurantDetailListview2 extends BaseAdapter {
//

//}
//

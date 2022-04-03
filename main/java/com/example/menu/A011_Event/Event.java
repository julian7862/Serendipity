package com.example.menu.A011_Event;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.menu.A07_RestaurantDetail.RestaurantDetailView;
import com.example.menu.A11_Finish.Finish;
import com.example.menu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Event extends FragmentActivity {

    ListView list;
    EventList adapter;
    ArrayList<String> name= new ArrayList<>();
    ArrayList<String> reply = new ArrayList<>();

    ArrayList<String> image,images;

    private ImageButton back;
    String IID,imgLink=null;
    DatabaseReference InviteRef,locations;

    ImageView i;
    TextView Title,subTitle,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a11_event);

        i = findViewById(R.id.image_content);
        Title = findViewById(R.id.title_content);
        subTitle = findViewById(R.id.subtitle_content);
        username = findViewById(R.id.user_name);

        username.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        IID = getIntent().getStringExtra("IID");
        Log.d("IID",IID);
        InviteRef = FirebaseDatabase.getInstance().getReference("Invitations");
        locations = FirebaseDatabase.getInstance().getReference().child("Locations");
        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.spinner_user);
        List<String> dataset = new LinkedList<>(Arrays.asList("可參加", "未回覆", "不克出席"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setSelectedIndex(1);
        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String s = "";
                switch (position){
                    case 0:
                        s= "true";
                        break;
                    case 2:
                        s="false";
                        break;
                    default:
                        s = "";
                        //reply.add("尚未回覆");
                }
                FirebaseDatabase.getInstance().getReference("Invitations").child(IID).child("invitee")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(s);
                adapter.notifyDataSetChanged();
            }
        });

        InviteRef.child(IID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    final AlertDialog alertDialog = new AlertDialog.Builder(Event.this).create();
                    alertDialog.setTitle("警告:");
                    alertDialog.setMessage("這項活動已經取消或是結束了...");

                    alertDialog.setButton(Dialog.BUTTON_POSITIVE,"確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            alertDialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                    });

                    alertDialog.show();
                    return;
                }else{
                    String t = dataSnapshot.child("PlaceName").getValue().toString();
                    String St = dataSnapshot.child("PlaceSubT").getValue().toString();
                    String p = dataSnapshot.child("PlaceImg").getValue().toString();
                    Title.setText(t);
                    subTitle.setText(St);
                    imgLink = p;
                    Picasso.get().load(p).into(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference iid = InviteRef.child(IID).child("invitee");
        iid.keepSynced(true);
        iid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.clear();
                reply.clear();
                for (DataSnapshot d: snapshot.getChildren()){
                    name.add(d.getKey());
                    Log.d("AAAA",""+name.size());
                    switch (d.getValue().toString()){
                        case "true":
                            reply.add("參加");
                            break;
                        case "false":
                            reply.add("不克出席");
                            break;
                            default:
                                reply.add("尚未回覆");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        name = new String[]{ "Spicy Fried Rice &amp; Bacon Amet", "Shrimp Curry Burger Ipsam dolor Sit", "Masala Spiced Chickpeas Dolor Amet", "Kung Pao Pastrami ad Minima Sit", "Chicken Doro Wat Nisi Commodo Amet","Mango Cile Chutney Et Dolore Sit"};
//
//        image = new int[] { R.drawable.listimg1,R.drawable.listimg2,
//                R.drawable.listimg3,R.drawable.listimg4,
//                R.drawable.listimg5,R.drawable.listimg6};
//
//        reply = new String[]{"不克參加", "暫不回覆", "可以參加", "不克參加", "可以參加", "不克參加"};
//
//        images = new int[] { R.drawable.pic2,R.drawable.pic3,
//                R.drawable.pic4};

//      回去Notifiction
        back = (ImageButton) findViewById(R.id.back_arrow);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Event.this.finish();
            }
        });


//      呼叫list
        list = (ListView) findViewById(R.id.mylist);

        // Pass results to ListViewAdapter Class
        Log.d("AAAA","ST"+name.size());
        adapter = new EventList(this, name,reply);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);



        // Capture ListView item click
////        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view,
////                                    int position, long id) {
////
////                Toast.makeText(Event.this,"You have selected :"+name[position], Toast.LENGTH_SHORT).show();
////
////
//            }
//
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void DetailLink(View v) {
       Intent i= new Intent(getApplicationContext(), RestaurantDetailView.class);
        i.putExtra("name",Title.getText());
        i.putExtra("note",subTitle.getText());
        i.putExtra("imgname",imgLink);
       startActivity(i);
    }

    public void GoFinish(View view) {
        Intent browserIntent = new Intent(Event.this, Finish.class);
        startActivity(browserIntent);
    }

}
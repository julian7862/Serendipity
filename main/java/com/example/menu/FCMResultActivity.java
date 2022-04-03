package com.example.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.classes.Invitee;
import com.example.menu.classes.inviter;
import com.example.menu.recycler.inviteelistAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FCMResultActivity extends AppCompatActivity {
    TextView inviter_view;
    RecyclerView recyclerView;
    Button button,button2;
    DatabaseReference InviteRef,TokenRef;
    String Invitation_id;
    inviteelistAdapter inviteelistAdapter;
    ArrayList<Invitee> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmresult);
        inviter_view = findViewById(R.id.inviter_text);
        recyclerView = findViewById(R.id.InviteeRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        button = findViewById(R.id.invitedbutton);
        button2 = findViewById(R.id.invitedbutton2);
        InviteRef = FirebaseDatabase.getInstance().getReference("Invitations");
        TokenRef = FirebaseDatabase.getInstance().getReference("Tokens");
        Invitation_id = getIntent().getStringExtra("IID");
        InviteRef.child(Invitation_id).child("inviter_uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inviter inviter = new inviter();
                if(dataSnapshot.getValue() == null){
                    final AlertDialog alertDialog = new AlertDialog.Builder(FCMResultActivity.this).create();
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
                }
                inviter.setInviter_uid(dataSnapshot.getValue().toString());
                TokenRef.child(inviter.getInviter_uid()).child("email").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        inviter_view.setText("Inviter:"+dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        arrayList = new ArrayList<>();
        inviteelistAdapter = new inviteelistAdapter(this,arrayList);
        recyclerView.setAdapter(inviteelistAdapter);
        InviteRef.child(Invitation_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    if (d.getKey().equals("inviter_uid")){
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null && d.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            button.setText("取消活動");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InviteRef.child(Invitation_id).removeValue();
                                }
                            });
                            button2.setText("");
                            button2.setEnabled(false);
                        }else{
                            button.setText("拒絕");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                                        InviteRef.child(Invitation_id).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(false);
                                    }
                                }
                            });
                            button2.setText("參加");
                            button2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                                        InviteRef.child(Invitation_id).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                    }
                                }
                            });
                        }
                        continue;
                    }
                    Invitee invitee = new Invitee();
                    invitee.setStatus(d.getValue().toString());
                    invitee.setUID(d.getKey());
                    arrayList.add(invitee);
                }
                inviteelistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

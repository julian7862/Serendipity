package com.example.menu.A011_Invite;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.menu.A05_Time.Time;
import com.example.menu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//import android.support.v4.view.MenuItemCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;

public class Invite extends AppCompatActivity {

    DatabaseReference locations;

    ListView list;
    String T=null,subT=null,img=null;
    //FirebaseRecyclerOptions<User> options;
    InviteListviewAdapter adapter;
    public static ArrayList<String> InviteeUID = new ArrayList<>();;
    public  static ArrayList<String> name=new ArrayList<String>();
    public static ArrayList <String> image = new ArrayList<String>();
    public  static ArrayList<String> UID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a11_invite);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        T = getIntent().getStringExtra("Title");
        subT = getIntent().getStringExtra("Subtitle");
        img = getIntent().getStringExtra("imgname");

        //profileRef = FirebaseDatabase.getInstance().getReference().child("Profile");
        locations = FirebaseDatabase.getInstance().getReference().child("Locations");
        locations.keepSynced(true);
        locations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UID.clear();
                image.clear();
                name.clear();
                for (DataSnapshot d:snapshot.getChildren()){
                    if (d.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        continue;
                    }
                    UID.add(d.getKey());
                    if (d.hasChild("img")){
                        image.add(String.valueOf(d.child("img").getValue()));
                    }else{
                        image.add(null);

                    }
                    if (d.hasChild("name")){
                        name.add(String.valueOf(d.child("name").getValue()));
                    }else{
                        name.add(String.valueOf(d.child("email").getValue()));
                    }
                }
               // Log.d("AAAA","IT"+name.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // Locate the ListView in listview_main.xml
        list =  findViewById(R.id.mylist);
//        options =
//                new FirebaseRecyclerOptions.Builder<User>()
//                        .setQuery(locations, User.class)
//                        .build();
        //Log.d("AAAA","ITS"+name.size());
        adapter = new InviteListviewAdapter(this,name,UID,image);
        list.setAdapter(adapter);

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
//                // 利用索引值取得點擊的項目內容。
//                // 整理要顯示的文字。
//                // 顯示。
//            }
//        });

        //userListAdapter = new UserListAdapter(this,options);
        //list.setAdapter(userListAdapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fav, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {

                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id){
            case R.id.action_search:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Back to Detail
    public void BackToDetail(View view) {
        Invite.this.finish();
    }

    // Go to create an activity
    public void GoTime(View view) {
        Intent intent = new Intent(Invite.this, Time.class);
        intent.putExtra("Invitee",InviteeUID);
        intent.putExtra("T",T);
        intent.putExtra("subT",subT);
        intent.putExtra("img",img);
        startActivity(intent);
        //finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InviteeUID.clear();
    }
}

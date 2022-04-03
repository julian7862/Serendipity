package com.example.menu.A011_Notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.menu.A011_Event.Event;
import com.example.menu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    ListView list;
    Notification_Listview_Adapter adapter;
    ArrayList<String> title= new ArrayList<>();
//    private ImageButton back;

    int[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a11_notification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseReference d = FirebaseDatabase.getInstance().getReference("Invitations");
        d.keepSynced(true);
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title.clear();
                for (DataSnapshot s: snapshot.getChildren()){
                    if (s.child("invitee").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        title.add(s.getKey());
                    }
                }
                //Log.d("BBBB",""+title.size());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
//        title = new ArrayList{ "AAA 邀您共餐", "您建立了一個飯局", "AAA 邀您共餐", };
//
//        content = new ArrayList{ "Amorim ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae nibh nisl. Cras etitikis mauris eget lorem ultricies ferme ntum a inti diam. ","Amorim ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae nibh nisl. Cras etitikis mauris eget lorem ultricies ferme ntum a inti diam. ","Amorim ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae nibh nisl. Cras etitikis mauris eget lorem ultricies ferme ntum a inti diam. "};
//
//        time = new ArrayList{ "7/16(四) 18:00","7/23(四) 18:00","7/30(四) 18:00"};

        images = new int[] { R.drawable.listimg1,R.drawable.listimg2,
                R.drawable.listimg3};


        list = (ListView) findViewById(R.id.mylist);

        // Pass results to ListViewAdapter Class
        //Log.d("BBBB","M:"+title.size());

        adapter = new Notification_Listview_Adapter(this,title);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        // Capture ListView item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


//                Toast.makeText(Notification.this,"You have selected :"+title[position], Toast.LENGTH_SHORT).show();
                Intent i= new Intent(getApplicationContext(), Event.class);
                i.putExtra("IID",title.get(position));
                Log.d("IID",title.get(position));
                startActivity(i);

            }

        });
    }

    public void BackToMap(View v) {
//        Intent browserIntent = new Intent(getApplicationContext(), TabMenu.class);
//        startActivity(browserIntent);
        Notification.this.finish();
    }

//    public void GoEvent(View v) {
//        Intent browserIntent = new Intent(getApplicationContext(), Event.class);
//        startActivity(browserIntent);
//    }




//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menus, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        //*** setOnQueryTextFocusChangeListener ***
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String searchQuery) {
//
//                return true;
//            }
//        });
//
//        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                // Do something when collapsed
//                return true;  // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                // Do something when expanded
//                return true;  // Return true to expand action view
//            }
//        });
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id){
            case R.id.action_search:

                return true;
            case R.id.action_setting:
                Toast.makeText(getApplicationContext(),"Setting Pressed",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

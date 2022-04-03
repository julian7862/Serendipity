package com.example.menu.A010_Favorite;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.menu.R;

//import android.support.v4.view.MenuItemCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;

public class Favorite extends AppCompatActivity {

    ListView list;
    FavoriteListviewAdapter adapter;
    public static String[] name,message;

    public static int[] image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a10_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = new String[]{ "Spicy Fried Rice &amp; Bacon Amet", "Shrimp Curry Burger Ipsam dolor Sit", "Masala Spiced Chickpeas Dolor Amet", "Kung Pao Pastrami ad Minima Sit", "Chicken Doro Wat Nisi Commodo Amet","Mango Cile Chutney Et Dolore Sit"};

        message = new String[]{ "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet","Lorem ipsum dolor sit amet","Lorem ipsum dolor sit amet"};


        image = new int[] { R.drawable.listimg1,R.drawable.listimg2,
                R.drawable.listimg3,R.drawable.listimg4,
                R.drawable.listimg5,R.drawable.listimg6};

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.mylist);

        // Pass results to ListViewAdapter Class
        //adapter = new FavoriteListviewAdapter(this, name,message,image);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        // Capture ListView item click
     /*   list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(Favorite.this,"You have selected :"+name[position], Toast.LENGTH_SHORT).show();


            }

        });*/



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


}

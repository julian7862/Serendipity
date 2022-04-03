package com.example.menu.A03_TabMenu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.menu.R;
import com.example.menu.SQLite.ItemAdapter;
import com.example.menu.SQLite.MyDB;

import java.util.ArrayList;


public class A04_FavoriteTab extends Fragment {

    public static MyDB myDB;

    RecyclerView list;
    public static ItemAdapter adapter;
    public static ArrayList<String> name =new ArrayList<String>();
    public static ArrayList<String> message =new ArrayList<String>();
    public static ArrayList<Integer> image = new ArrayList<Integer>();

    public A04_FavoriteTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new MyDB(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.a10_favorite, container, false);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipefresh);


        //message = new String[]{ , "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet","Lorem ipsum dolor sit amet","Lorem ipsum dolor sit amet"};

        // Locate the ListView in listview_main.xml
        list = (RecyclerView) view.findViewById(R.id.mylist);



        // Pass results to ListViewAdapter Class
        adapter = new ItemAdapter(getContext(), myDB.getData());
        // Binds the Adapter to the ListView
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter = new ItemAdapter(getContext(), myDB.getData());
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });





        //adapter.notifyDataSetChanged();
        // Capture ListView item click
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
////                Toast.makeText(getActivity(),"You have selected :"+name[position], Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(getActivity(), RestaurantDetailView.class);
//                startActivity(i);
//
//            }
//
//        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateView() {
        Log.d("AAAA","UV");
        A04_FavoriteTab.adapter.notifyDataSetChanged();
    }
}
//
//
//public class A04_FavoriteTab extends Fragment {
//
//    public A04_FavoriteTab() {
//        // Required empty public constructor
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View F1 = inflater.inflate(R.layout.a10_favorite, container, false);
//
//        return F1;
//    }
//
//
//}
//

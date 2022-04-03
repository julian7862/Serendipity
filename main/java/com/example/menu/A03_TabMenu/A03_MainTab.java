package com.example.menu.A03_TabMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.menu.A03_Adapter.CardAdapter;
import com.example.menu.A03_Model.Model;
import com.example.menu.A07_RestaurantDetail.RestaurantDetailView;
import com.example.menu.R;
import com.example.menu.SQLite.DBitem;
import com.huxq17.swipecardsview.SwipeCardsView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.example.menu.A03_TabMenu.A04_FavoriteTab.myDB;

public class A03_MainTab extends Fragment {

    public static SwipeCardsView swipeCardsView;
    List<Model> modelList = new ArrayList<>();
    private ImageButton go;

    SharedPreferences pref;

    public static CardAdapter cardAdapter;
    public static String searchResult;
//    public static boolean clearList = false;
    //ExecutorService es;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.a03_maintab, container, false);

        swipeCardsView = (SwipeCardsView) view.findViewById(R.id.SwipeCardsView);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);

        swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
            @Override
            public void onShow(int index) {

            }

            @Override
            public void onCardVanish(int index, SwipeCardsView.SlideType type) {
                switch (type){
                    case LEFT:

                        break;
                    case RIGHT:
                        // 加入
                        DBitem db = new DBitem(modelList.get(index).title, modelList.get(index).image,modelList.get(index).distance);
                        boolean insert = myDB.addItem(db);

                        break;
                }
            }

            @Override
            public void onItemClick(View cardImageView, int index) {
                Intent i = new Intent(getActivity(), RestaurantDetailView.class);
                //List<String> l = match(searchResult);
                i.putExtra("name",modelList.get(index).title);
                i.putExtra("note",modelList.get(index).distance);
                i.putExtra("imgname",modelList.get(index).image);
                startActivity(i);
            }
        });

        pref = getActivity().getSharedPreferences("test", MODE_PRIVATE);

        try {
            getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        ImageButton go = (ImageButton) view.findViewById(R.id.go_btn);
//        go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                view.findViewById(R.id.input_password);
//                Intent i = new Intent(getActivity(), RestaurantDetailView.class);
//                startActivity(i);
//            }
//        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void getData() throws InterruptedException, ExecutionException {
        List<String> l= null;
        if (searchResult != null){
            //Log.d("ResultC",searchResult);
            l = match(searchResult);
            //
            if (l != null&&l.size()>0){
                modelList.clear();
                List<Future<?>> futures = new ArrayList<Future<?>>();
                Log.d("MainTab",""+l.size());
                ExecutorService exec = Executors.newFixedThreadPool(l.size());
                for (String s:l){
                    OneShotTask worker = new OneShotTask(s);
                    Future<?> f = exec.submit(worker);
                    futures.add(f);
                }
                for(Future<?> future : futures){
                    future.get();
                    Log.d("ResultAA","future start"+futures.indexOf(future));
                }

                cardAdapter = new CardAdapter(modelList, getActivity());
                swipeCardsView.setAdapter(cardAdapter);
                swipeCardsView.notifyDatasetChanged(0);

                //System.out.println(cardAdapter.modelList.get(0));

//                es= Executors.newCachedThreadPool();
//                for (String s: l){
//                    es.execute(new OneShotTask(s));//Foo(s);
//                }
//                es.shutdown();
//                while (!es.awaitTermination(10, TimeUnit.SECONDS)){
//
//                }
            }
        }





    }

    List<String> match(String c){
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("[0-9]+")
                .matcher(c);
        while (m.find()) {
            allMatches.add(m.group());
        }

        return allMatches;
    }


    void Foo(String str )  {

        Thread t = new Thread(new OneShotTask(str));
        t.start();
    }

    class OneShotTask implements Runnable {
        String str;Integer i;
        OneShotTask(String s) { str = s; }
        public void run() {
            String data=null;
            //someFunc(str);
            try {
                URL url = new URL("http://140.117.71.141/search.php/?num="+str);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while(line!=null){
                    line = bufferedReader.readLine();
                    if (line != null){
                        data= data+line;
                    }
                }

                Log.d("ResultAA",data.replace("null",""));
                JSONObject JO = new JSONObject(data.replace("null",""));
                modelList.add(new Model(JO.getString("店名"),JO.getString("圖片"),JO.getString("地址")));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}


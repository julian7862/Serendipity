package com.example.menu.fetcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.menu.A03_TabMenu.A03_MainTab;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;




public class fetchData extends AsyncTask<Void,Void,Boolean> {

    String data="";
    String dataParsed ="";
    String singleParsed= "";
    String urllink="";
    Bitmap bitmap = null;
    ImageView imageViews;
    String text;
    // Weak references will still allow the Activity to be garbage-collected
    WeakReference<Context> weakReference;
    public OnTaskCompleted<Boolean> listener = null;

    public fetchData(OnTaskCompleted<Boolean> listener){
        this.listener=listener;
    }

    public  fetchData(Context context){
        weakReference = new WeakReference<>(context);
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        try {

            URL url = new URL("http://140.117.71.141:5000/?name="+ URLEncoder.encode(text,"UTF-8"));

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

            //Log.d("ResultAA",this.data);
            JSONObject JO = new JSONObject(data);
//            singleParsed = "編號:" + JO.getString("編號") + "\n" +
//                    "店名:" + JO.getString("店名") + "\n" +
//                    "分數:" + JO.getString("分數") + "\n" +
//                    "地址:" + JO.getString("地址") + "\n" +
//                    "距離:" + JO.getString("distance") + "\n";
            //dataParsed = dataParsed+singleParsed+"\n";
            //urllink = JO.getString("圖片");
            //urllink=JO.optString("圖片");
        } catch (MalformedURLException e) {
            e.printStackTrace();
             return false;
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    protected void onPostExecute(Boolean aVoid) {
        //super.onPostExecute(aVoid);
        //Log.d("ResultAA",this.data);
        A03_MainTab.searchResult = this.data;

        listener.onTaskCompleted(true);

        //MainActivity.data.setText(this.dataParsed);
        //Picasso.get().load(this.urllink).into(MainActivity.imageView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = weakReference.get();
        if (context !=null){
            SharedPreferences sharedPreferences = context.getSharedPreferences("test", MODE_PRIVATE);
            text = sharedPreferences.getString("Text","null");
            Log.d("PreEx", "onPreExecute: "+text);
        }
    }


}

package com.example.mannas.ytask.Content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mannas.ytask.BuildConfig;
import com.example.mannas.ytask.Content.Feed;
import com.example.mannas.ytask.PreferencesManager;
import com.google.gson.Gson;
import com.google.gson.internal.Primitives;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mannas on 6/19/2017.
 */

public class FeedsLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<Feed>> {
    public static int id = 1;

    OkHttpClient client;
    public static final int home = 0;
    public static final int world = 1;
    public static final int national = 2;
    public static final int politics = 3;
    public static final int nyregion = 4;
    public static final int business = 5;
    public static final int opinion = 6;
    public static final int technology = 7;
    public static final int science = 8;

    // world , national , politics , nyregion , business , opinion, technology , science


    int mType = -1;
    String BaseUriP1 = "https://api.nytimes.com/svc/topstories/v2/", BaseUriP2 =".json?api-key="+ BuildConfig.API_key;

    public FeedsLoader(Context context) {
        super(context);
        mType = PreferencesManager.getOrderBy(getContext());
    }

    private String BuildUrl(){
        switch ( mType) {
            case home:
                return BaseUriP1 + "home" + BaseUriP2;
            case world:
                return BaseUriP1 + "world" + BaseUriP2;
            case national:
                return BaseUriP1 + "national" + BaseUriP2;
            case politics:
                return BaseUriP1 + "politics" + BaseUriP2;
            case nyregion:
                return BaseUriP1 + "nyregion" + BaseUriP2;
            case business:
                return BaseUriP1 + "business" + BaseUriP2;
            case opinion:
                return BaseUriP1 + "opinion" + BaseUriP2;
            case technology:
                return BaseUriP1 + "technology" + BaseUriP2;
            case science:
                return BaseUriP1 + "science" + BaseUriP2;
            default:
                return "";
        }
    }
    @Override
    public ArrayList<Feed> loadInBackground() {
        String Url = BuildUrl();
        client = new OkHttpClient();
        String response = null;
        try {
            response = download(Url);
            cache(mType,response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return pars(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String download(String url) throws IOException {
        String []projection = {Contract.Feed.Columns.json};
        Cursor c = getContext().getContentResolver().query(Contract.Feed.uri, projection,Contract.Feed.Columns.id+" = "+mType ,null,null);

        if( c!=null && c.moveToFirst()){
            String json = c.getString(c.getColumnIndex(projection[0]));
            c.close();
            Log.i("loader ","finish");
            if(!json.equals(""))
                return json;
        }else if(!isOffline()){
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            Log.i("loader ","finish");
             return response.body().string();
        }
        return "";
    }

    private ArrayList<Feed> pars(String json) throws JSONException {
        ArrayList<Feed> ls = new ArrayList<>();
        if(json != null){
            JSONObject all = new JSONObject(json);
            JSONArray results = (JSONArray) all.get("results");
            if(results != null){
                Gson gson = new Gson();
                for(int i=0;i<results.length();i++){
                    Feed f = gson.fromJson(results.get(i).toString(),Feed.class);
                    StringBuilder del=new StringBuilder(f.updated_date);
                    del.deleteCharAt(22);
                    f.updated_date = del.toString();
                    ls.add(f);
                }
                return ls;
            }
        }
        return null;
    }

    private void cache(final int type, final String json){
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {}
            protected Void doInBackground(Void... unused) {
                if( json!=null && !json.equals("") ){
                    ContentValues v = new ContentValues(1);
                    v.put(Contract.Feed.Columns.json,json );
                    v.put(Contract.Feed.Columns.id,type );
                    getContext().getContentResolver().insert(Contract.Feed.uri, v);
                }
                return null;
            }
            protected void onPostExecute(Void unused) {}
        }.execute();

    }

    private Boolean isOffline(){
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info==null  || !info.isConnected();
    }
}

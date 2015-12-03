package com.example.krusty.exam;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.String;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    int limit, skip;
    String tags, jsonResult, source;
    boolean onlyInStock = false;
    GridView gv;
    Context context;
 int counter=0;
    List<String> typeList, idList, priceList, faceList, tagsList, sizeList, stockList, buynowList, nostockList;
    private CustomAdapter mListAdapter;

    private EditText edittext;

    int visibleThreshold = 0;
    int currentPage = 0;
    int previousTotal = 0;
    boolean loading = true;
    boolean novi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false

        if (isInternetPresent != true) {

            Toast.makeText(getApplicationContext(),
                    "THIS PROGRAM NEEDS INTERNET CONNECTION. PLEASE CONNECT TO INTERNET AND TRY AGAIN!", Toast.LENGTH_LONG).show();
            finish();

        } else {

            try {
                File httpCacheDir = new File(this.getCacheDir(), "http");
                long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
                HttpResponseCache.install(httpCacheDir, httpCacheSize);

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Cache install failed",
                        Toast.LENGTH_SHORT).show();
            }


            edittext = (EditText) findViewById(R.id.editText);

            edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_NEXT) {

                        tags = edittext.getText().toString();
                        try {
                            tags = URLEncoder.encode(tags, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new AssertionError("UTF-8 is unknown");

                        }
                        counter=0;

                        visibleThreshold = 0;
                        currentPage = 0;
                        previousTotal = 0;
                        loading = true;


                        typeList.clear();
                        idList.clear();
                        sizeList.clear();
                        priceList.clear();
                        faceList.clear();
                        stockList.clear();
                        tagsList.clear();
                        buynowList.clear();

                        load(0);


                        mListAdapter = null;
                        mListAdapter = new CustomAdapter(MainActivity.this, faceList, priceList, buynowList);

                        gv = (GridView) findViewById(R.id.gridView1);
                        gv.setAdapter(mListAdapter);
                        gv.setOnScrollListener(null);
                        gv.setOnScrollListener(new listener());


                        return true;
                    }
                    return false;
                }
            });


            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
            if (checkBox.isChecked()) {
                onlyInStock = true;

            } else {
                onlyInStock = false;
            }

            checkBox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //is chkIos checked?
                    if (((CheckBox) v).isChecked()) {
                        onlyInStock = true;
                           run();


                    } else {
                        onlyInStock =  false;
                        run();

                    }
                }
            });


            typeList = new ArrayList<String>();
            idList = new ArrayList<String>();
            sizeList = new ArrayList<String>();
            priceList = new ArrayList<String>();
            faceList = new ArrayList<String>();
            stockList = new ArrayList<String>();
            tagsList = new ArrayList<String>();
            buynowList = new ArrayList<String>();
            nostockList = new ArrayList<String>();

            load(0);

            gv = (GridView) findViewById(R.id.gridView1);



            mListAdapter = new CustomAdapter(this, faceList, priceList, buynowList);
            gv.setAdapter(mListAdapter);
            gv.setOnScrollListener(new listener());


        }
    }

public void run() {
counter=0;

    tags = edittext.getText().toString();
    try {
        tags = URLEncoder.encode(tags, "utf-8");
    } catch (UnsupportedEncodingException e) {
        throw new AssertionError("UTF-8 is unknown");


    }

    visibleThreshold = 0;
    currentPage = 0;
    previousTotal = 0;
    loading = true;

    typeList.clear();
    idList.clear();
    sizeList.clear();
    priceList.clear();
    faceList.clear();
    stockList.clear();
    tagsList.clear();
    buynowList.clear();

    load(0);

    mListAdapter = null;
    mListAdapter = new CustomAdapter(MainActivity.this, faceList, priceList, buynowList);

    gv = (GridView) findViewById(R.id.gridView1);
    gv.setAdapter(mListAdapter);
    gv.setOnScrollListener(null);
    gv.setOnScrollListener(new listener());

}


    public void load(int skip) {
counter++;

        if (edittext.getText().toString().matches("")) {

            new RequestTask().execute("http://74.50.59.155:5000/api/search?skip=" + String.valueOf(skip) + "&limit=9");
        } else {

            new RequestTask().execute("http://74.50.59.155:5000/api/search?skip=" + String.valueOf(skip)  + "&limit=9" + "&q=" + tags);
        }

    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {

            String responseString = null;
            try {

                URL obj = new URL(uri[0]);

                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setUseCaches(true);

                try {
                    con.addRequestProperty("Cache-Control",
                            "only-if-cached");
                    con.addRequestProperty("Cache-Control", "max-stale=" + (60 * 60));
                    BufferedReader cached = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response1 = new StringBuffer();

                    while ((inputLine = cached.readLine()) != null) {
                        response1.append(inputLine + "\n");
                    }
                    cached.close();

                    jsonResult = response1.toString();
                    source = "FROM CACHE";

                } catch (FileNotFoundException e) {

                    HttpURLConnection con2 = (HttpURLConnection) obj.openConnection();

                    con2.setRequestMethod("GET");
                    con2.addRequestProperty("Cache-Control", "max-stale=" + (60 * 60));

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con2.getInputStream()));
                    String inputLine;
                    StringBuffer response1 = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response1.append(inputLine + "\n");
                    }
                    in.close();

                    jsonResult = response1.toString();
                    source = "FROM ONLINE";
                }


            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String lines[] = jsonResult.split("\\r?\\n");

            if (onlyInStock) {
                for (int i = 0; i<lines.length;  i++) {
                    if (!lines[i].contains("\"stock\":0")) {
                     nostockList.add(lines[i]);

                    }
                }
              lines = nostockList.toArray(new String[nostockList.size()]);
nostockList.clear();
            }



            try {

                for (int i = 0; i < lines.length; i++) {
                    JSONObject jsonResponse = new JSONObject(lines[i]);

                if(    !idList.contains(jsonResponse.getString("id") )) {
                        typeList.add(jsonResponse.getString("type"));
                        idList.add(jsonResponse.getString("id"));
                        sizeList.add(jsonResponse.getString("size"));
                        priceList.add("$" + jsonResponse.getString("price"));
                        faceList.add(jsonResponse.getString("face"));
                        stockList.add(jsonResponse.getString("stock"));
                        tagsList.add(jsonResponse.getString("tags"));


                        if (jsonResponse.getString("stock").equals("1")) {
                            buynowList.add("BUY NOW!\n(Only 1 more in stock!)");

                        } else if (jsonResponse.getString("stock").equals("0")) {
                            buynowList.add("NOT IN STOCK");
                        } else {

                            buynowList.add("BUY NOW!");
                        }

                }
                }



            } catch (JSONException e) {

            }


            mListAdapter.notifyDataSetChanged();
            final Toast toast = Toast.makeText(getApplicationContext(), "LOADING " + source, Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 500);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();

        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            Toast.makeText(getApplicationContext(), "Cached: " + String.valueOf(cache.size()),
                    Toast.LENGTH_SHORT).show();

            cache.flush();
        }
    }


    public class listener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {


            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }

            if(!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 0))
            {


                load(9*counter);

                loading = true;
            }

        }
    }

    public class ConnectionDetector {

        private Context _context;

        public ConnectionDetector(Context context){
            this._context = context;
        }

        public boolean isConnectingToInternet(){
            ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null)
            {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return true;
                        }

            }
            return false;
        }
    }



}



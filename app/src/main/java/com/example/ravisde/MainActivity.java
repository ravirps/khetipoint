package com.example.ravisde;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout ;
    ListView list ;
    DatabaseClient dbclient;
    TextView textView;
    UserDao userDao;
    String responseData;
    ProgressBar progressBar;
    ImageView errorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout =  findViewById(R.id.refreshLayout);
        dbclient=DatabaseClient.getInstance(getApplicationContext());
        userDao = dbclient.getAppDatabase().userDao();
        progressBar=findViewById(R.id.progress_circular);
        errorImage=findViewById(R.id.errorImage);
        list = findViewById(R.id.list);
//
        textView = findViewById(R.id.text);
// ...

        // Declaring a layout (changes are to be made to this)
        // Declaring a textview (which is inside the layout)
        // Refresh  the layout
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // Your code goes here
                        // In this code, we are just
                        // changing the text in the textbox
                        refreshData();
                        // This line is important as it explicitly
                        // refreshes only once
                        // If "true" it implicitly refreshes forever
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }
    private void updateList()
    {
        if(responseData==null)
        {
            showError();
            return;
        }
        progressBar.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);
//        textView.setVisibility(View.VISIBLE);
//
//        textView.setText(responseData);
        list = findViewById(R.id.list);

        Gson gson = new Gson();

        Type userListType = new TypeToken<ArrayList<Root>>(){}.getType();

        ArrayList<Root> rootArray = gson.fromJson(responseData, userListType);
        CustomAdapter customAdapter = new CustomAdapter(this, rootArray);
        list.setAdapter(customAdapter);


//        Log.e("upadting",responseData);
    }

    private void refreshData()
    {

        progressBar.setVisibility(View.VISIBLE);
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://private-anon-8a33584439-githubtrendingapi.apiary-mock.com/repositories";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response.substring(0,500));

                        List<User> users = userDao.getAll();
                        if(users!=null)userDao.delete(users.get(0));
                        User curr=new User();
                        curr.data=response;
                        curr.uid=1;
                        userDao.insertAll(curr);
                        fetchFromDatabase();
                        updateList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Unable to fetch the data .check your internet connnection", Toast.LENGTH_LONG).show();
                fetchFromDatabase();
                updateList();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void fetchFromDatabase()
    {
        List<User> users= userDao.getAll();
        if(users==null)
        {
            responseData=null;
            updateList();
        }
        responseData=users.get(0).data;
    }
    void showError()
    {
        progressBar.setVisibility(View.GONE);
        list.setVisibility(View.GONE);

        errorImage.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"Unable to fetch the data .check your internet connnection", Toast.LENGTH_LONG).show();
    }

    public class BuiltBy{
        public String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String avatar;
        public String username;
    }

    public class Root{
        public String author;
        public String name;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLanguageColor() {
            return languageColor;
        }

        public void setLanguageColor(String languageColor) {
            this.languageColor = languageColor;
        }

        public int getStars() {
            return stars;
        }

        public void setStars(int stars) {
            this.stars = stars;
        }

        public int getForks() {
            return forks;
        }

        public void setForks(int forks) {
            this.forks = forks;
        }

        public int getCurrentPeriodStars() {
            return currentPeriodStars;
        }

        public void setCurrentPeriodStars(int currentPeriodStars) {
            this.currentPeriodStars = currentPeriodStars;
        }

        public List<BuiltBy> getBuiltBy() {
            return builtBy;
        }

        public void setBuiltBy(List<BuiltBy> builtBy) {
            this.builtBy = builtBy;
        }

        public String avatar;
        public String url;
        public String description;
        public String language;
        public String languageColor;
        public int stars;
        public int forks;
        public int currentPeriodStars;
        public List<BuiltBy> builtBy;
        public String totext()
        {
            String text="";
            String bb="";
                    for(int i=0;i<this.builtBy.size();i++)
                    {
                        bb.concat(builtBy.get(i)+",    ");
                    }
            text="Description= "+this.description+"\n"+"\n"+
                    "language= "+this.language+"\n"+"\n"+
                    "buitl by= " +bb;
            return text;
        }
    }
}
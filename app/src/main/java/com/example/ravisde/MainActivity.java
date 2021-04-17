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

        ListView list = findViewById(R.id.list);
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
        textView.setVisibility(View.VISIBLE);

        textView.setText(responseData);
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
        textView.setVisibility(View.GONE);

        errorImage.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"Unable to fetch the data .check your internet connnection", Toast.LENGTH_LONG).show();
    }
}
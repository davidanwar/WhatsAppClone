package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listViewUser;
    private ArrayList<String> waUser;
    private ArrayAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app);

        listViewUser = findViewById(R.id.listViewUser);
        waUser = new ArrayList<>();
        adapter = new ArrayAdapter(WhatsAppActivity.this, android.R.layout.simple_list_item_1, waUser);
        swipeRefreshLayout = findViewById(R.id.swipeContainer);

        listViewUser.setOnItemClickListener(this);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null){
                        for (ParseUser user : objects){
                            waUser.add(user.getUsername());
                        }
                        listViewUser.setAdapter(adapter);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

                    // tidak menampilkan data yang sudah ada di listView
                    parseQuery.whereNotContainedIn("username", waUser);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size() > 0) {
                                if (e == null){

                                }
                                for (ParseUser user : objects){
                                    waUser.add(user.getUsername());
                                }

                                // notifikasi listView bahwa data telah berubah
                                adapter.notifyDataSetChanged();

                                // agar tidak melakukan refresing terus menerus
                                if (swipeRefreshLayout.isRefreshing()){
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            } else {
                                if (swipeRefreshLayout.isRefreshing()){
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemLogoutUser:
                ParseUser.getCurrentUser();
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Intent intent = new Intent(WhatsAppActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(WhatsAppActivity.this, WhatsAppChatActivity.class);
        intent.putExtra("selectedUser", waUser.get(position));
        startActivity(intent);

    }
}

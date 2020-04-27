package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppChatActivity extends AppCompatActivity {

    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_chat);

        selectedUser = getIntent().getStringExtra("selectedUser");
        FancyToast.makeText(this, "Chat with " + selectedUser + " Now",
                FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();

        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList<>();
        //adapter = new ArrayAdapter()
    }
}

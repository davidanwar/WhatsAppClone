package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppChatActivity extends AppCompatActivity implements View.OnClickListener {

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

        findViewById(R.id.btnSendMessage).setOnClickListener(this);

        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatsList);

        // update listChat dengan adapter (PENTING!!!!)
        chatListView.setAdapter(adapter);

        try {
            // mengambil data query dari class "Chat"
            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTarget", selectedUser);

            secondUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTarget", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries); // memasukkan semua query
            // mengurutkan chat
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null){
                        for (ParseObject chatObject : objects){
                            String waMessage = chatObject.get("waMessage") + "";
                            if(chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())){
                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }
                            if (chatObject.get("waSender").equals(selectedUser)){
                                waMessage = selectedUser + ": " + waMessage;
                            }
                            chatsList.add(waMessage);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }




    }

    @Override
    public void onClick(View view) {

        final EditText editMessage = findViewById(R.id.editMessage);
        ParseObject chat = new ParseObject("Chat");
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTarget", selectedUser);
        chat.put("waMessage", editMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(WhatsAppChatActivity.this,
                            "Message From " + ParseUser.getCurrentUser().getUsername()
                                    + " send to " +  selectedUser,
                            FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                    chatsList.add(ParseUser.getCurrentUser().getUsername() + ": " + editMessage.getText().toString());
                    // update list view dengan data baru
                    adapter.notifyDataSetChanged();

                    // setelah mengirim pesan edit kembali kosong
                    editMessage.setText("");

                }
            }
        });

    }
}

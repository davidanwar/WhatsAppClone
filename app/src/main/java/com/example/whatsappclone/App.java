package com.example.whatsappclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Q4OPwsErOzubMSDHn8ImvhZxI9QhK5kUiHgwClfG")
                // if defined
                .clientKey("f36Qs9LocDMhSPEdQxyRdIPTZxrKY3qgS6XoADA7")
                .server("https://parseapi.back4app.com/")
                .build()
        );

        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("playerName", "Sean Plott");
        gameScore.saveInBackground();
    }
}

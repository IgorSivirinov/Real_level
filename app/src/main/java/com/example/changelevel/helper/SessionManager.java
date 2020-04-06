package com.example.changelevel.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

import com.example.changelevel.User.User;

public class SessionManager
{
    private static final String SHARED_PREF_NAME="my_name";
    private static SessionManager instance;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
    }
    public static synchronized SessionManager getInstance(Context cont)
    {
        if(instance==null) instance = new SessionManager(cont);
        return instance;
    }

    public void saveUser(User user)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("email",user.getMail());
        editor.putString("name",user.getUserName());
        editor.apply();
    }
    public User getUser()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(sharedPreferences.getString("email",null),sharedPreferences.getString("name",null));
    }
    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("email",null)!=null;
    }
    public void clear()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

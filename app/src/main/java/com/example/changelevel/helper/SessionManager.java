package com.example.changelevel.helper;

import android.content.Context;
import android.provider.ContactsContract;

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
}

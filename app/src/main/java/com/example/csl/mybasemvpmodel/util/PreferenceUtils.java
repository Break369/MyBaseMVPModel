package com.example.csl.mybasemvpmodel.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.csl.mybasemvpmodel.global.MyApplication;


public class PreferenceUtils {

    public static void commit(String name, String value) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("com.pensoon.ocr", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value).commit();
    }

    public static String getValue(String name, String name2) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("com.pensoon.ocr", Context.MODE_PRIVATE);
        String n = sharedPreferences.getString(name, name2);
        return n;
    }

}

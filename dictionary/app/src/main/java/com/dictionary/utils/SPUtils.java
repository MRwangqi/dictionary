package com.dictionary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.dictionary.model.WordModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */

public class SPUtils {

    private static final String FILE = "sharep";

    private static final String WORDS = "words";

    private static final String DAY = "day";

    public static void pushDay(Context context, String day) {
        SharedPreferences sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(DAY, day);
        editor.commit();
    }


    public static String popDay(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        return sp.getString(DAY, "");
    }

    public static List<WordModel> popWords(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        try {
            List<WordModel> list = stringTolist(sp.getString(WORDS, ""));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void pushWords(Context context, List<WordModel> list) {
        SharedPreferences sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        try {
            editor.putString(WORDS, listTostring(list));
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String listTostring(List<WordModel> list) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(list);
        String SceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return SceneListString;
    }

    public static List<WordModel> stringTolist(String str) throws Exception {
        byte[] mobileBytes = Base64.decode(str.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        List<WordModel> list = (List<WordModel>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return list;
    }
}
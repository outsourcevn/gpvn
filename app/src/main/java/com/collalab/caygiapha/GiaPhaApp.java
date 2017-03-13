package com.collalab.caygiapha;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.collalab.caygiapha.treeview.model.TreeNode;

import java.util.Hashtable;

import io.realm.Realm;

/**
 * Created by laptop88 on 3/6/2017.
 */

public class GiaPhaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

    }

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static TreeNode currentNode;

    public static Typeface getFont(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}

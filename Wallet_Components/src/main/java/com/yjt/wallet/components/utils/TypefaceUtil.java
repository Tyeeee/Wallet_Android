package com.yjt.wallet.components.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.SimpleArrayMap;

import com.yjt.wallet.components.constant.Regex;

public class TypefaceUtil {

    private static TypefaceUtil typefaceUtil;
    private static SimpleArrayMap<String, Typeface> simpleArrayMap = new SimpleArrayMap<>();

    private TypefaceUtil() {
        // cannot be instantiated
    }

    public static synchronized TypefaceUtil getInstance() {
        if (typefaceUtil == null) {
            typefaceUtil = new TypefaceUtil();
        }
        return typefaceUtil;
    }

    public static void releaseInstance() {
        if (typefaceUtil != null) {
            typefaceUtil = null;
        }
        if (simpleArrayMap != null) {
            simpleArrayMap.clear();
            simpleArrayMap = null;
        }
    }


    public synchronized Typeface get(Context ctx, String name) {
        if (!simpleArrayMap.containsKey(name)) {
            Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), String.format(Regex.FILE_TTF.getRegext(), name));
            simpleArrayMap.put(name, typeface);
            return typeface;
        }
        return simpleArrayMap.get(name);
    }
}

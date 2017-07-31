package com.emo.lkplayer.outerlayer.storage.daos;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
/**
 * Created by shoaibanwar on 7/30/17.
 */

public class TypeConverter_IntegerArray {

    @TypeConverter
    public static String intArrToString(int[] array) {
        Gson gson = new Gson();
        return gson.toJson(array);
    }

    @TypeConverter
    public static int[] fromStringToIntArr(String jsonString) {
        Gson gson = new Gson();
        int[] integerArr = gson.fromJson(jsonString,int[].class);
        return integerArr;
    }
}

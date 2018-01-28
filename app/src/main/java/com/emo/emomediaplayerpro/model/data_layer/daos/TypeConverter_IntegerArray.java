package com.emo.emomediaplayerpro.model.data_layer.daos;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;

import java.util.ArrayList;

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

    @TypeConverter
    public static String longListToString(ArrayList<Long> longlist) {
        Gson gson = new Gson();
        return gson.toJson(longlist);
    }

    @TypeConverter
    public static ArrayList<Long> fromStringToLongList(String jsonString) {
        Gson gson = new Gson();
        ArrayList<Long> longlist = gson.fromJson(jsonString,ArrayList.class);
        return longlist;
    }

    @TypeConverter
    public static String IntegerListToString(ArrayList<Integer> longlist) {
        Gson gson = new Gson();
        return gson.toJson(longlist);
    }

    @TypeConverter
    public static ArrayList<Integer> fromStringToIntegerList(String jsonString) {
        Gson gson = new Gson();
        ArrayList<Integer> longlist = gson.fromJson(jsonString,ArrayList.class);
        return longlist;
    }
}

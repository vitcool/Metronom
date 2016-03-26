package com.example.vitalykulyk.metronom;

/**
 * Created by Vitaly Kulyk on 26.03.2016.
 */
public class Constants {

    public interface ACTION {
        public static String MAIN_ACTION =              "com.example.vitalykulyk.metronom.action.main";
        public static String INIT_ACTION =              "com.example.vitalykulyk.metronom.action.init";
        public static String PREV_ACTION =              "com.example.vitalykulyk.metronom.action.prev";
        public static String PLAY_ACTION =              "com.example.vitalykulyk.metronom.action.play";
        public static String NEXT_ACTION =              "com.example.vitalykulyk.metronom.action.next";
        public static String STARTFOREGROUND_ACTION =   "com.example.vitalykulyk.metronom.action.startforeground";
        public static String STOPFOREGROUND_ACTION =    "com.example.vitalykulyk.metronom.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}

package com.example.mytask;


import android.provider.BaseColumns;

public class words {
    public words() {
    }
    public static abstract class Word implements BaseColumns {
        public static final String T="control";
        public static final String M="month";
        public static final String D="day";
        public static final String C="wendu";
        public static final String L="location";
    }

}

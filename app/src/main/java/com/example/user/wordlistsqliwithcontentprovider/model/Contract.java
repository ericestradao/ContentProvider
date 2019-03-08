package com.example.user.wordlistsqliwithcontentprovider.model;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    private Contract() {}
    public static final String DATABASE_NAME = "wordlist";


    public static final int ALL_ITEMS = -2;
    public static final String COUNT = "count";

    public static final String AUTHORITY =
            "com.example.user.wordlistsqlwithcontentprovider.provider";

    public static final String CONTENT_PATH = "words";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH);
    public static final Uri ROW_COUNT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH + "/" + COUNT);

    public static final String SINGLE_RECORD_MIME_TYPE =
            "vnd.android.cursor.item/vnd.com.example.provider.words";
    public static final String MULTIPLE_RECORDS_MIME_TYPE =
            "vnd.android.cursor.item/vnd.com.example.provider.words";


    public static abstract class WordList implements BaseColumns {
        public static final String WORD_LIST_TABLE = "word_entries";

        public static final String KEY_ID = "_id";
        public static final String KEY_WORD = "word";
    }

}

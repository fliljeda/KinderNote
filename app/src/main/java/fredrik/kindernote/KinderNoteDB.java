package fredrik.kindernote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fredrik on 2016-07-05.
 */
public class KinderNoteDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "KinderNoteDB";
    private static final int DATABASE_VERSION = 2;

    private static final String CHILDINFO_NAME = "child_basic_info";
    private static final String CHILDINFO_TABLE_CREATE =
            "CREATE TABLE " + CHILDINFO_NAME + " (" +
                "firstname" + " TEXT NOT NULL, " +
                "lastname" + " TEXT NOT NULL, " +
                "personalnumber" + " TEXT NOT NULL UNIQUE, " +
                "birthdate" + " TEXT NOT NULL" +
                ");";

    private static final String PARENTINFO_NAME = "parent_info";
    private static final String PARENTINFO_TABLE_CREATE =
            "CREATE TABLE " + PARENTINFO_NAME + " (" +
                    "childnumber" + " TEXT NOT NULL, " +
                    "parentname" + " TEXT NOT NULL, " +
                    "parentphone" + " TEXT NOT NULL, " +
                    "comment" + " TEXT NOT NULL" +
                    ");";

    private static final String DAILYNOTES_TABLE_NAME = "daily_notes";
    private static final String DAILYNOTES_TABLE_CREATE =
            "CREATE TABLE " + DAILYNOTES_TABLE_NAME + " (" +
                    "personalnumber" + " TEXT NOT NULL, " +
                    "date" + " TEXT NOT NULL, " +
                    "presence" + " TEXT NOT NULL, " +
                    "comment" + " TEXT NOT NULL, " +
                    "dayinshort" + " TEXT NOT NULL" +
                    ");";



    public KinderNoteDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CHILDINFO_TABLE_CREATE);
        db.execSQL(PARENTINFO_TABLE_CREATE);
        db.execSQL(DAILYNOTES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}

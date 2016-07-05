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

    private static final String CHILDINFO_NAME = "ChildInfo";
    private static final String CHILDINFO_TABLE_CREATE =
            "CREATE TABLE " + CHILDINFO_NAME + " (" +
                "name" + " TEXT NOT NULL, " +
                "personalnumber" + " TEXT NOT NULL, " +
                "birthdate" + " TEXT NOT NULL" +
                ");";



    public KinderNoteDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CHILDINFO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}

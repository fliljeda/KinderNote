package fredrik.kindernote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DisplayChild extends AppCompatActivity {
    private String personalnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        personalnumber = getIntent().getStringExtra("personalnumber");
        setupInformation();
    }

    private void setupInformation(){
        SQLiteDatabase db = new KinderNoteDB(this).getReadableDatabase();
        String[] childcolumns = {"firstname", "lastname", "birthdate"};
        String[] parentcolumns = {"parentname", "parentphone", "comment"};
        String[] pnum = {personalnumber};
        Cursor childcursor = db.query("child_basic_info", childcolumns, "personalnumber = ?", pnum, null, null, null, null);
        Cursor parentcursor = db.query("parent_info", parentcolumns, "childnumber = ?", pnum, null, null, null, null);
        childcursor.moveToFirst();
        if(childcursor.getCount()>0) {
            ((TextView) findViewById(R.id.child_name)).setText(childcursor.getString(0));
            ((TextView) findViewById(R.id.child_birth_date)).setText(childcursor.getString(2));
            ((TextView) findViewById(R.id.child_personal_number)).setText(personalnumber);
        }

    }





}

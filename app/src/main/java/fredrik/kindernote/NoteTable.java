package fredrik.kindernote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteTable extends AppCompatActivity {
    private int orderofcurrentchild = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNotes();
    }

    private void setupNotes(){
        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");

        SQLiteDatabase db = new KinderNoteDB(this).getReadableDatabase();
        String[] childcolumns = {"firstname", "lastname", "personalnumber", };
        String[] notecolumns = {"personalnumber", "date", "presence", "comment", "dayinshort"};
        Cursor childcursor = db.query("child_basic_info", childcolumns, null, null, null, null, null);

        if(childcursor != null && childcursor.getCount()>0){
            childcursor.moveToFirst();
            do {
                String[] qvalues = {childcursor.getString(2), date};
                Cursor notecursor = db.query("daily_notes", notecolumns, "personalnumber = ? AND date = ?", qvalues, null, null, null);
                String firstname, lastname, personalnumber, presence, comment, dayinshort;
                if(notecursor != null && notecursor.getCount()>0){
                    notecursor.moveToFirst();
                    presence = notecursor.getString(2);
                    comment = notecursor.getString(3);
                    dayinshort = notecursor.getString(4);
                }else{
                    presence = "";
                    comment = "";
                    dayinshort = "";
                }
                firstname = childcursor.getString(0);
                lastname = childcursor.getString(1);
                personalnumber = childcursor.getString(2);
                addRows(firstname, lastname, personalnumber, presence, comment, dayinshort);
            }while(childcursor.moveToNext());
        }
    }

    private void addRows(String firstname, String lastname,
                         String personalnumber, String presence,
                         String comment, String dayinshort){
        LinearLayout noteholder = (LinearLayout) findViewById(R.id.noteholder);

        LinearLayout thisnote = new LinearLayout(this);
        thisnote.setOrientation(LinearLayout.HORIZONTAL);

        TextView ordernumber = new TextView(this);
        ordernumber.setText("" + orderofcurrentchild);
        ordernumber.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) LinearLayout.LayoutParams.WRAP_CONTENT));
        thisnote.addView(ordernumber);


        TextView name = new TextView(this);
        name.setText(firstname + " " + lastname);
        name.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_name_weight));
        thisnote.addView(name);

        TextView number = new TextView(this);
        number.setText(personalnumber);
        number.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_personnumber_weight));
        thisnote.addView(number);

        TextView pres = new TextView(this);
        pres.setText(presence);
        pres.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_presence_weight));
        thisnote.addView(pres);

        TextView commentview = new TextView(this);
        commentview.setText(comment);
        commentview.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_comment_weight));
        thisnote.addView(commentview);

        TextView dayview = new TextView(this);
        dayview.setText(dayinshort);
        dayview.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_day_weight));
        thisnote.addView(dayview);

        noteholder.addView(thisnote);

        orderofcurrentchild++;
    }


}

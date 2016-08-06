package fredrik.kindernote;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteTable extends AppCompatActivity {
    private int orderofcurrentchild = 1;
    public ArrayList<LinearLayout> childrows = new ArrayList<LinearLayout>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNotes();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
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
                firstname = childcursor.getString(0);
                lastname = childcursor.getString(1);
                personalnumber = childcursor.getString(2);
                if(notecursor != null && notecursor.getCount()>0){
                    notecursor.moveToFirst();
                    presence = notecursor.getString(2);
                    comment = notecursor.getString(3);
                    dayinshort = notecursor.getString(4);
                }else{
                    presence = "";
                    comment = "";
                    dayinshort = "";

                    //if no has been found with that personalnumber, a new one will be added
                    SQLiteDatabase writedb = new KinderNoteDB(this).getWritableDatabase();
                    ContentValues insertValues = new ContentValues();
                    insertValues.put("personalnumber", personalnumber);
                    insertValues.put("date", date);
                    insertValues.put("presence", presence);
                    insertValues.put("comment", comment);
                    insertValues.put("dayinshort", dayinshort);
                    writedb.insert("daily_notes", null, insertValues);

                }

                addRows(firstname, lastname, personalnumber, presence, comment, dayinshort);
            }while(childcursor.moveToNext());
        }
    }

    private void addRows(String firstname, String lastname,
                         final String personalnumber, String presence,
                         String comment, String dayinshort){
        LinearLayout noteholder = (LinearLayout) findViewById(R.id.noteholder);

        LinearLayout thisnote = new LinearLayout(this);
        thisnote.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        thisnote.setOrientation(LinearLayout.HORIZONTAL);
        thisnote.setFocusableInTouchMode(true);

        TextView ordernumber = new TextView(this);
        ordernumber.setText("" + orderofcurrentchild);
        //ordernumber.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_ordernumber_weight));
        ordernumber.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 1));
        thisnote.addView(ordernumber);


        TextView name = new TextView(this);
        name.setText(firstname + " " + lastname);
       // name.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_name_weight));
        name.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 2));
        name.setClickable(true);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteTable.this, DisplayChild.class);
                intent.putExtra("personalnumber", personalnumber);
                startActivity(intent);

            }
        });
        thisnote.addView(name);

        TextView number = new TextView(this);
        number.setText(personalnumber);
        //number.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_personnumber_weight));
        number.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 2));
        thisnote.addView(number);

        final EditText pres = new EditText(this);
        pres.setText(presence);
        //pres.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_presence_weight));
        pres.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 2));
        pres.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SQLiteDatabase db = new KinderNoteDB(NoteTable.this).getWritableDatabase();
                ContentValues changeValues = new ContentValues();
                changeValues.put("presence", s.toString());
                String[] where = {personalnumber};
                int i = db.update("daily_notes", changeValues, "personalnumber=?", where);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        thisnote.addView(pres);

        EditText commentview = new EditText(this);
        commentview.setText(comment);
        //commentview.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_comment_weight));
        commentview.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 3));
        commentview.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SQLiteDatabase db = new KinderNoteDB(NoteTable.this).getWritableDatabase();
                ContentValues changeValues = new ContentValues();
                changeValues.put("comment", s.toString());
                String[] where = {personalnumber};
                int i = db.update("daily_notes", changeValues, "personalnumber=?", where);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        thisnote.addView(commentview);

        EditText dayview = new EditText(this);
        dayview.setText(dayinshort);
        //dayview.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) R.integer.notetable_day_weight));
        dayview.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 3));
        dayview.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SQLiteDatabase db = new KinderNoteDB(NoteTable.this).getWritableDatabase();
                ContentValues changeValues = new ContentValues();
                changeValues.put("dayinshort", s.toString());
                String[] where = {personalnumber};
                int i = db.update("daily_notes", changeValues, "personalnumber=?", where);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        thisnote.addView(dayview);

        //Adds to internal list
        childrows.add(thisnote);

        noteholder.addView(thisnote);

        orderofcurrentchild++;
    }

    public void goBack(View view){
        finish();
    }


}

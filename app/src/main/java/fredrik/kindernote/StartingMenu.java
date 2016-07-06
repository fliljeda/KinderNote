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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StartingMenu extends AppCompatActivity {
    Boolean pressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_starting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDailyPad(View view){
        //Intent intent = new Intent(this, );

        //Creating a new string with the format YYYYMMDD
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
        String date = dateFormat.format(c.getTime()).toString();

        Button button = (Button) findViewById(R.id.button);

        if(pressed) {
            button.setText(getString(R.string.open_daily_pad));
            pressed = false;
        }else{
            button.setText(date);
            pressed = true;
        }
    }

    //Opens the activity to add a new child
    public void addNewChild(View view){
        Intent intent = new Intent(this, AddNewChildActivity.class);
        startActivity(intent);
    }

    public void testDB(View view){
        SQLiteDatabase db = new KinderNoteDB(this).getReadableDatabase();
        String[] columns = {"parentname", "parentphone", "comment"};
        Cursor cursor = db.query("parent_info", columns, "childnumber = '9406194713'", null, null, null, null);
        if (cursor != null) {
            //cursor.moveToFirst();
            //String pno = "" + cursor.getCount();
            //if(pno != null && pno.length() > 0) {
            //    ((Button) findViewById(R.id.dbTest)).setText(pno);
            //}
            cursor.moveToFirst();

            if(cursor.getCount()> 0) {
                do {
                    TextView tv = (TextView) findViewById(R.id.tester);
                    tv.setText(tv.getText().toString() + "\n" + cursor.getString(0));

                } while (cursor.moveToNext());
            }
        }
    }

    public void dropDB(View view){
        this.deleteDatabase("KinderNoteDB");
    }
}

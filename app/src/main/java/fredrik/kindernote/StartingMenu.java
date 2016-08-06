package fredrik.kindernote;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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

        //Creating a new string with the format yyMMdd, example: 160619 for the 19 june 2016
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String date = dateFormat.format(c.getTime()).toString();

        Button button = (Button) findViewById(R.id.button);
        /*
        if(pressed) {
            button.setText(getString(R.string.open_daily_pad));
            pressed = false;
        }else{
            button.setText(date);
            pressed = true;
        }
        */
        Intent intent = new Intent(this, NoteTable.class);
        intent.putExtra("Date", date);
        startActivity(intent);
    }

    public void openPad(View view){
        String year = ((EditText) findViewById(R.id.startmenu_year)).getText().toString();
        String month = ((EditText) findViewById(R.id.startmenu_month)).getText().toString();
        String day = ((EditText) findViewById(R.id.startmenu_day)).getText().toString();
        String date;
        if(!(date = createDateFormat(year, month, day)).equals("err")){
            Intent intent = new Intent(this, NoteTable.class);
            intent.putExtra("Date", date);
            startActivity(intent);
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(StartingMenu.this).create();
            alertDialog.setTitle("Can't open log:");
            alertDialog.setMessage("Wrong Date Format");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private String createDateFormat(String year, String month, String day){
        String err = "err";
        int yearlength = year.length();
        if(yearlength != 2 && yearlength != 4){
            return err;
        }else if(month.length() == 0 || month.length() > 12){
            return err;
        }else if(day.length() == 0 || day.length() > 31){
            return err;
        }
        if(year.length() == 4){
            year = year.substring(2,4);
        }
        if(month.length() == 1){
            month = "0" + month;
        }
        if(day.length() == 1){
            day = "0" + day;
        }
        return year + month + day;
    }

    //Opens the activity to add a new child
    public void addNewChild(View view){
        Intent intent = new Intent(this, AddNewChildActivity.class);
        startActivity(intent);
    }


    /**
     * Opens remove child activity
     */
    public void removeChild(View view){
        Intent intent = new Intent(this, RemoveChild.class);
        startActivity(intent);

    }

    public void dropDB(View view){
        this.deleteDatabase("KinderNoteDB");
    }
}

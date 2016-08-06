package fredrik.kindernote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
            ((TextView) findViewById(R.id.child_birth_date)).setText(getDateString(childcursor.getString(2)));
            ((TextView) findViewById(R.id.child_personal_number)).setText(personalnumber);
        }

        addParentDetails(parentcursor);

    }

    /**
     * Gets a sql cursor and adds views with parentinformation
     */
    private void addParentDetails(Cursor parentcursor){

        if(parentcursor.getCount()>0){
            int parentnumber = 1;
            parentcursor.moveToFirst();
            LinearLayout holder = ((LinearLayout) findViewById(R.id.infoholder));
            do{
                TextView banner = new TextView(this);
                banner.setTextSize(TypedValue.COMPLEX_UNIT_PX, getPixelsFromDp(25f));
                banner.setText("Parent " + parentnumber);
                parentnumber++;
                banner.setPadding(0, getPixelsFromDp(5), 0 ,0);
                banner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.addView(banner);

                TextView name = new TextView(this);
                name.setTextSize(TypedValue.COMPLEX_UNIT_PX, getPixelsFromDp(20f));
                name.setText(parentcursor.getString(0));
                name.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.addView(name);

                TextView number = new TextView(this);
                number.setTextSize(TypedValue.COMPLEX_UNIT_PX, getPixelsFromDp(20f));
                number.setText(parentcursor.getString(1));
                number.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.addView(number);


                TextView comment = new TextView(this);
                comment.setTextSize(TypedValue.COMPLEX_UNIT_PX, getPixelsFromDp(20f));
                comment.setText(parentcursor.getString(2));
                comment.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.addView(comment);

            }while(parentcursor.moveToNext());
        }
    }


    /**
     * Fixes so make a 950515 date to 15 may 1995
     * Will be depracated after 21st century
     * Will show people born in 1900s as 2000s due to not storing what century in database
     */
    private String getDateString(String date){
        if(date.length() == 6) {
            String year = "20" + date.substring(0, 2); // 20 could be handled by date handling but will not be needed for quite some time
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "Oktober", "November", "December"};
            String month = months[Integer.parseInt(date.substring(2, 4)) - 1];
            String day;
            if (date.toCharArray()[4] == '0') {
                day = date.substring(5,6);
            }else{
                day = date.substring(4,6);
            }
            return day + " " + month + " " + year;
        }else {
            return date;
        }
    }



    /**
     * Get method to obtain a distance in pixels by using dp
     */
    private int getPixelsFromDp(float dp){
        final float scale = getResources().getDisplayMetrics().density;
        int pixeldistance = (int) (dp * scale + 0.5f);
        return pixeldistance;
    }


}

package fredrik.kindernote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RemoveChild extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_child);

        addChildrenRows();
    }

    /**
     * Adds the views of children from the database
     */
    private void addChildrenRows(){
        SQLiteDatabase db = new KinderNoteDB(this).getReadableDatabase();
        String[] getChildColumns = {"firstname", "lastname", "personalnumber"};
        Cursor cursor = db.query("child_basic_info", getChildColumns,null,null,"lastname, firstname",null,null);

        LinearLayout rowContainer = (LinearLayout) findViewById(R.id.removechild_holder);

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do{
                final LinearLayout infoContainer = new LinearLayout(this);
                infoContainer.setOrientation(LinearLayout.HORIZONTAL);
                infoContainer.setPadding(0,10,0,10);

                TextView name = new TextView(this);
                name.setText(cursor.getString(0) + " " + cursor.getString(1));
                name.setTextSize(TypedValue.COMPLEX_UNIT_PX, getPixelsFromDp(20f));
                name.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 3));


                TextView personalnumber = new TextView(this);
                personalnumber.setText(cursor.getString(2));
                personalnumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, getPixelsFromDp(20f));
                personalnumber.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 3));

                Button remove = new Button(this);
                remove.setText("-");
                remove.setTextSize(TypedValue.COMPLEX_UNIT_PX, getPixelsFromDp(20f));
                remove.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 1));
                remove.setClickable(true);
                remove.setBackgroundColor(Color.RED);
                final String personalnumberfin = cursor.getString(2);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemoveChild upperthis = RemoveChild.this;
                        SQLiteDatabase database = new KinderNoteDB(upperthis).getWritableDatabase();
                        database.delete("child_basic_info", "personalnumber = " + personalnumberfin, null);
                        database.delete("parent_info", "childnumber = " + personalnumberfin, null);
                        database.delete("daily_notes", "personalnumber = " + personalnumberfin, null);
                        ((LinearLayout) findViewById(R.id.removechild_holder)).removeView(infoContainer);
                    }
                });


                infoContainer.addView(name);
                infoContainer.addView(personalnumber);
                infoContainer.addView(remove);
                rowContainer.addView(infoContainer);
            }while(cursor.moveToNext());
        }
    }


    /*
    * Get method to obtain a distance in pixels by using dp
    * @param dp the dp value you want to convert into the proper float
    */
    private int getPixelsFromDp(float dp){
        final float scale = getResources().getDisplayMetrics().density;
        int pixeldistance = (int) (dp * scale + 0.5f);
        return pixeldistance;
    }
}

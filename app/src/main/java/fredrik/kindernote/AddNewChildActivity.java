package fredrik.kindernote;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddNewChildActivity extends AppCompatActivity {
    ArrayList<Field> fields = new ArrayList<Field>();
    ArrayList<Parent> parents = new ArrayList<Parent>();
    private int numberofparents = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addFields();
    }

    private class Parent{
        private Button removebutton;
        private TextView parenttext;
        private AddNewChildActivity upperthis;
        int parentnumber;
        private ArrayList<Field> fields; //fields that are required


        public Parent(AddNewChildActivity anca, int parentnumber){
            upperthis = anca;
            this.parentnumber = parentnumber;
            fields = new ArrayList<Field>();
            fixViews();
        }

        /**
         * Initiates the views that make up the parent fields and necessary elements
         */
        private void fixViews(){
            //Parentview to insert parent into
            LinearLayout parentholder = (LinearLayout) findViewById(R.id.parentDetails);

            //Holder for views
            LinearLayout thisparent = new LinearLayout(upperthis);
            thisparent.setOrientation(LinearLayout.VERTICAL);

            //Banner text
            TextView parenttext = new TextView(upperthis);
            this.parenttext = parenttext;
            parenttext.setText("Parent " + numberofparents + " Details");
            parenttext.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.parentDetail_text_size));
            parenttext.setTypeface(null, Typeface.BOLD);
            parenttext.setGravity(Gravity.CENTER_HORIZONTAL);
            thisparent.addView(parenttext);


            //Name
            EditText parentname = new EditText(upperthis);
            parentname.setHint("Name");
            thisparent.addView(parentname);
            fields.add(new Field(parentname));

            //Phone number
            EditText parentphone = new EditText(upperthis);
            parentphone.setHint("Phone Number");
            parentphone.setInputType(InputType.TYPE_CLASS_NUMBER);
            thisparent.addView(parentphone);
            fields.add(new Field(parentphone));

            //Comment field
            EditText comment = new EditText(upperthis);
            comment.setHint("Comment");
            thisparent.addView(comment);

            //Button
            Button removebutton = new Button(upperthis);
            this.removebutton =  removebutton;
            removebutton.setText("-");
            removebutton.setLayoutParams(new LinearLayout.LayoutParams(getPixelsFromDp(40f), getPixelsFromDp(40f)));
            removebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    removeParent(view);
                }
            });
            thisparent.addView(removebutton);

            parentholder.addView(thisparent);

        }

        public int getParentnumber(){return parentnumber;}

        public void setParentnumber(int i){parentnumber = i;}

        public View getButton(){
            return (View) removebutton;
        }

        public void changeNumber(int newnumber){
            parenttext.setText("Parent " + newnumber + " Details");
        }

        public ArrayList<Field> getFields(){return fields;}
    }

    /**
     * Help class to handle the fields
     */
    private class Field{
        private EditText et;
        private String originalhint;
        private int originalcolor;
        boolean isMarked = false;

        public Field(EditText et){
            this.et = et;
            originalhint = et.getHint().toString();
            originalcolor = et.getCurrentHintTextColor();
        }

        public boolean isMarkedEmpty(){
            return isMarked;
        }

        public void setMarkedEmpty(boolean b) {
            if(b){
                if(!isMarked) {
                    et.setHint("*" + originalhint);
                    et.setHintTextColor(Color.RED);
                    isMarked = true;
                }
            }else{
                et.setHint(originalhint);
                et.setHintTextColor(originalcolor);
                isMarked = false;
            }
        }

        public String getTextString(){
            return et.getText().toString();
        }

        public EditText getEditText(){
            return et;
        }

        public void setHint(String hint){
            et.setHint(hint);
        }

        public String getOriginalhint(){
            return originalhint;
        }

        public void setOriginalhint(){
            et.setHint(originalhint);
        }

    }

    private void addFields(){

        fields.add(new Field((EditText) findViewById(R.id.newChildName)));
        fields.add(new Field((EditText) findViewById(R.id.newChildYear)));
        fields.add(new Field((EditText) findViewById(R.id.newChildMonth)));
        fields.add(new Field((EditText) findViewById(R.id.newChildDay)));
        fields.add(new Field((EditText) findViewById(R.id.newChildNumber)));
        fields.add(new Field((EditText) findViewById(R.id.newParentName)));
        fields.add(new Field((EditText) findViewById(R.id.newParentPhone)));
    }


    public void addChild(View view){

        boolean reqfieldsfilled = true;

        //Checking if one or more necessary fields are empty
        for(Field field: fields) {
            if(field.getTextString() == null || field.getTextString().length() < 1){
                field.setMarkedEmpty(true);
                reqfieldsfilled = false;
            }else{
                field.setMarkedEmpty(false);
            }
        }

        //Checking if the added parent fields are empty or not
        for(Parent parent: parents){
            for(Field field: parent.getFields()){
                if(field.getTextString() == null || field.getTextString().length() < 1){
                    field.setMarkedEmpty(true);
                    reqfieldsfilled = false;
                }else{
                    field.setMarkedEmpty(false);
                }
            }
        }

        if(reqfieldsfilled){
            finish();
        }
    }

    /**
     * Add new parent to the child
     */
    public void addNewParent(View view){
        numberofparents++;
        parents.add(new Parent(this, numberofparents));
    }


    public void removeParent(View view){
        numberofparents--;

        //Remove from arraylist
        Parent toremove = null;
        int numberforparent = 2;
        for(Parent parent: parents){
            if(parent.getButton() == view){
                toremove = parent;
            }else{
                parent.changeNumber(numberforparent);
                numberforparent++;
            }
        }
        if(toremove!=null) {
            parents.remove(toremove);
        }

        //Remove view
        ((ViewGroup)view.getParent().getParent()).removeView((ViewGroup)view.getParent());

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

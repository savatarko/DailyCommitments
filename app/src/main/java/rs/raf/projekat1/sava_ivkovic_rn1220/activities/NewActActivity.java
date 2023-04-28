package rs.raf.projekat1.sava_ivkovic_rn1220.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewActActivity extends AppCompatActivity {

    private TextView datetv;

    private Button lowbt;
    private Button midbt;
    private Button highbt;
    private EditText titletf;
    //private EditText datetf;
    private EditText starttf;
    private EditText endtf;
    private EditText desctf;
    private Button createbt;
    private Button cancelbt;

    private int currentmode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_act);
        String d = getIntent().getExtras().getString("date");
        init(d);
    }
    private void init(String date){
        initView(date);
        initController();
    }

    private void initView(String date){
        datetv = findViewById(R.id.textView11);
        lowbt = findViewById(R.id.button9);
        midbt = findViewById(R.id.button10);
        highbt = findViewById(R.id.button11);
        titletf = findViewById(R.id.editTextTextPersonName3);
        //datetf = findViewById(R.id.editTextDate);
        starttf = findViewById(R.id.editTextTime);
        endtf = findViewById(R.id.editTextTime2);
        desctf = findViewById(R.id.editTextTextMultiLine);
        createbt = findViewById(R.id.button12);
        cancelbt = findViewById(R.id.button13);

        //datetf.setText(date);
        datetv.setText(date);

        //datetf.setEnabled(false);
    }

    private void initController(){
        lowbt.setOnClickListener(e->{
            currentmode = 1;
            lowbt.getBackground().setTint(getResources().getColor(R.color.loww));
            midbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            highbt.getBackground().setTint(getResources().getColor(R.color.notselected));
        });

        midbt.setOnClickListener(e->{
            currentmode = 2;
            lowbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            midbt.getBackground().setTint(getResources().getColor(R.color.mid));
            highbt.getBackground().setTint(getResources().getColor(R.color.notselected));
        });

        highbt.setOnClickListener(e->{
            currentmode = 3;
            lowbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            midbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            highbt.getBackground().setTint(getResources().getColor(R.color.high));
        });

        createbt.setOnClickListener(e->{
            if(titletf.getText().toString().equals("") || datetv.getText().toString().equals("") || starttf.getText().toString().equals("") ||
                    endtf.getText().toString().equals("") || desctf.getText().toString().equals("")){
                Toast toast = Toast.makeText(getApplicationContext(), R.string.empty_field, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            try{
                SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
                int id = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getInt("login", -1);
                if(id == -1 ){
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.internal_error, Toast.LENGTH_SHORT);
                    toast.show();
                    database.close();
                    return;
                }

                Cursor result = database.rawQuery("SELECT StartT, EndT, Title FROM Activity WHERE userid = " + id + " AND Date = '" + datetv.getText().toString() + "'", null);
                if(result.getCount() > 0){
                    result.moveToFirst();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Date start = sdf.parse(starttf.getText().toString());
                    Date end = sdf.parse(endtf.getText().toString());
                    result.moveToFirst();
                    while(!result.isAfterLast()){
                        Date acstart = sdf.parse(result.getString(0));
                        Date acend = sdf.parse(result.getString(1));
                        String title = result.getString(2);
                        if(start.after(acstart) && end.before(acend)){
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.overlap)+ " " + title, Toast.LENGTH_SHORT);
                            toast.show();
                            database.close();
                            return;
                        }
                        if(start.before(acstart) && !end.before(acstart)){
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.overlap) + " " + title, Toast.LENGTH_SHORT);
                            toast.show();
                            database.close();
                            return;
                        }
                        if(start.after(acstart) && start.before(acend)){
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.overlap) +" " + title, Toast.LENGTH_SHORT);
                            toast.show();
                            database.close();
                            return;
                        }
                        result.moveToNext();
                    }
                }


                //todo: proveri da li ima vec aktivnosti u bazi za to vreme
                //database.execSQL("INSERT INTO Activity (Title, Date, StartT, EndT, Details, priority, userid) VALUES ('" + titletf.getText().toString() + "', '" + datetf.getText().toString() + "', '" + starttf.getText().toString() + "', '" + endtf.getText().toString() + "', '" + desctf.getText().toString() + "', " + currentmode + "," + id + ")");
                ContentValues cv = new ContentValues();
                cv.put("Title", titletf.getText().toString());
                cv.put("Date", datetv.getText().toString());
                cv.put("StartT", starttf.getText().toString());
                cv.put("EndT", endtf.getText().toString());
                cv.put("Details", desctf.getText().toString());
                cv.put("priority", currentmode);
                cv.put("userid", id);
                int actid = (int)database.insert("Activity", null, cv);
                database.close();
                Intent intent = new Intent();
                intent.putExtra("date", datetv.getText().toString());
                intent.putExtra("startT", starttf.getText().toString());
                intent.putExtra("endT", endtf.getText().toString());
                intent.putExtra("title", titletf.getText().toString());
                intent.putExtra("desc", desctf.getText().toString());
                intent.putExtra("priority", currentmode);
                intent.putExtra("id", actid);
                setResult(RESULT_OK, intent);
                finish();
            }
            catch (Exception ee){
                Toast toast = Toast.makeText(getApplicationContext(), ee.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        cancelbt.setOnClickListener(e->{
            finish();
        });
    }
}
package rs.raf.projekat1.sava_ivkovic_rn1220.activities;

import androidx.appcompat.app.AppCompatActivity;

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
import rs.raf.projekat1.sava_ivkovic_rn1220.fragments.CalendarFragment;
import rs.raf.projekat1.sava_ivkovic_rn1220.fragments.DailyPlanFragment;
import rs.raf.projekat1.sava_ivkovic_rn1220.model.Activity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private TextView date;
    private Button lowbt;
    private Button midbt;
    private Button highbt;
    private EditText name;
    private EditText time;
    private EditText description;
    private Button savebt;
    private Button cancelbt;

    private int activityid;
    private int priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle extras = getIntent().getExtras();
        activityid = extras.getInt("activityid");
        init();
    }
    private void init(){
        initView();
        initData();
        initController();
        initActivityButtons();
    }

    private void initActivityButtons() {
        switch(priority){
            case 1:
                lowbt.getBackground().setTint(getResources().getColor(R.color.loww));
                midbt.getBackground().setTint(getResources().getColor(R.color.notselected));
                highbt.getBackground().setTint(getResources().getColor(R.color.notselected));
                break;
            case 2:
                lowbt.getBackground().setTint(getResources().getColor(R.color.notselected));
                midbt.getBackground().setTint(getResources().getColor(R.color.mid));
                highbt.getBackground().setTint(getResources().getColor(R.color.notselected));
                break;
            case 3:
                lowbt.getBackground().setTint(getResources().getColor(R.color.notselected));
                midbt.getBackground().setTint(getResources().getColor(R.color.notselected));
                highbt.getBackground().setTint(getResources().getColor(R.color.high));
                break;
        }
    }

    private void initView(){
        date = findViewById(R.id.textView13);
        lowbt = findViewById(R.id.button16);
        midbt = findViewById(R.id.button17);
        highbt = findViewById(R.id.button18);
        name = findViewById(R.id.editTextTextPersonName4);
        time = findViewById(R.id.editTextTextPersonName5);
        description = findViewById(R.id.editTextTextMultiLine2);
        savebt = findViewById(R.id.button19);
        cancelbt = findViewById(R.id.button20);
    }

    private void initController(){
        savebt.setOnClickListener(e->{
            try{
                SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
                //TODO: fix za vreme
                if(time.getText().toString().equals("") || description.getText().equals("") || name.getText().equals("") || time.getText().toString().split("-").length != 2){
                    Toast toast = Toast.makeText(this.getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                int id = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getInt("login", -1);
                if(id == -1 ){
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.internal_error, Toast.LENGTH_SHORT);
                    toast.show();
                    database.close();
                    return;
                }
                String starts = "", ends = "";

                Cursor result = database.rawQuery("SELECT StartT, EndT, Title FROM Activity WHERE userid = " + id + " AND Date = '" + date.getText().toString() + "'", null);
                if(result.getCount() > 0){
                    result.moveToFirst();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    starts = time.getText().toString().split("-")[0];
                    ends = time.getText().toString().split("-")[1];
                    starts = starts.substring(0, starts.length() - 1);
                    ends = ends.substring(1, ends.length());
                    Date start = sdf.parse(starts);
                    Date end = sdf.parse(ends);
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

                Intent intent = new Intent();
                intent.putExtra("activityid", activityid);
                intent.putExtra("title", name.getText().toString());
                intent.putExtra("priority", priority);
                intent.putExtra("description", description.getText().toString());
                intent.putExtra("starts", starts);
                intent.putExtra("ends", ends);
                setResult(RESULT_OK, intent);

                database.execSQL("UPDATE Activity SET Title = '" + name.getText().toString() + "', priority = " + priority + ", Details = '" + description.getText().toString() + "', StartT = '" + starts + "', EndT = '"+ends+"' WHERE id = " + activityid);
                database.close();
                for(Activity i : DailyPlanFragment.getActivities()){
                    if(i.getId() == activityid){
                        i.setTitle(name.getText().toString());
                        i.setPriority(priority);
                        i.setDesc(description.getText().toString());
                        i.setStartT(starts);
                        i.setEndT(ends);
                        DailyPlanFragment.update();
                        break;
                    }
                }
                CalendarFragment.check(date.getText().toString());
                finish();
            }
            catch (Exception ex){
                Toast toast = Toast.makeText(this.getApplicationContext(), R.string.internal_error, Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });
        cancelbt.setOnClickListener(e->{
            finish();
        });

        lowbt.setOnClickListener(e->{
            priority = 1;
            lowbt.getBackground().setTint(getResources().getColor(R.color.loww));
            midbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            highbt.getBackground().setTint(getResources().getColor(R.color.notselected));
        });

        midbt.setOnClickListener(e->{
            priority = 2;
            lowbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            midbt.getBackground().setTint(getResources().getColor(R.color.mid));
            highbt.getBackground().setTint(getResources().getColor(R.color.notselected));
        });

        highbt.setOnClickListener(e->{
            priority = 3;
            lowbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            midbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            highbt.getBackground().setTint(getResources().getColor(R.color.high));
        });
    }

    private void initData(){
        try{
            SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM Activity WHERE id = " + activityid, null);
            cursor.moveToFirst();
            date.setText(cursor.getString(2));
            name.setText(cursor.getString(1));
            time.setText(cursor.getString(3) + " - "+ cursor.getString(4));
            description.setText(cursor.getString(5));
            priority = cursor.getInt(6);
            cursor.close();
            database.close();
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this.getApplicationContext(), R.string.internal_error, Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }
}
package rs.raf.projekat1.sava_ivkovic_rn1220.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText password;
    private EditText confpass;
    private Button confirmbt;
    private Button cancelbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        initcontroller();
    }
    private void init(){
        password = findViewById(R.id.editTextTextPassword2);
        confpass = findViewById(R.id.editTextTextPassword3);
        confirmbt = findViewById(R.id.button14);
        cancelbt = findViewById(R.id.button15);
    }

    private void initcontroller(){
        cancelbt.setOnClickListener(e->{
            finish();
        });
        confirmbt.setOnClickListener(e->{
            if(!password.getText().toString().equals(confpass.getText().toString())){
                Toast toast = Toast.makeText(getApplicationContext(), R.string.pass_not_match, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            String pass = password.getText().toString();
            if(pass.length()<5 || pass.contains("~") || pass.contains("#") || pass.contains("$") || pass.contains("%") || pass.contains("^") || pass.contains("&") || pass.contains("*")){
                Toast toast = Toast.makeText(getApplicationContext(), R.string.wrong_pass_format, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            try{
                SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                int id = sharedPreferences.getInt("login", -1);
                Cursor cursor = database.rawQuery("SELECT Password FROM Login WHERE id = " + id, null);
                cursor.moveToFirst();
                String oldpass = cursor.getString(0);
                if(oldpass.equals(pass)){
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.same_pass, Toast.LENGTH_SHORT);
                    toast.show();
                    database.close();
                    return;
                }
                database.execSQL("UPDATE Login SET Password = '" + pass + "' WHERE id = " + id + ";");
                Toast toast = Toast.makeText(getApplicationContext(), R.string.pass_changed, Toast.LENGTH_SHORT);
                toast.show();
                database.close();
                finish();
            }
            catch (Exception ee){
                ee.printStackTrace();
            }
        });
    }

}
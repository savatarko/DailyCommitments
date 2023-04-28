package rs.raf.projekat1.sava_ivkovic_rn1220.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;

public class LoginActivity extends AppCompatActivity {

    private Button button;
    private EditText email;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    private void init(){
        initView();
        initListeners();
    }

    private void initView(){
        button = findViewById(R.id.button);
        email = findViewById(R.id.editTextTextEmailAddress);
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
    }

    private void initListeners(){
        button.setOnClickListener(e->{
            if(email.getText().toString().equals("") || username.getText().toString().equals("") || password.getText().toString().equals("")){
                Toast toast = Toast.makeText(getApplicationContext(), R.string.empty_field, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            String pass = password.getText().toString();
            if(pass.length()<5 || pass.contains("~") || pass.contains("#") || pass.contains("$") || pass.contains("%") || pass.contains("^") || pass.contains("&") || pass.contains("*")){
                Toast toast = Toast.makeText(getApplicationContext(), R.string.wrong_pass_format, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            int flag = 0;
            for(int i = 0; i < pass.length(); i++){
                if(pass.charAt(i) <='9' && pass.charAt(i) >= '0'){
                    flag |= 1;
                }
                if(pass.charAt(i) <='Z' && pass.charAt(i) >= 'A'){
                    flag |= 2;
                }
            }
            if(flag != 3){
                Toast toast = Toast.makeText(getApplicationContext(), R.string.wrong_pass_format, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if(checkDatabase()){
                Intent intent = new Intent(this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkDatabase(){
        try{
            SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
            Cursor result = database.rawQuery("SELECT * FROM Login WHERE Username = '" + username.getText().toString() +
                    "' AND Email = '" + email.getText().toString() + "' AND password = '" + password.getText().toString() + "';", null);
            if(result.getCount() == 0){
                Cursor result1 = database.rawQuery("SELECT * FROM Login WHERE Username = '" + username.getText().toString() +
                        "' AND Email = '" + email.getText().toString() + "';", null);
                Toast toast;
                if(result1.getCount() == 0){
                    toast = Toast.makeText(getApplicationContext(), R.string.account_not_found, Toast.LENGTH_SHORT);
                }
                else {
                    toast = Toast.makeText(getApplicationContext(), R.string.wrong_pass, Toast.LENGTH_SHORT);
                }
                toast.show();
                database.close();
                return false;
            }
            else{
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                /*
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //editor.putInt("login", result.getInt(0));
                editor.putInt("login", result.getInt(1));

                 */
                result.moveToFirst();
                sharedPreferences
                        .edit()
                        .putInt("login", result.getInt(0))
                        .apply();
                database.close();
                return true;
            }
        }
        catch (Exception e){
            Log.d("database_err", e.toString());
            Toast toast = Toast.makeText(getApplicationContext(), "Server error, please try again.", Toast.LENGTH_SHORT);
            return false;
        }
    }

}
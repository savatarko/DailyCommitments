package rs.raf.projekat1.sava_ivkovic_rn1220.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(!checkLogin())
            loadDatabase();
        else{
            Intent intent = new Intent(this, MainScreenActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private boolean checkLogin(){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("login", -1);
        if(id!=-1){
            return true;
        }
        return false;
    }

    private void loadDatabase() {
        try {
            SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);

            //database.execSQL("DROP TABLE ACTIVITY;");
            database.execSQL("CREATE TABLE IF NOT EXISTS Login(id integer primary key autoincrement, Email VARCHAR, Username VARCHAR, Password VARCHAR);");
            database.execSQL("CREATE TABLE IF NOT EXISTS Activity(id integer primary key autoincrement,Title text, Date text, StartT text, EndT text,Details text, priority integer, userid integer, FOREIGN KEY (userid) REFERENCES Login(id));");

            //database.execSQL("DELETE FROM Login;");

            //database.execSQL("DROP TABLE ACTIVITY;");

            Cursor result = database.rawQuery("SELECT * FROM Login WHERE Username = 'test';", null);
            if (result.getCount() == 0) {
                database.execSQL("INSERT INTO Login(Email, Username, Password) VALUES('test123@gmail.com', 'test', 'Test123');");
            }
            /*
            else{
                database.execSQL("UPDATE Login SET EMAIL = 'test123@gmail.com', Password = 'Test123' WHERE Username = 'test';");
            }

             */
            result = database.rawQuery("SELECT * FROM Login WHERE Username = 'test';", null);
            //Log.d("testt1", Integer.toString(result.getCount()));
            database.close();
            Intent intent = new Intent(this, LoginActivity.class);
            //wait(1);
            startActivity(intent);
        } catch (Exception e) {
            Log.d("database_err", e.toString());
        }
    }
}
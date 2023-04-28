package rs.raf.projekat1.sava_ivkovic_rn1220.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;
import rs.raf.projekat1.sava_ivkovic_rn1220.fragments.CalendarFragment;
import rs.raf.projekat1.sava_ivkovic_rn1220.fragments.DailyPlanFragment;
import rs.raf.projekat1.sava_ivkovic_rn1220.model.Activity;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityDetails extends AppCompatActivity {

    private TextView dayofactivity;
    private TextView timeofactivity;
    private TextView titleofactivity;
    private TextInputEditText descriptionofactivity;
    private Button editbt;
    private Button deletebt;
    private int activityid;
    private Button snackbarbt;

    OnSwipeTouchListener onSwipeTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.linearLayout21), this);
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            Toast toast = Toast.makeText(this.getApplicationContext(), R.string.internal_error, Toast.LENGTH_SHORT);
            toast.show();
            finish();
            return;
        }
        activityid = extras.getInt("activityid");
        init();
    }
    private void init(){
        initView();
        initController();
        initData();
    }
    private void initView(){
        dayofactivity = findViewById(R.id.textView5);
        timeofactivity = findViewById(R.id.textView7);
        titleofactivity = findViewById(R.id.textView8);
        descriptionofactivity = findViewById(R.id.detailstiet);
        editbt = findViewById(R.id.button7);
        deletebt = findViewById(R.id.button8);
        //snackbarbt = findViewById(R.id.button14);
    }
    private void initController(){
        /*
        editbt.setOnClickListener(e->{
            try{
                SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
                database.execSQL("UPDATE Activity SET Details = '" + descriptionofactivity.getText().toString() + "' WHERE id = " + activityid);
                database.close();
                finish();
            }
            catch (Exception ee){
                ee.printStackTrace();
            }
        });

         */
        editbt.setOnClickListener(e->{
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("activityid", activityid);
            startActivityForResult(intent, 1);
        });

        deletebt.setOnClickListener(e->{
            try{
                boolean finishflag = false;
                int deletedid = activityid;
                if(DailyPlanFragment.getActivities().size() == 1){
                    finishflag = true;
                }
                else{
                    move(-1);
                }
                SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
                database.execSQL("DELETE FROM Activity WHERE id = " + deletedid);
                database.close();
                for(Activity a : DailyPlanFragment.getActivities()){
                    if(a.getId() == deletedid){
                        DailyPlanFragment.getActivities().remove(a);
                        DailyPlanFragment.update();
                        break;
                    }
                }
                CalendarFragment.check(dayofactivity.getText().toString());
                if(finishflag){
                    finish();
                }
            }
            catch (Exception ee){
                ee.printStackTrace();
            }
            /*
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "This is a Snackbar", Snackbar.LENGTH_INDEFINITE)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Undo successful", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    })
                    .setActionTextColor(Color.RED);

            View snackView = snackbar.getView();
            TextView textView = snackView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();

             */
        });
        /*
        snackbarbt.setOnClickListener(e->{
            try{
                SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
                database.execSQL("DELETE FROM Activity WHERE id = " + activityid);
                database.close();
                finish();
            }
            catch (Exception ee){
                ee.printStackTrace();
            }
        });

         */
    }
    private void initData(){
        try{
            SQLiteDatabase database = openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM Activity WHERE id = " + activityid, null);
            cursor.moveToFirst();
            dayofactivity.setText(cursor.getString(2));
            timeofactivity.setText(cursor.getString(1));
            titleofactivity.setText(cursor.getString(3) + " - " + cursor.getString(4));
            descriptionofactivity.setText(cursor.getString(5));
            database.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            titleofactivity.setText(data.getStringExtra("title"));
            descriptionofactivity.setText(data.getStringExtra("description"));
            timeofactivity.setText(data.getStringExtra("starts") + " - " + data.getStringExtra("ends"));
        }
    }

    public static class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        private ActivityDetails activityDetails;
        Context context;
        OnSwipeTouchListener(Context ctx, View mainView, ActivityDetails activityDetails) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
            mainView.setOnTouchListener(this);
            context = ctx;
            this.activityDetails = activityDetails;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
        void onSwipeRight() {
            //Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
            activityDetails.move(-1);
            //this.onSwipe.swipeRight();
        }
        void onSwipeLeft() {
            //Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
            activityDetails.move(1);
            //this.onSwipe.swipeLeft();
        }
        void onSwipeTop() {
            //Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show();
            //this.onSwipe.swipeTop();
        }
        void onSwipeBottom() {
            //Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show();
            //this.onSwipe.swipeBottom();
        }
        interface onSwipeListener {
            void swipeRight();
            void swipeTop();
            void swipeBottom();
            void swipeLeft();
        }
        onSwipeListener onSwipe;
    }
    public void move(int step){
        Activity newa = null;
        for(int i = 0;i<DailyPlanFragment.getActivities().size();i++){
            if(DailyPlanFragment.getActivities().get(i).getId() == activityid){
                i +=step;
                if(i<0){
                    i = DailyPlanFragment.getActivities().size()-1;
                }
                else if(i>DailyPlanFragment.getActivities().size()-1){
                    i = 0;
                }
                newa = DailyPlanFragment.getActivities().get(i);
                break;
            }
        }
        if(newa == null){
            return;
        }
        dayofactivity.setText(newa.getDate());
        timeofactivity.setText(newa.getStartT() + " - " + newa.getEndT());
        titleofactivity.setText(newa.getTitle());
        descriptionofactivity.setText(newa.getDesc());
        activityid = newa.getId();
    }
}
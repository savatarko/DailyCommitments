package rs.raf.projekat1.sava_ivkovic_rn1220.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;
import rs.raf.projekat1.sava_ivkovic_rn1220.fragments.DailyPlanFragment;

public class SelectedDayActivity extends AppCompatActivity {

    private Button backbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_day);
        Bundle bundle = new Bundle();
        bundle.putString("date", getIntent().getStringExtra("date"));
        DailyPlanFragment dailyPlanFragment = new DailyPlanFragment();
        dailyPlanFragment.setArguments(bundle);

        backbt = findViewById(R.id.backbt);
        backbt.setOnClickListener(e->{
            finish();
        });
    }
}
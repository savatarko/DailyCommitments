package rs.raf.projekat1.sava_ivkovic_rn1220.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;
import rs.raf.projekat1.sava_ivkovic_rn1220.fragments.CalendarFragment;
import rs.raf.projekat1.sava_ivkovic_rn1220.fragments.DailyPlanFragment;
import rs.raf.projekat1.sava_ivkovic_rn1220.fragments.ProfileFragment;
import com.google.android.material.tabs.TabLayout;

public class MainScreenActivity extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;
    private final String calendarfrag = "calendarfrag";
    private final String dailyplanfrag = "dailyplanfrag";
    private final String profilefrag = "profilefrag";

    private TabLayout tabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        init();
    }

    private void init(){
        initFragment();
        initElements();
        initControllers();
    }

    private void initFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainerView5, new CalendarFragment());
        transaction.commit();
    }
    private void initElements(){
        tabView = findViewById(R.id.tabLayout);
        fragmentContainerView = findViewById(R.id.fragmentContainerView5);
    }

    private void initControllers(){
        tabView.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String text = tab.getText().toString();
                //tab.get
                if(text.equalsIgnoreCase(getString(R.string.calendar))){
                    CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentByTag(calendarfrag);
                    if(calendarFragment==null || !calendarFragment.isVisible()){
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerView5, new CalendarFragment(), calendarfrag);
                        transaction.commit();
                    }
                }
                else if(text.equalsIgnoreCase(getString(R.string.daily_plan))){
                    DailyPlanFragment dailyPlanFragment = (DailyPlanFragment) getSupportFragmentManager().findFragmentByTag(dailyplanfrag);
                    if(dailyPlanFragment==null || !dailyPlanFragment.isVisible()){
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerView5, new DailyPlanFragment(), dailyplanfrag);
                        transaction.commit();
                    }
                }
                else if(text.equalsIgnoreCase(getString(R.string.profile))){
                    ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(profilefrag);
                    if(profileFragment==null || !profileFragment.isVisible()){
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerView5, new ProfileFragment(), profilefrag);
                        transaction.commit();
                    }
                }
                /*
                switch(text){
                    case "Calendar":
                        CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentByTag(calendarfrag);
                        if(calendarFragment==null || !calendarFragment.isVisible()){
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainerView5, new CalendarFragment(), calendarfrag);
                            transaction.commit();
                        }
                        break;
                    case "Daily plan":
                        DailyPlanFragment dailyPlanFragment = (DailyPlanFragment) getSupportFragmentManager().findFragmentByTag(dailyplanfrag);
                        if(dailyPlanFragment==null || !dailyPlanFragment.isVisible()){
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainerView5, new DailyPlanFragment(), dailyplanfrag);
                            transaction.commit();
                        }
                        break;
                    case "Profile":
                        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(profilefrag);
                        if(profileFragment==null || !profileFragment.isVisible()){
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainerView5, new ProfileFragment(), profilefrag);
                            transaction.commit();
                        }
                        break;
                }

                 */
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
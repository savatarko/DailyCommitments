package rs.raf.projekat1.sava_ivkovic_rn1220.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;
import rs.raf.projekat1.sava_ivkovic_rn1220.activities.NewActActivity;
import rs.raf.projekat1.sava_ivkovic_rn1220.model.Activity;
import rs.raf.projekat1.sava_ivkovic_rn1220.adapter.ActivityItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyPlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyPlanFragment extends Fragment implements ActivityItemAdapter.EventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentActivity activity;

    private static RecyclerView recyclerView;
    private Button lowbt;
    private Button midbt;
    private Button highbt;
    int flags = 0;

    private static final MutableLiveData<List<Activity>> activityList = new MutableLiveData<>();

    final Observer<List<Activity>> activityObserver = new Observer<List<Activity>>() {
        @Override
        public void onChanged(List<Activity> activities) {
            activityList.setValue(activities);
            recyclerView.getAdapter().notifyDataSetChanged();

            //recyclerView.setAdapter(new ActivityItemAdapter(activity.getApplicationContext(), activityList.getValue()));
        }
    };
    private static List<Activity> activities = new ArrayList<>();


    private static View view;

    public DailyPlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyPlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyPlanFragment newInstance(String param1, String param2) {
        DailyPlanFragment fragment = new DailyPlanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_plan, container, false);
    }

    private FloatingActionButton fab;
    //private ActivityItemAdapter activityItemAdapter;
    private TextView doa;
    private TextView activityfilter;
    private CheckBox checkBox;
    private boolean showpast = true;
    private String date = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        activity = this.getActivity();
        activities.clear();
        lowbt = view.findViewById(R.id.button2);
        midbt = view.findViewById(R.id.button3);
        highbt = view.findViewById(R.id.button4);
        doa = view.findViewById(R.id.textView6);
        activityfilter = view.findViewById(R.id.editTextTextPersonName2);
        checkBox = view.findViewById(R.id.checkBox);
        //activityItemAdapter = new ActivityItemAdapter(this.getActivity().getApplicationContext(), activityList.getValue());
        super.onViewCreated(view, savedInstanceState);
        Bundle extras = null;
        if(getActivity().getIntent()!=null)
            extras = getActivity().getIntent().getExtras();
        int id = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), MODE_PRIVATE).getInt("login", -1);
        if(extras!=null){
            date = extras.getString("date");
        }
        else {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date = dateFormat.format(Calendar.getInstance().getTime());
        }
        loadDb(view, id);
        init(view, id);
        this.view = view;

        lowbt.setOnClickListener(e->{
            if((flags & 1) == 1)
                lowbt.getBackground().setTint(getResources().getColor(R.color.loww));
            else
                lowbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            flags ^=1;
            loadDb(view, id);
            recyclerView.getAdapter().notifyDataSetChanged();
        });
        midbt.setOnClickListener(e->{
            if((flags & 2) == 2)
                midbt.getBackground().setTint(getResources().getColor(R.color.mid));
            else
                midbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            flags ^=2;
            loadDb(view , id);
            recyclerView.getAdapter().notifyDataSetChanged();
        });
        highbt.setOnClickListener(e->{
            if((flags & 4) == 4)
                highbt.getBackground().setTint(getResources().getColor(R.color.high));
            else
                highbt.getBackground().setTint(getResources().getColor(R.color.notselected));
            flags ^=4;
            loadDb(view, id);
            recyclerView.getAdapter().notifyDataSetChanged();
        });

        activityfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadDb(view, id);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        checkBox.setOnClickListener(e->{
            showpast = !showpast;
            loadDb(view, id);
            recyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void loadDb(View view, int id){
        try{
            SQLiteDatabase database = view.getContext().openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
            if(date == null){
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                date = dateFormat.format(Calendar.getInstance().getTime());
            }
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            doa.setText(date);
            activities.clear();
            Cursor result = database.rawQuery("SELECT Title, Date, StartT, EndT, Details, priority, id FROM Activity WHERE userid = " + id + " AND Date = '" + date + "';", null);
            result.moveToFirst();
            while(!result.isAfterLast()){
                Activity activity = new Activity();
                activity.setTitle(result.getString(0));
                activity.setDate(result.getString(1));
                activity.setStartT(result.getString(2));
                activity.setEndT(result.getString(3));
                activity.setDesc(result.getString(4));
                activity.setPriority(result.getInt(5));
                activity.setId(result.getInt(6));
                if((flags & 1) == 1 && activity.getPriority() == 1) {
                    result.moveToNext();
                    continue;
                }
                if((flags & 2) == 2 && activity.getPriority() == 2) {
                    result.moveToNext();
                    continue;
                }
                if((flags & 4) == 4 && activity.getPriority() == 3) {
                    result.moveToNext();
                    continue;
                }
                if(!activityfilter.getText().toString().equals("") && !activity.getTitle().contains(activityfilter.getText().toString())){
                    result.moveToNext();
                    continue;
                }
                String tmp = activity.getDate() + " " + activity.getStartT();
                LocalDateTime activitydate = LocalDateTime.parse(tmp, format);
                if(activitydate.isBefore(LocalDateTime.now()) && !showpast){
                    result.moveToNext();
                    continue;
                }
                activities.add(activity);
                result.moveToNext();
            }

            database.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init(View view, int id){
        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(e->{
            Intent intent = new Intent(this.getActivity(), NewActActivity.class);
            String data = date;
            if(data == null){
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                data = dateFormat.format(Calendar.getInstance().getTime());
            }
            intent.putExtra("date", data);
            //startActivity(intent);
            startActivityForResult(intent, 1);
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        Collections.sort(activities);
        activityList.setValue(activities);

        recyclerView.setAdapter(new ActivityItemAdapter(this.getActivity().getApplicationContext(), activityList.getValue(), this));
        //recyclerView.getAdapter().notifyDataSetChanged();
    }
    /*

    public static void removeActivity(int id){
        Activity activity = null;
        for(Activity i : activities){
            if(i.getId() == id){
                activity = i;
                break;
            }
        }
        if(activity == null){
            return;
        }
        //TODO: izbrisi iz baze
        try{
            SQLiteDatabase database = view.getContext().openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
            database.execSQL("DELETE FROM Activity WHERE id = " + activity.getId() + ";");
            database.close();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        activities.remove(activity);
        activityList.setValue(activities);
    }

     */

    @Override
    public void onDelete(int id) {
        Activity activity = null;
        for(Activity i : activities){
            if(i.getId() == id){
                activity = i;
                break;
            }
        }
        if(activity == null){
            return;
        }
        //TODO: izbrisi iz baze
        try{
            SQLiteDatabase database = view.getContext().openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
            database.execSQL("DELETE FROM Activity WHERE id = " + activity.getId() + ";");
            database.close();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        activities.remove(activity);
        activityList.setValue(activities);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK){
                Activity activity = new Activity();
                activity.setTitle(data.getStringExtra("title"));
                activity.setDate(data.getStringExtra("date"));
                activity.setStartT(data.getStringExtra("startT"));
                activity.setEndT(data.getStringExtra("endT"));
                activity.setDesc(data.getStringExtra("desc"));
                activity.setPriority(data.getIntExtra("priority", 0));
                activity.setId(data.getIntExtra("id", 0));
                activities.add(activity);
                Collections.sort(activities);
                activityList.setValue(activities);
                recyclerView.getAdapter().notifyDataSetChanged();
                CalendarFragment.check(data.getStringExtra("date"));
            }
    }

    public static List<Activity> getActivities() {
        return activities;
    }

    public static void setActivities(List<Activity> activities) {
        DailyPlanFragment.activities = activities;
    }

    public static void update(){
        Collections.sort(activities);
        activityList.setValue(activities);
        recyclerView.getAdapter().notifyDataSetChanged();
        CalendarFragment.update();
    }
}
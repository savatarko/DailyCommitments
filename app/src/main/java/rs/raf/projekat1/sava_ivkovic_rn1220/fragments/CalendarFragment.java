package rs.raf.projekat1.sava_ivkovic_rn1220.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;

import rs.raf.projekat1.sava_ivkovic_rn1220.model.ActivityDate;
import rs.raf.projekat1.sava_ivkovic_rn1220.adapter.CalendarItemAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    private static RecyclerView recyclerView;
    //private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager layoutManager;
    private CalendarItemAdapter calendarItemAdapter;
    private static final MutableLiveData<List<ActivityDate>> calendarList = new MutableLiveData<>();

    private static List<ActivityDate> activityDates = new ArrayList<>();

    private TextView datetv;
    //private TextView datetv;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        datetv = view.findViewById(R.id.textView12);
        recyclerView = view.findViewById(R.id.recyclerView3);
        layoutManager = new GridLayoutManager(view.getContext(), 7);
        recyclerView.setLayoutManager(layoutManager);
        //calendarList.setValue(new ArrayList<>());
        initCalendar(view);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*
                int position = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                datetv.setText(calendarList.getValue().get(position).getDate());

                 */
                layoutManager.findFirstCompletelyVisibleItemPosition();
                datetv.setText(calendarList.getValue().get(layoutManager.findFirstCompletelyVisibleItemPosition()).getMonth() + " " + calendarList.getValue().get(layoutManager.findFirstVisibleItemPosition()).getYear());
            }
        });
    }
    private void initCalendar(View view){
        Map<String, Integer> priomap = new HashMap<>();
        try{
            int id = this.getActivity().getSharedPreferences(this.getActivity().getPackageName(), MODE_PRIVATE).getInt("login", -1);
            SQLiteDatabase database = view.getContext().openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM Activity WHERE userid = " + id + ";", null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                if(!priomap.containsKey(cursor.getString(2)) || (priomap.get(cursor.getString(2)) < cursor.getInt(6)))
                    priomap.put(cursor.getString(2), cursor.getInt(6));
                cursor.moveToNext();
            }
            database.close();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        LocalDate date = LocalDate.parse("2022-08-01");
        activityDates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int index = 0;
        for(int i = 0;i<1000;i++){
            ActivityDate activityDate = new ActivityDate();
            activityDate.setDay(Integer.toString(date.getDayOfMonth()));
            activityDate.setMonth(date.getMonth().toString());
            activityDate.setYear(Integer.toString(date.getYear()));
            activityDate.setPriority(0);
            activityDates.add(activityDate);
            activityDate.setPriority(priomap.getOrDefault(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 0));
            date = date.plusDays(1);
            if(today.getDayOfMonth() == date.getDayOfMonth() && today.getMonth() == date.getMonth() && today.getYear() == date.getYear())
                index = i - date.getDayOfMonth();
        }
        layoutManager.scrollToPosition(index);
        calendarList.setValue(activityDates);
        calendarItemAdapter = new CalendarItemAdapter(calendarList.getValue(), this.getActivity().getApplicationContext());
        recyclerView.setAdapter(calendarItemAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.refreshDrawableState();
    }

    public static List<ActivityDate> getActivityDates() {
        return activityDates;
    }

    public static void setActivityDates(List<ActivityDate> activityDates) {
        CalendarFragment.activityDates = activityDates;
    }

    public static void update(){
        calendarList.setValue(activityDates);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public static void check(String date){
        Cursor cursor = null;
        SQLiteDatabase database;
        try{
            database = recyclerView.getContext().openOrCreateDatabase("rafdnevnjak", MODE_PRIVATE, null);
            cursor = database.rawQuery("SELECT Date, priority FROM Activity;", null);
            cursor.moveToFirst();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date,formatter);
        int index = (int)ChronoUnit.DAYS.between(LocalDate.parse("2022-08-01"),localDate);
        int max = 0;
        while(!cursor.isAfterLast()){
            if(date.equals(cursor.getString(0)) &&  cursor.getInt(1) > max) {
                max = cursor.getInt(1);
                if(max == 3)
                    break;
            }
            cursor.moveToNext();
        }
        activityDates.get(index).setPriority(max);
        update();
        database.close();
    }
}
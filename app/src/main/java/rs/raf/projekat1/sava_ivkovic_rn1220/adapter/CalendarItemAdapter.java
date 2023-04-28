package rs.raf.projekat1.sava_ivkovic_rn1220.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;
import rs.raf.projekat1.sava_ivkovic_rn1220.activities.SelectedDayActivity;
import rs.raf.projekat1.sava_ivkovic_rn1220.model.ActivityDate;
import rs.raf.projekat1.sava_ivkovic_rn1220.viewmodel.CalendarViewHolder;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.List;
import java.util.Locale;

public class CalendarItemAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private List<ActivityDate> activityDates;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private Context context;

    public CalendarItemAdapter(List<ActivityDate> activityDates, Context context) {
        this.activityDates = activityDates;
        this.context = context;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_item, parent, false);
        //return new RecyclerView.ViewHolder(view);
        return null;

         */
        return new CalendarViewHolder(LayoutInflater.from(context).inflate(R.layout.calendar_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        ActivityDate date = activityDates.get(position);
        //holder.calendarDate.setText(dateFormatter.format(date));
        holder.getCalendarDate().setText(date.getDay());

        ActivityDate d = activityDates.get(position);
        switch(d.getPriority()){
            case 0:
                holder.getLayout().setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.getCalendarDate().setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                break;
            case 1:
                holder.getLayout().setBackgroundColor(ContextCompat.getColor(context, R.color.loww));
                holder.getCalendarDate().setBackgroundColor(ContextCompat.getColor(context, R.color.loww));
                break;
            case 2:
                holder.getLayout().setBackgroundColor(ContextCompat.getColor(context, R.color.mid));
                holder.getCalendarDate().setBackgroundColor(ContextCompat.getColor(context, R.color.mid));
                break;
            case 3:
                holder.getLayout().setBackgroundColor(ContextCompat.getColor(context, R.color.high));
                holder.getCalendarDate().setBackgroundColor(ContextCompat.getColor(context, R.color.high));
                break;
        }
        holder.getLayout().setOnClickListener(e->{
            Intent intent = new Intent(context, SelectedDayActivity.class);
            String mon = Integer.toString(Month.valueOf(date.getMonth()).getValue());
            if(mon.length() == 1){
                mon = "0" + mon;
            }
            String day = date.getDay();
            if(day.length() == 1){
                day = "0" + day;
            }
            String selected = day + "-" + mon + "-" + date.getYear();
            intent.putExtra("date", selected);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        if(activityDates == null)
            return 0;
        return activityDates.size();
    }
}

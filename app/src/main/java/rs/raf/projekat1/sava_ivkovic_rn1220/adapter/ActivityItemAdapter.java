package rs.raf.projekat1.sava_ivkovic_rn1220.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;
import rs.raf.projekat1.sava_ivkovic_rn1220.activities.ActivityDetails;
import rs.raf.projekat1.sava_ivkovic_rn1220.activities.EditActivity;
import rs.raf.projekat1.sava_ivkovic_rn1220.fragments.CalendarFragment;
import rs.raf.projekat1.sava_ivkovic_rn1220.model.Activity;
import rs.raf.projekat1.sava_ivkovic_rn1220.viewmodel.ActivityViewHolder;

import java.util.List;

public class ActivityItemAdapter extends RecyclerView.Adapter<ActivityViewHolder> {

    Context context;
    List<Activity> activities;


    public ActivityItemAdapter(Context context, List<Activity> activities, EventListener eventListener) {
        this.context = context;
        this.activities = activities;
        this.listener = eventListener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        holder.getTimetv().setText(activities.get(position).getStartT() + " - " + activities.get(position).getEndT());
        holder.getActivitytv().setText(activities.get(position).getTitle());
        holder.getDeletebt().setImageResource(R.drawable.delete);
        holder.getEditbt().setImageResource(R.drawable.pen);
        //holder.getActivityImage().setImageResource(R.drawable.activity);
        switch(activities.get(position).getPriority()){
            case 1:
                holder.getActivityImage().setImageResource(R.drawable.activity_low);
                break;
            case 2:
                holder.getActivityImage().setImageResource(R.drawable.activity_mid);
                break;
            case 3:
                holder.getActivityImage().setImageResource(R.drawable.activity_high);
                break;
        }
        if(activities.get(position).isPast()){
            //holder.getLayout().setBackgroundColor(ContextCompat.getColor(context, R.color.high));
            holder.getLayout().setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        }
        else{
            holder.getLayout().setBackgroundColor(ContextCompat.getColor(context, R.color.background));
        }
        holder.getEditbt().setOnClickListener(e->{
            Intent intent = new Intent(this.context, EditActivity.class);
            intent.putExtra("activityid", activities.get(position).getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.getDeletebt().setOnClickListener(e->{
            //DailyPlanFragment.removeActivity(activities.get(position).getId());
            String date = activities.get(position).getDate();
            listener.onDelete(activities.get(position).getId());
            CalendarFragment.check(date);
        });

        holder.getLayout().setOnClickListener(e->{
            Intent intent = new Intent(this.context, ActivityDetails.class);
            intent.putExtra("activityid", activities.get(position).getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    EventListener listener;
    public interface EventListener{
        void onDelete(int id);
    }




}

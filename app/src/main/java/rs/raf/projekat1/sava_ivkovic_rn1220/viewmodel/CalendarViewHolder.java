package rs.raf.projekat1.sava_ivkovic_rn1220.viewmodel;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder{
    private TextView calendarDate;
    private LinearLayout layout;

    public CalendarViewHolder(@NonNull View itemView) {
        super(itemView);
        calendarDate = itemView.findViewById(R.id.textView2);
        layout = itemView.findViewById(R.id.linearLayoutCal);
    }

    public TextView getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(TextView calendarDate) {
        this.calendarDate = calendarDate;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }
}

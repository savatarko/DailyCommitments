package rs.raf.projekat1.sava_ivkovic_rn1220.viewmodel;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import rs.raf.projekat1.sava_ivkovic_rn1220.R;

public class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ImageView activityImage;
    private TextView timetv;
    private TextView activitytv;
    private ImageButton editbt;
    private ImageButton deletebt;
    private LinearLayout layout;

    public ActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        activityImage = itemView.findViewById(R.id.imageView2);
        timetv = itemView.findViewById(R.id.textView9);
        activitytv = itemView.findViewById(R.id.textView10);
        editbt = itemView.findViewById(R.id.imageButton2);
        deletebt = itemView.findViewById(R.id.imageButton3);
        layout = itemView.findViewById(R.id.linearLayoutItem);
    }

    public ImageView getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(ImageView activityImage) {
        this.activityImage = activityImage;
    }

    public TextView getTimetv() {
        return timetv;
    }

    public void setTimetv(TextView timetv) {
        this.timetv = timetv;
    }

    public TextView getActivitytv() {
        return activitytv;
    }

    public void setActivitytv(TextView activitytv) {
        this.activitytv = activitytv;
    }

    public ImageButton getEditbt() {
        return editbt;
    }

    public void setEditbt(ImageButton editbt) {
        this.editbt = editbt;
    }

    public ImageButton getDeletebt() {
        return deletebt;
    }

    public void setDeletebt(ImageButton deletebt) {
        this.deletebt = deletebt;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    @Override
    public void onClick(View view) {

    }
}

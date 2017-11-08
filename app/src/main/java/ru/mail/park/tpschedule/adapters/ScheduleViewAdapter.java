package ru.mail.park.tpschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.mail.park.tpschedule.R;
import ru.mail.park.tpschedule.database.TimetableModel;

/**
 * Created by yaches on 09.11.17
 */

public class ScheduleViewAdapter extends RecyclerView.Adapter<ScheduleViewAdapter.ViewHolder> {
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleSource;

        ViewHolder(View itemView) {
            super(itemView);
            titleSource = itemView.findViewById(R.id.title);

            titleSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "SOME TEXT", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private List<TimetableModel> schedule;

    public ScheduleViewAdapter(List<TimetableModel> schedule) {
        this.schedule = schedule;
    }

    public void setData(List<TimetableModel> newSchedule) {
        this.schedule = newSchedule;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleSource.setText(schedule.get(position).getTitle() + " " + schedule.get(position).getScheduleDate());
    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }
}

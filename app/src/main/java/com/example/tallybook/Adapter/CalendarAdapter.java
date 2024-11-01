package com.example.tallybook.Adapter;

import java.util.List;

import org.w3c.dom.Text;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.example.tallybook.R;

import com.example.tallybook.Entity.CalendarDay;

public class CalendarAdapter extends RecyclerView.Adapter<ViewHolder>{
    Context mContext;
    List<CalendarDay> mDays;
    public CalendarAdapter(Context context, List<CalendarDay> days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_day, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CalendarViewHolder calendarHolder = (CalendarViewHolder) holder;
        CalendarDay day = mDays.get(position);
        calendarHolder.tvDay.setText(day.getDay());
        calendarHolder.tvAmount.setText(day.getAmount());
        if(day.getDay().equals("null")) {
            calendarHolder.tvDay.setVisibility(View.INVISIBLE);
            calendarHolder.tvAmount.setVisibility(View.INVISIBLE);
        } 
        if(day.getAmount().equals("0.0")) {
            calendarHolder.tvAmount.setVisibility(View.INVISIBLE);
        }
    }

    public class CalendarViewHolder extends ViewHolder {
        public TextView tvDay;
        public TextView tvAmount;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.calendar_day_day);
            tvAmount = itemView.findViewById(R.id.calendar_day_amount);
        }
        
    }
}

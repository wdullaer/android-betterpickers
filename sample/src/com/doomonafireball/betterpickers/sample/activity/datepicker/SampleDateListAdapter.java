package com.doomonafireball.betterpickers.sample.activity.datepicker;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.sample.activity.BaseSampleActivity;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleDateListAdapter extends BaseSampleActivity {
    static int[] months = {Calendar.JANUARY,Calendar.FEBRUARY,Calendar.MARCH,Calendar.APRIL,
            Calendar.MAY,Calendar.JUNE,Calendar.JULY,Calendar.AUGUST,Calendar.SEPTEMBER,
            Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView list = (ListView) findViewById(R.id.list);

        list.setAdapter(new SampleAdapter(this, getFragmentManager()));
    }

    private class SampleAdapter extends BaseAdapter implements DatePickerDialogFragment.DatePickerDialogHandler {

        private ArrayList<Calendar> mDateTimes;
        private LayoutInflater mInflater;
        private ViewHolder holder;
        private DatePickerBuilder mDatePickerBuilder;
        private DateFormat mDateTimeFormatter = SimpleDateFormat.getDateInstance();

        public SampleAdapter(Context context, FragmentManager fm) {
            super();
            mInflater = LayoutInflater.from(context);

            Calendar now = Calendar.getInstance();
            mDateTimes = new ArrayList<Calendar>();
            for (int i : months) {
                Calendar dt = Calendar.getInstance();
                dt.set(now.get(Calendar.YEAR)-1,i,1);
                mDateTimes.add(dt);
            }
            for (int i : months) {
                Calendar dt = Calendar.getInstance();
                dt.set(now.get(Calendar.YEAR),i,1);
                mDateTimes.add(dt);
            }
            for (int i : months) {
                Calendar dt = Calendar.getInstance();
                dt.set(now.get(Calendar.YEAR)+1,i,1);
                mDateTimes.add(dt);
            }

            mDatePickerBuilder = new DatePickerBuilder()
                    .setFragmentManager(fm)
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        }

        private class ViewHolder {

            public Button button;
            public TextView text;
        }

        @Override
        public int getCount() {
            return mDateTimes.size();
        }

        @Override
        public Calendar getItem(int position) {
            return mDateTimes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();
                holder.button = (Button) view.findViewById(R.id.button);
                holder.text = (TextView) view.findViewById(R.id.text);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            Calendar dt = getItem(position);
            holder.text.setText(mDateTimeFormatter.format(dt.getTime()));
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatePickerBuilder.setReference(position);
                    mDatePickerBuilder.addDatePickerDialogHandler(SampleAdapter.this);
                    mDatePickerBuilder.show();
                }
            });

            return view;
        }

        @Override
        public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
            Calendar dt = Calendar.getInstance();
            int i = months[monthOfYear];
            dt.set(year,i,dayOfMonth);
            mDateTimes.set(reference, dt);
            notifyDataSetChanged();
        }
    }
}

package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.hyveminds.rafmika.model.Event;

import java.util.List;

import rafmika.hyveminds.com.rafmika.R;

/**
 * Created by storm on 12/20/2016.
 */

public class EventAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Event> mDataSource;

    public EventAdapter(Context context, List<Event> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mDataSource.size();
    }
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refresh(List<Event> items)
    {
        this.mDataSource = items;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = mDataSource.get(position);
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_event, parent, false);
        TextView dayView = (TextView) rowView.findViewById(R.id.dayTextView);
        TextView monthView = (TextView) rowView.findViewById(R.id.monthTextView);
        TextView subjectView = (TextView) rowView.findViewById(R.id.subjectTextView);
        TextView addressView = (TextView) rowView.findViewById(R.id.addressTextView);
        String dateString = event.getDue_date();

        String monthString = dateString.split("-")[1];
        String dayString = dateString.split("-")[2];
        dayView.setText(dayString);
        monthView.setText(convertMonthToString(monthString));
        subjectView.setText(event.getSubject());
        addressView.setText(event.getLocation());

        return rowView;
    }
    private String convertMonthToString(String monthStr) {
        String result = "";
        switch (monthStr) {
            case "01":
                result = "JAN";
                break;
            case "02":
                result = "FEB";
                break;
            case "03":
                result = "MAR";
                break;
            case "04":
                result = "APR";
                break;
            case "05":
                result = "MAY";
                break;
            case "06":
                result = "JUN";
                break;
            case "07":
                result = "JUL";
                break;
            case "08":
                result = "AUG";
                break;
            case "09":
                result = "SEP";
                break;
            case "10":
                result = "OCT";
                break;
            case "11":
                result = "NOV";
                break;
            case "12":
                result = "DEC";
                break;
            default:
                break;
        }
        return result;
    }
}

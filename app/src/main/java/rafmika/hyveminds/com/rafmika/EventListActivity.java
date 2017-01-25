package rafmika.hyveminds.com.rafmika;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyveminds.rafmika.api.RafmikaAPI;
import com.hyveminds.rafmika.model.Event;
import com.hyveminds.rafmika.response.GetEventResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.EventAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by storm on 12/19/2016.
 */

public class EventListActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private String sessionName = null;
    private String userId = null;
    private ListView eventListView;
    private List<Event> events = null;
    private EventAdapter adapter = null;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_event_list);

        sessionName = getIntent().getStringExtra("EXTRA_SESSION_ID");
        userId = getIntent().getStringExtra("EXTRA_USER_ID");
        mContext = this;

        eventListView = (ListView)findViewById(R.id.envetListView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        events = new ArrayList<>();
        adapter = new EventAdapter(this, events);
        eventListView.setAdapter(adapter);
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Event event = ((EventListActivity)mContext).events.get(position);
                Intent intent = new Intent(EventListActivity.this, DetailEventActivity.class);
                Gson gson = new Gson();
                String eventJson = gson.toJson(event);
                intent.putExtra("eventJson", eventJson);
                intent.putExtra("EXTRA_SESSION_ID", sessionName);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        getEventLists();
                                    }
                                }
        );
    }
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        getEventLists();
    }

    public void getEventLists() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RafmikaAPI.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RafmikaAPI service = retrofit.create(RafmikaAPI.class);
        String query = "select * from Events;";
        Call<GetEventResponse> call = service.getEventList(this.sessionName, query);

        call.enqueue(new Callback<GetEventResponse>() {
            @Override
            public void onResponse(Response<GetEventResponse> response, Retrofit retrofit) {
                try {
                    if (response.body().isSuccessful()) {

                        List<Event> tmpEvents = response.body().getResult();

                        List<Event> filteredEvent = new ArrayList<Event>();
                        for (Event event : tmpEvents) {
                            if (isAfterDateFromToday(getCurrentDateFromString(event.getDue_date())) && event.getAssigned_user_id().equals(userId)) {
                                filteredEvent.add(event);
                            }
                        }
                        EventListActivity listActivity = (EventListActivity) mContext;
                        listActivity.events.clear();
                        listActivity.events = filteredEvent;

                        adapter.refresh(listActivity.events);
                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    } else {
                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(EventListActivity.this, response.body().getError().getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (Exception e) {
                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    public boolean isAfterDateFromToday(Date date) {
        Date today = new Date();
        if(date.after(today)){
            return true;
        }
        if(date.before(today)){
            return false;
        }
        if(date.equals(today)){
            return true;
        }
        return false;
    }

    public Date getCurrentDateFromString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            return convertedDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

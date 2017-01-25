package rafmika.hyveminds.com.rafmika;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyveminds.rafmika.api.RafmikaAPI;
import com.hyveminds.rafmika.model.Event;
import com.hyveminds.rafmika.model.LoginModel;
import com.hyveminds.rafmika.response.BaseResponse;
import com.hyveminds.rafmika.response.LoginResponse;

import org.w3c.dom.Text;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static java.security.AccessController.getContext;

/**
 * Created by storm on 12/20/2016.
 */

public class DetailEventActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView companyTextView;
    private TextView addressTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView activityTypeTextView;

    private String sessionName;

    private Button backButton = null;

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_event);

        sessionName = getIntent().getStringExtra("EXTRA_SESSION_ID");

        Gson gson = new Gson();
        final Event myEvent = gson.fromJson(getIntent().getStringExtra("eventJson"), Event.class);

        Log.d("subject", myEvent.getSubject());

        companyTextView = (TextView) findViewById(R.id.companyTextView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);
        startTimeTextView = (TextView) findViewById(R.id.startTimeTextView);
        endTimeTextView = (TextView) findViewById(R.id.endTimeTextView);
        activityTypeTextView = (TextView) findViewById(R.id.activityTypeTextView);

        String[] data = {"Started", "Planned", "Held", "Completed"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        final CustomSpinner spinner = (CustomSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        for (int i = 0; i < data.length; i++) {
            if (myEvent.getEventstatus().equals(data[i])) {
                spinner.setSelection(i, false);
                break;
            }
        }
        spinner.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            public void onSpinnerOpened() {
                spinner.setSelected(true);
            }
            public void onSpinnerClosed() {
                spinner.setSelected(false);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Spinner value...."+spinner.getSelectedItem().toString());

                final ProgressDialog progressDialog = new ProgressDialog(DetailEventActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating...");
                progressDialog.show();

                myEvent.setEventstatus(spinner.getSelectedItem().toString());
                Gson gson = new Gson();
                String eventJson = gson.toJson(myEvent);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(RafmikaAPI.baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RafmikaAPI service = retrofit.create(RafmikaAPI.class);
                Call<BaseResponse> call = service.update("update", DetailEventActivity.this.sessionName, eventJson);
                System.out.println("eventJson...."+eventJson);
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {
                        progressDialog.dismiss();
                        try {
                            if (response.body().isSuccessful()) {
                                Toast.makeText(DetailEventActivity.this, "Event status changed successfully!", Toast.LENGTH_SHORT).show();
                                return;

                            } else {
                                Toast.makeText(DetailEventActivity.this, response.body().getError().getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }


                        } catch (Exception e) {
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backButton = (Button) findViewById(R.id.backBuutton);
        backButton.setOnClickListener(this);

        companyTextView.setText(myEvent.getSubject());
        addressTextView.setText(myEvent.getLocation());
        startTimeTextView.setText(myEvent.getDue_date() + " " + myEvent.getTime_start());
        endTimeTextView.setText(myEvent.getDue_date() + " " + myEvent.getTime_end());
        activityTypeTextView.setText(myEvent.getActivitytype());

//        statusTextView.setText(myEvent.getEventstatus()+ " ▾");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBuutton) {
            onBackPressed();
        }
    }
}

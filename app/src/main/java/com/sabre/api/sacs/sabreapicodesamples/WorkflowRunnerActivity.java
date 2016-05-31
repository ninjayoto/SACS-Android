package com.sabre.api.sacs.sabreapicodesamples;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.sabre.api.sacs.workflow.SharedContext;
import com.sabre.api.sacs.workflow.Workflow;
import com.sabre.api.sacs.sabreapicodesamples.activity.LeadPriceCalendarActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WorkflowRunnerActivity extends AppCompatActivity {

    private AutoCompleteTextView.Validator iataCodeValidator = new AutoCompleteTextView.Validator() {

        @Override
        public boolean isValid(CharSequence text) {
            return (text.length() == 3);
        }

        @Override
        public CharSequence fixText(CharSequence invalidText) {
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_runner);

        String[] airports = getResources().getStringArray(R.array.airports);
        ArrayAdapter<String> originAirportsAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, airports);
        ArrayAdapter<String> destinationAirportsAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, airports);
        AutoCompleteTextView origins = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewAirportsOrigin);
        AutoCompleteTextView destinations = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewAirportsDestination);
        origins.setAdapter(originAirportsAdapter);
        origins.setValidator(iataCodeValidator);
        destinations.setAdapter(destinationAirportsAdapter);
        destinations.setValidator(iataCodeValidator);

        /*
        The below code is a quick and dirty solution to read the configuration file (authentication, etc.)
        We highly encourage, such information is not being kept in the application package for safety reasons.
         */
//        try {
//            InputStream rawResource = getResources().openRawResource(R.raw.SACSConfig);
//            SacsConfiguration.restConfig.load(rawResource);
//            System.out.println("The properties are now loaded");
//            System.out.println("properties: " + SacsConfiguration.restConfig);
//        } catch (Resources.NotFoundException e) {
//            System.err.println("Did not find raw resource: "+e);
//        } catch (IOException e) {
//            System.err.println("Failed to open DES property file");
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workflow_runner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void runWorkflow(View view) {
        String origin = ((AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewAirportsOrigin)).getText().toString();
        String destination = ((AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewAirportsDestination)).getText().toString();
        String departureDate = ((EditText)findViewById(R.id.editTextDepartureDate)).getText().toString();
        DateFormat sdf = SimpleDateFormat.getDateInstance();
        try {
            Date depDate = sdf.parse(departureDate);
            departureDate = new SimpleDateFormat("yyyy-MM-dd").format(depDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LeadPriceCalendarActivity leadPriceCalendarActivity = new LeadPriceCalendarActivity(origin.toUpperCase(), destination.toUpperCase(), departureDate);
        SharedContext context = new Workflow(leadPriceCalendarActivity).run();

        Intent intent = new Intent(this, ShowResultsActivity.class);
        //The results should not be public static.
        //It is done, as at the generation time, all the domain classes should be marked as serializable
        //in order to pass them to the activity with the Intent's putExtra() method.
        //The domain classes were copied from the SACS Java project, where the Serializable marker
        //was not required.
        ShowResultsActivity.results = context;
        startActivity(intent);
    }

    public void showDatePickerDialog(View v) {
        Calendar cal = Calendar.getInstance();
        AlertDialog newFragment = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        DateFormat sdf = SimpleDateFormat.getDateInstance();
                        EditText departureDate = (EditText) findViewById(R.id.editTextDepartureDate);
                        departureDate.setText(sdf.format(cal.getTime()));
                    }
                }, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        newFragment.show();

    }
}

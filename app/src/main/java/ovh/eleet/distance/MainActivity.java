package ovh.eleet.distance;

import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {


    LinearLayout linLayMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CoordinatorLayout colLay = (CoordinatorLayout) findViewById(R.id.coordLay);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fabSmall1 = (FloatingActionButton) findViewById(R.id.fabSmall1);
        final CoordinatorLayout mainCoord = (CoordinatorLayout) findViewById(R.id.mainCoord);
        ScrollView sv = (ScrollView) findViewById(R.id.sViewMain);
        sv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 0 ||scrollY <0 && fab.isShown()) {
                    fab.hide();
                    colLay.setVisibility(View.GONE);
                }
                else
                    fab.show();
            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Button is clicked", Toast.LENGTH_LONG).show();
                /*if(fabSmall1.getVisibility()==View.INVISIBLE)
                    fabSmall1.setVisibility(View.VISIBLE);
                else if(fabSmall1.getVisibility()==View.VISIBLE)
                    fabSmall1.setVisibility(View.INVISIBLE);*/
                mainCoord.setBackgroundColor(Color.parseColor("#AAB6C7"));
                if(colLay.getVisibility()==View.GONE)
                    colLay.setVisibility(View.VISIBLE);
                else if(colLay.getVisibility()==View.VISIBLE)
                    colLay.setVisibility(View.GONE);
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_white_24px));
                mainCoord.getBackground().setAlpha(50);
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new HttpRequestTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Record[] RECLIST;
    private class HttpRequestTask extends AsyncTask<Void, Void, Record> {
        @Override
        protected Record doInBackground(Void... params) {
            try {
                final String url = "http://www.31337.ovh:8080/distance/getAllRecords";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Record[] recList = restTemplate.getForObject(url, Record[].class);
                RECLIST = recList;
                return null;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Record record) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            linLayMain = (LinearLayout) findViewById(R.id.LinLay);
            linLayMain.removeAllViews();
            TableLayout tableLayout = new TableLayout(MainActivity.this);
            TableRow tableRow;


            for (Record r : RECLIST) {
                tableRow = new TableRow(MainActivity.this);

                TextView tvDate = new TextView(MainActivity.this);
                TextView tvDistance = new TextView(MainActivity.this);

                tvDistance.setText(String.valueOf(r.getDistance()) + " cm");

                tvDate.setText(df.format(r.getDate()));
                tvDate.setPadding(20, 20, 20, 20);

                tableRow.addView(tvDate);
                tableRow.addView(tvDistance);

                tableLayout.addView(tableRow);

                System.out.println("r: " + r.toString());
            }
            linLayMain.addView(tableLayout);

        }

    }
}


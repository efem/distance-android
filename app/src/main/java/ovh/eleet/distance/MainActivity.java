package ovh.eleet.distance;

import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import ovh.eleet.distance.dialog.BrowseAllDialog;
import ovh.eleet.distance.dialog.DateDialog;
import ovh.eleet.distance.dialog.DistanceDialog;
import ovh.eleet.distance.dialog.DistanceDialog.DistanceDialogListener;
import ovh.eleet.distance.helper.PrepareStatement;

import static ovh.eleet.distance.dialog.DateDialog.*;


public class MainActivity extends AppCompatActivity implements DistanceDialogListener, DateDialogListener, BrowseAllDialog.BrowseAllDialogListener {
    @Override
    public void onDistanceDialogPositiveClick(DialogFragment dialog, String fromDistance, String toDistance) {
        //Toast.makeText(getApplicationContext(), foo, Toast.LENGTH_SHORT).show();
        Log.i("DISTANCE", "From: " + fromDistance + " To: " + toDistance);
        try {
            double fromDistanceDouble = Double.parseDouble(fromDistance);
            double toDistanceDouble = Double.parseDouble(toDistance);
            if (fromDistanceDouble >= toDistanceDouble) {
                Toast.makeText(getApplicationContext(), "Values are incorrect: FROM is greater or equal to TO", Toast.LENGTH_SHORT).show();
            } else {
                new HttpRequestTask(PrepareStatement.prepareRangeStatement(fromDistance, toDistance)).execute();
            }

        } catch (NumberFormatException nfe) {
            Toast.makeText(getApplicationContext(), "Values cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDistanceDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onDateDialogPositiveClick(DialogFragment dialog, String fromDate, String toDate, String fromTime, String toTime) {
        //Toast.makeText(getApplicationContext(), foo, Toast.LENGTH_SHORT).show();
        Log.i("DATE", "From: " + fromDate + " To: " + toDate);
        Log.i("TIME", "From: " + fromTime + " To: " + toTime);
        String fullDateFrom = fromDate + "_" + fromTime.replace(":", "-") + "-00";
        String fullDateTo = toDate + "_" + toTime.replace(":", "-") + "-00";

        new HttpRequestTask(PrepareStatement.prepareDateStatement(fullDateFrom, fullDateTo)).execute();

    }

    @Override
    public void onDateDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onDateDialogLastDayClick(DialogFragment dialog) {
        new HttpRequestTask(PrepareStatement.prepareDateStatementMinusDay(1)).execute();
    }

    @Override
    public void onDateDialogLast3DaysClick(DialogFragment dialog) {
        new HttpRequestTask(PrepareStatement.prepareDateStatementMinusDay(3)).execute();
    }

    @Override
    public void onBrowseAllDialogPositiveClick(DialogFragment dialog, String pageNo, String etPageSize) {
        Log.i("REC_COUNT", "Record count: " + etPageSize);
        Log.i("PAGE_NO", "Page Nu,ber: " + pageNo);

        new HttpRequestTask(PrepareStatement.prepareBrowseAllStatement(pageNo, etPageSize)).execute();

    }

    @Override
    public void onBrowseAllDialogNegativeClick(DialogFragment dialog) {

    }

    LinearLayout linLayMain;
    static boolean fabClicked = false;

    static CoordinatorLayout colLay = null;
    static FloatingActionButton fab = null;
    static CoordinatorLayout mainCoord = null;
    static ScrollView sv = (ScrollView) null;
    static RelativeLayout mainRelLay = null;
    static FloatingActionButton fabSmallRange = null;
    static FloatingActionButton fabSmallDate = null;
    static FloatingActionButton fabSmallRBrowseAll = null;
    static FloatingActionButton fabSmallRefresh = null;
    static TextView tvDateHead = null;
    static TextView tvDistanceHead = null;

    protected void fabActionStateNormal(boolean fabStateNormal, boolean bgStateNormal) {
        if (fabStateNormal) {
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_white_24px));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_clear_white_24px));
            colLay.bringToFront();
        }
        if (bgStateNormal){
            mainCoord.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.backgroundMain));
            colLay.bringToFront();
            mainCoord.getBackground().setAlpha(100);
        } else {
            mainCoord.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.backgroundDarker));
            //mainCoord.getBackground().setAlpha(50);
        }
    }
    public void showDistanceDialog() {
        DialogFragment dialog = new DistanceDialog();
        dialog.show(getFragmentManager(), "DistanceDialog");
    }

    public void showDateDialog() {
        DialogFragment dialog = new DateDialog();
        dialog.show(getFragmentManager(), "DateDialog");
    }

    public void showBrowseAllDialog() {
        DialogFragment dialog = new BrowseAllDialog();
        dialog.show(getFragmentManager(), "BrowseAllDialog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        colLay = (CoordinatorLayout) findViewById(R.id.coordLay);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mainCoord = (CoordinatorLayout) findViewById(R.id.mainCoord);
        sv = (ScrollView) findViewById(R.id.sViewMain);
        mainRelLay = (RelativeLayout) findViewById(R.id.include);
        fabSmallRange = (FloatingActionButton) findViewById(R.id.fabSmallRange);
        fabSmallDate = (FloatingActionButton) findViewById(R.id.fabSmallDate);
        fabSmallRefresh = (FloatingActionButton) findViewById(R.id.fabSmallRefresh);
        fabSmallRBrowseAll = (FloatingActionButton) findViewById(R.id.fabSmallBrowseAll);


        sv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 0 ||scrollY <0 && fab.isShown()) {
                    fab.hide();
                    fabActionStateNormal(true, true);
                    colLay.setVisibility(View.INVISIBLE);

                } else {
                    fabActionStateNormal(true, true);
                    fab.show();
                }
            }

        });

        fabSmallRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colLay.setVisibility(View.INVISIBLE);
                fabActionStateNormal(true, true);
                showDistanceDialog();
                Toast.makeText(getApplicationContext(), "Distance dialog shown", Toast.LENGTH_SHORT);
            }
        });

        fabSmallDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colLay.setVisibility(View.INVISIBLE);
                fabActionStateNormal(true, true);
                showDateDialog();
                Toast.makeText(getApplicationContext(), "Date dialog shown", Toast.LENGTH_SHORT);
            }
        });

        fabSmallRBrowseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colLay.setVisibility(View.INVISIBLE);
                fabActionStateNormal(true, true);
                showBrowseAllDialog();
                Toast.makeText(getApplicationContext(), "Browse all dialog shown", Toast.LENGTH_SHORT);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(colLay.getVisibility()==View.INVISIBLE) {
                    colLay.setVisibility(View.VISIBLE);
                    fabClicked = true;
                }
                else if(colLay.getVisibility()==View.VISIBLE) {
                    colLay.setVisibility(View.INVISIBLE);
                    fabClicked = false;
                }
                if (fabClicked) {
                    fabActionStateNormal(false, false);
                } else {
                    fabActionStateNormal(true, true);
                }
            }
        });

        fabSmallRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colLay.setVisibility(View.INVISIBLE);
                fabActionStateNormal(true, true);
                new HttpRequestTask().execute();
                Toast.makeText(getApplicationContext(), "Refresh OK", Toast.LENGTH_SHORT);
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
    public static String URL = "";



    private class HttpRequestTask extends AsyncTask<Void, Void, Record> {

        public HttpRequestTask() {
            if (URL == null || URL == "")
            URL = "http://www.31337.ovh:8080/distance/getAllRecords";
        }
        public HttpRequestTask(String url) {
            URL = url;
            Log.i("URL", url);
        }



        @Override
        protected Record doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Record[] recList = restTemplate.getForObject(URL, Record[].class);
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
            Typeface roboto = Typeface.createFromAsset(getApplicationContext().getAssets(),
                    "font/RobotoCondensed-Light.ttf"); //use this.getAssets if you are calling from an Activity
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            tvDateHead = (TextView) findViewById(R.id.tvDateHead);
            tvDistanceHead = (TextView) findViewById(R.id.tvDistanceHead);
            tvDateHead.setTypeface(roboto);
            tvDistanceHead.setTypeface(roboto);

            linLayMain = (LinearLayout) findViewById(R.id.LinLay);
            linLayMain.removeAllViews();
            TableLayout tableLayout = new TableLayout(MainActivity.this);
            TableRow tableRow;
            TableRow tableRowLine;
            /*TableRow tableRowLine = new TableRow(MainActivity.this);
            tableRowLine.setBackgroundResource(R.drawable.row_border);*/

            Resources resource = getResources();

            for (Record r : RECLIST) {
                tableRow = new TableRow(MainActivity.this);
                tableRowLine = new TableRow(MainActivity.this);
                tableRowLine.setBackgroundColor(getColor(R.color.fontColorMain));
                tableRowLine.setMinimumHeight(2);
                //tableRowLine.setBackgroundResource(R.color.backgroundDarker);

                TextView tvDate = new TextView(MainActivity.this);
                TextView tvDistance = new TextView(MainActivity.this);

                tvDistance.setTypeface(roboto);
                tvDistance.setTextColor(getColor(R.color.fontColorMain));
                tvDistance.setText(String.valueOf(r.getDistance()) + " cm");

                tvDate.setTypeface(roboto);
                tvDate.setTextColor(getColor(R.color.fontColorMain));
                tvDate.setText(df.format(r.getDate()));
                tvDate.setPadding(0, 20, 20, 20);
                tvDate.setWidth((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, resource.getDisplayMetrics()));


                tableRow.addView(tvDate);
                tableRow.addView(tvDistance);

                tableLayout.addView(tableRow);
                tableLayout.addView(tableRowLine);

                System.out.println("r: " + r.toString());
            }
            linLayMain.addView(tableLayout);


        }

    }
}


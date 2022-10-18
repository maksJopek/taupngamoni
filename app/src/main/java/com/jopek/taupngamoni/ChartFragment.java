package com.jopek.taupngamoni;

import static com.jopek.taupngamoni.Currency.CURRENCIES;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.BaseWithMarkers;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Cartesian cartesian;
    private boolean columnChart = true;
    private List<DataEntry> seriesData = new ArrayList<>();

    private Currency curFrom = CURRENCIES.get(0);
    private Currency curTo = CURRENCIES.get(0);
    private long chartTime = 7;
    private char chartTimeF = 'd';
    private AnyChartView anyChartView;
    private int checkFrom = 0;
    private int checkTo = 0;
    private MySpinnerAdapter spinnerFromAdapter;
    private MySpinnerAdapter spinnerToAdapter;

    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        Log.d("maks", "onCreateView: " + anyChartView + curFrom.code);
        initializeSpinners(view);
//        if (anyChartView == null) {
        anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        anyChartView.resetPivot();
//        if (cartesian != null) cartesian.removeAllSeries();
        cartesian = AnyChart.line();
        cartesian.removeAllSeries();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair().yLabel(true).yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        anyChartView.setChart(cartesian);
//        }
        checkFrom = 0;
        checkTo = 0;
        refreshChart();

//        cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.");

//        cartesian.yAxis(0).title("Number of Bottles Sold (thousands)");
//        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);


        view.findViewById(R.id.b1d).setOnClickListener(v -> onBtnClick("9m"));
        view.findViewById(R.id.b7d).setOnClickListener(v -> onBtnClick("7d"));
        view.findViewById(R.id.b1m).setOnClickListener(v -> onBtnClick("1m"));
        view.findViewById(R.id.b3m).setOnClickListener(v -> onBtnClick("3m"));
        view.findViewById(R.id.b6m).setOnClickListener(v -> onBtnClick("6m"));
        view.findViewById(R.id.b1y).setOnClickListener(v -> onBtnClick("1y"));

        TextView btn = view.findViewById(R.id.btn_change_chart_type);
        btn.setOnClickListener(v -> {
            columnChart = !columnChart;
            Log.d("maks", "onCreateView: ");
            refreshChart();
        });
        anyChartView.setVisibility(View.INVISIBLE);
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                () -> {
                    anyChartView.setVisibility(View.VISIBLE);
                    refreshChart();
                },
                1500);


        return view;
    }

    public void refreshChart() {
        ZonedDateTime nowO = ZonedDateTime.now();
        String now = nowO.format(DateTimeFormatter.ISO_LOCAL_DATE);
        ZonedDateTime pastO = ZonedDateTime.now();
        if (chartTimeF == 'd') pastO = pastO.minusDays(chartTime);
        else if (chartTimeF == 'm') pastO = pastO.minusMonths(chartTime);
        else if (chartTimeF == 'y') pastO = pastO.minusYears(chartTime);

        String past = pastO.format(DateTimeFormatter.ISO_LOCAL_DATE);
        Log.d("maks", "refreshChart: " + curFrom.code);
        String url = "https://api.exchangerate.host/timeseries?base=" + curFrom.code + "&symbols=" + curTo.code + "&start_date=" + past + "&end_date=" + now;
        Log.d("maks", "refreshChart: " + url);

        Requests requests = new Requests();
        requests.getJson(url, json -> {
            Log.d("maks", "refreshChart: " + json);
            try {
                JSONObject rates = json.getJSONObject("rates");
                seriesData = new ArrayList<>();
                for (Iterator<String> it = rates.keys(); it.hasNext(); ) {
                    String key = it.next();
                    JSONObject rate = rates.getJSONObject(key);
                    seriesData.add(new CustomDataEntry(key, rate.getDouble(rate.keys().next())));
                }

                redrawChart();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void redrawChart() {
        if (cartesian.getSeriesAt(0) != null) cartesian.removeAllSeries();

        Log.d("maks", "redrawChart: " + seriesData);
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping seriesMapping = set.mapAs("{ x: 'x', value: 'value' }");

        BaseWithMarkers series;
        if (columnChart) series = cartesian.column(seriesMapping);
        else series = cartesian.line(seriesMapping);

//        series.hovered().markers()
//                .enabled(true)
//                .type(MarkerType.CIRCLE)
//                .size(4d);
//        series.tooltip()
//                .position("right")
//                .anchor(Anchor.LEFT_CENTER)
//                .offsetX(5d)
//                .offsetY(5d);
//        Log.d("maks", "redrawChart: " + seriesMapping.getJsBase());
        series.data(seriesMapping);
//        series.name("Brandy");

//        cartesian.legend().enabled(true)
//                .fontSize(13d)
//                .padding(0d, 0d, 10d, 0d);
    }

    public void onBtnClick(String time) {
        chartTime = Character.getNumericValue(time.charAt(0));
        chartTimeF = time.charAt(1);

        Log.d("maks", "onBtnClick: ");
        refreshChart();
    }

    public void initializeSpinners(View view) {
        Spinner cur_from = view.findViewById(R.id.cur_from);
        Spinner cur_to = view.findViewById(R.id.cur_to);
        if (spinnerFromAdapter == null)
            spinnerFromAdapter = new MySpinnerAdapter(requireContext(), CURRENCIES);
        cur_from.setAdapter(spinnerFromAdapter);

        if (spinnerToAdapter == null)
            spinnerToAdapter = new MySpinnerAdapter(requireContext(), CURRENCIES);
        cur_to.setAdapter(spinnerToAdapter);

        cur_from.setSelection(1);
        cur_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("maks", "onItemSelected: " + checkFrom);
                String oldCode = curFrom.code;
                curFrom = CURRENCIES.get(position);
                if (++checkFrom < 2 || oldCode.equals(curFrom.code)) return;
                if (curTo != null) {
                    refreshChart();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        cur_to.setSelection(0);
        cur_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String oldCode = curTo.code;
                curTo = CURRENCIES.get(position);
                Log.d("maks", "onItemSelected: " + checkTo + curTo.code);
                if (++checkTo < 2 || oldCode.equals(curTo.code)) return;
                if (curFrom != null) {
                    refreshChart();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.d("maks", "onDestroyView: ");
//        anyChartView.clear();
//        anyChartView.resetPivot();
//        cartesian.removeAllSeries();
//        seriesData.clear();
//        cartesian.column(seriesData);
//    }


    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }
}
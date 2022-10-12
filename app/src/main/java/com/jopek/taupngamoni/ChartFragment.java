package com.jopek.taupngamoni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.BaseWithMarkers;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AnyChartView anyChartView;
    private Cartesian cartesian;
    private boolean columnChart = true;
    private List<DataEntry> seriesData = new ArrayList<>();

    private BaseWithMarkers series;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("1986", 3.6));
        seriesData.add(new CustomDataEntry("1987", 7.1));
        seriesData.add(new CustomDataEntry("1988", 8.5));
        seriesData.add(new CustomDataEntry("1989", 9.2));
        seriesData.add(new CustomDataEntry("1990", 10.1));

        refreshChart();
        anyChartView.setChart(cartesian);

//        cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.");

//        cartesian.yAxis(0).title("Number of Bottles Sold (thousands)");
//        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);


        view.findViewById(R.id.b1d).setOnClickListener(v -> onBtnClick("1d"));
        view.findViewById(R.id.b7d).setOnClickListener(v -> onBtnClick("7d"));
        view.findViewById(R.id.b1m).setOnClickListener(v -> onBtnClick("1m"));
        view.findViewById(R.id.b3m).setOnClickListener(v -> onBtnClick("3m"));
        view.findViewById(R.id.b6m).setOnClickListener(v -> onBtnClick("6m"));
        view.findViewById(R.id.b1y).setOnClickListener(v -> onBtnClick("1y"));

        view.findViewById(R.id.btn_change_chart_type).setOnClickListener(v -> {
            columnChart = !columnChart;
            refreshChart();
        });

        return view;
    }

    public void refreshChart() {
        cartesian.removeAllSeries();

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping seriesMapping = set.mapAs("{ x: 'x', value: 'value' }");

        if (columnChart)
            series = cartesian.column(seriesMapping);
        else
            series = cartesian.line(seriesMapping);

        series.data(seriesMapping);
//        series.name("Brandy");
        series.hovered().markers()
                .enabled(true)
                .type(MarkerType.CIRCLE)
                .size(4d);
        series.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true)
                .fontSize(13d)
                .padding(0d, 0d, 10d, 0d);
    }

    public void onBtnClick(String time) {
         seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("1982", 8.5));
        seriesData.add(new CustomDataEntry("1992", 7.1));
        seriesData.add(new CustomDataEntry("1992", 10.1));
        seriesData.add(new CustomDataEntry("1992", 9.2));
        seriesData.add(new CustomDataEntry("1992", 3.6));

        refreshChart();
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }
}
package com.example.thirtyseven.myschedule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {

    final String LOG_TAG = "myLogs";
    final String ID_TAG = "_id";
    final String NAME_TAG = "name";
    final String WEEKDAY_TAG = "weekday";
    final String TIME_TAG = "time";
    final String GROUP_TAG = "myGroup";
    final String AUDIENCE_TAG = "audience";
    final String ODD_OR_EVEN_OR_NOT_TAG = "oddOrEvenOrNot";
    final String TEACHER_TAG = "teacher";

    Spinner day, group;
    String[] dayStr;
    View myView;
    ArrayList<String> groupArray = new ArrayList<>();
    Schedule dbHelper;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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

        if (myView == null) {
            myView = inflater.inflate(R.layout.fragment_schedule, container, false);
        }
        dbHelper = new Schedule(getActivity().getApplicationContext());
        init();
        fillArray();
        setListeners();
        ArrayAdapter<?> dayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.day, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, groupArray);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group.setAdapter(groupAdapter);
        if (day != null)
            day.setAdapter(dayAdapter);


        return myView;
    }

    private void fillArray() {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // делаем запрос всех данных из таблицы schedule, получаем Cursor
        Cursor c = db.query("schedule", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(ID_TAG);
            int nameColIndex = c.getColumnIndex(NAME_TAG);
            int weekdayColIndex = c.getColumnIndex(WEEKDAY_TAG);
            int timeColIndex = c.getColumnIndex(TIME_TAG);
            int groupColIndex = c.getColumnIndex(GROUP_TAG);
            int audienceColIndex = c.getColumnIndex(AUDIENCE_TAG);
            int oddOrEvenOrNotColIndex = c.getColumnIndex(ODD_OR_EVEN_OR_NOT_TAG);
            int teacherColIndex = c.getColumnIndex(TEACHER_TAG);

            do {
                if (groupArray.size() != 0) {
                    for (String s : groupArray) {
                        if (c.getString(groupColIndex).equals(s)) break;
                        else groupArray.add(c.getString(groupColIndex));
                    }
                }else groupArray.add(c.getString(groupColIndex));
            } while (c.moveToNext());
        }
    }

    private void setListeners() {
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayStr = getResources().getStringArray(R.array.day);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init() {
        day = (Spinner) myView.findViewById(R.id.day_spinner);
        group = (Spinner) myView.findViewById(R.id.group_spinner);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

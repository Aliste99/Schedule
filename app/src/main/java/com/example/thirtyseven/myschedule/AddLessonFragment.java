package com.example.thirtyseven.myschedule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class AddLessonFragment extends Fragment {

    final String LOG_TAG = "myLogs";
    final String ID_TAG = "_id";
    final String NAME_TAG = "name";
    final String WEEKDAY_TAG = "weekday";
    final String TIME_TAG = "time";
    final String GROUP_TAG = "myGroup";
    final String AUDIENCE_TAG = "audience";
    final String ODD_OR_EVEN_OR_NOT_TAG = "oddOrEvenOrNot";
    final String TEACHER_TAG = "teacher";
    View view;

    Button btnAdd, btnRead, btnClear;
    EditText etName, etTime, etTeacher, etAudience, etGroup;
    Spinner etWeekday;

    RadioButton not, even, odd;
    RadioGroup radioGroup;

    Schedule dbHelper;

    DayHelper dayHelper;

    int oddOrEvenOrNot, myGroup, time;
    String name, weekday, audience, teacher;

    View.OnClickListener onClickListener;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ScheduleFragment.OnFragmentInteractionListener mListener;

    public AddLessonFragment() {
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
        dbHelper = new Schedule(getActivity().getApplicationContext());
        if (view == null)
            view = inflater.inflate(R.layout.fragment_add_lesson, container, false);
        init();
        not.setText("not");
        even.setText("even");
        odd.setText("odd");
        not.setChecked(true);
        setListeners();
        // создаем объект для создания и управления версиями БД



        return view;
    }

    private void setListeners() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // создаем объект для данных
                ContentValues cv = new ContentValues();
                // подключаемся к БД
                SQLiteDatabase db = dbHelper.getWritableDatabase();


                switch (v.getId()) {
                    case R.id.btnAdd:

                        name = etName.getText().toString();
                        weekday = String.valueOf(etWeekday.getSelectedItemPosition());
                        time = Integer.parseInt(etTime.getText().toString());
                        audience = etAudience.getText().toString();
                        myGroup = Integer.parseInt(etGroup.getText().toString());
                        teacher = etTeacher.getText().toString();
                        if (not.isChecked())
                            oddOrEvenOrNot = 0;
                        else if (even.isChecked()) oddOrEvenOrNot = 1;
                        else if (odd.isChecked()) oddOrEvenOrNot = 2;

                        Log.d(LOG_TAG, "--- Insert in schedule: ---");
                        // подготовим данные для вставки в виде пар: наименование столбца - значение
                        cv.put(NAME_TAG, name);
                        cv.put(WEEKDAY_TAG, weekday);
                        cv.put(AUDIENCE_TAG, audience);
                        cv.put(TIME_TAG, time);
                        cv.put(GROUP_TAG, myGroup);
                        cv.put(ODD_OR_EVEN_OR_NOT_TAG, oddOrEvenOrNot);
                        cv.put(TEACHER_TAG, teacher);
                        // вставляем запись и получаем ее ID
                        long rowID = db.insert("schedule", null, cv);
                        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                        break;

                    case R.id.btnRead:
                        Log.d(LOG_TAG, "--- Rows in schedule: ---");
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
                                // получаем значения по номерам столбцов и пишем все в лог
                                Log.d(LOG_TAG,
                                        "ID = " + c.getInt(idColIndex) +
                                                ", " + NAME_TAG + " = " + c.getString(nameColIndex) +
                                                ", " + WEEKDAY_TAG + " = " + c.getString(weekdayColIndex) +
                                                ", " + TIME_TAG + " = " + c.getString(timeColIndex) +
                                                ", " + GROUP_TAG + " = " + c.getString(groupColIndex) +
                                                ", " + AUDIENCE_TAG + " = " + c.getString(audienceColIndex) +
                                                ", " + ODD_OR_EVEN_OR_NOT_TAG + " = " + c.getString(oddOrEvenOrNotColIndex) +
                                                ", " + TEACHER_TAG + " = " + c.getString(teacherColIndex));
                                // переход на следующую строку
                                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                            } while (c.moveToNext());
                        } else
                            Log.d(LOG_TAG, "0 rows");
                        c.close();
                        break;

                    case R.id.btnClear:
                        Log.d(LOG_TAG, "--- Clear schedule: ---");
                        // удаляем все записи
                        int clearCount = db.delete("schedule", null, null);
                        Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                        break;
                }
                // закрываем подключение к БД
                dbHelper.close();


            }
        };




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.not:
                        oddOrEvenOrNot = 0;
                        break;
                    case R.id.even:
                        oddOrEvenOrNot = 2;
                        break;
                    case R.id.odd:
                        oddOrEvenOrNot = 1;
                        break;
                }
            }
        });

        btnAdd.setOnClickListener(onClickListener);
        btnRead.setOnClickListener(onClickListener);
        btnClear.setOnClickListener(onClickListener);

    }


    private void init() {
        ArrayAdapter<?> dayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.day, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnRead = (Button) view.findViewById(R.id.btnRead);
        btnClear = (Button) view.findViewById(R.id.btnClear);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        etName = (EditText) view.findViewById(R.id.etName);
        etWeekday = (Spinner) view.findViewById(R.id.etWeekday);
        etAudience = (EditText) view.findViewById(R.id.etAudience);
        etGroup = (EditText) view.findViewById(R.id.etGroup);
        etTeacher = (EditText) view.findViewById(R.id.etTeacher);
        etTime = (EditText) view.findViewById(R.id.etTime);

        not = (RadioButton) view.findViewById(R.id.not);
        even = (RadioButton) view.findViewById(R.id.even);
        odd = (RadioButton) view.findViewById(R.id.odd);

        etWeekday.setAdapter(dayAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
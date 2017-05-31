package com.example.thirtyseven.myschedule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by ThirtySeven on 30.05.2017.
 */

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
    EditText etName, etWeekday, etTime, etTeacher, etAudience, etGroup;

    RadioButton not, even, odd;

    Schedule dbHelper;

    int oddOrEvenOrNot, myGroup, time;
    String name, weekday, audience, teacher;

    View.OnClickListener onClickListener;
    CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private BlankFragment.OnFragmentInteractionListener mListener;

    public AddLessonFragment() {
    }

    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
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

        view = inflater.inflate(R.layout.fragment_add_lesson, container, false);
        init();
        setListeners();
        // создаем объект для создания и управления версиями БД
        dbHelper = new Schedule(getActivity());

        btnAdd.setOnClickListener(onClickListener);
        btnRead.setOnClickListener(onClickListener);
        btnClear.setOnClickListener(onClickListener);

        not.setOnCheckedChangeListener(onCheckedChangeListener);
        even.setOnCheckedChangeListener(onCheckedChangeListener);
        odd.setOnCheckedChangeListener(onCheckedChangeListener);

        return inflater.inflate(R.layout.fragment_add_lesson, container, false);
    }

    private void setListeners() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// создаем объект для данных
                ContentValues cv = new ContentValues();

                // получаем данные из полей ввода

                // подключаемся к БД
                SQLiteDatabase db = dbHelper.getWritableDatabase();


                switch (v.getId()) {
                    case R.id.btnAdd:

                        name = etName.getText().toString();
                        weekday = etWeekday.getText().toString();
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

        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.not:
                        even.setChecked(false);
                        odd.setChecked(false);
                        break;
                    case R.id.even:
                        not.setChecked(false);
                        odd.setChecked(false);
                        break;
                    case R.id.odd:
                        even.setChecked(false);
                        not.setChecked(false);
                        break;

                }
            }
        };
    }


    private void init() {
        btnAdd = (Button) view.findViewById(R.id.btnAdd);

        btnRead = (Button) view.findViewById(R.id.btnRead);

        btnClear = (Button) view.findViewById(R.id.btnClear);

        etName = (EditText) view.findViewById(R.id.etName);
        etWeekday = (EditText) view.findViewById(R.id.etWeekday);
        etAudience = (EditText) view.findViewById(R.id.etAudience);
        etGroup = (EditText) view.findViewById(R.id.etGroup);
        etTeacher = (EditText) view.findViewById(R.id.etTeacher);
        etTime = (EditText) view.findViewById(R.id.etTime);

        not = (RadioButton) view.findViewById(R.id.not);
        even = (RadioButton) view.findViewById(R.id.even);
        odd = (RadioButton) view.findViewById(R.id.odd);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
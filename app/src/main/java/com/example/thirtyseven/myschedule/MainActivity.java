package com.example.thirtyseven.myschedule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear;
    EditText etName, etWeekday, etTime, etTeacher, etAudience, etGroup;

    RadioButton not, even, odd;

    Schedule dbHelper;

    int oddOrEvenOrNot, group, time, id;
    String name, weekday, audience, teacher;

    View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        standart();

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// создаем объект для данных
                ContentValues cv = new ContentValues();

                // получаем данные из полей ввода
                name = etName.getText().toString();
                weekday = etWeekday.getText().toString();
                time = Integer.parseInt(etTime.getText().toString());
                audience = etAudience.getText().toString();
                group = Integer.parseInt(etGroup.getText().toString());
                teacher = etTeacher.getText().toString();
                if(not.isChecked())
                    oddOrEvenOrNot = 0;
                else if (even.isChecked()) oddOrEvenOrNot = 1;
                else if(odd.isChecked())oddOrEvenOrNot = 2;


                // подключаемся к БД
                SQLiteDatabase db = dbHelper.getWritableDatabase();


                switch (v.getId()) {
                    case R.id.btnAdd:
                        Log.d(LOG_TAG, "--- Insert in mytable: ---");
                        // подготовим данные для вставки в виде пар: наименование столбца - значение

                        cv.put("name", name);
                        cv.put("weekday", weekday);
                        cv.put("audience", audience);
                        cv.put("group", group);
                        cv.put("oddOrEvenOrNot", oddOrEvenOrNot);
                        cv.put("teacher", teacher);
                        // вставляем запись и получаем ее ID
                        long rowID = db.insert("mytable", null, cv);
                        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                        break;

                    case R.id.btnRead:
                        Log.d(LOG_TAG, "--- Rows in mytable: ---");
                        // делаем запрос всех данных из таблицы mytable, получаем Cursor
                        Cursor c = db.query("mytable", null, null, null, null, null, null);

                        // ставим позицию курсора на первую строку выборки
                        // если в выборке нет строк, вернется false
                        if (c.moveToFirst()) {

                            // определяем номера столбцов по имени в выборке
                            int idColIndex = c.getColumnIndex("id");
                            int nameColIndex = c.getColumnIndex("name");
                            int emailColIndex = c.getColumnIndex("weekday");

                            do {
                                // получаем значения по номерам столбцов и пишем все в лог
                                Log.d(LOG_TAG,
                                        "ID = " + c.getInt(idColIndex) +
                                                ", name = " + c.getString(nameColIndex) +
                                                ", weekday = " + c.getString(emailColIndex));
                                // переход на следующую строку
                                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                            } while (c.moveToNext());
                        } else
                            Log.d(LOG_TAG, "0 rows");
                        c.close();
                        break;

                    case R.id.btnClear:
                        Log.d(LOG_TAG, "--- Clear mytable: ---");
                        // удаляем все записи
                        int clearCount = db.delete("mytable", null, null);
                        Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                        break;
                }
                // закрываем подключение к БД
                dbHelper.close();
            }
        };

        RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
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

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(onClickListener);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(onClickListener);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(onClickListener);

        etName = (EditText) findViewById(R.id.etName);
        etWeekday = (EditText) findViewById(R.id.etWeekday);
        etAudience = (EditText) findViewById(R.id.etAudience);
        etGroup = (EditText) findViewById(R.id.etGroup);
        etTeacher = (EditText) findViewById(R.id.etTeacher);
        etTime = (EditText) findViewById(R.id.etTime);

        not = (RadioButton) findViewById(R.id.not);
        even = (RadioButton) findViewById(R.id.even);
        odd = (RadioButton) findViewById(R.id.odd);

        // создаем объект для создания и управления версиями БД
        dbHelper = new Schedule(this);

    }



    private void standart() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

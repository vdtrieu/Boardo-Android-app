package com.bku.tuanvu.boardo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Trieu on 16-Mar-18.
 */

public class AddSubjectActivity extends AppCompatActivity implements setTimeDialog.DialogListener{
    private Button btnSetTime;
    private EditText subjectName, room, teacherName, teacherEmail, teacherPhone, note, date_timeView, startWeek, numOfWeek, weekBreak, timeNoti;
    public Subject subject;
    Calendar calendar;
    String strDate, strExpDate;
    Date date, expDate;
    SimpleDateFormat dateFormat;
    Switch notiSw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        //Create Actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Add Subject");

        //connect
   //     txtview12 = (EditText) findViewById(R.id.textView12);
        subjectName = findViewById(R.id.editSubject);
        room = findViewById(R.id.edit_room);
        teacherName = findViewById(R.id.editNameTeacher);
        teacherEmail = findViewById(R.id.editEmail);
        teacherPhone = findViewById(R.id.editPhone);
        note = findViewById(R.id.editNote);
        startWeek = findViewById(R.id.editStartingWeek);
        numOfWeek = findViewById(R.id.editNumWeek);
        weekBreak = findViewById(R.id.editWeekBreak);
        notiSw = findViewById(R.id.switch_set_noti);
        timeNoti = findViewById(R.id.edit_time_noti);

        // set time and strDate
        date_timeView = findViewById(R.id.timeDayView);
        btnSetTime = findViewById(R.id.buttonSetTime);
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
                date_timeView.setVisibility(View.VISIBLE);
                room.setVisibility(View.VISIBLE);
            }
        });
//        set week
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        date = calendar.getTime();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        int crntWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        startWeek.setText(Integer.toString(crntWeek));
        startWeek.setOnClickListener(showDatepicker);
        strDate = dateFormat.format(date);

        weekBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBreakWeekDialog();
            }
        });

        // Notification setting
        timeNoti.setVisibility(View.INVISIBLE);
        notiSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean swStat = notiSw.isChecked();
                if (swStat) timeNoti.setVisibility(View.VISIBLE);
                else timeNoti.setVisibility(View.INVISIBLE);
            }
        });



    }

    private void showBreakWeekDialog() {
                // Prepare grid view
                final GridView gridView = new GridView(this);

                List<Integer> mList = new ArrayList<>();
                for (int i = Integer.parseInt(startWeek.getText().toString()); i < (Integer.parseInt(startWeek.getText().toString())+Integer.parseInt(numOfWeek.getText().toString())); i++) {
                    mList.add(i);
                }
                gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                gridView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, mList));
                gridView.setNumColumns(3);

                gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(AddSubjectActivity.this,"check", Toast.LENGTH_SHORT).show();
                    }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddSubjectActivity.this,"check", Toast.LENGTH_SHORT).show();

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(AddSubjectActivity.this,"check", Toast.LENGTH_SHORT).show();
            }
        });

        // Set grid view to alertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(gridView);
        builder.setTitle("Tuần nghỉ");
        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String string = "";
                for (int i = 0; i < gridView.getCount(); i++) {
                    boolean isChecked = gridView.isItemChecked(i);
                    if (isChecked) {
//                        Toast.makeText(AddSubjectActivity.this,gridView.getItemAtPosition(i).toString(),Toast.LENGTH_SHORT).show();
                        string += gridView.getItemAtPosition(i).toString()+", ";
                        weekBreak.setText(string);
                    }
                }
                if (gridView.getCheckedItemCount() == 0) weekBreak.setText("N/A");

            }
        });

        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void showAlertDialog() {
        setTimeDialog dialog = new setTimeDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void applyTexts_room(String strRoom) {

        room.setText(strRoom);
    }

    @Override
    public void applyTexts_dateNtime(String dateAndTime) {
        date_timeView.setText(dateAndTime);

    }


    String EncoderDateTime(String dateNTime) {

            int index = 0;
            int beginIndex = 0;
            int endIndex = 0;
            String dnt = "";
            String date = "";
            String time = "";
            int firstPeriod = 0;
            int lastPeriod = 0;
            int[] arr = new int[12];
        if (!dateNTime.isEmpty()) {
            while (index < dateNTime.length()) {
//                    if (dateNTime.charAt(index) ==  ' ') dnt = dnt + " ";
//                        s = s + dateNTime.charAt(index);
                switch (dateNTime.charAt(index)) {
                    case ' ':
                        if (index != (dateNTime.length() - 1)) dnt = dnt + " ";
                        firstPeriod = 0;
                        lastPeriod = 0;
                        break;

                    case '-':
                        beginIndex = index + 1;
                        date = dateNTime.substring(index - 3, index);
                        switch (date) {
                            case "Mon":
                                dnt += "0 ";
                                break;
                            case "Tue":
                                dnt += "1 ";
                                break;
                            case "Wed":
                                dnt += "2 ";
                                break;
                            case "Thu":
                                dnt += "3 ";
                                break;
                            case "Fri":
                                dnt += "4 ";
                                break;
                            case "Sat":
                                dnt += "5 ";
                                break;
                            case "Sun":
                                dnt += "6 ";
                                break;
                        }
                        break;

                    case ':':
                        endIndex = index;
                        firstPeriod = Integer.parseInt(dateNTime.substring(beginIndex, endIndex));
                        arr[12 - firstPeriod] = 1;
                        beginIndex = index + 1;
                        break;

                    case ';':
                        endIndex = index;
                        lastPeriod = Integer.parseInt(dateNTime.substring(beginIndex, endIndex));
                        arr[12 - lastPeriod] = 1;
                        break;
                }
                if (firstPeriod * lastPeriod != 0) {
                    for (int i = 0; i < 12; i++) {
                        if ((11-i) < (firstPeriod - 1) || (11-i) > (lastPeriod - 1)) arr[i] = 0;
                        else arr[i] = 1;
                        dnt += arr[i];
                    }
                }
                index++;
            }
        }
        return dnt;
    }
    //    Create menu on Actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_subject,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Set event for button on action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                //      set endWeek...

                if (date_timeView.getText().toString().length() != 0) {
                    calendar.add(Calendar.WEEK_OF_YEAR, Integer.parseInt(numOfWeek.getText().toString()));
                    expDate = calendar.getTime();
                    strExpDate = dateFormat.format(expDate);

                    subject = new Subject(subjectName.getText().toString(),
                            room.getText().toString(),
                            EncoderDateTime(date_timeView.getText().toString()),
                            strDate + "-" + numOfWeek.getText().toString()+ "-" + weekBreak.getText().toString(),
                            teacherName.getText().toString(),
                            teacherPhone.getText().toString(),
                            teacherEmail.getText().toString(),
                            note.getText().toString()
                    );

                    Intent intent = new Intent(AddSubjectActivity.this, MainActivity.class);

                    intent.putExtra("data", subject);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {Toast.makeText(AddSubjectActivity.this, "Input Missing",Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Set daypicker dialog
    View.OnClickListener showDatepicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(year, month, dayOfMonth);
                    startWeek.setText(Integer.toString(calendar.get(Calendar.WEEK_OF_YEAR)));
                    date = calendar.getTime();
                    strDate = dateFormat.format(date);
                }
            };

            String strArrtmp[] = strDate.split("/");
            int ngay = Integer.parseInt(strArrtmp[0]);
            int thang = Integer.parseInt(strArrtmp[1]) - 1;
            int nam = Integer.parseInt(strArrtmp[2]);
            //Hiển thị ra Dialog
            DatePickerDialog pic = new DatePickerDialog(
                    AddSubjectActivity .this,
                    callback, nam, thang, ngay);
            pic.setTitle("Chọn tuần bắt đầu");
            pic.show();
        }
    };

}



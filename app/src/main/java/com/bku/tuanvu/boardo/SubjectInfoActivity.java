package com.bku.tuanvu.boardo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.support.v7.widget.Toolbar;
import android.widget.Switch;
import android.widget.Toast;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class SubjectInfoActivity extends AppCompatActivity implements setTimeDialog.DialogListener {



    Database database;

    EditText subjectName, teacherName, teacherPhone, teacherEmail, note, room, time, startWeek, numOfWeek, brkWeek ;
    ImageButton  editBtn, delBtn, saveBtn;

    Toolbar toolbar;
    Subject s;
    ArrayList<Subject> list;
    Button setTimeBtn;
    Calendar calendar;
    String strDate;
    Date date, getDate;
    SimpleDateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int id = -1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_info);

        subjectName = findViewById(R.id.Name);
        room = findViewById(R.id.edit_room);
        note = findViewById(R.id.editNote);
        teacherPhone = findViewById(R.id.editPhone);
        teacherName = findViewById(R.id.editNameTeacher);
        teacherEmail = findViewById(R.id.editEmail);
        editBtn = findViewById(R.id.editBtn);
        delBtn = findViewById(R.id.delBtn);
        saveBtn = findViewById(R.id.btnSave);
        setTimeBtn = findViewById(R.id.buttonSetTime);
        time = findViewById(R.id.timeDayView);
        startWeek = findViewById(R.id.editStartingWeek);
        numOfWeek = findViewById(R.id.editNumWeek);
        brkWeek = findViewById(R.id.editWeekBreak);


        subjectName.setFocusable(false);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableEditing();
                saveBtn.setVisibility(View.VISIBLE);
            }
        });

        //        set week
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        date = calendar.getTime();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        int crntWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        strDate = dateFormat.format(date);
//        startWeek.setText(Integer.toString(crntWeek));
        startWeek.setOnClickListener(showDatepicker);

        database = new Database(this);
        Intent intent = getIntent();
        if (intent != null) {
            final Bundle bundle1 = intent.getBundleExtra(MainActivity.BUNDLE1);
            if (bundle1 != null) {
                id = bundle1.getInt(MainActivity.ID);
                s = database.getSubjectbyID(id);

                String strArrWeek[] = s.getWeek().split("-");
                try {
                    getDate = dateFormat.parse(strArrWeek[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cld = new GregorianCalendar();
                cld.setTime(getDate);
                int startingWeek = cld.get(Calendar.WEEK_OF_YEAR);


                s.setId(id);
                subjectName.setText(s.getName());
                room.setText(s.getAddress());
                note.setText(s.getNote());
                teacherPhone.setText(s.getPhoneNumber());
                teacherName.setText(s.getTeacher());
                teacherEmail.setText(s.getEmail());
                time.setText(Decoder2TimenDay(s));
                numOfWeek.setText(strArrWeek[1]);
                startWeek.setText(Integer.toString(startingWeek));
                brkWeek.setText(strArrWeek[2]);

            }
        }




        brkWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBreakWeekDialog();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s.setWeek(strDate + "-" + numOfWeek.getText().toString() + "-" + brkWeek.getText().toString());
                s.setName(subjectName.getText().toString());
                s.setAddress(room.getText().toString());
                s.setEmail(teacherEmail.getText().toString());
                s.setTeacher(teacherName.getText().toString());
                s.setPhoneNumber(teacherPhone.getText().toString());
                s.setNote(note.getText().toString());
                s.setTime(EncoderDateTime(time.getText().toString()));
                database.editSubject(s);
                Toast.makeText(SubjectInfoActivity.this, "Edited!", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(SubjectInfoActivity.this, MainActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent2);
                finish();
            }
        });

        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();

            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deleteSubject(s);
                Toast.makeText(SubjectInfoActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(SubjectInfoActivity.this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Subject Detail");
        // Create Back button on actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    void EnableEditing() {
        subjectName.setFocusableInTouchMode(true);
        subjectName.setFocusable(true);
        teacherName.setFocusableInTouchMode(true);
        teacherName.setFocusable(true);
        teacherEmail.setFocusableInTouchMode(true);
        teacherEmail.setFocusable(true);
        teacherPhone.setFocusableInTouchMode(true);
        teacherPhone.setFocusable(true);
        note.setFocusableInTouchMode(true);
        note.setFocusable(true);
        startWeek.setEnabled(true);
        numOfWeek.setFocusableInTouchMode(true);
        numOfWeek.setFocusable(true);
        numOfWeek.setEnabled(true);
        setTimeBtn.setEnabled(true);
        brkWeek.setEnabled(true);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_subject_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.action_edit:
//                EnableEditing();
//                return true ;
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    String Decoder2TimenDay(Subject subject){
        String dateNTime = "";
        String date = "" ;
        int firstPeriod = 0;
        int lastPeriod = 0;
        int time = 0;
        int n = 0;

        n = subject.getTotalClass();
        for (int i =1; i<= n; i++){

            switch (subject.weekdayToInt(i)){
                case 0:
                    date = "Mon";
                    break;
                case 1:
                    date = "Tue";
                    break;
                case 2:
                    date = "Wed";
                    break;
                case 3:
                    date = "Thu";
                    break;
                case 4:
                    date = "Fri";
                    break;
                case 5:
                    date = "Sat";
                    break;
                case 6:
                    date = "Sun";
                    break;
            }
            time = subject.periodToInt(i);
            for (int j=1; j <= 12; j++){
                if ((time & 1)== 1) {
                    firstPeriod = j;
                    break;
                }
                else {
                    int i1 = time >> 1;
                    time = i1;
                }
            }
            long t = Long.parseLong(Long.toBinaryString(subject.periodToInt(i)));
            lastPeriod = (int)(Math.log10((double)t)+1);
            dateNTime += date + "-" + firstPeriod + ":" + lastPeriod + "; ";
        }
        return dateNTime;
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
        time.setText(dateAndTime);

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

    // showdate picker
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
                    SubjectInfoActivity.this,
                    callback, nam, thang, ngay);
            pic.setTitle("Chọn tuần bắt đầu");
            pic.show();
        }
    };

    private void showBreakWeekDialog() {
//        break_week_dialog weekDialog = new break_week_dialog();
//        weekDialog.show(getSupportFragmentManager(), "week dialog");
        // Prepare grid view
        final GridView gridView = new GridView(this);

        List<Integer> mList = new ArrayList<>();
        for (int i = Integer.parseInt(startWeek.getText().toString()); i < (Integer.parseInt(startWeek.getText().toString())+Integer.parseInt(numOfWeek.getText().toString())); i++) {
            mList.add(i);
        }
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        gridView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, mList));
        gridView.setNumColumns(3);

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
                        brkWeek.setText(string);
                    }
                }
                if (gridView.getCheckedItemCount() == 0) brkWeek.setText("N/A");

            }
        });

        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}

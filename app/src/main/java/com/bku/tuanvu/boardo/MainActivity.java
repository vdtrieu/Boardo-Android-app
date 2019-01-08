package com.bku.tuanvu.boardo;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String BUNDLE1 = "bundle1";
    public static final String ID = "id";
    public static final String TIMEVIEW = "timeview";

    private String mainTittle,  timedate;
    private Subject subject;
     private    Database data ;
    private TextView subjectView, dateView, mon1;
    ArrayList<Subject> listSubject;
    Date date, expDate, stDate;
    Calendar cld;
    ConstraintLayout scheduleView;
    LayoutTransition transition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleView = findViewById(R.id.Schedule);

        transition = new LayoutTransition();

        //tạo sự kiện cho CreateButton
        final ImageButton CreateBtn = findViewById(R.id.CreateButton);
        CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSubjectActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        //get the current week of the year
        cld = new GregorianCalendar();
        cld.setFirstDayOfWeek(Calendar.MONDAY);
        date = cld.getTime();
        int weekOfYear = cld.get(Calendar.WEEK_OF_YEAR);

        //set tittle
        mainTittle= getResources().getText(R.string.week) +" "+ weekOfYear;
        setTitle(mainTittle);


        final Intent intent = getIntent();
        subject = (Subject) intent.getSerializableExtra("data");

        data = new Database(this);

        if (subject != null) {
            data.addSubject(subject);
        }

        // create Schedule
        CreateSchedule(data);
    }
        int Decode2First(int time){
        int firstPeriod = -1;
        for (int i=1; i <= 12; i++){
            if ((time & 1)== 1) {
                firstPeriod = i;
                break;
            }
            else {
                int i1 = time >> 1;
                time = i1;
            }
        }
        return firstPeriod;
    }
    int Decode2Last(int time){
        long t = Long.parseLong(Long.toBinaryString(time));
        int lastPeriod = (int)(Math.log10((double)t)+1);

        return lastPeriod;
    }
//  get hour of each subject
    String HourOfSubject( Subject s){
       int num = s.getTotalClass();
       Calendar cal = new GregorianCalendar();

       String hour = "";
       String w = "hour";
       for (int i =1; i <= num; i++){
           int fstperiod = Decode2First(s.periodToInt(i));
           if (fstperiod < 7){
               cal.set(Calendar.HOUR_OF_DAY,6);
               cal.set(Calendar.MINUTE, 30);
               cal.add(Calendar.MINUTE, 45*(fstperiod - 1));
               if (fstperiod > 2 && fstperiod < 6) cal.add(Calendar.MINUTE, (fstperiod-2)*10);
           }
           else {
               cal.set(Calendar.HOUR_OF_DAY,12);
               cal.set(Calendar.MINUTE, 30);
               cal.add(Calendar.MINUTE, 45*(fstperiod-7));
               if (fstperiod > 8 && fstperiod < 12) cal.add(Calendar.MINUTE, (fstperiod-8)*10);
           }
           hour += cal.get(Calendar.HOUR_OF_DAY) + " ";
       }

//       Log.i(w,hour);

       return hour;
    }
//     get minute of each subject
    String MinuteOfSubJect(Subject s){
        int num = s.getTotalClass();
        Calendar cal = new GregorianCalendar();
        String min = "";
        String w = "min";
        for (int i =1; i <= num; i++){
            int fstperiod = Decode2First(s.periodToInt(i));
            if (fstperiod < 7){
                cal.set(Calendar.HOUR_OF_DAY,6);
                cal.set(Calendar.MINUTE, 30);
                cal.add(Calendar.MINUTE, 45*(fstperiod - 1));
                if (fstperiod > 2 && fstperiod < 6) cal.add(Calendar.MINUTE, (fstperiod-2)*10);
            }
            else {
                cal.set(Calendar.HOUR_OF_DAY,12);
                cal.set(Calendar.MINUTE, 30);
                cal.add(Calendar.MINUTE, 45*(fstperiod-7));
                if (fstperiod > 8 && fstperiod < 12) cal.add(Calendar.MINUTE, (fstperiod-8)*10);
            }
            min += cal.get(Calendar.MINUTE) + " ";
        }

//        Log.i(w,min);

        return min;
    }
//    Create menu on Actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Set event for button on action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                if ( data!= null && listSubject.size() != 0) {
                    data.deleteAllSubject();
                    Toast.makeText(MainActivity.this, " Deleted all!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MainActivity.this, "Nothing to delete!",Toast.LENGTH_SHORT).show();
//                refresh
                getIntent().removeExtra("data");
                recreate();
                break;
            case R.id.action_backward:
                int nextWeek;
                cld.add(Calendar.WEEK_OF_YEAR,-1);
                nextWeek = cld.get(Calendar.WEEK_OF_YEAR);
                mainTittle= getResources().getText(R.string.week) +" "+ nextWeek;
                setTitle(mainTittle);
//                re-create Schedule
                scheduleView.removeAllViews();

                scheduleView.setLayoutTransition(transition);
                date = cld.getTime();
                CreateSchedule(data);
                break;
            case R.id.action_forward:
                cld.add(Calendar.WEEK_OF_YEAR, 1);
                int preWeek;
                preWeek = cld.get(Calendar.WEEK_OF_YEAR);
                mainTittle= getResources().getText(R.string.week) +" "+ preWeek;
                setTitle(mainTittle);
//                re-Create Schedule

                scheduleView.setLayoutTransition(transition);
                scheduleView.removeAllViews();
                date = cld.getTime();
                CreateSchedule(data);
                break;
            case R.id.action_refresh:
                scheduleView.setLayoutTransition(transition);
                scheduleView.removeAllViews();
                getIntent().removeExtra("data");
                recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    void CreateSchedule(Database data){
        if(data != null) listSubject = data.getAllSubject();

        ConstraintSet setPos = new ConstraintSet();


        dateView = findViewById(R.id.monday);
//        mon1 = findViewById(R.id.mon1);

        if (listSubject != null) {
            for (final Subject s : listSubject) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String ArrStrWeek[] = s.getWeek().split("-");
                try {
                    stDate = dateFormat.parse(ArrStrWeek[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(stDate);
                int numWeek = Integer.parseInt(ArrStrWeek[1]);

                String arrStrWeekBrk[] = ArrStrWeek[2].split(", ");
                List<String> list = Arrays.asList(arrStrWeekBrk);
                boolean isBrkWeek = list.contains(Integer.toString(cld.get(Calendar.WEEK_OF_YEAR)));
                boolean isNotHasBrkWk = list.contains("N/A");
                if (isNotHasBrkWk) {
                    calendar.add(Calendar.WEEK_OF_YEAR, numWeek );
                } else {
                    calendar.add(Calendar.WEEK_OF_YEAR, numWeek + arrStrWeekBrk.length);
                }
                expDate = calendar.getTime();


                if (!date.after(expDate) && !date.before(stDate)){
                    int NthClass;
                    if (s.getTime() != null) {
                        NthClass = s.getTotalClass();
                        for (int i = 1; i <= NthClass; i++) {


                            subjectView = new TextView(this);
                            subjectView.setGravity(Gravity.CENTER);
                            subjectView.setId(View.generateViewId());
                            String name = s.getName();
                            String arrStrAds[] = s.getAddress().split("; ");
                            if (s.getAddress().length() != 0) name += "\n " + arrStrAds[i-1 ];
                            if(isBrkWeek) {
                                name += "\n" + "(Tạm nghỉ)";
                            }
                            subjectView.setText(name);
                            subjectView.setHeight(getResources().getDimensionPixelSize(R.dimen.periodHeight)
                                    * (Decode2Last(s.periodToInt(i)) - Decode2First(s.periodToInt(i)) + 1)
                            );
                            if (subjectView.getText().toString() == "") subjectView.setVisibility(View.INVISIBLE);
                            subjectView.setBackground(getResources().getDrawable(R.drawable.subject_shape));
                            subjectView.setWidth(getResources().getDimensionPixelSize(R.dimen.day));
                            scheduleView.addView(subjectView);
                            setPos.clone(scheduleView);

                            setPos.connect(subjectView.getId(),
                                    ConstraintSet.TOP,
                                     ConstraintSet.PARENT_ID,
                                    ConstraintSet.TOP,
                                    getResources().getDimensionPixelOffset(R.dimen.periodHeight) * (Decode2First(s.periodToInt(i)) - 1) );
                            setPos.connect(subjectView.getId(),
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    (getResources().getDimensionPixelOffset(R.dimen.day)  + getResources().getDimensionPixelOffset(R.dimen.minor_margin))* s.weekdayToInt(i)
                            );
                            setPos.applyTo(scheduleView);

                            subjectView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent1 = new Intent(MainActivity.this, SubjectInfoActivity.class);
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putInt(ID, s.getId());
                                    bundle1.putString(TIMEVIEW, timedate);
                                    intent1.putExtra(BUNDLE1, bundle1);
                                    startActivity(intent1);
                                }
                            });

                        }
                    }
                }
            }
        }
    }
}



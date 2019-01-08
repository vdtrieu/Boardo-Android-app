package com.bku.tuanvu.boardo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Tuan Vu on 23/03/2018.
 */

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "subject_list";
    private static final String TABLE_NAME = "subject";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String TIME = "time";
    private static final String WEEK = "week";
    private static final String TEACHER = "teacher";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String EMAIL = "email";
    private static final String NOTE = "note";


    private Context context;
    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID +" integer primary key autoincrement, " +
                NAME +" TEXT, "+
                ADDRESS + " TEXT, "+
                TIME + " TEXT, "+
                WEEK + " TEXT, "+
                TEACHER + " TEXT, "+
                PHONE_NUMBER + " TEXT, "+
                EMAIL + " TEXT, "+
                NOTE + " TEXT)";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
        Toast.makeText(context, "Drop successfully", Toast.LENGTH_SHORT).show();
    }

    //Add new Subject
    public void addSubject(Subject subject){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, subject.getName());
        values.put(ADDRESS, subject.getAddress());
        values.put(TIME, subject.getTime());
        values.put(WEEK, subject.getWeek());
        values.put(TEACHER, subject.getTeacher());
        values.put(PHONE_NUMBER, subject.getPhoneNumber());
        values.put(EMAIL, subject.getEmail());
        values.put(NOTE, subject.getNote());

        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    //Select a student by ID
    public Subject getSubjectbyID(int id){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                new String[] { ID, NAME, ADDRESS, TIME, WEEK, TEACHER, PHONE_NUMBER,EMAIL,NOTE},
                ID + "=?",
                new String[] { String.valueOf(id)}, null, null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        Subject subject = new Subject(  cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8));

        cursor.close();
        database.close();
        return  subject;
    }

    //Select all

    public ArrayList<Subject> getAllSubject(){
        ArrayList<Subject> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if(cursor.moveToFirst()){
            do{
                Subject subject = new Subject();
                subject.setId(cursor.getInt(0));
                subject.setName(cursor.getString(1));
                subject.setAddress(cursor.getString(2));
                subject.setTime(cursor.getString(3));
                subject.setWeek(cursor.getString(4));
                subject.setTeacher(cursor.getString(5));
                subject.setPhoneNumber(cursor.getString(6));
                subject.setEmail(cursor.getString(7));
                subject.setNote(cursor.getString(8));

                list.add(subject);
            }while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    //Edit
    public void editSubject(Subject subject){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, subject.getName());
        values.put(ADDRESS, subject.getAddress());
        values.put(TIME, subject.getTime());
        values.put(WEEK, subject.getWeek());
        values.put(TEACHER, subject.getTeacher());
        values.put(PHONE_NUMBER, subject.getPhoneNumber());
        values.put(EMAIL, subject.getEmail());
        values.put(NOTE, subject.getNote());

        database.update(TABLE_NAME, values, ID+"=? ", new String[] { String.valueOf(subject.getId())});
        database.close();
    }

    //Delete
    public void deleteSubject(Subject subject){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, ID + "=? ", new String[]{ String.valueOf(subject.getId())});
        database.close();
    }

    //Delete all Subject
    public void deleteAllSubject(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
        database.delete("SQLITE_SEQUENCE","NAME = ?", new String[]{TABLE_NAME});
        database.close();
    }

}
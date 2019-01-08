package com.bku.tuanvu.boardo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by ADMIN on 23-03-18.
 */

public class setTimeDialog extends AppCompatDialogFragment {
    private EditText fstPeriod, lstPeriod, timedayView, room, editRoom;
    private Spinner spnCategory;
    private DialogListener listener;
    private ListView listTime ;
    private Button addButton, updateButton, setTDRBtn;
    ArrayList<String > arrTime = null;
    ArrayAdapter<String> adapterT = null;
    int itemPos = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        final View view= inflater.inflate(R.layout.dialog_set_time,null);

        listTime = view.findViewById(R.id.listTimeView);
        addButton = view.findViewById(R.id.addBtn);
        updateButton = view.findViewById(R.id.updateButton);
        spnCategory = view.findViewById(R.id.setDay);
        fstPeriod = view.findViewById(R.id.firstPeriod);
        lstPeriod= view.findViewById(R.id.lastPeriod);
        timedayView = getActivity().findViewById(R.id.timeDayView);
        room = view.findViewById(R.id.edit_room);
        editRoom = getActivity().findViewById(R.id.edit_room);
        setTDRBtn = getActivity().findViewById(R.id.buttonSetTime);

        arrTime = new ArrayList<>();
        int a = timedayView.getText().toString().length();
        int b = editRoom.getText().toString().length();
        if ((a != 0) && (b != 0)) {
            String arrStrTD[] = timedayView.getText().toString().split("; ");
            String arrStrR[] = editRoom.getText().toString().split("; ");
            String arrStrTDR[] = new String[arrStrTD.length] ;
            for (int i = 0 ; i < arrStrTD.length ; i++){
                arrStrTDR[i] = arrStrTD[i] + " " + arrStrR[i];
            }
            arrTime.addAll(Arrays.asList(arrStrTDR));
        }



        adapterT = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                arrTime);
        listTime.setAdapter(adapterT);

        listTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateButton.setVisibility(View.VISIBLE);
                String ItemStringArray[] = arrTime.get(position).split(" ");
                String StrPerDayArr[] = ItemStringArray[0].split("-");
                String PerArray[] = StrPerDayArr[1].split(":");
                fstPeriod.setText(PerArray[0]);
                lstPeriod.setText(PerArray[1]);
                room.setText(ItemStringArray[1]);
                itemPos = position;
            }
        });

        listTime.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                arrTime.remove(position);
                adapterT.notifyDataSetChanged();
                Toast.makeText(getContext(), "Removed!", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(fstPeriod.getText().toString(), "") && !Objects.equals(lstPeriod.getText().toString(), "") && !Objects.equals(room.getText().toString(), "")) {
                    arrTime.set(itemPos,
                                spnCategory.getSelectedItem().toString() + "-"
                                        + fstPeriod.getText().toString() + ":"
                                        + lstPeriod.getText().toString() + " "
                                        + room.getText().toString()
                                );
                } else { Toast.makeText(getContext(), "Input Missing", Toast.LENGTH_SHORT).show();
                }
                fstPeriod.setText("");
                lstPeriod.setText("");
                room.setText("");
                adapterT.notifyDataSetChanged();
                updateButton.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(fstPeriod.getText().toString(), "") && !Objects.equals(lstPeriod.getText().toString(), "" )&& !Objects.equals(room.getText().toString(),"")) {
                    if (Integer.parseInt(fstPeriod.getText().toString()) <= Integer.parseInt(lstPeriod.getText().toString()) ) {
                        arrTime.add(spnCategory.getSelectedItem().toString() + "-"
                                + fstPeriod.getText().toString() + ":"
                                + lstPeriod.getText().toString() + " "
                                + room.getText().toString()
                        );
                    }else Toast.makeText(getContext(), "Wrong Input !",Toast.LENGTH_SHORT).show();
                    fstPeriod.setText("");
                    lstPeriod.setText("");
                    room.setText("");
                    adapterT.notifyDataSetChanged();
                }
                else Toast.makeText(getContext(),"Input Missing!",Toast.LENGTH_SHORT).show();
            }
        });



        String[] day = getResources().getStringArray(R.array.listDays);
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,day);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });



        builder.setView(view)
                .setTitle("Thời gian học")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            String dateNtime = "";
                            String strRoom = "";
                        if (arrTime.size() != 0) {
                            for ( int i = 0; i < arrTime.size() ; i++){
                                String ArrStrTime[] = arrTime.get(i).split(" ");
                                dateNtime += ArrStrTime[0] + "; ";
                                strRoom += ArrStrTime[1] + "; ";
                            }
                        }
                        else Toast.makeText(getContext(), "Input Missing!", Toast.LENGTH_SHORT).show();

                        listener.applyTexts_dateNtime(dateNtime);
                        listener.applyTexts_room(strRoom);

                    }
                })
                .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement DialogListener"
            ) ;
        }
    }

    public interface DialogListener{
        void applyTexts_dateNtime(String dateAndTime);
        void applyTexts_room(String room);

    }
}

package com.edumaite.adam.edumiate_poc;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class HomeworkFragment extends Fragment implements View.OnClickListener{


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public HomeworkFragment() {
        // Required empty public constructor
    }

    public static HomeworkFragment newInstance(String param1, String param2) {
        HomeworkFragment fragment = new HomeworkFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_homework, container, false);
        Button t = (Button) v.findViewById(R.id.homework_submit);
        t.setOnClickListener(this);
        Button s = (Button) v.findViewById(R.id.homework_duedate);
        t.setOnClickListener(this);


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == R.id.homework_submit){
            EditText message = (EditText)getActivity().findViewById(R.id.homework_message);
            EditText title = (EditText)getActivity().findViewById(R.id.homework_title);

            if(message != null) message.setText("");
            if(title != null) message.setText("");

        } else if (id == R.id.homework_duedate){
            Toast.makeText(getContext(), "duedate button selected ", Toast.LENGTH_LONG).show();
            DialogFragment newFragment = new SelectDateFragment();
            newFragment.show(getFragmentManager(), "DatePicker");

        }else{
            Toast.makeText(getContext(), "Something was clicked " +id, Toast.LENGTH_LONG).show();
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package com.edumaite.adam.edumiate_poc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserSelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSelectionFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private static MainActivity activity;

    public UserSelectionFragment() {
        // Required empty public constructor
    }
    public static UserSelectionFragment newInstance() {
        UserSelectionFragment fragment = new UserSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.user_selection_fragment, container, false);
        Button t = (Button) v.findViewById(R.id.teacher_button);
        Button s = (Button) v.findViewById(R.id.student_button);
        t.setOnClickListener(this);
        s.setOnClickListener(this);

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
        switch(v.getId()){
            case R.id.teacher_button:
                Toast.makeText(getContext(), "Clicked on teacher button", Toast.LENGTH_LONG).show();
                activity.user = "teacher";
                Class tfrag = TeacherSplash.class;
                activity.replaceFragments(tfrag);
                activity.setTitle("Edumaite - Teacher");
                break;
            case R.id.student_button:
                Toast.makeText(getContext(), "Clicked on student button", Toast.LENGTH_LONG).show();
                activity.user = "student";
                Class sfrag = StudentSplash.class;
                activity.replaceFragments(sfrag);
                activity.setTitle("Edumaite - Student");
                break;
        }

        activity.changeUserView();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

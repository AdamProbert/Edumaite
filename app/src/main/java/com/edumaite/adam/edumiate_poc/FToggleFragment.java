package com.edumaite.adam.edumiate_poc;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edumaite.adam.edumiate_poc.adapters.AppAdapter;
import com.edumaite.adam.edumiate_poc.adapters.RecyclerItemClickListener;
import com.edumaite.adam.edumiate_poc.db.AppViewModel;
import com.edumaite.adam.edumiate_poc.models.App;

import java.util.List;
import java.util.Objects;


public class FToggleFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    private AppViewModel mAppViewModel;
    private AppAdapter mAppAdapter;

    public FToggleFragment() {
        // Required empty public constructor
    }


    public static FToggleFragment newInstance() {
        FToggleFragment fragment = new FToggleFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.toggle_fragment, container, false);

        RecyclerView recyclerView = fragView.findViewById(R.id.recyclerview);
        mAppAdapter = new AppAdapter(getContext());
        recyclerView.setAdapter(mAppAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        mAppViewModel.getAllApps().observe(this, new Observer<List<App>>() {
            @Override
            public void onChanged(@Nullable final List<App> apps) {
                // Update the cached copy of the words in the adapter.
                mAppAdapter.setApps(apps);
            }
        });


        return fragView;
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
//        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

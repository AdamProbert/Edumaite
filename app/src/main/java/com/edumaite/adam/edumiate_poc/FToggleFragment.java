package com.edumaite.adam.edumiate_poc;

import android.app.Application;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.edumaite.adam.edumiate_poc.adapters.AppAdapter;
import com.edumaite.adam.edumiate_poc.adapters.RecyclerItemClickListener;
import com.edumaite.adam.edumiate_poc.db.AppViewModel;
import com.edumaite.adam.edumiate_poc.models.App;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class FToggleFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    private AppViewModel mAppViewModel;
    private AppAdapter mAppAdapter;
    private static Application app;
    private static Context context;

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
        context = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.toggle_fragment, container, false);

        RecyclerView recyclerView = fragView.findViewById(R.id.recyclerview);

        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        Application app = getActivity().getApplication();
        mAppAdapter = new AppAdapter(getContext(), mAppViewModel);

        recyclerView.setAdapter(mAppAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mAppViewModel.getAllApps().observe(this, new Observer<List<App>>() {
            @Override
            public void onChanged(@Nullable final List<App> apps) {
                Log.i("adam", "ToggleFragment on changed called");
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
        Log.i("adam", "On detach called from FToggleFragment");
        super.onDetach();
        //        mListener = null;
        List<App> apps2 = mAppAdapter.getApps();
        for(App app: apps2) {
            mAppViewModel.insert(app);

        }
        mAppAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("adam", "togglefragment.onSaveInstanceState called");
        List<App> apps2 = mAppAdapter.getApps();
        for(App app: apps2) {
            mAppViewModel.insert(app);

        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

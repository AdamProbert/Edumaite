package com.edumaite.adam.edumiate_poc;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.edumaite.adam.edumiate_poc.adapters.AppAdapter;
import com.edumaite.adam.edumiate_poc.dataCollection.AppCollector;
import com.edumaite.adam.edumiate_poc.db.AppViewModel;
import com.edumaite.adam.edumiate_poc.models.App;

import java.util.List;


public class FToggleFragment extends Fragment implements AdapterView.OnItemClickListener {

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
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.Planets, android.R.layout.simple_list_item_1);
//        setListAdapter(adapter);
//        getListView().setOnItemClickListener(this);
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
        final AppAdapter mAppAdapter = new AppAdapter(getContext());
        recyclerView.setAdapter(mAppAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //List<ResolveInfo> apps = new AppCollector(getContext()).getInstalledApps();
        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);


        App mApp = new App("Test app", "test image");
        mAppViewModel.insert(mApp);


        mAppViewModel.getAllApps().observe(this, new Observer<List<App>>() {
            @Override
            public void onChanged(@Nullable final List<App> apps) {
                // Update the cached copy of the words in the adapter.
                mAppAdapter.setApps(apps);
            }
        });


        return fragView;
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
//        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

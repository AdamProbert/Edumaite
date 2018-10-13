package com.edumaite.adam.edumiate_poc.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.edumaite.adam.edumiate_poc.models.App;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private EdumaiteRepository mRepository;

    private LiveData<List<App>> mAllApps;

    public AppViewModel(Application application) {
        super(application);
        mRepository = new EdumaiteRepository(application);
        mAllApps = mRepository.getAllApps();
    }

    public LiveData<List<App>> getAllApps() { return mAllApps; }

    public void insert(App app) { mRepository.insert(app); }

}
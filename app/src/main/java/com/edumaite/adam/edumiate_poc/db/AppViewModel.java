package com.edumaite.adam.edumiate_poc.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.edumaite.adam.edumiate_poc.models.App;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private EdumaiteRepository mRepository;
    private LiveData<List<App>> mAllApps;
    private LiveData<List<App>> mAllBlockedApps;



    public AppViewModel(Application application) {
        super(application);
        mRepository = new EdumaiteRepository(application);
        mAllApps = mRepository.getAllApps();
        mAllBlockedApps = mRepository.getMyBlacklistApps();
    }

    public LiveData<List<App>> getAllApps() { return mAllApps; }

    public LiveData<List<App>> getAllBlockedApps() { return mAllBlockedApps; }


    public void insert(App app) { mRepository.insert(app); }

}
package com.edumaite.adam.edumiate_poc.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.edumaite.adam.edumiate_poc.models.App;

import java.util.List;

public class EdumaiteRepository {


    private AppDao myAppDao;
    private LiveData<List<App>> myAllApps;

    public EdumaiteRepository(Application application){
        EdumaiteDB db = EdumaiteDB.getDatabase(application);
        myAppDao = db.appDao();
        myAllApps = myAppDao.getAllApps();

    }

    LiveData<List<App>> getAllApps() {
        return myAllApps;
    }

    public void insert (App app) {
        new insertAppSyncTask(myAppDao).execute(app);
    }

    private static class insertAppSyncTask extends AsyncTask<App, Void, Void> {

        private AppDao appDao;

        insertAppSyncTask(AppDao dao) {
            appDao = dao;
        }

        @Override
        protected Void doInBackground(final App... params) {
            appDao.insert(params[0]);
            return null;
        }
    }
}






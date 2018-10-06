package com.edumaite.adam.edumiate_poc.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.edumaite.adam.edumiate_poc.models.App;

@Database(entities = {App.class}, version = 1, exportSchema = false)
public abstract class EdumaiteDB extends RoomDatabase{
    public abstract AppDao appDao();


    private static volatile EdumaiteDB INSTANCE;

    static EdumaiteDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EdumaiteDB.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EdumaiteDB.class, "edumaite_db")
                            .addCallback(sEdumaiteDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static EdumaiteDB.Callback sEdumaiteDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDao mAppDao ;

        PopulateDbAsync(EdumaiteDB db) {
            mAppDao = db.appDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAppDao.deleteAll();
            App app = new App("Facebook", "Image");
            mAppDao.insert(app);
            app = new App("Youtube", "Image");
            mAppDao.insert(app);
            return null;
        }
    }
}
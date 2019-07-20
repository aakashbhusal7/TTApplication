package com.example.ttapp.db.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ttapp.R;
import com.example.ttapp.db.room.dao.PictureDao;
import com.example.ttapp.db.room.dao.ProfileDao;
import com.example.ttapp.db.room.entity.PictureEntity;
import com.example.ttapp.db.room.entity.ProfileEntity;

@Database(entities = {ProfileEntity.class, PictureEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, context.getString(R.string.profile_table_name))
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;

    }

    public abstract ProfileDao profileDao();

    public abstract PictureDao pictureDao();
}

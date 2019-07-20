package com.example.ttapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.ttapp.db.room.dao.ProfileDao;
import com.example.ttapp.db.room.entity.ProfileEntity;
import com.example.ttapp.utils.DatabaseManager;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Flowable;

public class ProfileViewModel extends AndroidViewModel {

    private DatabaseManager databaseManager;
    private Flowable<List<ProfileEntity>>profileEntityList;
    private ProfileDao profileDao;

    public ProfileViewModel(Application application){
        super(application);
        databaseManager=new DatabaseManager(application);
    }

    public Flowable<List<ProfileEntity>>getProfileEntityList(){
        if(profileEntityList==null){
            profileEntityList=databaseManager.getAllInfo();
        }
        return profileEntityList;
    }

    public void insertDatabase(ProfileEntity profileEntity){
        databaseManager.insertInfo(profileEntity);
    }

    public ProfileEntity getDatabaseById(int id)throws ExecutionException,InterruptedException{
        return databaseManager.getId(id);
    }

    public void deleteAll(){
        databaseManager.deleteDatabase();
    }

}

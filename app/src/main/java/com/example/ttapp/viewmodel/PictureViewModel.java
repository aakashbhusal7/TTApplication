package com.example.ttapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.ttapp.db.room.dao.PictureDao;
import com.example.ttapp.db.room.entity.PictureEntity;
import com.example.ttapp.utils.DatabaseManager;

import java.util.List;

import io.reactivex.Flowable;

public class PictureViewModel extends AndroidViewModel {

    private DatabaseManager databaseManager;
    private Flowable<List<PictureEntity>>pictureEntityList;
    private PictureDao pictureDao;

    public PictureViewModel(Application application){
        super(application);
        databaseManager=new DatabaseManager(application);
    }

    public Flowable<List<PictureEntity>>getPictureEntityList(){
        if(pictureEntityList==null){
            pictureEntityList=databaseManager.getPicture();
        }
        return pictureEntityList;
    }

    public void insertPicture(PictureEntity pictureEntity){
        databaseManager.insertPicture(pictureEntity);
    }
}

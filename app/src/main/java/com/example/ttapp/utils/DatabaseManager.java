package com.example.ttapp.utils;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ttapp.db.room.AppDatabase;
import com.example.ttapp.db.room.dao.PictureDao;
import com.example.ttapp.db.room.dao.ProfileDao;
import com.example.ttapp.db.room.entity.PictureEntity;
import com.example.ttapp.db.room.entity.ProfileEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DatabaseManager {

    private static final String TAG=DatabaseManager.class.getSimpleName();
    private ProfileDao profileDao;
    private PictureDao pictureDao;
    private int id;
    private Flowable<List<ProfileEntity>> profileEntity;
    private Flowable<List<PictureEntity>>pictureEntity;
    private CompositeDisposable compositeDisposable;

    public DatabaseManager(Application application){
        AppDatabase appDatabase=AppDatabase.getDatabase(application);
        profileDao=appDatabase.profileDao();
        pictureDao=appDatabase.pictureDao();
        pictureEntity=pictureDao.getPicture();
        profileEntity=profileDao.getAllInfo();
    }

    public Flowable<List<ProfileEntity>>getAllInfo(){
        return profileEntity;
    }

    public Flowable<List<PictureEntity>>getPicture(){
        return pictureEntity;
    }

    public ProfileEntity getId(int id) throws ExecutionException,InterruptedException{
        return new getDbAsync(profileDao).execute(id).get();
    }

    public void insertInfo(final ProfileEntity profileEntity){
        Completable.fromAction(()->
                profileDao.insertInfo(profileEntity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        id=profileEntity.getId();
                        Log.d(TAG,"ON COMPLETE");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG,"ON error");
                    }
                });

    }

    public void insertPicture(final PictureEntity pictureEntity){
        Completable.fromAction(()->
                pictureDao.insertInfo(pictureEntity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"ON pic complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG,"On pic error");
                    }
                });
    }

    public void deleteDatabase(){
        Completable.fromAction(()->
                profileDao.deleteAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private static class getDbAsync extends AsyncTask<Integer,Void,ProfileEntity> {
        private ProfileDao profileDaoAsync;
        getDbAsync(ProfileDao profileDao){
            profileDaoAsync=profileDao;
        }

        @Override
        protected ProfileEntity doInBackground(Integer... integers) {
            return profileDaoAsync.getInfoById(integers[0]);
        }
    }
}

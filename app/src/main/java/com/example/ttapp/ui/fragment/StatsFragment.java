package com.example.ttapp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttapp.R;
import com.example.ttapp.adapter.StatsAdapter;
import com.example.ttapp.db.room.entity.ProfileEntity;
import com.example.ttapp.viewmodel.ProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class StatsFragment extends Fragment {

    DatabaseReference databaseReference;
    private List<ProfileEntity> profileEntityList;
    private ProfileViewModel profileViewModel;
    ProfileEntity profileEntity;
    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable;
    private static final String TAG=StatsFragment.class.getSimpleName();
    FloatingActionButton floatingActionButton;

    TextView textView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //databaseReference= FirebaseDatabase.getInstance().getReference("userProfile");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView=inflater.inflate(R.layout.content_stats,container,false);
        profileEntityList=new ArrayList<>();
        recyclerView=fragmentView.findViewById(R.id.recycler_stats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileViewModel= ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getProfileEntityList().map(new Function<List<ProfileEntity>, Object>() {
            @Override
            public Object apply(List<ProfileEntity> profileEntityList) throws Exception {
                System.out.println("profileEntityList = " + profileEntityList);;
               return profileEntityList;
            }
        });
        Log.d(TAG,"LIST=" +profileViewModel.getProfileEntityList());
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        compositeDisposable=new CompositeDisposable();
        compositeDisposable.add(profileViewModel.getProfileEntityList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<ProfileEntity>>() {
            @Override
            public void accept(List<ProfileEntity> profileEntityList) throws Exception {
                recyclerView.setAdapter(new StatsAdapter(getActivity(),profileEntityList));
            }
        }));
    }
}

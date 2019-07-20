package com.example.ttapp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.ttapp.MainActivity;
import com.example.ttapp.R;
import com.example.ttapp.databinding.NavigationDrawerBinding;
import com.example.ttapp.databinding.NavigationHeaderBinding;
import com.example.ttapp.db.UserGame;
import com.example.ttapp.db.UserProfile;
import com.example.ttapp.db.room.entity.PictureEntity;
import com.example.ttapp.db.room.entity.ProfileEntity;
import com.example.ttapp.ui.fragment.DashboardFragment;
import com.example.ttapp.viewmodel.PictureViewModel;
import com.example.ttapp.viewmodel.ProfileViewModel;
import com.example.ttapp.worker.NotificationHandler;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DashboardFragment.DashboardItemClickListener {

    private NavigationDrawerBinding navigationDrawerBinding;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private Fragment currentFragment;
    CircleImageView circleImageView;
    private FragmentManager fragmentManager;
    private DashboardFragment dashboardFragment;
    private FirebaseAuth firebaseAuth;
    private NavigationHeaderBinding navigationHeaderBinding;
    FirebaseAuth.AuthStateListener authStateListener;
    TextView profileEmail;
    EditText profileName;
    String username;
    private Map<String, UserGame> map;
    private Bundle bundle;
    private CompositeDisposable compositeDisposable;
    private PictureEntity pictureEntity;
    private ProfileEntity profileEntity;
    private String passedKey;
    private int sumWins = 0, sumMatches = 0;
    private String matches, day, wins;
    private PictureViewModel pictureViewModel;
    private DatabaseReference databaseReference;
    private ProfileViewModel profileViewModel;
    private List<UserGame> userGameList;
    private List<String> winList, matchesList;
    private List<PictureEntity> pictureEntityList = new ArrayList<>();
    public static final String KEY_USERNAME="USERNAME";
    public static final String SEND_NOTIFICATION="SEND_NOTIFICATION";
    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_GET_FILE = 2;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST = 105;
    private static final String TAG = DashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationDrawerBinding = DataBindingUtil.setContentView(this, R.layout.navigation_drawer);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        pictureViewModel = ViewModelProviders.of(this).get(PictureViewModel.class);


        userGameList = new ArrayList<>();
        winList = new ArrayList<>();
        matchesList = new ArrayList<>();
        map = new HashMap<>();

        toolbar = findViewById(R.id.toolbar);
        navigationDrawerBinding.navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawerBinding.drawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer);
        navigationDrawerBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);


        actionBarDrawerToggle.syncState();
        fragmentManager = getSupportFragmentManager();
        dashboardFragment = new DashboardFragment();
        //dashboardFragment.setDashboardItemClickListener((DashboardFragment.DashboardItemClickListener) this);
        replaceMainFragment(dashboardFragment);


        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                }
            }
        };

        //get the bundle from login activity.......................................
        bundle = getIntent().getExtras();
        if (bundle != null) {
            //......read data from firebase using the passed key............................................


            passedKey = bundle.getString(SignInActivity.KEY_USERNAME);

            databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference gameRef = databaseReference.
                    child("userProfile")
                    .child(passedKey)
                    .child("userGame");
            gameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        UserGame userGame = dataSnapshot1.getValue(UserGame.class);
                        map = (HashMap<String, UserGame>) dataSnapshot.getValue();
                        winList.add(userGame.getWins());
                        matchesList.add(userGame.getMatches());
                        userGameList = new ArrayList<>(map.values());

                    }
                    Log.d(TAG, "TEST ARRAY LIST= " + winList);
                    for (int i = 0; i < winList.size(); i++) {
                        sumWins = sumWins + Integer.parseInt(winList.get(i));
                    }
                    for (int j = 0; j < matchesList.size(); j++) {
                        sumMatches = sumMatches + Integer.parseInt(matchesList.get(j));
                    }

                    Log.d(TAG, "SUM OF MATCHES= " + sumMatches);
                    Log.d(TAG, "SUM OF WINS= " + sumWins);

                    if(sumWins==25){
                        Log.d(TAG,"REACHed 25");
                        FirebaseUser winnerUser=FirebaseAuth.getInstance().getCurrentUser();
                        Data data= new Data.Builder()
                                .putString(KEY_USERNAME,winnerUser.getEmail().toString())
                                .build();
                        startWorkManager(data);
                    }

                    passStats(sumMatches, sumWins);

                    Collection<UserGame> values = map.values();
                    Log.d(TAG, "Collection= " + values);
                    sumWins = 0;
                    sumMatches = 0;
                    winList.clear();
                    matchesList.clear();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "DATABSE ERROR: " + databaseError.getDetails());
                }
            });

            Log.d(TAG, "PASSED USERNAME= " + passedKey);
        }


        View headerView = navigationDrawerBinding.navigationView.getHeaderView(0);
        profileEmail = headerView.findViewById(R.id.profile_email);
        profileName = headerView.findViewById(R.id.profile_name);
        circleImageView = headerView.findViewById(R.id.profile_image);
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            profileEmail.setText(firebaseUser.getEmail());
            Uri profileUri = firebaseUser.getPhotoUrl();
            Log.d(TAG, "profile Name= " + firebaseUser.getProviderData());
            username = firebaseUser.getDisplayName();

            for (UserInfo userInfo : firebaseUser.getProviderData()) {
                if (username == null && userInfo.getDisplayName() != null) {
                    username = userInfo.getDisplayName();
                }
                if (profileUri == null && userInfo.getPhotoUrl() != null) {
                    profileUri = userInfo.getPhotoUrl();
                }
            }
            compositeDisposable = new CompositeDisposable();
            if (pictureEntityList != null) {
                compositeDisposable.add(pictureViewModel.getPictureEntityList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<PictureEntity>>() {
                            @Override
                            public void accept(List<PictureEntity> pictureEntities) throws Exception {
                                pictureEntityList = pictureEntities;
                                Log.d(TAG, "NEW ARRAY= " + pictureEntities);
                                circleImageView.setImageURI(Uri.parse(pictureEntities.get(0).getImageUri()));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        }));
            }

            pictureViewModel.getPictureEntityList();
            Log.d(TAG, "ARRAY LIST= " + pictureEntityList);
            pictureEntityList.add(pictureEntity);
            Log.d(TAG, "FINAL ARRAY LIST= " + pictureEntityList);
//            if(pictureEntityList!=null){
//                Log.d(TAG,"FINAL PIC URI= "+Uri.parse(pictureEntityList.get(0).getImageUri()));
//                circleImageView.setImageURI(Uri.parse(pictureEntityList.get(0).getImageUri()));
//            }
//            else {
//                Log.d(TAG,"Reached Here");
//                circleImageView.setImageURI(profileUri);
//            }
            profileName.setText(username);
            //userProfileList=new ArrayList<>();
            //addUserDetail();
        }

        circleImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_FILE);
        });


        navigationDrawerBinding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, MatchActivity.class);
            intent.putExtra(SignInActivity.KEY_USERNAME, passedKey);
            startActivityForResult(intent, REQUEST_CODE);
        });

    }

//    private void addUserDetail(){
//         username=profileName.getText().toString();
//         String id=databaseReferenceProfile.push().getKey();
//         UserProfile userProfile=new UserProfile(username);
//         databaseReferenceProfile.child(id).setValue(userProfile);
//    }

    @Override
    public void onBackPressed() {
//        if (navigationDrawerBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            navigationDrawerBinding.drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            finishAffinity();
//            //super.onBackPressed();
//        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    private void passStats(int matches, int wins) {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();
        Log.d(TAG, "FINALLLLLLLL WINS= " + wins);
        menu.findItem(R.id.matches).setTitle("Total Matches: " + matches);
        menu.findItem(R.id.wins).setTitle("Wins: " + wins);
        menu.findItem(R.id.losees).setTitle("Losses: " + (matches - wins));
    }

    private void startWorkManager(Data data){
        OneTimeWorkRequest oneTimeWorkRequest=new OneTimeWorkRequest.Builder(NotificationHandler.class)
                .setConstraints(constraints())
                .setInputData(data)
                .build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

    }

    private Constraints constraints(){
        Constraints constraints= new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();
        return constraints;
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDashboardGridItemClick(int clickedItemId) {
        //navigateWithItemId(clickedItemId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.account:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case R.id.delete:
                profileViewModel.deleteAll();
            case R.id.settings:
                break;

        }
        navigationDrawerBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                matches = data.getStringExtra(MatchActivity.KEY_MATCHES);
                wins = data.getStringExtra(MatchActivity.KEY_WINS);
                day = data.getStringExtra(MatchActivity.KEY_DAY);
                saveInfo(matches, wins, day);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "Data not received");
            } else {
                Log.d(TAG, "Nothing done");
            }
        } else if (requestCode == REQUEST_GET_FILE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                Log.d(TAG, "IMAGE URI =" + imageUri);
                circleImageView.setImageURI(imageUri);
                Log.d(TAG, "INITIAL PIC URI= " + imageUri);
                pictureEntity = new PictureEntity(imageUri.toString());
                pictureViewModel.insertPicture(pictureEntity);
                pictureEntityList.add(pictureEntity);
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!checkStoragePermission()) return;
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST);
                return false;
            } else return true;
        }
        return true;
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.CONTENT_TYPE);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void saveInfo(String matches, String wins, String day) {
        try {
            profileEntity = new ProfileEntity(username, day, matches, wins);
            profileViewModel.insertDatabase(profileEntity);
            int id = profileEntity.getId();
            profileViewModel.getDatabaseById(id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void replaceMainFragment(Fragment fragment) {
        setCurrentFragment(fragment);

        //a new FragmentTransaction must be created while make a transaction
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);

        //don't allow back navigation for home fragment
        if (fragment instanceof DashboardFragment) {
            fragmentTransaction.addToBackStack(null);
        } else {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        fragmentTransaction.commit();
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

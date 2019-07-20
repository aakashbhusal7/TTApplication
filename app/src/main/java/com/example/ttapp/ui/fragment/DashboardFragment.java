package com.example.ttapp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttapp.R;
import com.example.ttapp.adapter.MainGridAdapter;
import com.example.ttapp.ui.activity.DashboardActivity;
import com.example.ttapp.utils.CustomClickListener;
import com.example.ttapp.utils.SpacesItemDecoration;
import com.flipboard.bottomsheet.BottomSheetLayout;

public class DashboardFragment extends Fragment implements MainGridAdapter.ItemClickListener  {

    private static final String TAG= DashboardFragment.class.getSimpleName();
    private DashboardItemClickListener dashboardItemClickListener;

    private RecyclerView recyclerView;
    private MainGridAdapter mainGridAdapter;

    private int mIndexCount;
    private int mIndex = 0;


    String[]menu={
            "Stats",
            "Ranking",
            "Events",
            "News"
    };
    int[]imageId= {
            R.drawable.poll,
            R.drawable.rankings,
            R.drawable.event,
            R.drawable.news
    };

    public DashboardFragment(){}

//    private View.OnClickListener onClickListener=new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            RecyclerView.ViewHolder viewHolder=(RecyclerView.ViewHolder)view.getTag();
//            int position=viewHolder.getAdapterPosition();
//            Toast.makeText(getContext(),"you clicked: "+imageId[position],Toast.LENGTH_LONG).show();
//        }
//    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView=inflater.inflate(R.layout.content_main_menu,container,false);
        setHasOptionsMenu(true);

        recyclerView=fragmentView.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing)));

//        ((DashboardActivity) getActivity()).setToolbarTitle(getResources().getString(R.string.app_name_branding), null);
//        ((DashboardActivity) getActivity()).setNavigationCheckedItem(R.id.nav_home);


        MainGridAdapter.ItemClickListener itemClickListener=new MainGridAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(),"Position= "+position,Toast.LENGTH_LONG).show();
            }
        };

        mainGridAdapter=new MainGridAdapter(getActivity(), menu, imageId, new CustomClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        StatsFragment statsFragment=new StatsFragment();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container,statsFragment,"STATS_FRAGMENT")
                                .addToBackStack(null)
                                .commit();

                }


            }
        });
        recyclerView.setAdapter(mainGridAdapter);

        return fragmentView;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //prepareGrid();
    }
//    private void prepareGrid(){
//        mainGridAdapter=new MainGridAdapter(getActivity(),menu,imageId);
////        recyclerView.setAdapter(mainGridAdapter);
////        mainGridAdapter.notifyDataSetChanged();
//       mainGridAdapter.setClickListener(new MainGridAdapter.ItemClickListener() {
//           @Override
//           public void onItemClick(View view, int position) {
//               Log.d(TAG,"on item click: "+view);
//               Log.d(TAG,"on item click :"+position);
//           }
//       });
//    }

    public void setDashboardItemClickListener(DashboardItemClickListener itemClickListener) {
        this.dashboardItemClickListener = itemClickListener;
    }

    public interface DashboardItemClickListener {

        /**
         * Interface to handle the items clicked in the main view of the fragment,
         * that needs to interact with the controllers in the holding/attached activity.
         *
         * @param clickedItemId The clicked item id in the view of the fragment.
         */
        void onDashboardGridItemClick(int clickedItemId);

    }

}

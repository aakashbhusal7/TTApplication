//package com.example.ttapp.utils;
//
//import android.view.View;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.ttapp.R;
//
//public class ItemClickSupport {
//    private final RecyclerView mRecyclerView;
//    private OnItemClickListener mOnItemClickListener;
//
//
//    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
//            = new RecyclerView.OnChildAttachStateChangeListener() {
//        @Override
//        public void onChildViewAttachedToWindow(View view) {
//        }
//
//        @Override
//        public void onChildViewDetachedFromWindow(View view) {
//
//        }
//    };
//
//    private ItemClickSupport(RecyclerView recyclerView) {
//        mRecyclerView = recyclerView;
//        mRecyclerView.setTag(R.id.item_click_support, this);
//        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
//    }
//
//    public static ItemClickSupport addTo(RecyclerView view) {
//        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
//        if (support == null) {
//            support = new ItemClickSupport(view);
//        }
//        return support;
//    }
//
//    public static ItemClickSupport removeFrom(RecyclerView view) {
//        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
//        if (support != null) {
//            support.detach(view);
//        }
//        return support;
//    }
//
//    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
//        mOnItemClickListener = listener;
//        return this;
//    }
//
//    private void detach(RecyclerView view) {
//        view.removeOnChildAttachStateChangeListener(mAttachListener);
//        view.setTag(R.id.item_click_support, null);
//    }
//
//    public interface OnItemClickListener {
//        void onItemClicked(RecyclerView recyclerView, int position, View v);
//        void onItemDoubleClicked(RecyclerView recyclerView, int position, View v);
//    }
//}

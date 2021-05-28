package com.yomplex.tests.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.yomplex.tests.R;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;
    private Context context;
    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        this.context = context;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.e("recylcer touch","onlongpress.....e.getAction()..."+e.getAction());
                if(e.getAction() == 0){

                }else{
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }

            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.e("recylcer touch","onInterceptTouchEvent.....e.getAction()..."+e.getAction());
        View child = null,c=null;
        if(e.getAction() == 0){
            child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                c = child.findViewById(R.id.bookRL);
                c.setForeground(context.getResources().getDrawable(R.drawable.selector_medium_high));
            }


            // child.getForeground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
            Log.e("recylcer touch","onInterceptTouchEvent... if.....child..."+child);
            Log.e("recylcer touch","onInterceptTouchEvent... if.....clickListener..."+clickListener);
            Log.e("recylcer touch","onInterceptTouchEvent... if.....gestureDetector.onTouchEvent(e)..."+gestureDetector.onTouchEvent(e));
        }else if(e.getAction() == 1){
            child = rv.findChildViewUnder(e.getX(), e.getY());

            Log.e("recylcer touch","onInterceptTouchEvent...else if.....e.getAction()..."+e.getAction());
            Log.e("recylcer touch","onInterceptTouchEvent...else if.....child..."+child);
            Log.e("recylcer touch","onInterceptTouchEvent...else if.....clickListener..."+clickListener);
            Log.e("recylcer touch","onInterceptTouchEvent...else if.....gestureDetector.onTouchEvent(e)..."+gestureDetector.onTouchEvent(e));
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
               // child.getForeground().clearColorFilter();
                c = child.findViewById(R.id.bookRL);
                //child.getBackground().clearColorFilter();
                c.setForeground(null);

                Log.e("recylcer touch","onInterceptTouchEvent... if.....e.getAction()..."+e.getAction());
                clickListener.onClick(child, rv.getChildPosition(child));
            }

        }


        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.e("recylcer touch","onTouchEvent.....e.getAction()..."+e.getAction());
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child));
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}

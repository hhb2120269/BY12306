package com.example.hhb.by12306.tool.sidemenu.util;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.hhb.by12306.R;
import com.example.hhb.by12306.tool.sidemenu.animation.FlipAnimation;
import com.example.hhb.by12306.tool.sidemenu.interfaces.Resourceble;
import com.example.hhb.by12306.tool.sidemenu.interfaces.ScreenShotable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Konstantin on 12.01.2015.
 */
public class ViewAnimator<T extends Resourceble> {
    private final int ANIMATION_DURATION = 175;
    public static final int CIRCULAR_REVEAL_ANIMATION_DURATION = 500;

    private AppCompatActivity appCompatActivity;

    private List<T> list;

    private List<View> viewList = new ArrayList<>();
    private ScreenShotable screenShotable;
    private DrawerLayout drawerLayout;
    private ViewAnimatorListener animatorListener;


    public ViewAnimator(AppCompatActivity activity,
                        List<T> items,
                        ScreenShotable screenShotable,
                        final DrawerLayout drawerLayout,
                        ViewAnimatorListener animatorListener) {
        this.appCompatActivity = activity;

        this.list = items;
        this.screenShotable = screenShotable;
        this.drawerLayout = drawerLayout;
        this.animatorListener = animatorListener;
    }
    public ViewAnimator(AppCompatActivity activity,
                        List<T> items,
                        final DrawerLayout drawerLayout,
                        ViewAnimatorListener animatorListener) {
        this.appCompatActivity = activity;
        this.list = items;
        this.drawerLayout = drawerLayout;
        this.animatorListener = animatorListener;
    }


    public void showMenuContent() {
        setViewsClickable(false);
        viewList.clear();
        double size = list.size();
        for (int i = 0; i < size; i++) {
            View viewMenu = appCompatActivity.getLayoutInflater().inflate(R.layout.menu_list_item, null);

            final int finalI = i;
            viewMenu.setOnClickListener(setOnItemClickListener(finalI));
            ((ImageView) viewMenu.findViewById(R.id.menu_item_image)).setImageResource(list.get(i).getImageRes());
            viewMenu.setVisibility(View.GONE);
            viewMenu.setEnabled(false);
            if(list.get(finalI).isSelected() && finalI!=0){
                viewMenu.setBackgroundResource(R.drawable.item_down);
            }else{
                viewMenu.setBackgroundResource(R.drawable.item_up);
            }
            viewList.add(viewMenu);
            animatorListener.addViewToContainer(viewMenu);
            final double position = i;
            final double delay = 3 * ANIMATION_DURATION * (position / size);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (position < viewList.size()) {
                        animateView((int) position);
                    }
                    if (position == viewList.size() - 1) {
                        if(screenShotable!=null){
                            screenShotable.takeScreenShot();
                        }else{
                            Log.e("showMenuContent","screenShotable=null!");
                        }
                        setViewsClickable(true);
                    }
                }
            }, (long) delay);
        }

    }

    private void hideMenuContent() {
        setViewsClickable(false);
        double size = list.size();
        for (int i = list.size(); i >= 0; i--) {
            final double position = i;
            final double delay = 3 * ANIMATION_DURATION * (position / size);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (position < viewList.size()) {
                        animateHideView((int) position);
                    }
                }
            }, (long) delay);
        }

    }
    private void refreshItemsSelected(int indexSelected){
        double size = list.size();
        for (int i = 0; i < size; i++) {
            View viewMenu = viewList.get(i);
            Resourceble data = list.get(i);
            ((ImageView) viewMenu.findViewById(R.id.menu_item_image)).setImageResource(list.get(i).getImageRes());
            if(indexSelected == i){
                viewMenu.setBackgroundResource(R.drawable.item_down);
                data.setSelected(true);
            }else{
                viewMenu.setBackgroundResource(R.drawable.item_up);
                data.setSelected(false);
            }


//            viewMenu.setVisibility(View.GONE);
//            viewMenu.setEnabled(false);
        }
    }

    /**
     *
     * @param finalI
     * @return
     */
    private View.OnClickListener setOnItemClickListener(final int finalI){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = {0, 0};
                v.getLocationOnScreen(location);
                switchItem(list.get(finalI), location[1] + v.getHeight() / 2);
                /** when item was selected, to refresh the UI of the item **/
                selectItemOnList(list, finalI, v);
            }
        };
    }

    private void setViewsClickable(boolean clickable) {
        animatorListener.disableHomeButton();
        for (View view : viewList) {
            view.setEnabled(clickable);
        }
    }

    private void animateView(int position) {
        final View view = viewList.get(position);
        view.setVisibility(View.VISIBLE);
        FlipAnimation rotation =
                new FlipAnimation(90, 0, 0.0f, view.getHeight() / 2.0f);
        rotation.setDuration(ANIMATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(rotation);
    }

    private void animateHideView(final int position) {
        final View view = viewList.get(position);
        FlipAnimation rotation =
                new FlipAnimation(0, 90, 0.0f, view.getHeight() / 2.0f);
        rotation.setDuration(ANIMATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.INVISIBLE);
                if (position == viewList.size() - 1) {
                    animatorListener.enableHomeButton();
                    drawerLayout.closeDrawers();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(rotation);
    }

    private void switchItem(Resourceble slideMenuItem, int topPosition) {
        if(screenShotable!=null){
            this.screenShotable = animatorListener.onSwitch(slideMenuItem, screenShotable, topPosition);
            hideMenuContent();
        }else{
            Log.e("switchItem","screenShotable=null!");
            animatorListener.onSwitch(slideMenuItem, topPosition);
            hideMenuContent();
        }
    }

    /**
     * when item was selected, to refresh the UI of the item
     * @param slideMenuItemList List<SlideMenuItem>
     * @param index int
     */
    private void selectItemOnList(List<T> slideMenuItemList, int index,View itemView) {
        animatorListener.onSelectInList(slideMenuItemList,  index);
        refreshItemsSelected(index);
        hideMenuContent();
    }

    public interface ViewAnimatorListener <T extends Resourceble> {

        public ScreenShotable onSwitch(Resourceble slideMenuItem, @Nullable ScreenShotable screenShotable, int position);
        public void onSwitch(Resourceble slideMenuItem, int position);

        /**
         * when item was selected, to refresh the UI of the item
         * @param slideMenuItemList List<SlideMenuItem>
         * @param position int index
         * @return
         */
        public void onSelectInList(List<T> slideMenuItemList, int position);

        public void disableHomeButton();

        public void enableHomeButton();

        public void addViewToContainer(View view);

    }
}

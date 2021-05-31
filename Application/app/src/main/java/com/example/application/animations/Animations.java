package com.example.application.animations;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Class that holds different kinds of animations 
 */

public class Animations {

    /**
     * This method expands the view with the view that is with the method calling.
     * @param view The view to be expanded
     */
    public static void expand(View view) {
        Animation animation = expandAction(view);
        view.startAnimation(animation);
    }
    
    private static Animation expandAction(final View view) {

        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int actualheight = view.getMeasuredHeight();

        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);

        //Starts the animation with a timer based on the height of the object
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : Math.max(1, (int) (actualheight * interpolatedTime));
                view.requestLayout();
            }
        };


        animation.setDuration((long) (actualheight / view.getContext().getResources().getDisplayMetrics().density));

        view.startAnimation(animation);

        return animation;


    }

    /**
     * This method collapses the view with the view that is with the method calling.
     * @param view The view to be collapsed.
     */
    public static void collapse(final View view) {

        final int actualHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = actualHeight - (int) (actualHeight * interpolatedTime);
                    view.requestLayout();

                }
            }
        };

        animation.setDuration((long) (actualHeight/ view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }


    /**
     * Rotates a view by 180 degrees, mostly used to rotate the arrow on each card
     * @param view The view to be rotated
     * @param isExpanded Checks which way the arrow need to be rotated (Up/Down)
     * @return Whether or not the view that the arrow is with is now rotated or not.
     */
    public static boolean toggleArrow(View view, boolean isExpanded) {

        if (isExpanded) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }
}

package ch.uzh.supersede.feedbacklibrary.components.views;

import android.view.LayoutInflater;
import android.view.View;

public abstract class AbstractFeedbackPartView implements Comparable{
    protected int viewOrder = 0;
    private View enclosingLayout = null;
    private LayoutInflater layoutInflater;

    public AbstractFeedbackPartView(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public View getEnclosingLayout() {
        return enclosingLayout;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public void setEnclosingLayout(View enclosingLayout) {
        this.enclosingLayout = enclosingLayout;
    }

    public abstract void updateModel();

    protected abstract void colorPrimary(int color);

    protected abstract void colorSecondary(int color);

    protected abstract void colorTertiary(int color);

    public void colorView(Integer... colors){
        if (colors != null && colors.length >= 1){
            colorPrimary(colors[0]);
        }
        if (colors != null && colors.length >= 2){
            colorSecondary(colors[1]);
        }
        if (colors != null && colors.length >= 3){
            colorTertiary(colors[2]);
        }
    }

    public int getViewOrder() {
        return viewOrder;
    }
}

package ch.uzh.supersede.feedbacklibrary.components.views;

import android.view.LayoutInflater;
import android.view.View;

public abstract class AbstractMechanismView implements Comparable{
    protected int viewOrder = 0;
    private View enclosingLayout = null;
    private LayoutInflater layoutInflater;

    public AbstractMechanismView(LayoutInflater layoutInflater) {
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

    public int getViewOrder() {
        return viewOrder;
    }
}

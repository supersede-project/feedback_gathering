package com.example.matthias.feedbacklibrary.views;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Mechanism view
 */
public abstract class MechanismView {
    private View enclosingLayout = null;
    private LayoutInflater layoutInflater = null;

    public MechanismView(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    /**
     * This method returns the enclosing layout view
     *
     * @return the enclosing layout
     */
    public View getEnclosingLayout() {
        return enclosingLayout;
    }

    /**
     * This method returns the layout inflater.
     *
     * @return the inflater
     */
    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    /**
     * This method sets the enclosing layout view
     *
     * @param enclosingLayout the enclosing layout
     */
    public void setEnclosingLayout(View enclosingLayout) {
        this.enclosingLayout = enclosingLayout;
    }

    /**
     * This method updates the model with the values from the corresponding view.
     */
    public abstract void updateModel();
}

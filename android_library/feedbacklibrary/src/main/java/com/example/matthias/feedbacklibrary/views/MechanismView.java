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
}

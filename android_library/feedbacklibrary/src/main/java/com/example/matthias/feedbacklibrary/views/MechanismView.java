package com.example.matthias.feedbacklibrary.views;

import android.view.LayoutInflater;
import android.view.View;

import com.example.matthias.feedbacklibrary.models.Mechanism;

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
    public void setEnclosingLayout(View enclosingLayout) {
        this.enclosingLayout = enclosingLayout;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public abstract void updateModel();
}

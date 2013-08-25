package com.vlaaad.tenseconds.world.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.vlaaad.common.logging.Logger;
import com.vlaaad.tenseconds.AppConfig;

/**
 * Created 24.08.13 by vlaaad
 */
public class CellView extends TextButton {
    private int pad;

    public CellView() {
        super("9", AppConfig.skin);
        getLabel().setFontScale(2);
    }


    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        Label label = getLabel();
        float h = label.getPrefHeight();
        float factor = height / h;
        label.setFontScale(getScale(factor));
    }

    public float getScale(float real) {
        float snap = 0.5f;
        int snaps = Math.round(real / snap);
        return snaps * snap;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isPressed() && isOver() && !isDisabled()) {
            setPad(-2);
        } else {
            setPad(-8);
        }
    }

    private void setPad(int value) {
        if (pad == value)
            return;
        pad = value;
        getLabelCell().padTop(pad);
        invalidate();

    }
}

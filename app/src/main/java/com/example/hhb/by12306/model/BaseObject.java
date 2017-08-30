package com.example.hhb.by12306.model;

import java.io.Serializable;

/**
 * Created by hhb on 17/8/7.
 */

public class BaseObject implements Serializable {
    private boolean selected;           //显示：被选中
    private boolean ischanged;           //显示：是新的

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean ischanged() {
        return ischanged;
    }

    public void setIschanged(boolean ischanged) {
        this.ischanged = ischanged;
    }
}

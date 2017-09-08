package com.example.hhb.by12306.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by hhb on 17/8/7.
 */

public class BaseObject implements Serializable {
    private boolean _selected;           //显示：被选中
    private boolean _ischanged;           //显示：是新的
    private long _changeTime;           //时间


    public boolean is_selected() {
        return _selected;
    }

    public void set_selected(boolean _selected) {
        this._selected = _selected;
    }

    public boolean is_ischanged() {
        return _ischanged;
    }

    public void set_ischanged(boolean _ischanged) {
        this._ischanged = _ischanged;
    }

    public long get_changeTime() {
        return _changeTime;
    }

    public void set_changeTime() {//fixme: 默认是系统时间 System.currentTimeMillis();
        this._changeTime = System.currentTimeMillis();
    }

}

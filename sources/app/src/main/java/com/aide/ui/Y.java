package com.aide.ui;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import com.s1243808733.aide.util.MainInterface;

public class Y implements Callback {

    @Override
    public boolean onCreateActionMode(ActionMode p1, Menu p2) {
        MainInterface.onCreateActionMode(p1, p2);
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode p1, Menu p2) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode p1, MenuItem p2) {
        MainInterface.onActionItemClicked(p1, p2);
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode p1) {
    }


}

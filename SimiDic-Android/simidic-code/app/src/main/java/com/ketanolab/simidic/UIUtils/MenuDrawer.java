package com.ketanolab.simidic.UIUtils;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.ketanolab.simidic.R;

/**
 * Created by pteran on 05-02-16.
 */
public class MenuDrawer {
    public static ActionBarDrawerToggle getMenuDrawer(final ActionBarActivity ctx, final ActionBar actionBar, DrawerLayout drawerLayout){
        ActionBarDrawerToggle mDrawerToggle =
        new ActionBarDrawerToggle(
                ctx,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.open_memu_drawer_description_for_accesibility,  /* "open drawer" description for accessibility */
                R.string.close_memu_drawer_description_for_accesibility  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                actionBar.invalidateOptionsMenu();
                // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //supportInvalidateOptionsMenu();
                actionBar.invalidateOptionsMenu();
                InputMethodManager imm = (InputMethodManager)ctx.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(), 0);
            }

        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        return mDrawerToggle;
    }
}

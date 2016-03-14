package com.ketanolab.simidic.Constants;

import com.ketanolab.simidic.R;

/**
 * Created by pteran on 05-02-16.
 */
public enum EMenuItems {
    DICTIONAIRES(R.string.dictionaries,  R.drawable.ic_action_books,1),
    CREDITS(R.string.credits,  R.drawable.ic_action_info,2),
    FAVORITES(R.string.favorites,R.drawable.ic_action_star,3),
    DOWNLOADS(R.string.downloads,  R.drawable.ic_action_download,4);
private final int text, icon, id;

    private EMenuItems(int text, int icon, int id){
        this.text = text;
        this.icon = icon;
        this.id = id;
    }

    public int getText() {
        return text;
    }

    public int getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }


}

package com.ketanolab.simidic.Constants

import com.ketanolab.simidic.R

/**
 * Created by pteran on 05-02-16.
 */
enum class EMenuItems(val text: Int, val icon: Int, val id: Int) {
    DICTIONAIRES(R.string.dictionaries, R.drawable.ic_action_books, 1), CREDITS(
        R.string.credits,
        R.drawable.ic_action_info,
        2
    ),
    FAVORITES(R.string.favorites, R.drawable.ic_action_star, 3), DOWNLOADS(
        R.string.downloads,
        R.drawable.ic_action_download,
        4
    );

}
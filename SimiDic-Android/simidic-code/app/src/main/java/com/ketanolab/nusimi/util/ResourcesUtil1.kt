package com.ketanolab.nusimi.util

import android.content.Context
import android.preference.PreferenceManager
import java.util.Locale

/**
 * this code is sent as it is and we are not responsible of any damage, lose or anything if you compile this code. If you have bugs to report please go to wiki.processmaker.com first. don't like reading shitty bugs!!
 * Created by Pedro Teran Gezn on 09-09-15.
 *
 *
 * © Copyright ProcessMaker Inc. 2000-2015. All Rights Reserved.
 */
object ResourcesUtil {
    fun setUpResources(context: Context) {
        var languageCode = PreferenceManager
            .getDefaultSharedPreferences(context).getString("language_code", "")
        if (languageCode == null || languageCode.isEmpty()) languageCode =
            Locale.getDefault().language
        val res = context.resources
        // Change locale settings in the app.
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = Locale(languageCode!!.toLowerCase())
        res.updateConfiguration(conf, dm)
    }
}
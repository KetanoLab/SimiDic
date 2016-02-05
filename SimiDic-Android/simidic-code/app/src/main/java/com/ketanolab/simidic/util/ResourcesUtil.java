package com.ketanolab.simidic.util;

import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;



import java.util.Locale;

/**
 * this code is sent as it is and we are not responsible of any damage, lose or anything if you compile this code. If you have bugs to report please go to wiki.processmaker.com first. don't like reading shitty bugs!!
 * Created by Pedro Teran Gezn on 09-09-15.
 * <p/>
 * © Copyright ProcessMaker Inc. 2000-2015. All Rights Reserved.
 */
public class ResourcesUtil {


    public static void setUpResources(final  Context context){
        String languageCode = PreferenceManager
                .getDefaultSharedPreferences(context).getString("language_code","");
        if(languageCode == null ||languageCode.isEmpty())
            languageCode = Locale.getDefault().getLanguage();

        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(languageCode.toLowerCase());
        res.updateConfiguration(conf, dm);
    }
}

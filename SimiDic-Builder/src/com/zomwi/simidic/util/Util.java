package com.zomwi.simidic.util;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 12-10-12
 * Time: 06:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    public static String generateDatabaseName(String beginLanguage, String endLanguage, String author) {
        String name = "";
        beginLanguage = beginLanguage.toLowerCase();
        endLanguage = endLanguage.toLowerCase();
        author = author.toLowerCase();

        name = getLanguageCode(beginLanguage) + "_" + getLanguageCode(endLanguage) + "_" + author.substring(0, 2);

        return name;
    }

    public static String getLanguageCode(String language) {
        if (language.equals("aymara")) {
            return "ay";
        } else if (language.equals("quechua")) {
            return "qu";
        } else if (language.equals("guarani") || language.equals("guaraní")) {
            return "gn";
        } else if (language.equals("castellano") || language.equals("español") || language.equals("espanol")) {
            return "es";
        }
        return language;
    }
}

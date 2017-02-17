package com.example.eric.demomapsapp;

import android.webkit.MimeTypeMap;

/**
 * Created by Eric on 2/18/2017.
 */

public class Config {
    public static String base_url = "http://savtech.co.ke/demomapsapp/";

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
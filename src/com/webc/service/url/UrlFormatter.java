package com.webc.service.url;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlFormatter {


    public static String merge(String prefix, String suffix) {

        if(suffix.length() > 4 && suffix.substring(0,4).equals("http"))
            return suffix;
        else if(suffix.length() > 2 && suffix.substring(0,2).equals("//"))
            return "https:" + suffix;
        else if(prefix.charAt(prefix.length()-1)=='/' && suffix.charAt(0) == '/')
            return prefix.substring(0, prefix.length()-1) + suffix;
        else if(prefix.charAt(prefix.length()-1)!='/' && suffix.charAt(0) != '/')
            return prefix + "/" + suffix;
        return prefix + suffix;

    }


    public static String generateBaseUrl(String startUrl) throws MalformedURLException {

        URL url = new URL(startUrl);
        String path = url.getFile().substring(0, url.getFile().lastIndexOf('/'));
        return url.getProtocol() + "://" + url.getHost();

    }

}

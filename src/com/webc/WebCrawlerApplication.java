package com.webc;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WebCrawlerApplication {

    public static Map<String, String> statusCode = new HashMap<>();
    public static String baseUrl = "";

    public static String formatUrl(String prefix, String suffix) {

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

    public static String baseUrl(String startUrl) throws MalformedURLException {

        URL url = new URL(startUrl);

        String path = url.getFile().substring(0, url.getFile().lastIndexOf('/'));
        baseUrl = url.getProtocol() + "://" + url.getHost();
        return startUrl;
    }

    public static void statusCode(String url) throws IOException {

            if(!statusCode.containsKey(url)) {

                OkHttpRequest req = new OkHttpRequest();

                Map<String, String> response = req.get(url);

                statusCode.put(url, response.get("code"));
                System.out.println("url: " + url + ", status code: " +response.get("code"));

                Document page = Jsoup.parseBodyFragment(response.get("body"));
                List<Element> links = page.getElementsByTag("a");

                    for (Element link : links) {
                        if(link.attr("href").isEmpty())
                            break;
                        else if(link.attr("href").equals("/"))
                            continue;
                        statusCode(formatUrl(baseUrl, link.attr("href")));
                    }
            }
    }

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a url of the form : https://domain.xyz/");
        String startUrl = sc.nextLine();
        statusCode(baseUrl(startUrl));

    }
}
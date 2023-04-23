# Web Crawler Using Java #

A basic webcrawler using Java that gets status codes for all the urls/links on each url starting with the user input.

## OkHttp ##
OkHttp is an HTTP client for exchanging data and media efficiently.

### Get a URL ###

This program downloads a URL and prints its contents as a string. Full source.

```
OkHttpClient client = new OkHttpClient();

String run(String url) throws IOException {
  Request request = new Request.Builder()
      .url(url)
      .build();

  try (Response response = client.newCall(request).execute()) {
    return response.body().string();
  }
}
```

### Post to a Server ###

```
public static final MediaType JSON
    = MediaType.get("application/json; charset=utf-8");

OkHttpClient client = new OkHttpClient();

String post(String url, String json) throws IOException {
  RequestBody body = RequestBody.create(json, JSON);
  Request request = new Request.Builder()
      .url(url)
      .post(body)
      .build();
  try (Response response = client.newCall(request).execute()) {
    return response.body().string();
  }
}

```

## JSoup ##

jsoup is a Java library for working with real-world HTML. It provides a very convenient API for fetching URLs and extracting and manipulating data, using the best of HTML5 DOM methods and CSS selectors.

jsoup implements the WHATWG HTML5 specification, and parses HTML to the same DOM as modern browsers do.

scrape and parse HTML from a URL, file, or string
find and extract data, using DOM traversal or CSS selectors
manipulate the HTML elements, attributes, and text
clean user-submitted content against a safelist, to prevent XSS attacks
output tidy HTML

Code to parse an HTML document and get links

```

Document page = Jsoup.parseBodyFragment(response.get("body"));
List<Element> links = page.getElementsByTag("a");

 for (Element link : links) {
    System.out.println(link.attr("href))                      
  }
  
```


package org.example.urlShortener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UrlShortener {

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final AtomicLong counter = new AtomicLong(1);

    private final ConcurrentHashMap<String, String> shortToLong = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> longToShort = new ConcurrentHashMap<>();

    public String shorten(String url) {

        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }

        return longToShort.computeIfAbsent(url, key -> {
            String shortUrl = encode(counter.getAndIncrement());
            shortToLong.put(shortUrl, key);
            return shortUrl;
        });
    }

    public String resolve(String shortUrl) {

        String url = shortToLong.get(shortUrl);

        if (url == null) {
            throw new IllegalArgumentException("Short URL not found");
        }

        return url;
    }

    private String encode(long id) {

        StringBuilder sb = new StringBuilder();

        while (id > 0) {
            int index = (int) (id % 62);
            sb.append(BASE62.charAt(index));
            id /= 62;
        }

        return sb.reverse().toString();
    }
}
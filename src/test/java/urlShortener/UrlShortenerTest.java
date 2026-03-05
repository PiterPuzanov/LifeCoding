package urlShortener;

import org.example.urlShortener.UrlShortener;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class UrlShortenerTest {

    private final UrlShortener service = new UrlShortener();

    @Test
    void shortenAndResolve_shouldReturnOriginalUrl() {

        String url = "https://google.com";

        String shortUrl = service.shorten(url);

        String resolved = service.resolve(shortUrl);

        assertEquals(url, resolved);
    }

    @Test
    void shortenSameUrl_shouldReturnSameShortUrl() {

        String url = "https://google.com";

        String short1 = service.shorten(url);
        String short2 = service.shorten(url);

        assertEquals(short1, short2);
    }

    @Test
    void resolve_unknownShortUrl_shouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.resolve("unknown")
        );
    }

    @Test
    void shorten_emptyUrl_shouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.shorten("")
        );
    }

    @Test
    void concurrentShorten_shouldReturnSameShortUrl() throws Exception {

        String url = "https://google.com";

        int threads = 20;

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        CountDownLatch latch = new CountDownLatch(threads);

        ConcurrentHashMap<String, Boolean> results = new ConcurrentHashMap<>();

        for (int i = 0; i < threads; i++) {

            executor.submit(() -> {

                String shortUrl = service.shorten(url);

                results.put(shortUrl, true);

                latch.countDown();
            });
        }

        latch.await();

        executor.shutdown();

        assertEquals(1, results.size());
    }
}
package com.szd.boxgo.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import static java.lang.Thread.sleep;

//@Component
public class AppDontSleepForRenderRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        sleep(100_000);
        while (true) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<?> entity = new HttpEntity<>(headers);
            restTemplate.exchange("https://gobox.onrender.com/hi",  HttpMethod.GET, entity, String.class);
            try {
                sleep(25_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package com.schmix.branch.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class HttpService {
    private final HttpClient client;
    private final Logger logger;

    public HttpService() {
        this(HttpClient.newHttpClient());
    }

    public HttpService(HttpClient client) {
        this.client = client;
        logger = LoggerFactory.getLogger(HttpService.class);
    }

    public String get(String url) {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(new URI(url)).GET().build();
        } catch (URISyntaxException e) {
            logger.error("Error creating request from URI", e);
            throw new IllegalArgumentException("Invalid URL provided");
        }

        String responseBody = null;
        try {
            logger.debug(String.format("sending request to %s", url));
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug(String.format("Response has status %d", response.statusCode()));

            if (response.statusCode() == HttpStatus.OK.value()) {
                responseBody = response.body();
            }
        } catch (IOException e) {
            logger.error("Error sending/receiving request", e);
        } catch (InterruptedException e) {
            logger.error("Request was interrupted", e);
        }

        return responseBody;
    }
}

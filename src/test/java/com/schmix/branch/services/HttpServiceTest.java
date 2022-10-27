package com.schmix.branch.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HttpServiceTest {
    private final HttpClient httpClient = mock(HttpClient.class);
    private final HttpService httpService = new HttpService(httpClient);

    @Test
    public void get_Success() throws IOException, InterruptedException {
        String body = "the return body";
        String url = "https://www.google.com";
        HttpResponse<Object> response = mock(HttpResponse.class);
        when(httpClient.send(any(), any())).thenReturn(response);

        when(response.statusCode()).thenReturn(HttpStatus.OK.value());
        when(response.body()).thenReturn(body);

        String returnedBody = httpService.get(url);
        Assertions.assertEquals(body, returnedBody);
    }

    @Test
    public void get_SadStatusCode() throws IOException, InterruptedException {
        String body = "the return body";
        String url = "https://www.google.com";
        HttpResponse<Object> response = mock(HttpResponse.class);
        when(httpClient.send(any(), any())).thenReturn(response);

        when(response.statusCode()).thenReturn(HttpStatus.I_AM_A_TEAPOT.value());
        when(response.body()).thenReturn(body);

        String returnedBody = httpService.get(url);
        Assertions.assertEquals(null, returnedBody);
    }

    @Test
    public void get_BadUrl() {
        String url = "bad url";
        Assertions.assertThrows(IllegalArgumentException.class, () -> httpService.get(url));
    }
}

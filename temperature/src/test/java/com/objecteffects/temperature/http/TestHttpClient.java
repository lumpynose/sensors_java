package com.objecteffects.temperature.http;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

/**
 * @author rusty
 */
class TestHttpClient {
    private final static Logger log = LogManager.getLogger();

    private final SimpleDateFormat jdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    @Test
    void testUp() throws IOException, InterruptedException {
        final HttpClientPrometheus client = new HttpClientPrometheus();

//        final String upQuery = "query?query=up&time=2023-02-24T11:40:05-08:00";
        final String upQuery2 = "query?query=up";

        final HttpResponse<String> response = client.sendAndReceive(upQuery2);

        log.debug("status: " + response.statusCode());
        log.debug("response body: " + response.body());

        final Gson gson = new Gson();

        final PromDataMatrix target = gson.fromJson(response.body(),
                PromDataMatrix.class);

        log.debug("target: " + target);
        log.debug("data: " + target.getData());
    }

//    @Test
//    void testLabels() throws IOException, InterruptedException {
//        final HttpClientPrometheus client = new HttpClientPrometheus();
//
//        final String labelsQuery = "labels";
//
//        final HttpResponse<String> response = client
//                .sendAndReceive(labelsQuery);
//
//        log.debug("status: " + response.statusCode());
//        log.debug("response body: " + response.body());
//
//        final Gson gson = new Gson();
//
//        final PromDataMatrix target = gson.fromJson(response.body(),
//                PromDataMatrix.class);
//
//        log.debug("target: " + target);
//        log.debug("data: " + target.getData());
//    }

    @Test
    void testTemperatureRange() throws IOException, InterruptedException {
        final HttpClientPrometheus client = new HttpClientPrometheus();

        final String beforeEncode = "temperature&step=60s&start=1677302457&end=1677302517";

        final String query = "query_range?query=" + beforeEncode;

        final HttpResponse<String> response = client.sendAndReceive(query);

        log.debug("status: " + response.statusCode());
        log.debug("response body: " + response.body());

        final Gson gson = new Gson();

        final PromDataMatrix promDataMatrix = gson.fromJson(response.body(),
                PromDataMatrix.class);
        log.debug("promData: " + promDataMatrix);

        for (final PromDataMatrix.PromSensor ps : promDataMatrix.getData()
                .getResult()) {
            log.debug(String.format("sensor: %s, %s",
                    ps.getMetric().getSensor(), ps.getMetric().getJob()));

            for (final JsonArray values : ps.getValues()) {
                log.debug(String.format("values: %s, %s",
                        new Date(values.get(0).getAsLong() * 1000L),
                        values.get(1)));
            }
        }
    }

    @Test
    void testTemperatureInstant() throws IOException, InterruptedException {
        final HttpClientPrometheus client = new HttpClientPrometheus();

        final String beforeEncode = "temperature&time=1677468358";

        final String query = "query?query=" + beforeEncode;

        final HttpResponse<String> response = client.sendAndReceive(query);

        log.debug("status: " + response.statusCode());
        log.debug("response body: " + response.body());

        final Gson gson = new Gson();

        final PromDataVector promDataVector = gson.fromJson(response.body(),
                PromDataVector.class);
        log.debug("promData: " + promDataVector);

        for (final PromDataVector.PromSensor ps : promDataVector.getData()
                .getResult()) {
            log.debug(String.format("sensor: %s, %s",
                    ps.getMetric().getSensor(), ps.getMetric().getJob()));

            final List<JsonPrimitive> value = ps.getValue();
            log.debug(String.format("values: %s, %s",
                    new Date(value.get(0).getAsLong() * 1000L), value.get(1)));
        }
    }
}

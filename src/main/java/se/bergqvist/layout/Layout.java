package se.bergqvist.layout;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Layout, handling of the turnouts.
 *
 * @author Daniel Bergqvist (C) 2025
 */
public class Layout {

    public interface TurnoutListener {
        void state(int turnout, boolean value);
    }


    private final String TURNOUT_PREFIX = "LT";

    private final Map<String, Integer> _turnouts = new HashMap<>();
    private final List<TurnoutListener> _listeners = new ArrayList<>();
    private HttpClient _client;


    public static Layout get() {
        return GET_INSTANCE.INSTANCE;
    }

    public void addListener(TurnoutListener l) {
        _listeners.add(l);
    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    private String decodeValue(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
    }

    public boolean getTurnout(int turnout) {
        try {
            String turnoutStr = encodeValue(TURNOUT_PREFIX + Integer.toString(turnout));
            return _turnouts.containsKey(encodeValue(turnoutStr))
                    && _turnouts.get(encodeValue(turnoutStr)) == 4;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setTurnout(int turnout, boolean value) {
        try {
            String turnoutStr = TURNOUT_PREFIX + Integer.toString(turnout);

            int newState = value ? 4 : 2;

            System.out.format("Set turnout %d to %d%n", turnout, newState);

            HttpRequest request = HttpRequest.newBuilder()
                      .uri(new URI("http://localhost:12080/json/turnout/" + encodeValue(turnoutStr)))
                    .version(HttpClient.Version.HTTP_2)
                    .header("content-type", "application/json;charset=utf-8")
                    .timeout(Duration.of(2, SECONDS))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"type\":\"turnout\","
                                    + "\"method\":\"post\","
                                    + "\"data\":{"
                                        + "\"name\":\"" + encodeValue(turnoutStr) +"\","
                                        + "\"state\":\"" + Integer.toString(newState) + "\"}}"))
                    .build();


            HttpResponse<String> response =  _client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.format("StatusCode: %d, Body: %s%n", response.statusCode(), response.body());

            if (response.statusCode() == 200) {
                JSONObject jo = new JSONObject(response.body());
                if ("turnout".equals(jo.getString("type"))) {
                    JSONObject data = jo.getJSONObject("data");
                    String turnoutStr2 = data.getString("name");
                    if (!turnoutStr.equals(turnoutStr2)) {
                        System.out.format("Error: Response is not about the same turnout as the request. Request: %s, Response: %s%n",
                                turnoutStr, turnoutStr2);
                    }
                    int state = data.getInt("state");
                    if (!_turnouts.containsKey(turnoutStr2) || _turnouts.get(turnoutStr2) != state) {
                        _turnouts.put(turnoutStr2, state);
                        System.out.format("Turnout: %s, state: %d%n", turnoutStr2, state);
                        for (TurnoutListener l : _listeners) {
                            l.state(turnout, value);
                        }
                    }
                }
            } else if (response.statusCode() == 404) {
                JSONObject jo = new JSONObject(response.body());
                if ("error".equals(jo.getString("type"))) {
                    JSONObject data = jo.getJSONObject("data");
                    if (data.getInt("code") == 404) {
                        // The turnout doesn't exists. Create it.
                        HttpRequest request2 = HttpRequest.newBuilder()
                                  .uri(new URI("http://localhost:12080/json/turnout/" + encodeValue(turnoutStr)))
                                .version(HttpClient.Version.HTTP_2)
                                .header("content-type", "application/json;charset=utf-8")
                                .timeout(Duration.of(2, SECONDS))
                                .PUT(HttpRequest.BodyPublishers.ofString(
                                        "{\"type\":\"turnout\","
                                                + "\"method\":\"put\","
                                                + "\"data\":{"
//                                                    + "\"name\":\"" + encodeValue(turnoutStr) + "\"}}"))
                                                    + "\"name\":\"" + encodeValue(turnoutStr) +"\","
                                                    + "\"userName\":\"Hej\"}}"))
//                                                    + "\"state\":\"" + Integer.toString(newState) + "\"}}"))
                                .build();


                        HttpResponse<String> response2 =  _client.send(request2, HttpResponse.BodyHandlers.ofString());
                        System.out.format("Try to create turnout %s. StatusCode: %d. Response: %s%n", turnoutStr, response2.statusCode(), response.body());
                    }
/*
                    String turnoutStr2 = data.getString("name");
                    if (!turnoutStr.equals(turnoutStr2)) {
                        System.out.format("Error: Response is not about the same turnout as the request. Request: %s, Response: %s%n",
                                turnoutStr, turnoutStr2);
                    }
                    int state = data.getInt("state");
                    if (!_turnouts.containsKey(turnoutStr2) || _turnouts.get(turnoutStr2) != state) {
                        _turnouts.put(turnoutStr2, state);
                        System.out.format("Turnout: %s, state: %d%n", turnoutStr2, state);
                        for (TurnoutListener l : _listeners) {
                            l.state(turnout, value);
                        }
                    }
*/
                }
            } else {
                System.out.format("Invalid status code: %d%n", response.statusCode());
                System.out.format("StatusCode: %d, Body: %s%n", response.statusCode(), response.body());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Layout initWebClient() {
        // https://www.baeldung.com/java-9-http-client
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html

        _client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

        Runnable task = () -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:12080/json/turnout"))
    //                    .uri(new URI("http://localhost:12080/json/turnout/MT+1"))
//////                        .uri(new URI("http://localhost:12080/json/turnout/MT%2B1"))
                        .version(HttpClient.Version.HTTP_2)
                        .header("content-type", "application/json;charset=utf-8")
                        .timeout(Duration.of(2, SECONDS))
                        .GET()
    //                    .POST(HttpRequest.BodyPublishers.ofString("Sample request body"))
                        .build();


                HttpResponse<String> response =  _client.send(request, HttpResponse.BodyHandlers.ofString());

//                var headers = response.headers().map();
//                for (var entry : headers.entrySet()) {
//                    System.out.format("Header: %s, %s%n", entry.getKey(), entry.getValue());
//                }

//                System.out.format("StatusCode: %d, Body: %s%n", response.statusCode(), response.body());

                if (response.statusCode() == 200) {
                    JSONArray array = new JSONArray(response.body());
//                    JSONObject jo = new JSONObject(response.body());
//                    for (String key : jo.keySet()) {
//                        System.out.format("Key: %s%n", key);
//                    }

                    for (int i=0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        if ("turnout".equals(jo.getString("type"))) {
                            JSONObject data = jo.getJSONObject("data");
                            String turnoutStr = decodeValue(data.getString("name"));
                            int turnout = -1;
                            if (turnoutStr.startsWith(TURNOUT_PREFIX)) {
                                turnout = Integer.parseInt(turnoutStr.substring(TURNOUT_PREFIX.length()));
                            }
                            int state = data.getInt("state");
                            if (!_turnouts.containsKey(turnoutStr) || _turnouts.get(turnoutStr) != state) {
                                _turnouts.put(turnoutStr, state);
                                for (TurnoutListener l : _listeners) {
                                    l.state(turnout, state == 4);
                                }
                                System.out.format("Turnout: %s, state: %d%n", turnoutStr, state);
                            }
                        }
                    }
                } else {
                    System.out.format("Invalid status code: %d%n", response.statusCode());
                }
            } catch (URISyntaxException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long delay  = 2000L;    // Give JMRI time to start up and read all the turnouts.
        long period = 300L;
        executor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);

        return this;
    }


    private static class GET_INSTANCE {

        private static Layout INSTANCE = new Layout().initWebClient();

    }

}

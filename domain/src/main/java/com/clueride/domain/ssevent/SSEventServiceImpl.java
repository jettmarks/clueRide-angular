/*
 * Copyright 2018 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 9/15/18.
 */
package com.clueride.domain.ssevent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * Sends formatted JSON string representing expected SSE event message to Broadcast server for SSE.
 *
 * Picks up the SSE Host information from System Property.
 */
public class SSEventServiceImpl implements SSEventService {
    private static final Logger LOGGER = Logger.getLogger(SSEventServiceImpl.class);
    private final String sseHost;

    public SSEventServiceImpl() {
        this.sseHost = System.getProperty("sse-host");
        LOGGER.debug("Communicating with SSE server at " + sseHost);
    }

    @Override
    public Integer sendTeamReadyEvent(Integer outingId) {
        return sendEvent(outingId, eventMessageFromString("Team Assembled", outingId));
    }

    @Override
    public Integer sendArrivalEvent(Integer outingId) {
        return sendEvent(outingId, eventMessageFromString("Arrival", outingId));
    }

    @Override
    public Integer sendDepartureEvent(Integer outingId) {
        return sendEvent(outingId, eventMessageFromString("Departure", outingId));
    }

    private String eventMessageFromString(String event, Integer outingId) {
        return String.format("{\"event\":\"%s\",\"outingId\":%d}", event, outingId);
    }

    /**
     * Bundles up the formatted message for sending as a post to the game state broadcast URL.
     * @param outingId Identifier for the Outing which tells which channel to broadcast.
     * @param message JSON-formatted plain text to be sent as the message.
     * @return the HTTP Response code (exception thrown if it's not 200).
     */
    private Integer sendEvent(
            Integer outingId,
            String message
    ) {
        Integer responseCode = 500;
        try {
            URL url = new URL("http://" + sseHost + "/rest/game-state-broadcast/" + outingId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            OutputStream os = conn.getOutputStream();
            os.write(message.getBytes());
            os.flush();

            responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + responseCode);
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()
                    )
            );

            String output;
            LOGGER.debug("Output from SSE Server:");
            while ((output = br.readLine()) != null) {
                LOGGER.debug(output);
            }

            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseCode;
    }

}

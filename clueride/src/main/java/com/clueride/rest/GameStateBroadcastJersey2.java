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
 * Created by jett on 2/25/18.
 */
package com.clueride.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

import com.clueride.rest.helper.ServerSentEventChannel;

/**
 * Broadcasts posted Game State changes to each of the clients who have subscribed to the events for a given Outing ID.
 */
@Singleton
@Path("game-state-broadcast")
public class GameStateBroadcastJersey2 {
    private static final Logger LOGGER = Logger.getLogger(GameStateBroadcastJersey2.class);

    private Map<Integer,SseBroadcaster> broadcasterMap = new HashMap<>();
    private Map<Integer,ServerSentEventChannel> channelMap = new HashMap<>();

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    @Path("{outingId}")
    public EventOutput subscribeToOutingIdChannel(
            @PathParam("outingId") Integer outingId,
            @QueryParam("r") final String requestId
    ) {
        LOGGER.debug("Outing ID: " + outingId + "  Request ID: " + requestId);
        ServerSentEventChannel channel = getEventChannel(outingId);
        return channel.getEventOutput();
    }

    private void eventGenerator(final Integer outingId) {
        final EventOutput eventOutput = getEventChannel(outingId).getEventOutput();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        LOGGER.info("About to write message");
                        final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
                        eventBuilder.id("" + i);
                        eventBuilder.name("message-to-client");
                        eventBuilder.data(String.class, "Hello world " + i + "!");
                        final OutboundEvent event = eventBuilder.build();
                        eventOutput.write(event);
                        LOGGER.info("Message sent: " + event.toString());
                    } catch (IOException e) {
                        throw new RuntimeException("Error when writing the event.", e);
                    } catch (NullPointerException npe) {
                        LOGGER.warn("Event Not sent against " + outingId, npe);
                    }

                    try {
                        TimeUnit.SECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        // Eat exception
                    }
                }
            }
        }).start();
    }

    @POST
    @Path("{outingId}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String broadcastMessage(
            String message,
            @PathParam("outingId") Integer outingId
    ) {
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent event = eventBuilder.name("message")
                .mediaType(MediaType.TEXT_PLAIN_TYPE)
                .data(String.class, message)
                .build();

        SseBroadcaster broadcaster = getSseBroadcaster(outingId);
        broadcaster.broadcast(event);
        return "Message '" + message + "' has been broadcast.";
    }

    SseBroadcaster getSseBroadcaster(Integer outingId) {
        SseBroadcaster broadcaster;
        if (broadcasterMap.containsKey(outingId)) {
            broadcaster = broadcasterMap.get(outingId);
        } else {
            broadcaster = new SseBroadcaster();
            broadcasterMap.put(outingId, broadcaster);
        }
        return broadcaster;
    }

    ServerSentEventChannel getEventChannel(Integer outingId) {
        ServerSentEventChannel channel;
        if (channelMap.containsKey(outingId)) {
            channel = channelMap.get(outingId);
        } else {
            channel = new ServerSentEventChannel(outingId);
            channelMap.put(outingId, channel);
            eventGenerator(outingId);
        }
        return channel;
    }

//    @GET
//    @Path("{outingId}")
//    @Produces(SseFeature.SERVER_SENT_EVENTS)
//    public EventOutput listenToBroadcast(
//            @PathParam("outingId") Integer outingId,
//            @QueryParam("lastEventId") String lastEventId,
//            @QueryParam("r") String unsure
//    ) {
//        final EventOutput eventOutput = new EventOutput();
//        LOGGER.info("R: " + unsure);
//        SseBroadcaster broadcaster = getSseBroadcaster(outingId);
//        broadcaster.add(eventOutput);
//        return eventOutput;
//    }

}

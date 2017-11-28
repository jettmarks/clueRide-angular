/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 11/25/17.
 */
package com.clueride.domain.badge.event;

import java.security.Principal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.principal.PrincipalService;

/**
 * Implementation of Badge Event service which dispatches events from a queue that is populated by clients.
 *
 * The dispatch involves persisting and logging of the events.
 */
@Singleton
public class BadgeEventServiceImpl implements BadgeEventService {
    private static final Logger LOGGER = Logger.getLogger(BadgeEventServiceImpl.class);
    private static BlockingQueue<BadgeEvent.Builder> eventQueue = new LinkedTransferQueue<>();
    private static Thread workerThread = null;
    private boolean runnable = true;
    private final BadgeEventStore badgeEventStore;
    private final MemberService memberService;
    private final PrincipalService principalService;

    @Inject
    public BadgeEventServiceImpl(
            BadgeEventStore badgeEventStore,
            MemberService memberService,
            PrincipalService principalService
    ) {
        this.badgeEventStore = badgeEventStore;
        this.memberService = memberService;
        this.principalService = principalService;

        if (workerThread == null) {
            workerThread = new Thread(
                    new BadgeEventServiceImpl.Worker()
            );
            workerThread.start();
        }

    }

    @Override
    public void send(BadgeEvent.Builder badgeEvent) {
        try {
            synchronized(eventQueue) {
                eventQueue.put(badgeEvent);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BadgeEvent getBadgeEventById(Integer badgeEventId) {
        BadgeEvent.Builder badgeEventBuilder = badgeEventStore.getById(badgeEventId);
        fillDbBuilder(badgeEventBuilder);
        return badgeEventBuilder.build();
    }

    private void fillDbBuilder(BadgeEvent.Builder badgeEventBuilder) {
        Member member = memberService.getMember(badgeEventBuilder.getMemberId());
        badgeEventBuilder.withPrincipal(
                principalService.getPrincipalForEmailAddress(
                        member.getEmailAddress()
                )
        );
    }

    private void fillClientBuilder(BadgeEvent.Builder badgeEventBuilder) {
        Principal principal = badgeEventBuilder.getPrincipal();
        Integer memberId = memberService.getMemberByEmail(
                principal.getName()
        ).getId();

        badgeEventBuilder.withMemberId(memberId);
    }

    public class Worker implements Runnable {

        @Override
        public void run() {
            while (runnable) {
                BadgeEvent.Builder badgeEventBuilder = null;
                try {
                    badgeEventBuilder = eventQueue.take();
                    fillClientBuilder(badgeEventBuilder);
                    LOGGER.info("Captured Event: " + badgeEventBuilder.toString());
                    badgeEventStore.add(badgeEventBuilder);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    runnable = false;
                } catch (RuntimeException rte) {
                    LOGGER.error("Problem Storing Badge Event: " + badgeEventBuilder.getTimestamp(), rte);
                    throw(rte);
                }

            }
        }

    }


}

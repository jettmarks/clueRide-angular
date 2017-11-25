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
 * Created by jett on 11/18/17.
 */
package com.clueride.aop;

import javax.inject.Inject;

import com.clueride.aop.badge.BadgeCapture;

/**
 * This class provides a method which is to be annotated by the classes under test.
 */
public class AopServiceConsumerImpl implements AopServiceConsumer {
    private AopDummyService dummyService;

    @Inject
    public AopServiceConsumerImpl(
            AopDummyService dummyService
    ) {
        /**
         * It is important that we inject something because Guice takes
         * the short-cut to invoke a no arg constructor otherwise and this leads to the @BadgeCapture method interceptor
         * never being added.
         */
        this.dummyService = dummyService;
    }

    @Override
    @BadgeCapture
    public Integer performService(Integer input) {
        Integer response = input;
        dummyService.doSomeWork();
        return response;
    }

}

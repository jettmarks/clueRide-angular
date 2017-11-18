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
 * Created by jett on 11/17/17.
 */
package com.clueride.aop;

import com.google.inject.AbstractModule;

import com.clueride.aop.badge.BadgeCapture;
import com.clueride.aop.badge.BadgeCaptureInterceptor;
import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

/**
 * Guice Module for defining the AOP components.
 */
public class AopModule extends AbstractModule {

    @Override
    protected void configure() {
        BadgeCaptureInterceptor badgeCaptureInterceptor = new BadgeCaptureInterceptor();
        bindInterceptor(
                any(),
                annotatedWith(BadgeCapture.class),
                badgeCaptureInterceptor
        );
    }

}

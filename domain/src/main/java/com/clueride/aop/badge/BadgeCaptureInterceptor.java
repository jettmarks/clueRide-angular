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
package com.clueride.aop.badge;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

/**
 * Method Interceptor responsible for capturing events that count toward the awarding of badges.
 */
public class BadgeCaptureInterceptor implements MethodInterceptor {
    private static final Logger LOGGER = Logger.getLogger(BadgeCaptureInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        String className = invocation.getMethod().getDeclaringClass().getCanonicalName();

        LOGGER.info("Called method " + methodName + " of class " + className);
        Object returnValue = invocation.proceed();
        LOGGER.info("Return Value: " + returnValue.toString());
        return returnValue;
    }
}

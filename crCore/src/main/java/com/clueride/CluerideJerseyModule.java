/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 12/10/15.
 */
package com.clueride;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.apache.log4j.Logger;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import com.clueride.infrastructure.AuthenticationFilter;
import com.clueride.infrastructure.CoreGuiceSetup;

/**
 * Gets Jersey 2 (HK2) and Guice to play together.
 *
 * See https://github.com/aluedeke/jersey2-guice-example.
 */
public class CluerideJerseyModule extends ResourceConfig {
    private static final Logger LOGGER = Logger.getLogger(CluerideJerseyModule.class);

    @Inject
    public CluerideJerseyModule(
            @Context ServletContext servletContext,
            ServiceLocator serviceLocator
    ) {
        super(
                MultiPartFeature.class,
                CluerideObjectMapperProvider.class,
                JacksonFeature.class
        );

        LOGGER.info("Creating Clueride Module");

        LOGGER.info("Registering JAXB");
        register(JacksonJaxbJsonProvider.class);

        LOGGER.info("Scanning Jersey REST Packages");
        packages("com.clueride.rest");

        LOGGER.info("Preparing Guice-HK2 Bridge");
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);

        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(CoreGuiceSetup.getGuiceInjector(servletContext));
        dumpBindings();
        LOGGER.info("Jersey Module is Prepared");

        LOGGER.info("Adding Auth Filter");
        register(AuthenticationFilter.class);
    }

    void dumpBindings() {
        Injector injector = CoreGuiceSetup.getGuiceInjector(null);
        Map<Key<?>,Binding<?>> map = injector.getBindings();
        for(Map.Entry<Key<?>, Binding<?>> e : map.entrySet()) {
            LOGGER.debug(e.getValue());
        }
    }

}

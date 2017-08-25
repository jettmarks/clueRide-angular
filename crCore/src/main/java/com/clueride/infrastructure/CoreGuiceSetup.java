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
 * Created by jett on 12/5/15.
 */
package com.clueride.infrastructure;

import javax.servlet.ServletContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import com.clueride.CluerideGuiceModule;
import com.clueride.config.GeoToolsGuiceModule;
import com.clueride.domain.DomainGuiceModule;

/**
 * Configure the Guice container.
 */
public class CoreGuiceSetup extends GuiceServletContextListener {
    private static Injector myInjector;

    @Override
    protected Injector getInjector() {
        myInjector =  Guice.createInjector(
                new ServletModule() { },
                new DomainGuiceModule(),
                new GeoToolsGuiceModule(),
                new CluerideGuiceModule(),
                new ServiceGuiceModule()
        ); // <-- Adding other Guice Dependency Injection Modules
        return myInjector;
    }

    public static Injector getGuiceInjector(ServletContext servletContext) {
        if (myInjector == null) {
            new CoreGuiceSetup().getInjector();
        }
        return myInjector;
    }
}

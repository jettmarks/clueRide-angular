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
 * Created by jett on 8/6/17.
 */
package com.clueride.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mock;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.DomainGuiceProviderModule;
import com.clueride.domain.GamePath;
import com.clueride.domain.dev.Node;
import com.clueride.domain.factory.PointFactory;
import com.clueride.io.PojoJsonService;
import com.clueride.service.NetworkEval;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Configures bindings for the Service module.
 */
public class ServiceGuiceModuleTest extends AbstractModule {
    @Mock
    private ClueStore clueStore;

    @Mock
    private NetworkEval networkEval;

    @Mock
    private PojoJsonService pojoJsonService;

    @Mock
    private NodeStore nodeStore;

    @Override
    protected void configure() {
        initMocks(this);
        install(new DomainGuiceProviderModule());
        bind(ClueStore.class).toInstance(clueStore);
        bind(NetworkEval.class).toInstance(networkEval);
        bind(NodeStore.class).toInstance(nodeStore);
        bind(PojoJsonService.class).toInstance(pojoJsonService);
    }

    // Handled by DomainGuiceProviderModule
//    @Provides
//    private Member getMember() {
//        return Member.Builder.builder()
//                .withFirstName("ClueRide")
//                .withLastName("Guest")
//                .withEmailAddress("guest.dummy@clueride.com")
//                .withBadges(Collections.singletonList(Badge.LOCATION_EDITOR))
//                .withDisplayName("ClueRide Guest")
//                .withPhone("123-456-7890")
//                .build();
//    }

    @Provides
    private GamePath.Builder getGamePathBuilder(
            NetworkEval networkEval
    ) {
        Integer startNodeId = 30;
        Integer endNodeId = 8;

        return GamePath.Builder.getBuilder(networkEval)
                .withStartNodeId(startNodeId)
                .withEndNodeId(endNodeId)
                .addEdgeId(39)
                .addEdgeId(43)
                .addEdgeId(79)
                .addEdgeId(78)
                .addEdgeId(87)
                ;
    }

    @Provides
    private Node getNode() {
        return new DefaultGeoNode(
                PointFactory.getJtsInstance(33.0, -84.0, 300.0)
        );
    }
}

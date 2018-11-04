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
 * Created by jett on 11/3/18.
 */
package com.clueride.rest;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.hibernate.boot.Metadata;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;
import com.clueride.infrastructure.MetadataExtractorIntegrator;

/**
 * Web Service for {@link Member} operations.
 */
@Path("member")
public class MemberWebService {
    private static final Logger LOGGER = Logger.getLogger(MemberWebService.class);
    private final MemberService memberService;

    @Inject
    public MemberWebService(
            MemberService memberService
    ) {
        this.memberService = memberService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{memberId}")
    public Member getMemberById(@PathParam("memberId") Integer memberId) {
        dumpEntities();
        return memberService.getMember(memberId);
    }

    private void dumpEntities() {
        Metadata metadata = MetadataExtractorIntegrator.INSTANCE.getMetadata();

        for ( PersistentClass persistentClass : metadata.getEntityBindings()) {

            Table table = persistentClass.getTable();

            LOGGER.info( String.format("Entity: {} is mapped to table: {}",
                    persistentClass.getClassName(),
                    table.getName())
            );

            for(Iterator propertyIterator = persistentClass.getPropertyIterator();
                propertyIterator.hasNext(); ) {
                Property property = (Property) propertyIterator.next();

                for(Iterator columnIterator = property.getColumnIterator();
                    columnIterator.hasNext(); ) {
                    Column column = (Column) columnIterator.next();

                    LOGGER.info( String.format("Property: {} is mapped on table column: {} of type: {}",
                            property.getName(),
                            column.getName(),
                            column.getSqlType())
                    );
                }
            }
        }
    }

}

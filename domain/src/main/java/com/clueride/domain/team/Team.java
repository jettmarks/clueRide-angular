/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created 11/17/15.
 */
package com.clueride.domain.team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.concurrent.Immutable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.account.member.Member;
import static java.util.Objects.requireNonNull;

@Immutable
public class Team {
    private final Integer id;
    private final String name;
    private final List<Member> members;

    public Team(Builder builder) {
        this.id = requireNonNull(builder.getId());
        this.name = requireNonNull(builder.getName());
        this.members = requireNonNull(builder.getMembers());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void add(Member member) {
        members.add(member);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Entity(name="teamBuilder")
    @Table(name="team")
    public static final class Builder {
        @Id
        @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="team_pk_sequence")
        @SequenceGenerator(name="team_pk_sequence",sequenceName="team_id_seq", allocationSize = 1)
        private Integer id;

        private String name;

        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(
                name="team_membership",
                joinColumns = {@JoinColumn(name="team_id")},
                inverseJoinColumns = {@JoinColumn(name="member_id")}
        )
        private Set<Member.Builder> memberBuilders = new HashSet<>();

        /* Allows Jackson to construct. */
        public Builder() {}

        /* Normal pattern for constructing a Builder. */
        public static Builder builder() {
            return new Builder();
        }

        public Team build() {
            return new Team(this);
        }

        public Integer getId() {
            return id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public List<Member> getMembers() {
            List<Member> members = new ArrayList<>();
            for (Member.Builder builder: memberBuilders) {
                members.add(builder.build());
            }
            return members;
        }

        public Builder withMembers(Set<Member.Builder> memberBuilders) {
            this.memberBuilders = memberBuilders;
            return this;
        }

        public Builder withNewMember(Member newMember) {
            this.memberBuilders.add(Member.Builder.from(newMember));
            return this;
        }
    }
}

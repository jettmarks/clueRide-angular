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
 * Created by jett on 11/23/15.
 */
package com.clueride.domain.user.puzzle;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.user.answer.Answer;
import com.clueride.domain.user.answer.AnswerKey;
import com.clueride.domain.user.location.Location;
import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedClueIdProvider;

@Immutable
public class Puzzle {
    private Integer id;
    private String name;
    private String locationName;
    private String question;
    private List<Answer> answers = new ArrayList<>();
    private AnswerKey correctAnswer;
    private Integer points;

    private Puzzle(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.locationName = builder.getLocationBuilder().getName();
        this.question = builder.getQuestion();
        this.answers = builder.getAnswers();
        this.correctAnswer = builder.getCorrectAnswer();
        this.points = builder.getPoints();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getQuestion() {
        return question;
    }

    public AnswerKey getCorrectAnswer() {
        return correctAnswer;
    }

    public Integer getPoints() {
        return points;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Entity(name="PuzzleBuilder")
    @Table(name = "puzzle")
    public static final class Builder {
        @Id
        @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="puzzle_pk_sequence")
        @SequenceGenerator(name="puzzle_pk_sequence",sequenceName="puzzle_id_seq", allocationSize=1)
        private Integer id;

        private String name;
        private String question;

        @OneToMany(
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL,
                mappedBy = "puzzleBuilder"
        )
        private List<Answer> answers = new ArrayList<>();

        @Column(name = "correct_answer") private AnswerKey correctAnswer;
        private Integer points;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "location_id")
        private Location.Builder locationBuilder;

        @Transient
        private static IdProvider clueIdProvider = new MemoryBasedClueIdProvider();

        /* Public so Jackson can construct one of these. */
        public Builder() {
            id = clueIdProvider.getId();
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Puzzle instance) {
            return builder()
                    .withId(instance.id)
                    .withName(instance.name)
                    .withQuestion(instance.question)
                    .withCorrectAnswer(instance.correctAnswer)
                    .withAnswers(instance.answers)
                    .withPoints(instance.points)
                    ;
        }

        public String getQuestion() {
            return question;
        }

        public Builder withQuestion(String question) {
            this.question = question;
            return this;
        }

        public List<Answer> getAnswers() {
            return answers;
        }

        public Builder withAnswers(List<Answer> answers) {
            this.answers = answers;
            return this;
        }

        public AnswerKey getCorrectAnswer() {
            return correctAnswer;
        }

        public Builder withCorrectAnswer(AnswerKey correctAnswer) {
            this.correctAnswer = correctAnswer;
            return this;
        }

        public Integer getPoints() {
            return points;
        }

        public Builder withPoints(Integer points) {
            this.points = points;
            return this;
        }

        public Integer getId() {
            return id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withLocationBuilder(Location.Builder locationBuilder) {
            this.locationBuilder = locationBuilder;
            return this;
        }

        public Location.Builder getLocationBuilder() {
            return locationBuilder;
        }

        public String getName() {
            return name;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Puzzle build() {
            return new Puzzle(this);
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setQuestion(String question) {
            this.question = question;
            return this;
        }

        public Builder setAnswers(List<Answer> answers) {
            this.answers = answers;
            return this;
        }

        public Builder setCorrectAnswer(AnswerKey correctAnswer) {
            this.correctAnswer = correctAnswer;
            return this;
        }

        public Builder setPoints(Integer points) {
            this.points = points;
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}

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
package com.clueride.domain.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.clueride.service.IdProvider;
import com.clueride.service.MemoryBasedClueIdProvider;

public class Clue {
    private Integer id;
    private String name;
    private String question;
    private List<Answer> answers = new ArrayList<>();
    private AnswerKey correctAnswer;
    private List<URL> hintUrls = new ArrayList<>();
    private Integer points;

    private Clue(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.question = builder.getQuestion();
        this.answers = builder.getAnswers();
        this.correctAnswer = builder.getCorrectAnswer();
        this.hintUrls = builder.getHintUrls();
        this.points = builder.getPoints();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
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


    public List<URL> getHintUrls() {
        return hintUrls;
    }

    public static final class Builder {
        private Integer id;
        private String name;
        private String question;
        private List<Answer> answers = new ArrayList<>();
        private AnswerKey correctAnswer;
        private List<URL> hintUrls = new ArrayList<>();
        private Integer points;

        private static IdProvider clueIdProvider = new MemoryBasedClueIdProvider();

        /* Public so Jackson can construct one of these. */
        public Builder() {
            id = clueIdProvider.getId();
        }

        public static Builder builder() {
            return new Builder();
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

        public List<URL> getHintUrls() {
            return hintUrls;
        }

        public Builder withHintUrls(List<URL> hintUrls) {
            this.hintUrls = hintUrls;
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

        public String getName() {
            return name;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Clue build() {
            return new Clue(this);
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

        public Builder setHintUrls(List<URL> hintUrls) {
            this.hintUrls = hintUrls;
            return this;
        }

        public Builder setPoints(Integer points) {
            this.points = points;
            return this;
        }
    }
}

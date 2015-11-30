package com.clueride.domain.user;

import java.util.ArrayList;
import java.util.List;

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
 * Created by jett on 11/23/15.
 */
public class Clue {
    private Integer id;
    private String question;
    private List<Answer> answers = new ArrayList<>();
    private AnswerKey correctAnswer;
    Integer points;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public AnswerKey getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(AnswerKey correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}

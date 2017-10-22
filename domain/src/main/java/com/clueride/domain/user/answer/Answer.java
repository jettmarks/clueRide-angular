package com.clueride.domain.user.answer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.user.puzzle.Puzzle;

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
@Entity(name = "Answer")
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="answer_pk_sequence")
    @SequenceGenerator(name="answer_pk_sequence",sequenceName="answer_id_seq", allocationSize=1)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle_id")
    private Puzzle.Builder puzzleBuilder;

    @Column(name = "answer_key") private AnswerKey answerKey;
    @Column(name = "answer") private String answer;

    public Answer() {}

    public Answer(AnswerKey answerKey, String answer) {
        this.answerKey = answerKey;
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public Answer withId(Integer id) {
        this.id = id;
        return this;
    }

    public AnswerKey getKey() {
        return answerKey;
    }

    public Answer setKey(AnswerKey answerKey) {
        this.answerKey = answerKey;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public Answer setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public Answer withPuzzleBuilder(Puzzle.Builder puzzleBuilder) {
        this.puzzleBuilder = puzzleBuilder;
        return this;
    }

    public Answer withAnswerKey(AnswerKey answerKey) {
        this.answerKey = answerKey;
        return this;
    }

    public Answer withAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}

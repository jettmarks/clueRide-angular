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
 * Created by jett on 12/20/15.
 */
package com.clueride.domain.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.domain.user.answer.Answer;
import com.clueride.domain.user.answer.AnswerKey;
import static org.testng.Assert.assertEquals;

/**
 * Exercises the ClueTest class.
 */
public class ClueTest {

    private Clue toTest;
    private Clue.Builder clueBuilder;

    @BeforeMethod
    public void setUp() throws Exception {
        clueBuilder = populateClue();
        toTest = clueBuilder.build();
    }

    @Test
    public void testGetId() throws Exception {
        Integer expected = 1;
        Integer actual = toTest.getId();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetQuestion() throws Exception {
        String expected = clueBuilder.getQuestion();
        String actual = toTest.getQuestion();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetCorrectAnswer() throws Exception {
        AnswerKey expected = clueBuilder.getCorrectAnswer();
        AnswerKey actual = toTest.getCorrectAnswer();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetPoints() throws Exception {
        Integer expected = clueBuilder.getPoints();
        Integer actual = toTest.getPoints();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetAnswers() throws Exception {
        List<Answer> expected = clueBuilder.getAnswers();
        List<Answer> actual = toTest.getAnswers();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetHintUrls() throws Exception {
        List<URL> expected = clueBuilder.getHintUrls();
        List<URL> actual = toTest.getHintUrls();
        assertEquals(actual, expected);
    }

    private Clue.Builder populateClue() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(AnswerKey.A, "It's my bicycle"));
        answers.add(new Answer(AnswerKey.B, "The Art on the BeltLine"));
        answers.add(new Answer(AnswerKey.C, "The Ride Leader"));
        answers.add(new Answer(AnswerKey.D, "This application"));

        return Clue.Builder.builder()
                .withId(1)
                .withQuestion("Who is the fairest of them all?")
                .withAnswers(answers)
                .withCorrectAnswer(AnswerKey.B)
                .withPoints(1);
    }
}
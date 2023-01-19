package com.nps.answer.persistence;

import com.nps.answer.entity.Answer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AnswerCustomRepository {
    private final EntityManager em;

    public AnswerCustomRepository(EntityManager em) {
        this.em = em;
    }

    public static final String AND = " and ";

    public List<Answer> findAnswer(Long answerId, String response, Integer score, Long questionId) {
        String query = "select a from ANSWER a ";
        String condition = " where ";

        if (answerId != null) {
            query += condition + " a.answerId = :answerId ";
            condition = AND;
        }

        if (response != null) {
            query += condition + " a.response = :response";
            condition = AND;
        }

        if (score != null) {
            query += condition + " a.score = :score";
            condition = AND;
        }

        if (questionId != null) {
            query += condition + " a.questionId = :questionId";
        }

        TypedQuery<Answer> q = em.createQuery(query, Answer.class);

        if (answerId != null) {
            q.setParameter("answerId", answerId);
        }

        if (response != null) {
            q.setParameter("response", response);
        }

        if (score != null) {
            q.setParameter("score", score);
        }

        if (questionId != null) {
            q.setParameter("questionId", questionId);
        }

        return q.getResultList();
    }
}

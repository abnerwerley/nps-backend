package com.nps.question.persistence;

import com.nps.question.entity.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class QuestionCustomRepository {
    private final EntityManager em;

    public QuestionCustomRepository(EntityManager em) {
        this.em = em;
    }

    public static final String AND = " and ";

    public List<Question> findQuestions(Long questionId, String enquiry) {
        String query = "select q from QUESTION q ";
        String condition = " where ";

        if (questionId != null) {
            query += condition + " q.questionId = :questionId ";
            condition = AND;
        }

        if (enquiry != null) {
            query += condition + " q.enquiry like concat('%', :enquiry, '%') ";
            condition = AND;
        }

        TypedQuery<Question> q = em.createQuery(query, Question.class);

        if (questionId != null) {
            q.setParameter("questionId", questionId);
        }

        if (enquiry != null) {
            q.setParameter("enquiry", enquiry);
        }

        return q.getResultList();
    }
}

package com.questionnaire;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.questionnaire.exceptions.IllegalModelException;
import com.questionnaire.model.BooleanQuestion;
import com.questionnaire.model.MultipleChoiceChoice;
import com.questionnaire.model.MultipleChoiceQuestion;
import com.questionnaire.model.NumericQuestion;
import com.questionnaire.model.User;
import com.questionnaire.services.BooleanQuestionService;
import com.questionnaire.services.MultipleChoiceChoiceService;
import com.questionnaire.services.MultipleChoiceQuestionService;
import com.questionnaire.services.NumericQuestionService;
import com.questionnaire.services.UserService;

@Component
public class Autopopulate {
    
    @Autowired
    private UserService userService;

    @Autowired
    private BooleanQuestionService booleanQuestionService;

    @Autowired
    private NumericQuestionService numericQuestionService;

    @Autowired
    private MultipleChoiceQuestionService multipleChoiceQuestionService;

    @Autowired
    private MultipleChoiceChoiceService multipleChoiceChoiceService; 

    @PostConstruct
    public void auto() throws IllegalModelException {
        User user = new User();

        List<String> roles = new ArrayList<String>();
        roles.add("CUSTOMER");
        roles.add("ADMIN");

        user.setId(0);
        user.setUsername("dave");
        user.setPassword("1234");
        user.setRoles(roles);

        userService.save(user);

        BooleanQuestion bq1 = new BooleanQuestion();
        bq1.setTitle("¿Te gusta el Betis Balompié?");
        bq1 = booleanQuestionService.save(bq1);

        BooleanQuestion bq2 = new BooleanQuestion();
        bq2.setTitle("¿Te gusta el batido de fresa?");
        bq2 = booleanQuestionService.save(bq2);

        BooleanQuestion bq3 = new BooleanQuestion();
        bq3.setTitle("¿Has estado en Murcia?");
        bq3 = booleanQuestionService.save(bq3);

        NumericQuestion nq1 = new NumericQuestion();
        nq1.setTitle("¿Cuánto te gusta la tortilla de patatas?");
        nq1 = numericQuestionService.save(nq1);

        NumericQuestion nq2 = new NumericQuestion();
        nq2.setTitle("¿Cuánto te gusta levantarte a las 5 AM?");
        nq2 = numericQuestionService.save(nq2);

        NumericQuestion nq3 = new NumericQuestion();
        nq3.setTitle("¿Dónde está el Betis?");
        nq3 = numericQuestionService.save(nq3);

        MultipleChoiceQuestion mq1 = new MultipleChoiceQuestion();
        mq1.setTitle("¿Betis?");
        mq1 = multipleChoiceQuestionService.save(mq1);

        MultipleChoiceChoice mc1 = new MultipleChoiceChoice();
        mc1.setTitle("Sí");
        mc1.setQuestion(mq1);
        mc1 = multipleChoiceChoiceService.save(mc1);

        MultipleChoiceChoice mc2= new MultipleChoiceChoice();
        mc2.setTitle("No");
        mc2.setQuestion(mq1);
        mc2 = multipleChoiceChoiceService.save(mc2);
    }
}

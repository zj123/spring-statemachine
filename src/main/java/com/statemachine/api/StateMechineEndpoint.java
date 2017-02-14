package com.statemachine.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.statemachine.enums.Events;
import com.statemachine.enums.States;

@RestController
public class StateMechineEndpoint {

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @RequestMapping("/state")
    public String feedAndGetState() throws Exception {
        stateMachine.getExtendedState().getVariables().put("id", 11);
        stateMachine.sendEvent(Events.E1);
        return stateMachine.getState().getId().toString();
    }
}

package com.statemachine.config;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import com.statemachine.enums.Events;
import com.statemachine.enums.States;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration().autoStartup(true).listener(myLinster());
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates().initial(States.SI).states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions.withExternal().source(States.SI).target(States.UNPAID).event(Events.E1).action(myaction1()).

                and().withExternal().source(States.UNPAID).target(States.CANCELLED).event(Events.E2).

                and().withExternal().source(States.UNPAID).target(States.COMPLETE).event(Events.E3).

                and().withExternal().source(States.UNPAID).target(States.QUERYING).event(Events.E4).

                and().withExternal().source(States.QUERYING).target(States.COMPLETE).event(Events.E5);
    }

    private Action<States, Events> myaction1() {
        return new Action<States, Events>() {

            @Override
            public void execute(StateContext<States, Events> context) {
                logger.info("创建订单" + context.getExtendedState().getVariables().get("id"));
            }
        };
    }

    @Bean
    public StateMachineListener<States, Events> myLinster() {
        return new StateMachineListenerAdapter<States, Events>() {

            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                System.out.println("State change to " + to.getId());
            }

        };
    }
}

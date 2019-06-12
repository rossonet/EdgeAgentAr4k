package org.ar4k.agent.config;

import java.util.EnumSet;
import java.util.Set;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaEvents;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableWithStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@EnableStateMachine
/**
 * State machine per gestore principale del ciclo di vita
 * 
 * @author andrea
 *
 */
public class AnimaStateMachineConfig extends EnumStateMachineConfigurerAdapter<AnimaStates, AnimaEvents> {
  private static final Logger logger = Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  @Override
  public void configure(StateMachineConfigurationConfigurer<AnimaStates, AnimaEvents> config) throws Exception {
    config.withConfiguration().autoStartup(true).listener(listener());
  }

  @Override
  public void configure(StateMachineStateConfigurer<AnimaStates, AnimaEvents> states) throws Exception {
    states.withStates().initial(AnimaStates.INIT)// .end(AnimaStates.KILLED).end(AnimaStates.FAULTED)
        .states(EnumSet.allOf(AnimaStates.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<AnimaStates, AnimaEvents> transitions) throws Exception {
    transitions.withExternal().source(AnimaStates.INIT).target(AnimaStates.STAMINAL).event(AnimaEvents.BOOTSTRAP).and()
        .withExternal().source(AnimaStates.INIT).target(AnimaStates.KILLED).event(AnimaEvents.STOP).and().withExternal()
        .source(AnimaStates.INIT).target(AnimaStates.FAULTED).event(AnimaEvents.EXCEPTION).and().withExternal()
        .source(AnimaStates.STAMINAL).target(AnimaStates.CONFIGURED).event(AnimaEvents.SETCONF).and().withExternal()
        .source(AnimaStates.STAMINAL).target(AnimaStates.KILLED).event(AnimaEvents.STOP).and().withExternal()
        .source(AnimaStates.STAMINAL).target(AnimaStates.FAULTED).event(AnimaEvents.EXCEPTION).and().withExternal()
        .source(AnimaStates.CONFIGURED).target(AnimaStates.RUNNING).event(AnimaEvents.START).and().withExternal()
        .source(AnimaStates.CONFIGURED).target(AnimaStates.KILLED).event(AnimaEvents.STOP).and().withExternal()
        .source(AnimaStates.CONFIGURED).target(AnimaStates.FAULTED).event(AnimaEvents.EXCEPTION).and().withExternal()
        .source(AnimaStates.RUNNING).target(AnimaStates.STASIS).event(AnimaEvents.PAUSE).and().withExternal()
        .source(AnimaStates.RUNNING).target(AnimaStates.KILLED).event(AnimaEvents.STOP).and().withExternal()
        .source(AnimaStates.RUNNING).target(AnimaStates.FAULTED).event(AnimaEvents.EXCEPTION).and().withExternal()
        .source(AnimaStates.STASIS).target(AnimaStates.KILLED).event(AnimaEvents.HIBERNATION).and().withExternal()
        .source(AnimaStates.STASIS).target(AnimaStates.RUNNING).event(AnimaEvents.START).and().withExternal()
        .source(AnimaStates.STASIS).target(AnimaStates.FAULTED).event(AnimaEvents.STOP);
  }

  @Bean
  public StateMachineListener<AnimaStates, AnimaEvents> listener() {
    return new StateMachineListenerAdapter<AnimaStates, AnimaEvents>() {
      @Override
      public void stateChanged(State<AnimaStates, AnimaEvents> from, State<AnimaStates, AnimaEvents> to) {
        logger.info("State change to " + to.getId());
      }
    };
  }

  @Bean
  public Action<AnimaStates, AnimaEvents> errorAction() {
    return ctx -> System.out.println("Error in change action " + ctx.getSource().getId() + ctx.getException());
  }

  @Bean
  public Guard<AnimaStates, AnimaEvents> guardStartType() {
    return new Guard<AnimaStates, AnimaEvents>() {
      @Override
      public boolean evaluate(StateContext<AnimaStates, AnimaEvents> context) {
        boolean ritorno = false;
        logger.info("Evaluate change state: " + context.getEvent() + " on " + context.getSource().getId());
        ritorno = true;
        return ritorno;
      }
    };

  }

  public Set<AnimaStates> getBaseStates() {
    return EnumSet.allOf(AnimaStates.class);
  }

}
package org.ar4k.agent.config;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaEvents;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.Ar4kStaticLoggerBinder;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachine;
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
public class AnimaStateMachineConfig extends EnumStateMachineConfigurerAdapter<AnimaStates, AnimaEvents> {
  private static final Logger logger = Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private Set<AnimaStates> baseStates = null;

  private Set<AnimaStates> runningStates = null;

  @PostConstruct
  private void postConstruct() {
    runningStates = new HashSet<AnimaStates>();
    runningStates.add(AnimaStates.SERVICE);
    runningStates.add(AnimaStates.CONSOLE);
    runningStates.add(AnimaStates.LAMBDA);
    runningStates.add(AnimaStates.BOT);
    baseStates = new HashSet<AnimaStates>();
    baseStates.add(AnimaStates.INIT);
    baseStates.add(AnimaStates.RUNNING);
    baseStates.add(AnimaStates.KILLED);
    baseStates.add(AnimaStates.STARTING);
    baseStates.add(AnimaStates.STAMINAL);
    baseStates.add(AnimaStates.CONFIGURED);
    baseStates.add(AnimaStates.PAUSED);
    baseStates.add(AnimaStates.STOPED);
    baseStates.add(AnimaStates.FAULTED);
    baseStates.add(AnimaStates.STASIS);
    baseStates.add(AnimaStates.BOT);
  }

  @Override
  public void configure(StateMachineConfigurationConfigurer<AnimaStates, AnimaEvents> config) throws Exception {
    config.withConfiguration().autoStartup(true).listener(listener());
  }

  @Override
  public void configure(StateMachineStateConfigurer<AnimaStates, AnimaEvents> states) throws Exception {
    states.withStates().initial(AnimaStates.INIT).choice(AnimaStates.RUNNING).end(AnimaStates.KILLED)
        .states(baseStates);
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<AnimaStates, AnimaEvents> transitions) throws Exception {
    transitions.withExternal().source(AnimaStates.INIT).target(AnimaStates.STARTING).event(AnimaEvents.BOOTSTRAP).and()
        .withExternal().source(AnimaStates.STARTING).target(AnimaStates.STAMINAL).event(AnimaEvents.BORN).and()
        .withExternal().source(AnimaStates.STAMINAL).target(AnimaStates.CONFIGURED).event(AnimaEvents.SETCONF).and()
        .withExternal().source(AnimaStates.CONFIGURED).target(AnimaStates.RUNNING).event(AnimaEvents.START).and()
        .withChoice().source(AnimaStates.RUNNING).first(AnimaStates.SERVICE, guardStartType())
        .then(AnimaStates.CONSOLE, guardStartType()).then(AnimaStates.LAMBDA, guardStartType())
        .then(AnimaStates.BOT, guardStartType()).last(AnimaStates.FAULTED).and().withExternal()
        .source(AnimaStates.SERVICE).target(AnimaStates.PAUSED).event(AnimaEvents.PAUSE).and().withExternal()
        .source(AnimaStates.CONSOLE).target(AnimaStates.PAUSED).event(AnimaEvents.PAUSE).and().withExternal()
        .source(AnimaStates.LAMBDA).target(AnimaStates.PAUSED).event(AnimaEvents.PAUSE).and().withExternal()
        .source(AnimaStates.BOT).target(AnimaStates.PAUSED).event(AnimaEvents.PAUSE).and().withExternal()
        .source(AnimaStates.SERVICE).target(AnimaStates.STOPED).event(AnimaEvents.STOP).and().withExternal()
        .source(AnimaStates.CONSOLE).target(AnimaStates.STOPED).event(AnimaEvents.STOP).and().withExternal()
        .source(AnimaStates.LAMBDA).target(AnimaStates.STOPED).event(AnimaEvents.STOP).and().withExternal()
        .source(AnimaStates.BOT).target(AnimaStates.STOPED).event(AnimaEvents.STOP).source(AnimaStates.SERVICE)
        .target(AnimaStates.STASIS).event(AnimaEvents.HIBERNATION).and().withExternal().source(AnimaStates.CONSOLE)
        .target(AnimaStates.STASIS).event(AnimaEvents.HIBERNATION).and().withExternal().source(AnimaStates.LAMBDA)
        .target(AnimaStates.STASIS).event(AnimaEvents.HIBERNATION).and().withExternal().source(AnimaStates.BOT)
        .target(AnimaStates.STASIS).event(AnimaEvents.HIBERNATION).and().withExternal().source(AnimaStates.PAUSED)
        .target(AnimaStates.RUNNING).event(AnimaEvents.START).and().withExternal().source(AnimaStates.STOPED)
        .target(AnimaStates.RUNNING).event(AnimaEvents.START).and().withExternal().source(AnimaStates.STASIS)
        .target(AnimaStates.RUNNING).event(AnimaEvents.START).and().withExternal().source(AnimaStates.STOPED)
        .target(AnimaStates.KILLED).event(AnimaEvents.FINALIZE).and().withExternal().target(AnimaStates.FAULTED)
        .event(AnimaEvents.EXCEPTION);
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
  public Guard<AnimaStates, AnimaEvents> guardStartType() {
    return new Guard<AnimaStates, AnimaEvents>() {
      @Override
      public boolean evaluate(StateContext<AnimaStates, AnimaEvents> context) {
        boolean ritorno = false;
        if (context.getTarget().getId()
            .equals(((Anima) Anima.getApplicationContext().getBean("anima")).getTargetState())) {
          ritorno = true;
        }
        return ritorno;
      }
    };
  }

  public Set<AnimaStates> getBaseStates() {
    return baseStates;
  }

  public Set<AnimaStates> getRunningStates() {
    return runningStates;
  }
}
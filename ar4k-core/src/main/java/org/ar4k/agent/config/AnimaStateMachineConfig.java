package org.ar4k.agent.config;

import java.util.EnumSet;
import java.util.Set;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaEvents;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
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
//@EnableStateMachineFactory
/**
 * State machine per gestore principale del ciclo di vita
 *
 * @author andrea
 *
 */
public class AnimaStateMachineConfig extends EnumStateMachineConfigurerAdapter<AnimaStates, AnimaEvents> {
  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(AnimaStateMachineConfig.class.toString());

  @Autowired
  Anima anima;

  @Override
  public void configure(StateMachineConfigurationConfigurer<AnimaStates, AnimaEvents> config) throws Exception {
    config.withConfiguration().autoStartup(false).listener(listener());
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
        .source(AnimaStates.STAMINAL).target(AnimaStates.CONFIGURED).event(AnimaEvents.SETCONF).guard(guardConfig())
        .and().withExternal().source(AnimaStates.STAMINAL).target(AnimaStates.KILLED).event(AnimaEvents.STOP).and()
        .withExternal().source(AnimaStates.STAMINAL).target(AnimaStates.FAULTED).event(AnimaEvents.EXCEPTION).and()
        .withExternal().source(AnimaStates.CONFIGURED).target(AnimaStates.RUNNING).event(AnimaEvents.START).and()
        .withExternal().source(AnimaStates.CONFIGURED).target(AnimaStates.KILLED).event(AnimaEvents.STOP).and()
        .withExternal().source(AnimaStates.CONFIGURED).target(AnimaStates.FAULTED).event(AnimaEvents.EXCEPTION).and()
        .withExternal().source(AnimaStates.CONFIGURED).target(AnimaStates.CONFIGURED).event(AnimaEvents.RESTART)
        .guard(guardConfig()).and().withExternal().source(AnimaStates.RUNNING).target(AnimaStates.STASIS)
        .event(AnimaEvents.PAUSE).and().withExternal().source(AnimaStates.RUNNING).target(AnimaStates.KILLED)
        .event(AnimaEvents.STOP).and().withExternal().source(AnimaStates.RUNNING).target(AnimaStates.FAULTED)
        .event(AnimaEvents.EXCEPTION).and().withExternal().source(AnimaStates.RUNNING).target(AnimaStates.CONFIGURED)
        .event(AnimaEvents.RESTART).guard(guardConfig()).and().withExternal().source(AnimaStates.STASIS)
        .target(AnimaStates.KILLED).event(AnimaEvents.HIBERNATION).and().withExternal().source(AnimaStates.STASIS)
        .target(AnimaStates.RUNNING).event(AnimaEvents.START).and().withExternal().source(AnimaStates.STASIS)
        .target(AnimaStates.FAULTED).event(AnimaEvents.EXCEPTION).and().withExternal().source(AnimaStates.STASIS)
        .target(AnimaStates.CONFIGURED).event(AnimaEvents.RESTART).guard(guardConfig()).and().withExternal()
        .source(AnimaStates.INIT).target(AnimaStates.INIT).event(AnimaEvents.COMPLETE_RELOAD).guard(guardConfig()).and()
        .withExternal().source(AnimaStates.STAMINAL).target(AnimaStates.INIT).event(AnimaEvents.COMPLETE_RELOAD)
        .guard(guardConfig()).and().withExternal().source(AnimaStates.KILLED).target(AnimaStates.INIT)
        .event(AnimaEvents.COMPLETE_RELOAD).guard(guardConfig()).and().withExternal().source(AnimaStates.CONFIGURED)
        .target(AnimaStates.INIT).event(AnimaEvents.COMPLETE_RELOAD).guard(guardConfig()).and().withExternal()
        .source(AnimaStates.FAULTED).target(AnimaStates.INIT).event(AnimaEvents.COMPLETE_RELOAD).guard(guardConfig())
        .and().withExternal().source(AnimaStates.RUNNING).target(AnimaStates.INIT).event(AnimaEvents.COMPLETE_RELOAD)
        .guard(guardConfig());
  }

  @Bean
  public StateMachineListener<AnimaStates, AnimaEvents> listener() {
    return new StateMachineListenerAdapter<AnimaStates, AnimaEvents>() {
      @Override
      public void stateChanged(State<AnimaStates, AnimaEvents> from, State<AnimaStates, AnimaEvents> to) {
        // workaround Spring State Machine
        if (anima.getState().equals(AnimaStates.INIT)) {
          anima.initAgent();
        }
        if (anima.getState().equals(AnimaStates.STAMINAL)) {
          anima.startingAgent();
        }
        if (anima.getState().equals(AnimaStates.KILLED)) {
          anima.finalizeAgent();
        }
        if (anima.getState().equals(AnimaStates.CONFIGURED)) {
          anima.configureAgent();
        }
        if (anima.getState().equals(AnimaStates.RUNNING)) {
          anima.runPots();
          anima.runServices();
          anima.startCheckingNextConfig();
        }
        if (anima.getState().equals(AnimaStates.STASIS)) {
          anima.prepareAgentStasis();
        }
        anima.stateChanged();
      }
    };
  }

  @Bean
  public Guard<AnimaStates, AnimaEvents> guardConfig() {
    return new Guard<AnimaStates, AnimaEvents>() {

      @Override
      public boolean evaluate(StateContext<AnimaStates, AnimaEvents> context) {
        if (context.getEvent().equals(AnimaEvents.SETCONF)) {
          if (anima.getRuntimeConfig() != null) {
            return true;
          } else {
            logger
                .error("To change the state to CONFIGURED is needed a configuration. anima.getRuntimeConfig() is null");
            return false;
          }
        } else if (context.getEvent().equals(AnimaEvents.RESTART)) {
          logger.warn("The agent will be restarted. Please wait...");
          anima.prepareRestart();
          return true;
        } else if (context.getEvent().equals(AnimaEvents.COMPLETE_RELOAD)) {
          logger.warn("Agent reload starting. Please wait...");
          anima.reloadAgent();
          return true;
        } else {
          return true;
        }
      }
    };
  }

  @Bean
  public Action<AnimaStates, AnimaEvents> errorAction() {
    return ctx -> logger.logException(ctx.getException());
  }

  public Set<AnimaStates> getBaseStates() {
    return EnumSet.allOf(AnimaStates.class);
  }

  public Anima getAnima() {
    return anima;
  }

}
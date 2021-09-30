package org.ar4k.agent.core;

import java.util.EnumSet;
import java.util.Set;

import org.ar4k.agent.core.Homunculus.HomunculusEvents;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
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
/**
 * State machine per gestore principale del ciclo di vita
 *
 * @author andrea
 *
 */
public class HomunculusStateMachineConfig
		extends EnumStateMachineConfigurerAdapter<HomunculusStates, HomunculusEvents> {
	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(HomunculusStateMachineConfig.class);

	@Autowired
	EdgeAgentCore edgeAgentCore;

	@Override
	public void configure(StateMachineConfigurationConfigurer<HomunculusStates, HomunculusEvents> config)
			throws Exception {
		config.withConfiguration().autoStartup(false).listener(listener());
	}

	@Override
	public void configure(StateMachineStateConfigurer<HomunculusStates, HomunculusEvents> states) throws Exception {
		states.withStates().initial(HomunculusStates.INIT).states(EnumSet.allOf(HomunculusStates.class));
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<HomunculusStates, HomunculusEvents> transitions)
			throws Exception {
		transitions.withExternal().source(HomunculusStates.INIT).target(HomunculusStates.STAMINAL)
				.event(HomunculusEvents.BOOTSTRAP).and().withExternal().source(HomunculusStates.INIT)
				.target(HomunculusStates.KILLED).event(HomunculusEvents.STOP).and().withExternal()
				.source(HomunculusStates.INIT).target(HomunculusStates.FAULTED).event(HomunculusEvents.EXCEPTION).and()
				.withExternal().source(HomunculusStates.STAMINAL).target(HomunculusStates.CONFIGURED)
				.event(HomunculusEvents.SETCONF).guard(guardConfig()).and().withExternal()
				.source(HomunculusStates.STAMINAL).target(HomunculusStates.KILLED).event(HomunculusEvents.STOP).and()
				.withExternal().source(HomunculusStates.STAMINAL).target(HomunculusStates.FAULTED)
				.event(HomunculusEvents.EXCEPTION).and().withExternal().source(HomunculusStates.CONFIGURED)
				.target(HomunculusStates.RUNNING).event(HomunculusEvents.START).and().withExternal()
				.source(HomunculusStates.CONFIGURED).target(HomunculusStates.KILLED).event(HomunculusEvents.STOP).and()
				.withExternal().source(HomunculusStates.CONFIGURED).target(HomunculusStates.FAULTED)
				.event(HomunculusEvents.EXCEPTION).and().withExternal().source(HomunculusStates.CONFIGURED)
				.target(HomunculusStates.CONFIGURED).event(HomunculusEvents.RESTART).guard(guardConfig()).and()
				.withExternal().source(HomunculusStates.RUNNING).target(HomunculusStates.STASIS)
				.event(HomunculusEvents.PAUSE).and().withExternal().source(HomunculusStates.RUNNING)
				.target(HomunculusStates.KILLED).event(HomunculusEvents.STOP).and().withExternal()
				.source(HomunculusStates.RUNNING).target(HomunculusStates.FAULTED).event(HomunculusEvents.EXCEPTION)
				.and().withExternal().source(HomunculusStates.RUNNING).target(HomunculusStates.CONFIGURED)
				.event(HomunculusEvents.RESTART).guard(guardConfig()).and().withExternal()
				.source(HomunculusStates.STASIS).target(HomunculusStates.KILLED).event(HomunculusEvents.HIBERNATION)
				.and().withExternal().source(HomunculusStates.STASIS).target(HomunculusStates.RUNNING)
				.event(HomunculusEvents.START).and().withExternal().source(HomunculusStates.STASIS)
				.target(HomunculusStates.FAULTED).event(HomunculusEvents.EXCEPTION).and().withExternal()
				.source(HomunculusStates.STASIS).target(HomunculusStates.CONFIGURED).event(HomunculusEvents.RESTART)
				.guard(guardConfig()).and().withExternal().source(HomunculusStates.INIT).target(HomunculusStates.INIT)
				.event(HomunculusEvents.COMPLETE_RELOAD).guard(guardConfig()).and().withExternal()
				.source(HomunculusStates.STAMINAL).target(HomunculusStates.INIT).event(HomunculusEvents.COMPLETE_RELOAD)
				.guard(guardConfig()).and().withExternal().source(HomunculusStates.KILLED).target(HomunculusStates.INIT)
				.event(HomunculusEvents.COMPLETE_RELOAD).guard(guardConfig()).and().withExternal()
				.source(HomunculusStates.CONFIGURED).target(HomunculusStates.INIT)
				.event(HomunculusEvents.COMPLETE_RELOAD).guard(guardConfig()).and().withExternal()
				.source(HomunculusStates.FAULTED).target(HomunculusStates.INIT).event(HomunculusEvents.COMPLETE_RELOAD)
				.guard(guardConfig()).and().withExternal().source(HomunculusStates.RUNNING)
				.target(HomunculusStates.INIT).event(HomunculusEvents.COMPLETE_RELOAD).guard(guardConfig());
	}

	@Bean
	public StateMachineListener<HomunculusStates, HomunculusEvents> listener() {
		return new StateMachineListenerAdapter<HomunculusStates, HomunculusEvents>() {
			@Override
			public void stateChanged(State<HomunculusStates, HomunculusEvents> from,
					State<HomunculusStates, HomunculusEvents> to) {
				// workaround Spring State Machine
				if (edgeAgentCore.getState().equals(HomunculusStates.INIT)) {
					edgeAgentCore.initAgent();
				}
				if (edgeAgentCore.getState().equals(HomunculusStates.STAMINAL)) {
					edgeAgentCore.startingAgent();
				}
				if (edgeAgentCore.getState().equals(HomunculusStates.KILLED)) {
					edgeAgentCore.finalizeAgent();
				}
				if (edgeAgentCore.getState().equals(HomunculusStates.CONFIGURED)) {
					edgeAgentCore.configureAgent();
				}
				if (edgeAgentCore.getState().equals(HomunculusStates.RUNNING)) {
					edgeAgentCore.runPreScript();
					edgeAgentCore.runServices();
					edgeAgentCore.runPostScript();
					edgeAgentCore.startCheckingNextConfig();
				}
				if (edgeAgentCore.getState().equals(HomunculusStates.STASIS)) {
					edgeAgentCore.prepareAgentStasis();
				}
				edgeAgentCore.stateChanged();
			}
		};
	}

	@Bean
	public Guard<HomunculusStates, HomunculusEvents> guardConfig() {
		return new Guard<HomunculusStates, HomunculusEvents>() {

			@Override
			public boolean evaluate(StateContext<HomunculusStates, HomunculusEvents> context) {
				if (context.getEvent().equals(HomunculusEvents.SETCONF)) {
					if (edgeAgentCore.getRuntimeConfig() != null) {
						return true;
					} else {
						logger.error(
								"To change the state to CONFIGURED is needed a configuration. homunculus.getRuntimeConfig() is null");
						return false;
					}
				} else if (context.getEvent().equals(HomunculusEvents.RESTART)) {
					logger.warn("The agent will be restarted. Please wait...");
					edgeAgentCore.prepareRestart();
					return true;
				} else if (context.getEvent().equals(HomunculusEvents.COMPLETE_RELOAD)) {
					logger.warn("Agent reload starting. Please wait...");
					edgeAgentCore.reloadAgent();
					return true;
				} else {
					return true;
				}
			}
		};
	}

	@Bean
	public Action<HomunculusStates, HomunculusEvents> errorAction() {
		return ctx -> logger.logException(ctx.getException());
	}

	public Set<HomunculusStates> getBaseStates() {
		return EnumSet.allOf(HomunculusStates.class);
	}

	public EdgeAgentCore getHomunculus() {
		return edgeAgentCore;
	}

}
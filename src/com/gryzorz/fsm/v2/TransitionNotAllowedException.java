package com.gryzorz.fsm.v2;


/**
 * @author Benoit Fernandez
 */
public class TransitionNotAllowedException extends FiniteStateMachineException {
    public State from = null;
    public Event event = null;
    public TransitionNotAllowedException(State from, Event event) {
        super("There is no transition from state " + from + " reacting to event " + event);
        this.from = from;
        this.event = event;
    }
}

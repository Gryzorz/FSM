package com.gryzorz.fsm.v2;


/**
 * @author Benoit Fernandez
 */
public class TransitionAlreadyExistException extends FiniteStateMachineException {
    public TransitionAlreadyExistException(State from, Event event) {
        super("There is no transition from state " + from + " reacting to event " + event);
    }
}

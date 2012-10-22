package com.gryzorz.fsm.v2;

/**
 * Exists only to provide a default empty implementation of interface FiniteStateMachineListener,
 * in case you can afford the luxury to inherit it !
 * 
 * @author Benoit Fernandez
 */
public abstract class DefaultFiniteStateMachineListener implements FiniteStateMachineListener {

    public void eventOccurred(State from, Event event, State to) {
        
    }

    public void stateChanged(State from, Event event, State to) {
        
    }

    public void stateLoop(State state, Event event) {
        
    }

    public void unexistingTransition(State from, Event event) {
        
    }
}

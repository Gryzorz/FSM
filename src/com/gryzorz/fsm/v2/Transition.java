package com.gryzorz.fsm.v2;

/**
 * 
 * @author Benoit Fernandez
 */
class Transition {
    Event event;
    State destinationState;
    
    Transition(Event e, State s) {
        this.event = e;
        this.destinationState = s;
    }
    
    Event getEvent() { return this.event; }
    State getDestinationState() { return this.destinationState; }
}

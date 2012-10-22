package com.gryzorz.fsm.v1;


/**
 * Prefer to use this class to build a FiniteStateMachine.<br>
 * 0) identify and define your "State" and "Event" as constants<br> 
 * 1) Instanciate a FiniteStateMachineBuilder<br>
 * 2) use method addTransition(...) as often as you can using your "State" and "Event" constants<br>
 * 3) use method createFSM(...) specifying the initial state to retrieve the instance of your state machine<br>
 * 4) see FiniteStateMachine to see how to use it
 * @author Benoit Fernandez
 */
public class FiniteStateMachineBuilder {
    private FiniteStateMachine fsm = new FiniteStateMachine();
    
    protected void addTransition(State from, Event event, State to) throws FiniteStateMachineException {
        fsm.addTransition(from, event, to);
    }
    
    final protected FiniteStateMachine createFSM(State initialState) throws FiniteStateMachineException {
        if(initialState == null)
            throw new FiniteStateMachineException("Cannot create a FiniteStateMachnie without an initial State");

        fsm.setInitialState(initialState);
        
        return fsm;
    }
}

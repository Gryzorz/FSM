package com.gryzorz.fsm.v2;


/**
 * This class handles the creation of a FiniteStateMachine.<br>
 * How to build a FiniteStateMachine.<br>
 * 0) draw your FSM on a paper
 * 1) List your "States" and "Events" and define them as constants in the class handling the state you want to record<br> 
 * 2) Instanciate a FiniteStateMachineBuilder<br>
 * 3) use method addTransition(...) as often as you need to build the correct transitions according to your drawing<br>
 * 4) use method createFSM(...) specifying the initial state to retrieve the instance of your state machine<br>
 * 5) see FiniteStateMachine to see how to use it
 * 
 * @author Benoit Fernandez
 */
public class FiniteStateMachineBuilder {
    private FiniteStateMachine fsm = new FiniteStateMachine();
    
    public void addTransition(State from, Event event, State to) throws FiniteStateMachineException {
        fsm.addTransition(from, event, to);
    }
    
    public FiniteStateMachine createFSM(State initialState) throws FiniteStateMachineException {
        if(initialState == null)
            throw new FiniteStateMachineException("Cannot create a FiniteStateMachnie without an initial State");

        fsm.setInitialState(initialState);
        
        return fsm;
    }
}

package com.gryzorz.fsm.v1;

import java.util.*;

/**
 * This is an implementation of an extremely simple finite state machine.
 * It is composed of 'states', which can be linked between them through
 * 'transitions'.<br>
 * See FiniteStateMachineBuilder to see how to safely build a FiniteStateMachine.<br>
 * Once created, call method processEvent(Event) to inject incoming events into the machine.<br>
 * It will navigate through different states according to the transitions that were registered, ensuring you to be always in a state that you allowed.<br>
 * There is no mechanism to be informed of a state change.<br>
 * If a transition was not planned, there is not state change, but there is no mechanism to be informed of a "forbidden" or consequence free event.<br>
 * 
 * @author Benoit Fernandez
 */
public class FiniteStateMachine {
    protected State currentState = null;
    protected Map<State, List<Transition>> stateMap = new HashMap<State, List<Transition>>();
    
    FiniteStateMachine() {}
    
    public void processEvent(Event event) {
        List<Transition> list = stateMap.get(currentState);
        if(list != null) //in case there is no transition starting from the currentstate
            for(Transition transition : list) {
                if(transition.getEvent() == event) {
                    currentState = transition.getDestinationState();
                    break;
                }
            }
    }
    
    public State getState() {
        return currentState;
    }
    
    void setInitialState(State initialState) {
        currentState = initialState;
    }
    
    void addTransition(State from, Event event, State to) throws FiniteStateMachineException {
        if(from == null)
            throw new FiniteStateMachineException("addTransition : cannot add a transition with a null origin state");
        if(event == null)
            throw new FiniteStateMachineException("addTransition : cannot add a transition with a null event");
        if(to == null)
            throw new FiniteStateMachineException("addTransition : cannot add a transition with a null destination state");
        
        List<Transition> list = stateMap.get(from);
        if(list == null) {
            list = new ArrayList<Transition>();
            stateMap.put(from, list);
        }
        
        boolean transitionAlreadyExist = false;
        for(Transition transition : list) {
            if(transition.getEvent() == event) {
                transitionAlreadyExist = true;
                break;
            }
        }
        if(transitionAlreadyExist)
            throw new FiniteStateMachineException("addTransition : there already exists a transition from this state with the same event");

        list.add(new Transition(event, to));
    }
}

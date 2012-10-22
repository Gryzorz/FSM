package com.gryzorz.fsm.v2;

import java.util.*;

/**
 * This is an implementation of a simple finite state machine.
 * It is composed of 'states', which can be linked between them through 'transitions'.<br>
 * See FiniteStateMachineBuilder to see how to safely build a FiniteStateMachine.<br>
 * Once created, call method processEvent(Event) to inject incoming events into the machine.<br>
 * It will navigate through different states according to the transitions that were registered,
 * ensuring you to be always in a state that you allowed.<br>
 * <br>
 * Evolution from version 1, now you can be informed through an exception if a transition not 
 * planned has occured. That feature was required depending on what the state machine was used for.<br>   
 * You can also be informed of a non planned transition using a FiniteStateMachineListener<br>
 * <br>
 * There also is now a mechanism to allow a class to be informed of a state change (or a state
 * self loop if planned) : FiniteStateMachineListener.
 * <br>
 * You can add a listener with method addListener(...)
 * 
 * @author Benoit Fernandez
 */
public class FiniteStateMachine {
    protected Object instanceMonitor = new Object(); //easier to read that the self-reference "this"
    
    protected State currentState = null;
    protected Map<State, List<Transition>> stateMap = new HashMap<State, List<Transition>>();
    
    protected FiniteStateMachine() {}
    
    void setInitialState(State initialState) {
        currentState = initialState;
    }
    
    void addTransition(State from, Event event, State to) throws FiniteStateMachineException {
        synchronized (instanceMonitor) {
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
                throw new TransitionAlreadyExistException(from, event);

            list.add(new Transition(event, to));
        }
    }
    

    /**************************************************************************
     *                           public methods                               *
     *************************************************************************/
    
    
    /**
     * Use this method to notify the state machine of the events happening.<br>
     * Unlike method processEventWithoutErrorNotification(...), this method notifies
     * the caller with an exceptino in case the transition is not allowed.<br>
     * This behavior is useful when you want to notify a client that the operation
     * he is intending is not allowed.<br>
     * In case you don't want to be informed, use method processEventWithoutErrorNotification(...)<br> 
     * @param event the event that happened
     * @throws TransitionNotAllowedException the exception thrown in case no transition exist
     * from the current state reacting to the event.
     */
    public void processEvent(Event event) throws TransitionNotAllowedException {
        boolean hasToThrowException = false;
        synchronized (instanceMonitor) {
            List<Transition> list = stateMap.get(currentState);
            if(list != null) { //in case there is no transition starting from the currentstate
                for(Transition transition : list) {
                    if(transition.getEvent() == event) {
                        State stateBefore = currentState;
                        currentState = transition.getDestinationState();
                        State stateAfter = currentState;
                        if(stateBefore != stateAfter) { //state change
                            _notifyStateChanged(stateBefore, event, stateAfter);
                        } else { //state loop
                            _notifyStateLoop(currentState, event);
                        }
                        break;
                    }
                }
            } else { //there are no transitions from the current state reacting to this event
                _notifyUnexistingTransition(currentState, event);
                hasToThrowException = true;
            }
        }
        if(hasToThrowException) {
            throw new TransitionNotAllowedException(currentState, event);
        }
    }
    
    /**
     * This method acts the same as processEvent(...) but without notification of an
     * unsupported transition. This behavior can be preferred when many events are processed,
     * and we only want to capture a certain state (in case of a SAX parser for example).
     * @param event the event that happened
     */
    public void processEventWithoutErrorNotification(Event event) {
        try {
            processEvent(event);
        } catch(TransitionNotAllowedException e) {
            //we do not notify the client
        }
    }
    
    /**
     * This method can be used to know preventively if there is a transition existing from the
     * current state reacting to the event given in parameters.<br>
     * Be extremely careful while using this method. You migh want to use it to decide or not to
     * inject an event into the state machine. This is extremely wrong usage of a state machine.<br>
     * A state machine is used to replace very complex bunch of tests into clear states that
     * govern an object's behavior.<br>
     * That's why same events should always be triggered by same actions (i.e : primitives)
     * no matter what. Transitions are explicitely existing to prevent or allow a state change.
     * In no case you must use this method to replace or not an event trigger.<br>
     * <br>
     * This method is provided and must be used only for graphic purpose.<br>
     * Imagine an action (i.e : one primitive of your object) is mapped to a gui button, and
     * you want to restrict the availability of the button depending on if the primitive will
     * trigger a state change or not. In other words, grey out the button if it will produce
     * no effect, and allow the user to clic if it will result in a state change.<br>
     * The implementation of this pattern must be done as follow :<br>
     *  1) define a unique method that update each button availability (with this method)
     *  depending on each event they are supposed to trigger<br>  
     *  2) create your FiniteStateMachine, then call your method to update the button statuses<br>
     *  3) use a FiniteStateMachineListener and override method stateChanged(...) to get notified
     *  of any state change occuring (because staying on a same state won't change anything, might
     *  there exist a valid transition or not)<br>
     *  4) call your method to refresh button statuses inside method statusChanged(...) to keep
     *  your UI up to date<br>
     *  <br>
     *  Remember that each primitive behind each button must keep triggering each event no matter
     *  what, because remember that your primitives might always be used outside of a UI, so they
     *  must have a consistent behavior.<br>
     *  
     * @param event the event to be tested
     * @return true if a transition exist
     */
    public boolean isTransitionExisting(Event event) {
        boolean transitionExist = false;
        List<Transition> list = stateMap.get(currentState);
        if(list != null) { //in case there is no transition starting from the currentstate
            for(Transition transition : list) {
                if(transition.getEvent() == event) {
                    transitionExist = true;
                    break;
                }
            }
        }
        return transitionExist;
    }
    
    /**
     * Retrieves the currentState of the state machine
     * @return the current state
     */
    public State getState() {
        return currentState;
    }
    
    
    /**************************************************************************
     *                           listeners part                               *
     *************************************************************************/
    protected List<FiniteStateMachineListener> listeners = new ArrayList<FiniteStateMachineListener>();
    
    public void addListener(FiniteStateMachineListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(FiniteStateMachineListener listener) {
        listeners.remove(listener);
    }

    protected void _notifyStateChanged(State from, Event event, State to) {
        for(FiniteStateMachineListener listener : listeners) {
            listener.eventOccurred(from, event, to);
            listener.stateChanged(from, event, to);
        }
    }
    
    protected void _notifyStateLoop(State state, Event event) {
        for(FiniteStateMachineListener listener : listeners) {
            listener.eventOccurred(state, event, state);
            listener.stateLoop(state, event);
        }
    }
    
    protected void _notifyUnexistingTransition(State from, Event event) {
        for(FiniteStateMachineListener listener : listeners) {
            listener.eventOccurred(from, event, null);
            listener.unexistingTransition(from, event);
        }
    }
}

package com.gryzorz.fsm.v2;

/**
 * This class provides callback methods that are called whenever something happens
 * within you FiniteStateMachine.
 * You can override any method you like to catch the different things happening.<br>
 * When an event occurs, there are 3 different scenarios possible :<br>
 * 1) the event produces a state change from state A to state B<br>
 * 2) the event produces a loop on the same state A<br>
 * 3) the even is not planned to happen while in current state<br>
 * 
 * @author Benoit Fernandez
 */
public interface FiniteStateMachineListener{

    /**
     * This method is very generic and is called each time an event is triggered
     * in the FiniteStateMachine.<br>
     * Depending on the situation, the parameters can have different values : 
     * @param from this State is always the State of the FiniteStateMachine when the event occurs
     * @param event this Event is always the event that occurs
     * @param to this State depends on the situation :<br>
     * <i>case 1)</i> State changed : its value is the new state B.<br>
     * If you want to capture think kind of scenario only, override method stateChanged(...)<br>
     * <i>case 2)</i> Self loop : its value is exactly the same as state "from" (or A in our example).<br>
     * If you want to capture this kind of scenario only, override method stateLoop(...)<br>
     * <i>case 3)</i> Transition not planned : its value is null, as there were no transition existing
     * responding to this event from the current state.<br>
     * If you want to capture this kind of scenario only, override method unexistingTransition(...)<br>
     * <i>Important note</i> : this method is called prior to the other 3 methods, but is called
     * no matter if you have overridden ony of the other 3 methods. Be careful not to process things
     * twice<br>
     */
    void eventOccurred(State from, Event event, State to);

    /**
     * This method is called only if the event that occurred produced a change of state that
     * is not a loop.<br>
     * Beware, if you override method eventOccurred(...), you should know that it will be called
     * prior to this method when an effective state changes occurs.
     * @param from the state before the event occurred
     * @param event the event that occurred
     * @param to the state after the event occurred
     */
    void stateChanged(State from, Event event, State to);
    
    /**
     * This method is called only if the event that occurred produced a loop that was foreseen
     * (i.e : a transition to allow a loop was created).<br>
     * Beware, if you override method eventOccurred(...), you should know that it will be called
     * prior to this method when an effective state loop occurs.
     * @param state the state before and after the event occurred
     * @param event the event that occurred
     */
    void stateLoop(State state, Event event);
    
    /**
     * This method is called only if the event that occurred was not foreseen (i.e : there
     * are no transition planned starting from the current state responding to that event).<br>
     * Beware, if you override method eventOccurred(...), you should know that it will be called
     * prior to this method when an effective unexisting transition occurs.
     * @param from the state of the state machine when the event occurred (and after it occurred)
     * @param event the event that occurred
     */
    void unexistingTransition(State from, Event event);
}

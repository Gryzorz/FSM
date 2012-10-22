package com.gryzorz.fsm.v2;

/**
 * Generic superclass for all exceptions for the FiniteStateMachine objects<br>
 * You can retrieve the error message with <b>getMessage()</b> method
 * 
 * @author Benoit Fernandez
 */
public class FiniteStateMachineException extends Exception {
    String message = "";
    public FiniteStateMachineException(String message) {
        if(message != null)
            this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}

package com.gryzorz.fsm.v2;

/**
 * Defines the Event type
 * 
 * @author Benoit Fernandez
 */
public class Event {
    String name;
    
    /**
     * @param name It is highly recommended to specifiy a very explicit name since
     * it is used for debugging purpose, and it's commonly known that effective debugging
     * is the first brick to a solid wall.
     */    
    public Event(String name) {
        if(name != null && !"".equals(name)) {
            this.name = name;
        } else {
            this.name = "generic_event_name";
        }
    }
    
    @Override
    public String toString() {
        return name;
    }
}

package com.dh.carsimulationassignment;

/**
 * Enumerations for race states
 * @author Daniel Haddadeen (2588731)
 */
public enum RaceState {
    WAITING("Waiting for race start..."),
    PAUSED("Race is paused..."),
    ONGOING("Race is ongoing..."),
    FINISHED("Race is finished!");
    
    private String raceStatusMessage;
    
    RaceState(String raceStatusMessage) {
        this.raceStatusMessage = raceStatusMessage;
    }
    
    public String getRaceStatusMessage() {
        return raceStatusMessage;
    }
}

package ru.endoskop.util;

public class StateManager {
    public enum State{
        AddSurvey, EditSurvey, PrintData, SearchSurvey, AddUser, EditUser,
        AddDictionary, EditDictionary};
    private State currentState;
    
    public void setState(State state){
        currentState = state;
    }
    
    public State getState(){
        return currentState;
    }
}

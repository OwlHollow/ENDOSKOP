package ru.endoskop.util;

import java.util.ArrayList;
import javafx.scene.Node;

public class FocusManager {
    private final ArrayList<Node> nodes;
    private int currentNodeIndex;
    
    public FocusManager(ArrayList<Node> nodes){
        this.nodes = nodes;
        currentNodeIndex = 0;
    }
    
    public void focus(int index){
        if(index < 0 || index > nodes.size() - 1)
            index = 0;
        nodes.get(index).requestFocus();
        currentNodeIndex = index;
    }
    
    public void focusPrevious(){
        currentNodeIndex--;
        if(currentNodeIndex < 0)
            currentNodeIndex = nodes.size() - 1;
        nodes.get(currentNodeIndex).requestFocus();
    }
    
    public void focusNext(){
        currentNodeIndex++;
        if(currentNodeIndex > nodes.size() - 1)
            currentNodeIndex = 0;
        nodes.get(currentNodeIndex).requestFocus();
    }
    
    public int getCurrentIndex(){
        return currentNodeIndex;
    }
}

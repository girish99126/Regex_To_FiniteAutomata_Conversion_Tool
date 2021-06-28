package com.leaf.helloworld;
import java.util.*;
public class TransitionRow
{
    ArrayList<Node> currentState;
    ArrayList<TransitionCol> transitions;
    TransitionRow(ArrayList<Node> currentState,ArrayList<TransitionCol> transitions)
    {
        this.currentState = currentState;
        this.transitions = transitions;
    }
    
}
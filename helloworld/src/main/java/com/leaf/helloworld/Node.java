package com.leaf.helloworld;
import java.util.ArrayList;

public class Node
{
    int label=-1;
    ArrayList<Transition> edges = new ArrayList<Transition>();
    void setTransition(String onSymbol,Node toState)
    {
        Transition temp = new Transition(onSymbol,toState);
        edges.add(temp);

    }
    @Override
    public String toString() {
        return String.valueOf(this.label);
    }
}
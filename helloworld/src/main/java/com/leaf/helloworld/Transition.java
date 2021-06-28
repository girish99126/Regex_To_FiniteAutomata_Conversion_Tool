package com.leaf.helloworld;
public class Transition {
    String onSymbol;
    Node toState;
    Transition(String onSymbol,Node toState)
    {
        this.onSymbol = onSymbol;
        this.toState  = toState;
    }
}


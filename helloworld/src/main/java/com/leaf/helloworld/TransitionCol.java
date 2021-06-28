package com.leaf.helloworld;
import java.util.*;
public class TransitionCol
{
    String onSymbol;
    ArrayList<Node> toStates;
    TransitionCol(String onSymbol,ArrayList<Node> toStates)
    {
        this.onSymbol=onSymbol;
        this.toStates = toStates;
    }
}
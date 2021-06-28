package com.leaf.helloworld;
import java.util.*;

public class eNfa {
    Graph build(String postfix)
    {
        Stack<Graph> stack = new Stack<Graph>();
        for(int i=0;i<postfix.length();i++)
        {
            String current=String.valueOf(postfix.charAt(i));
            if(current.equals("+") || current.equals(".") || current.equals("*"))
            {
                Graph temp1,temp2;
                switch(current)
                {       
                    case ".":   
                        temp2=stack.pop();
                        temp1=stack.pop();
                        stack.push(this.concat(temp1,temp2));
                        break;
                    case "+":
                        temp2 = stack.pop();
                        temp1 = stack.pop();
                        stack.push(this.union(temp1,temp2));
                        break;
                    case "*":
                        temp1 =stack.pop();
                        stack.push(this.kleene(temp1));
                        break;

                }
            }
            else
            {
                stack.push(this.alphabet(current));
            }
        }    
        Graph result = stack.pop();    
        return result;
    }
    Graph alphabet(String a)
    {
        Graph temp = new Graph();
        Node temp1 = new Node();
        Node temp2 = new Node();
        temp.states.add(temp1);
        temp.states.add(temp2);
        temp.start = temp1;
        temp.finalStates.add(temp2);
        temp1.setTransition(a, temp2);
        return temp;
    }


     Graph union(Graph a, Graph b){

        Graph temp = new Graph();
        Node newStart = new Node();
        Node newFinal = new Node();
        temp.states.add(newStart);
        temp.states.addAll(a.states);
        temp.states.addAll(b.states);
        temp.states.add(newFinal);
        newStart.setTransition("ϵ",a.start);
        newStart.setTransition("ϵ", b.start);
        temp.start=newStart;
        temp.finalStates.add(newFinal);
        a.finalStates.get(0).setTransition("ϵ", newFinal);
        b.finalStates.get(0).setTransition("ϵ", newFinal);
        a.start=null;
        a.finalStates=null;
        b.start=null;
        b.finalStates=null;
        a.states=null;
        b.states=null;
        return temp;
        

    }

     Graph concat(Graph a,Graph b)
    {
        Graph temp = new Graph();
        a.finalStates.get(0).setTransition("ϵ", b.start);
        temp.states.addAll(a.states);
        temp.states.addAll(b.states);   
        temp.start = a.start;
        temp.finalStates.add(b.finalStates.get(0));
        a.start=null;
        a.finalStates=null;
        b.start=null;
        b.finalStates=null;
        a.states=null;
        b.states=null;
        return temp;
    }
    Graph kleene(Graph a){

        Graph temp = new Graph();
        Node newStart = new Node();
        Node newFinal = new Node();
        temp.states.add(newStart);
        temp.states.addAll(a.states);
        temp.states.add(newFinal);
        newStart.setTransition("ϵ", a.start);
        newStart.setTransition("ϵ",newFinal);
        a.finalStates.get(0).setTransition("ϵ", newFinal);
        a.finalStates.get(0).setTransition("ϵ", a.start);
        a.start =null;
        a.finalStates = null;
        a.states=null;

        temp.start = newStart;
        temp.finalStates.add(newFinal);
        
        return temp;
        

    }
  
    
    
}
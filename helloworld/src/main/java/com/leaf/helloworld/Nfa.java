
package com.leaf.helloworld;
import java.util.*;
public class Nfa {  
    static ArrayList<Node> eclosure(Node a)
    {
        Set<Node> result = new HashSet<Node>();
        eclosureHelper(a, result);

        return (new ArrayList<Node>(result));
    }
    static void eclosureHelper(Node a,Set<Node> result)
    {        
        result.add(a);
        ArrayList<Node> toStates = Graph.delta(a,"ϵ");
        Iterator<Node> iter = toStates.iterator();
        while(iter.hasNext())
        {
            Node tempToState = iter.next();
            result.add(tempToState);
            eclosureHelper(tempToState,result);
        }
    }
    static ArrayList<Node> eclosure(ArrayList<Node> a)
    {
        Set<Node> result = new HashSet<Node>();
        eclosureHelper(a, result);
        return (new ArrayList<Node>(result)) ;
    }
    static void eclosureHelper(ArrayList<Node>a, Set<Node> result)
    {
        Iterator<Node> iter = a.iterator();
        while(iter.hasNext())
        {
            Node tempNode = iter.next();
            result.addAll(eclosure(tempNode));
            eclosureHelper(tempNode, result);
        }
    }
    static void build(Graph a)
    {
      ArrayList<Node> states = a.states;
      ArrayList<String> alphabet = a.alphabet;

      for(int i=0;i<states.size();i++)
      {
          ArrayList<Transition> tempEdges = new ArrayList<Transition>();
          Node tempState = states.get(i);
          for(int j=0;j<alphabet.size();j++)
          {
            String tempAlphabet = alphabet.get(j);
            ArrayList<Node> newStates = eclosure(Graph.delta(eclosure(tempState),tempAlphabet));
            for(int k=0;k<newStates.size();k++)
            {
                Node tempNewState = newStates.get(k);
                Transition t = new Transition(tempAlphabet,tempNewState);
                tempEdges.add(t);
            }
            
          }
          tempState.edges = tempEdges;
      }
    }
    
}

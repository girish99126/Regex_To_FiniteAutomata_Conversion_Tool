package com.leaf.helloworld;
import java.util.*;
public class DfaTransitionTable {
    ArrayList<ArrayList<Node>> states;
    ArrayList<ArrayList<Node>> finalStates;
    ArrayList<TransitionRow> table;
    ArrayList<String> alphabet;
    ArrayList<Node> dfaStart;
    DfaTransitionTable()
    {
        finalStates = new ArrayList<ArrayList<Node>>();
        alphabet=new ArrayList<String>();
    }

    ArrayList<Node> delta(ArrayList<Node> states, String onSymbol)
    {
        for(int i=0;i<table.size();i++)
        {
            TransitionRow currentRow = table.get(i);
            if(currentRow.currentState.equals(states))
            {
                for(int j=0;j<currentRow.transitions.size();j++)
                {
                    TransitionCol tempCol = currentRow.transitions.get(j);
                    if(tempCol.onSymbol.equals(onSymbol))
                    {
                        return tempCol.toStates;
                    }
                }
            }
        }
        return null;
    } 
    ArrayList<ArrayList<Node>> deltaArr(ArrayList<ArrayList<Node>> states, String onSymbol)
    {
        Set<ArrayList<Node>> result = new HashSet<ArrayList<Node>>();
        for(int i=0;i<states.size();i++)
        {
            ArrayList<Node> tempState = states.get(i);
            result.add(delta(tempState,onSymbol));
            
        }
        return (new ArrayList<ArrayList<Node>>(result));
    }
    
    ArrayList<TransitionRow> build(Graph a)
    {
        ArrayList<TransitionRow> table = new ArrayList<TransitionRow>();
        ArrayList<ArrayList<Node>> visited = new ArrayList<ArrayList<Node>>();
        ArrayList<ArrayList<Node>> unvisited = new ArrayList<ArrayList<Node>>();
        dfaStart = Nfa.eclosure(a.start);

        ArrayList<Node> dead = new ArrayList<Node>();
        
        dead.add(new Node());
        dead.get(0).label=100;
        unvisited.add(dfaStart);
        while(unvisited.size()>0)
        {
            
            ArrayList<Node> tempNode = unvisited.get(0);
            if(tempNode.contains(a.finalStates.get(0)) && !isEqualLists(finalStates,tempNode))
            {
                finalStates.add(tempNode);
            }

            ArrayList<TransitionCol> tempCol = new ArrayList<TransitionCol>();

            for(int i=0;i<a.alphabet.size();i++)
            {
                TransitionCol tempcolvar;
                ArrayList<Node> tempDelta = Graph.delta(tempNode, a.alphabet.get(i));

                if(tempDelta.size()==0)
                {
                    tempcolvar = new TransitionCol(a.alphabet.get(i),dead);
                }
                else
                {
                    tempcolvar = new TransitionCol(a.alphabet.get(i),tempDelta);
                }

                
               
                if( this.isEqualLists(visited, tempcolvar.toStates) == false && this.isEqualLists(unvisited, tempcolvar.toStates)==false)
                {
                    unvisited.add(tempcolvar.toStates);
                }
                
                tempCol.add(tempcolvar);
                
            }
            table.add(new TransitionRow(tempNode,tempCol));
            visited.add(tempNode);
            unvisited.remove(tempNode);
        }
        states=visited;
        
        Set<ArrayList<Node>> finalStateSet = new HashSet<ArrayList<Node>>(finalStates);
        finalStates = new ArrayList<ArrayList<Node>>(finalStateSet);
        
        return table;
    }

    public static String Traverse(ArrayList<TransitionRow> result,int label)
    {   
        ArrayList<ArrayList<Node>> state_names = new ArrayList<ArrayList<Node>>();
        

        for(int i=0;i<result.size();i++){

            state_names.add(result.get(i).currentState);
        }
       

        ArrayList<ArrayList<Node>> final_states = new ArrayList<ArrayList<Node>>(); 
        ArrayList<Integer> finals = new ArrayList<Integer>(); 
       
        String k="";
        
        for(int i=0;i<result.size();i++)
        {

            
            for(int j=0;j<result.get(i).transitions.size();j++)
            {   
                k+="q"+state_names.indexOf(result.get(i).currentState)+" -> "+"q"+state_names.indexOf(result.get(i).transitions.get(j).toStates)+" [label = \""+result.get(i).transitions.get(j).onSymbol+"\"];";

            }
        }
        String s = "digraph finite_state_machine { rankdir=LR; size=\"8,5\";rank=same q0[shape = circle];node [shape = doublecircle];";
        
         for(int i=0;i<state_names.size();i++){
            for(int j=0;j<state_names.get(i).size();j++){
                if(state_names.get(i).get(j).toString().equals(String.valueOf(label))){
                    final_states.add(state_names.get(i));
                    finals.add(state_names.indexOf(state_names.get(i)));

                }
            }
          
        } 
        for(int i=0;i<finals.size();i++){
            s+="q"+finals.get(i)+" ";

        }
        
        
        s+=";node [shape = point]; qi;node [shape = circle]; qi->q0[label=\"start\"];";
       
        String dot = s+" "+k+"}";
        

        return dot;
    }

    boolean equalListsHelper(ArrayList<Node> a,ArrayList<Node> b)
    {
        if(a.size()!=b.size())
            return false;
        
        for(int i=0;i<a.size();i++)
        {
            if(!b.contains(a.get(i)))
            {
                return false;
            }
            
        }
        return true;
    }
    boolean isEqualLists(ArrayList<ArrayList<Node>> a,ArrayList<Node> b)
    {
        for(int i=0;i<a.size();i++)
        {
            if(equalListsHelper(a.get(i), b))
            {
                return true;
            }
        }
        return false;
    }
}



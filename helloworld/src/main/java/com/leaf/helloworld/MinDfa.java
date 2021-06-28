package com.leaf.helloworld;
import java.util.*;
public class MinDfa
{
    ArrayList<ArrayList<ArrayList<Node>>> mergedStates;
    Set<ArrayList<ArrayList<Node>>> finalStates;
    ArrayList<ArrayList<Node>> start;
    
    MinDfa()
    {
        mergedStates = new ArrayList<ArrayList<ArrayList<Node>>>();
        finalStates = new HashSet<ArrayList<ArrayList<Node>>>();
        start = new ArrayList<ArrayList<Node>>();
    }
    public void minimize(DfaTransitionTable dfa)
    {
        ArrayList<ArrayList<ArrayList<Node>>> p1 = new ArrayList<ArrayList<ArrayList<Node>>>();
        ArrayList<ArrayList<ArrayList<Node>>> p = new ArrayList<ArrayList<ArrayList<Node>>>();
        ArrayList<ArrayList<ArrayList<Node>>> temp_p1=new ArrayList<ArrayList<ArrayList<Node>>>();
        ArrayList<ArrayList<ArrayList<Node>>> temp_p=new ArrayList<ArrayList<ArrayList<Node>>>();
        ArrayList<ArrayList<Node>> nonFinalStates = dfa.states;
        nonFinalStates.removeAll(dfa.finalStates);
    
        p1.add(new ArrayList<ArrayList<Node>>(nonFinalStates));
        p1.add(new ArrayList<ArrayList<Node>>(dfa.finalStates));        
         do
        {
            ArrayList<ArrayList<ArrayList<Node>>> temp_p12 = new ArrayList<ArrayList<ArrayList<Node>>>();
            temp_p12=copy(p1);          
            temp_p1.clear();
            temp_p1=copy(p); 
            p.clear();
            
            for(int i=0;i<p1.size();i++)
            {
                ArrayList<ArrayList<Node>> currentPartition = p1.get(i);
            
                for(int j=0;j<currentPartition.size();j++)
                {  
                    temp_p.clear();

                    ArrayList<Node> currentState = currentPartition.get(j);
                   
                    for(int k=j+1;k<currentPartition.size();k++)
                    {
                        if(temp_p.size()>0)
                        {
                                 
                            if(isDistinguishable(dfa,p1, currentState, currentPartition.get(k)))
                            {
                                
                                for(int x=0;x<temp_p.size();x++)
                                {
                                    ArrayList<ArrayList<Node>> temp_x = temp_p.get(x);
                                    if(isDistinguishable(dfa,temp_p12,temp_x.get(0),currentPartition.get(k)))
                                    {
                                         ArrayList<ArrayList<Node>> tempArrayList = new ArrayList<ArrayList<Node>>();
                                        tempArrayList.add(currentPartition.get(k));
                                        temp_x.add(currentPartition.get(k));
                                        currentPartition.remove(k);
                                        
                                    }
                                }
                               
                            }
                        }
                        else
                        {
                            if(isDistinguishable(dfa,temp_p12, currentState, currentPartition.get(k)))
                            {


                                ArrayList<ArrayList<Node>> tempArrayList = new ArrayList<ArrayList<Node>>();
                                tempArrayList.add(currentPartition.get(k));
                                temp_p.add(tempArrayList);

                                currentPartition.remove(currentPartition.get(k));

                                
                            }
                        }                                  
                    }
                    if(!p.contains(currentPartition))
                    {
                        p.add(currentPartition);
                    }
                    p.addAll(temp_p);
                    temp_p.clear();
                    
                       
                }

            }  
            p1.clear();
            p1.addAll(p);

        } while(!p.equals(temp_p1));

        mergedStates=p;
    }

    public  String build(DfaTransitionTable dfa)
    {
        
        this.minimize(dfa);
        String s ="";
        String k="digraph finite_state_machine { rankdir=LR; size=\"8,5\";node [shape = doublecircle];";
        for(int i=0;i<mergedStates.size();i++)
        {
            ArrayList<ArrayList<Node>> tempState = mergedStates.get(i);
            if(isStart(dfa,tempState))
                start.addAll(tempState);
            if(isFinal(dfa,tempState))
                finalStates.add(tempState);
            for(int j=0;j<dfa.alphabet.size();j++)
            {       
             s+="q"+mergedStates.indexOf(tempState)+" -> "+"q"+mergedStates.indexOf(returnMergedNode(dfa.deltaArr(tempState, dfa.alphabet.get(j))))+" [label = \""+dfa.alphabet.get(j)+"\"];";
             }
        } 
        Iterator iter = finalStates.iterator();
        int n=finalStates.size();
        while(n>0){
            k+="q"+mergedStates.indexOf(iter.next())+" ";
            n--;
            
        }
        k+=";node [shape = point]; qi;node [shape = circle];";
        k+="qi -> q"+mergedStates.indexOf(start)+"[label=\"start\"] ;";
        String dot = k+" "+s+" }";
        return dot;

    }
    boolean isStart(DfaTransitionTable dfa,ArrayList<ArrayList<Node>>a)
    {
        if(a.contains(dfa.dfaStart))
        {
            return true;
        }
        return false;
    }
    boolean isFinal(DfaTransitionTable dfa,ArrayList<ArrayList<Node>> a)
    {
        for(int i=0;i<dfa.finalStates.size();i++)
        {
            if(a.contains(dfa.finalStates.get(i)))
                return true;
        }
        return false;
    }

    ArrayList<ArrayList<Node>> returnMergedNode(ArrayList<ArrayList<Node>> a)
    {
        for(int i=0;i<mergedStates.size();i++)
        {
            if(mergedStates.get(i).containsAll(a))
            {
                
                return mergedStates.get(i);
            }
        }
        return null;
    }

    boolean isDistinguishable(DfaTransitionTable dfa,ArrayList<ArrayList<ArrayList<Node>>> p1,ArrayList<Node> state_1,ArrayList<Node> state_2)
    {
        
        int count=0;
        for(int i=0;i<dfa.alphabet.size();i++)
        {
            String currentAlphabet = dfa.alphabet.get(i);
            
            ArrayList<Node> delta_state_1 = dfa.delta(state_1, currentAlphabet);
            ArrayList<Node> delta_state_2 = dfa.delta(state_2, currentAlphabet);
            for(int j=0;j<p1.size();j++)
            {
                ArrayList<ArrayList<Node>> currentState=p1.get(j);                
                if( (isEqualSet(currentState,delta_state_1,delta_state_2)) )
                {
                    count++;
                    continue;
                }
            }
            
       }
 
       if(count==dfa.alphabet.size())
         return false;
       return true;
    }

    boolean isEqualSet(ArrayList<ArrayList<Node>> set ,ArrayList<Node>a,ArrayList<Node>b)
    {
        int count=0;
        for(int i=0;i<set.size();i++)
        {
            if(equalListsHelper(set.get(i),a))
                count++;
            if(equalListsHelper(set.get(i), b))
                count++;
            if(count==2)
                return true;
        }
        return false;
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
    ArrayList<ArrayList<ArrayList<Node>>> copy(ArrayList<ArrayList<ArrayList<Node>>> a)
    {
        ArrayList<ArrayList<ArrayList<Node>>> result = new ArrayList<ArrayList<ArrayList<Node>>>();

        for(int i=0;i<a.size();i++)
        {
            ArrayList<ArrayList<Node>> temp1 = a.get(i);
            ArrayList<ArrayList<Node>> tempResult1=new ArrayList<ArrayList<Node>>();
            for(int j=0;j<temp1.size();j++)
            {
                ArrayList<Node> temp2 =temp1.get(j);
                ArrayList<Node> tempResult2 = new ArrayList<Node>();
                for(int k=0;k<temp2.size();k++)
                {
                    tempResult2.add(temp2.get(k));
                }
                tempResult1.add(tempResult2);

            }
            result.add(tempResult1);

            
        }
        return result;
    }
} 
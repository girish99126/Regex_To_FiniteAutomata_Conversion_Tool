package com.leaf.helloworld;
import java.util.*;
public class Graph {
    Node start;
    ArrayList<String> alphabet = new ArrayList<String>();
    ArrayList<Node> finalStates = new ArrayList<Node>();
    ArrayList<Node> states = new ArrayList<Node>();

    static ArrayList<Node> delta(Node state,String symbol)
    {
        Set<Node> result = new HashSet<Node>();
        ArrayList<Transition> trans = state.edges;
        for(int i=0;i<trans.size();i++)
        {
            Transition temp = trans.get(i);
            if(temp.onSymbol.equals(symbol))
            {
                result.add(temp.toState);
                result.addAll(Nfa.eclosure(temp.toState));
            }
        }
        return new ArrayList<Node>(result);
    }

   static ArrayList<Node> delta(ArrayList<Node> states,String symbol)
    {
        Set<Node> result = new HashSet<Node>();
        Iterator<Node> iter = states.iterator();
        while(iter.hasNext())
        {
            Node tempToState = iter.next();
            result.addAll(delta(tempToState,symbol));
            
        }
        return (new ArrayList<Node>(result)) ;
    }

    void setLabels()
    { 
        int count=0;
        for(int i=0;i<states.size();i++)
        {
            states.get(i).label=count++;
        }
    }
    public static String addConcatenation(String regex)
    {   
        
        String res="";
        ArrayList operators = new ArrayList();
        operators.add('+');
        operators.add('*');
        
        for(int i=0;i<regex.length();i++)
        {
            Character c1 = regex.charAt(i);
            if(i+1<regex.length())
            {
                Character c2=regex.charAt(i+1);
                res+=c1;
                if(!c1.equals('(') && !c2.equals(')')  && !c1.equals('+') && !c2.equals('*') && !c2.equals('+') &&  !c2.equals('.') && !c1.equals('.'))
                {
                    res+='.';
                }
            }
        }
        res += regex.charAt(regex.length()-1);
        return res;      
    }
    
    public  static int getPrecedence(String c){

        if(c.equals("(")){
            return 1;
        }
        else if(c.equals("+")){
            return 2;
            
        }
        else if(c.equals(".")){
            return 3;
        }
        else if(c.equals("*")){
            return 4;
        }
        else{
            return 6;
        }   

    }
    public static String Shunt(String regex)
    {
        regex = addConcatenation(regex);
        Stack stack = new Stack();
        String postfix="";
        
        for(int i=0;i<regex.length();i++)
        {   
          
            Character token = regex.charAt(i);
             
            if(token.equals('(')){
                stack.push(token);
            }
            else if(token.equals(')')){
                while(!stack.peek().equals('('))
                {
                    postfix+=stack.pop();
        
                }
                stack.pop();
            }
            else if(token.equals('.') || token.equals('+') || token.equals('*') )
            {
                int token_precedence = getPrecedence(token.toString());
               
                if(stack.size()>0)
                {   
                     int peek_precedence = getPrecedence(stack.peek().toString());
                    
                    while((token_precedence <= peek_precedence) )
                    {   
                       
                        
                        postfix+=stack.pop();
                        if(stack.size()>0)
                        {
                            peek_precedence = getPrecedence(stack.peek().toString());

                        }
                        else
                            break;
                            
                    }
                } 
                stack.push(token);
            }
            else
                postfix+=token;        
            
        }
        while(stack.size()>0)
        {
            postfix+=stack.pop();
        }
    
        return postfix;
    }
    void Bfs(Node start,ArrayList<Node> visited,ArrayList<String> dot_code)
    {
        
           Iterator<Transition> iter = start.edges.iterator();
            Queue<Node> queue = new LinkedList<Node>();
            while(iter.hasNext())
            {
                Transition tempTrans = iter.next();
                queue.add(tempTrans.toState);
                String out =" q"+start.label+" -> "+"q"+tempTrans.toState.label+" [ label = "+"\""+tempTrans.onSymbol+"\""+" ];";
                dot_code.add(out);
             
            }
            visited.add(start);
            while(queue.size()>0)
            {
                Node tempNode=queue.remove();
                if(!visited.contains(tempNode))
                    Bfs(tempNode,visited,dot_code);
            }
            

    }

    
    ArrayList<String> Traverse()
    {
        ArrayList<Node> visited = new ArrayList<Node>();
        ArrayList<String> dot_code = new ArrayList<String>();
        this.Bfs(start,visited,dot_code);
        return dot_code;
          
    }
    
}


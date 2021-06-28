package com.leaf.helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
@Controller
public class HelloworldController {
    public String mes ;
    @GetMapping("/full")
    public String full(Model model){
        model.addAttribute("dot", mes);
        return "full_view.html";
    }

    @GetMapping("/hello")
    public String hell(Model model){
        return "helloworld.html";
    }

   

    @PostMapping("/hello")
    public String input(Model model,HttpServletRequest request){
       
       String regex=request.getParameter("regular_expression");
      
        String convert =request.getParameter("country");

       if(!regex.isEmpty()){
           
        String s=graphBuilder(regex,convert);
        mes = s;
        if(s.equals("ϵ")){
            model.addAttribute("error", "invalid input");
            model.addAttribute("inputreturn", regex);
        }
        else{
            model.addAttribute("message", s);
            model.addAttribute("inputreturn", regex);
        }
        
       }
       
        
        return "helloworld.html";
    }
   
    public static String  graphBuilder(String regex,String convert){

        String postfix;
        try{
             postfix = Graph.Shunt(regex);
           
        }
        catch(Exception e){
            String k="ϵ";
            return k;
        }
       
       
       String s="";


       if(convert.equals("NFA")){
            eNfa temp = new eNfa();
            Graph result = temp.build(postfix);
            for(int i=0;i<postfix.length();i++){
                if((!Character.toString(postfix.charAt(i)).equals("."))&& (!Character.toString(postfix.charAt(i)).equals("*"))&&(!Character.toString(postfix.charAt(i)).equals("+")) && !result.alphabet.contains(Character.toString(postfix.charAt(i))) ){
                    result.alphabet.add(Character.toString(postfix.charAt(i)));
                }
            }
            result.setLabels();
            Nfa.build(result);
            ArrayList<String> g = new ArrayList<String>();
            g=result.Traverse();
            ArrayList<Node> fi=new ArrayList<Node>();
            fi=result.finalStates;
            String m="";
            for(int x=0;x<fi.size();x++){
                m+=" node [shape = doublecircle]; q"+fi.get(x).label+";";

            }

                s ="digraph finite_state_machine { rankdir=LR; size=\"8,5\";"+m+"node [shape = point]; qi;node [shape = circle]; qi->q0[label=\"start\"];";

            for(int i=0;i< g.size();i++){

                s+=g.get(i);
            }
            s+="}";


        }
        else if(convert.equals("E-NFA")){
            eNfa temp = new eNfa();
            Graph result = temp.build(postfix);
            
            result.setLabels();
            ArrayList<String> g = new ArrayList<String>();
            g=result.Traverse();
            s ="digraph finite_state_machine { rankdir=LR; size=\"8,5\"; node [shape = doublecircle]; q"+result.finalStates.get(0)+";node [shape = point]; qi;node [shape = circle]; qi->q0[label=\"start\"];";

         
            for(int i=0;i< g.size();i++){

                s+=g.get(i);
               
            }
            s+="}";
            

        }
        else if(convert.equals("DFA")){
            eNfa temp = new eNfa();
            Graph result = temp.build(postfix);
            for(int i=0;i<postfix.length();i++){
                if((!Character.toString(postfix.charAt(i)).equals("."))&& (!Character.toString(postfix.charAt(i)).equals("*"))&&(!Character.toString(postfix.charAt(i)).equals("+")) && !result.alphabet.contains(Character.toString(postfix.charAt(i))) ){
                    result.alphabet.add(Character.toString(postfix.charAt(i)));
                }
            }
            result.setLabels();
         
          
            DfaTransitionTable tempresult = new DfaTransitionTable();

           tempresult.table=tempresult.build(result);
           tempresult.alphabet.addAll(result.alphabet);
          
       
        
            String dot =DfaTransitionTable.Traverse(tempresult.table,result.finalStates.get(0).label) ;
           
            s=dot;
         

        }
        else if(convert.equals("min-DFA")){
            eNfa temp = new eNfa();
            Graph result = temp.build(postfix);
            for(int i=0;i<postfix.length();i++){
                if((!Character.toString(postfix.charAt(i)).equals("."))&& (!Character.toString(postfix.charAt(i)).equals("*"))&&(!Character.toString(postfix.charAt(i)).equals("+")) && !result.alphabet.contains(Character.toString(postfix.charAt(i))) ){
                    result.alphabet.add(Character.toString(postfix.charAt(i)));
                }
            }
            result.setLabels();
            DfaTransitionTable tempresult = new DfaTransitionTable();

           tempresult.table=tempresult.build(result);
           tempresult.alphabet.addAll(result.alphabet);

            MinDfa mindfa= new MinDfa();
            mindfa.minimize(tempresult);
            s=mindfa.build(tempresult); 

                    
            
        }
        
        return s;

    }



 
  
     
}

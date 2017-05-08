/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfatodfa;

/**
 *
 * @author Arham Saeed
 */
public class NFAtoDFA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        NFA o1 = new NFA();
        DFA o2 = o1.NFAtoDFA(o1);
        o2.print();
        
       // DFA o3 = new DFA();
        //DFA o4 = o3.KleenClosure();
        //o4.print();
        //DFA o5 = new DFA();
        //DFA o6 =new DFA();
        //DFA o7 = o5.Concat(o6);
    }
}

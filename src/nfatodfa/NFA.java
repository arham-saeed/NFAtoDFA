]/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfatodfa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Arham Saeed
 */
public class NFA {
    //ArrayList<Transition> TransitionList=new ArrayList<Transition>();
    ArrayList<Transition> TT[];
    int IS;
    int[] FS;
    int noState;
    int noVariables;
    int noFinalState;
    char[] variables;
    Scanner in = new Scanner(System.in);
    NFA(){
        System.out.println("Enter the number of variables : ");
        noVariables = in.nextInt();
        variables = new char[noVariables];
        for (int i = 0; i < noVariables; i++) {
            System.out.print("Enter Variables : ");
            variables[i] = in.next().charAt(0);
        }
        System.out.println("Number of States: ");
        noState = in.nextInt();         //adding two for the initial and final state
        System.out.println("Enter Initial State : ");
        IS = in.nextInt();
        
        System.out.println("No. of Final States: ");
        noFinalState = in.nextInt();
        FS = new int[noFinalState];
        for (int i = 0; i < noFinalState; i++) {
            System.out.println("Enter Final State "+(i)+": ");
            FS[i] = in.nextInt();
        }
        TT = (ArrayList<Transition>[])new ArrayList[noState];             //creating jagged array of object transition
        
        for (int i = 0; i < noState; i++) {       //-2 bc last two states are for initial and final
            TT[i] = new ArrayList<Transition>();
            System.out.println();
            System.out.print("No. of transition of state "+(i)+": ");
            int nt = in.nextInt();             //array of array      //adding one for final state
            for (int j = 0; j < nt; j++) {
                System.out.println("Transition "+(j)+"?");
                System.out.print("\tEnter input character = ");
                char inputChar = in.next().charAt(0);
                System.out.print("\tEnter state = ");
                int state = in.nextInt();
                
                TT[i].add(new Transition(i, inputChar, state));
            }
        }
    }
    ArrayList<Integer> transition( int st,char ch){
        int result = -1;
        ArrayList<Integer> temp = new ArrayList<Integer>();
            int index = -1;
        for (int i = 0; i < TT[st].size(); i++) {
            if (ch == TT[st].get(i).input) {
                temp.add(TT[st].get(i).st);
            }
        }
        return temp;
    }
    public DFA NFAtoDFA(NFA nfa){
        //int InitialState = {0, 0};
        ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>>();
        ArrayList<int[]> TT = new ArrayList<int[]>();
        ArrayList<Integer> FinalStates = new ArrayList<Integer>();
        combinations.add(new ArrayList<Integer>());
        combinations.get(0).add(nfa.IS);
        if (checkFinalState(combinations.get(0),nfa)) {
            FinalStates.add(IS);
        }
        int count = 0;
        int index;
        while(count < combinations.size()){
            int[] temp = new int[2];
            for (int i = 0; i < nfa.noVariables; i++) {
                boolean check = false;      //for final state
                ArrayList<Integer> s1 = new ArrayList<Integer>();
                for (int j = 0; j < combinations.get(count).size(); j++) {
                    if (combinations.get(count).get(j) != -1) {
                        ArrayList<Integer> tempstate = (nfa.transition(combinations.get(count).get(j), nfa.variables[i]));
                        for (Integer integer : tempstate) {
                            s1.add(integer);
                        }
                    }else 
                        s1.add(-1);
                }
                Set<Integer> hs = new HashSet<Integer>(s1);
                s1.clear();
                s1.addAll(hs);
                Collections.sort(s1);
                if (s1.size() == 0) {
                    s1.add(-1);
                }
                if (!combinationExists(s1,combinations)) {
                    combinations.add(s1);
                    index = combinations.size()-1;
                    if (checkFinalState(s1, nfa)) {
                        FinalStates.add(index);
                    }
                }else{
                    index = getIndex(s1,combinations);
                }
                temp[i] = index;
            }
            TT.add(temp);
            count++;
        }
        int[][] tempArray =convertTo2dArray(TT);
        DFA dfa = new DFA(tempArray, nfa.IS, FinalStates, nfa.variables);
        return dfa;
    }
    private static boolean combinationExists(ArrayList<Integer> s1, ArrayList<ArrayList<Integer>> combinations) {
        Boolean check = false;
        for (int i = 0; i < combinations.size(); i++) {
            if (s1.size() == combinations.get(i).size()) {
                check = true;
                for (int j = 0; j < s1.size(); j++) {
                    if (s1.get(j) != combinations.get(i).get(j)) {
                        check = false;
                    }
                }
                if (check) return check;
            }
        }
        return check;
    }
    private static int getIndex(ArrayList<Integer> s1, ArrayList<ArrayList<Integer>> combinations) {
        int count = 0;
        for (int i = 0; i < combinations.size(); i++) {
            if (s1.equals(combinations.get(i))) {
                count = i;
            }
        }
        return count;
    }
    private int[][] convertTo2dArray(ArrayList<int[]> TT) {
        int[][] tempArray = new int[TT.size()][this.noVariables];
        for (int i = 0; i < TT.size(); i++) {
            for (int j = 0; j < this.noVariables; j++) {
                tempArray[i][j] = TT.get(i)[j];
            }
        }
        return tempArray;
    }
    private boolean checkFinalState(ArrayList<Integer> s1, NFA nfa) {
       Boolean check = false;
        for (int i = 0; i < s1.size(); i++) {
            for (int j = 0; j < nfa.noFinalState; j++) {
                if (s1.get(i) == nfa.FS[j]) {
                    check = true;
                }
            }
        }
        return check; 
    }
}

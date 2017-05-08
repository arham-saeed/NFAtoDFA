/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfatodfa;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  
 * @author Arham Saeed
 */
public class DFA {

    Scanner in = new Scanner(System.in);
    int states;
    int noVariables;
    char[] variables;
    int initialState;
    int numberofFS;
    int finalState[];
    int TT[][];
    String RE[];
    DFA(int[][]tempArray, int is, ArrayList<Integer> fs, char[] var){
        states = tempArray.length;
        TT = tempArray;
        initialState = is;
        numberofFS = fs.size();
        finalState = new int[numberofFS];
        for (int i = 0; i < numberofFS; i++) {
            finalState[i] = fs.get(i);
        }
        variables = var;
        noVariables = var.length;
    }
    DFA(){
        System.out.println("Enter Number of states: ");
        states = in.nextInt();
        System.out.println("Enter Number of characters: ");
        noVariables = in.nextInt();
        variables = new char[noVariables];
        System.out.println("Enter the characters: ");
        for (int i = 0; i < noVariables; i++) {
            variables[i] = in.next().charAt(0);
        }
        System.out.println("Enter initial State: ");
        initialState = in.nextInt();
        System.out.println("Enter the number of final States: ");
        numberofFS = in.nextInt();
        finalState = new int[numberofFS];
        for (int i = 0; i < numberofFS; i++) {
            System.out.println("Enter the final state: ");
            finalState[i] = in.nextInt();
        }
        TT =new int[states][noVariables];
        for (int i = 0; i < TT.length; i++) {
            for (int j = 0; j < TT[0].length; j++) {
                System.out.println("enter ");
                System.out.println("\n");
                System.out.print("["+i+", ");
                System.out.print(j+"] = ");
                TT[i][j]= in.nextInt();
            }
        }
        //re();
    }
    public int[] getFS(){
        return finalState;
    }
    public int getIS(){
        return initialState;
    }
   /* public void re(){
        int check = 0;
        RE = new String[variables];
        System.out.println("\n\n\tEnter the Regular expressions for the input");
        for (int i = 0; i < variables; i++) {
            System.out.println("RE of "+(i+1)+" input? (Enter 1 for alphabets,2 for digits, \n3 for _, 4 for +-, 5 for e, 6 for .)");
            check = in.nextInt();
            switch (check) {
            case 1:
                RE[0] = "[A-Za-z]";
                break;
            case 2:
                RE[1] = "[0-9]";
                break;
            case 3:
                RE[2] = "[_]";
                break;
            case 4:
                RE[3] = "[+-]";
                break;
            case 5:
                RE[4] = "[e]";
                break;
            case 6:
                RE[3] = "[.]";
                break;
            default:
                break;
            }
        }
        
    }*/
    boolean validate(String input){
        int state = initialState;
        for (int i = 0; i < input.length(); i++) {
            state = transition(state,input.charAt(i));
            if (state == -1) {
                return false;
            }
        }
        boolean check =false;
        for (int i = 0; i < finalState.length; i++) {
            if (state == finalState[i]) {
                check = true;
            }
        }
        return check;
    }
    int transition( int st,char ch){
        int result = -1;
            int index=-1;
            for (int i = 0; i < noVariables; i++)
            {
                if (variables[i] == ch)
                {
                    index = i;
                }

            }
            result = TT[st][index];
            return result;
    }
    public DFA OR(DFA o2){
        int[] InitialState = {0, 0};
        ArrayList<int[]> combinations = new ArrayList<int []>();
        ArrayList<int[]> TT = new ArrayList<int[]>();
        ArrayList<Integer> FinalStates = new ArrayList<Integer>();
        combinations.add(InitialState);
        Boolean checkifinitialfinal = false;
        for (int i = 0; i < this.numberofFS; i++) {
            if (this.getIS() == this.finalState[i]) {
                checkifinitialfinal = true;
            }
        }
        for (int i = 0; i < o2.numberofFS; i++) {
            if (o2.getIS() == o2.finalState[i]) {
                checkifinitialfinal = true;
            }
        }
        if (checkifinitialfinal) {
            FinalStates.add(0);
        }
        int count = 0;
        int index;
        while(count < combinations.size()){            
            int[] temp = new int[2];
            for (int i = 0; i < this.noVariables; i++) {
                boolean check = false;      //for final state
                int s1 = this.transition(combinations.get(count)[0], this.variables[i]);
                int s2 = o2.transition(combinations.get(count)[1], this.variables[i]);
                //checking for final states
                for (int j = 0; j < this.getFS().length; j++) {
                    if (s1 == this.getFS()[j]) {
                        check = true;
                    }
                }
                for (int j = 0; j < o2.getFS().length; j++) {
                    if (s2 == o2.getFS()[j]) {
                    check = true;
                    }
                }
                int st[] = {s1, s2};
                if (!combinationExists(st, combinations)) {
                    combinations.add(st);
                    index = combinations.size() - 1;
                    
                    if (check) {
                        FinalStates.add(index);
                    }
                }
                else{
                    index = getIndex(st, combinations);
                }
                temp[i] = index;
            }
            TT.add(temp);
            count++;
        }
        int[][] tempArray = convertTo2dArray(TT);
        DFA dfa = new DFA(tempArray, this.initialState, FinalStates, this.variables);
        return dfa;
    }
    private boolean combinationExists(int[] st, ArrayList<int[]> combinations){
        boolean result = false;
        for (int i = 0; i < combinations.size(); i++) {
            if (combinations.get(i)[0] == st[0] && combinations.get(i)[1] == st[1]) {
                result = true;
                break;
            }   
        }
        return result;
    }
    private int getIndex(int[] st, ArrayList<int[]> combinations){
        int index = -1 ;
        for (int i = 0; i < combinations.size(); i++) {
            if (combinations.get(i)[0] == st[0] && combinations.get(i)[1] == st[1]) {
                return i;
            }
        }
        return index;
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
    public void print(){
        System.out.print("\n\n");
        for (int i = 0; i < noVariables; i++) {
            System.out.print("\t "+variables[i]);
        }
        System.out.println();
        for (int i = 0; i < states; i++) {
            System.out.print(i
            );
            if (initialState == i) {
                System.out.print("-");
            }
            for (int j = 0; j < finalState.length; j++) {
                if (i == finalState[j]) {
                    System.out.print("+");
                }
            }
            for (int j = 0; j < noVariables; j++) {
                System.out.print("\t"+TT[i][j]);
            }
            System.out.println();
        }
    }
    public DFA complement(){
        int nofinalstate = states - numberofFS;
        ArrayList<Integer> finalst = new ArrayList<Integer>();
        for (int i = 0; i < states; i++) {
            for (int j = 0; j < numberofFS; j++) {
                if (i != finalState[j]) {
                finalst.add(i);
                } 
            }
        }
        DFA dfa = new DFA(TT, this.initialState, finalst, this.variables);
        return dfa;
    }
    public DFA intersection(DFA o1){
        DFA acomp = this.complement();
        DFA bcomp = o1.complement();
        DFA aplusb = acomp.OR(bcomp);
        DFA result = aplusb.complement();
        return result;
    }
    public DFA Concat(DFA o2){
        int InitialState = 0;
        ArrayList<Integer> XCombination = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> YCombination = new ArrayList<ArrayList<Integer>>();
        ArrayList<int []> TT = new ArrayList<int []>();
        XCombination.add(InitialState);
        YCombination.add(new ArrayList<Integer>());
        Boolean checkifinitialfinal = false;
        for (int i = 0; i < this.numberofFS; i++) {
            if (this.initialState == finalState[i]) {
                YCombination.get(0).add(o2.initialState);
                checkifinitialfinal = true;
                break;
            }
        }
        if (!checkifinitialfinal) {
            YCombination.get(0).add(-1);
        }
        ArrayList<Integer> FinalStates = new ArrayList<Integer>();
        int count = 0;
        int index;
        while(count< XCombination.size()){
            int[] temp = new int[2];
            for (int i = 0; i < this.noVariables; i++) {
                int s1 = this.transition(XCombination.get(count), this.variables[i]);
                ArrayList<Integer> s2 = new ArrayList<Integer>();
                for (int j = 0; j < this.getFS().length; j++) {
                    if (s1 == this.getFS()[j]) {
                        s2.add(o2.getIS());
                    }
                }
                if (YCombination.get(count).get(0) != -1){
                    for (int j = 0; j < YCombination.get(count).size(); j++) {
                        s2.add(transition(YCombination.get(count).get(j), this.variables[i], o2));
                    }                
                    Set<Integer> hs = new HashSet<Integer>(s2);
                    s2.clear();
                    s2.addAll(hs);
                    Collections.sort(s2);
                }
                if (s2.size() == 0) {
                    s2.add(-1);
                }
                //removeRepeat(s2);
                if (!combinationExists(s1,s2,XCombination, YCombination)) {
                    XCombination.add(s1);
                    YCombination.add(s2);
                    index = XCombination.size()-1;
                    
                    if(checkFinalState(s2, o2)) 
                        FinalStates.add(index);
                }else{
                    index = getIndex(s1, s2, XCombination, YCombination);
                }
                 temp[i] = index;
            }
            TT.add(temp);
            count++;
        }
        int[][] tempArray = convertTo2dArray(TT);
        DFA dfa = new DFA(tempArray, this.initialState, FinalStates, this.variables);
        return dfa;
    }

    private Boolean combinationExists(int s1, ArrayList s2, ArrayList<Integer> XComb, ArrayList<ArrayList<Integer>> YComb){
        Boolean check = false;
        int count = 0;
        while(count < XComb.size()){
            if (s1 == XComb.get(count)) check = true;
            if (s2.size() != YComb.get(count).size())   check = false; 
            else{
                for (int i = 0; i < YComb.get(count).size(); i++) {
                    if (s2.get(i) != YComb.get(count).get(i))   check = false;
                }  
            }
            if (check) {
                return true;
            }
            count++;
        }
        return check;
    }
    
    private int getIndex(int s1, ArrayList<Integer> s2, ArrayList<Integer> XComb, ArrayList<ArrayList<Integer>> YComb) {
        Boolean check = false;
        int count = 0;
        while(count < XComb.size()){
            if (s1 == XComb.get(count)) check = true;
            if (s2.size() != YComb.get(count).size())   check = false; 
            else{
                for (int i = 0; i < YComb.get(count).size(); i++) {
                    if (s2.get(i) != YComb.get(count).get(i))   check = false;
                }  
            }
            if (check)  return count;
            count++;
        }
        return count;
    }

    private Integer transition(int st, char ch, DFA o2) {
        int result = -1;
            int index=-1;
            for (int i = 0; i < o2.noVariables; i++)
            {
                if (o2.variables[i] == ch)
                {
                    index = i;
                }
            }
            result = o2.TT[st][index];
            return result;
    }

    private Boolean checkFinalState(ArrayList<Integer> s2, DFA o2) {
        Boolean check = false;
        for (int i = 0; i < s2.size(); i++) {
            for (int j = 0; j < o2.numberofFS; j++) {
                if (s2.get(i) == o2.finalState[j]) {
                    check = true;
                }
            }
        }
        return check;
    }
    public DFA NFAtoDFA(NFA nfa){
        //int InitialState = {0, 0};
        ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>>();
        ArrayList<int[]> TT = new ArrayList<int[]>();
        ArrayList<Integer> FinalStates = new ArrayList<Integer>();
        combinations.add(new ArrayList<Integer>());
        combinations.get(0).add(nfa.IS);
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
                    }
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
    private static int getIndex(ArrayList<Integer> s1, ArrayList<ArrayList<Integer>> combinations) {
        int count = 0;
        for (int i = 0; i < combinations.size(); i++) {
            if (s1.equals(combinations.get(i))) {
                count = i;
            }
        }
        return count;
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
    public DFA KleenClosure(){
        ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>>();
        ArrayList<int[]> TT = new ArrayList<int[]>();
        ArrayList<Integer> FinalStates = new ArrayList<Integer>();
        combinations.add(new ArrayList<Integer>());
        Boolean checkifinitialfinal = false;
        for (int i = 0; i < numberofFS; i++) {
            if (initialState == finalState[i]) {
                checkifinitialfinal = true;
            }
        }
        combinations.get(0).add(initialState);
        if (!checkifinitialfinal) {
            combinations.get(0).add(-99);
        }
        FinalStates.add(initialState);
        int count = 0;
        int index;
        while(count < combinations.size()){
            int[] temp = new int[noVariables];
            for (int i = 0; i < noVariables; i++) {
                ArrayList<Integer> s1 = new ArrayList<Integer>();
                for (int j = 0; j < combinations.get(count).size(); j++) {
                    if (combinations.get(count).get(j) >= 0) {
                        int tempstate = (transition(combinations.get(count).get(j), variables[i]));
                        s1.add(tempstate);
                        ArrayList<Integer> templist = new ArrayList<Integer>();
                        templist.add(tempstate);
                        if (checkFinalState(templist, this)) {
                            s1.add(initialState);
                        }
                    }
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
                    if (checkFinalState(s1, this)) {
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
        DFA dfa = new DFA(tempArray, 0, FinalStates, variables);
        return dfa;
    }
}

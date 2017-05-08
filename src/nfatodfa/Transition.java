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
public class Transition {
    int currentState;
    char input;
    int st;
    Transition(int curr, char in, int state){
        currentState = curr;
        input = in;
        st = state;
    }
}

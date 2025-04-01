/*
 * Copyright (c) 2024. Robin Hillyard
 */

 package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

 import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
 import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
 
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Optional;
 
 public class TicTacToeNode implements Node<TicTacToe> {
 
     /**
      * @return true if this node is a leaf node (in which case no further exploration is possible).
      */
     public boolean isLeaf() {
         return state().isTerminal();
     }
 
     /**
      * @return the State of the Game G that this Node represents.
      */
     public State<TicTacToe> state() {
         return state;
     }
 
     /**
      * Method to determine if the player who plays to this node is the opening player (by analogy with chess).
      * For this method, we assume that X goes first so is "white."
      * NOTE: this assumes a two-player game.
      *
      * @return true if this node represents a "white" move; false for "black."
      */
     public boolean white() {
         return state.player() == state.game().opener();
     }
 
     /**
      * @return the children of this Node.
      */
     public Collection<Node<TicTacToe>> children() {
         return children;
     }
 
     /**
      * Method to add a child to this Node.
      *
      * @param state the State for the new child.
      */
     public void addChild(State<TicTacToe> state) {
         Node<TicTacToe> child = new TicTacToeNode(state);
         children.add(child);
     }
 
     /**
      * This method sets the number of wins and playouts according to the children states.
      * This is called by the default explore() method in the Node interface.
      */
     public void backPropagate() {
         // In this implementation, we rely on manual management of
         // the wins and playouts stats via the addResult method
     }
     
     /**
      * Adds a simulation result to this node.
      * 
      * @param result 2 for win, 1 for draw, 0 for loss
      */
     public void addResult(int result) {
         playouts++;
         wins += result;
     }
 
     /**
      * @return the score for this Node and its descendents a win is worth 2 points, a draw is worth 1 point.
      */
     public int wins() {
         return wins;
     }
 
     /**
      * @return the number of playouts evaluated (including this node). A leaf node will have a playouts value of 1.
      */
     public int playouts() {
         return playouts;
     }
 
     public TicTacToeNode(State<TicTacToe> state) {
         this.state = state;
         children = new ArrayList<>();
         wins = 0;
         playouts = 0;
     }
 
     private final State<TicTacToe> state;
     private final ArrayList<Node<TicTacToe>> children;
 
     private int wins;
     private int playouts;
 }

 #code added
/*
 * Copyright (c) 2024. Robin Hillyard
 */

 package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

 import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
 import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
 import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
 
 import java.util.Collection;
 import java.util.Comparator;
 
 /**
  * Class to represent a Monte Carlo Tree Search for TicTacToe.
  */
 public class MCTS {
 
     public static void main(String[] args) {
         TicTacToe game = new TicTacToe();
         State<TicTacToe> initialState = game.start();
         MCTS mcts = new MCTS(new TicTacToeNode(initialState));
         
         System.out.println("Running Monte Carlo Tree Search with 1000 iterations...");
         long startTime = System.currentTimeMillis();
         
         // Run the search with 1000 iterations
         mcts.search(1000);
         
         long endTime = System.currentTimeMillis();
         System.out.println("Search completed in " + (endTime - startTime) + "ms");
         
         // Find the best move
         Node<TicTacToe> bestNode = mcts.bestChild();
         System.out.println("Best node stats - Wins: " + bestNode.wins() + ", Playouts: " + bestNode.playouts());
         
         double winRatio = bestNode.playouts() > 0 ? (double) bestNode.wins() / bestNode.playouts() : 0.0;
         System.out.println("Best move found. Win ratio: " + winRatio);
         
         // You can continue the game with the best move
         State<TicTacToe> nextState = bestNode.state();
         System.out.println("Board after best move:");
         System.out.println(((TicTacToe.TicTacToeState) nextState).position().render());
         
         // Optionally, print information about all available moves
         System.out.println("\nAll possible moves:");
         mcts.printAllMoves();
     }
 
     public MCTS(Node<TicTacToe> root) {
         this.root = root;
     }
     
     /**
      * The main MCTS algorithm.
      * 
      * @param iterations number of iterations to run
      */
     public void search(int iterations) {
         for (int i = 0; i < iterations; i++) {
             // 1. Selection: Find the most promising node to explore
             Node<TicTacToe> selected = treePolicy(root);
             
             // 2. Simulation: Not needed as we directly evaluate terminal positions
             // This is handled by the Node.explore() method called in treePolicy
             
             // 3. Print some debugging info
             if (i % 100 == 0) {
                 System.out.println("Iteration " + i + " - Root stats: Wins=" + 
                     root.wins() + ", Playouts=" + root.playouts());
             }
         }
     }
     
     /**
      * The Tree Policy to select the most promising node to explore.
      * If the node is a leaf or hasn't been explored yet, it expands it.
      * Otherwise, it selects the best child using UCT.
      * 
      * @param node the starting node
      * @return the selected node
      */
     private Node<TicTacToe> treePolicy(Node<TicTacToe> node) {
         // If we reach a terminal node, return it
         if (node.isLeaf()) {
             return node;
         }
         
         // If the node hasn't been explored yet, explore it and return
         if (node.children().isEmpty()) {
             node.explore();
             return node;
         }
         
         // Otherwise, select the best child and continue
         return treePolicy(uctBestChild(node));
     }
     
     /**
      * Selects the best child according to the UCT formula.
      * 
      * @param node the parent node
      * @return the best child
      */
     private Node<TicTacToe> uctBestChild(Node<TicTacToe> node) {
         final double c = 1.414; // Sqrt(2) - standard UCT exploration parameter
         final int parentPlayouts = Math.max(1, node.playouts()); // Avoid division by zero
         
         return node.children().stream()
                 .max(Comparator.comparingDouble(child -> {
                     int childPlayouts = Math.max(1, child.playouts()); // Avoid division by zero
                     double exploitation = (double) child.wins() / childPlayouts;
                     double exploration = c * Math.sqrt(Math.log(parentPlayouts) / childPlayouts);
                     return exploitation + exploration;
                 }))
                 .orElseThrow(() -> new RuntimeException("No children found"));
     }
     
     /**
      * Returns the best child of the root based on win ratio.
      * 
      * @return the best child node
      */
     public Node<TicTacToe> bestChild() {
         if (root.children().isEmpty()) {
             root.explore();
         }
         
         return root.children().stream()
                 .max(Comparator.comparingDouble(child -> 
                     child.playouts() > 0 ? (double) child.wins() / child.playouts() : 0.0))
                 .orElseThrow(() -> new RuntimeException("No children found"));
     }
     
     /**
      * Returns the best move from the current position.
      * 
      * @return the best move
      */
     public Move<TicTacToe> bestMove() {
         Node<TicTacToe> best = bestChild();
         State<TicTacToe> currentState = root.state();
         State<TicTacToe> bestState = best.state();
         
         // Find the move that leads to the best child state
         for (Move<TicTacToe> move : currentState.moves(currentState.player())) {
             if (currentState.next(move).equals(bestState)) {
                 return move;
             }
         }
         
         throw new RuntimeException("Best move not found");
     }
     
     /**
      * Prints information about all possible moves from the root.
      */
     public void printAllMoves() {
         if (root.children().isEmpty()) {
             root.explore();
         }
         
         int i = 1;
         for (Node<TicTacToe> child : root.children()) {
             double winRatio = child.playouts() > 0 ? (double) child.wins() / child.playouts() : 0.0;
             System.out.printf("%d. Win ratio: %.2f%% (%d/%d playouts)%n", 
                     i++, winRatio * 100, child.wins(), child.playouts());
             System.out.println(((TicTacToe.TicTacToeState) child.state()).position().render());
             System.out.println();
         }
     }
 
     private final Node<TicTacToe> root;
 }
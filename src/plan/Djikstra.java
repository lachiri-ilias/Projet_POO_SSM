package plan;

import java.util.*;
import robot.*;

public class Dijkstra {

  // public void dijkstra(Graph graph, int source) {
  //   int taille = graph.getGraphSize();
  //   int i,v;
  //   boolean[] sommetVisite = new boolean[taille];
  //   int[] distance = new int[taille];
  //   for (i = 0; i < taille; i++) {
  //     sommetVisite[i] = false;
  //     distance[i] = 999; //Integer.MAX_VALUE; A modifier apres ;;;!!!!!
  //   }

  //   // Distance of self loop is zero
  //   distance[source] = 0;
  //   for (i = 0; i < taille; i++) {
  //     // Update the distance between neighbouring vertex and source vertex
  //     int u = findMinDistance(distance, sommetVisite);
  //     sommetVisite[u] = true;

  //     // Update all the neighbouring vertex distances
  //     for (v = 0; v < taille; v++) {
  //       if (!sommetVisite[v] &&  getGraphVal(u,v) != 0 && (distance[u] + getGraphVal(u,v) < distance[v])) {
  //         distance[v] = distance[u] +  getGraphVal(u,v);
  //       }
  //     }
  //   }
  //   for (i = 0; i < distance.length; i++) {
  //     System.out.println(String.format("Distance from %s to %s is %s", source, i, distance[i]));
  //   }
  // }

  // // Finding the minimum distance
  // private  int findMinDistance(int[] distance, boolean[] sommetVisite) {
  //   int minDistance = 999; //Integer.MAX_VALUE;
  //   int minDistanceVertex = -1;
  //   for (int i = 0; i < distance.length; i++) {
  //     if (!sommetVisite[i] && distance[i] < minDistance) {
  //       minDistance = distance[i];
  //       minDistanceVertex = i;
  //     }
  //   }
  //   return minDistanceVertex;
  // }
  }
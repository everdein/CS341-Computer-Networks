// Matthew Clark
// CS341 - Computer Networks
// Assignment 3 - Dijkstra's Algorithm

#include <iostream>
using namespace std;
#include <stdio.h>
#include <limits.h>

// Number of vertices in the graph.
#define V 7

void dijkstra(int graph[V][V], int source);

// Main function creates matrix and calls Dijkstra function.
int main()
{
	int graph[V][V] =
			 	   {// 0  1  2  3  4   5   6
					  {0, 2, 4, 0, 0,  7,  0}, // 0
					  {2, 0, 3, 3, 0,  0,  0}, // 1
					  {4, 3, 0, 4, 3,  8,  0}, // 2
					  {0, 3, 4, 0, 6,  0,  0}, // 3
					  {0, 0, 3, 6, 0,  6,  8}, // 4
					  {7, 0, 8, 0, 6,  0, 12}, // 5
					  {0, 0, 0, 0, 8, 12,  0}  // 6
					 };
	dijkstra(graph, 0);
	return 0;
}

// Finds the vertex with minimum distance value from the set of vertices.
int minimumDistance(int distance[], bool shortestPathTreeSet[])
{
	// Initializes minimum value.
	int minimum = INT_MAX, minimum_index;

	for (int v = 0; v < V; v++)
		if (shortestPathTreeSet[v] == false && distance[v] <= minimum)
		{
			minimum = distance[v], minimum_index = v;
		}
	return minimum_index;
}

// Prints the constructed distance array.
int printSolution(int distance[], int n)
{
	printf("Vertex   Distance From Source\n");
	for (int i = 0; i < V; i++)
	{
		printf("%d \t\t %d\n", i, distance[i]);
	}
	return 0;
}

// Implements Dijkstra's shortest path algorithm for adjacent matrix representation.
void dijkstra(int graph[V][V], int source)
{
	// Hold the shortest distance from source.
	int distance[V];

	// shortestPathTreeSet[i] will true if vertex i is included in shortest path tree or shortest distance from source to i is finalized.
	bool shortestPathTreeSet[V];

	// Initialize all distances as INFINITE and shortestPathTreeSet[] as false.
	for (int i = 0; i < V; i++)
	{
        distance[i] = INT_MAX, shortestPathTreeSet[i] = false;
	}

	// Distance of source vertex from itself is always 0.
	distance[source] = 0;

	// Finds shortest path for all vertices.
	for (int count = 0; count < V-1; count++)
	{
		// Picks the minimum distance vertex from the set of vertices not yet processed, and u is always equal to source in first iteration.
		int u = minimumDistance(distance, shortestPathTreeSet);

		// Mark the picked vertex as processed.
		shortestPathTreeSet[u] = true;

		// Updates distance value of the adjacent vertices of the picked vertex.
		for (int v = 0; v < V; v++)
		{
			/* Update distance[v] only if it's not in shortestPathTreeSet, there is an edge from u to v,
			   and total weight of path from source to v through u is smaller than current value of distance[v]. */
			if (!shortestPathTreeSet[v] && graph[u][v] && distance[u] != INT_MAX && distance[u]+graph[u][v] < distance[v])
			{
				distance[v] = distance[u] + graph[u][v];
			}
		}
	}
	
	// Prints the constructed distance array.
	printSolution(distance, V);
}

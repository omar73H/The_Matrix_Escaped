# The_Matrix_Escaped

## Search Algorithms ##

### Breadth-First Search: ###

The Breadth-First Search (BFS) algorithm starts at the root node and explores all nodes at a certain level before moving on to the next level. The algorithm is guaranteed to find a solution given that it exists and the number of operators is finite as under this assumption (which in most use cases is valid), the algorithm will not get stuck. For our implementation, we used a priority queue that uses the depth of the node to decide the priority. The root node is inserted into the queue and poll operations are performed until the queue is empty or a node that satisfies the goal condition is reached. Shallower nodes (those closer to the root) have a higher priority and are therefore expanded first.


### Depth-First Search: ###

The Breadth-First Search (BFS) algorithm starts at the root node and explores as deep as possible before backtracking to untraversed paths. In general This algorithm is not guaranteed to find a solution if the search tree is infinite, which is the case in many practical problems. We again employ a priority queue that uses the depth of the node to decide the priority in a way opposite to DFS. The root node is inserted into the queue and poll operations are performed until the queue is empty or a node that satisfies the goal condition is reached. Deeper nodes (those further from the root) have a higher priority and are therefore expanded first.


### Iterative Deepening Search: ###

The Iterative Deepening Search (IDS) is a modification to the Depth-First Search that aims to solve the problem that DFS is not guaranteed to find a solution while maintaining its low space complexity. IDS works by iteratively performing DFS up to a limited maximum depth and increasing this max after each iteration. Implementation-wise, IDS calls DFS inside a perpetual loop and passes a parameter specifying the stopping depth


### Uniform Cost Search: ###

Conceived by Edsger Dijkstra, Uniform Cost Search (UCS), also known as Dijkstra’s algorithm, is the fourth search algorithm we implemented, and unlike the previous three, it is an optimal one. Meaning that it finds the path with the lowest cost. UCS works by assigning a “path cost” to each node which is the summation of the cost visiting the node and the cost of visiting the nodes prior to it (In other words, the cost of the node + the path cost of the previous node). After calculating the path cost for a node, it’s inserted into the priority queue and the node with the lowest path cost is polled first.


### Greedy Search: ###

The fifth algorithm we implemented is Greedy Search. This algorithm utilizes a so-called “heuristic function” which tries to estimate the cost from a given node to the nearest goal. Such an estimation can not be exact without exploring the entire search space which would defeat the purpose of the search. Instead, simplifying assumptions are applied to find appropriate estimates that satisfy certain conditions (discussed later). The result of the heuristic function is used for the priority queue in the case of Greedy Search.


### A* Search: ###

The final algorithm we implemented is A* Search. It is the second optimal algorithm out of the six. A* also takes advantage of the heuristic function utilized by Greedy Search, however, what make A* optimal is that it adds the path cost to the result of the heuristic function and the result is used by the priority queue. A* Search can be shown to expand a number of nodes less than or equal to any optimal algorithm



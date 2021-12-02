# The_Matrix_Escaped


## Search tree Node ##
Information is power that is why feeding Node with information, we solve the matrix based on the knowledge the node contains.
That is why in our SearchTreeNode class (Node) we tried to state all the information needed to get the solution of any matrix grid.
so we create a pointer to the parent Node so we can reach from any node it's parent or ansestors , also used the opString and operator to explain what is the operator done to reach this node from its parent (the parent node and opString pairs can be used to build up the solution for the Matrix Grid).Moreover included the depth for a node and its pathcost so that we can identify which depth the node is right now , this can be useful when interpeting the DFS and BFS algorithms , and the path cost for any algorithm that relies on Cost to reach optimality as (UCS and A*), and lastly the state of the node so that the Neo can understand the enviroment .
## State #
State is a tredecuple (13 parameters) that consists of Neo's both X ,Y cooordinates and Health , we also include the state of all dynamic objects in grid as Agents,Hostage and pills , also we introduced some parameters that will benefit Neo in some action and understand what was an effect on action for objects in grid.Lets unravel the mystery and understand what are the parameters we used.Let us begin with the hostage health , hostage health is a byte array that contains the health of all hostage ,we decided to declare it as byte for memory optimization purposes as we found that the maxDamage is between 0-100 which are ranges that can presented by Byte.for currentlyCarriedHostages,movedHostages,hostagesToAgents,killedTransHostages and pills were declared as a short because of the growth of these parameters with limit as for any hostage related parameter its max would be 16 hostages , and since pills are less than hostages we decided to give it the same Short as the rest 3 parameters. the last 4 parameters are a bitmask representationfor the Hostages or pills respectively.currentlyCarriedHostages represent that Neo is currently carried hostage X and to know which hostage we are carrying right now, we can just check the bit with the hostage ID (IDs starts from 0-15 "16 hostages at ma") whether it's 1 for carried and 0 for not carried.For the movedHostages we turn the index with hostage ID X into 1 since the time it was carried , but when we deliver hostage to the telephone booth we just make the hostage Dropped index in currentlyCarriedHostages into 0 but nothing changes in movedHostage , this enable us to understand that hostage reached , the same logic applies for hostageToAgents and killedTransHostage but in case of hostageToAgent bit 1 for a specific hostage means that hostage with ID x turned into an Agent and 0 means it does not.killedTransHostage means that if we kill a hostage that turned into an agent we just it's bit crossponding to in the bitmask into 1.pill bitmask is self exploratory , it just highlights that if the pill is taken or not .We found out that the max growth for normal agents in a 15x15 grid is at most 217 agent so to make a bit mask that contains 217 bit is impossible  but we can create 3 Long parameters and 1 int to cover the 217 bits , that is why we created 4 parameters called killedNormalAgent0-3 , these parameters are used to indicate the normal agents that got killed by Neo from start to the current Node that hold this state.

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



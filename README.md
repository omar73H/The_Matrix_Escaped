# The_Matrix_Escaped


## Search tree Node ##
Information is power that is why feeding Node with information solve the matrix based on the knowledge the node contains.
That is why in our SearchTreeNode class (Node) we tried to state all the information needed to get the solution of any matrix grid.
so we create a pointer to the parent Node so we can reach from any node it's parent or ansestors , also used the opString and operator to explain what is the operator done to reach this node from its parent (the parent node and opString pairs can be used to build up the solution for the Matrix Grid).Moreover included the depth for a node and its pathcost so that we can identify which depth the node is right now , this can be useful when interpeting the DFS and BFS algorithms , and the path cost for any algorithm that relies on Cost to reach optimality as (UCS and A*), and lastly the state of the node so that the Neo can understand the enviroment .
## State #
State is a tredecuple (13 parameters) that consists of Neo's both X ,Y cooordinates and Health , we also include the state of all dynamic objects in grid as Agents,Hostage and pills , also we introduced some parameters that will benefit Neo in some action and understand what was an effect on action for objects in grid.Lets unravel the mystery and understand what are the parameters we used.Let us begin with the hostage health , hostage health is a byte array that contains the health of all hostage ,we decided to declare it as byte for memory optimization purposes as we found that the maxDamage is between 0-100 which are ranges that can presented by Byte.for currentlyCarriedHostages,movedHostages,hostagesToAgents,killedTransHostages and pills were declared as a short because of the growth of these parameters with limit as for any hostage related parameter its max would be 16 hostages , and since pills are less than hostages we decided to give it the same Short as the rest 3 parameters. the last 4 parameters are a bitmask representationfor the Hostages or pills respectively.currentlyCarriedHostages represent that Neo is currently carried hostage X and to know which hostage we are carrying right now, we can just check the bit with the hostage ID (IDs starts from 0-15 "16 hostages at ma") whether it's 1 for carried and 0 for not carried.For the movedHostages we turn the index with hostage ID X into 1 since the time it was carried , but when we deliver hostage to the telephone booth we just make the hostage Dropped index in currentlyCarriedHostages into 0 but nothing changes in movedHostage , this enable us to understand that hostage reached , the same logic applies for hostageToAgents and killedTransHostage but in case of hostageToAgent bit 1 for a specific hostage means that hostage with ID x turned into an Agent and 0 means it does not.killedTransHostage means that if we kill a hostage that turned into an agent we just it's bit crossponding to in the bitmask into 1.pill bitmask is self exploratory , it just highlights that if the pill is taken or not .We found out that the max growth for normal agents in a 15x15 grid is at most 217 agent so to make a bit mask that contains 217 bit is impossible  but we can create 3 Long parameters and 1 int to cover the 217 bits , that is why we created 4 parameters called killedNormalAgent0-3 , these parameters are used to indicate the normal agents that got killed by Neo from start to the current Node that hold this state.


## The Matrix Problem ##
To implement the Matrix Problem we began by implementing a generic abstract class "SearchProblem" to be later extended by class "Matrix" which carries the initial state and requires the implementation of two functions: calculatePathCost and goalTest. "Matrix" overrides calculatePathCost and based on the operator performed, number of dead hostages and number of killed agents, the method determines the cost of the node and adds it to the cost of the previous nodes. As for the implementation of goalTest, we check if Neo has arrived at telephone booth with all hostages either delivered or killed. If so the given state is a goal. Matrix also defines a function called expand which is used by the various search algorithms (discussed later) to apply on a given node the valid operators (discussed in the next section) and generate the resulting nodes


## Main functions implemented ##
We now need to give our Neo actions that the AI can utilize to reach the goal defined  , So let us start with Move actions.
## Move Action ##
Move action allows Neo go through the grid cell by cell to check what other actions can be performed from its location; that is why we need to make Neo move in all directions so we defined up movement,down movement , left and right movements , that is why we introduced move function, it takes actionId and currentNode as parameters.actionId ranges from 0-3 as 0 means up, 1 means down ,2 means left and 3 means right, currentNode carries the parent node before movement. So we check if action Id is 0,so we need to check that we are not in the up farthest row in the grid to not move out of grid , and we also need to check if the cell we are going does not contains any kind of agent whether it is a normal one or a hostage turned to agent so here we created a helper function that tells me if there is an agent or not in that cell(agentAt). AgentAt function takes all the currentNode state parameters and a boolean tryKill which will be false if Neo is trying to move and true when Neo kills. Since we use this helper function from the move, then we input tryKill as false, so basically, this helper function tries to loop on all the game hostages and normal agents and check if hostages are deceased and turned into an agent or going to die in the next time step (after performing the action), so we will stop Neo movement to a cell with a current normal agent, or hostage turned into an agent or its current health is 98 or 99 which means that if Neo moved to this cell, The hostage would turn agent at the time Neo steps into the neighboring cell, so Neo will be at a cell with an agent, which is an illegal move. Back to move function and after executing the agentAt helper function, if there is no agent at the up Cell, we can perform the up action so we can create the new node that will be passed to the priority queue.new Node creation is made in 2 steps. First, we need to call another helper function called timeStep, which takes the currentNode as input and performs the fundamental state changes as it increases the non-saved hostage damage and checks if any hostage damage reached 100 and turns it into an agent and return this new state, then we can change the neo location based on the action and create the new node with the new state and give the node all the information needed as its parent which is currentNode, depth to be the parent depth +1, the operator applied to reach this node and the node path cost. The same logic is applied for different movements (down, left, and right ), but we change the action Id, also change the Neo new location and the node operatorId.

## Kill Action ##
So in some scenarios Neo need to kill the agent to save hostage fast (less action) or if neo is trapped by these agent, that is why Kill action need to be implemented. Kill action can also be made in up, down, left, right directions but all at the same time .so we performed the kill function that also takes the currentnode(Parent) as an input and at first we check a condition that will not allow us to kill if it's true which is a state where iam in a cell with a hostage  damage equals to 98 or 99 , and then i performed kill which kills all agents around me which makes the new state , a state with an agent in the same cell iam in , that is why i need to check if iam in a cell with a hostage and damage equal 98 or 99 so i wont kill at this point.Let us say that there is no hostage with this stats, we need to perform the kill action in every direction so we use the agentAt helper function to check if there is a killable agent or a hostage turned to agent at the cell iam checking at,so we check in every direction if there is an agent to kill  so if there we use timestep to create the new state and we can maipulate the index of the killed agents in there crossponding location in Node state, so if we killed a hostage turned agent we flip the 0 to 1 in killedTransHostage parameter in node state and for normal agent we flip it's index in killednormalagent0-3 parameters, and whatever number of kills from 1-4 we just increase Neo's damage by 20.

## Take pill Action ##
To help Neo in his journey to save the hostage, taking pills in some cells would be helpful as it decreases both Neo and all hostages damage by 20, take pill function takes currentNode as input, and inside we check if Neo in a cell that contains a pill by using the helper function isTherePill which return true if Neo in a cell which contains pill that is not taken before, we can know this for the Pill parameter in State as we check the index of the pill, if it's 0 then it is not taken. After checking if there is a pill at cell Neo in, we go through all the hostages and decrease their damages by 20 if they were not already dead (turned to an agent) or saved(dropped at telephone booth), and decrease neo damage by 20. The new State would be the same as its parent, but we change the hostage's health and neo health so that the new node will be a node with Takepill operation Id, and the new State stated before also with new calculated path cost and depth of parent +1.

## Fly Action ##
Flying with a pad is handy to travel throughout the grid faster as Neo can use a pad to travel significantly to a far cell in the grid. The fly function also, as all the past function takes currentNode as input, and we use a helper function to check if Neo is in a cell that contains a Pad so Neo can travel to its corresponding end pad, so isTherePad helper function loops through all pads and check if Neo in a location that contains a pad, so if there is a pad we continue in fly function and use the time step function to calculate the changes will happen in next move. After returning the state, we just change the neo location to another end of the pad Neo used and return the new node with its correct form in-depth, operator, cost, and parent.

## Carry Action ##
So to be able to save a hostage, Neo needs to carry the hostage and go telephone booth to drop it there. That is why the pickup agent function is used to carry a hostage, so we check if the number of carries allowed for us is at max by checking all hostages Neo is carrying right now. Suppose there is room for another hostage. In that case, we check if Neo is in a location with a hostage that can be carried by utilizing the helper function hostageAt, which checks if the hostage at this cell was not carried or moved by Neo so if the hostage is not moved. It is healthy, and Neo is in the same cell as this hostage. Neo can carry this hostage, and we flip the carried hostage index in the currentlyCarriedHostages parameter in the state. We apply the time step to get the new state and replace the currentlyCarriedHostages with the one computed above after carrying.

## Drop Action ##
Dropping a hostage is only allowed when Neo is in telephone booth location; that is why we check if Neo location is the same as the Telephone booth cell location, so if it comes out to be true, we simply drop all hostage at once, and this can be made by setting the bitmask currentlyCarriedHostages in the state to 0 which means that Neo no longer carries any hostage, then we call the time step to calculate the changes happened to other hostages by performing an action and outputting a state  in currentlyCarriedHostages will be replaced with the newcurrentlyCarriedHostages in and create the new node with the correct form and pass it to Queue


## Repeated state handeling ##
Since multiple nodes can be expanded from just one parent, this means that from these nodes, we can reach the same state but using a different branch. and since this would affect the size and running complexity for any strategy, handling such a repeated state will be handy to optimize the time taken to reach the goal and to decrease the size the code takes to reach the goal (decrease the number of nodes in the Queue).In our case, we tried to remove the repeated states by creating a hash map that contains a key and value. The key was a string encoding of all the states parameters except for hostage health and neo health because, in the case of hostage health, we can see that if we included in the encoding, this will result in a new different state every step Neo takes, for example, Neo move down, right, up then left this should be a repeated state because it is useless set of actions, that is why including such a state with hostage health will not make us able to find that this set of actions led us to a repeated state. For Neo's health, we do not need it in our string encoding because based on the killing parameters and pills taken to interpret Neo's health, neo health would not make any difference in repeated states. For the hash map value, we decided to put inside the sum of the hostage damages in the corresponding state encoding, so now when we find a new state with the same encoding as the one in hashmap, we check the summation of hostage's damage, if the hostage summation for the current state is same as or greater than the one stated in the hash map Value section we won't expand this node because it is the same state, but in a condition where the hostage's damage is not good as the one already inside the hash map, so we won't expand such a node. However, if we find out that the hostage's health summation is less than the one inside the hashmap, we change it with the new health summation, which is a better scenario, and then expand this node.


## Search Algorithms ##

### Breadth-First Search: ###

The Breadth-First Search (BFS) algorithm starts at the root node and explores all nodes at a certain level before moving on to the next level. The algorithm is guaranteed to find a solution given that it exists and the number of operators is finite as under this assumption (which in most use cases is valid), the algorithm will not get stuck. For our implementation, we used a priority queue that uses the depth of the node to decide the priority. The root node is inserted into the queue and poll operations are performed until the queue is empty or a node that satisfies the goal condition is reached. Shallower nodes (those closer to the root) have a higher priority and are therefore expanded first.


### Depth-First Search: ###

The Depth-First Search (DFS) algorithm starts at the root node and explores as deep as possible before backtracking to untraversed paths. In general This algorithm is not guaranteed to find a solution if the search tree is infinite, which is the case in many practical problems. However in our case it is complete and guaranteed to find a solution if exists and this is because we completely eliminante repeated states so the search tree is finite. We again employ a priority queue that uses the depth of the node to decide the priority in a way opposite to DFS. The root node is inserted into the queue and poll operations are performed until the queue is empty or a node that satisfies the goal condition is reached. Deeper nodes (those further from the root) have a higher priority and are therefore expanded first.


### Iterative Deepening Search: ###

The Iterative Deepening Search (IDS) is a modification to the Depth-First Search that aims to solve the problem that DFS is not guaranteed to find a solution while maintaining its low space complexity. IDS works by iteratively performing DFS up to a limited maximum depth and increasing this max after each iteration. Implementation-wise, IDS calls DFS inside a perpetual loop and passes a parameter specifying the stopping depth


### Uniform Cost Search: ###

Conceived by Edsger Dijkstra, Uniform Cost Search (UCS), also known as Dijkstra’s algorithm, is the fourth search algorithm we implemented, and unlike the previous three, it is an optimal one. Meaning that it finds the path with the lowest cost. UCS works by assigning a “path cost” to each node which is the summation of the cost visiting the node and the cost of visiting the nodes prior to it (In other words, the cost of the node + the path cost of the previous node). After calculating the path cost for a node, it’s inserted into the priority queue and the node with the lowest path cost is polled first.


### Greedy Search: ###

The fifth algorithm we implemented is Greedy Search. This algorithm utilizes a so-called “heuristic function” which tries to estimate the cost from a given node to the nearest goal. Such an estimation can not be exact without exploring the entire search space which would defeat the purpose of the search. Instead, simplifying assumptions are applied to find appropriate estimates that satisfy certain conditions (discussed later). The result of the heuristic function is used for the priority queue in the case of Greedy Search.


### A* Search: ###

The final algorithm we implemented is A* Search. It is the second optimal algorithm out of the six. A* also takes advantage of the heuristic function utilized by Greedy Search, however, what make A* optimal is that it adds the path cost to the result of the heuristic function and the result is used by the priority queue. A* Search can be shown to expand a number of nodes less than or equal to any optimal algorithm


## Heuristic Functions ##
A heuristic function H is some function that given a node, it returns an estimate of the cost still required to reach to the nearest goal from this node.
It also should have the centering property which is H(goalNode) = 0

The heuristic functions are core components for Greedy and A* search algorithms, additionally for the A* algorithm the heuristic function should have the property of being admissible, which is never over-estimating the cost still required from any node to reach the nearest goal.

A good strategy for coming up with a heuristic function is to think of a more unrestricted version of your problem and try to estimate the cost to reach a goal based on this relaxed version

In our project we introduce two different ideas for implementing a heuristic function for this search problem

### Heuristic function 1 ###
The core assumption we made to have a more relaxed version of the problem is that Neo require only two actions for each unprocessed hostage in our state
(An uprocessed hostage is a living hostage not yet carried by Neo or a dead mutated hostage not yet killed by Neo)
Note: The mutated hostages are treated differently to ensure admissibility (explained in the demonstration of admissibility below)
Moreover, if Neo is at neither the telephone booth nor at a cell with a pill, we count the number of hostages that will die immediately after the next time step (those of damage 98 or 99).
Let the number of unprocessed hostages = x, and the number of dying hostages = y
Therefore, the output of the heuristic function for this node will be 2 * x * costForOneAction  +  y * costForHostageDied

First it is clear that the function has the centering property, because in a goalNode, there will be 0 unprocessed hostages and Neo will be at the telephone booth

Secondly, showing that this heuristic is admissible can be achieved by noticing the follows:
* For every not yet processed hostage, Neo cannot have less than two actions to process this hostage
(either to carry it and deliver to TB or kill it if mutated(additionally to ensure admissibility we divide the number of mutated hostages by 6 because Neo can in some cases kill 3 hostages in one kill action)).
* If Neo is not at the TB nor at a cell with a pill, then he cannot by any means save the life of the hostages dying in the next time step.


### Heuristic function 2 ###
In this heuristic our relaxed version of the problem is based on the assumption that Neo can use the pads freely (i.e. all the pads are connected and Neo can go from any pad to any other pad without any restrictions)
Using this assumprtion we can calculate the minimum distance between two points in the grid (P1,P2), such that the minimum distance will be the minimum between:
* The direct Manhattan distance from P1 to P2
* The Manhattan distance from P1 to the nearest pad to P1 plus the Manhattan distance from P2 to the nearest pad to P2

Now to calculate the heuristic value we do the follwing:
For each one of the unprocessed hostages we calculate the minimum distance between Neo and this hostage plus the minimum distance between this hostage and the booth
(This will be the shortest path for Neo to go to the telephone booth and passing by this hostage)
And among all of these distances we pick the maximum one, Let's call it longestMinDistance   -----------> 1)

We also have a second part in our heuristic where we try to estimate the count of the hostages that will die for sure, which we achieved by checking for each alive and not yet carried hostage,
if the actions count remained before this hostage dies is less than both the min distance to deliver it to the TB and the min distance between Neo and the nearest pill,
then this hostage will die for sure.
Note that we can claculate the actions count for a hostage remained before dying because each action Neo does increases the hostage damage by 2, therefore actionsRemained = (100-hostageHealth+1)/2
And also we can calculate either the minimum distance to deliver it or the minimum distance from Neo to the nearest pill using the free pads assumptions and the approach for calculating the minimum distance between two points explained above.

therefore we obtain longestMinDistance from part one, and let's call the number of dying hostages y

The heuristic output will be longestMinDistance  +  y * costForHostageDied

The function has the centering property as well, because in a goalNode, there will be 0 non-processed hostages (all the hostages are either deliverd or killed)

The heuristic is also admissible:
* longestMinDistance is the minimum distance required (using pads freely) to pass by the farthest unprocessed hostage then to the telephone booth and for sure Neo has to pass by this hostage and go to the booth in order to reach a goal node
* If the count of action remains before this hostage dies less than both the min distance to deliver it to the TB and the min distance between Neo and the nearest pill,
  then Neo has no way to save the life for this hostage and it will die for sure.



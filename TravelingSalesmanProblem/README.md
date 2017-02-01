#TravelingSalesmanProblem
Since hearing about the Traveling Salesman Problem, I have put in a number of attempts at solving it. In this one I tried a heuristic approach of iterating through the points, measuring the widest angles between sets of three points, sorting based off of those angles (from widest to narrowest), and then iterating through that angle list and forming the connections that make up those angles so long as each of the points in the would-be connections have less than two connections already formed with them.

I only tested this heurism with a very limited amount of points, so that I could also run a brute-force algorithm, and compare my heurism's result with the best possible solution.

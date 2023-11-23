# Lahmacun Entrepreneur (A HashMap Project)

In this project, we have a "lahmacun" (a Turkish food) company. We want to track different branches of our company and employees in those branches. I used a hash table to store branches and employees. I implemented my own HashMap class. I used separate chaining to handle collisions.

## Operations

- Add: Add a new employee to the given branch at the given position.
- Leave: Remove the given employee from the given branch if possible. If it's not possible give a bonus to the employee.
- Performance Update: Process the monthly score of the given employee, there may be promotions and bonuses based on this score.
- Print Monthly Bonuses: Prints total amount of bonus given that month at the specified branch.
- Print Overall Bonuses: Prints total amount of bonus that have given from the start of input file until the end in the specified branch.
- Print Manager: Prints the manager of the specified branch.

## Performance

All operations execute in O(1) complexity, because I used a hash table to store branches and employees. For printing bonuses, I used a variable to store the total bonus amount. So, printing bonuses also executes in O(1) complexity.

I didn't upload the large test cases because they exceeded the 100mb limit of Github. But they execute fairly quick. (around 1 to 3 seconds for extremely large inputs like 1 million lines of input)

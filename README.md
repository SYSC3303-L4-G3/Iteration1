# SYSC 3303 PROJECT

Date: Feb 5th 2022.  
**Version 1.0**

Team Members
- Han Jiatong
- Iyamu Ese
- Man Qiushuo
- Zhang Boshen
- Zhu Ziheng

# ITERATION 1 
For this project we have the following java classes:
- Elevator.java     
 This class represents the Elevator Subsystem which emulates an elevator car. It sends
 calls out to the scheduler when there is a request from the elevator and receives instructions
 from the scheduler when theres a message from the floor.

- Floor.java      
 This classs represents the Floor Subsystem which emulates a floor in a building.
 The floor Subsystem exchanges messages with the scheduler.

- Main.java     
  This class contains the main method used to run the code.
 
- Scheduler.java      
  This class recieives instructions form the floor and elevator class. It is used to schedule the elevator 
  cars and the order they respond to requests.
  
- RequestMessage     
  This class serves as a buffer between the floor, elevator and scheduler. It sends the messages recived 
  from the floor and elevator to the scheduler. 
  
- ArrivalMessage     
  This class is responsible for outputing the arrival of an elevator

### Breakdown of responibilites:
- Han Jiatong:- Elevator.java, UML diagram, test case
- Zhang Boshen:- Floor.java, Scheduler.java, coding fixed, UML sequence diagram
- Qiushuo Man:- Scheduler.java, data_structure
- Iyamu Ese:- README, UML Class diagram

### Set up and test instructions:
 To run project:
- Import project from git on eclipese
- Run main()

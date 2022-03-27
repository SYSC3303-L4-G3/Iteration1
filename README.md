# SYSC 3303 PROJECT

Date: Mar 27th 2022.  
**Version 4.0**

## - RequestMsg
This class is used to express the format of massage communicated between each class. In this case, we use four numbers to represent where original floor from, elevator id, elevator movements and destination.

## - Floor.java 
Floor will receive the user input from input.txt, it will use UTP methods and send packet to scheduler.

### For the elevator systems, there are 4 different states. The default state is called idle state and whenever the elevator gets requests from scheduler, it will turn to Closing state meaning doors closed and prepare to move which is called MoveEle state. When the elevator reach the destination floor, it will become to Opening state and then change to idle to wait for the next request.

## - Elevator.java 
This class represents the Elevator Subsystem which emulates an elevator car. It sends calls out to the scheduler when there is a request from the elevator and receives instructions from the scheduler when there is a message/event from the floor. The setCurrentState method is used to declare the current state of elevator. In this iteration, we contribute that each state will maintain 1 second and then switch to the next state, the timerStart method is built for this. In addition, receiveMsg is the method is used to get requests from scheduler and report after arrival using report method.

## - idle.java
This class make the elevator keep opening and it is the default state of the elevator.

## - Closing.java
This class handles the closing operation and closing state of the elevator of the elevator doors.

## - MoveEle.java
This class move the elevator and MoveEle state of the elevator.

## - Opening.java 
This class handles the opening operation and opening state of the elevator of the elevator doors.

## - Scheduler.java
This class receives instructions form the floor and elevator class. It is used to schedule the elevator cars and the order they respond to requests. In Scheduler, there are two lists which are used to put requests that to go up or down from floor and elevators. handleRequest method will implement this step and then addElevator should adjust the elevator systems. After arrival, method called arrival will send massage to floor so that one program routine finished.

This time the scheduler has two states Waiting and Instructing, it will automatically changed if last state accomplished.

## - ArrivalMessage.java
This class is responsible for outputing the arrival of an elevator. It returns a boolean variable to represent if elevator has arrived or not.

## - Main.java
This class contains the main method used to run the code.

### Breakdown of responibilites:
- Han Jiatong:- Elevator.java, Elevator system, test case
- Zhang Boshen:- Floor.java, Scheduler.java, Elevator.java, UML sequence diagram, README.md, javadoc
- Iyamu Ese:- UML Class diagram, Javadoc
- Ziheng Zhu: State diagram

Team Members
- Han Jiatong
- Iyamu Ese
- Zhang Boshen
- Zhu Ziheng

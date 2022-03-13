/**
 * 
 * A.java
 * Interface class that has the 5 states of the elevator.
 * 
 */
public interface ElevatorState {
    void openDoor();
    void closeDoor();
    void timeIsUp();
    void receiveRequest();

    void reachFloor(int floor);

}

import javax.swing.*;

import static org.junit.Assert.*;

public class ElevatorTest {
    private Elevator elevator=new Elevator();

    @org.junit.Before
    public void setUp() throws Exception {
        Thread elevatorThread=new Thread(elevator);
        elevatorThread.start();
    }



    /*@org.junit.Test
    public void closeDoor() throws InterruptedException {
        elevator.receiveRequest();
        Thread.sleep(1000);
        assertEquals(elevator.getCurrentDirection(),Elevator.);

    }
     */
}

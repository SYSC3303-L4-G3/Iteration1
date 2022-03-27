package data_structure;

/**
 * Request message format sent from Floor and Elevator
 *
 * @author Boshen Zhang
 */
import java.time.LocalTime;

public class RequestMsg {
	private LocalTime time;
    private int startFloor;
    private String direction; // -1 if moving down, 1 if moving up, 0 if stay still
    private int destination;

    public RequestMsg(LocalTime time, int startFloor, String direction, int destination){
        this.time = time;
        this.startFloor = startFloor;
        this.direction = direction;
        this.destination = destination;
    }

    public void setFrom(LocalTime time) {
        this.time = time;
    }
    
    public void setstartFloor(int startFloor) {
		this.startFloor = startFloor;
	}

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public LocalTime getTime() {
		return this.time;
	}
    
    public int getstartFloor() {
		return this.startFloor;
	}
    public String getDirection(){return this.direction;}
    
    public int getDestination() { return destination; }
    
    @Override
	public String toString() {
		return this.time + " " + this.startFloor + " " + this.direction + " " + this.destination;
	}

}

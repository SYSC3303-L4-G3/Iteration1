 /* @author  Boshen Zhang
 * @version 4.0
 * 
 * Class Scheduler emulates/handles the scheduling of 
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
import java.net.*;
import data_structure.*;

public class Scheduler implements Runnable {
    ArrayList<Integer> destiUp = new ArrayList<>();  // list of destinations above the elevator's current position.
    ArrayList<Integer> destiDown = new ArrayList<>(); // list of destinations below the elevator's current position.
    private Elevator elevator;
    private LinkedList<RequestMsg> request =new LinkedList<>();
    private byte req[] = new byte[40];

    DatagramPacket sendPacket, receivePacket, instructionPacket;
    DatagramSocket sendReceiveSocket, receiveSocket, sendSocket;
    DatagramSocket eSendSocket, eReceiveSocket;
    
    String ELEVATOR_IP_ADDRESS = "";
    
    /**
     * Constructor for class Scheduler
     */
    public Scheduler() {
    	try {
            //sendReceiveSocket = new DatagramSocket();
            eSendSocket = new DatagramSocket();
            eReceiveSocket = new DatagramSocket(69);
            receiveSocket = new DatagramSocket(23);
            sendSocket = new DatagramSocket();
        } catch (SocketException se) { 
            se.printStackTrace();
            System.exit(1);
        }
    }
    
    //Not used for iteration 4
//    public synchronized void handleRequest(RequestMsg msg) {
//
//        while (!request.isEmpty()) {
//            try {
//                wait(); // wait when there is no destinations to go
//            } catch (InterruptedException e) {
//                return;
//            }
//        }
//
//        if(msg != null){
//            destiUp.add(msg.getDestination());
//        }
//        else if(msg.getMovement() == -1){
//            destiDown.add(msg.getDestination());
//        }
//        else;
//        request.add(msg);
//
//        notifyAll();
//    }
    
    /**
     * addElevator adds elevator to requests
     * @param elevator
     */
    public void addElevator(Elevator elevator) {
        this.elevator=elevator;
    }
    
    /**
     * getRequest gets request from the floors and elevators
     */
    
    public synchronized void getRequest(){ //not used in iteration 3
        while(request.isEmpty()){
            try{
                wait();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        RequestMsg a=request.pop();
        System.out.println(a);
        elevator.receiveMsg(a);

        notifyAll();
    }
    
    /**
     * arrival
     */
    public synchronized boolean arrival(int upOrDown,ArrivalMessage arrMsg){
        if(arrMsg.isArrived()){
            if(upOrDown==-1){destiDown.remove(arrMsg.getFloor());}
            else if(upOrDown==1){destiUp.remove(arrMsg.getFloor());}
            return true;
        }
        return false;
    }
    
    /**
     *MachineState handles the states of the scheduler
     */
    public enum MachineState {
        Waiting {
            @Override
            public MachineState nextState() {
                return Instructing;
            }
            
            @Override
            public String currentState() {
            	return "Waiting";
            }
        },
        Instructing {
            @Override
            public MachineState nextState() {
                return Waiting;
            }
            
            @Override
            public String currentState() {
            	return "Instructing";
            }
        };
        public abstract MachineState nextState();
        public abstract String currentState();
    }
    
    /**
     * receiveFromFloor receives request from floor
     */
    public void receiveFromFloor() {
    	
    	if(destiUp.isEmpty()&&destiDown.isEmpty()) {
			byte msg[] = new byte[40];
		}
    	
    	receivePacket = waitPacket(receiveSocket, "Scheduler");
    	byte[] msg = receivePacket.getData();
    	req = msg;
    	System.out.println("Scheduler receved floor request");
    	if (msg[0] > msg[3]) {
    		destiDown.add((int) msg[3]); 
    	}
    	
    	if(msg[0] < msg[3]) {
    		destiUp.add((int) msg[3]);		
    	}
    	
    	//DatagramPacket reply = waitPacket(receiveSocket, "Scheduler");
		Floor.sendPacket(receivePacket.getData(), receivePacket.getLength(), receivePacket.getAddress(), 
				receivePacket.getPort(), sendSocket);
       
    }
    /**
     * Wait the packet from floor
     * @param s
     * @param source
     * @return
     */
    public DatagramPacket waitPacket(DatagramSocket s, String source){
		
		byte data[] = new byte[40];
		
		DatagramPacket receivedPacket = new DatagramPacket(data, data.length);
		System.out.println(source + " is waiting");
		
		try{
			System.out.println("waiting...");
			s.receive(receivedPacket);
		}catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("From host: " + receivedPacket.getAddress());
		System.out.println("Destination host port: " + receivedPacket.getPort());
		return receivedPacket;
	}
    
    
    
    @Override
	public void run(){
		MachineState state = MachineState.Waiting;
		while (true) {
			while (state.currentState().equals("Waiting")) {
				
				receiveFromFloor();
				System.out.println("elevator goes" + req[3]);
				state = state.nextState();
			}
				
			while (state.currentState().equals("Instructing")) {
				DatagramPacket receivePacket = Floor.waitPacket(receiveSocket, "Scheduler");
				
				Floor.sendPacket(receivePacket.getData(), receivePacket.getLength(), 
						receivePacket.getAddress(), 2, sendSocket);
				System.out.println("elevator" + req[1] + "should move");
				state = state.nextState();
			}
		}
	}
    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
    	Thread schedThread;

    	schedThread = new Thread(new Scheduler(),"The scheduler");

        schedThread.start();
    }
    
}
  

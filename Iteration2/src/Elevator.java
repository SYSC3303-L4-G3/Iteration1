/**This class is a representation of an elevator
 * @author Jiatong Han
 * @anthor Boshen Zhang
 */
import data_structure.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

public class Elevator implements Runnable {
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final int STILL = 0;
    private int id;
    private int currentHeight= STILL;
    private Scheduler scheduler;
    private int currentDirection;
    private boolean workToBeDone = false;
    private Timer timer;
    private TimerTask ring;

    private PriorityQueue<Integer> upQueue=new PriorityQueue();
    private PriorityQueue<Integer> downQueue=new PriorityQueue<>(Comparator.reverseOrder());

    ElevatorState idle;
    ElevatorState opening;
    ElevatorState moveEle;
    ElevatorState closing;
    ElevatorState reachFloor;

    ElevatorState currentState;

    private static DatagramPacket receivePacket;
    private static DatagramSocket sendSocket, receiveSocket;
    private final int Id = 2;
    private byte request[] = new byte[40];


    /**
     * Constructor for Elevator class
     * @param id
     */
//    public Elevator(int id) {
//        idle=new Idle(this);
//        closing=new Closing(this);
//        opening =new Opening(this);
//        moveEle=new MoveEle(this);
//        reachFloor=new ReachFloor(this);
//
//        currentState =idle;
//        this.id=id;
//    }

//    public Elevator(Scheduler scheduler) {
//        idle=new Idle(this);
//        closing=new Closing(this);
//        opening =new Opening(this);
//        moveEle=new MoveEle(this);
//        reachFloor=new ReachFloor(this);
//
//        currentState =idle;
//
//        this.scheduler = scheduler;
//        scheduler.addElevator(this);
//    }
//  
    /**
     * Overload constructor for Elevator class
     */
    public Elevator(){
        try{
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(2);
        }catch (SocketException se){
            se.printStackTrace();
            System.exit(1);
        }
        idle=new Idle(this);
        closing=new Closing(this);
        opening =new Opening(this);
        moveEle=new MoveEle(this);
        reachFloor=new ReachFloor(this);

        currentState =idle;

    }

    private void receviveFromScheduler(){

        receivePacket = Floor.waitPacket(receiveSocket, "Elevator NO 2");

        byte[] data = receivePacket.getData();
        request = data;

        System.out.println("Elevator sending");
        System.out.println("Elevator No 2 send user to floor" + request[3]);
        sendPacket(data, data.length, receivePacket.getAddress(), 23, sendSocket);
        System.out.println("Elevator has sent");
    }

    public static void sendPacket(byte[]array, int len, InetAddress destadderss, int port, DatagramSocket socket){

        DatagramPacket packet = new DatagramPacket(array, len, destadderss, port);

        System.out.println("The Elevator is sending arrival:");
        System.out.println("From host: " + packet.getAddress());
        System.out.println("Destination host port: " + packet.getPort());

        try{
            socket.send(packet);
        } catch (IOException ie){
            ie.printStackTrace();
            System.exit(1);
        }

        System.out.println("Elevator: sent to scheduler");
    }
    
    /**
     * setCurrentState 
     * @param newElevatorState
     */
    public void setCurrentState(ElevatorState newElevatorState){
        currentState =newElevatorState;
    }
    
    /**
     * timerStart 
     */
    public void timerStart(int delay) {
        timer=new Timer();
        ring =new TimerTask() {
            @Override
            public void run() {
                currentState.timeIsUp();
            }
        };
        timer.schedule(ring,delay);
    }

    /**
     * openDoor handles the opening of elevator doors
     */
    public void openDoor(){
        currentState.openDoor();
    }
    
    /**
     * closeDoor
     */
    public void closeDoor(){
        currentState.closeDoor();
    }
    
    /**
     * receiveRequest 
     */
    public void receiveRequest(RequestMsg requestMsg){
        String direction=requestMsg.getDirection();
        int from=requestMsg.getstartFloor();
        int destination=requestMsg.getDestination();
        if(currentDirection==UP){
            if(direction==UP){
                upQueue.add(from);
                upQueue.add(destination);
            }else if(direction==DOWN){
                upQueue.add(from);
                downQueue.add(destination);
            }else{
                System.out.println("ERROR");
            }
        }else if(currentDirection==DOWN){
            if(direction==DOWN){
                downQueue.add(from);
                downQueue.add(destination);
            }else if(direction==UP){
                downQueue.add(from);
                upQueue.add(destination);
            }else{
                System.out.println("ERROR");
            }
        }else{//elevator idle
            if(from>(getCurrentFloor())){
                currentDirection=UP;
                receiveRequest(requestMsg);
            }else if(from < (currentDirection/10)){
                currentDirection=DOWN;
                receiveRequest(requestMsg);
            }else{
                if(direction==UP){
                    upQueue.add(destination);
                }else if(direction==DOWN){
                    downQueue.add(destination);
                }else{
                    System.out.println("ERROR");
                }
            }
        }
        System.out.println("Element in upQueue");
        System.out.println(Arrays.toString(upQueue.toArray()));
        System.out.println("Element in downQueue");
        System.out.println(Arrays.toString(downQueue.toArray()));

        currentState.receiveRequest();
    }
    
    /**
     * reachFloor indicates when the elevator has reached a floor
     * @param floor
     */
    public void reachFloor(int floor){
        currentState.reachFloor(floor);
    }
    
    /**
     * getCurrentState returns the current state of the elevator
     * @return currentState
     */
    public ElevatorState getCurrentState() {
        return currentState;
    }
    
    /**
     * getClosing returns closing state
     * @return closing
     */
    public ElevatorState getClosing() {
        return closing;
    }
    /**
     * getOpenig returns opening state
     * @return opening
     */
    public ElevatorState getOpening() {
        return opening;
    }
    
    /**
     * getEleMove returns moveEle state
     * @return moveEle
     */
    public ElevatorState getEleMove(){
        return moveEle;
    }
    
    /**
     * getIdle returns idle state
     * @return idle
     */
    public ElevatorState getIdle() {
        return idle;
    }

    public Timer getTimer() {
        return timer;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    /**
     * receiveMsg receives message from the scheduler
     * @param requestMsg
     */
    public void receiveMsg(RequestMsg requestMsg) {
        System.out.println("Get message from scheduler");
        System.out.println(requestMsg.getFrom());
        System.out.println(requestMsg.getMovement());
        System.out.println(requestMsg.getDestination());
        report(new RequestMsg(2, -1, 3), new ArrivalMessage(3, true));

    }

    /**
     * report reports to the scheduler 
     * @param requestMsg
     * @param arrivalMessage
     */
    private void report(RequestMsg requestMsg, ArrivalMessage arrivalMessage) {
        if (scheduler.arrival(requestMsg.getMovement(), arrivalMessage)) {
            System.out.println("Elevator has arrived floor No." + arrivalMessage.getFloor());
        }
        System.out.println("Report to scheduler");
        System.out.println("Finish request");

    }


    public void increaseHeight(){
        currentHeight++;
        if(currentHeight%10==0){
            reachFloor(getCurrentFloor());
        }else{
            timerStart(200);
        }
    }

    public void decreaseHeight(){
        currentHeight--;
        if(currentHeight%10==0){
            reachFloor(getCurrentFloor());
        }else{
            timerStart(200);
        }
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    /**
    *convert height to floor
     */
    public int getCurrentFloor(){
        return currentHeight/10;
    }


    public void isDestination(){
        System.out.println("Current direction is ");
        System.out.println(getCurrentDirection());
        System.out.println("Current floor is ");
        System.out.println(getCurrentHeight()/10);

        if(getCurrentDirection()==UP){
            if (getCurrentFloor()==upQueue.peek()){
                upQueue.poll();
                //up Queue is empty
                if(upQueue.isEmpty()){
                    System.out.println("Up queue is empty");
                    //down queue is empty
                    if(downQueue.isEmpty()){
                        currentDirection=STILL;
                        reportStatus();
                        currentState=opening;
                        timerStart(1000);
                    }else{
                        System.out.println("Down queue is not empty");
                        currentDirection=DOWN;
                        reportStatus();
                        currentState=opening;
                        timerStart(1000);
                    }
                }

            }else{
                timerStart(200);
            }
        }else if(getCurrentDirection()==DOWN){
            if (getCurrentFloor()==downQueue.peek()){
                downQueue.poll();
                //down Queue is empty
                if(downQueue.isEmpty()){
                    //up queue is empty
                    if(upQueue.isEmpty()){
                        currentDirection=STILL;
                        reportStatus();
                        currentState=opening;
                        timerStart(1000);
                    }else{
                        currentDirection=UP;
                        reportStatus();
                        currentState=opening;
                        timerStart(1000);
                    }
                }

            }else{
                timerStart(200);
            }
        }


    }

    public void reportStatus(){

    }

    /**
     * 
     */
    @Override
    public void run() {

        while (true) {

        	receviveFromScheduler();
        }
    }
    
    /**
     * main method 
     */
    public static void main(String[] args) {
    	Thread elevThread;

    	elevThread = new Thread(new Elevator(),"The elevator");

    	elevThread.start();
    }
}

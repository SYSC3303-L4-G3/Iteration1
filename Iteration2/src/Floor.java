 /* @author : Boshen Zhang
 * @version 4.0
 * Class Floor represents a floor in a building
 */
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.*;
import data_structure.*;

public class Floor implements Runnable {

    private Scheduler scheduler;
    private RequestMsg requestMsg;
    private ArrivalMessage arrivalMessage;
    private ArrayList<String> info = new ArrayList<String>();
    private long startTime;
    
    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendReceiveSocket, receiveSocket;
    
    String SCHEDULER_IP_ADDRESS = "";

    /**
    * Constructor for floor
    * @param scheduler
    */
    public Floor() {
      
        //this.requestMsg = requestMsg;
        //this.arrivalMessage = arrivalMessage;
    	
    	startTime = System.currentTimeMillis();
    	
        try {
			sendReceiveSocket = new DatagramSocket();
			
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
    }

    /**
     * Read the input from input.txt
     * @throws FileNotFoundException
     */
    public void readFile() throws FileNotFoundException {
		
		File file = new File("./input.txt");
		Scanner scan = new Scanner(file);

		while (scan.hasNextLine()) {

			String line = scan.nextLine();
//			System.out.println(line);
			String[] input = line.split(" ");
			
			if (input.length == 4) {
				
				LocalTime time = LocalTime.parse(input[0]);
				int floorNumber = Integer.parseInt(input[1]);
				String direction = input[2];
				direction = direction.toUpperCase(); 
				int dNumber = Integer.parseInt(input[3]);
					
				RequestMsg r = new RequestMsg(time, floorNumber, direction, dNumber);
				
				String convertedString = String.valueOf(r);//convert Request to type string	
				info.add(convertedString);//add String to array list AL
			
			} else {
				System.out.print("Inputs' format incorrect, cannot process request. \n");
				continue;
			}
		}
		scan.close();
		System.out.println("Users input would be:");
		System.out.println(info);	
	}
    
    /**
	 * Change the input time to milliseconds
	 * @param requestTime
	 * @return request time
	 */
	public long changeTime(String requestTime) {
		LocalTime localTime = LocalTime.parse(requestTime);
		return localTime.toSecondOfDay() * 1000;
	}
	
	/**
	 * Determine the time difference between each input
	 * @return
	 */
	public long timeSpace() {
		return System.currentTimeMillis() - startTime;
	}
    
	/**
     * readEvent receives request from scheduler
     * @param requestMsg
     * @return
     */
    public static byte[] readEvent(ArrayList<String> info){
    	
    	String s = info.get(0);
    	byte data[] = s.getBytes();
    	
        System.out.println("Get message from user convert into bytes");
        info.remove(0);
        return data;
    }

    /**
    * floorSend sends requests from the floor to scheduler
    * Not used this time
    * @param requestMsg
//    */
//    public void floorSend(RequestMsg requestMsg) {
//        scheduler.handleRequest(requestMsg);   	
//        System.out.println("Report to scheduler");
//    }
    
    /**
     * sendAndReceive sends and receives messages from scheduler via UDP
     * @param data
     * @param port
     */
    public void sendAndReceive(byte[] data, int port){		
		try {
			sendPacket(data, data.length, InetAddress.getLocalHost(), 23, sendReceiveSocket);
		}
		catch (UnknownHostException he){
			he.printStackTrace();
			System.exit(1);
		}

	}
    
    /**
     * sendPacket sends messages to scheduler via UDP
     * @param array
     * @param len
     * @param destadderss
     * @param port
     * @param socket
     */
    public static void sendPacket(byte[]array, int len, InetAddress destadderss, int port, DatagramSocket socket){
		
		DatagramPacket packet = new DatagramPacket(array, len, destadderss, port);
		System.out.println("The Floor is sending a request:");
		System.out.println("From host: " + packet.getAddress());
		System.out.println("Destination host port: " + packet.getPort());

		try{
			socket.send(packet);
		} catch (IOException ie){
			ie.printStackTrace();
			System.exit(1);
		}
		System.out.println("floor reports to scheduler");
	    System.out.print("Containing request: ");
	    System.out.println(new String(packet.getData(),0,len)); 
	}
    
    /**
     * wait the Packet send to scheduler
     * @param s
     * @param source
     * @return
     */
    public static DatagramPacket waitPacket(DatagramSocket s, String source){
		
		byte data[] = new byte[40];
		
		DatagramPacket receivedPacket = new DatagramPacket(data, data.length);
		System.out.println(source + " is waiting response");
		
		try{
			System.out.println("waiting...");
			s.receive(receivedPacket);
		}catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println( source + " has received feedback from scheduler");
		System.out.println("From host: " + receivedPacket.getAddress());
		System.out.println("Destination host port: " + receivedPacket.getPort());
		return receivedPacket;
	}
    
    /**
     *FloorState handles the states of the elevator 
     *It is unnecessary but also working
     */
    public enum FloorState {
        Idle {
            @Override
            public FloorState nextState() {
                return Requesting;
            }

            @Override
            public String currentState() {
                return "Idle";
            }
        },
        Requesting {
            @Override
            public FloorState nextState() {
                return Waiting;
            }

            @Override
            public String currentState() {
                return "Requesting";
            }
        },
        Waiting {
            @Override
            public FloorState nextState() {
                return Waiting;
            }

            @Override
            public String currentState() {
                return "Waiting";
            }
        };
        public abstract FloorState nextState();
        public abstract String currentState();
    }
    
    
    @Override
    public void run(){
//   	FloorState state = FloorState.Idle;
        
    	while(true) {    		
//  		while(state.currentState().equals("Idle")) {
    			try {
    				this.readFile();
    			} catch (FileNotFoundException e2) {
    				e2.printStackTrace();
    			}
//				state = state.nextState();
// 		}  		
//   		while (state.currentState().equals("Requesting")) {
    			byte[] msg = readEvent(info);
    			sendAndReceive(msg , 23);
// 				state = state.nextState();
//  		}
//  		while (state.currentState().equals("Waiting")) {
    			waitPacket(sendReceiveSocket, "Floor");
// 				state = state.nextState();
    		}
//            arrivalMessage = new ArrivalMessage(3,true);
//            if(scheduler.arrival(requestMsg.getDirection(), arrivalMessage)) {
//                System.out.println("get there");
//            }
        }
    
    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
    	Thread floorThread;

        floorThread = new Thread(new Floor(),"The floor");

        floorThread.start();
    }
    

}

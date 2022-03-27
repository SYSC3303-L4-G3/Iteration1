// //import java.util.ArrayList;
//
//
//public class main {
//    public static void main(String[] args)
//    {
//        Thread schedThread, floorThread, elevThread;
//        Scheduler scheduler;
//
//        scheduler = new Scheduler();
//
//        schedThread = new Thread(new Server(scheduler),"The scheduler");
//        elevThread = new Thread(new Elevator(scheduler),"The elevator");
//        floorThread = new Thread(new Floor(scheduler),"The floor");
//
//        elevThread.start();
//        schedThread.start();
//        floorThread.start();
//
//    }
//}
//
//class Server implements Runnable{
//    private Scheduler scheduler;
//    private ArrayList<String> ingredientsSupply = new ArrayList<>();
//
//    public Server(Scheduler scheduler)
//    {
//        this.scheduler = scheduler;
//    }
//    public void run() {
//    	
//
//    }
//}

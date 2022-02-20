import java.util.ArrayList;

public class main {
    public static void main(String[] args)
    {
        Thread schedThread, floorThread, elevThread;
        Scheduler scheduler;

        scheduler = new Scheduler(); // shared by Agent and Chef

        schedThread = new Thread(new Server(scheduler),"The scheduler");
        floorThread = new Thread(new Elevator(scheduler),"The elevator");
        elevThread = new Thread(new Floor(scheduler),"The floor");

        schedThread.start();
        floorThread.start();
        elevThread.start();
    }
}

class Server implements Runnable{
    private Scheduler scheduler;
    private ArrayList<String> ingredientsSupply = new ArrayList<>();

    public Server(Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }
    public void run() {

    }
}
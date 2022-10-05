package CPUScheduler;


public class SchedulerMain {
    public static void main(String[] args){
        SimulationStream p = SimulationStream.getInstance();
        p.preProcessArgumentsAndStartSimulation(args);
    }
}

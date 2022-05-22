package CPUScheduler.SchedulerAlgorithms;

import CPUScheduler.Processor.ProcessObjects;

import java.util.List;

public class RoundRobin extends Scheduler{
    private int TimeQuantum;

    public RoundRobin(int processCount, List<Integer> preprocessPCBs, String TimeQuantum){
        super(processCount,preprocessPCBs);
        this.TimeQuantum = Integer.parseInt(TimeQuantum);
        PrintAlgorithmNameAndProcesses(this.getClass().getSimpleName(),this.TimeQuantum);
    }

    @Override
    public ProcessObjects selectNextProcess() {
        return null;
    }

    @Override
    public void Algorithm() {
        CheckSchedulerExitCondition();
        IntegratedInitialJobPerEachCircular();
        /// Logic ///

        /////////////
        IntegratedAfterJobPerEachCircular();
    }
}

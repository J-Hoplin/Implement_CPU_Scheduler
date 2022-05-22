package CPUScheduler.SchedulerAlgorithms;

import CPUScheduler.Configurations.FixedVariables;
import CPUScheduler.Processor.ProcessObjects;

import java.util.Arrays;
import java.util.List;

/*
* First Come First Served Algorithm
*
* */

public class FistComeFirstServed extends Scheduler{
    public FistComeFirstServed(int processCount,List<Integer> preprocessPCBs) {
        super(processCount,preprocessPCBs);
        PrintAlgorithmNameAndProcesses(this.getClass().getSimpleName());
        Algorithm();
    }

    // 다음프로세스 선택 알고리즘 작성
    @Override
    public ProcessObjects selectNextProcess() {
        // FCFS는 먼저온 프로세스가 들어가야 한다 그렇기 때문에 head부분에 있는 프로세스가 들어가게 된다
        return ReadyQueue.removeFirst();
    }

    // FCFS Algorithm
    @Override
    public void Algorithm() {
        while(true){
            System.out.println("[ Log at time : " + SchedulerTotalRunningTime + " ]");
            CheckSchedulerExitCondition();
            IntegratedInitialJobPerEachCircular();
            /// Logic ///
            //System.out.println(cpu.getProcess());
            //System.out.println(Arrays.toString(IOQueue.toArray()));
            //CPU안에서 돌아가는 프로세스가 있다면
            if(cpu.CPUhasProcess()){
                // 만약 CPU안에서 돌아가고 있는 프로세스의 cpu burst time이 0 이하가 되거나
                // CPU안에서 돌고있는 프로세스 cpu time이 0이하가 되는 경우에 대해서
                if(cpu.getProcess().getRemaining_cpu_burst() <= 0 || cpu.getProcess().getRemaining_cpu_time() <= 0){
                    // Ready Queue가 비어있지 않는 경우
                    ProcessObjects p = null;
                    if(!ReadyQueueEmpty()){
                        while(true){
                            if(!ReadyQueueEmpty()){
                                ProcessObjects nextProcess = selectNextProcess();
                                if(nextProcess.getRemaining_cpu_burst() <= 0){
                                    FixedVariables.ConsolePrintFileWriteParellel("Process " + nextProcess.getPid() + " selected but go to IOQueueChecker due to " + nextProcess.getRemaining_cpu_burst() + " burst time");
                                    EnqueToIOQueue(nextProcess);
                                }else{
                                    p = dispatcher.ContextSwitching(cpu,nextProcess);
                                    break;
                                }
                            }else{
                                p = dispatcher.ContextSwitching(cpu);
                                break;
                            }
                        }
                    }
                    // ReadyQueue가 비어있는 경우
                    else{
                        p = dispatcher.ContextSwitching(cpu);
                    }
                    EnqueToIOQueue(p);
                }
                // 아닌 경우에는 넘긴다.
                else{
                    ;
                }
            }
            //CPU안에서 돌아가는 프로세스가 없다면
            else{
                if(!ReadyQueueEmpty()){
                    cpu.setProcess(selectNextProcess());
                }
            }
            /////////////
            IntegratedAfterJobPerEachCircular();
        /*
        FixedVariables.BreakConsole()
         */
        }

    }
}

package CPUScheduler.SchedulerAlgorithms;

import CPUScheduler.Configurations.FixedVariables;
import CPUScheduler.Processor.ProcessObjects;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShortestJobFirst extends Scheduler{
    public ShortestJobFirst(int processCount,List<Integer> preprocessPCBs) {
        super(processCount,preprocessPCBs);
        PrintAlgorithmNameAndProcesses(this.getClass().getSimpleName());
        Algorithm();
    }

    @Override
    public ProcessObjects selectNextProcess() {
        //출력문은 정렬결과를 보이기 위함임
        /*
        System.out.println("==========================");
        System.out.println("Before Sorting");
        System.out.println(ReadyQueue);
         */
        Collections.sort(ReadyQueue, new Comparator<ProcessObjects>() {
            @Override
            public int compare(ProcessObjects o1, ProcessObjects o2) {
                if(o1.getRemaining_cpu_burst() == o2.getRemaining_cpu_burst()){
                    return 0;
                }else if(o1.getRemaining_cpu_burst() <o2.getRemaining_cpu_burst()){
                    return -1;
                }else{
                    return 1;
                }
            }
        });
        /*
        System.out.println("Result of Ready Queue Sort Based on CPU Burst");
        System.out.println(ReadyQueue);
        System.out.println("==========================");
         */
        return ReadyQueue.removeFirst();
    }

    // Non-Preemptive SJF Algorithm
    @Override
    public void Algorithm() {
        while (true){
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
                // 다음 프로세스를 뽑았을때 CPU Burst가 0인것에 대한 예외처리를 위한 코드
                if(!ReadyQueueEmpty()){
                    while(true){
                        // ReadyQueue가 비어있지 않은 경우이다.
                        if(!ReadyQueueEmpty()){
                            // ReadyQueue Head 프로세스를 뽑는다
                            ProcessObjects nextProcess = selectNextProcess();
                            // 해당 프로세스의 CPU Burst값이 0보다 작거나 같으면 IO Queue에 넣는 검사과정을 거친다.
                            if(nextProcess.getRemaining_cpu_burst() <= 0){
                                FixedVariables.ConsolePrintFileWriteParellel("Process " + nextProcess.getPid() + " selected but go to IOQueueChecker due to " + nextProcess.getRemaining_cpu_burst() + " burst time");
                                EnqueToIOQueue(nextProcess);
                                // 0보다 큰 경우에는 CPU의 프로세스로 세팅한다.
                            }else{
                                cpu.setProcess(nextProcess);
                                break;
                            }
                            // 검사를 하다가 Ready Queue가 비어있을 수 도 있다. 이런 경우에는 Break하고 다음 사이클로 넘어간다.
                        }else{
                            break;
                        }
                    }
                }
            }
            /////////////
            IntegratedAfterJobPerEachCircular();
            //FixedVariables.BreakConsole(600);
        }
    }
}

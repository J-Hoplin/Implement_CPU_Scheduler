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
                        // 다음 프로세스를 뽑았을 때 CPU Burst가 0인것에 대한 예외처리 코드
                        while(true){
                            // 레디큐가 비어있지 않은 경우
                            if(!ReadyQueueEmpty()){
                                // 레디큐에서 다음 프로세스를 꺼낸다
                                ProcessObjects nextProcess = selectNextProcess();
                                // 꺼낸 프로세스의 CPU Burst가 0이하인 경우
                                // IOQueue 삽입 검사 함수에 넘긴다.
                                if(nextProcess.getRemaining_cpu_burst() <= 0){
                                    FixedVariables.ConsolePrintFileWriteParellel("Process " + nextProcess.getPid() + " selected but go to IOQueueChecker due to " + nextProcess.getRemaining_cpu_burst() + " burst time");
                                    EnqueToIOQueue(nextProcess);
                                    // 꺼낸 프로세스의 CPU Burst가 0 초과인 경우
                                    // Context Swithching 진행
                                }else{
                                    p = dispatcher.ContextSwitching(cpu,nextProcess);
                                    break;
                                }
                                // 레디큐가 비어있는 경우
                                // CPU에서 작동이 끝난 해당 프로세스만 꺼낸다.
                            }else{
                                p = dispatcher.ContextSwitching(cpu);
                                break;
                            }
                        }
                    }
                    // ReadyQueue가 비어있는 경우
                    // CPU에서 작동이 끝난 해당 프로세스만 꺼낸다.
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
        /*
        FixedVariables.BreakConsole()
         */
        }

    }
}

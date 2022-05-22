package CPUScheduler.SchedulerAlgorithms;

import CPUScheduler.Configurations.FixedVariables;
import CPUScheduler.Processor.ProcessObjects;

import java.sql.Time;
import java.util.List;

public class RoundRobin extends Scheduler{
    //  원래 Time Quantum값을 저장, 연산에 활용되는 TimeQuantum의 값이 0이하가 될때 다시 초기화할 때 사용된다.
    private int originalTimeQuantumSaved;
    // 연산에 활용될 Time Quantum값
    private int TimeQuantum;

    public RoundRobin(int processCount, List<Integer> preprocessPCBs, String TimeQuantum){
        super(processCount,preprocessPCBs);
        this.originalTimeQuantumSaved = this.TimeQuantum = Integer.parseInt(TimeQuantum);
        PrintAlgorithmNameAndProcesses(this.getClass().getSimpleName(),this.TimeQuantum);
        Algorithm();
    }

    private void oneSecondPastTimeQuantum(){
        // Time Quantum 1초 지난것에 대해
        this.TimeQuantum--;
    }

    private void resetTimeQuantum(){
        System.out.println("Time Quatum Reset!");
        // Time Quantum 초기화하는 함수
        this.TimeQuantum = this.originalTimeQuantumSaved;
    }

    private void resetTimeQuantum(String pid){
        // Time Quantum 초기화하는 함수
        this.TimeQuantum = this.originalTimeQuantumSaved;
    }

    @Override
    public ProcessObjects selectNextProcess() {
        return ReadyQueue.removeFirst();
    }

    @Override
    public void Algorithm() {
        while(true){
            System.out.println("[ Log at time : " + SchedulerTotalRunningTime + " ]");
            CheckSchedulerExitCondition();
            IntegratedInitialJobPerEachCircular();
            /// Logic ///
            // 기존 로직에 TimeQuantum이 0이 됐을때에 대해서를 추가한다.
            if(cpu.CPUhasProcess()){
                if(cpu.getProcess().getRemaining_cpu_burst() <= 0 || cpu.getProcess().getRemaining_cpu_time() <= 0 || this.TimeQuantum <= 0){
                    ProcessObjects p = null;
                    if(!ReadyQueueEmpty()){
                        while (true){
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
                    else{
                        // ReadyQueue가 비어있는 경우
                        // CPU에서 작동이 끝난 해당 프로세스만 꺼낸다
                        // 꺼낸 후에는 TimeQuantum을 우선적으로 초기화해준다.
                        p = dispatcher.ContextSwitching(cpu);
                    }
                    // 기존에 Running하던 CPU만 나오던가
                    // ContextSwitching이 일어났다거나
                    // 이 경우들에 대해서는 모두 Time Quantum을 Reset해주어야한다.
                    resetTimeQuantum();
                    EnqueToIOQueue(p);
                }
                //위 조건이 아니라면 그냥 진행
                else{
                    ;
                }
            }

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
                // CPU안에 프로세스가 없더라도 새로운 프로세스가 들어왔을떄 Time Quantum은 초기화된 상태이다.
                // 그렇기에 추가적인 TimeQuantum Reset작업은 불필요하다.ㄴ
            }
            /////////////
            IntegratedAfterJobPerEachCircular();
            // TimeQuantum 1초는 CPU가 있을때에만 해당한다.
            // 그렇기 때문에 CPU에 프로세스가 있는지 확인하고 1초를 뺀다.
            if(cpu.CPUhasProcess()){
                oneSecondPastTimeQuantum();
            }
        }
    }
}

package CPUScheduler.SchedulerAlgorithms;

import CPUScheduler.CPU.CPU;
import CPUScheduler.Configurations.FixedVariables;
import CPUScheduler.Dispatcher.Dispatcher;
import CPUScheduler.Processor.ProcessObjects;

import java.util.*;
import java.util.function.Predicate;

public abstract class Scheduler{
    //스케줄러 이름
    private String SchedulerAlgorithmName;

    // 수행이 완료된 프로세스 저장
    protected LinkedList<ProcessObjects> finishedQueue =new LinkedList<>();
    // Genereated Processor List Stack
    // Set access modifier protected, Inherited class should be access this variable
    protected LinkedList<ProcessObjects> ProcessStack = new LinkedList<>();
    // Process Ready Queue : Ready state
    protected LinkedList<ProcessObjects> ReadyQueue = new LinkedList<>();
    // IO Stated Queue : Blocking state
    protected LinkedList<ProcessObjects> IOQueue = new LinkedList<>();

    // 스케줄러 전체 작동 시간을 의미한다.
    protected int SchedulerTotalRunningTime = 0;
    //Total Process Count
    private final int processCount;
    // CPU Instance
    protected CPU cpu = CPU.getInstance();
    // Dispatcher Instance
    protected Dispatcher dispatcher = Dispatcher.getInstance();

    //CPU IDLE Time
    private int CPUIDLETime = 0;
    // I/O IDLE Time
    private int IOIDLETime = 0;



    // Initiate Processes for this Scheduler
    private void initiateProcess(List<Integer> preprocessPCBs){
        int pid_counter = 1;
        for(int i = 0; i < preprocessPCBs.size();i += 4){
            /*
            System.out.println(Integer.toString(preprocessPCBs.get(i))+
                    Integer.toString(preprocessPCBs.get(i+1))
                    +Integer.toString(preprocessPCBs.get(i+2))
                    +Integer.toString(preprocessPCBs.get(i+3)));
             */
            ProcessStack.add(new ProcessObjects(
                    pid_counter
                    ,preprocessPCBs.get(i)
                    ,preprocessPCBs.get(i+1)
                    ,preprocessPCBs.get(i+2)
                    ,preprocessPCBs.get(i+3)));
            pid_counter++;
        }
    }

    // Arrival Time을 기준으로 프로세스들을 정렬한다. ProcessObject의 compareTo를 구현해 놓음
    public void SortProcessStackWithArrivalTime(){
        Collections.sort(ProcessStack);
    }

    //Abstract Class "Scheduler"'s Constructor
    //Initiate Process
    public Scheduler(int processCount,List<Integer> preprocessPCBs){
        System.out.println("===========================");
        System.out.println("[ Scheduler Simulator is now Running ]");
        initiateProcess(preprocessPCBs);
        this.processCount = processCount;
    }

    protected void ReEnqueueToReadyQueue(ProcessObjects processObjects){
        //프로세스 객체의 CPU Burst, IOBurst를 다시 랜덤값으로 초기화한다.
        processObjects.setArrivalTime(SchedulerTotalRunningTime);
        processObjects.setCPUBurstIOBurstRandom();
        // ReadyQueue가 비어있고, CPU에서 Running State인 프로세스가 존재하지 않는다면
        // ReadyQueue로 가지 않고 바로 CPU에 올린다.
        if(ReadyQueueEmpty() && !cpu.CPUhasProcess()){
            cpu.setProcess(processObjects);
            // 위 두 조건중 하나라도 충족 안할 시 ReadyQueue로 보낸다.
        }else{
            ReadyQueue.add(processObjects);
        }
    }
    // Overloading ReEnqueToReadyQueue
    protected void ReEnqueueToReadyQueue(ProcessObjects processObjects,boolean ifCpuValuesNotChanged){
        // 만약 CPU 관련 value들이(CPU Time, CPU Burst) 변하지 않는다고 한다면,
        if(ifCpuValuesNotChanged){
            // 만약 ReadyQueue가 비어있고, CPU에서 작동중인 프로세스가 없으면 바로 CPU에 넣어준다.
            if(ReadyQueueEmpty() && !cpu.CPUhasProcess()){
                System.out.println("Process  : " + processObjects.getPid() + " re-set to Running State. Ready Queue & CPU Running Process is empty");
                cpu.setProcess(processObjects);
            }else{
                // 그대로 넣는다
                System.out.println("Process " + processObjects.getPid() + " go back to ReadyQueue Directly.");
                System.out.println("Process left CPU Time : " + processObjects.getRemaining_cpu_time());
                System.out.println("Process left CPU Burst Time : " + processObjects.getRemaining_cpu_burst());
                ReadyQueue.add(processObjects);
            }
        }
        // 변해야하는 경우에는 기존 ReEnqueueToReadyQueue()메소드를 그대로 사용
        else{
            ReEnqueueToReadyQueue(processObjects);
        }
    }

    public void EnqueToIOQueue(ProcessObjects processObjects){
        // 만약 프로세스의 전체 IO Burst가 0보다 작거나 같으면 IO Queue로 안들어 가고 ReadyQueue로 가게 된다
        // IO Burst가 0이하인 프로세스에 대해서
        if(processObjects.getIOBurstTime() <= 0){
            // 만약 CPU Time이 끝난 프로세스라면 finish queue 로
            if(processObjects.getRemaining_cpu_time() <= 0){
                processObjects.setFinishedTime(SchedulerTotalRunningTime);
                addToFinishedQueue(processObjects);
                // 아닌 경우에는 정상적으로 다시 Ready Queue에 넣는다.
            }else{
                System.out.println("Process : " + processObjects.getPid() + " reenque to ready queue");
                // 만약 Ready Queue도 비어있고, CPU에서 Running하고 있는 프로세스도 없다면, 해당 프로세스를 다시 레디큐에 넣지 않고 CPU에 넣는다
                if(ReadyQueueEmpty() && !cpu.CPUhasProcess()){
                    System.out.println("Process  : " + processObjects.getPid() + " re-set to Running State. Ready Queue & CPU Running Process is empty");
                    cpu.setProcess(processObjects);
                }
                // ContextSwitching에 의해 CPU에 다른 프로세스가 있거나 Ready Queue에 프로세스가 있는 경우에는
                else{
                    ReEnqueueToReadyQueue(processObjects);
                }
            }
        }
        // 만약 아닌 경우, IOQueue로 들어가게 된다.
        else{
            System.out.println("Process : " + processObjects.getPid() + " go to I/O State(Blocked State)");
            IOQueue.add(processObjects);
        }
    }


    // print selected algorithm and Processes that initialized
    protected void PrintAlgorithmNameAndProcesses(String AlgorithmName){
        SchedulerAlgorithmName = AlgorithmName;
        System.out.println("[ Selected Scheduling Algorithm : " + AlgorithmName + " Algorithm ]");
        System.out.println("[ Process initiated. Process initiated list will be show below ]");
        printProcessList();
        System.out.println("===========================");
    }

    // Overloading
    // additional print : Time Quantum for Rounded Robin
    protected void PrintAlgorithmNameAndProcesses(String AlgorithmName,int TimeQuantum){
        SchedulerAlgorithmName = AlgorithmName;
        System.out.println("[ Selected Scheduling Algorithm : " + AlgorithmName + " Algorithm ]");
        System.out.println("[ Time Quantum : " + TimeQuantum + " ]");
        System.out.println("[ Process initiated. Process initiated list will be show below ]");
        printProcessList();
        System.out.println("===========================");
    }

    protected void addToFinishedQueue(ProcessObjects process){
        FixedVariables.ConsolePrintFileWriteParellel("Process " + process.getPid() + " end of running and enque to finished Queue at " + SchedulerTotalRunningTime);
        finishedQueue.add(process);
    }

    //각 순환별 최초로 실행해야하는 작업들이다.
    protected void IntegratedInitialJobPerEachCircular(){
        CheckSchedulerExitCondition(); // 종료조건 검사
        CheckProcessStackAndEnqueToReadyQueue(); // 프로세스 스택에서 각 프로세스들의 ArrivalTime을 검사한 후에 ReadyQueue에 넣는작업
        CheckIOQueueUnLockBlockedState(); // IO Queue에서 프로세스의 IO Burst를 검사한다.
    }

    // 각 순환별 마지막으로 실행해 주어야 하는 작업들이다.
    // 이유 : 초반 실행히 CPU의 프로세스가 null인 상태에서 연산을 하게되면 NullPointerException 예외의 위험성으로 인해
    protected void IntegratedAfterJobPerEachCircular(){
        printCycleSummary();
        CheckSchedulerExitCondition(); // 종료조건 검사 : 사이클이 끝나기 전에도 검사를 해주어야 한다. 이 과정이 없으면 뒤에 불필요한 연산이 생기므로
        ReadyQueueAddOneSecond(); // Ready Queue의 프로세스 ready state 1초씩 증가
        IOQueueAddOneSecond(); // IO Queue의 프로세스들 blocked state 1초씩 증가
        cpu.processOneSecondPast(); // CPU의 주도권을 가지고 있는 프로세스의
        IOIDLEOneSecond(); // IO IDLE상태에 대한 검사
        CPUIDLEOneSecond(); // CPU IDLE상태에 대한 검사
        SchedulerTotalRunningTime++;
        System.out.println("Cycle ends\n");
    }

    public void printCycleSummary(){
        System.out.println("\n** Cycle Summary **");
        ArrayList<String> e = new ArrayList<>();
        System.out.println("( CPU Info )");
        System.out.print("Running Process : ");
        if(cpu.CPUhasProcess()){
            System.out.println(cpu.getProcess().getPid());
        }else{
            System.out.println("No other process Running");
        }
        System.out.println("( Ready Queue )");
        System.out.print("Ready Queue : ");
        if(ReadyQueueEmpty()){
            System.out.println("No other process is ready state");
        }else{
            for(ProcessObjects p : ReadyQueue){
                e.add(p.getPid());
            }
            System.out.println(String.join("->",e));
        }
        e.clear();
        System.out.println("( I/O Queue )");
        System.out.print("I/O Queue : ");
        if(IOQueueEmpty()){
            System.out.println("No other process is blocked state");
        }else{
            for(ProcessObjects p : IOQueue){
                //Debugging Comment
                //System.out.println(p.getRemaining_cpu_time() + " " + p.getRemaining_cpu_burst() + " " + p.getRemaining_io_burst());
                e.add(p.getPid());
            }
            System.out.println(String.join("->",e));
        }
        System.out.println("*******************\n");
    }

    protected void printProcessList(){
        String msg = "";
        for(ProcessObjects p : ProcessStack){
            msg += p;
        }
        System.out.println(msg);
    }

    // Ready Queue에 있는 모든 프로세스에 대해 ready state값을 1씩 증가시켜준다
    public void ReadyQueueAddOneSecond(){
        for(ProcessObjects p : ReadyQueue){
            p.oneSecondPastReadyQueue();
        }
    }
    // IO Queue에 있는 모든 프로세스에 대해 blocked state값을 1씩 증가시키고 remaining io burst를 1감소시킨다.
    public void IOQueueAddOneSecond(){
        for(ProcessObjects p : IOQueue){
            p.oneSecondPastIOQueue();
        }
    }

    //스케줄러의 실행시간에 1초를 더해준다.
    public void SchedulerRunningTimeAddOneSecond(){
        SchedulerTotalRunningTime++;
    }

    public void CPUIDLEOneSecond(){
        // CPU에 프로세스가 없을때만 IDLE Time에 1초를 더해준다
        if(!cpu.CPUhasProcess()){
            CPUIDLETime++;
        }
    }

    public void IOIDLEOneSecond(){
        // IO Queue에서 I/O상태인 프로세스가 없을때만 1초를 더해준다.
        if(IOQueue.isEmpty()){
            IOIDLETime++;
        }
    }

    //ProcessStack에서 현재 스케줄러 러닝 타임에 비해 작거나 같은 프로세스가 있으면 Ready Queue에 Enque한다
    // 스케줄러 최초 실행해 ProcessStack은 ArrivalTime을 기준으로 정렬되기 때문에 이렇게 해도 문제가 되지 않음
    public void CheckProcessStackAndEnqueToReadyQueue(){
        System.out.println("Checking Process Arrival Time");
        int counter = 0;
        for(ProcessObjects p : ProcessStack){
            if(p.getArrivalTime() <= SchedulerTotalRunningTime){
                counter++;
            }
        }
        for(int i = 0; i < counter;i++){
            ReadyQueue.add(ProcessStack.removeFirst());
        }
    }

    // IOQueue에서 Remaining IO Burst Time이 0보다 작은경우에 대해 처리한다
    public void CheckIOQueueUnLockBlockedState(){
        Queue<ProcessObjects> q = new LinkedList<>();
        // 우선 IO Queue에서 IOBurst가 0보다 작은 Process가 있는지 확인한다. 있는 경우 q에 저장
        for (ProcessObjects processObjects : IOQueue) {
            if (processObjects.getRemaining_io_burst() <= 0) {
                q.add(processObjects);
            }
        }
        //I/O작업이 끝난 프로세스들에 대해서
        for(ProcessObjects processObjects : q){
            // CPU Time이 0 이하인 경우 -> 종료된 프로세스
            if(processObjects.getRemaining_cpu_time() <= 0){
                System.out.println(processObjects.getPid());
                processObjects.setFinishedTime(SchedulerTotalRunningTime);
                addToFinishedQueue(processObjects);
                // CPU Time이 1이상 -> 아직 작동중인 프로세스
            }else{
                ReEnqueueToReadyQueue(processObjects);
            }
            IOQueue.remove(processObjects);
        }
    }

   // ReadyQueue비어있는지 확인한다
    public boolean ReadyQueueEmpty(){
        return ReadyQueue.isEmpty();
    }
    //IOQueue비어있는지 확인한다
    public boolean IOQueueEmpty(){
        return IOQueue.isEmpty();
    }
    //ProcessStack비어있는지 확인한다
    public boolean ProcessStackEmpty(){
        return ProcessStack.isEmpty();
    }

    public void CheckSchedulerExitCondition(){
        System.out.println("Check Scheduler Exit Condition");
        /*
        * 종료조건
        * 1. ProcessStack에 남은 스택 없음
        * 2. ReadyQueue가 비어있음
        * 3. IOQueue가 비어있음
        * 4. CPU에 돌아가고 있는 프로세스가 없을때
        * */
        if(ProcessStackEmpty() && ReadyQueueEmpty() && IOQueueEmpty() && !cpu.CPUhasProcess()){
            System.out.println("Scheduler Simulation End!");
            /*
             이 시뮬레이터에서 스케줄러는 IO Running Time까지 포함한 시간이다.
             그렇기 때문에 이 종류 시점에는 마지막 프로세스의 IO Time까지 포함한 시간을 값으로 가지게 된다
             하지만 실제 스케줄러(간트 차트상에서)에서는 마지막 프로세스의 IO Time을 뺀 시간까지만 고려를 하게된다

             CPU스케줄러와 Blocked State는 별개이므로
             결국 스케줄러의 총 Running Time은 마지막 Finish 프로세스의 Finish Time과 동일해야 한다.
             */
            SchedulerTotalRunningTime = finishedQueue.getLast().getFinishedTime();
            printSummary();
            FixedVariables.ExitProgram();
        }
    }

    // 분기줄 계산메소드
    public String CalculateLines(String msg){
        int max = 50;
        int size = max - msg.length();
        size = size / 2;
        String line = "";
        for(int i = 0; i < size;i++){
            line += "=";
        }
        return "\n" + line + " " + msg + " " + line;
    }

    public void printSummary(){
        int totalRunningOfProcesses = 0;
        int totalBlockedOfProcesses = 0;
        int totalTurnAroundTimeProcesses = 0;
        int totalWaitingTimeProcesses = 0;


        System.out.println(CalculateLines("[ Summary of " + SchedulerAlgorithmName + " finished order ]"));
        // 종료한 순서대로 PID 출력
        Queue<String> FinishQueuePID = new LinkedList<>();
        for(ProcessObjects p : finishedQueue){
            FinishQueuePID.add(p.getPid());
        }
        System.out.println(String.join(" -> ",FinishQueuePID));
        System.out.println(CalculateLines("[ Summary of each process ]"));

        for(ProcessObjects p : finishedQueue){
            System.out.println("< Process : " + p.getPid() + " >");
            System.out.println("Finishing Time : " + p.getFinishedTime());
            System.out.println("Turnaround Time : " + (p.getFinishedTime() - p.getFirstArrivedTime()));
            totalTurnAroundTimeProcesses += (p.getFinishedTime() - p.getFirstArrivedTime());
            System.out.println("CPU Time(Running State Time) : " + p.getTotal_running_time());
            totalRunningOfProcesses += p.getTotal_running_time();
            System.out.println("I/O Time(Blocked State Time) : " + p.getTotal_blocked_time());
            totalBlockedOfProcesses += p.getTotal_blocked_time();
            System.out.println("Waiting Time(Ready State Time) : " + p.getTotal_ready_time());
            totalWaitingTimeProcesses += p.getTotal_ready_time();
            System.out.println("\n");
        }

        System.out.println(CalculateLines("[ Summary of Scheduler ]"));
        System.out.println("Scheduler Finishing Time : " + SchedulerTotalRunningTime);
        System.out.println("Average turnaround time : " + (totalTurnAroundTimeProcesses / processCount));
        System.out.println("Average waiting time : " + (totalWaitingTimeProcesses / processCount));
        System.out.println("CPU Utilization : " + returnCPUUtilization());
        System.out.println("I/O Utilization : " + returnIOUtilization());
        System.out.println("Throughput in processes completed per hundred time units : " + returnThroughPutInProcessCompletedPerHundredTimeUnit());
    }

    // CPU Utilization
    public String returnCPUUtilization(){
        float t = ((float)(SchedulerTotalRunningTime - CPUIDLETime) / (float)SchedulerTotalRunningTime) * 100;
        // 소수점 두자리까지만
        String value = String.format("%.2f",t);
        return value + " %";
    }

    // IO Utilization
    public String returnIOUtilization(){
        float t = ((float)(SchedulerTotalRunningTime - IOIDLETime) / (float)SchedulerTotalRunningTime) * 100;
        // 소수점 두자리까지만
        String value = String.format("%.2f",t);
        return value + " %";
    }

    public String returnThroughPutInProcessCompletedPerHundredTimeUnit(){
        float t = ((float)(finishedQueue.size())/(float)SchedulerTotalRunningTime) * 100;
        String value = String.format("%.2f",t);
        return value + " %";
    }


    // 스케줄링 알고리즘마다 다음 프로세스 선택기준이 다를 수 있으므로 추상메소드 정의
    public abstract ProcessObjects selectNextProcess();
    // Implement each cpu scheduling algorithm in this method
    public abstract void Algorithm();
}

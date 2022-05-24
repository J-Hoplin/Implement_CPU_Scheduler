package CPUScheduler.CPU;

import CPUScheduler.Processor.ProcessObjects;


// Basic class of containing cpu principle
public class CPU {
    private static CPU cpu;
    private ProcessObjects process;

    // CPU Instance should be exist one instance
    private CPU(){}

    // Apply Singleton Pattern
    public static CPU getInstance(){
        if(cpu == null){
            cpu = new CPU();
        }
        return cpu;
    }

    public ProcessObjects getProcess(){
        return this.process;
    }

    public void setProcess(ProcessObjects processObjects){
        System.out.println("Process " + processObjects.getPid() + " state changed to running state");
        this.process = processObjects;
    }

    // CPU 안에서 돌아가는 프로세스를 완전히 제거한다.
    public void clearCPUProcess(){
        this.process = null;
    }

    // CPU안에서 돌아가고 있는 프로세스가 있는지 판별한다.
    public boolean CPUhasProcess(){
        return process != null;
    }

    public void processOneSecondPast(){
        if(CPUhasProcess()){
            process.oneSecondRunningState();
        }
    }
}

package CPUScheduler.Process;

import CPUScheduler.Exceptions.IllegalCPUBurstException;
import CPUScheduler.Exceptions.IllegalIOBurtException;

import java.util.Random;

public class ProcessObjects implements Comparable<ProcessObjects>{
    // 최초 Arrival Time
    protected int FirstArrivedTime;

    //Ready Queue에 있었던 시간
    protected int total_ready_time = 0;
    protected int total_blocked_time = 0;
    protected int total_running_time = 0;

    // CPU remaining time은 공유자원 선언
    protected String pid;
    protected int ArrivalTime;// Refers to Parameter "A"

    protected int RequireCPUTime; // Refers to Parameter "C"
    protected int remaining_cpu_time;

    protected int CPUBurstRandomSeed; //Refers to Parameter "B"
    protected int CPUBurstTime;
    protected int remaining_cpu_burst;

    protected int IOBurstRandomSeed; // Refers to Parameter "IO"
    protected int IOBurstTime;
    protected int remaining_io_burst;

    protected int finishedTime; // Process Finished Time;

    private Random random = new Random();

    public ProcessObjects(int pid, int ArrivalTime, int RequireCPUTime, int CPUBurstRandomSeed, int IOBurstRandomSeed){
        this.pid = "PID_" + pid;
        this.FirstArrivedTime = ArrivalTime;
        this.ArrivalTime = ArrivalTime;
        this.RequireCPUTime = this.remaining_cpu_time = RequireCPUTime;
        this.CPUBurstRandomSeed = CPUBurstRandomSeed;
        this.IOBurstRandomSeed = IOBurstRandomSeed;
        setCPUBurstIOBurstRandom();
    }

    @Override
    public String toString() {
        return (new StringBuilder()
                .append("-----------")
                .append("\nPID : ")
                .append(this.pid)
                .append("\n")
                .append("Arrival Time : ")
                .append(this.ArrivalTime)
                .append("\n")
                .append("Required CPU Time : ")
                .append(this.RequireCPUTime)
                .append("\n")
                .append("Remaining CPU Time : ")
                .append(this.remaining_cpu_time)
                .append("\n")
                .append("CPU Burst Time : ")
                .append(this.CPUBurstTime)
                .append("\n")
                .append("Remaining CPU Burst Time : ")
                .append(this.remaining_cpu_burst)
                .append("\n")
                .append("IO Burst Time : ")
                .append(this.IOBurstTime)
                .append("\n")
                .append("Remaining IO Burst Time : ")
                .append(this.remaining_io_burst)
                .append("\n")
                .append("-----------\n")).toString();
    }

    // Ready Queue, Waiting Queue, Running 상태에서 1초씩 더해주는 메소드들
    public void oneSecondPastReadyQueue(){
        // Ready Queue에 있을때 1초를 더함
        total_ready_time++;
    }
    public void oneSecondPastIOQueue(){
        // IO Queue에 있을때 1초를 더함
        total_blocked_time++;
        remaining_io_burst--;
    }
    public void oneSecondRunningState(){
        // Running State 관련해서 1초를 더한다
        total_running_time++;
        remaining_cpu_time--;
        remaining_cpu_burst--;
    }

    public void setCPUBurstIOBurstRandom() {
        try {
            setRandomIOBurst();
            setRandomCPUBurst();
        } catch (IllegalCPUBurstException | IllegalIOBurtException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }


    // Set Random CPU Burst
    private void setRandomCPUBurst() throws IllegalCPUBurstException {
        if (CPUBurstRandomSeed < 0){
            throw new IllegalCPUBurstException();
        }
        else if(CPUBurstRandomSeed == 0){
            this.CPUBurstTime = remaining_cpu_burst = 0;
        }else{
            this.CPUBurstTime = remaining_cpu_burst = random.nextInt(CPUBurstRandomSeed) + 1;
        }
    }
    // Set Random IO Burst
    private void setRandomIOBurst() throws IllegalIOBurtException {
        if(IOBurstRandomSeed < 0){
            throw new IllegalIOBurtException();
        }
        else if(IOBurstRandomSeed == 0){
            this.IOBurstTime = remaining_io_burst = 0;
        }else{
            this.IOBurstTime = remaining_io_burst = random.nextInt(IOBurstRandomSeed) + 1;
        }
    }

    public int getFirstArrivedTime(){
        return this.FirstArrivedTime;
    }

    // Getter and Setter of Arrival Time
    public int getArrivalTime() {
        return ArrivalTime;
    }
    public void setArrivalTime(int arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    // Get Remaining CPU Burst Time
    public int getRemaining_cpu_burst(){
        return remaining_cpu_burst;
    }
    // Get Total IO Burst Time
    public int getIOBurstTime(){
        return IOBurstTime;
    }
    // Get Remaining IO Burst Time
    public int getRemaining_io_burst(){
        return remaining_io_burst;
    }
    public int getRemaining_cpu_time(){return remaining_cpu_time;}
    public int getFinishedTime(){return this.finishedTime;}
    // Set Process Fininshed time
    public void setFinishedTime(int time){
        finishedTime = time;
    }
    // Getter of PID
    public String getPid(){
        return this.pid;
    }

    public int getTotal_ready_time(){
        return this.total_ready_time;
    }
    public int getTotal_blocked_time(){
        return this.total_blocked_time;
    }
    public int getTotal_running_time(){
        return this.total_running_time;
    }

    @Override
    public int compareTo(ProcessObjects p) {
        /*
         * 현재 객체 < 매개변수로 받은 객체 : 음수 리턴
         * 현재 객체 > 매개변수로 넘어온 객체 : 양수리턴
         * 현재 객체 == 매개변수로 넘어온 객체 : 0 리턴
         *
         *
         * 0 혹은 음수가 반환되면 유지, 양수일 경우 두 객체의 자리를 바꾼다
         * */
        if(this.ArrivalTime == p.getArrivalTime()) return 0;
        else if(this.ArrivalTime < p.getArrivalTime()) return -1;
        return 1;
    }
}

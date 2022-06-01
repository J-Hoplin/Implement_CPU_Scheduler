# Implement CPU Scheduling Algorithm
***
Source Code / Readme Written By : 윤준호(a.k.a Hoplin)

Subject : Operating System

Description : Implement CPU Scheduling. Use programming language except Script Language(Python, JavaScript...etc)<br>

Language : Java(jdk 1.8(Zulu Open JDK 11.04), Java 11)

Tool : [JetBrain IntelliJ IDEA Ultimate](https://www.jetbrains.com/ko-kr/idea/download/#section=mac)

[운영체제 프로세스 스케줄링 관련 정리 PDF](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/blob/main/Cpu%20Scheduler.pdf) : [Download](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/raw/main/Cpu%20Scheduler.pdf)
***
### Contents Shortcut

[1. How to use?](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler#how-to-use)<br>
[2. Class Diagram](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler#class-diagram)<br>
[3. Version State](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler#version-state)<br>
[4. Assignment Description](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler#assignment-description)


***
### How to use

```
$ git clone https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler.git
$ cd OS_Implement_CPU_Scheduler/OperatingSystemAssignment/src
$ javac CPUScheduler/SchedulerMain.java -encoding UTF-8
$ java CPUScheduler/SchedulerMain --help
```
***
### Class Diagram
<img width="1249" alt="image" src="https://user-images.githubusercontent.com/45956041/170594703-3f29e421-7833-4835-9883-f478c83e5a8c.png">

***
### TODO
- [ ] Template Method Pattern 적용하기  
***
### Version State

- [v 1.1.0](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/v-1.1.0) : **해당 버전의 코드로 과제 제출**
    - Scheduler Summary에서 Average Turnaround, Average Waiting에 대해 소수점 둘째자리까지 표시되게끔 변경하였습니다.
    - Logic Error : 프로세스가 마지막 CPU Burst가 끝난 다음, IO Burst가 일어나지 않는다는 점을 모르고 있었습니다. 이 내용을 알고리즘에 적용시켰습니다.
    ```java
    public void EnqueToIOQueue(ProcessObjects processObjects){
        //프로세스의 CPU Time이 0이된 순간에는 더이상 IO가 발생하지 않는다. 그렇기 때문에, 바로 Finish Queue로 넣어준다.
        if(processObjects.getRemaining_cpu_time() <= 0){
            processObjects.setFinishedTime(SchedulerTotalRunningTime);
            addToFinishedQueue(processObjects);
        }
        // CPU Time은 0보다 크다
        // 만약 프로세스의 전체 IO Burst가 0보다 작거나 같으면 IO Queue로 안들어 가고 ReadyQueue로 가게 된다
        // IO Burst가 0이하인 프로세스에 대해서
        else if(processObjects.getIOBurstTime() <= 0){
            ReEnqueueToReadyQueue(processObjects);
        }
        // 만약 아닌 경우, IOQueue로 들어가게 된다.
        else{
            System.out.println("Process : " + processObjects.getPid() + " go to I/O State(Blocked State)");
            IOQueue.add(processObjects);
        }
    }
    ```
***
### Assignment Description
이 과제에서는 CPU scheduling 알고리즘에 따라 여러 가지 성능수치가 어떻게 달라지는가를 관찰하기 위한 시뮬레이션을 수행한다. 시뮬레이션 프로그램이 수행해야 할 가장 기본적인 작업은 computation과 I/O 요청을 번갈아 수행하는 process들에 대해 CPU scheduling을 수행하는 것이다. 이를 위해 다음과 같이 간단한 가정을 한다. 

- 각 process에 대해 그 process가 도착한 시각을 A 라고 하고, 그 process가 종료될 때까지  필요로 하는 총 CPU time을 C 라고 하자. 
- CPU burst time은 0과 어떤 수 B 사이에서 uniformly distributed random integer이다. 또한 IO burst time은 0과 어떤 수 IO 사이의 uniformly distributed random integer이다.
프로세스는 이 4개의 파라미터 (A, C, B, IO) 에 의해 정의된다. 이 숫자들의 단위는 단순히 time unit 이다.

프로그램은 n개의 프로세스들을 기술한 (즉 4개의 숫자가 한 그룹이  되는 그룹들이 n개가 있어야 할 것임) 파일을 읽어 들인 후 그 n개의 process가 모두 끝날 때까지 그 process들을 시뮬레이션 해야 한다. 이를 위한 기본적인 방법은 각 프로세스의 상태를 추적해서 필요할 때마다 상태전이(state transition)를 수행하고 시간을 전진시키는 것이다. 

이렇게 해서 모든 process의 수행이 끝나면 사용된 CPU scheduling 알고리즘, 사용된 파라미터(예: Round Robin방식에서 사용되는 quantum), simulate된 process의 수 등을 출력하고, 그 다음 각 process에 대해 다음과 같은 내용을 출력한다.

#### About Each Process

- (A,C,B,IO)
- Finishing Time(Process End Time)
- Turnaround Time(Finishing Time - @param A)
- CPU Time(Total time of Process' running state)
- I/O Time(Totla time of Process' blocked state)

#### Summary of Scheduler Simulation

- Finishing Time(모든 프로세스가 다 끝난 시각)
- CPU Utilization(Percentage of time some job is running)
- I/O Utilization(Percentage of time some job is blocked)
- Throughput in processes completed per hundred time unit
- Average turnaround time
- Average waiting time

#### Need to implement these types of scheduling algorithm

- FCFS(First Come First Served)
    - Non-Preemptive
- SJF(Shortest Job First)
    - Non-Preemptive
- RR(Round Robin)
    - Preemptive
    - With Time Quantum 1
    - With Time Quantum 10
    - With Time Quantum 100

#### Test Cases

1. >> 1 (0 5 1 0)
2. >> 1 (0 5 1 1)
3. >> 3 (0 5 1 1)(0 5 1 1)(3 5 1 1)
4. >> 5 (0 200 3 3)(0 500 9 3)(0 500 20 3)(100 100 1 0)(100 500 100 3)
5. >> 5 (0 200 3 3)(0 500 9 3)(0 500 20 30)(100 100 1 0)(100 500 100 3)

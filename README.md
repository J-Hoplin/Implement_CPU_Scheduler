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
[2. Class Diagram](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler#how-to-use)<br>
[3. Version State](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler#how-to-use)<br>
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
### Version State

- [v 1.0.0 : Implement FCFS / SJF : 2022/05/22](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/Implement_FCFS_SJF)
    - Deprecated : 잘못된 알고리즘이 작성되어있습니다.(Process의 End Time을 I/O Time을 포함해서 연산하도록 되어있음)
- [v 1.0.1 : Implement RR : 2022/05/23](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/Implement_Round_Robin)
    - Not Recommended : Round Robin알고리즘에서 Time Quantum 종료에 의해서 Ready Queue로 들어갈 때 값을 유지해 주어야 하는데 이부분을 고려하지 않음
    - Todos
        - [x] Add CPU Utilization / I/O Utilization Algorithms
            - Fetched in [version 1.0.2](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/v-1.0.2)
        - [x] Fix Scheduler Finish Time. Exclude I/O Count
            - Fetched in [version 1.0.2](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/v-1.0.2)
- [v 1.0.1_alpha_1 : Implement RR](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/v-1.0.1_alpha_1)
    - Not Recommended : 불안정
    - v 1.0.1 Dev Version
- [v 1.0.2 : Debug fetch_1 : 2022/05/25](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/v-1.0.2)
    - 특정 프로세스를 Ready Queue에 넣기 전에 Ready Queue가 비어있고,CPU안에서 돌아가는 프로세스가 없는 경우, 해당 프로세스를 바로 CPU로 올려야 합니다. v 1.0.1에서는 해당 부분이 적용되지 않았지만 v 1.0.2에서는 적용이 됩니다. 예제 코드는 아래와 같습니다. 이 부분의 소스코드는 약간의 하드코딩이 되어있습니다. 다음 버전에서 'ReadyQueueEmpty() && !cpu.CPUhasProcess()'를 하나의 메소드로 묶을 예정입니다.
    ```java
    if(ReadyQueueEmpty() && !cpu.CPUhasProcess()){
        cpu.setProcess(processObjects);
        // 위 두 조건중 하나라도 충족 안할 시 ReadyQueue로 보낸다.
    }else{
        ReadyQueue.add(processObjects);
    }
    ```
    - v 1.0.1에서 있었던 Round Robin 문제를 아래와 같이 ReEnqueueToReadyQueue메소드를 Overloading하여 해결하였습니다
    ```java
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
    ```
    - I/O Utilization, CPU Utilization, Throughput in processes completed per hundered time units 연산 알고리즘을 적용하였습니다.
- [v 1.0.3 : 2022/05/25](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/v_1.0.3)
    - 'ReadyQueueEmpty() && !cpu.CPUhasProcess()'를 하나의 메소드로 묶었습니다
    ```java
    protected boolean checkCPUReadyQueueEmpty(){
        return ReadyQueueEmpty() && !cpu.CPUhasProcess();
    }
    ```

- [v 1.0.4 : 2022/05/27](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/v_1.0.4) : [main](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler)
    - 실행되는 플랫폼에 따라 절대경로 지정을 달리하였습니다.
    ```java
    public static String getRootDirectory(){
        String RootDirectory = System.getProperty("user.dir");
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("windows")){
            RootDirectory += "\\CPUScheduler\\";
        }else if(os.contains("mac") || os.contains("nix") || os.contains("linux")){
            RootDirectory +="/CPUScheduler/";
        }
        return RootDirectory;
    }
    ```
    - 커맨드 실행시 매개변수로 파일 이름과 알고리즘 이름을 적어줍니다. 알고리즘 이름을 기존에는 모두 대문자로 입력해야했지만, 소문자로 입력해도 실행되도록 변경하였습니다.(String toUpper 적용)
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

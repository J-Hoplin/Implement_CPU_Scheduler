# Operating System Programming Assignment
***
Source Code / Readme Written By : 윤준호(a.k.a Hoplin)

Subject : Operating System

Description : Implement CPU Scheduling. Use programming language except Script Language(Python, JavaScript...etc)
***
### Version State

- [v 1.0.0 : Implement FCFS / SJF](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/Implement_FCFS_SJF)
    - Deprecated : 잘못된 알고리즘이 작성되어있습니다.(Process의 End Time을 I/O Time을 포함해서 연산하도록 되어있음)
- [v 1.0.1 : Implement RR](https://github.com/J-hoplin1/OS_Implement_CPU_Scheduler/tree/Implement_Round_Robin)
    - Todos
        - [ ] Add CPU Utilization / I/O Utilization Algorithms
        - [ ] Fix Scheduler Finish Time. Exclude I/O Count

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

- Finishing Time(Scheduler End Time)
- CPU Utilization
- I/O Utilization
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

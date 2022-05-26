package CPUScheduler;

import CPUScheduler.Configurations.FixedVariables;
import CPUScheduler.Configurations.SchedulerTypes;
import CPUScheduler.Processor.ProcessObjects;
import CPUScheduler.SchedulerAlgorithms.FistComeFirstServed;
import CPUScheduler.SchedulerAlgorithms.RoundRobin;
import CPUScheduler.SchedulerAlgorithms.Scheduler;
import CPUScheduler.SchedulerAlgorithms.ShortestJobFirst;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SimulationStream {
    // 프로세스 만들때 나눠야하는 배열 단위개수
    private final int parameterBlockSize = 4;

    // 프로세스 개수 저장
    private int ProcessCount = 0;
    // 스케줄러 객체저장
    private Scheduler scheduler;
    // Get Scheduler Type Enumeration as a list
    private SchedulerTypes[] types = SchedulerTypes.values();


    // Designate ProcessorObject Class Type to here
    private ProcessObjects type;


    // Get Instance : Use singleton pattern
    private static SimulationStream simulationStream;
    private SimulationStream(){}
    public static SimulationStream getInstance(){
        if(simulationStream == null){
            simulationStream = new SimulationStream();
        }
        return simulationStream;
    }

    // Check arguments array and initiate Scheduler;
    private void selectScheduler(String[] args,List<Integer> preprocessPCBs){
        String schedulerField = args[1].toUpperCase();
        switch (schedulerField){
            case "FCFS":
                scheduler = new FistComeFirstServed(ProcessCount,preprocessPCBs);
                break;
            case "RR":
                try{
                    scheduler = new RoundRobin(ProcessCount,preprocessPCBs,args[2]);
                }catch (IndexOutOfBoundsException e){
                    CommandUtilities.RaiseHelpCommand("[ Parameter Error : Time Quantum Lost Error ]\n");
                }
                break;
            case "SJF":
                scheduler = new ShortestJobFirst(ProcessCount,preprocessPCBs);
                break;
            default:
                System.out.println();
                CommandUtilities.RaiseHelpCommand("[ Parameter Error : Not Supported Type Scheduler]\n");
        }
    }


    public void preProcessArgumentsAndStartSimulation(String[] args){
        //Check arguments format
        CommandUtilities.RaiseHelpCommand(args);

        String testcase="";
        BufferedReader br;
        try{
            //Read File and save first line to variable
            // 절대경로로 파일 탐색
            br = new BufferedReader(new FileReader(args[0]));
            testcase = br.readLine();
        } catch (FileNotFoundException e){
            try {
                // 실행 디렉토리내에서 파일 탐색
                br = new BufferedReader(new FileReader(FixedVariables.RootDirectory + args[0]));
                testcase = br.readLine();
            } catch (FileNotFoundException ex) {
                System.out.println(FixedVariables.RootDirectory + args[0]);
                // File Not Found Block
                System.out.println(new StringBuilder().append("File '").append(args[0]).append("' not exist.").toString());
                FixedVariables.ExitProgram();
            }catch (IOException ep){
                // I/O Exception Block
                System.out.println("I/O Exception Occured. Print stack trace");
                FixedVariables.ExitProgram();
            }
        }catch (IOException e){
            // I/O Exception Block
            System.out.println("I/O Exception Occured. Print stack trace");
            e.printStackTrace();
            FixedVariables.ExitProgram();
        }
        String[] splits = testcase.split(" ");

        // 프로세스 개수 정보 저장
        ProcessCount = Integer.parseInt(splits[0]);

        // 배열 슬라이싱 : 1 ~ length of splits
        splits = Arrays.copyOfRange(splits,1,splits.length);

        // 밑에서 Integer 타입으로 Mapping할것을 대비하여 Object타입으로 제네릭을 지정한다.
        List<Integer> preprocessPCBs = new ArrayList<>();

        for(int i = 0;i<splits.length;i++){
            if(splits[i].contains(")(")){
                splits[i] = splits[i].replace(")("," ");
                String[] p = splits[i].split(" ");

                for(String a:p){
                    preprocessPCBs.add(Integer.valueOf(a));
                }
            }else{
                if(splits[i].contains(")")){
                    splits[i] = splits[i].replace(")","");
                }else if(splits[i].contains("(")){
                    splits[i] = splits[i].replace("(","");
                }else{
                    ;
                }
                preprocessPCBs.add(Integer.valueOf(splits[i]));
            }
        }

        startSimulation(args,preprocessPCBs);
    }


    private void startSimulation(String[] args,List<Integer> preprocessPCBs){
        selectScheduler(args,preprocessPCBs);
    }
}

package CPUScheduler.Logger;

import CPUScheduler.Configurations.GlobalUtilities;
import CPUScheduler.Exceptions.DirectoryGenerateFailedException;
import CPUScheduler.Exceptions.IllegalMethodCallException;
import CPUScheduler.SchedulerAlgorithms.Scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static StringBuilder LOGMSG = new StringBuilder();
    public Log(){}
    private static void AppendToLogMSG(String msg,Boolean newline){
        if(newline){
            Log.LOGMSG.append(msg).append("\n");
        }else{
            Log.LOGMSG.append(msg);
        }
    }
    public static void Logger(String msg){
        System.out.println(msg);
        AppendToLogMSG(msg,true);
    }
    public static void Logger(String msg,Boolean newline){
        if(newline) {
            Logger(msg);
        }else{
            System.out.print(msg);
            AppendToLogMSG(msg,false);
        }
    }
    public static void SaveLogAsTxt() throws DirectoryGenerateFailedException {
        System.out.println("\nSaving log file...");
        String logdirectory = GlobalUtilities.getRootDirectory() + "logfiles/";
        File root = new File(logdirectory);
        // Check directory
        if(!root.exists()){
            boolean res = root.mkdir();
            if(!res){
                throw new DirectoryGenerateFailedException();
            }
        }
        try{
            String AlgorithmName = Scheduler.getSelectedAlgorithmName();
            LocalDateTime n = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy_MM_dd hh_mm_ss");
            String timeInfo = n.format(format);
            String textFileName = timeInfo + "_" + AlgorithmName + ".txt";
            File file = new File(logdirectory + textFileName);
            FileWriter w = new FileWriter(file);
            w.write(Log.LOGMSG.toString());
            w.flush();
            w.close();
        }catch (IllegalMethodCallException | IOException e) {
            e.printStackTrace();
            GlobalUtilities.ExitProgram();
        }
    }
}

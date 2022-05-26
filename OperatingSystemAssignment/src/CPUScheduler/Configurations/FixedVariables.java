package CPUScheduler.Configurations;

import java.util.Locale;

public class FixedVariables {
    // Stataic Initiatlize Block
    static {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("Windows")){
            RootDirectory = System.getProperty("user.dir") + "\\CPUScheduler\\";
        }else if(os.contains("mac") || os.contains("nix") || os.contains("linux")){
            RootDirectory = System.getProperty("user.dir") + "/CPUScheduler/";
        }
    }

    // Exit Program
    public static void ExitProgram(){
        System.exit(0);
    }

    // Root Directory
    public static String RootDirectory;

    public static void ConsolePrintFileWriteParellel(Object msg){
        System.out.println(msg);
    }

    public static void BreakConsole(int millisecond){
        try{
            Thread.sleep(millisecond);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

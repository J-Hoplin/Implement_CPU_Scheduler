package CPUScheduler.Configurations;

public class FixedVariables {

    // Exit Program
    public static void ExitProgram(){
        System.exit(0);
    }

    // Root Directory
    public static String RootDirectory = System.getProperty("user.dir") + "/src/CPUScheduler/";

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

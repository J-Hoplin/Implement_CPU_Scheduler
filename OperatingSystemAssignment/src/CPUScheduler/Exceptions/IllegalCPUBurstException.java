package CPUScheduler.Exceptions;

public class IllegalCPUBurstException extends Exception{
    private static final String DefaultMsg="Illeagal CPU burst value exception.Negative number can't be CPU burst value.";
    public IllegalCPUBurstException(){
        super(IllegalCPUBurstException.DefaultMsg);
    }
    public IllegalCPUBurstException(String msg){
        super(msg);
    }
}

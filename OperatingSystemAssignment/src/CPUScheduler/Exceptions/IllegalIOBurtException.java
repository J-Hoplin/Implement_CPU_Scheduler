package CPUScheduler.Exceptions;

public class IllegalIOBurtException extends Exception{
    private static final String DefaultMsg = "Illegal IO burst value exception. Negative number can't be IO burst value.";
    public IllegalIOBurtException(){super(IllegalIOBurtException.DefaultMsg);}
    public IllegalIOBurtException(String msg){super(msg);}
}

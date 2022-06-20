package CPUScheduler.Exceptions;

public class IllegalMethodCallException extends Exception{
    private static final String DefaultMsg = "Illegal Method call detected";
    public IllegalMethodCallException(){super(IllegalMethodCallException.DefaultMsg);}
    public IllegalMethodCallException(String msg){super(msg);}
}

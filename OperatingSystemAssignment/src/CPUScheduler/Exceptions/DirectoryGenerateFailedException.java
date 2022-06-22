package CPUScheduler.Exceptions;

public class DirectoryGenerateFailedException extends Exception{
    private static final String DefaultMsg = "Fail to generate directory.";
    public DirectoryGenerateFailedException(){super(DirectoryGenerateFailedException.DefaultMsg);}
    public DirectoryGenerateFailedException(String msg){super(msg);}
}

package CPUScheduler;

import CPUScheduler.Configurations.CommandMSGs;
import CPUScheduler.Configurations.FixedVariables;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandUtilities {

    private static void printManPage(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(FixedVariables.RootDirectory + "help.txt"));
            String line = "";
            while(true){
                try {
                    line = br.readLine();
                    if(line == null){
                        break;
                    }
                    System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("\n");
        } catch (FileNotFoundException e) {
            System.out.println("Can't find file 'help.txt'");
        }
        FixedVariables.ExitProgram();
    }

    public static void RaiseHelpCommand(String[] args){
        if(args.length == 0){
            System.out.println("No arguments found type --help for help");
            FixedVariables.ExitProgram();
        }else if(args[0].equals(CommandMSGs.helpCommand)){
            printManPage();
        }else if(args.length < 2){
            System.out.println("\n[ Parameter Error Occured ]\n");
            printManPage();
        }
    }

    // Overloading
    public static void RaiseHelpCommand(String msg){
        System.out.println(msg);
        printManPage();
    }
}

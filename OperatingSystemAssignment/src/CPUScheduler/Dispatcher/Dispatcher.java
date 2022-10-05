package CPUScheduler.Dispatcher;

import CPUScheduler.CPU.CPU;
import CPUScheduler.Logger.Log;
import CPUScheduler.Process.ProcessObjects;

public class Dispatcher {
    private static Dispatcher dispatcher;

    private Dispatcher(){};
    // Apply Dispatcher as Sigleton
    public static Dispatcher getInstance(){
        if(dispatcher == null){
            dispatcher = new Dispatcher();
        }
        return dispatcher;
    }

    // ContextSwitching
    public ProcessObjects ContextSwitching(CPU cpu, ProcessObjects processObjects){
        ProcessObjects endProcess = cpu.getProcess();
        Log.Logger("Context Switching Between " + endProcess.getPid() + " <-> " + processObjects.getPid());
        cpu.setProcess(processObjects);
        return endProcess;
    }

    public ProcessObjects ContextSwitching(CPU cpu){
        ProcessObjects p = cpu.getProcess();
        Log.Logger("Only Process Terminated : " + p.getPid() + " / Ready Queue Empty");
        cpu.clearCPUProcess();
        return p;
    }
}

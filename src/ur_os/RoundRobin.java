/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler{

    int q;
    int cont;
    boolean multiqueue;
    
    RoundRobin(OS os){
        super(os);
        q = 5;
        cont=0;
    }
    
    RoundRobin(OS os, int q){
        this(os);
        this.q = q;
    }

    RoundRobin(OS os, int q, boolean multiqueue){
        this(os);
        this.q = q;
        this.multiqueue = multiqueue;
    }
    

    
    void resetCounter(){
        cont=0;
    }
   
    @Override
    public void getNext(boolean cpuEmpty) {
        if (!cpuEmpty && !processes.isEmpty() && cont >= (q - 1)) {
            Process current = os.getProcessInCPU();
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, current);

            Process next = processes.remove(0);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, next);
            resetCounter();

        }else if (cpuEmpty && !processes.isEmpty()) {
            Process p = processes.remove(0);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
            resetCounter();
        } else {
            cont++;
        }
    }
    
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}

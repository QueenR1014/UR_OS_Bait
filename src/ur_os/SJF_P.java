/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author prestamour
 */
public class SJF_P extends Scheduler {

    SJF_P(OS os) {
        super(os);
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        if (!cpuEmpty) {
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
            addContextSwitch(); // <-- CPU process switched due to preemption
        }
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
            if (!cpuEmpty) {
                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
                addContextSwitch(); // <-- CPU process switched due to preemption
            }
    }
    
    

    @Override
    public void getNext(boolean cpuEmpty) {
        if (!processes.isEmpty() && cpuEmpty) {
            Process shortest = null;

            for (Process p : processes) {
                if (shortest == null) {
                    shortest = p;
                } else if (p.getRemainingTimeInCurrentBurst() < shortest.getRemainingTimeInCurrentBurst()) {
                    shortest = p;
                } else if (p.getRemainingTimeInCurrentBurst() == shortest.getRemainingTimeInCurrentBurst()) {
                    shortest = tieBreaker(shortest, p);
                }
            }

            if (shortest != null) {
                processes.remove(shortest);
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortest);
                addContextSwitch();  // <-- Count the context switch here
            }
        }
    }

}

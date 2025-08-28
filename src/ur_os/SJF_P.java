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
        }
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        
        if (!cpuEmpty) {
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
        }
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (cpuEmpty && !processes.isEmpty()) {
            Process menor_burst = null;


            for (Process p : processes) {
                if (menor_burst == null) {
                    menor_burst = p;
                } else if (p.getRemainingTimeInCurrentBurst() <= menor_burst.getRemainingTimeInCurrentBurst()) {
                    menor_burst = p;
                } else if (p.getRemainingTimeInCurrentBurst() == shortest.getRemainingTimeInCurrentBurst()) {
                    shortest = tieBreaker(shortest, p); 
                }

            }

       
            if (menor_burst != null) {
                processes.remove(menor_burst);
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, menor_burst);
            }
        }
    }
}

package ur_os;

import java.util.ArrayList;
import java.util.Arrays;
//Implementation non preemptive to avoid parent to child queue communication, also just relies 
//on quantum to liberate their cpu space.
public class PriorityQueue extends Scheduler {

    private int currentScheduler;
    private ArrayList<Scheduler> schedulers;

    // 1. Single priority queue (default)
    PriorityQueue(OS os) {
        super(os);
        this.schedulers = new ArrayList<>();
        this.currentScheduler = -1;

        Scheduler rr = new RoundRobin(os, 5);
        schedulers.add(rr);
        currentScheduler = 0;
    }

    // 2. Multiple queues, default thresholds
    PriorityQueue(OS os, Scheduler... s) {
        this(os);
        schedulers.addAll(Arrays.asList(s));
        this.currentScheduler = (s.length > 0) ? 0 : -1;
    }

    @Override
    public void addProcess(Process p) {
        int prio = p.getPriority();

        if (prio > schedulers.size() - 1) {
            prio = schedulers.size() - 1;
        }

        Scheduler s = schedulers.get(prio);
        s.addProcess(p);

        if (os.isCPUEmpty()) {
            getNext(true);
        }
    }

    int defineCurrentScheduler() {
        int topPriority = -1;
        int i = 0;

        while (i < schedulers.size()) {
            Scheduler s = schedulers.get(i);
            if (!s.processes.isEmpty()) {
                topPriority = i;
                break;
            }
            i++;
        }
        return topPriority;
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (!cpuEmpty) return;

        int topPrio = defineCurrentScheduler();

        if (topPrio == -1) {
            return;
        }

        Scheduler top = schedulers.get(topPrio);
        top.getNext(true);
        currentScheduler = topPrio;
    }

    public int getIndexOf(Scheduler s) {
        return schedulers.indexOf(s);
    }

    public int getTopPriority() {
        return defineCurrentScheduler();
    }

    @Override
    public void newProcess(boolean cpuEmpty) {}

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {}
}

package ur_os;

public class SJF_NP extends Scheduler {

    SJF_NP(OS os) {
        super(os);
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (!processes.isEmpty() && cpuEmpty) {
            Process shortest = null;

            for (Process p : processes) {
                if (shortest == null) {
                    shortest = p;
                } else if (p.getBurstTime() < shortest.getBurstTime()) {
                    shortest = p; // compare total burst time
                } else if (p.getBurstTime() == shortest.getBurstTime()) {
                    shortest = tieBreaker(shortest, p);
                }
            }

            if (shortest != null) {
                processes.remove(shortest);
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortest);
                addContextSwitch(); // CPU got a new process
            }
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        if (cpuEmpty) {
            getNext(true); // schedule immediately if CPU is empty
        }
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        if (cpuEmpty) {
            getNext(true); // schedule immediately if CPU is empty
        }
    }
}

package ur_os;

/**
 * Round Robin scheduler designed to be used inside a preemptive PriorityQueue
 * Checks for higher-priority queues before loading processes.
 */
public class RRPriority extends Scheduler {

    private int q;          // time quantum
    private int counter;    // current quantum counter

    public RRPriority(OS os, int q) {
        super(os);
        this.q = q;
        this.counter = 0;
    }

    public RRPriority(OS os) {
        this(os, 5); // default quantum
    }

    private void resetCounter() {
        counter = 0;
    }

    @Override
    public void getNext(boolean cpuEmpty) {

        // Check if a higher-priority queue has a process
        if (os.rq.s instanceof PriorityQueue pq) {
            int topPrio = pq.defineCurrentScheduler();
            int myPrio = pq.schedulers.indexOf(this);

            // If not top priority, do nothing
            if (myPrio > topPrio) return;
        }

        // If CPU is empty, load next process from this queue
        if (cpuEmpty && !processes.isEmpty()) {
            Process p = processes.remove(0);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
            resetCounter();
            addContextSwitch();
            return;
        }

        // If CPU is busy and quantum expired, preempt
        if (!cpuEmpty && !processes.isEmpty() && counter >= (q - 1)) {
            Process running = os.getProcessInCPU();
            // Move running process back to queue
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, running);
            addContextSwitch();

            // Load next process from this queue
            Process next = processes.remove(0);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, next);
            resetCounter();
            addContextSwitch();
            return;
        }

        // Otherwise, increment quantum counter
        counter++;
    }

    @Override
    public void addProcess(Process p) {
        processes.add(p);
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        // Non-preemptive on process creation
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        // Non-preemptive on IO return
    }
}

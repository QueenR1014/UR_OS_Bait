package ur_os;

public class SJF_Predicted extends Scheduler {

    private double alpha = 0.5;
    private double[] predictions; // array to save predictions for each process

    SJF_Predicted(OS os) {
        super(os);
        predictions = new double[100]; // suppose max 100 processes
        for (int i = 0; i < predictions.length; i++) {
            predictions[i] = -1; // -1 means not set yet
        }
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (!processes.isEmpty() && cpuEmpty) {
            Process shortest = null;

            for (Process p : processes) {
                int pid = p.getPid(); // assume process has an ID

                // if prediction not set, use burst time
                if (predictions[pid] == -1) {
                    predictions[pid] = p.getBurstTime();
                }

                if (shortest == null) {
                    shortest = p;
                } else if (predictions[pid] < predictions[shortest.getPid()]) {
                    shortest = p;
                } else if (predictions[pid] == predictions[shortest.getPid()]) {
                    shortest = tieBreaker(shortest, p);
                }
            }

            if (shortest != null) {
                processes.remove(shortest);
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortest);
                addContextSwitch();
            }
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        if (cpuEmpty) {
            getNext(true);
        }
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        if (cpuEmpty) {
            getNext(true);
        }
    }

    // update prediction after process finishes a burst
    public void updatePrediction(Process p, int actualBurst) {
        int pid = p.getPid();
        if (predictions[pid] == -1) {
            predictions[pid] = actualBurst;
        } else {
            predictions[pid] = alpha * actualBurst + (1 - alpha) * predictions[pid];
        }
    }
}

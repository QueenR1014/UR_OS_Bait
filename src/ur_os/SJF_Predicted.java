package ur_os;


import java.util.HashMap;

public class SJF_Predicted extends Scheduler {
    
    private static final double ALPHA = 0.5; // Factor de predicción
    private static final double DEFAULT_TAU = 5.0; // Predicción inicial
    private HashMap<Process, Double> predictions; // Guardar predicciones por proceso
    
    public SJF_Predicted(OS os) {
        super(os);
        predictions = new HashMap<>();
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (processes.isEmpty()) return;

        Process next = processes.get(0);
        for (Process p : processes) {
            if (getPrediction(p) < getPrediction(next)) {
                next = p;
            } else if (getPrediction(p) == getPrediction(next)) {
                next = tieBreaker(p, next);
            }
        }

        if (cpuEmpty) {
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, removeProcess(next));
            addContextSwitch();
        } else {
            Process current = os.getProcessInCPU();
            if (current != null && getPrediction(next) < getPrediction(current)) {
                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, removeProcess(next));
                addContextSwitch();
            }
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        getNext(cpuEmpty);
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        getNext(cpuEmpty);
    }

    
    private double getPrediction(Process p) {
        
        return predictions.getOrDefault(p, DEFAULT_TAU);
    }

   
    public void updatePrediction(Process p, int lastBurst) {
        double oldTau = getPrediction(p);
        double newTau = ALPHA * lastBurst + (1 - ALPHA) * oldTau;
        predictions.put(p, newTau);
    }
}

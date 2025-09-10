/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author prestamour
 */

//Implementation is preemptive
public class PriorityQueue extends Scheduler{

    private int currentScheduler;
    private ArrayList<Scheduler> schedulers;
    
    //----------------- 1. Single priority queues
    PriorityQueue(OS os) {
        super(os);
        this.schedulers = new ArrayList<>();
        //this.threshold = new ArrayList<>();
        //this.starvationCounter = new ArrayList<>();
        this.currentScheduler = -1;

        // default: one RR
        Scheduler rr = new RoundRobin(os, 5);
        schedulers.add(rr);
        /*threshold.add(10);
        starvationCounter.add(0);*/
        currentScheduler = 0; //default value for single queue
    }
    
    //----------------- 2. Multiple queues, default thresholds
    PriorityQueue(OS os, Scheduler... s) {
        this(os); // initialize lists
        schedulers.addAll(Arrays.asList(s));

        /*int defaultStarvation = 10;
        for (int i = 0; i < s.length; i++) {
            threshold.add(defaultStarvation);
            starvationCounter.add(0);
        }*/

        this.currentScheduler = (s.length > 0) ? 0 : -1;
    }

    //----------------- 3. Multiple queues, specified thresholds
    /*PriorityQueue(OS os, ArrayList<Scheduler> s, ArrayList<Integer> t){
        super(os);
        //check every queue has it's starvation limit
        if(s.size() != t.size()){
            throw new IllegalArgumentException("Ammount of schedulers not equal to ammount of thresholds");
        }
        this.schedulers = s;
        this.threshold = t;
        this.starvationCounter = new ArrayList<>();
        //initialize counters
        for(int i = 0; i < t.size();i++){
            starvationCounter.add(0);
        }

        this.currentScheduler = (s.size() > 0) ? 0: 1;
    }
    */
    
    @Override
    public void addProcess(Process p){
        int prio = p.getPriority();

        if(prio > schedulers.size() - 1){
            //Go to last scheduler 
            prio = schedulers.size() - 1;
        }
        //Let the priority be the queue a process is in
        Scheduler s = schedulers.get(prio);
        
        // Check if current process preempts the loaded process in cpu
        if(!os.isCPUEmpty()){
            Process running = os.getProcessInCPU();
            if(running != null){
                //smaller index => higher priority
                if(prio < running.getPriority()){
                    //No comentar lo de abajo si buggea
                    //s.removeProcess(p);
                    
                    os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, running);
                    addContextSwitch(); //context switch for interruption
                    
                    os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
                    addContextSwitch();
                }
            }
        }

        //if no preemption needed add it to the RQ
        s.addProcess(p);
        
        //schedule if cpu empty
        if(os.isCPUEmpty()){
            getNext(true);
        }
    }
    
    int defineCurrentScheduler(){
        //This method is suggested to help you find the scheduler that should be the next in line to provide processes... perhaps the one with process in the queue?
        
        int topPriority = -1; //function will return -1 if all queues are empty
        int i = 0;

        while(i < schedulers.size()){
            Scheduler s = schedulers.get(i);
            if(!s.processes.isEmpty()){
                topPriority = i;
                break;
            }
            i++;
        }
        return topPriority;
    }
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
        //Define if any higher priority queue is loading processes 
        int topPrio = defineCurrentScheduler();

        if(topPrio == -1){
            //all queues empty, no process has to be loaded in cpu
            return;
        }
        
        //If CPU is empty, load the highest priority queue
        if(cpuEmpty){
            Scheduler top = schedulers.get(topPrio);
            top.getNext(true);
            currentScheduler = topPrio;
            return;
        }

        //If CPU is not empty, check if we should preempt
        Process running = os.getProcessInCPU();
        int runningPrio = running.getPriority();

        if(topPrio < runningPrio){
            //the next process has a higher priority
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, running);
            addContextSwitch();

            Scheduler top = schedulers.get(topPrio);
            top.getNext(true);
            currentScheduler = topPrio;
        }else{
            //No higher priority than current process -> let current queue handle
            Scheduler s = schedulers.get(runningPrio);
            s.getNext(cpuEmpty);
            currentScheduler = runningPrio;
        }
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}

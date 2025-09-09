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
    }
    
    void defineCurrentScheduler(){
        //This methos is suggested to help you find the scheduler that should be the next in line to provide processes... perhaps the one with process in the queue?
    }
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
        //Suggestion: now that you know on which scheduler a process is, you need to keep advancing that scheduler. If it a preemptive one, you need to notice the changes
        //that it may have caused and verify if the change is coherent with the priority policy for the queues.
        //Suggestion: if the CPU is empty, just find the next scheduler based on the order and the existence of processes
        //if the CPU is not empty, you need to define that will happen with the process... if it fully preemptive, and there are process pending in higher queue, does the
        //scheduler removes a process from the CPU or does it let it finish its quantum? Make this decision and justify it.
  
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}

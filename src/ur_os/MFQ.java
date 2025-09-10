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
public class MFQ extends Scheduler{

    private int currentScheduler;
    
    private ArrayList<Scheduler> schedulers;
    //This may be a suggestion... you may use the current sschedulers to create the Multilevel Feedback Queue, or you may go with a more tradicional way
    //based on implementing all the queues in this class... it is your choice. Change all you need in this class.
    
    MFQ(OS os){
        super(os);
        currentScheduler = -1;
        schedulers = new ArrayList<>();
    }
    
    MFQ(OS os, Scheduler... s){ //Received multiple arrays
        this(os);
        schedulers.addAll(Arrays.asList(s));
        if(!schedulers.isEmpty())
            currentScheduler = 0;
    }
        
    @Override
    public void addProcess(Process p){
       //Overwriting the parent's addProcess(Process p) method may be necessary in order to decide what to do with process coming from the CPU.
       if(!schedulers.isEmpty()){
           schedulers.get(0).addProcess(p);
           defineCurrentScheduler();
           addContextSwitch();
       }
    }
    
    void defineCurrentScheduler(){
        //This methos is siggested to help you find the scheduler that should be the next in line to provide processes... perhaps the one with process in the queue?
        currentScheduler = -1;
        for(int i=0; i<schedulers.size(); i++){
            if(!schedulers.get(i).isEmpty()){
                currentScheduler = i;
                break;
            }
        }
    }
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
        //Suggestion: now that you know on which scheduler a process is, you need to keep advancing that scheduler. If it a preemptive one, you need to notice the changes
        //that it may have caused and verify if the change is coherent with the priority policy for the queues.
        if(currentScheduler==1){
            defineCurrentScheduler();
        }
        if(currentScheduler!=-1){
            schedulers.get(currentScheduler).getNext(cpuEmpty);
            addContextSwitch();
            defineCurrentScheduler();
        }
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {
        defineCurrentScheduler();
        if(currentScheduler!=-1){
            schedulers.get(0).newProcess(cpuEmpty);
            addContextSwitch();
        }
    } 

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        defineCurrentScheduler();
        if(currentScheduler!=-1){
            schedulers.get(0).IOReturningProcess(cpuEmpty);
            addContextSwitch();
        }
    }
    public void downgradeProcess(Process p, int currentLevel) {
        if (currentLevel < schedulers.size() - 1) {
            schedulers.get(currentLevel + 1).addProcess(p);
            addContextSwitch();
        } else {
            schedulers.get(currentLevel).addProcess(p);
            addContextSwitch();
        }
        defineCurrentScheduler();
    }

}

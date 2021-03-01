/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.Collection;
import javax.annotation.Resource;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

/**
 *
 * @author Yuxin
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    @Resource 
    private SessionContext sessionContext;

    
    
    public EjbTimerSessionBean() {
    }
    
    @Override
    public void createTimer(long duration) {
        TimerService timerService = sessionContext.getTimerService();
        
        // Programmatic single action
        timerService.createSingleActionTimer(duration, new TimerConfig("createSingleActionTimer", true));
    }
    
    @Override
    public void cancelTimers(){
        TimerService timerService = sessionContext.getTimerService();
        Collection<Timer> timers = timerService.getTimers();
        
        for(Timer timer:timers) {
            if(timer.getInfo() != null) {
                if(timer.getInfo().toString().equals("createSingleActionTimer")) {
                    try {
                        System.out.println("********** EjbTimerSession.cancelTimers(): Cancelling: " + timer.getInfo().toString());
                        timer.cancel();
                        System.out.println("********** EjbTimerSession.cancelTimers(): Cancelled: " + timer.getInfo().toString());
                    }
                    catch(NoSuchObjectLocalException ex) {
                        System.err.println("********** EjbTimerSession.cancelTimers(): ERROR CANCELING, timer already cancelled or does not exist");
                    }
                }
            }
        }
    }
    
    
}

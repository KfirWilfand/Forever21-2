package server.controllers;

import java.lang.System.Logger.Level;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class AutomaticFunctionsController {

		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	    volatile boolean isStopIssued;

		private static AutomaticFunctionsController instance;
	    
	    private AutomaticFunctionsController(){}
	    
	    public static AutomaticFunctionsController getInstance(){
	        if(instance == null){
	            instance = new AutomaticFunctionsController();
	        }
	        return instance;
	    }
	    
	    public void startExecutionAt(int targetHour, int targetMin, int targetSec)
	    {
	        Runnable taskWrapper = new Runnable(){

	            @Override
	            public void run() 
	            {
	                try {
						AutomaticFunctions.checkStudentsGraduate();
						AutomaticFunctions.checkLatesInReturns();
						AutomaticFunctions.remainderOneDayBeforeReturns();
						AutomaticFunctions.moveToTheNextSubscriberInQueue();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	                //startExecutionAt(targetHour, targetMin, targetSec);
	            }

	        };
	        long delay = computeNextDelay(targetHour, targetMin, targetSec);
	        executorService.schedule(taskWrapper, delay, TimeUnit.SECONDS);
	    }

	    private long computeNextDelay(int targetHour, int targetMin, int targetSec) 
	    {
	        LocalDateTime localNow = LocalDateTime.now();
	        ZoneId currentZone = ZoneId.systemDefault();
	        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
	        ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
	        if(zonedNow.compareTo(zonedNextTarget) > 0)
	            zonedNextTarget = zonedNextTarget.plusDays(1);

	        Duration duration = Duration.between(zonedNow, zonedNextTarget);
	        return duration.getSeconds();
	    }

	    public void stop()
	    {
	        executorService.shutdown();
	        try {
	            executorService.awaitTermination(1, TimeUnit.DAYS);
	        } catch (InterruptedException ex) {
	            //Logger.getLogger(MyTaskExecutor.class.getName()).log(Level.SEVERE, null, ex);
	        	System.out.println("error at execution");
	        }
	    }
}

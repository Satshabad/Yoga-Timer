package net.satshabad.android.yogatimer;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Exercise implements Serializable
{
   String name;
   long time;
   long pauseTime;
   
   public Exercise(String s, long t)
   {
      name = s;
      time = t;
   }

   public String getName()
   {
      return name;
   }
   
   public String timeToString(long t){
      long totalSeconds = t/1000;
      String seconds = Integer.toString((int)(totalSeconds % 60));
      String minutes = Integer.toString((int)((totalSeconds % 3600) / 60));
      String hours = Integer.toString((int)(totalSeconds / 3600));
      for (int i = 0; i < 2; i++) {
      if (seconds.length() < 2) {
      seconds = "0" + seconds;
      }
      if (minutes.length() < 2) {
      minutes = "0" + minutes;
      }
      if (hours.length() < 2) {
      hours = "0" + hours;
      }
      }
       if (hours.equals("00")){

          return "" + minutes + ":" + seconds;
       }
          

       
      return "" + hours + ":" + minutes + ":" + seconds;
      
      
   }

   public long getTime()
   {
      return time;
   }
   
   public void setPauseTime(long t){
      pauseTime = t;
   }

   public long getPauseTime()
   {
      
      return pauseTime;
   }
}


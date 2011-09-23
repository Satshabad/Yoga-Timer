package net.satshabad.android.yogatimer;

import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountDown extends CountDownTimer
{
   private TextView timeDisplay;
   private static boolean exists = false;
   
   public static MyCountDown getIntsance(long millisInFuture, long countDownInterval, Exercise ex){
      if (exists)
         return null;
      
      return new MyCountDown(millisInFuture, countDownInterval, ex);
         
   }
   private MyCountDown(long millisInFuture, long countDownInterval, Exercise ex)
   {
      super(millisInFuture, countDownInterval);
   }

   @Override
   public void onFinish()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void onTick(long millisUntilFinished)
   {

      
   }

}

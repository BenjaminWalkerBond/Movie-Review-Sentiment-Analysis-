package assign3;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner{
  /**
  TestRunner class specification
  @author Bond
  */

  public static void main(String[] args)
  {
      Result result = JUnitCore.runClasses(ReviewHandlerTest.class);
      if(result.getFailures().size() == 0)
      {
          System.out.println("All tests successful !!!");
      }
      else
      {
          System.out.println("Number of failed test cases="+result.getFailures().size());
          for (Failure failure : result.getFailures())
              System.out.println(failure.toString());
      }
  }

}

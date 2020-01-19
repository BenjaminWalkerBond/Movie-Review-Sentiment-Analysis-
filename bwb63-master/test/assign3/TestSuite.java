package assign3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import junit.framework.JUnit4TestAdapter;


@RunWith(Suite.class)
@Suite.SuiteClasses({ ReviewHandlerTest.class})

public class TestSuite
{
   public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestSuite.class);
   }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
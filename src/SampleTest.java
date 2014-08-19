import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ScreenRecorderRule;


/**
 * This is a sample test class that will output videos of Selenium WebDriver execution on Fail and
 * Error results.
 */
@RunWith( JUnit4.class )
public class CreateAdHocTaskExample
{

   @Rule public ScreenRecorderRule iRule = new ScreenRecorderRule( false, true, true );

   /**
   * This is a sample test case
   */
   @Test public void testCreateAdHocTask()
   {
      Assert.fail( "Not yet implemented" );
   }
}

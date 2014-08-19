import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
 
 
/**
 * JUnit Rule to record the test execution to an AVI file, depending on the test result status
 * (success, fail, or error)
 *
 * @author  jclarkin
 */
public class ScreenRecorderRule
   implements TestRule
{
 
   private final boolean iRecordError;
   private final boolean iRecordFailure;
   private final boolean iRecordSuccess;
 
   /**
   * Creates a new {@linkplain ScreenRecorderRule} object.
   *
   * @param  aRecordSuccess  If true, will record test on success
   * @param  aRecordFailure  If true, will record test on failure
   * @param  aRecordError    If true, will record test on error
   */
   public ScreenRecorderRule( boolean aRecordSuccess, boolean aRecordFailure,
      boolean aRecordError
      )
   {
      super();
      this.iRecordSuccess = aRecordSuccess;
      this.iRecordFailure = aRecordFailure;
      this.iRecordError = aRecordError;
   }
 
   /**
   * {@inheritDoc}
   */
   @Override public Statement apply( Statement aBase, Description aDescription )
   {
      String lFilename = aDescription.getClassName() + "." + aDescription.getMethodName();
 
      return new ScreenRecorderStatement(
            aBase,
            lFilename,
            iRecordSuccess,
            iRecordFailure,
            iRecordError
            );
   }
 
   /**
    * Handles execution of test and potentially records the execution as well
    */
   public class ScreenRecorderStatement
      extends Statement
   {
 
      private final Statement iBase;
      private final String iFilename;
      private final boolean iRecordError;
      private final boolean iRecordFailure;
      private final boolean iRecordSuccess;
 
      /**
      * Creates a new {@linkplain ScreenRecorderStatement} object.
      *
      * @param  aBase           The base test statement
      * @param  aFilename       The prefix to use for the recording file name
      * @param  aRecordSuccess  If true, will record test on success
      * @param  aRecordFailure  If true, will record test on failure
      * @param  aRecordError    If true, will record test on error
      */
      public ScreenRecorderStatement(
         Statement aBase,
         String aFilename,
         boolean aRecordSuccess,
         boolean aRecordFailure,
         boolean aRecordError
         )
      {
         this.iRecordSuccess = aRecordSuccess;
         this.iRecordFailure = aRecordFailure;
         this.iRecordError = aRecordError;
         this.iBase = aBase;
         this.iFilename = aFilename;
      }
 
      /**
      * Executes the test and saves a recording depending on the test result status
      *
      * @throws  Throwable  If an error or failure occurs
      */
      @Override public void evaluate()
         throws Throwable
      {
         TestRecorder lRecorder = new TestRecorder( iFilename );
         boolean lSucceeded = false;
         boolean lFailed = false;
         boolean lError = false;
 
         try {
            lRecorder.start();
            iBase.evaluate();
            lSucceeded = true;
         } catch ( Throwable e ) {
            if ( e instanceof AssertionError ) {
               lFailed = true;
            } else {
               lError = true;
            }
 
            throw e;
         } finally {
            lRecorder.stop();
 
            boolean lKeepFile =
               ( lSucceeded && iRecordSuccess ) || ( lFailed && iRecordFailure ) ||
                     ( lError && iRecordError );
 
            // If not keeping the file, delete it
            if ( !lKeepFile ) {
               lRecorder.delete();
            }
         }
      }
   }
}
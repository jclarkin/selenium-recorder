import java.io.File;
import java.io.IOException;
 
import java.text.SimpleDateFormat;
 
import java.util.Date;
 
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import static org.monte.media.VideoFormatKeys.*;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
 
import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
 
 
/**
 * A customized instance of the ScreenRecorder that allows the filename prefix to be specified, as
 * well as functionality to delete the recording
 */
public class TestRecorder
   extends ScreenRecorder
{
 
   private String iFilename;
   private File iMovieFile;
 
   /**
   * Creates a new {@linkplain TestRecorder} object.
   *
   * @param   aFilename  The Prefix filename to use
   *
   * @throws  IOException   If an error occurs with saving the recording
   * @throws  AWTException  If an error occurs in accessing the video screen
   */
   public TestRecorder( String aFilename )
      throws IOException, AWTException
   {
      super( buildConfig(), buildFileFormat(), buildScreenFormat(), buildMouseFormat(), null );
 
      iFilename = aFilename;
   }
 
   /**
   * Delete the recorded file
   *
   * @return  True if succeeded to delete
   */
   public boolean delete()
   {
      boolean lDeleted = false;
      if ( State.DONE == this.getState() ) {
         lDeleted = iMovieFile.delete();
      }
 
      return lDeleted;
   }
 
 
   /**
   * {@inheritDoc}
   */
   @Override protected File createMovieFile( Format aFileFormat )
      throws IOException
   {
      if ( !movieFolder.exists() ) {
         movieFolder.mkdirs();
      } else if ( !movieFolder.isDirectory() ) {
         throw new IOException( "\"" + movieFolder + "\" is not a directory." );
      }
 
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd 'at' HH.mm.ss" );
 
      iMovieFile =
         new File(
            movieFolder,  //
            iFilename + " " + lDateFormat.format( new Date() ) + "." +
            Registry.getInstance().getExtension( aFileFormat )
            );
 
      return iMovieFile;
   }
 
 
   /**
   * Generate the Graphics configuration used to access the video screen
   *
   * @return  Graphics configuration
   */
   private static GraphicsConfiguration buildConfig()
   {
      return GraphicsEnvironment.getLocalGraphicsEnvironment()
               .getDefaultScreenDevice()
               .getDefaultConfiguration();
   }
 
 
   /**
   * Generate the fileformat for the recording to be saved
   *
   * @return  The fileformat of the recording
   */
   private static Format buildFileFormat()
   {
      return new Format( MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI );
   }
 
 
   /**
   * Generate the Mouse accessor details for recording
   *
   * @return  Mouse accessor configuration
   */
   private static Format buildMouseFormat()
   {
      return new Format(
            MediaTypeKey,
            MediaType.VIDEO,
            EncodingKey,
            "black",
            FrameRateKey,
            Rational.valueOf( 30 )
            );
   }
 
 
   /**
   * Generate the Screen accessor details for recording
   *
   * @return  Screen accessor configuration
   */
   private static Format buildScreenFormat()
   {
      return new Format(
            MediaTypeKey,
            MediaType.VIDEO,
            EncodingKey,
            ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
            CompressorNameKey,
            ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
            DepthKey,
            24,
            FrameRateKey,
            Rational.valueOf( 15 ),
            QualityKey,
            1.0f,
            KeyFrameIntervalKey,
            15 * 60
            );
   }
}
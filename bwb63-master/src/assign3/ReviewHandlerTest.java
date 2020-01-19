package assign3;

import java.io.FileNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;
import java.io.ByteArrayOutputStream;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ReviewHandlerTest{
  /**
    ReviewHandlerTest class specification
    @author Bond
  */
  private ReviewHandler reviewHandler;
  private final static String NEWLINE = System.getProperty("line.separator");
  /**
    Set up Environment
  */
  @Before
  public void setUp(){

    reviewHandler = new ReviewHandler();

    System.out.println("Set up environment.");
  }
  /**
    Clear the environment
  */
  @After
  public void tearDown(){
    reviewHandler.database = new HashMap<Integer, MovieReview>();
    System.out.println("Cleared Environment.");
  }
  /**
    Scenario: loadReviews() is passed a directory path with 500 reviews
  */
  @Test
  public void loadReviewsDirectoryTest(){
    String negativeWordsDirectory = "/Users/michaelrwalker/Documents/RDCConnections/txstate_git_repo/bwb63/Data/Movie-reviews/neg";

    reviewHandler.loadReviews(negativeWordsDirectory, 0);

    assertTrue("Check that the database contains exactly 500 entries", reviewHandler.database.size() == 500);
  }
  /**
    Scenario: loadReviews() is passed a file path with 1 review
  */
  @Test
  public void loadReviewsFileTest(){
    String negativeWordsFile = "/Users/michaelrwalker/Documents/RDCConnections/txstate_git_repo/bwb63/Data/Movie-reviews/neg/0_3.txt";

    reviewHandler.loadReviews(negativeWordsFile, 0);

    assertTrue("Check that the database contains exactly 1 entry", reviewHandler.database.size() == 1);
  }
  /**
    Scenario: Input file path does not exist
    Exception is expected as it is not handled
  */
  @Test(expected = NullPointerException.class)
  public void loadReviewsNotExistFilePath(){

   reviewHandler.loadReviews("./NoSuchFileOrFolder", 0);
   assertEquals(0, reviewHandler.database.size());
  }
  /**
    Scenario: classify a positive, negative, and nuetral review
  */
  @Test
  public void classifyReviewTest(){
      MovieReview mock1 = new MovieReview(0, "mock1", 0);
      MovieReview mock2 = new MovieReview(1, "mock2", 1);
      MovieReview mock3 = new MovieReview(2, "mock1", 2);

      assertEquals("Check if mock1 was classified as negative.", 0, reviewHandler.classifyReview(mock1));
  }
  /**
    Scenario: A movie review of each type is created and deleted.
  */
  @Test
  public void deleteReviewTest(){
    MovieReview mock1 = new MovieReview(0, "mock1", 0);
    reviewHandler.database.put(mock1.getId(), mock1);
    reviewHandler.deleteReview(mock1.getId());
    assertTrue("Check if the review was deleted", reviewHandler.database.size() == 0);

    MovieReview mock2 = new MovieReview(0, "mock1", 1);
    reviewHandler.database.put(mock1.getId(), mock1);
    reviewHandler.deleteReview(mock1.getId());
    assertTrue("Check if the review was deleted", reviewHandler.database.size() == 0);

    MovieReview mock3 = new MovieReview(0, "mock1", 2);
    reviewHandler.database.put(mock1.getId(), mock1);
    reviewHandler.deleteReview(mock1.getId());
    assertTrue("Check if the review was deleted", reviewHandler.database.size() == 0);
  }
  /**
    Scenario: delete a review from the database when it is empty
  */
  @Test
  public void deleteReviewWhenDatabaseEmpty(){
    assertEquals(null, reviewHandler.database.remove(0));
  }
  /**
    Scenario: readReview is passed a directory instead of a file
  */
  public void readReviewPassedDirectoryPath(){
    String negativeWordsDirectory = "/Users/michaelrwalker/Documents/RDCConnections/txstate_git_repo/bwb63/Data/Movie-reviews";
    try{
      reviewHandler.readReview("/Users/michaelrwalker/Documents/RDCConnections/txstate_git_repo/bwb63/Data/Movie-reviews", 0);

      fail("expected IOException");
    }catch(IOException e){
      // do nothing, expected
    }
  }
  /**
    Scenario: readReview is given a file path that does not exist
  */
  public void readReviewNotExistFilePath(){
    try{
      MovieReview mock = new MovieReview(0, "", 0);
      mock = reviewHandler.readReview("./NoSuchFile", 0);

      fail("expected IOException");
    }catch(IOException e){
      // do nothing, expected
    }
  }
  /**
    Scenario: The user presses 0 and the program must exit and save the database.
    The database contains three mock movie review objects
  */
  @SuppressWarnings("unchecked")
  @Test
  public void saveSerialDBTest(){

    MovieReview mock1 = new MovieReview(0, "mock1", 0);
    MovieReview mock2 = new MovieReview(0, "mock1", 0);
    MovieReview mock3 = new MovieReview(0, "mock1", 0);

    reviewHandler.database.put(mock1.getId(), mock1);
    reviewHandler.database.put(mock2.getId(), mock2);
    reviewHandler.database.put(mock3.getId(), mock3);

    reviewHandler.saveSerialDB();

    InputStream file = null;
    InputStream buffer = null;
    ObjectInput input = null;
    try {
        file = new FileInputStream("database.ser");
        buffer = new BufferedInputStream(file);
        input = new ObjectInputStream(buffer);

        reviewHandler.database = (Map<Integer, MovieReview>)input.readObject();

        input.close();
    } catch (IOException | ClassNotFoundException | ClassCastException e) {
        System.err.println(e.toString());
        e.printStackTrace();
    }

    assertTrue("Check if the database contains mock1",reviewHandler.database.containsKey(mock1.getId()));
    assertTrue("Check if the database contains mock2",reviewHandler.database.containsKey(mock2.getId()));
    assertTrue("Check if the database contains mock3",reviewHandler.database.containsKey(mock3.getId()));

  }
  /**
    Scenario: Add three movie reviews to the database, save the database, and exit the program.
    Then reload the database on next program run
  */
  @Test
  public void loadSerialDBTest(){
    MovieReview mock1 = new MovieReview(0, "mock1", 0);
    MovieReview mock2 = new MovieReview(1, "mock2", 1);
    MovieReview mock3 = new MovieReview(2, "mock3", 2);

    reviewHandler.database.put(mock1.getId(), mock1);
    reviewHandler.database.put(mock2.getId(), mock2);
    reviewHandler.database.put(mock3.getId(), mock3);

    OutputStream file = null;
    OutputStream buffer = null;
    ObjectOutput output = null;
    try {

        file = new FileOutputStream("database.ser");
        buffer = new BufferedOutputStream(file);
        output = new ObjectOutputStream(buffer);

        output.writeObject(reviewHandler.database);

        output.close();
    } catch (IOException ex) {
        System.err.println(ex.toString());
        ex.printStackTrace();
    }
    reviewHandler.database = null;

    reviewHandler.loadSerialDB();

    assertTrue("Check that the size of the database is three", reviewHandler.database.size() == 3);
    assertTrue("Check if the database contains mock1",reviewHandler.database.containsKey(mock1.getId()));
    assertTrue("Check if the database contains mock2",reviewHandler.database.containsKey(mock2.getId()));
    assertTrue("Check if the database contains mock3",reviewHandler.database.containsKey(mock3.getId()));

  }
  /**
    Scenario: insert new movie review into the database and search for it by id
  */
  @Test
  public void searchByIdTest(){
    MovieReview mock1 = new MovieReview(0, "mock1", 0);

    reviewHandler.database.put(mock1.getId(), mock1);
    assertNotNull("Check if the object was found", reviewHandler.searchById(0));
    assertNull("Check if, when given an id not in the database, returns null", reviewHandler.searchById(-1));
  }
  /**
    Checks whether the default search method of searchBySubstring
    works for all possible sentiment classifications and for
    all possible duplicate strings
  */
  @Test
  public void searchBySubstringTest(){
    // create mock reviews to check that searching in the positive, negative,
    // and nuetral categories all work the same
    MovieReview mock1 = new MovieReview(0, "mock1", 0);
    MovieReview mock2 = new MovieReview(1, "mock2", 1);
    MovieReview mock3 = new MovieReview(2, "mock3", 2);

    reviewHandler.database.put(mock1.getId(), mock1);
    reviewHandler.database.put(mock2.getId(), mock2);
    reviewHandler.database.put(mock3.getId(), mock3);
    // add movie reviews with duplicate text to ArrayList for check.
    assertEquals(mock1.getId(), reviewHandler.searchBySubstring("mock1").get(0).getId());
    assertEquals(mock2.getId(), reviewHandler.searchBySubstring("mock2").get(0).getId());
    assertEquals(mock3.getId(), reviewHandler.searchBySubstring("mock3").get(0).getId());
    // rh.loadReview(negativeWordsFile);
  }
  /**
    Scenario: Checks that movie reviews with duplicate text are added to the returned ArrayList
  */
  @Test
  public void searchBySubstringDuplicateTest(){
    // create mock reviews that contain the same text, and
    // some that contain the keyword with other text.

    List<MovieReview> mockList = new ArrayList<MovieReview>();
    MovieReview mock1 = new MovieReview(0, "duplicate", 0);
    MovieReview mock2 = new MovieReview(1, "duplicate", 1);

    mockList.add(mock1);
    mockList.add(mock2);

    // add all mock reviews to database
    reviewHandler.database.put(mock1.getId(), mock1);
    reviewHandler.database.put(mock2.getId(), mock2);

    assertThat(reviewHandler.searchBySubstring("duplicate"), hasItem(mock1));
    assertThat(reviewHandler.searchBySubstring("duplicate"), hasItem(mock2));

  }

}

package assign2;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.lang.ClassNotFoundException;
import sentiment.Sentiment;

/**
 *Review Handler Class specification
 * @author bond
 */
public class ReviewHandler extends AbstractReviewHandler{
  /**
   * Loads reviews from a given path. If the given path is a .txt file, then
   * a single review is loaded. Otherwise, if the path is a folder, all reviews
   * in it are loaded.
   * @param filePath The path to the file (or folder) containing the review(sentimentModel).
   * @param realClass The real class of the review (0 = Negative, 1 = Positive
   * 2 = Unknown).
   */
  public void loadReviews(String filePath, int realClass){
    try{
    // accuracy is the total realClass minus the total predictClass
    // divided by the total movie review objects.
    // either accurate or not.
    double accuracy = 0;
    int totalReviews = 0;
    File fP = new File(filePath);
    File[] directoryListing = fP.listFiles();
    if(directoryListing != null){
      for(File child: directoryListing){
        MovieReview review = readReview(child.getPath(), realClass);
        if(review.getRealPolarity() == review.getPredictedPolarity()){
        accuracy++;
        }
        //increment totalReviews.
        totalReviews++;
        //add the review to the database
        database.put(review.getId(), review);
      }//end for
    }else{
      MovieReview review = readReview(fP.getPath(), realClass);
      if(review.getRealPolarity() == review.getPredictedPolarity()){
      accuracy++;
      totalReviews++;
      }
      database.put(review.getId(), review);
    }
    accuracy = (accuracy / totalReviews) * 100;
    System.out.println("The accuracy is: " + accuracy);
    }catch(IOException e){
      System.out.println("File path not found.");
    }catch(NullPointerException n){
      System.out.println("File path not found.");
    }// end try/catch

  }// end loadReviews
  /**
   * Reads a single review file and returns it as a MovieReview object.
   * This method also calls the method classifyReview to predict the polarity
   * of the review.
   * @param reviewFilePath A path to a .txt file containing a review.
   * @param realClass The real class entered by the user.
   * @return a MovieReview object.
   * @throws IOException if specified file cannot be openned.
   */
  public MovieReview readReview(String reviewFilePath, int realClass) throws IOException {

    Scanner src = new Scanner(new FileReader(reviewFilePath));
    // Create an object to hold the reviewText
    String currString = "";
    // Create a file object
    File fP = new File(reviewFilePath);

    // Loop over the review text file, putting the text into a String
    while(src.hasNextLine()){
      currString = currString + src.nextLine();
    }
    // Remove occurences of html <br />
    currString.replaceAll("<br />", " ");

    // Read the text from the file
    MovieReview review = new MovieReview(fP.getName().hashCode(),
                         currString, realClass);
    review.setPredictedPolarity(classifyReview(review));
    src.close();
    return review;
  }

  /**
   * Deletes a review from the database, given its id.
   * @param id The id value of the review.
   */
  public void deleteReview(int id){
    database.remove(id);
  }
  /**
   * Loads review database.
   */
   @SuppressWarnings("unchecked")
  public void loadSerialDB(){
    try{

      // Create a file input stream to read the file and an object input stream
      // to read the object in the file.
      FileInputStream fis = new FileInputStream(DATA_FILE_NAME);
      ObjectInputStream ois = new ObjectInputStream(fis);

      // Downcast the object to a HashMap and assign it to the database
      // in order to load the database.
      database = (HashMap)ois.readObject();
      // Close the two streams
      ois.close();
      fis.close();
  }catch(IOException e){
    System.out.println("The database will not load.");
  }catch(ClassNotFoundException f){
    System.out.println("The database will not load.");
  }
  };
  /**
   * Searches the review database by id.
   * @param id The id to search for.
   * @return The review that matches the given id or null if the id does not
   * exist in the database.
   */
  public MovieReview searchById(int id){
    // If the database contains the parameter id, return the MovieReview
    // from the database, else, return null
    if(database.containsKey(id)){
      return database.get(id);
    }else{
      return null;
    }

  }
  /**
   * Searches the review database for reviews matching a given substring.
   * @param substring The substring to search for.
   * @return Return a list of review objects matching the search criterion.
   */
  public List<MovieReview> searchBySubstring(String substring){

    List<MovieReview> tempList = new ArrayList<MovieReview>();
      for(Map.Entry<Integer, MovieReview> entry: database.entrySet()){
        if(entry.getValue().getText().lastIndexOf(substring)>=0){
          tempList.add(entry.getValue());
        }
      }

    if(tempList.size() != 0){
      // Return a list of reviews that contain the substring
      return tempList;
    }else{
      // No review contains the substring
      return null;
    }
 }
 /**
  * The unique id of the movie review created from the file name
  */
 private static int ID;
}

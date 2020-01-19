import static java.lang.System.out;
import java.util.regex.Pattern;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.util.HashSet;
import java.io.FileNotFoundException;

/**
   A class used to process movie review files in a directory
   @author Benjamin Bond
*/
public class FileProcessor {
  private int totalReviews; //total reviews in a directory
  private final String ACTUAL_REVIEW_TYPES[] = {"Negative" , "Positive"}; //actual type of review
  /**
      Constructs a FileProcessor object and initializes totalReview to zero
  */
  FileProcessor(){
    this.totalReviews = 0;
  }
  /**
      Method to count the number of positive and negative words in a review
      @return the total reviews correctly classified
  */
  int countWords(HashSet<String> posWords, HashSet<String> negWords, File dir, int type){ //start countWords

    int totalReviewsCorrect=0;
    String review = "";
    File[] directoryListing;
    directoryListing = dir.listFiles();
    this.totalReviews = this.totalReviews + directoryListing.length;
    int counter =0;
    Sentiment[] currentSent = new Sentiment[1000];

    try{

      for(File child: directoryListing){ // start NEGATIVE DIRECTORY LOOP
         currentSent[counter]= new Sentiment(child.getName());
         FileReader cur = new FileReader(child);
         Scanner sc = new Scanner(cur);
         String[] currentReview;
         review=" ";

         while(sc.hasNext()){
           review = review + " " + sc.nextLine();
         }
         //replace all punctuation with empty strings, convert all upper case letters to lower case
         //and assign every word in the review to an index in current review.
         currentReview= review.replaceAll("\\p{Punct}","").toLowerCase().split("\\s+");
         //iterates of currentReview, incrementing the positiveWordCount field
         //or negativeWordCount field if either type of word is found in a review.
         for( int x = 0; x< currentReview.length; x++){
           if(posWords.contains(currentReview[x])){
             currentSent[counter].incrementPWC();
           }
           if(negWords.contains(currentReview[x])){
             currentSent[counter].incrementNWC();
           }
         }
         //update the score of the review in order to determine its reviewType field
         currentSent[counter].updateScore();
         //add one to totalReviewsCorrect if
         if(currentSent[counter].getScore() == type){
           totalReviewsCorrect++;
         }
         System.out.println( currentSent[counter] +  " Actual Type: " + ACTUAL_REVIEW_TYPES[type]);
         counter = counter +1;
         sc.close();
    } //end DIRECTORY LOOP

  }catch(FileNotFoundException ex){
      System.out.println("The file was not found.");
  }
    return totalReviewsCorrect;
} //end countWords
/**
   Getter
   @return value of the totalReviews field
*/
  int getTotalReviews(){
    return totalReviews;
  }
}

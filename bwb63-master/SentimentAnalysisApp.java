import static java.lang.System.out;
import java.util.regex.Pattern;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.util.HashSet;
/**
 * The main sentiment analysis application
 * @author Benjamin Bond
 */
public class SentimentAnalysisApp{
  /**
     Print total reviews and correctly classified reviews
	   @param args Main executable
  */
  public static void main(String args[]) throws Exception{
    //read command line arguments directly
    File posDir = new File(args[2]);
    File negDir = new File(args[3]);
    //create strings to hold command line arguments and manipulate file contents
    String currentFile, review = " ";
    String[] pWordsArray;
    String[] nWordsArray;
    HashSet<String> posWords = new HashSet<String>();
    HashSet<String> negWords = new HashSet<String>();
    int totalReviewsCorrect = 0;
    int totalReviews = 0;

    //initialize an input stream and scanner for that stream for positive-word.txt
    currentFile = args[0];
    FileReader fr = new FileReader(currentFile);
    Scanner src = new Scanner(fr);

    //loop through the file, create a string containing all pos words.
    while(src.hasNext()){
      String temp = src.nextLine();
        if(temp.startsWith(";")){
          review = review + " ";
        }else{
          review = review + " " + temp;
        }
    }
    src.close();
    pWordsArray = review.replaceAll("\\p{Punct}","").toLowerCase().split("\\s+");

    //Add all positive words to hash set
    for(int x = 0; x < pWordsArray.length; x++){
      posWords.add(pWordsArray[x]);
    }
    //initialize an input stream and scanner for that stream for negative-word.txt
    currentFile = args[1];
    fr = new FileReader(currentFile);
    src= new Scanner(fr);
    review = "";
    //loop through the file, create a list of all neg words.
    while(src.hasNext()){
      String temp = src.nextLine();
        if(temp.startsWith(";")){
          review = review + " ";
        }else{
          review = review + " " + temp;
        }
    }
    nWordsArray = review.replaceAll("\\p{Punct}","").toLowerCase().split("\\s+");
    //Add all negative words to hash set
    for(int x = 0; x < nWordsArray.length; x++){
      negWords.add(nWordsArray[x]);
    }
    src.close();

    //Check whether each and every review contains both positive and negative words,
    //computing a score for each review based on the amount of each type in each review.
    FileProcessor fp= new FileProcessor();
    totalReviewsCorrect= totalReviewsCorrect + fp.countWords(posWords, negWords, posDir, 1);
    totalReviewsCorrect= totalReviewsCorrect + fp.countWords(posWords, negWords, negDir, 0);
    System.out.println("Total Reviews: " + fp.getTotalReviews() + " Correctly Classified: " + totalReviewsCorrect);
  }//main function bracket
}//class bracket

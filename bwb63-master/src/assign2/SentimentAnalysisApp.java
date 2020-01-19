package assign2;

import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.regex.Pattern;
import java.lang.NumberFormatException;
/**
 * SentimentAnalysisApp Class Implmentation
    @author bond
 */
public class SentimentAnalysisApp{

    public static void main(String [] args) {

      // Create object
      ReviewHandler rh = new ReviewHandler();
      // Load database if it exists
      if( new File(rh.DATA_FILE_NAME).exists()){
        rh.loadSerialDB();
      }
      String[] totalInput;
      String choice3;
      String input;
      int choice = -1;
      int iD = 0;
      int realClass = -1;
      String fil = "";
      Scanner inp = new Scanner(System.in);
      //Display menu and process user selection
      do{
        try{
          System.out.println("0. Exit program.");
          System.out.println("1. Load new movie review collection (given a folder or a file path) .");
          System.out.println("2. Delete movie review from database (given its id) .");
          System.out.println("3. Search movie reviews in database by id or by matching a substring.");
          input = inp.nextLine();

          totalInput = input.split("\\s+");

          //if(totalInput.length<2 || )
          choice = Integer.parseInt(totalInput[0]);
          if(choice == 0){
          System.out.println("Exiting program.");
          // 1. Load new movie review collection (given a folder or a file path) .
          }else if(choice == 1){

            fil = totalInput[1];
            System.out.println("Please input the real class of the review(s): ");
            realClass = inp.nextInt();
            // consume the scanners next line
            inp.nextLine();
            System.out.println("Real class is: " + realClass);
            rh.loadReviews(fil, realClass);

          // 2. Delete movie review from database (given its id) .
          }else if(choice == 2){

            iD = Integer.parseInt(totalInput[1]);
            rh.deleteReview(iD);
            System.out.println("Deleted review successfully.");

          // 3. Search movie reviews in database by id or by matching a substring.
          }else if(choice == 3){

            System.out.println("Input A to search by ID or B to search by text");
            choice3 = inp.nextLine();
              if(choice3.equalsIgnoreCase("A")){

                System.out.println("Please enter an ID");
                int dummy = inp.nextInt();

                //consume the scanners next line
                inp.nextLine();
                System.out.println("dummy is: " + dummy);
                MovieReview check = rh.searchById(dummy);
                if(check == null){
                  System.out.println("We didn't find that movie review.");
                }else{
                  printTable(check);
                }
              }else if(choice3.equalsIgnoreCase("B")){

                System.out.println("Please enter some text from the MovieReview");
                List<MovieReview> check2= rh.searchBySubstring(inp.nextLine());
                  if(check2 == null){
                    System.out.println("We didn't find that movie review.");
                  }else{
                    System.out.println("We found your movie review(s).");
                    printTable(check2);
                  }
                //consume the scanners next line
              }else{
                throw new InputMismatchException(" ");
              }

            }else{
              System.out.println("Please enter an integer between 0 and 3");
          }

        // Start catch statements
        }catch(InputMismatchException e){
          System.out.println("You entered the wrong character or did not enter a character.");
        }catch(NumberFormatException n){
          System.out.println("Error: Please input in the form: choice filePath, ");
          System.out.println("choice ID, or simply choice for choice 0 and 3.");
        }catch(ArrayIndexOutOfBoundsException a){
          System.out.println("Error: Please input in the form: choice filePath, ");
          System.out.println("choice ID, or simply choice for choice 0 and 3.");
        }
      }while(choice != 0);


      rh.saveSerialDB();
      System.out.println("See you!");
        // MODIFY THIS TO ADD YOUR CODE.

    }
  /**
  * Prints out a single movie review in table format
  * @param review A single movie review
  */
  public static void printTable(MovieReview review){

        String width="";
        System.out.println("---------------------------------------------------------------------------");
        System.out.println(" Movie ID: " + review.getId() + " Review content: " + review.getText().substring(0,50));
        System.out.println(" Predicted Class: " + review.getPredictedPolarity() + " Real Class: " + review.getRealPolarity());
        System.out.println("---------------------------------------------------------------------------");
    }
  /**
  * Prints out a list of movie reviews in table format
  * @param reviews A list of movie reviews
  */
  public static void printTable(List<MovieReview> reviews){

    for(MovieReview current: reviews){
        System.out.println("----------------------------------------------------------------------------");
        System.out.println(" Movie ID: " + current.getId() + " Review content: " + current.getText().substring(0,50));
        System.out.println(" Predicted Class: " + current.getPredictedPolarity() + " Real Class: " + current.getRealPolarity());
    }
  }
}

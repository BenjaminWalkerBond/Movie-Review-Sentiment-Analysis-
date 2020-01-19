/**
   A class used to classify each review as either positive or negative
   @author Benjamin Bond
*/
public class Sentiment{
  private int positiveWordCount, negativeWordCount;
  private int score;
  private String reviewName;
  private final int DEFAULT_PRINT_WIDTH = 10;
  private final String REVIEW_TYPES[] = {"Negative" , "Positive"};
  private String reviewType;
  /**
       Constructs a Sentiment object that initializes the positiveWordCount, negativeWordCount,
       and score to zero, the reviewName to "", and the reviewType to Negative by default
  */
  Sentiment(){
    this.positiveWordCount = 0;
    this.negativeWordCount = 0;
    this.score = 0;
    this.reviewName = "";
    this.reviewType = REVIEW_TYPES[0];
  }
  /**
     Constructs a Sentiment object that initializes the review name but defaults the other fields.
     @param revName the name of the review.
  */
  Sentiment(String revName){
    this.positiveWordCount = 0;
    this.negativeWordCount = 0;
    this.score = 0;
    this.reviewName = revName;
    this.reviewType = REVIEW_TYPES[0];
  }
  /**
  Increments the negativeWordCount field by one
  */
  public void incrementPWC(){
    this.positiveWordCount+=1;
  }
  /**
     Increments the negativeWordCount field by one
  */
  public void incrementNWC(){
    this.negativeWordCount+=1;
  }
  /**
     Updates the score of the sentiment object and matches the score with the review type
  */
  public void updateScore(){
    if(this.positiveWordCount > this.negativeWordCount){ //this represent positive
      score = 1;
      reviewType = REVIEW_TYPES[1];
    }else{
      score = 0; //this represents negative
      reviewType = REVIEW_TYPES[0];
    }
  }
  /**
     Getter
     @return value of the reviewType field
  */
  public String getReviewType(){
    return reviewType;
  }
  /**
     Getter
     @return value of the score field
  */
  public int getScore(){
    return score;
  }
  public String getReviewName(){
    return reviewName;
  }
  /**
     Getter
     @return strings of the review name and real type fields
  */
  public String toString(){
    //for loop to fix width for each output
    String width="";
    for(int x = 0 ; x< (DEFAULT_PRINT_WIDTH - reviewName.length()); x++){
      width = width+ " ";
    }
    return  " Review: " + this.reviewName + width + "Real Type: " + reviewType;
}
}

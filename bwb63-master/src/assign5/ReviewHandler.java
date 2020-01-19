package assign5;

import java.io.*;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CS3354 Spring 2019 Review Handler Class assignment 2 solution
    @author metsis
    @author tesic
    @author wen
    @author bond
 */
public class ReviewHandler extends AbstractReviewHandler {

    private static int ID = 0;

    /**
     * Loads reviews from a given path. If the given path is a .txt file, then
     * a single review is loaded. Otherwise, if the path is a folder, all reviews
     * in it are loaded.
     * @param filePath The path to the file (or folder) containing the review(sentimentModel).
     * @param realClass The real class of the review (0 = Negative, 1 = Positive
     * 2 = Unknown).
     */
    @Override
    public void loadReviews(String filePath, int realClass) {
        File fileOrFolder = new File(filePath);
        try {
            if (fileOrFolder.isFile()) {
                // File
                if (filePath.endsWith(".txt")) {
                    // Import review
                    MovieReview review = readReview(filePath, realClass);
                    // Add to database
                    database.put(review.getId(), review);
                    //Output result: single file
                    SentimentAnalysisApp.log.info("Review imported.");
                    SentimentAnalysisApp.log.info("ID: " + review.getId());
                    SentimentAnalysisApp.log.info("Text: " + review.getText());
                    SentimentAnalysisApp.log.info("Real Class: " + review.getRealPolarity());
                    SentimentAnalysisApp.log.info("Classification result: " + review.getPredictedPolarity());
                    if (realClass == 2) {
                        SentimentAnalysisApp.log.info("Real class unknown.");
                    } else if (realClass == review.getPredictedPolarity()) {
                        SentimentAnalysisApp.log.info("Correctly classified.");
                    } else {
                        SentimentAnalysisApp.log.info("Misclassified.");
                    }
                    SentimentAnalysisApp.log.info("");
                    SentimentAnalysisApp.outputArea.append("\nFile successfuly loaded.\n");

                } else {
                    // Cannot import non-txt files
                    SentimentAnalysisApp.log.info("Input file path is neither a txt file nor folder.");
                    SentimentAnalysisApp.outputArea.append("Input file path is neither a txt file nor folder.");
                }
            } else {
                // Folder
                String[] files = fileOrFolder.list();
                String fileSeparatorChar = System.getProperty("file.separator");
                int counter = 0;
                for (String fileName : files) {
                    if (fileName.endsWith(".txt")) {
                        // Only import txt files
                        // Import review
                        MovieReview review = readReview(filePath + fileSeparatorChar + fileName, realClass);
                        // Add to database
                        database.put(review.getId(), review);
                        // Count correct classified reviews, only real class is known
                        if (realClass != 2 && review.getRealPolarity() == review.getPredictedPolarity()) {
                            counter++;
                        }
                    } else {
                        //Do nothing
                        SentimentAnalysisApp.outputArea.append("\n Unable to read file: " + fileName +
                        " because it is not a text file.");
                    }
                }
                SentimentAnalysisApp.outputArea.append("\nSuccessfully read all readable files in folder.\n");
                // Output result: folder
                SentimentAnalysisApp.log.info("Folder imported.");
                SentimentAnalysisApp.log.info("Number of entries: " + files.length);

                // Only output accuracy if real class is known
                if (realClass != 2) {
                    SentimentAnalysisApp.log.info("Correctly classified: " + counter);
                    SentimentAnalysisApp.log.info("Misclassified: " + (files.length - counter));
                    SentimentAnalysisApp.log.info("Accuracy: " + ((double)counter / (double)files.length * 100) + "%");
                }
            }
        } catch (IOException e) {
            //log.info("System shutdown.");
            //e.printStackTrace();
            SentimentAnalysisApp.log.warning("\nCould not load review(s) with file path: " + filePath);
            SentimentAnalysisApp.outputArea.append( "\nCould not find movie review(s) with that path.\n");
        }

    }

    /**
     * Reads a single review file and returns it as a MovieReview object.
     * This method also calls the method classifyReview to predict the polarity
     * of the review.
     * @param reviewFilePath A path to a .txt file containing a review.
     * @param realClass The real class entered by the user.
     * @return a MovieReview object.
     * @throws IOException if specified file cannot be opened.
     */
    @Override
    public MovieReview readReview(String reviewFilePath, int realClass) throws IOException {
        // Read file for text
        Scanner inFile = new Scanner(new FileReader(reviewFilePath));
        String text = "";
        while (inFile.hasNextLine()) {
            text += inFile.nextLine();
        }
        // Remove the <br /> occurences in the text and replace them with a space
        text = text.replaceAll("<br />"," ");

        // Create review object, assigning ID and real class
        MovieReview review = new MovieReview(ID, text, realClass);
        // Update ID
        ID++;
        // Classify review
        classifyReview(review);

        return review;
    }

    /**
     * Deletes a review from the database, given its id.
     * @param id The id value of the review.
     */
    @Override
    public void deleteReview(int id) {
        if (!database.containsKey(id)) {
            // Review with given ID does not exist
            SentimentAnalysisApp.outputArea.append("Review ID: " + id + " does not exist");
            SentimentAnalysisApp.log.info("ID " + id + " does not exist.");
        } else {
            database.remove(id);
            SentimentAnalysisApp.outputArea.append("Review ID: " + id + " deleted.");
            SentimentAnalysisApp.log.info("Review with ID " + id + " deleted.");
        }
        SentimentAnalysisApp.outputArea.setCaretPosition(SentimentAnalysisApp.outputArea.getDocument().getLength());
    }

    /**
     * Loads review database.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadSerialDB() {
        SentimentAnalysisApp.log.info("Reading database...");
        // serialize the database
        InputStream file = null;
        InputStream buffer = null;
        ObjectInput input = null;
        try {
            file = new FileInputStream(DATA_FILE_NAME);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);

            database = (Map<Integer, MovieReview>)input.readObject();
            SentimentAnalysisApp.log.info(database.size() + " entry(s) loaded.");

            // Find the current maximum ID
            int currMaxId = Collections.max(database.keySet());
            ID = currMaxId + 1;

            input.close();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            SentimentAnalysisApp.log.log(Level.SEVERE, null, e);
            SentimentAnalysisApp.outputArea.append( "\nCould not load the database.\n");
            //System.err.println(e.toString());
          // e.printStackTrace();
        } finally {
            close(file);
        }
        SentimentAnalysisApp.log.info("Done!");
        SentimentAnalysisApp.outputArea.setCaretPosition(SentimentAnalysisApp.outputArea.getDocument().getLength());
    }

    /**
     * Searches the review database by id.
     * @param id The id to search for.
     * @return The review that matches the given id or null if the id does not
     * exist in the database.
     */
    @Override
    public MovieReview searchById(int id) {
        if (database.containsKey(id)) {
            SentimentAnalysisApp.log.info("Searching for review ID in database...");
            SentimentAnalysisApp.outputArea.append("\nReview with ID: " + id + " was found.\n");
            return database.get(id);
        }
        SentimentAnalysisApp.log.info("Review with ID: " + id + " not found.");
        SentimentAnalysisApp.outputArea.append("\nReview with ID: " + id + " was not found.\n");
        SentimentAnalysisApp.outputArea.setCaretPosition(SentimentAnalysisApp.outputArea.getDocument().getLength());
        return null;
    }

    /**
     * Searches the review database for reviews matching a given substring.
     * @param substring The substring to search for.
     * @return A list of review objects matching the search criterion.
     */
    @Override
    public List<MovieReview> searchBySubstring(String substring) {
        List<MovieReview> tempList = new ArrayList<MovieReview>();

        for (Map.Entry<Integer, MovieReview> entry : database.entrySet()){
            if (entry.getValue().getText().contains(substring)) {
                tempList.add(entry.getValue());
            }
        }

        if (!tempList.isEmpty()) {
            return tempList;
        } else {
            // No review has given substring
            SentimentAnalysisApp.log.info("Review(s) with quote: " + substring + "not found.");
            SentimentAnalysisApp.outputArea.append("Review(s) with quote: " + substring + " not found.");
            SentimentAnalysisApp.outputArea.setCaretPosition(SentimentAnalysisApp.outputArea.getDocument().getLength());
            return null;
        }

    }
}

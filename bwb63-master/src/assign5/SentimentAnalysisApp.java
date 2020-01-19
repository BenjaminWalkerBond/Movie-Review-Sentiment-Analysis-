package assign5;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 *  CS3354 Spring 2019 Sentiment Analysis Class implementation
    @author metsis
    @author tesic
    @author wen
    @author bond
 */
 @SuppressWarnings("rawtypes")
public class SentimentAnalysisApp {

    // Used to read from System's standard input
    //private static final Scanner CONSOLEINPUT = new Scanner(System.in);
    private static final ReviewHandler rh = new ReviewHandler();

    //Static log, used package wise
    static protected final Logger log = Logger.getLogger("SentimentAnalysis");

    /**
     * Main method demonstrates how to use Stanford NLP library classifier.
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("SentimentAnalysis.%u.%g.log");
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.setLevel(Level.INFO);

        // Load the database first.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // Load database if it exists.
                File databaseFile = new File(ReviewHandler.DATA_FILE_NAME);
                if (databaseFile.exists()) {
                    rh.loadSerialDB();
                }


                createAndShowGUI();

            }
        });
    }
    //Components for the layout
    static private final JPanel topPanel = new JPanel();
    static private final JPanel bottomPanel = new JPanel();
    static private final JLabel commandLable = new JLabel("Please select the command",JLabel.RIGHT);
    static private final JComboBox comboBox = new JComboBox();


    //add more Components

    //Output area components
    /**
     * The output area that displays all information about the program during its execution
     */
    static protected JTextArea outputArea = new JTextArea();
    static private  JScrollPane outputScrollPane = new JScrollPane(outputArea);

    //width and height of the monitor
    private static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    //width and height of the window (JFrame)
    private static int windowsWidth = 800;
    private static int windowsHeight = 600;

    //Static variables in order to hold combo box inputs
    static private String reviewFilePath="";
    static private String reviewID ="";
    static private String reviewPolarity="";



    /**
     * Initialize the JFrame and JPanels, and show them.
     * Also set the location to the middle of the monitor.
     */
    private static void createAndShowGUI() {

        createTopPanel();
        createBottomPanel();
        //instantiate and add containers
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new GridLayout(3,0));
        panelContainer.add(topPanel);
        panelContainer.add(bottomPanel);

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("SentimentAnalysis");

        // Save when quit.
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                log.info("Closing window.");
                outputArea.append("Closing window. Database will be saved.\n");
                super.windowClosing(e);
                log.info("Saving database.");
                rh.saveSerialDB();
                log.info("System shutdown.");
                System.exit(0);
            }

        });
        panelContainer.setOpaque(true);
        frame.setBounds((width - windowsWidth) / 2,
                (height - windowsHeight) / 2, windowsWidth, windowsHeight);
        frame.setContentPane(panelContainer);

        frame.setVisible(true);

    }
    /**
     * This method initialize the top panel, which is the commands using a ComboBox
     */
     @SuppressWarnings("unchecked")
    private static void createTopPanel() {
        comboBox.addItem("Please select...");
        comboBox.addItem(" 1. Load new movie review collection (given a folder or a file path).");
        comboBox.addItem(" 2. Delete movie review from database (given its id).");
        comboBox.addItem(" 3. Search movie reviews in database by id.");
        comboBox.addItem(" 4. Search movie reviews in database by substring.");
        comboBox.addItem(" 0. Exit program.");
        comboBox.setSelectedIndex(0);

        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                log.info("Command chosen, Item = " + e.getItem());
                log.info("StateChange = " + e.getStateChange());
                if (e.getStateChange() == 1) {
                    if (e.getItem().equals("Please select...")) {
                        // add code
                    } else if (e.getItem().equals(" 1. Load new movie review collection (given a folder or a file path).")) {
                        loadReviews();
                    } else if (e.getItem().equals(" 2. Delete movie review from database (given its id).")) {
                        deleteReviews();
                    } else if (e.getItem().equals(" 3. Search movie reviews in database by id.")) {
                        searchReviewsId();
                    } else if (e.getItem().equals(" 4. Search movie reviews in database by substring.")) {
                        searchReviewsSubstring();
                    } else if (e.getItem().equals(" 0. Exit program.")) {
                        exit();
                    }
                }

            }
        });

        GridLayout gl = new GridLayout(0,3,10,10);

        topPanel.setLayout(gl);
        topPanel.add(commandLable);
        topPanel.add(comboBox);

        //Keep the comboBox at the first line.
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());

        topPanel.updateUI();

     //add code
    }
    /**
    * This method initializes the bottom panel, which gives users feedback on the systems state
    */
    private static void createBottomPanel(){

      final Font fontCourier = new Font("Courier", Font.PLAIN, 16);
      outputArea.setFont(fontCourier);


      outputArea.setText("\nSentiment Analysis Application\n");
      outputArea.setEditable(false);

      final Border border = BorderFactory.createLineBorder(Color.BLACK);
      outputArea.setBorder(BorderFactory.createCompoundBorder(border,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
      bottomPanel.setBorder(BorderFactory.createCompoundBorder(border,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
      bottomPanel.setLayout(new GridLayout(1,0));

      outputScrollPane.createVerticalScrollBar();
      outputScrollPane.createHorizontalScrollBar();

      bottomPanel.add(outputScrollPane);

      bottomPanel.updateUI();
    }


    /**
     * Method 1: load new reviews text file.
     *
     */
    public static void loadReviews() {
        //add code
        topPanel.removeAll();
        topPanel.add(commandLable);
        topPanel.add(comboBox);


        final JLabel filePathLabel = new JLabel("Movie Review File Path", JLabel.RIGHT);
        final JTextField filePathInput = new JTextField("");

        final JButton positiveButton = new JButton("Positive");
        final JButton negativeButton = new JButton("Negative");
        final JButton unkownButton = new JButton("Unknown");


        topPanel.add( new JLabel());
        topPanel.add(filePathLabel);
        topPanel.add(filePathInput);
        topPanel.add( new JLabel());

        topPanel.add(positiveButton);
        topPanel.add(negativeButton);
        topPanel.add(unkownButton);

        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());

        outputArea.append( "\n 1. Load new movie review collection (given a folder or a file path). \n");

        topPanel.updateUI();

        //action listener for postive button
        positiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                SentimentAnalysisApp.log.info("Load Reviews button clicked.");
                Runnable myRunnable = new Runnable(){

                    public void run(){
                        try{
                            Thread.sleep(1000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        try{
                            outputArea.append("\nAdding review(s) to database...\n");
                            reviewFilePath = filePathInput.getText();

                            rh.loadReviews(filePathInput.getText(), 1);
                            SentimentAnalysisApp.log.info("Review file path is: " + reviewFilePath);
                            outputArea.setCaretPosition(outputArea.getDocument().getLength());
                        } catch(Exception e){
                          SentimentAnalysisApp.outputArea.append("\nCould not open file or folder with specified path.\n");
                          SentimentAnalysisApp.log.warning("Could not execute command 1.");
                          outputArea.setCaretPosition(SentimentAnalysisApp.outputArea.getDocument().getLength());
                        }
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });
        //action listener for negative button
        negativeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                SentimentAnalysisApp.log.info("Load Reviews button clicked.");
                Runnable myRunnable = new Runnable(){

                    public void run(){
                        try{
                            Thread.sleep(1000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        try{
                            outputArea.append("\nAdding review(s) to database...\n");
                            reviewFilePath = filePathInput.getText();

                            rh.loadReviews(filePathInput.getText(), 0);
                            SentimentAnalysisApp.log.info("Review file path is: " + reviewFilePath);
                            outputArea.setCaretPosition(outputArea.getDocument().getLength());
                        } catch(Exception e){
                          SentimentAnalysisApp.outputArea.append("\nCould not open file or folder with specified path.\n");
                          SentimentAnalysisApp.log.warning("Could not execute command 1.");
                          outputArea.setCaretPosition(SentimentAnalysisApp.outputArea.getDocument().getLength());
                        }
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });
        //action listener for unknown button
        unkownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                SentimentAnalysisApp.log.info("Load Reviews button clicked.");
                Runnable myRunnable = new Runnable(){

                    public void run(){
                        try{
                            Thread.sleep(1000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        try{
                            outputArea.append("\nAdding review(s) to database...\n");
                            reviewFilePath = filePathInput.getText();

                            rh.loadReviews(filePathInput.getText(), 2);
                            SentimentAnalysisApp.log.info("Review file path is: " + reviewFilePath);
                            outputArea.setCaretPosition(outputArea.getDocument().getLength());
                        } catch(Exception e){
                          SentimentAnalysisApp.outputArea.append("\nCould not open file or folder with specified path.\n");
                          SentimentAnalysisApp.log.warning("Could not execute command 1.");
                          outputArea.setCaretPosition(SentimentAnalysisApp.outputArea.getDocument().getLength());
                        }
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });


        filePathInput.setEditable(true);

    }

    /**
     * Method 2: delete reviews from database.
     *
     */
    public static void deleteReviews() {
        //add code
        topPanel.removeAll();
        topPanel.add(commandLable);
        topPanel.add(comboBox);

        final JLabel reviewIDLabel = new JLabel("Movie Review ID", JLabel.RIGHT);
        final JTextField reviewIdInput = new JTextField("");
        final JButton deleteReviewButton = new JButton("Delete Review");

        topPanel.add(new JLabel());
        topPanel.add(reviewIDLabel);
        topPanel.add(reviewIdInput);
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(deleteReviewButton);
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());

        outputArea.append( "\n 2. Delete movie review from database (given its id). \n");

        deleteReviewButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e){
            SentimentAnalysisApp.log.info("Delete Review button clicked.");
            if(reviewIdInput.getText() == null || reviewIdInput.getText().length() == 0){
              outputArea.append("Please input Review ID\n");
              SentimentAnalysisApp.log.info("Review ID invalid.");
              outputArea.setCaretPosition(outputArea.getDocument().getLength());
            }else{

            Runnable myRunnable = new Runnable(){

              public void run(){
                try{
                  Thread.sleep(2000);
                }catch( InterruptedException e){
                  e.printStackTrace();
                }
                try{
                  outputArea.append("\nDeleting review from database.\n");
                  outputArea.setCaretPosition(outputArea.getDocument().getLength());
                  reviewID = reviewIdInput.getText();

                  rh.deleteReview(Integer.valueOf(reviewIdInput.getText()));
                }catch(Exception e){
                  SentimentAnalysisApp.log.warning("Could not execute command 2.");
                }
              }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
          }
          }
        });

        topPanel.updateUI();
    }

    /**
     * Method 3: search reviews from database by Id.
     *
     */
    public static void searchReviewsId() {

      List<MovieReview> returned = new ArrayList<MovieReview>();
      //add code
      topPanel.removeAll();
      topPanel.add(commandLable);
      topPanel.add(comboBox);

      final JLabel reviewIdLabel = new JLabel("Review ID: ", JLabel.RIGHT);
      final JTextField reviewIdField = new JTextField("");
      final JButton searchByIdButton = new JButton("Search by ID");

      topPanel.add(new JLabel());
      topPanel.add(reviewIdLabel);
      topPanel.add(reviewIdField);
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());

      topPanel.add(searchByIdButton);

      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());

      outputArea.append("\n 3. Search movie reviews in database by id.\n");

      searchByIdButton.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
          SentimentAnalysisApp.log.info("Search by ID button clicked.");

          if(reviewIdField.getText() == null || reviewIdField.getText().length() == 0){
            outputArea.append("Please input Review ID\n");
            SentimentAnalysisApp.log.info("Review ID invalid.");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
          }else{

              try{
                int input = Integer.valueOf(reviewIdField.getText());
                if(input < 0 ){
                  throw new IOException("ID must be greater than or equal to 0");
                }
                if(rh.searchById(input) != null){
                returned.add(rh.searchById(input));
                }
                if(!returned.isEmpty()){
                printJTable(returned);
                }

              } catch (Exception d){
                outputArea.setCaretPosition(outputArea.getDocument().getLength());
                SentimentAnalysisApp.log.warning("Could not execute command 3.");
              }
            }
      }
    });
      topPanel.updateUI();
    }

    /**
     * Method 4: search reviews from database by Id.
     *
     */
    public static void searchReviewsSubstring() {
      // add code
      List<MovieReview> returned = new ArrayList<MovieReview>();
      topPanel.removeAll();
      topPanel.add(commandLable);
      topPanel.add(comboBox);
      topPanel.add(new JLabel());

      final JLabel reviewSubstringLabel = new JLabel("Review Quote: ", JLabel.RIGHT);
      final JTextField reviewSubstringField = new JTextField("");
      final JButton searchBySubstringButton = new JButton("Search by quote");

      topPanel.add(reviewSubstringLabel);
      topPanel.add(reviewSubstringField);
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(searchBySubstringButton);

      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());

      outputArea.append("\n 4. Search movie reviews in database by substring.\n");

      searchBySubstringButton.addActionListener( new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
          SentimentAnalysisApp.log.info("\nSearch by quote button pressed\n");

          if(reviewSubstringField.getText() == null || reviewSubstringField.getText().length() == 0){
            outputArea.append("Please input Review Quote\n");
            SentimentAnalysisApp.log.info("Review Quote invalid.");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
          }else{

            Runnable myRunnable = new Runnable(){

              public void run(){
                try{
                  Thread.sleep(2000);
                } catch( InterruptedException e){
                  SentimentAnalysisApp.log.warning("Could not execute command 4.");
                }
                  printJTable(rh.searchBySubstring(reviewSubstringField.getText()));
              }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
          }
        }
      });


      topPanel.updateUI();
    }

    /**
     * Method 0: save and quit.
     */
    public static void exit() {
      topPanel.removeAll();
      topPanel.add(commandLable);
      topPanel.add(comboBox);

      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.add(new JLabel());

      topPanel.add(new JLabel());
      topPanel.add(new JLabel());
      topPanel.updateUI();

      rh.saveSerialDB();
      outputArea.append("Database updated.\n");
      outputArea.append("Application exiting...");

      Timer timer = new Timer();
      TimerTask exit = new TimerTask(){
        public void run(){
          System.exit(0);
        }
      };
      timer.schedule(exit, new Date(System.currentTimeMillis()+ 1000));

    }


    /**
     * Print out the formatted JTable for list
     @param target_List
     */
    public static void printJTable(List<MovieReview> target_List) {
        // Create columns names
        JFrame resultFrame = new JFrame("Search Results");

        JTable displayTable;
        String[] columnNames = {"ID", "Predicted", "Real", "Text"};
        String[][] dataValues = new String[target_List.size()][4];

        for(int x = 0; x < target_List.size(); x++){
          for(int y= 0; y < 4; y++){
            if(y == 0){
              dataValues[x][y] = Integer.toString(target_List.get(x).getId());
            }
            if(y == 1){
              dataValues[x][y] = Integer.toString(target_List.get(x).getPredictedPolarity());
            }
            if( y == 2){
              dataValues[x][y] = Integer.toString(target_List.get(x).getRealPolarity());
            }
            if(y == 3){
              dataValues[x][y] = target_List.get(x).getText();
            }
          }
        }
        resultFrame.setBounds((width - windowsWidth) / 4,
                        (height - windowsHeight) / 4, windowsWidth, windowsHeight/2);
        displayTable = new JTable(dataValues,columnNames);
        JScrollPane scrollPane = new JScrollPane(displayTable);
        scrollPane.createVerticalScrollBar();
        scrollPane.createHorizontalScrollBar();

        resultFrame.setContentPane(scrollPane);
        resultFrame.setVisible(true);

      }
}

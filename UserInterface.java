import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class UserInterface extends JFrame implements ActionListener {
	
	// Interface components
	JTextField userIDField;
	JTextField imagePathField;
	
	JTextArea db;
	
	JScrollPane scrollPane;
	
    JButton enrollButton = new JButton("Enroll");
    JButton quitButton = new JButton("Quit");
    JButton mainMenuButton = new JButton("MainMenu");
    JButton submitButton = new JButton("Submit");
    JButton databaseButton = new JButton("Go to Database");
    
    // Processor
    loadbitmap processor = new loadbitmap();
    
	// Constructor
    public UserInterface(String name) {
        super(name);
    }

 // Main GUI build
    private static void mainMenu(UserInterface frame) {
        //Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setSize(450, 450);
        //Set up the content pane.
        frame.buildMainMenu();
        
        //Display the window.
        //frame.pack();
        frame.setVisible(true);
    }
    
    // GUI submodule builds
    private void buildMainMenu() {
        
        enrollButton.addActionListener(this);
        databaseButton.addActionListener(this);
        quitButton.addActionListener(this);
         
        getContentPane().add(enrollButton);
        getContentPane().add(databaseButton);
        getContentPane().add(quitButton);
        this.setSize(400, 70);
    }
    
    private void buildEnrollment() {
    	
    	JLabel userIDLabel = new JLabel("Enter user ID:");
    	userIDField = new JTextField(25);
        userIDField.addActionListener(this);
        
        JLabel imagePathLabel = new JLabel("   Enter path to image:");
    	imagePathField = new JTextField(25);
        imagePathField.addActionListener(this);
        
        submitButton.addActionListener(this);
        
        getContentPane().add(userIDLabel);
        getContentPane().add(userIDField);
        getContentPane().add(imagePathLabel);
        getContentPane().add(imagePathField);
        getContentPane().add(submitButton);
        
        this.setSize(1000, 70);
    }
    
    private void buildImageProcessor() throws IOException {
    	
    	BufferedImage origImg = ImageIO.read(new File("sample_fingerprint_grayscale.bmp"));
    	JLabel originalImage = new JLabel(new ImageIcon(origImg));
    	JLabel originalImageLabel = new JLabel("Original Image");
    	
    	BufferedImage binImg = ImageIO.read(new File("binarization.bmp"));
    	JLabel binImage = new JLabel(new ImageIcon(binImg));
    	JLabel binImageLabel = new JLabel("Binarized");
    	
    	BufferedImage normImg = ImageIO.read(new File("normalization.bmp"));
    	JLabel normImage = new JLabel(new ImageIcon(normImg));
    	JLabel normImageLabel = new JLabel("Normalized");
    	
    	BufferedImage scrapImg = ImageIO.read(new File("sample_fingerprint_grayscale.bmp"));
    	JLabel scrapImage = new JLabel(new ImageIcon(scrapImg));
    	JLabel scrapImageLabel = new JLabel("Scraps Removed");
    	
    	BufferedImage ridgesDetectedImg = ImageIO.read(new File("sample_fingerprint_grayscale.bmp"));
    	JLabel ridgesDetectedImage = new JLabel(new ImageIcon(ridgesDetectedImg));
    	JLabel ridgesDetectedImageLabel = new JLabel("Ridges Detected");
    	
    	BufferedImage ridgesIsolatedImg = ImageIO.read(new File("sample_fingerprint_grayscale.bmp"));
    	JLabel ridgesIsolatedImage = new JLabel(new ImageIcon(ridgesIsolatedImg));
    	JLabel ridgesIsolatedImageLabel = new JLabel("Normalized");
    	
    	JLabel resultsLabel = new JLabel("Here is where the results go");
    	
    	databaseButton.addActionListener(this);
    	mainMenuButton.addActionListener(this);
    	
        getContentPane().add(originalImage);
        getContentPane().add(originalImageLabel);
        getContentPane().add(binImage);
        getContentPane().add(binImageLabel);
        getContentPane().add(normImage);
        getContentPane().add(normImageLabel);
        getContentPane().add(scrapImage);
        getContentPane().add(scrapImageLabel);
        getContentPane().add(ridgesDetectedImage);
        getContentPane().add(ridgesDetectedImageLabel);
        getContentPane().add(ridgesIsolatedImage);
        getContentPane().add(ridgesIsolatedImageLabel);
        getContentPane().add(resultsLabel);
        getContentPane().add(databaseButton);
        getContentPane().add(mainMenuButton);
        
        this.setSize(1020, 1000);
    }
    
    private void buildTemplateDatabase() {
    	
    	JLabel templateDBHeaderLabel = new JLabel("Here is the template database");
    	
    	db = new JTextArea("This is text in the text area");
    	scrollPane = new JScrollPane(db);
    	
    	scrollPane.setPreferredSize(new Dimension(600, 500));
  
    	mainMenuButton.addActionListener(this);
    	
    	getContentPane().add(templateDBHeaderLabel);
    	getContentPane().add(scrollPane, BorderLayout.CENTER);
    	getContentPane().add(mainMenuButton);
    	
    	this.setSize(700, 600);
    }
    
    public void actionPerformed(ActionEvent e) {
	
    	Object src = e.getSource();
    	
    	// Button actions
    	if(src == enrollButton)
    	{
    		this.getContentPane().removeAll();
    		this.buildEnrollment();
    		this.setVisible(true);
    	}
    	else if (src == databaseButton)
    	{
    		this.getContentPane().removeAll();
    		this.buildTemplateDatabase();
    		this.setVisible(true);
    	}
    	else if(src == quitButton)
    	{
    		System.exit(0);
    	}
    	else if(src == submitButton)
    	{
    		this.getContentPane().removeAll();
    		try {
				loadbitmap.processImages();
				
    			this.buildImageProcessor();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		this.setVisible(true);
    	}
    	else if(src == mainMenuButton)
    	{
    		this.getContentPane().removeAll();
    		this.buildMainMenu();
    		this.setVisible(true);
    	}
	
    }
    
    
    // Main method
    public static void main(String[] args) {
	
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			UserInterface frame = new UserInterface("Fingerprint Authenticator");
    			UserInterface.mainMenu(frame);
    			frame.setVisible(true);			
    		}
    	});
    }  
}

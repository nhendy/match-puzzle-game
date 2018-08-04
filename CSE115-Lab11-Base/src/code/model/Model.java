package code.model;

import java.awt.Point;
import java.io.*;
import code.ui.UI;

public class Model {
	
	private UI _observer;	
	private Board _board;
	private Selector _selector;
	private int _highScore;
	private int _totalGameScore;
	
	public Model()  {
		_board = new Board(5,5);   
		_selector = new Selector(_board);
		_totalGameScore = 0;
		createHighScoreFile(); //if applicable
		loadHighScoreFile();
		
	}
	
	private void composeOfNewBoard(int r, int c){ _board = new Board(r, c); }

	private void composeOfNewSelector(){ _selector = new Selector(_board); }

	private void resetUI(){ _observer.run(); }
		
	private void createHighScoreFile(){
		File firstFile = new File("highScore.txt");
		if(!(firstFile.exists())){
			FileWriter fw = null;
			String fileName = "highScore.txt";
	        try {
	        	fw = new FileWriter(fileName, false);
	        	fw.write("0");} 
	        catch ( IOException e ) { e.printStackTrace(); }
	        finally {
	          if ( fw != null ) {
	        	  try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	          }
	        }
		}
	}
	
	// FOR LOADING HIGHSCORE
	private void loadHighScoreFile(){
		String fileName = "highScore.txt";
		FileReader fr = null;
		BufferedReader br = null;
		try{
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			_highScore = Integer.parseInt(br.readLine());
		}
		catch( Exception e){
			e.printStackTrace();
		}
		finally{
				try {
					fr.close();
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		}
	}
	
	public void addObserver(UI ui) {
		_observer = ui;
		_observer.update();
	}
	
	public int rows() { return _board.rows(); }
	public int cols() { return _board.cols(); }

	public String get(Point p) {
		return _board.get(p);
	}
	
	public Point selectedFirst() {
		return _selector.selectedFirst();
	}

	public void select(int r, int c) {
		_selector.select(new Point(r,c));
		isTherePossibleMove();
		_observer.update();
	}
	
	public void isTherePossibleMove(){
		if(!(_board.hasLegalMove())){
			calculateTotalScore();   //total score should be updated before exiting
			System.out.println("Your Score: " + _totalGameScore);
			if (_totalGameScore > getHighScore()){
				System.out.println("WOW! You made a new high score!");
				FileWriter fw = null;
				try{
					fw = new FileWriter("highScore.txt");
					fw.write(""+_totalGameScore);
				}
				catch(IOException e){
					e.printStackTrace();
				}
				finally {
					try { if (fw != null){ fw.close();} }
					catch (IOException e) { e.printStackTrace(); }
				}
			}
			System.exit(0);
		}
		
		else {
			checkLevel();
		}
	}
	
	// FOR INCREASING LEVEL
	private void checkLevel(){
		int maxScore = 10 * rows();
		if (getScore() >= maxScore ){
			calculateTotalScore();    //total score should updated before discarding the board
			composeOfNewBoard(rows() + 1, cols() + 1);
			composeOfNewSelector();
			_observer.disappear();
			resetUI();
		}
	}
	
	public void calculateTotalScore(){
		_totalGameScore += getScore();
		
	}
	
	public Point getHint(){
		return _board.provideHint();
	}
	
	public int getHighScore(){
		return _highScore;
	}
	
	public int getScore(){
		return _board.getScore();
	}
}

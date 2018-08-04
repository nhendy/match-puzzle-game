package code.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import code.model.Model;

public class UI implements Runnable {

	private JFrame _frame;
	private Model _model;
	private JTextField _display1;
	private JTextField _display2;
	private ArrayList<ArrayList<JButton>> _viewBoard;

	public UI() {
		_model = new Model();
		_viewBoard = new ArrayList<ArrayList<JButton>>();
	}

	public void disappear(){
		_frame.setVisible(false);
	}
	
	@Override
	public void run() {
		_frame = new JFrame("Noureldin Hendy's Lab 11");
		JPanel board = new JPanel();
		board.setLayout(new GridLayout(_model.rows(), _model.cols()));
		_frame.getContentPane().setLayout(new GridLayout(1,0));
		_frame.add(board);
		_viewBoard = new ArrayList<ArrayList<JButton>>();
		for (int r=0; r<_model.rows(); r++) {
			_viewBoard.add(new ArrayList<JButton>());
			for (int c=0; c<_model.cols(); c=c+1) {
				JButton button = new JButton();
				button.setOpaque(true);
				_viewBoard.get(r).add(button);
				board.add(button);
				button.addActionListener(new EventHandler(_model, r, c));
			}
		}		
		
		JPanel screen = new JPanel();
		screen.setLayout(new GridLayout(2, 2));
		_frame.add(screen);
		
		JLabel l1 = new JLabel("Your score : ");
		l1.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
		screen.add(l1);
		
		_display1 = new JTextField(4);
		setUpJTextField(_display1);
		screen.add(_display1);
		
		JLabel l2 = new JLabel("High score : ");
		l2.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
		screen.add(l2);
		
		_display2 = new JTextField(4);
		setUpJTextField(_display2);
		screen.add(_display2);
		
		_model.addObserver(this);
		update();
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.pack();
		_frame.setVisible(true);
		displayHighScore();
	}
	
	public void update() {
		// UPDATE BOARD - redraw the whole thing
		for (int r=0; r<_model.rows(); r=r+1) {
			for (int c=0; c<_model.cols(); c=c+1) {
				JButton button = _viewBoard.get(r).get(c);
				button.setIcon(new ImageIcon(_model.get(new Point(r,c))));
				button.setBackground(Color.WHITE);
			}
		}

		//display score
		displayScore();
		
		//HIGHLIGHT HINT
		highlightHint();
		
		// MARK FIRST SELECTED - if applicable
		Point p = _model.selectedFirst();
		if (p != null) {
			_viewBoard.get(p.x).get(p.y).setBackground(Color.RED);
		}
		
		

		// REPAINT JFrame
		_frame.repaint();
	}
	
	private void displayHighScore(){
		_display2.setText(""+_model.getHighScore());
	}
	
	private void displayScore(){
		_display1.setText(""+_model.getScore());
	}

	private void highlightHint(){
		Point p = _model.getHint();
		if(p != null){
			_viewBoard.get(p.x).get(p.y).setBackground(Color.CYAN);
		}
	}
	private void setUpJTextField(JTextField display){
		display.setEditable(false);
		display.setBackground(Color.WHITE);
		display.setForeground(Color.BLACK);
		display.setFont(new Font("Helvetica Neue", Font.BOLD, 36));
	}
}

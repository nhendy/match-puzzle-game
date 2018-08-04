package code.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Board {
	private ArrayList<ArrayList<String>> _board;
	private ArrayList<String> _colorFileNames;
	private ArrayList<Point> _boardPoints;
	private Random _rand;
	private int _rows;
	private int _cols;
	private int _currentBoardScore = 0;
	private Partition _partition;
	
	private static int MAX_COLORS = 6; // max possible is 6

	public Board(int rows, int cols) {
		_board = new ArrayList<ArrayList<String>>();
		_rand = new Random();
		_partition = new Partition();
		_rows = rows;
		_cols = cols;
		_colorFileNames = new ArrayList<String>();
		for (int i=0; i<MAX_COLORS; i=i+1) {
			_colorFileNames.add("Images/Tile-"+i+".png");
		}
		pickTiles();
		while(isThereMatch() || !(hasLegalMove())){   
			_board.clear();
			pickTiles();
		}

		_boardPoints = new ArrayList<Point>();
		for ( int x = 0; x < _rows; x += 1){
			for ( int y = 0; y < _cols; y+= 1){
				_boardPoints.add(new Point(x, y));
			}
		}

	}

	//called by selector when second button is selected
	public void exchangeAndCheckMatch(Point s1, Point s2){
		  exchange(s1, s2);
		  if(isThereMatch()){
		    while(isThereMatch()){
		      HashSet<HashSet<Point>> partitions = _partition.trimmedPartition(_board);
		      calculateScore(partitions);
		      removeMatches(partitions);
		      dropDown();
		    }
		  }
		  else {
		    exchange(s1, s2);
		  }
	}

	
	//drop down new tiles
	private void dropDown(){
		ArrayList<Point> emptyTiles = new ArrayList<Point>();
		for (Point p : _boardPoints){
			if(get(p) == null ){
				emptyTiles.add(moveUp(p));
			}
		}
		for (Point p : emptyTiles){
			set(p, _colorFileNames.get(_rand.nextInt(_colorFileNames.size())));
		}
	}
	
	
	//checks if there can be any match produced by making a single move
	public boolean hasLegalMove(){
		for ( int x = 0; x < _rows; x += 1){
			for (int y = 0; y < _cols; y += 1){
				Point p = new Point(x, y);
				
				if (y + 1 < _board.get(x).size()){
					Point right = new Point(x, y + 1);
					exchange(p, right);
					if (isThereMatch()){
						exchange(right, p);
						return true;
					}
					else{
						exchange(right, p);
					}
				}
					
				if (x + 1 < _board.size()){
					Point below = new Point(x + 1, y);
					exchange(p, below);
					if (isThereMatch()){
						exchange(below, p);
					    return true;
					}
					else {
						exchange(below, p);
					}
				}
					

				}
			}
		return false;
		}
	
	private Point pointCausedMatch(Point p1, Point p2, HashSet<HashSet<Point>> match){
		Point result = null;
		for(HashSet<Point> h : match){
			if(h.contains(p1)){ 
				result = p2;
				break;
			}
			if (h.contains(p2)){
				result = p1;
				break;
			}
		}
		return result;
	}
	
	public Point provideHint(){
		for ( int x = 0; x < _rows; x += 1){
			for (int y = 0; y < _cols; y += 1){
				Point p = new Point(x, y);
				if (y + 1 < _board.get(x).size()){
					Point right = new Point(x, y + 1);
					exchange(p, right);
					if (isThereMatch()){
						Point r = pointCausedMatch(p, right, this.match());
						exchange(right, p);
						return r;
					}
					else{
						exchange(right, p);
					}
				}
				if (x + 1 < _board.size()){
					Point below = new Point(x + 1, y);
					exchange(p, below);
					if (isThereMatch()){
						Point r = pointCausedMatch(p, below, this.match());
						exchange(below, p);
						return r;
					}
					else {
						exchange(below, p);
					}
				}
				}
			}
		return null;
		}

	//assigns null to matched regions
	private void removeMatches(HashSet<HashSet<Point>> partitions){
		for(HashSet<Point> matches : partitions){
			for (Point p : matches){
				set(p, null);
			}
		}
		
	}

	//move point up 
	private Point moveUp(Point p){
		while (p.x >= 1){
			//TEST
			Point above = new Point(p.x - 1, p.y);
			if(get(above) != null){
				exchange(p, above);
				p = above;
			}
			else{
				break;
			}
		}
		return p;
	}

	//picks random tiles for the board
	private void pickTiles(){
		for (int r=0; r<_rows; r=r+1) {
			ArrayList<String> row = new ArrayList<String>();
			for (int c=0; c<_cols; c=c+1) {
				row.add(_colorFileNames.get(_rand.nextInt(_colorFileNames.size())));
			}
			_board.add(row);
		}
	}
	
	public int rows() { return _rows; }
	public int cols() { return _cols; }
	
	//returns image at point P
	public String get(Point p) {
		return _board.get(p.x).get(p.y);
	}

	//sets the tile at point p to new Image S and returns the previous image
	private String set(Point p, String s) {
		return _board.get(p.x).set(p.y, s);
	}

	private void exchange(Point p, Point q) {
		String temp = get(p);
		set(p, get(q));
		set(q, temp);
	}
	
	
	//returns set of matching adjacent points PRIVATE
	private HashSet<HashSet<Point>> match() {
		return _partition.trimmedPartition(_board);
	}

	
	public boolean isThereMatch() {
		return match().size() > 0;    
	}

	private void calculateScore(HashSet<HashSet<Point>> partitions){
		  for(HashSet<Point> h: partitions){
			 _currentBoardScore +=  3 + (h.size() - 3)*(h.size() - 3);
		  }
	}
	
	public int getScore(){
		return _currentBoardScore;
	}

}
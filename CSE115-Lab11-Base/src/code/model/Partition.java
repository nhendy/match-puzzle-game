package code.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

public class Partition {
	
	public HashSet<HashSet<Point>> trimmedPartition(ArrayList<ArrayList<String>> board){
		HashSet<HashSet<Point>> result = new HashSet<HashSet<Point>>();
		HashSet<HashSet<Point>> untrimmedPartition = partition(board);
		
		for(HashSet<Point> h: untrimmedPartition){
			HashSet<Point> potentialRegion = trimmedRegion(h);
			if(potentialRegion.size() >= 3){
				result.add(potentialRegion);
			}
		}
		return result;
	}
	
	
	private HashSet<Point> trimmedRegion(HashSet<Point> in){
		HashSet<Point> out = new HashSet<Point>();
		for(Point p : in){
			if(inRow(p, in) || inCol(p, in)){
				out.add(p);
			}
		}
		return out;
	}
	
	
	
	
	private HashSet<HashSet<Point>> partition(ArrayList<ArrayList<String>> board) {
		HashSet<HashSet<Point>> result = new HashSet<HashSet<Point>>();
		if (board != null ){
			ArrayList<Point> boardPoints = new ArrayList<Point>();
			for(int x = 0; x < board.size(); x += 1){
				for(int y = 0; y < board.get(x).size(); y += 1){
					boardPoints.add(new Point(x, y));
				}
			
			}
			while(!(boardPoints.isEmpty())){
				Point p = boardPoints.get(0);
				result.add(maximalMatchedRegion(p, board));
				boardPoints.removeAll(maximalMatchedRegion(p, board));
			}
		}
		return result;
	}
	
	private boolean inCol(Point p, HashSet<Point> in){
		return (in.contains(new Point(p.x, p.y-2)) && in.contains(new Point(p.x, p.y-1))
			||  in.contains(new Point(p.x, p.y-1)) && in.contains(new Point(p.x, p.y+1))
			||  in.contains(new Point(p.x, p.y+1)) && in.contains(new Point(p.x, p.y+2)));
	}

	
	
	private boolean inRow(Point p, HashSet<Point> in){
		return (in.contains(new Point(p.x-2, p.y)) && in.contains(new Point(p.x-1, p.y))
			||  in.contains(new Point(p.x-1, p.y)) && in.contains(new Point(p.x+1, p.y))
			||  in.contains(new Point(p.x+1, p.y)) && in.contains(new Point(p.x+2, p.y)));
	}

	private HashSet<Point> maximalMatchedRegion(Point start, ArrayList<ArrayList<String>> board) {
		HashSet<Point> matchedRegion = new HashSet<Point>();
		HashSet<Point> candidatesForInclusion = new HashSet<Point>();
		HashSet<Point> adjacentMatches = new HashSet<Point>();
		candidatesForInclusion.add(start);
		while (! candidatesForInclusion.isEmpty()) {
			for (Point p : candidatesForInclusion) {
				for (Point q : adjacentAndMatching(p, board)) {
					if (!matchedRegion.contains(q) && !candidatesForInclusion.contains(q)) { adjacentMatches.add(q); }
				}
			}
			matchedRegion.addAll(candidatesForInclusion);
			HashSet<Point> tmp = candidatesForInclusion;
			candidatesForInclusion = adjacentMatches;
			adjacentMatches = tmp;
			adjacentMatches.clear();
		}
		return matchedRegion;
	}

	private HashSet<Point> adjacentAndMatching(Point p, ArrayList<ArrayList<String>> board) {
		HashSet<Point> result = new HashSet<Point>();
		Point check;
		check = new Point(p.x-1,p.y); if (inBounds(check, board) && matches(p,check,board)) { result.add(check); }
		check = new Point(p.x+1,p.y); if (inBounds(check, board) && matches(p,check,board)) { result.add(check); }
		check = new Point(p.x,p.y-1); if (inBounds(check, board) && matches(p,check,board)) { result.add(check); }
		check = new Point(p.x,p.y+1); if (inBounds(check, board) && matches(p,check,board)) { result.add(check); }
		return result;
	}

	private boolean matches(Point p, Point q, ArrayList<ArrayList<String>> board) { return board.get(p.x).get(p.y).equals(board.get(q.x).get(q.y)); }

	private boolean inBounds(Point p, ArrayList<ArrayList<String>> board) { return p.x >=0 && p.x < board.size() && p.y >= 0 && p.y < board.get(0).size(); }

}




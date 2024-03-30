import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MazeSolver implements IMazeSolver {
	private class Pair{
		Integer x;
		Integer y;
		Pair(Integer x, Integer y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "x: " + this.x + " y: " + this.y;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Pair)) {
				return false;
			}

			final Pair pair = (Pair) o;

			if (x != pair.x) {
				return false;
			}
			if (y != pair.y) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 17 * x + 31 * y;;
			return result;
		}
	}

	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	Queue<ArrayList<Pair>> queue;
	HashMap<Pair,Boolean> visited;
	Integer startRow;
	Integer startCol;

	public MazeSolver() {
		// TODO: Initialize variables.
		this.maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		this.queue = new LinkedList<>();
		this.visited = new HashMap<>();
	}

	private boolean canGo(int row, int col, int dir) {
		// not needed since our maze has a surrounding block of wall
		// but Joe the Average Coder is a defensive coder!
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;

		switch (dir) {
			case NORTH:
				return !maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !maze.getRoom(row, col).hasWestWall();
		}

		return false;
	}

	//im assuming we're using bfs
	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		this.startRow = startRow;
		this.startCol = startCol;

		Pair initialPair = new Pair(startRow, startCol);
		ArrayList<Pair> path = new ArrayList<>();
		path.add(initialPair);
		queue.add(path);
		visited.put(initialPair, true);

		while (!queue.isEmpty()) {
			path = queue.remove();
			Pair last = path.get(path.size() - 1);
			visited.put(last, true);
			//System.out.println(last.toString());
			if (last.x == endRow && last.y == endCol) {
				//System.out.println("--------------------------------------------------------");
				for (Pair p: path) {
					//System.out.println(p);
					maze.getRoom(p.x, p.y).onPath = true;
				}
				return path.size();
			}


//			System.out.println("block has North wall: " + maze.getRoom(last.x, last.y).hasNorthWall());
//			System.out.println("block has South wall: "  + maze.getRoom(last.x, last.y).hasSouthWall());
//			System.out.println("block has East wall: "  + maze.getRoom(last.x, last.y).hasEastWall());
//			System.out.println("block has West wall: "  + maze.getRoom(last.x, last.y).hasWestWall());

			//if each of the walls do not exist, add each path to the queue
			for (int direction = 0; direction < 4; direction++) {
				Integer newX = last.x + DELTAS[direction][0];
				Integer newY =  last.y + DELTAS[direction][1];
				Pair newPair = new Pair(newX, newY);

				if (canGo(last.x, last.y, direction) && !visited.getOrDefault(newPair, false)) {
					path.add(newPair); 						//add last element

					queue.add(new ArrayList<>(path));		//add to main queue
					path.remove(newPair);

				}
			}
			//System.out.println(queue.toString());

		}
		return null;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		this.queue = new LinkedList<>();
		this.visited = new HashMap<>();

		Pair initialPair = new Pair(startRow, startCol);
		ArrayList<Pair> path = new ArrayList<>();
		path.add(initialPair);
		queue.add(path);
		visited.put(initialPair, true);
		int sum = 0;

		while (!queue.isEmpty()) {
			path = queue.remove();
			Pair last = path.get(path.size() - 1);
			visited.put(last, true);

			if (path.size()-1 == k) {
				sum += 1;
			}

			//if each of the walls do not exist, add each path to the queue
			for (int direction = 0; direction < 4; direction++) {
				Integer newX = last.x + DELTAS[direction][0];
				Integer newY = last.y + DELTAS[direction][1];
				Pair newPair = new Pair(newX, newY);

				if (canGo(last.x, last.y, direction) && !visited.getOrDefault(newPair, false)) {
					path.add(newPair);                        //add last element
					queue.add(new ArrayList<>(path));        //add to main queue
					path.remove(newPair);

				}
			}

		}


		return sum;
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			MazePrinter.printMaze(maze);

			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 2, 3));
			MazePrinter.printMaze(maze);
			//ImprovedMazePrinter.printMaze(maze,0,0);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

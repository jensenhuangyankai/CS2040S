import java.lang.reflect.Array;
import java.util.*;

public class MazeSolver implements IMazeSolver {
	private class Node{
		Integer x;
		Integer y;
		Integer stepsTaken;
		Node(Integer x, Integer y, Integer stepsTaken) {
			this.x = x;
			this.y = y;
			this.stepsTaken = stepsTaken;
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
			if (!(o instanceof Node)) {
				return false;
			}

			final Node pair = (Node) o;

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
	Queue<Node> queue;
	HashMap<Node,Boolean> visited;
	HashMap<Node, Node> parent;
	Integer startRow;
	Integer startCol;
	private int rows, cols;
	HashMap<Integer, Integer> kFrontiers; //this is to store the number of things you can reach with k

	public MazeSolver() {
		this.maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.queue = new LinkedList<>();
		this.visited = new HashMap<>();
		this.rows = maze.getRows();
		this.cols = maze.getColumns();
		this.kFrontiers = new HashMap<>();
		this.parent = new HashMap<>();
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

	private ArrayList<Node> backTrace(HashMap<Node, Node> parent, Node node) {
		//System.out.println(node);
		ArrayList<Node> path = new ArrayList<>();
		//System.out.println(node.x + " " + startCol + " " + node.y + " " + startRow);
		while (node.x != startRow || node.y != startCol) {
			path.add(node);
			node = parent.get(node);
			//System.out.println(node);
		}
		path.add(node);
		//System.out.println(path);
		Collections.reverse(path);

		return path;
	}



	//im assuming we're using bfs
	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				maze.getRoom(i, j).onPath = false;
			}
		}
		this.visited = new HashMap<>();
		this.queue = new LinkedList<>();
		this.kFrontiers = new HashMap<>();
		this.startRow = startRow;
		this.startCol = startCol;
		this.parent = new HashMap<>();

		if (startRow < 0 || startCol < 0 || startRow >= this.rows || startCol >= this.cols ||
				endRow < 0 || endCol < 0 || endRow >= this.rows || endCol >= this.cols) {
			throw new Exception("Invalid start/end coordinate");
		}
		if (this.maze == null) {
			throw new Exception("Initialise maze first");
		}

		Node initialNode = new Node(startRow, startCol, 0);
		visited.put(initialNode, true);
		queue.add(initialNode);

		ArrayList<ArrayList<Node>> pathsList = new ArrayList<>();
		while (!queue.isEmpty()) {
			Node node = queue.remove();
			visited.put(node, true);

			if (node.x == endRow && node.y == endCol) {
				pathsList.add(backTrace(parent, node));
			}

			for (int direction = 0; direction < 4; ++direction) {
				Integer newX = node.x + DELTAS[direction][0];
				Integer newY = node.y + DELTAS[direction][1];
				Integer newStepsTaken = node.stepsTaken + 1;
				Node newNode = new Node(newX, newY, newStepsTaken);
				if (canGo(node.x, node.y, direction) && !visited.getOrDefault(newNode, false)) {
					if (!queue.contains(newNode)){
						kFrontiers.put(newNode.stepsTaken, kFrontiers.getOrDefault(newNode.stepsTaken,0) + 1); //calculation for next qn
						parent.put(newNode, node); //set parent of newpair to be node
						queue.add(newNode);
					}

				}
			}

		}

		if (pathsList.size() != 0) {
			ArrayList<Node> finalPath = pathsList.get(0);
			for (ArrayList<Node> p: pathsList) {
				if (p.size() < finalPath.size()) {
					finalPath = p;
				}
			}
			for (Node node : finalPath) {
				maze.getRoom(node.x, node.y).onPath = true;
			}
			return finalPath.size() - 1;
		}
		else {
			return null;
		}
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (k == 0) return 1;
		if (k < 0) {
			throw new Exception("no.");
		}
		return kFrontiers.getOrDefault(k,0);
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			//MazePrinter.printMaze(maze);

			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(2, 3, 0, 0));
			MazePrinter.printMaze(maze);
			//ImprovedMazePrinter.printMaze(maze,0,0);

			for (int i = 0; i <= 12; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}

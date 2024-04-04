import java.util.*;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private class Node{
		Integer x;
		Integer y;
		Integer stepsTaken;
		Integer nSuperpower; //number of superpowers remaining
		Node(Integer x, Integer y, Integer stepsTaken, Integer nSuperpower) {
			this.x = x;
			this.y = y;
			this.stepsTaken = stepsTaken;
			this.nSuperpower = nSuperpower;
		}

		@Override
		public String toString() {
			return "x: " + this.x + " y: " + this.y + " nSuperpower: " + nSuperpower;
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
			result = 17 * x + 31 * y + 123*nSuperpower;
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
	HashMap<Node, ArrayList<Node>> parent;
	Integer startRow;
	Integer startCol;
	private int rows, cols;
	HashMap<Integer, Integer> kFrontiers; //this is to store the number of things you can reach with k


	public MazeSolverWithPower() {
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
	private boolean canGoWithSuperpower(int row, int col, int dir, int nSuperpower) {
		// not needed since our maze has a surrounding block of wall
		// but Joe the Average Coder is a defensive coder!
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;
		if (nSuperpower - 1 < 0) return false;

		return true;
	}

	private ArrayList<Node> backTrace(HashMap<Node, ArrayList<Node>> parent, Node node) {
		//System.out.println(node);
		ArrayList<Node> path = new ArrayList<>();
		//System.out.println(node.x + " " + startCol + " " + node.y + " " + startRow);
		while (node.x != startRow || node.y != startCol) {
			path.add(node);
			List<Node> nodeList = parent.get(node);
			Node smallestNode = new Node(0, 0, 0, Integer.MAX_VALUE);
			for (Node n: nodeList) {
				if (n.nSuperpower < smallestNode.nSuperpower) {
					smallestNode = n;
				}
			}
			node = smallestNode;
			//System.out.println(node);
		}
		path.add(node);

		Collections.reverse(path);

		return path;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol, int superpowers) throws Exception {
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

		Node initialNode = new Node(startRow, startCol, 0, superpowers);
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
				Node newNode = new Node(newX, newY, newStepsTaken, node.nSuperpower);
				if (canGo(node.x, node.y, direction) && !visited.getOrDefault(newNode, false)) {
					if (!queue.contains(newNode)){
						kFrontiers.put(newNode.stepsTaken, kFrontiers.getOrDefault(newNode.stepsTaken,0) + 1); //calculation for next qn
						ArrayList<Node> nodeList = parent.getOrDefault(newNode, new ArrayList<>());
						nodeList.add(node);
						parent.put(newNode, nodeList); //set parent of newpair to be node
						queue.add(newNode);
					}
				}
				newNode = new Node(newX, newY, newStepsTaken, node.nSuperpower - 1);
				if (canGoWithSuperpower(node.x, node.y, direction, node.nSuperpower) && !visited.getOrDefault(newNode, false)) {
					if (!queue.contains(newNode)){

						kFrontiers.put(newNode.stepsTaken, kFrontiers.getOrDefault(newNode.stepsTaken,0) + 1); //calculation for next qn
						ArrayList<Node> nodeList = parent.getOrDefault(newNode, new ArrayList<>());
						nodeList.add(node);
						parent.put(newNode, nodeList); //set parent of newpair to be node
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
			//System.out.println(finalPath);
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
		// TODO: Find number of reachable rooms.
		return 0;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		return null;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 4, 4, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

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
			return "x: " + this.x + " y: " + this.y + " nSuperpower: " + nSuperpower + " steps: " + stepsTaken;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Node)) {
				return false;
			}

			final Node node = (Node) o;

			if (x != node.x) {
				return false;
			}
			if (y != node.y) {
				return false;
			}
//			if (stepsTaken != node.stepsTaken) {
//				return false;
//			}
//			if (nSuperpower != node.nSuperpower) {
//				return false;
//			}


			return true;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 17 * x + 31 * y;// + 123*nSuperpower; //+ 12*stepsTaken;
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
	HashMap<Node, Node> parent2;
	Integer startRow;
	Integer startCol;
	private int rows, cols;
	HashMap<Integer, Integer> kPowerFrontiers; //this is to store the number of things you can reach with k
	HashMap<Integer, Integer> kNoPowerFrontiers;


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
		this.kPowerFrontiers = new HashMap<>();
		this.kNoPowerFrontiers = new HashMap<>();
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
		switch (dir) {
			case NORTH:
				return maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return maze.getRoom(row, col).hasEastWall();
			case WEST:
				return maze.getRoom(row, col).hasWestWall();
		}
		return true;
	}

	private ArrayList<Node> backTrace(HashMap<Node, ArrayList<Node>> parent, Node node) {
		//System.out.println(node);
		ArrayList<Node> path = new ArrayList<>();
		//System.out.println(node.x + " " + startCol + " " + node.y + " " + startRow);
		while (node.x != startRow || node.y != startCol) {
			//System.out.println(node);
			path.add(node);
			List<Node> nodeList = parent.get(node);
			Node smallestNode = new Node(0, 0, 0, Integer.MAX_VALUE);
			for (Node n: nodeList) {
				if (n.nSuperpower < smallestNode.nSuperpower) {
					smallestNode = n;
				}
			}
			node = smallestNode;
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
		this.kPowerFrontiers = new HashMap<>();
		this.kNoPowerFrontiers = new HashMap<>();
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
		if (superpowers < 0) {
			throw new Exception("superpowerpls");
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
			System.out.println("NEW NODE_____________________");
			for (int direction = 0; direction < 4; ++direction) {
				Integer newX = node.x + DELTAS[direction][0];
				Integer newY = node.y + DELTAS[direction][1];
				Integer newStepsTaken = node.stepsTaken + 1;


				Node newNode = new Node(newX, newY, newStepsTaken, node.nSuperpower);
				if (Objects.equals(newNode.x, node.x) && Objects.equals(newNode.y, node.y)) continue;

				Node temp = new Node(newX, newY, node.stepsTaken, node.nSuperpower);
				if (canGo(node.x, node.y, direction) && !visited.getOrDefault(newNode, false)) {
					if (!queue.contains(newNode)){
						kNoPowerFrontiers.put(newNode.stepsTaken, kNoPowerFrontiers.getOrDefault(newNode.stepsTaken,0) + 1); //calculation for next qn
						ArrayList<Node> nodeList = parent.getOrDefault(newNode, new ArrayList<>());
						nodeList.add(node);
						parent.put(newNode, nodeList); //set parent of newpair to be node
						queue.add(newNode);
						System.out.println("nopower: " + newNode);
					}
				}

				newNode = new Node(newX, newY, newStepsTaken, node.nSuperpower - 1);
				if (canGoWithSuperpower(node.x, node.y, direction, node.nSuperpower) && !visited.getOrDefault(temp, false)) {
					if (!queue.contains(newNode)){
						kPowerFrontiers.put(newNode.stepsTaken, kPowerFrontiers.getOrDefault(newNode.stepsTaken,0) + 1); //calculation for next qn
						ArrayList<Node> nodeList = parent.getOrDefault(newNode, new ArrayList<>());
						nodeList.add(node);
						parent.put(newNode, nodeList); //set parent of newpair to be node
						queue.add(newNode);
						System.out.println("power: " + newNode);
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
		if (k == 0) return 1;
		if (k < 0) {
			throw new Exception("no.");
		}

		System.out.println(kNoPowerFrontiers);
		System.out.println(kPowerFrontiers);
		return kNoPowerFrontiers.getOrDefault(k,0) + kPowerFrontiers.getOrDefault(k,0) ;
	}


	private ArrayList<Node> originalBackTrace(HashMap<Node, Node> parent, Node node) {
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

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				maze.getRoom(i, j).onPath = false;
			}
		}
		this.visited = new HashMap<>();
		this.queue = new LinkedList<>();
		this.kNoPowerFrontiers = new HashMap<>();
		this.kPowerFrontiers = new HashMap<>();
		this.startRow = startRow;
		this.startCol = startCol;
		this.parent2 = new HashMap<>();

		if (startRow < 0 || startCol < 0 || startRow >= this.rows || startCol >= this.cols ||
				endRow < 0 || endCol < 0 || endRow >= this.rows || endCol >= this.cols) {
			throw new Exception("Invalid start/end coordinate");
		}
		if (this.maze == null) {
			throw new Exception("Initialise maze first");
		}

		Node initialNode = new Node(startRow, startCol, 0,0);
		visited.put(initialNode, true);
		queue.add(initialNode);

		ArrayList<ArrayList<Node>> pathsList = new ArrayList<>();
		while (!queue.isEmpty()) {
			Node node = queue.remove();
			visited.put(node, true);

			if (node.x == endRow && node.y == endCol) {
				pathsList.add(originalBackTrace(parent2, node));
			}

			for (int direction = 0; direction < 4; ++direction) {
				Integer newX = node.x + DELTAS[direction][0];
				Integer newY = node.y + DELTAS[direction][1];
				Integer newStepsTaken = node.stepsTaken + 1;
				Node newNode = new Node(newX, newY, newStepsTaken, 0);
				if (canGo(node.x, node.y, direction) && !visited.getOrDefault(newNode, false)) {
					if (!queue.contains(newNode)){
						kNoPowerFrontiers.put(newNode.stepsTaken, kNoPowerFrontiers.getOrDefault(newNode.stepsTaken,0) + 1); //calculation for next qn
						parent2.put(newNode, node); //set parent of newpair to be node
						queue.add(newNode);
					}

				}
			}

		}

		if (!pathsList.isEmpty()) {
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

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(4,0, 4,4,2));
			ImprovedMazePrinter.printMaze(maze,4,0);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

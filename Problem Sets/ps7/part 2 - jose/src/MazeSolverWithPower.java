import java.util.HashSet;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private int rows;
	private int cols;

	private boolean solved;
	private int[][] visited;

	private Integer[][][] parent;

	private int[] reachable;

	private int startRow, startCol, endRow, endCol;

	private int steps;

	private int pathSteps;

	private HashSet<int[]> frontier;

	public MazeSolverWithPower() {
		this.solved = false;
		this.maze = null;
	}


	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.rows = maze.getRows();
		this.cols = maze.getColumns();
		this.visited = new int[rows][cols];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		/*
		Throw an exception if the maze hasn't yet been initialized
		 */
		if (this.maze == null) {
			throw new Exception("The maze hasn't been initialized yet!");
		}
		/*
		Throw an exception if start or the endpoint is negative
		 */
		if (startRow < 0 || startCol < 0 || startRow >= this.rows || startCol >= this.cols ||
				endRow < 0 || endCol < 0 || endRow >= this.rows || endCol >= this.cols) {
			throw new Exception("Invalid start/end coordinate");
		}
		/*
		Save the start and the endpoint
		 */
		this.startRow = startRow;
		this.startCol = startCol;
		this.endRow = endRow;
		this.endCol = endCol;
		/*
		Initialize the frontier and the arrays to be used for memoization
		and reset the progress of the search algorithm
		 */
		this.reachable = new int[rows * cols];
		this.parent = new Integer[rows][cols][2];
		this.frontier = new HashSet<>();
		this.solved = false;
		this.steps = 0;
		/*
		Reset the arrays used for memoization
		and the onPath status of every room in the maze
		 */
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				this.visited[i][j] = -1;
				maze.getRoom(i, j).onPath = false;
			}
		}
		/*
		Start the bfs search algorithm with the starting point in the frontier
		 */
		int[] startCoord = new int[] {startRow, startCol};
		this.frontier.add(startCoord);
		this.visited[startRow][startCol] = 0;
		bfsPathSearch();
		/*
		Return how many steps it took if the endpoint has been found
		else return null
		 */
		return this.solved ? this.pathSteps : null;
	}

	/*
	Perform a breadth-first search on the maze
	so that if the shortest path is of length 'k',
	it can be found on the kth iteration of the search algorithm
	Instead of terminating the algorithm once a path has been found
	run the algorithm until the frontier is completely empty
	in order to memoize how many cells are certain distances away from the start
	and make the numReachable() method calls take O(1) time
	*/
	public void bfsPathSearch() {
		while (!frontier.isEmpty()) {
			HashSet<int[]> nextFrontier = new HashSet<int[]>();
			for (int[] coord : frontier) {
				int row = coord[0];
				int col = coord[1];
				/*
				For each coordinate stored in the frontier
				set the visited memoization matrix for that frontier as visited
				and increment the memoization array for storing the number of cells at that distance
				 */
				this.reachable[steps] += 1;
                /*
                For the current coordinate
                iterate in all four directions (north, south, east, west)
                to check if moving in that direction is possible
                If it is, add the cell in that direction to the frontier
                if it hasn't been visited yet
                */

				for (int direction = 0; direction < 4; direction++) {
					switch (direction) {
						case NORTH:
							if (maze.getRoom(row, col).hasNorthWall()) continue;
							break;
						case SOUTH:
							if (maze.getRoom(row, col).hasSouthWall()) continue;
							break;
						case EAST:
							if (maze.getRoom(row, col).hasEastWall()) continue;
							break;
						case WEST:
							if (maze.getRoom(row, col).hasWestWall()) continue;
							break;
					}
					int newRow = row + DELTAS[direction][0];
					int newCol = col + DELTAS[direction][1];
					if (visited[newRow][newCol] == -1) {
						nextFrontier.add(new int[]{newRow, newCol});
						visited[newRow][newCol] = 0;
						parent[newRow][newCol] = new Integer[]{row, col};
					}
				}
				/*
				If the path to the endpoint hasn't been found
				and the current coordinate is the endpoint
				set the solved status as true
				and set the onPath field for this coordinate and its parent as true
				 */
				if (!this.solved && row == endRow && col == endCol) {
					this.solved = true;
					maze.getRoom(row, col).onPath = true;
					while (parent[row][col][0] != null) {
						Integer[] parentCoord = parent[row][col];
						maze.getRoom(parentCoord[0], parentCoord[1]).onPath = true;
						row = parentCoord[0];
						col = parentCoord[1];
					}
					this.pathSteps = this.steps;
				}
			}
			this.steps += 1;
			this.frontier = nextFrontier;
		}
	}
	/*
	Return the number of reachable cells based on the given distance
	by checking the memoization array at that distance index
	 */
	@Override
	public Integer numReachable(int k) throws Exception {
		/*
		Throw an exception if the given distance is less than 0
		as the distance can't be negative
		 */
		if (k < 0) throw new Exception("k should be greater than 0!");
		/*
		The longest distance from one cell to another is the product of row and the columns minus one
		If the given distance is equal to or greater than that, return 0
		 */
		if (k >= this.reachable.length) {
			return 0;
		}
		/*
		Return the number of cells stored at the index of the distance
		of the memoization array
		 */
		return this.reachable[k];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol, int superpowers) throws Exception {
				/*
		Throw an exception if the maze hasn't yet been initialized
		 */
		if (this.maze == null) {
			throw new Exception("The maze hasn't been initialized yet!");
		}

		/*
		Throw an exception if start or the endpoint is negative
		 */
		if (startRow < 0 || startCol < 0 || startRow >= this.rows || startCol >= this.cols ||
				endRow < 0 || endCol < 0 || endRow >= this.rows || endCol >= this.cols) {
			throw new Exception("Invalid start/end coordinate");
		}

		/*
		Save the start and the endpoint and the number of superpowers
		 */
		this.startRow = startRow;
		this.startCol = startCol;
		this.endRow = endRow;
		this.endCol = endCol;

		/*
		Initialize the frontier and the arrays to be used for memoization
		and reset the progress of the search algorithm
		 */
		this.reachable = new int[rows * cols];
		this.parent = new Integer[rows][cols][2];
		this.frontier = new HashSet<>();
		this.solved = false;
		this.steps = 0;

		/*
		Reset the arrays used for memoization
		and the onPath status of every room in the maze
		 */
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				this.visited[i][j] = -1;
				maze.getRoom(i, j).onPath = false;
			}
		}

		/*
		Start the bfs search algorithm with the starting point in the frontier
		 */
		int[] startCoord = new int[] {startRow, startCol, superpowers};
		this.frontier.add(startCoord);
		bfsSuperPathSearch();
		/*
		Return how many steps it took if the endpoint has been found
		else return null
		 */
		return this.solved ? this.pathSteps : null;
	}


	public void bfsSuperPathSearch() {
		while (!frontier.isEmpty()) {
			HashSet<int[]> nextFrontier = new HashSet<int[]>();
			for (int[] coord : frontier) {
				int row = coord[0];
				int col = coord[1];
//				System.out.println("Looking at coordinates " + row + "," + col + " with superpower " + coord[2]);
				/*
				For each coordinate stored in the frontier
				if the coordinate hasn't been visited before
				memoize it to the number of reachable cells
				 */
//				this.reachable[steps] += 1;
//				System.out.println(this.visited[row][col]);
				if (this.visited[row][col] == -1) this.reachable[steps] += 1;

				if (this.visited[row][col] < coord[2]) {
					this.visited[row][col] = coord[2];
				} else {
//					System.out.println("break");
					continue;
				}

                /*
                For the current coordinate
                iterate in all four directions (north, south, east, west)
                to check if moving in that direction is possible
                If it is, add the cell in that direction to the frontier
                if it isn't, check if there is a superpower we can use
                If it is,
                */

				for (int direction = 0; direction < 4; direction++) {
					int newPowerCount = coord[2];
					switch (direction) {
						case NORTH:
							if (maze.getRoom(row, col).hasNorthWall()) {
								if (coord[2] <= 0 || row == 0) continue;
								else newPowerCount -= 1;
							}
							break;
						case SOUTH:
							if (maze.getRoom(row, col).hasSouthWall()) {
								if (coord[2] <= 0 || row == this.rows - 1) continue;
								else newPowerCount -= 1;
							}
							break;
						case EAST:
							if (maze.getRoom(row, col).hasEastWall()) {
								if (coord[2] <= 0 || col == this.cols - 1) continue;
								else newPowerCount -= 1;
							}
							break;
						case WEST:
							if (maze.getRoom(row, col).hasWestWall()) {
								if (coord[2] <= 0 || col == 0) continue;
								else newPowerCount -= 1;
							}
							break;
					}
					int newRow = row + DELTAS[direction][0];
					int newCol = col + DELTAS[direction][1];
//					System.out.println("new row is " + newRow);
//					System.out.println("new col is " + newCol);

					if (visited[newRow][newCol] < newPowerCount) {
						nextFrontier.add(new int[]{newRow, newCol, newPowerCount});
						//visited[newRow][newCol] = coord[2];
						if (parent[newRow][newCol][0] == null) parent[newRow][newCol] = new Integer[]{row, col};
					}

				}
				/*
				If the path to the endpoint hasn't been found
				and the current coordinate is the endpoint
				set the solved status as true
				and set the onPath field for this coordinate and its parent as true
				 */
				if (!this.solved && row == endRow && col == endCol) {
					this.solved = true;
					maze.getRoom(row, col).onPath = true;
					while (parent[row][col][0] != null) {
						Integer[] parentCoord = parent[row][col];
						maze.getRoom(parentCoord[0], parentCoord[1]).onPath = true;
						row = parentCoord[0];
						col = parentCoord[1];
//						System.out.println(row + " " + col);
					}
					this.pathSteps = this.steps;
				}
			}
			this.steps += 1;
			this.frontier = nextFrontier;
		}

	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(4,0, 4,4,1));
			ImprovedMazePrinter.printMaze(maze,4,0);
			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

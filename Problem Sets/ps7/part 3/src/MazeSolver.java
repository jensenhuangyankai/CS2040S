import java.util.*;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	private class Coord{
		Integer x;
		Integer y;
		Coord(Integer x, Integer y) {
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
			if (!(o instanceof Coord)) {
				return false;
			}
			final Coord coord = (Coord) o;
			if (x != coord.x) {
				return false;
			}
			if (y != coord.y) {
				return false;
			}
			return true;
		}
		@Override
		public int hashCode() {
			int result = x;
			result = 17 * x + 31 * y;
			return result;
		}
	}

	private class Node implements Comparable<Node> {
		Coord coord;
		Integer fear;
		Node(Coord coord, Integer fear) {
			this.coord = coord;
			this.fear = fear;
		}

		@Override
		public String toString() {
			return "Coord: " + this.coord + " Fear Level: " + this.fear;
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
			if (!coord.equals(node.coord)) {
				return false;
			}
			if (fear != node.fear) {
				return false;
			}
			return true;
		}
		@Override
		public int hashCode() {
			int result = this.coord.hashCode();
			result = 17 * result + 124 * fear;
			return result;
		}

		@Override
		public int compareTo(Node node) {
			return this.fear.compareTo(node.fear);
		}
	}



	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};


	private Maze maze;
	private PriorityQueue<Node> pq;
	private HashMap<Coord, Integer> distTo;
	Integer rows, cols;
	public MazeSolver() {
		pq = new PriorityQueue<>();
		distTo = new HashMap<>();
	}


	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.rows = maze.getRows();
		this.cols = maze.getColumns();
	}

	public boolean canGo(Coord coord, Integer dir) {
		//TODO err recheck this
//		System.out.println("direction " + dir);
//		System.out.println("x: " + coord.x + DELTAS[dir][0]);
//		System.out.println("y: " + coord.y + DELTAS[dir][1]);
		if (coord.x + DELTAS[dir][0] < 0 || coord.x + DELTAS[dir][0] >= maze.getRows()) return false;
		if (coord.y + DELTAS[dir][1] < 0 || coord.y + DELTAS[dir][1] >= maze.getColumns()) return false;

		switch (dir) {
			case NORTH:
//				System.out.println(maze.getRoom(coord.x, coord.y).getNorthWall());
				if (maze.getRoom(coord.x, coord.y).getNorthWall() == Integer.MAX_VALUE) return false;
				return true;
			case SOUTH:
//				System.out.println(maze.getRoom(coord.x, coord.y).getSouthWall());
				if (maze.getRoom(coord.x, coord.y).getSouthWall() == Integer.MAX_VALUE) return false;
				return true;
			case EAST:
//				System.out.println(maze.getRoom(coord.x, coord.y).getEastWall());
				if (maze.getRoom(coord.x, coord.y).getEastWall() == Integer.MAX_VALUE) return false;
				return true;
			case WEST:
//				System.out.println(maze.getRoom(coord.x, coord.y).getWestWall());
				if (maze.getRoom(coord.x, coord.y).getWestWall() == Integer.MAX_VALUE) return false;
				return true;
		}

		return true;
	}

	public Integer getFear(Coord coord, Integer dir) {
		Integer value = null;
		switch (dir) {
			case NORTH:
				value = maze.getRoom(coord.x, coord.y).getNorthWall();
				break;
			case SOUTH:
				value = maze.getRoom(coord.x, coord.y).getSouthWall();
				break;
			case EAST:
				value = maze.getRoom(coord.x, coord.y).getEastWall();
				break;
			case WEST:
				value = maze.getRoom(coord.x, coord.y).getWestWall();
				break;
		}
		//System.out.println(value);
		if (value == 0) { //weird thing when there is empty space you have to plus one fear but the maze.getroom returns 0;
			return 1;
		}
		return value;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (startRow == endRow && startCol == endCol) return 0;
		if (startRow < 0 || startCol < 0 || startRow >= this.rows || startCol >= this.cols ||
				endRow < 0 || endCol < 0 || endRow >= this.rows || endCol >= this.cols) {
			throw new Exception("Invalid start/end coordinate");
		}
		if (this.maze == null) {
			throw new Exception("Initialise maze first");
		}


		// TODO: Find minimum fear level.
		pq = new PriorityQueue<>();
		distTo = new HashMap<>();

		Coord startCoord = new Coord(startRow, startCol);
		Coord endCoord = new Coord(endRow, endCol);
		Node startNode = new Node(startCoord, 0);
		pq.add(startNode);
		distTo.put(startCoord, 0); //stores fear level

		while (!pq.isEmpty()) {
			Node node = pq.remove();
			for (int direction = 0; direction < 4; direction++) {
				//System.out.println("direction" + direction);
				//System.out.println(canGo(node.coord,direction));
				if (canGo(node.coord, direction)) { //basically checks that there isnt a wall
					Integer newFear = getFear(node.coord, direction); //gets the distance to the neighbouring node
					Integer newX = node.coord.x + DELTAS[direction][0];
					Integer newY = node.coord.y + DELTAS[direction][1];
					Coord newCoord = new Coord(newX, newY);
					Integer distance = distTo.getOrDefault(newCoord, Integer.MAX_VALUE); //checks if neighbour already exists in the distTo
//					System.out.println("newFear" + newFear);
//					System.out.println("nodefear" + node.fear);
					if (distance > newFear + node.fear) {
						distTo.put(newCoord, newFear + node.fear);
						Node newNode = new Node(newCoord, newFear + node.fear);
						pq.add(newNode);

					}

				}
			}
		}
//		System.out.println(distTo);
//		System.out.println(endCoord);
		if (distTo.get(endCoord) == null) {
			return null;
		}
		else{
			return distTo.get(endCoord);
		}

	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 1, 0,2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		return null;
	}


}

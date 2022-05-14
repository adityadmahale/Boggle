import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Boggle {

	// The cell class represents a single cell in the puzzle grid
	private class Cell {
		char character;

		public Cell(char character) {
			this.character = character;
		}
	}

	// The first row index value of the grid
	private static final int FIRST_ROW_INDEX = 0;
	// Minimum index value for the rows and columns in a grid
	private static final int MIN_INDEX_VALUE = 0;
	// X index in a coordinate
	private static final int X_INDEX = 0;
	// Y index in a coordinate
	private static final int Y_INDEX = 1;
	// Directions represent the character encodings for the moves in the grid
	private static final char[] directions = { 'U', 'D', 'L', 'R', 'N', 'W', 'S', 'E' };
	// Moves represent the movement in X and Y direction to visit a particular cell
	private static final int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, -1 }, { 1, -1 }, { 1, 1 }, { -1, 1 } };

	// The Trie data structure holds the dictionary of words
	private Trie trie;
	// Represents the puzzle grid
	private List<Cell[]> grid;
	// Size of the row in a grid
	private int gridRowSize;
	// Size of the column in a grid
	private int gridColumnSize;
	// Boolean value representing if the puzzle is valid
	private boolean isPuzzleValid;
	// Boolean value representing if the dictionary is valid
	private boolean isDictionaryValid;
	// Holds the words found in the grid
	private Set<String> foundWords;

	public boolean getDictionary(BufferedReader stream) {
		// Mark the dictionary as invalid initially
		isDictionaryValid = false;

		// Create a new trie for storing words
		trie = new Trie();

		// Minimum word length constraint
		final int MINIMUM_WORD_LENGTH = 2;
		String word;
		try {
			while ((word = stream.readLine()) != null) {
				// The blank line marks the end of words
				if (isBlank(word)) {
					break;
				}

				// If the word length is less than 2, then skip it
				if (word.length() < MINIMUM_WORD_LENGTH) {
					continue;
				}

				// Add the word to the trie
				trie.add(word);
			}
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Stream cannot be null");
		} catch (IOException e) {
			throw new IllegalStateException();
		}

		// Mark the dictionary as valid and return true
		isDictionaryValid = true;
		return true;
	}

	// Checks if the line is blank
	private boolean isBlank(String text) {
		return text.equals("");
	}

	public boolean getPuzzle(BufferedReader stream) {
		// Row size of an empty puzzle
		final int EMPTY_GRID_ROW_SIZE = 0;

		// Initially mark the puzzle as invalid
		isPuzzleValid = false;

		// Initialize the grid
		grid = new ArrayList<>();
		String row;
		try {
			// Iterate to parse the puzzle rows
			while ((row = stream.readLine()) != null) {

				// The blank line marks the end of puzzle
				if (isBlank(row)) {
					break;
				}

				// Check if the row size matches the previous row size
				// If it matches, then add that row
				// Otherwise, return false
				if (grid.size() == EMPTY_GRID_ROW_SIZE || grid.get(FIRST_ROW_INDEX).length == row.length()) {
					grid.add(createRowCells(row));
				} else {
					throw new IllegalArgumentException("Row sizes do not match.");
				}
			}
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Stream cannot be null");
		} catch (IOException e) {
			throw new IllegalStateException();
		}

		// Get grid row size
		gridRowSize = grid.size();
		// Get the column size
		if (gridRowSize != EMPTY_GRID_ROW_SIZE) {
			gridColumnSize = grid.get(FIRST_ROW_INDEX).length;
		} else {
			gridColumnSize = EMPTY_GRID_ROW_SIZE;
		}

		// Mark the puzzle as valid and return true
		isPuzzleValid = true;
		return true;
	}

	// Created a row of cells
	private Cell[] createRowCells(String row) {
		int length = row.length();
		Cell[] rowCells = new Cell[length];

		// Encapsulates the character in a cell and adds it to the row
		for (int i = 0; i < length; i++) {
			rowCells[i] = new Cell(row.charAt(i));
		}
		return rowCells;
	}

	public List<String> solve() {
		// Initial word string
		final String INITIAL_WORD = "";
		// Initial sequence string
		final String INITIAL_SEQUENCE = "";

		// Check if it is possible to solve the puzzle
		if (!isPuzzleSolvable()) {
			throw new IllegalStateException("The puzzle or words dictionary is invalid.");
		}

		// Create a new list for holding the list of output strings
		List<String> output = new ArrayList<>();

		// If the trie is empty then return an empty list
		if (!trie.hasWords()) {
			return output;
		}

		// Initialize found words set
		foundWords = new HashSet<>();
		Set<Cell> visitedCells;

		// Do the recursive search for all the cells in the grid
		for (int y = 0; y < gridColumnSize; y++) {
			for (int x = gridRowSize - 1; x >= 0; x--) {
				visitedCells = new HashSet<>();
				solve(trie.getRootNode(), x, y, x, y, INITIAL_SEQUENCE, INITIAL_WORD, output, visitedCells);
			}
		}

		// Sort the output list of strings
		Collections.sort(output);
		return output;
	}

	// Private method that gets called recursively
	private void solve(Node root, int startX, int startY, int x, int y, String sequence, String word, List<String> output, Set<Cell> visitedCells) {
		// If the coordinate is does not exist in the grid, then return
		if (isOutOfRange(x, y)) {
			return;
		}

		// Get the cell for the x and y coordinate
		Cell cell = getCell(x, y);

		// If the cell is already visited, then return
		if (visitedCells.contains(cell)) {
			return;
		}

		// Get the character
		char ch = cell.character;
		// Check if the character is present in the trie node. If not, then return
		if (!root.hasChildCharacter(ch)) {
			return;
		}

		// Append the character to the word
		word += ch;

		// Get the Trie node. If the node is the end of a word then add it to the output
		Node node = root.getChildCharacterNode(ch);
		if (node.isEndOfWord() && !foundWords.contains(word)) {
			foundWords.add(word);
			// Calculate the coordinates
			int coordX = startY + 1;
			int coordY = gridRowSize - startX;
			output.add(word + "\t" + coordX + "\t" + coordY + "\t" + sequence);
		}

		// Add the cell to the visitedCells set
		visitedCells.add(cell);

		// Recursively call the solve method of all eight directions
		for (int i = 0; i < directions.length; i++) {
			solve(node, startX, startY, x + moves[i][X_INDEX], y + moves[i][Y_INDEX], sequence + directions[i], word, output, visitedCells);
		}

		// Remove the cell from the visited cells
		visitedCells.remove(cell);
	}

	// Returns the grid cell for the given X and Y location
	private Cell getCell(int x, int y) {
		return grid.get(x)[y];
	}

	// Checks if the coordinates are outside the puzzle grid
	private boolean isOutOfRange(int x, int y) {
		return x < MIN_INDEX_VALUE || y < MIN_INDEX_VALUE || x > gridRowSize - 1 || y > gridColumnSize - 1;
	}

	// Checks if it is possible to solve the puzzle
	private boolean isPuzzleSolvable() {
		return isPuzzleValid && isDictionaryValid;
	}

	// Prints the Puzzle board
	public String print() {
		// If the puzzle is not valid, the return an empty string
		if (!isPuzzleValid) {
			return "";
		}

		// Iterate the grid cells to print them
		StringBuffer buffer = new StringBuffer();
		for (var row : grid) {
			for (var cell : row) {
				buffer.append(cell.character);
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
}

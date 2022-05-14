import java.util.HashMap;
import java.util.Map;

public class Node {
	// Node represents a character
	private char character;
	
	// Holds the child nodes of a character
	private Map<Character, Node> characters;
	
	// Identifies if the current node is the end of a word
	private boolean isEndOfWord;
	
	public Node(char character) {
		this.character = character;
		this.characters = new HashMap<>();
	}
	
	// Returns character of the node
	public char getCharacter() {
		return character;
	}
	
	// Adds the child character to the node
	public void addChildCharacter(char ch) {
		characters.put(ch, new Node(ch));
	}
	
	// Checks if the node has a child character node
	public boolean hasChildCharacter(char ch) {
		return characters.containsKey(ch);
	}
	
	// Returns the child character node
	public Node getChildCharacterNode(char ch) {
		return characters.get(ch);
	}
	
	// Checks if the node is the end of a word
	public boolean isEndOfWord() {
		return isEndOfWord;
	}
	
	// Sets the node as the end of a word
	public void setEndOfWord(boolean isEndOfWord) {
		this.isEndOfWord = isEndOfWord;
	}
	
	// Returns the number of characters present as the child of the current node
	public int getCharacterSize() {
		return characters.size();
	}
}


public class Trie {
	// Character placeholder for the root node
	private static final char ROOT_NODE_CHARACTER = ' ';
	
	// Initialize the root node of the Trie
	private Node root = new Node(ROOT_NODE_CHARACTER);
	
	// Adds a word to the trie data structure
	public void add(String word) {
		// Start by setting the current variable to the root node
		Node current = root;
		
		//Iterate over each character in the word to add it to trie
		for (char ch: word.toCharArray()) {
			// If the character is not present, then add it as the child character
			if (!current.hasChildCharacter(ch)) {
				current.addChildCharacter(ch);
			} 
			// Move a step ahead to the current character node
			current = current.getChildCharacterNode(ch);
		}
		
		// Once the word gets added, mark the last character node of the word as the end of word
		current.setEndOfWord(true);
	}
	
	// Checks if the trie data structure holds any words
	// Returns false if no words are present
	public boolean hasWords() {
		return root.getCharacterSize() > 0;
	}
	
	// Returns the root node
	public Node getRootNode() {
		return root;
	}
	
	// Returns the child character node of the given character of a node
	public Node getChildCharacterNode(Node node, char ch) {
		return node.getChildCharacterNode(ch);
	}
}

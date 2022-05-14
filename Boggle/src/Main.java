import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		try (
				BufferedReader wordsStream = 
					new BufferedReader(new FileReader(new File("words.txt")));
				BufferedReader puzzleStream = 
						new BufferedReader(new FileReader(new File("puzzle.txt")));
			) {
			Boggle boggle = new Boggle();
			boggle.getDictionary(wordsStream);
			System.out.println(boggle.getPuzzle(puzzleStream));
			System.out.println(boggle.print());
			var output = boggle.solve();
			
			for (var line: output) {
				System.out.println(line);
			}
		} catch (IOException e) {
			
		}
	}

}

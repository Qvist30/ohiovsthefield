package ohiovsthefield;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Allows a user a simple choice. To live out the rest of their meaningless
 * existence in Ohio, or to play the field and hope for a winner in a evenly
 * distributed random selection of a different state. In either case a randomly
 * assigned congressional district is given, for which the user must live the
 * remainder of their lives. Travel within the state is allowed, but you may
 * never again leave your assigned state. Good Luck!
 * 
 * @author thomaskennedy
 */
public class OhioVsTheField {

	private static Map<String, List<String>> statesToDistricts = new HashMap<>(50);
	private static Random random = new Random(System.currentTimeMillis());

	/**
	 * Executes the program. Type A for Ohio, or B for the field, and then hit
	 * enter. Only total fools would ever type a different character.
	 * 
	 * @param args
	 *            No args accepted.
	 * @throws IOException
	 *             If somehow the input or the file is bad.
	 */
	public static void main(String[] args) throws IOException {
		boolean exit = false;
		Scanner scan = new Scanner(System.in);

		while(!exit) {
			System.out.println("You can choose to live in either:");
			System.out.println("A. Ohio");
			System.out.println("B. The Field");
			String choice = scan.next().substring(0, 1);
			Stream<String> stream = Files.lines(Paths.get("resources/districts.txt"));
	
			stream.forEach(s -> addToMap(s));
			stream.close();
	
			if ("A".equalsIgnoreCase(choice)) {
				printRandomDistrictChoice("Ohio");
			} else if ("B".equalsIgnoreCase(choice)) {
				printRandomChoice();
			} else {
				System.out.println(
						"You have chosen neither, so you shall be doomed to live for the remainder of eternity in Ohio.");
				printRandomDistrictChoice("Ohio");
			}
			
			System.out.println("Try Again? (y/n)");
			choice = scan.next().substring(0, 1);
			if(!"Y".equalsIgnoreCase(choice)) {
				exit = true;
			}
		}
		scan.close();
	}

	/**
	 * Randomly assigns a non-Buckeye state.
	 */
	private static void printRandomChoice() {
		List<String> states = new ArrayList<String>(statesToDistricts.keySet());
		states.remove("Ohio");
		String state = states.get(random.nextInt(states.size()));
		printRandomDistrictChoice(state);
	}

	/**
	 * Here it is, the outcome for the rest of your life.
	 */
	private static void printRandomDistrictChoice(String state) {
		List<String> districts = statesToDistricts.get(state);
		String district = districts.get(random.nextInt(districts.size()));
		System.out.println("You have been assigned to live out your life in " + state + " " + district);
	}

	/**
	 * Adds each line in the file to the district map.
	 * 
	 * @param s The line in the file.
	 */
	private static void addToMap(String s) {
		int indexOfLastSpace = s.lastIndexOf(" ");
		String state = s.substring(0, indexOfLastSpace);
		String district = s.substring(indexOfLastSpace + 1, s.length());
		if (!statesToDistricts.containsKey(state)) {
			statesToDistricts.put(state, new ArrayList<String>());
		}
		statesToDistricts.get(state).add(district);
	}
}

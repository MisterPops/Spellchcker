import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class spellChecker{

	public static void main(String[] args) throws FileNotFoundException {
		
		String dictionary;			//Default dictionary.
		String customDictionary;	//Custom dictionary.
		Scanner scan;				//File to be spell checked.
		int lineNum = 1;			//Current line number.
		
		//Load dictionaries.
		dictionary = new Scanner(new File("./dictionary.txt")).useDelimiter("\\Z").next();
		StringTokenizer dictionaryToken = new StringTokenizer(dictionary);
		
		customDictionary = new Scanner(new File("./personal_dictionary.txt")).useDelimiter("\\Z").next();
		StringTokenizer customDictionaryToken = new StringTokenizer(customDictionary);
		
		//Make hash table and load dictionaries into hash table.
		QuadraticProbingHashTable<String> dictionaryHash = new QuadraticProbingHashTable<String>(dictionaryToken.countTokens() +
				customDictionaryToken.countTokens());

		while(dictionaryToken.hasMoreTokens())
			dictionaryHash.insert(dictionaryToken.nextToken());

		while(customDictionaryToken.hasMoreTokens())
			dictionaryHash.insert(customDictionaryToken.nextToken());
		
		//Load text file to be spell checked.
		//If no file is entered use default file ./testfile.txt
		if(args.length == 0) {
			scan = new Scanner(new File("./testfile.txt"));
		} else {
			scan = new Scanner(new File(args[0]));
		}
		
		System.out.println("Misspelled words: ");
		while(scan.hasNextLine()) {
			String string = scan.nextLine();
			String[] split = string.split("[^A-Za-z0-9]+");
			
			for(String singleWord:split){
				if(!dictionaryHash.contains(singleWord.toLowerCase())) {
					System.out.println(singleWord + ": line: " + lineNum + " ");
				}
			}
			
			lineNum++;
		}
		
		scan.close();
	}

}

package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages
 * in which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the
	 * associated value is an array list of all occurrences of the keyword in
	 * documents. The array list is maintained in DESCENDING order of frequencies.
	 */
	HashMap<String, ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String, ArrayList<Occurrence>>(1000, 2.0f);
		noiseWords = new HashSet<String>(100, 2.0f);
	}

	public void printTable(HashMap<String, Occurrence> hello) {
		for (Map.Entry<String, Occurrence> entry : hello.entrySet()) {
			System.out.println(entry.getKey());
		}
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword
	 * occurrences in the document. Uses the getKeyWord method to separate keywords
	 * from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an
	 *         Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String, Occurrence> loadKeywordsFromDocument(String docFile) throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		Scanner sc = new Scanner(new File(docFile));
		HashMap<String, Occurrence> kumar = new HashMap<String, Occurrence>();
		while (sc.hasNext()) {
			String word = sc.next();
			// System.out.println(word);
			String s = getKeyword(word);
			if (s == null) {// not a keyword
				continue;
			} else {// is a keyword
				if (isDuplicate(kumar, s)) {// duplicate
					Occurrence rar = kumar.get(s);
					rar.frequency++;// increase freq by 1
				} else {// not duplicate
					Occurrence bkn = new Occurrence(docFile, 1);
					kumar.put(s, bkn);
				}
			}
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return kumar;
	}

	private boolean isDuplicate(HashMap<String, Occurrence> map, String word) {
		if (map.isEmpty()) {
			return false;
		} else {
			for (String key : map.keySet()) {
				if (key.equals(word)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex hash
	 * table. For each keyword, its Occurrence in the current document must be
	 * inserted in the correct place (according to descending order of frequency) in
	 * the same keyword's Occurrence list in the master hash table. This is done by
	 * calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String, Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for (String key : kws.keySet()) {
			ArrayList<Occurrence> occurList = new ArrayList<Occurrence>();
			if (keywordsIndex.containsKey(key)) {// duplicate
				occurList = keywordsIndex.get(key);// in master table
				Occurrence input = kws.get(key);// in input table
				occurList.add(input);// increases freq
				insertLastOccurrence(occurList);
				keywordsIndex.put(key, occurList);
			} else {
				Occurrence input = kws.get(key);
				occurList.add(input);
				insertLastOccurrence(occurList);
				keywordsIndex.put(key, occurList);
			}
		}
	}

	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of
	 * any trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!' NO
	 * OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be
	 * stripped So "word!!" will become "word", and "word?!?!" will also become
	 * "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		word = word.toLowerCase();
		if (noiseWords.contains(word)) {
			return null;
		}
		boolean isKey = isKeyword(word);
		if (isKey && hasPunc(word)) {
			// System.out.println("hasPunc");
			int k = 0;
			int puncPos = 0;
			while (k < word.length()) {
				char z = word.charAt(k);
				if (z == '.' || z == ',' || z == '?' || z == '!' || z == ':' || z == ';') {
					puncPos = k;
					break;
				} else {
					k++;
				}
			}
			word = word.substring(0, puncPos);
			word = word.toLowerCase();
			return word;
		}
		if (isKey) {
			// System.out.println("noPunc");
			word = word.toLowerCase();
			return word;
		} else {
			return null;
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
	}

	private boolean hasPunc(String word) {
		if (word.contains(".") || word.contains(",") || word.contains("?") || word.contains("!") || word.contains(":")
				|| word.contains(";")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isKeyword(String word) {
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {// if letter
				continue;
			}
			if (c == '.' || c == ',' || c == '?' || c == '!' || c == ':' || c == ';') {// if punctuation
				if (i + 1 == word.length()) {// if last char in word
					// System.out.println("jello");
					return true;
				}
				for (int j = i; j < word.length(); j++) {// checking chars after punc
					char b = word.charAt(j);
					if (b != '.' && b != ',' && b != '?' && b != '!' && b != ':' && b != ';') {// checking next char
						return false;
					} else {
						if (j + 1 == word.length()) {
							return true;
						}
						continue;
					}
				}
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in
	 * the list, based on ordering occurrences on descending frequencies. The
	 * elements 0..n-2 in the list are already in the correct order. Insertion is
	 * done by first finding the correct spot using binary search, then inserting at
	 * that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary
	 *         search process, null if the size of the input list is 1. This
	 *         returned array list is only used to test your code - it is not used
	 *         elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		if (occs.size() < 2) {
			// occs.add(e)
			return null;
		}
		ArrayList<Integer> mp = new ArrayList<Integer>();
		int l = 0;// left
		int r = occs.size() - 2;// right
		int mid = 0;
		int point = occs.get(occs.size() - 1).frequency;// int to insert
		while (l < r) {
			mid = (l + r) / 2;
			int x = occs.get(mid).frequency;// midpoint
			mp.add(mid);
			if (point > x) {// point more than midpoint
				r = mid - 1;
				continue;
			}
			if (point < x) {// point less than midpoint
				l = mid + 1;
				continue;
			}
			if (x == point) {// midpoint equal to point
				mp.add(mid);
				Occurrence temp = occs.remove(occs.size() - 1);// removing point
				occs.add(mid, temp);
				return mp;
			}
		}
		mid = (l + r) / 2;
		mp.add(mid);
		Occurrence temp = occs.remove(occs.size() - 1);// removing point
		occs.add(mid, temp);
		return mp;

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code

	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all
	 * keywords, each of which is associated with an array list of Occurrence
	 * objects, arranged in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile       Name of file that has a list of all the document file
	 *                       names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise
	 *                       word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input
	 *                               files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String, Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}

	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2
	 * occurs in that document. Result set is arranged in descending order of
	 * document frequencies.
	 * 
	 * Note that a matching document will only appear once in the result.
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. That is,
	 * if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same
	 * frequency f1, then doc1 will take precedence over doc2 in the result.
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all,
	 * result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in
	 *         descending order of frequencies. The result size is limited to 5
	 *         documents. If there are no matches, returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> text = new ArrayList<String>();
		ArrayList<Occurrence> kw1list = new ArrayList<Occurrence>();
		ArrayList<Occurrence> kw2list = new ArrayList<Occurrence>();
//		if (!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {
//			return null;
//		}
		if (keywordsIndex.containsKey(kw1)) {
			kw1list = keywordsIndex.get(kw1);
		}
		if (keywordsIndex.containsKey(kw2)) {
			kw2list = keywordsIndex.get(kw2);
		}

		ArrayList<Occurrence> finalList = new ArrayList<Occurrence>();
		finalList.addAll(kw1list);
		finalList.addAll(kw2list);

		if (kw1list.isEmpty() == false && kw2.isEmpty() == false) {
			for (int i = 0; i < finalList.size(); i++) {
				for (int k = 1; k < finalList.size() - 1; k++) {
					if (finalList.get(k - 1).frequency < finalList.get(k).frequency) {
						// sorting by preference
						Occurrence temp = finalList.get(k - 1);
						finalList.set(k - 1, finalList.get(k));
						finalList.set(k, temp);
					}
				}
			}

			for (int y = 0; y < finalList.size(); y++) {
				for (int z = y + 1; z < finalList.size(); z++) {
					if (finalList.get(y).document == finalList.get(z).document) {
						// remove dupes
						finalList.remove(z);
					}
				}
			}
		}
		while (finalList.size() > 5) {
			finalList.remove(finalList.size() - 1);
		}

		// System.out.println(finalList);

		for (Occurrence occur : finalList) {
			text.add(occur.document);
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return text;

	}
}

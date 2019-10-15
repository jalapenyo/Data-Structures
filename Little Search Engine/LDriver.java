package lse;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class LDriver {
	public static void main(String[] args) throws FileNotFoundException {

//		Scanner sc = new Scanner(System.in);
//		System.out.println("Enter File name: ");
//		String file = sc.nextLine();
		String s = "AliceCh1.txt";
		LittleSearchEngine yo = new LittleSearchEngine();
		yo.makeIndex("docs.txt", "noisewords.txt");

//		for (String key : yo.keywordsIndex.keySet()) {
//			// System.out.println("I print");
//			System.out.println(key + " " + yo.keywordsIndex.get(key).toString());
//		}

		System.out.println();
		System.out.println();

		ArrayList<String> topFive = yo.top5search("deep", "world");

		System.out.println("Top five of the two: ");

		for (int i = 0; i < topFive.size(); i++) {

			System.out.print(topFive.get(i) + ", ");

		}
		// System.out.println(yo.getKeyword(s));
		// HashMap<String, Occurrence> hello = yo.loadKeywordsFromDocument(file);
		// yo.printTable(hello);
		// System.out.println(hello.get("alice's"));

		// yo.mergeKeywords(hello);

		// ArrayList<Occurrence> occ = new ArrayList<Occurrence>();

//				Occurrence a = new Occurrence("", 12);
//				occ.add(a);
//				Occurrence b = new Occurrence("", 8);
//				occ.add(b);
//				Occurrence c = new Occurrence("", 7);
//				occ.add(c);
//				Occurrence d = new Occurrence("", 5);
//				occ.add(d);
//				Occurrence e = new Occurrence("", 3);
//				occ.add(e);
//				Occurrence f = new Occurrence("", 2);
//				occ.add(f);
//				Occurrence g = new Occurrence("", 1);
//				occ.add(g);
//				Occurrence h = new Occurrence("", 6);
//				occ.add(h);
//		
//				ArrayList<Integer> mp = yo.insertLastOccurrence(occ);
//				for (int i = 0; i < mp.size(); i++) {
//					System.out.println(mp.get(i));
//				}
	}

}

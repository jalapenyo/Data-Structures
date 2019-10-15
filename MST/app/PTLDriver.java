package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import structures.Graph;
import structures.PartialTree;
import structures.Arc;

public class PTLDriver {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		PartialTreeList PTL = new PartialTreeList();
		Graph graph = new Graph("graph4.txt");
		PTL = PTL.initialize(graph);
		Iterator<PartialTree> iter = PTL.iterator();
		while (iter.hasNext()) {
			PartialTree pt = iter.next();
			System.out.println(pt.toString());
		}

		ArrayList<Arc> MST = new ArrayList<Arc>();
		MST = PTL.execute(PTL);
	}

}

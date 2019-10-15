package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.MinHeap;
import structures.PartialTree;
import structures.Vertex;
import structures.Vertex.Neighbor;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {

	/**
	 * Inner class - to build the partial tree circular linked list
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;

		/**
		 * Next node in linked list
		 */
		public Node next;

		/**
		 * Initializes this node by setting the tree part to the given tree, and setting
		 * next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;

	/**
	 * Number of nodes in the CLL
	 */
	private int size;

	/**
	 * Initializes this list to empty
	 */
	public PartialTreeList() {
		rear = null;
		size = 0;
	}

	/**
	 * Adds a new tree to the end of the list
	 * 
	 * @param tree Tree to be added to the end of the list
	 */
	public void append(PartialTree tree) {
		Node ptr = new Node(tree);
		if (rear == null) {
			ptr.next = ptr;
		} else {
			ptr.next = rear.next;
			rear.next = ptr;
		}
		rear = ptr;
		size++;
	}

	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		PartialTreeList pt = new PartialTreeList();
		Vertex[] vertices = graph.vertices;
		for (int i = 0; i < vertices.length; i++) {
			Vertex v = vertices[i];// root
			PartialTree ptree = new PartialTree(v);// taking each vertex as a tree
			while (v.neighbors != null) {// going through neighbors of vertex
				Vertex adj = v.neighbors.vertex;// adjacent vertex
				// System.out.println("neighbor: " + adj.name);
				// System.out.println("weight: " + v.neighbors.weight);
				Arc arc = new Arc(v, adj, v.neighbors.weight);// arc with root, adj, and weight
				ptree.getArcs().insert(arc);
				v.neighbors = v.neighbors.next;
			}
			pt.append(ptree);
		}
		/* COMPLETE THIS METHOD */

		return pt;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree
	 * list for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is
	 *         irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		ArrayList<Arc> MST = new ArrayList<Arc>(); // arcs part of MST
		System.out.println();
		System.out.println("MST Components: ");
		while (ptlist.size() > 1) {

			PartialTree PTX = ptlist.remove(); // remove first partial tree from L
			MinHeap<Arc> PQX = PTX.getArcs(); // Priority Queue from first partial tree
			Vertex PTXroot = PTX.getRoot(); // Root Vertex of PTX

			Arc alpha = null;
			Vertex v2 = null;
			while (PQX.isEmpty() == false) {
				alpha = (Arc) PQX.deleteMin(); // first arc in priority queue
				v2 = alpha.getv2(); // Vertex v2 of arc alpha
				if (inSameTree(PTXroot, v2)) {
					continue; // goes to next arc in priority queue
				} else {
					break; // continue to next step
				}
			}

			MST.add(alpha); // adding arc to MST
			System.out.println(alpha.toString());

			PartialTree PTY = ptlist.removeTreeContaining(v2);

			PTX.merge(PTY); // merging PTX with v2 vertex partial tree
			// also merges all arcs between their minheaps

			ptlist.append(PTX);
		}

		return MST;
	}

	/**
	 * Removes the tree that is at the front of the list.
	 * 
	 * @return The tree that is removed from the front
	 * @throws NoSuchElementException If the list is empty
	 */
	public PartialTree remove() throws NoSuchElementException {

		if (rear == null) {
			throw new NoSuchElementException("list is empty");
		}
		PartialTree ret = rear.next.tree;
		if (rear.next == rear) {
			rear = null;
		} else {
			rear.next = rear.next.next;
		}
		size--;
		return ret;

	}

	/**
	 * Removes the tree in this list that contains a given vertex.
	 * 
	 * @param vertex Vertex whose tree is to be removed
	 * @return The tree that is removed
	 * @throws NoSuchElementException If there is no matching tree
	 */
	public PartialTree removeTreeContaining(Vertex vertex) throws NoSuchElementException {
		/* COMPLETE THIS METHOD */
		if (size == 0) {
			throw new NoSuchElementException("Tree size = 0");
		}

		Node ptNode = rear.next;
		Node prev = rear;

		if (size == 1) {
			rear = null;
			size--;
			return ptNode.tree;
		}

		do {
			if (vertex.getRoot().equals(ptNode.tree.getRoot())) {
				if (vertex.getRoot().equals(rear.tree.getRoot())) {
					prev.next = rear.next;
					rear = prev;
					size--;
					return ptNode.tree;
				}
				prev.next = ptNode.next;
				size--;
				return ptNode.tree;
			}
			ptNode = ptNode.next;
			prev = prev.next;
		} while (ptNode != rear.next);
		throw new NoSuchElementException("Tree not found");
	}

	private static boolean inSameTree(Vertex x, Vertex y) {
		Vertex xp = x;
		Vertex yp = y;

		// climbing chain of parents to find root
		while (xp.parent != null) {
			if (xp.equals(xp.parent)) {
				break;
			}
			xp = xp.parent;
		}

		while (yp.parent != null) {
			if (yp.equals(yp.parent)) {
				break;
			}
			yp = yp.parent;
		}

		if (xp.equals(yp)) {
			return true;
		}

		return false;
	}

	/**
	 * Gives the number of trees in this list
	 * 
	 * @return Number of trees
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns an Iterator that can be used to step through the trees in this list.
	 * The iterator does NOT support remove.
	 * 
	 * @return Iterator for this list
	 */
	public Iterator<PartialTree> iterator() {
		return new PartialTreeListIterator(this);
	}

	private class PartialTreeListIterator implements Iterator<PartialTree> {

		private PartialTreeList.Node ptr;
		private int rest;

		public PartialTreeListIterator(PartialTreeList target) {
			rest = target.size;
			ptr = rest > 0 ? target.rear.next : null;
		}

		public PartialTree next() throws NoSuchElementException {
			if (rest <= 0) {
				throw new NoSuchElementException();
			}
			PartialTree ret = ptr.tree;
			ptr = ptr.next;
			rest--;
			return ret;
		}

		public boolean hasNext() {
			return rest != 0;
		}

		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

	}
}

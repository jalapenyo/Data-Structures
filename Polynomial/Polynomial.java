package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {

	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage
	 * format of the polynomial is:
	 * 
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * 
	 * with the guarantee that degrees will be in descending order. For example:
	 * 
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * 
	 * which represents the polynomial:
	 * 
	 * <pre>
	 * 4 * x ^ 5 - 2 * x ^ 3 + 2 * x + 3
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients
	 *         and degrees read from scanner
	 */
	public static Node read(Scanner sc) throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node polySum = null;
		if (poly1 == null) {
			if (poly2 == null) {
				return null;
			} else {
				polySum = poly2;
				return mergeSort(polySum);
			}
		}
		if (poly2 == null) {
			polySum = poly1;
			return mergeSort(polySum);
		}
		while (poly1 != null) {
			while (poly2 != null) {
				if (poly1.term.degree > poly2.term.degree) {
					polySum = new Node(poly2.term.coeff, poly2.term.degree, polySum);
					poly2 = poly2.next;
					break;
				}
				if (poly1.term.degree < poly2.term.degree) {
					polySum = new Node(poly1.term.coeff, poly1.term.degree, polySum);
					poly1 = poly1.next;
					break;
				}
				if (poly1.term.degree == poly2.term.degree) {
					float sum = poly1.term.coeff + poly2.term.coeff;
					polySum = new Node(sum, poly1.term.degree, polySum);
					poly2 = poly2.next;
					poly1 = poly1.next;
					break;
				}
			}
			if (poly2 == null) {
				while (poly1 != null) {
					polySum = new Node(poly1.term.coeff, poly1.term.degree, polySum);
					poly1 = poly1.next;
				}
				break;
			}
			if (poly1 == null) {
				while (poly2 != null) {
					polySum = new Node(poly2.term.coeff, poly2.term.degree, polySum);
					poly2 = poly2.next;
				}
				break;
			}
		}
		return zero(mergeSort(polySum));
	}

	private static Node zero(Node poly) {
		if (poly == null) {
			return null;
		}

		Node prev = null;
		Node curr = poly;
		while (curr != null) {
			if (curr.term.coeff == 0) {
				if (prev == null) {
					poly = curr.next;
				} else {
					prev.next = curr.next;
				}
			} else {
				prev = curr;
			}
			curr = curr.next;
		}
		return poly;
	}

	private static Node mergeSort(Node poly) {
		if (poly == null || poly.next == null) {
			return poly;
		}

		Node middle = getMiddle(poly);
		Node nextOfMiddle = middle.next;
		middle.next = null;

		Node left = mergeSort(poly);
		Node right = mergeSort(nextOfMiddle);

		Node sortedList = mergeTwoListRecursive(left, right);

		return sortedList;
	}

	private static Node mergeTwoListRecursive(Node leftStart, Node rightStart) {
		if (leftStart == null)
			return rightStart;

		if (rightStart == null)
			return leftStart;

		Node temp = null;

		if (leftStart.term.degree < rightStart.term.degree) {
			temp = leftStart;
			temp.next = mergeTwoListRecursive(leftStart.next, rightStart);
		} else {
			temp = rightStart;
			temp.next = mergeTwoListRecursive(leftStart, rightStart.next);
		}
		return temp;
	}

	private static Node getMiddle(Node poly) {
		if (poly == null) {
			return poly;
		}

		Node ptr1 = poly, ptr2 = poly;

		while (ptr2 != null && ptr2.next != null && ptr2.next.next != null) {
			ptr1 = ptr1.next;
			ptr2 = ptr2.next.next;
		}
		return ptr1;
	}

	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node newPoly = null;
		if (poly1 == null || poly2 == null) {
			return null;
		}
		for (Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next) {
			for (Node ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next) {
				float coeff = ptr1.term.coeff * ptr2.term.coeff;
				int degree = ptr1.term.degree + ptr2.term.degree;
				newPoly = new Node(coeff, degree, newPoly);
			}
		}
		return mergeSort(zero(simplify(newPoly)));
	}

	private static Node simplify(Node poly) {
		if (poly == null) {
			return null;
		}

		poly = mergeSort(poly);
		Node sPoly = null;
		while(poly!=null) {
			if(poly.next==null) {
				Node temp = new Node(poly.term.coeff,poly.term.degree,null);
				sPoly=add(sPoly,temp);
				break;
			}
			int skip=0;
			Node poly2=poly;
			while(poly2!=null) {
				if(poly.term.degree==poly2.term.degree) {
					skip++;
					Node temp = new Node(poly2.term.coeff,poly2.term.degree,null);
					sPoly=add(sPoly,temp);
				}
				poly2=poly2.next;
			}
			while(skip!=0) {
				poly=poly.next;
				skip--;
			}
		}
		
		return sPoly;
		
		//System.out.println("*");
		
		
//		Node sPoly = new Node(poly.term.coeff, poly.term.degree, null);
//
//		while (poly != null) {
//			if (poly.next == null) {
//				break;
//			}
//
//			Node temp = new Node(poly.next.term.coeff, poly.next.term.degree, null);
//			Node ayy = add(sPoly, temp);
//			sPoly = new Node(ayy.term.coeff, ayy.term.degree, sPoly);
//			poly=poly.next;
//			
//		}
//		return sPoly;
	}

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x    Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (poly == null) {
			return 0;
		}

		float eval = 0;
		for (Node ptr = poly; ptr != null; ptr = ptr.next) {
			eval += (float) (ptr.term.coeff * Math.pow(x, ptr.term.degree));
		}
		return eval;
	}

	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		}

		String retval = poly.term.toString();
		for (Node current = poly.next; current != null; current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}
}

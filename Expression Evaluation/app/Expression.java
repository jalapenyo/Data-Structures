package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is
	 * created and stored, even if it appears more than once in the expression. At
	 * this time, values for all variables and all array items are set to zero -
	 * they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr   The expression
	 * @param vars   The variables array list - already created by the caller
	 * @param arrays The arrays array list - already created by the caller
	 */
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		/**
		 * DO NOT create new vars and arrays - they are already created before being
		 * sent in to this method - you just need to fill them in.
		 **/
		expr = expr.replaceAll("\\s", "");
		expr = expr.trim();
		StringTokenizer sp = new StringTokenizer(expr, delims, false);
		while (sp.hasMoreTokens()) {
			boolean repeat = false;
			String token = sp.nextToken();
			// System.out.println(token);
			int p = expr.indexOf(token);
			int p2 = p + token.length();
			if (isNumber(token) == false) {
				Variable var = new Variable(token);
				for (int i = 0; i < vars.size(); i++) {
					if (vars.get(i).equals(var)) {
						repeat = true;
						break;
					}
				}
				if (repeat) {
					continue;
				}
				vars.add(var);
				continue;
			}
			if (isNumber(token) == true) {
				if (sp.hasMoreTokens() == false) {
					return;
				}
				continue;
			}

			if (expr.charAt(p2) == '[') {
				Array arr = new Array(token);
				for (int i = 0; i < arrays.size(); i++) {
					if (arrays.get(i).equals(arr)) {
						repeat = true;
						break;
					}
				}
				if (repeat) {
					continue;
				}
				arrays.add(arr);
				continue;
			}

		}
//    	System.out.println("varslist: ");
//    	for(int i=0;i<vars.size();i++) {
//    		System.out.println(vars.get(i).name);
//    	}
//    	System.out.println("arrayslist: ");
//    	for(int j=0;j<arrays.size();j++) {
//    		System.out.println(arrays.get(j).name);
//    	}
	}

	public static boolean isNumber(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException If there is a problem with the input
	 * @param vars   The variables array list, previously populated by
	 *               makeVariableLists
	 * @param arrays The arrays array list - previously populated by
	 *               makeVariableLists
	 */
	public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays)
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;
				}
			}
		}
	}

//	public static void eval(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
//		
//	}
	/**
	 * Evaluates the expression.
	 * 
	 * @param vars   The variables array list, with values for all variables in the
	 *               expression
	 * @param arrays The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */
	public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		// following line just a placeholder for compilation
		expr = expr.replace("\\s", "");
		expr = expr.trim();
		String temp = expr;

		if (temp.lastIndexOf("(") > temp.indexOf(")")) {// (a+b)*(c+d)
			int firstparen = 0;
			int lastparen = 0;
			for (int i = temp.length() - 1; i >= 0; i--) {
				if (temp.charAt(i) == '(') {
					firstparen = i;
					break;
				}
			}
			for (int j = firstparen; j < temp.length(); j++) {
				if (temp.charAt(j) == ')') {
					lastparen = j;
					break;
				}
			}
			temp = temp.substring(firstparen + 1, lastparen);
			float x = evaluate(temp, vars, arrays);
			String y = Float.toString(x);
			expr = expr.substring(0, firstparen) + y + expr.substring(lastparen + 1);
			return evaluate(expr, vars, arrays);
		}
		int lastparen = temp.lastIndexOf(")");
		int firstparen = temp.indexOf("(");
		if (lastparen > -1) {// nested par
			temp = temp.substring(firstparen + 1, lastparen);
			float x = evaluate(temp, vars, arrays);
			String y = Float.toString(x);
			expr = expr.substring(0, firstparen) + y + expr.substring(lastparen + 1);
			return evaluate(expr, vars, arrays);
		}
//		if (expr.contains("[")) {
//			for (int l = 0; l < expr.length(); l++) {
//				String c = expr.substring(l, l + 1);
//				if (c.equals("*") || c.equals("/") || c.equals("+") || c.equals("-")) {
//					continue;
//				}
//				if (isNumber(c) == false) {
//					int brVar = l;// bracket variable location
//					// boolean isArr=false;
//					for (int g = l; g <= expr.length(); g++) {
//						String d = expr.substring(l, l + 1);
//						if (isNumber(d)) {
//							break;
//						}
//						if (c.equals("*") || c.equals("/") || c.equals("+") || c.equals("-")) {
//							break;
//						}
//						if (expr.substring(l, l + 1).equals("[")) {
//							// isArr=true;
//							int fbrLoc = g;
//							int lbrLoc = expr.lastIndexOf("]");
//							temp = temp.substring(fbrLoc + 1, lbrLoc);
//							int arrindex = (int) evaluate(temp, vars, arrays);
//							int arr=arrays.indexOf(expr.substring(brVar,g));
//							Array ar=arrays.get(arr);
//							expr=expr.substring(0,brVar)+
//						} else {
//							continue;
//						}
//					}
//					continue;
//				} else {
//					continue;
//				}
//			}
//		}
//		Stack<String> brackets = new Stack();
//		StringTokenizer br = new StringTokenizer(expr,delims,true);
//		String brtok=br.nextToken();
//		while(br.countTokens()>=1) {
//			if(isNumber(brtok) == false) {
//				int brVar=
//				brtok=br.nextToken();
//				if(brtok.equals("[")) {
//					
//					int bracketLoc=expr.charAt('[');
//					for(int)
//				}
//			}
//			brtok=br.nextToken();
//		}
//		if (temp.contains("[")) {
////			if(temp.lastIndexOf("(") > temp.indexOf(")")){
////				
////			}
//			int fbrak = temp.lastIndexOf("[");
//			int lbrak = temp.indexOf("]");
//			temp = temp.substring(fbrak + 1, lbrak);
//			int x = (int) evaluate(temp, vars, arrays);
//			
//			String y = Float.toString(x);
//		}

		Stack<Float> num = new Stack();
		Stack<String> op = new Stack();
		StringTokenizer sp = new StringTokenizer(expr, delims, true);
		String Token = sp.nextToken();
		if (sp.countTokens() <= 1) {
			if (Token.equals("-")) {
				float result = Float.valueOf(expr);
				return result;
			}
			if (Token.contains(".") || isNumber(Token)) {
				float result = Float.valueOf(expr);
				return result;
			}
			if (isNumber(Token) == false) {
				for (int j = 0; j < vars.size(); j++) {
					if (vars.get(j).name.equals(Token)) {
						return ((float) vars.get(j).value);
					}
				}
			}
		}

		while (sp.countTokens() >= 0) {
			// checking if op
			if (Token.equals("*") || Token.equals("/") || Token.equals("+") || Token.equals("-")) {
				op.push(Token);
				Token = sp.nextToken();
				continue;
			}

			if (Token.contains(".") || isNumber(Token)) {// checking if float or int
				num.push(Float.valueOf(Token));
				if (sp.countTokens() == 0 && num.isEmpty() == true) {// only number in expr
					return num.pop();
				}
			}

			if (isNumber(Token) == false) {// checking if variable
				// System.out.println(vars.size());
				for (int j = 0; j < vars.size(); j++) {
					if (vars.get(j).name.equals(Token)) {
						num.push((float) vars.get(j).value);
						break;
					}
				}
				if (sp.countTokens() == 0 && num.isEmpty() == true) {// only variable in expr
					return num.pop();
				}
			}

			if (op.size() > 0) {// checks if there are op
				if (op.peek().equals("*") || op.peek().equals("/")) {// checks for * or /
					if ((op.size() + 1) == (num.size())) {// checks if operator can be used
						if (num.size() >= 2) { // with at least 2 numbers
							if (op.peek().equals("*")) {// multiply
								float b = num.pop();
								float a = num.pop();
								float product = a * b;
								op.pop();
								num.push(product);
								if (sp.hasMoreTokens()) {// if there are more token in expr
									Token = sp.nextToken();
									continue;
								}
								if (num.size() == 1) {// last value in expr? && and no more tokens
									return num.pop();
								} else {
									break;// go to add and subtract loop
								}
							}
							if (op.peek().equals("/")) {
								float b = Float.valueOf(num.pop());
								float a = Float.valueOf(num.pop());
								float quotient = a / b;
								op.pop();
								num.push(quotient);
								if (sp.hasMoreTokens()) {
									Token = sp.nextToken();
									continue;
								}
								if (num.size() == 1) {
									return num.pop();
								} else {
									break;// go to add and subtract loop
								}
							}
						}
					}
					Token = sp.nextToken();
					continue;
				}
				if (sp.hasMoreTokens()) {// there is a + or - in the op
					Token = sp.nextToken();
					continue;
				}
			} // no op
			if (sp.hasMoreTokens()) {
				Token = sp.nextToken();
			} else {
				break;
			}
		}
		// if token=[ or (\

		if (op.peek().equals("+") || op.peek().equals("-")) {// it should eval to this
			do {
				if (num.size() >= 2) {
					if (op.peek().equals("+")) {
						if (op.size() == 1) {
							float b = Float.valueOf(num.pop());
							float a = Float.valueOf(num.pop());
							float sum = a + b;
							op.pop();
							if (num.isEmpty()) {
								return sum;
							} else {
								num.push(sum);
							}
						}
						if (op.size() > 1) {
							String isMinus = op.pop();
							if (op.peek().equals("-")) {// if previous number is negative
								op.pop();
								float b = Float.valueOf(num.pop());
								float a = Float.valueOf(num.pop()) * -1;
								float sum = a + b;
								op.push("+");
								if (num.isEmpty()) {
									return sum;
								} else {
									num.push(sum);
									continue;
								}
							} else {// if previous number is NOT negative
								op.push(isMinus);
								float b = Float.valueOf(num.pop());
								float a = Float.valueOf(num.pop());
								float sum = a + b;
								op.pop();
								if (num.isEmpty()) {
									return sum;
								} else {
									num.push(sum);
									continue;
								}
							}
						}
					}
					if (op.peek().equals("-")) {
						if (op.size() == 1) {
							float b = Float.valueOf(num.pop());
							float a = Float.valueOf(num.pop());
							float difference = a - b;
							op.pop();
							if (num.isEmpty()) {
								return difference;
							} else {
								num.push(difference);
								continue;
							}
						}
						if (op.size() > 1) {
							String isMinus = op.pop();
							if (op.peek().equals("-")) {
								op.pop();
								float b = Float.valueOf(num.pop());
								float a = Float.valueOf(num.pop()) * -1;
								float difference = a - b;
								op.push("+");
								if (num.isEmpty()) {
									return difference;
								} else {
									num.push(difference);
									continue;
								}
							} else {
								op.push(isMinus);
								float b = Float.valueOf(num.pop());
								float a = Float.valueOf(num.pop());
								float difference = a - b;
								op.pop();
								if (num.isEmpty()) {
									String f = Float.toString(difference);
									return difference;
								} else {
									num.push(difference);
									continue;
								}
							}
						}
					}
				}
			} while (op.peek().equals("+") || op.peek().equals("-"));
		}

		return 0;
	}
}

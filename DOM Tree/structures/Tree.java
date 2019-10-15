package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode,
 * with fields for tag/text, first child and sibling.
 * 
 */
public class Tree {

	/**
	 * Root node
	 */
	TagNode root = null;

	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;

	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}

	/**
	 * Builds the DOM tree from input HTML file, through scanner passed in to the
	 * constructor and stored in the sc field of this object.
	 * 
	 * The root of the tree that is built is referenced by the root field of this
	 * object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		ArrayList<TagNode> tags = new ArrayList<TagNode>();

		root = new TagNode("<html>", null, null);
		tags.add(root);
		if (!sc.hasNext()) {
			return;
		}
		System.out.println(sc.nextLine());

		while (sc.hasNextLine()) {// all lines into arraylist as TagNodes
			String token = sc.nextLine();
			TagNode p = new TagNode(token, null, null);
			tags.add(p);
		}

		int emb = 0;
		int p = 0;
		int table = 0;
		int tr = 0;
		int td = 0;

		// Stack<TagNode> lists = new Stack<TagNode>();
		Stack<Integer> listsPos = new Stack<Integer>();
		Stack<Integer> itemPos = new Stack<Integer>();

		for (int i = 0; i < tags.size(); i++) {

			TagNode node = tags.get(i);
			// System.out.println(node.tag);
			String token = node.tag;

			if (token.contains("<") && token.contains("/") && token.contains(">")) {// if closing bracket skip
				if (token.equals("</em>") || token.equals("</b>")) {
					TagNode next = tags.get(i + 1);// node after /em or /b
					String x = next.tag;
					if (!x.contains("<") && !x.contains(">")) {// if next tag is plain text
						tags.get(emb).sibling = next;// assign sibling of em or b to next tag
					}
				}
				if (token.equals("</p>")) {
					TagNode next = tags.get(i + 1);// node after /p
					String x = next.tag;
					if (x.contains("<") && x.contains(">") && !x.contains("/")) {// if next tag is an opening tag
						tags.get(p).sibling = next;// assign sibling of p to next tag
					}
				}
				if (token.equals("</tr>")) {
					TagNode next = tags.get(i + 1);// node after /tr
					String x = next.tag;
					if (x.equals("<tr>")) {// if next tag is tr
						tags.get(tr).sibling = next;// assign sibling of tr to next tag
					}
				}
				if (token.equals("</td>")) {
					TagNode next = tags.get(i + 1);// node after /td
					String x = next.tag;
					if (x.equals("<td>")) {// if next tag is td
						tags.get(td).sibling = next;// assign sibling of td to next tag
					}
				}
				if (token.equals("</table>")) {
					TagNode next = tags.get(i + 1);// node after /table
					String x = next.tag;
					if (x.contains("<") && x.contains(">") && !x.contains("/")) {// if next tag is an opening tag
						tags.get(table).sibling = next;// assign sibling of table to next tag
					}
				}
				if (token.equals("</ol>") || token.equals("</ul>")) {
					TagNode next = tags.get(i + 1);// node after /ol or/ul
					String x = next.tag;
					int pos = listsPos.pop();
					if (x.contains("<") && x.contains(">") && !x.contains("/")) {// if next tag is an opening tag
						tags.get(pos).sibling = next;// assign sibling of table to next tag
					}
				}
				if (token.equals("</li>")) {
					TagNode next = tags.get(i + 1);// node after /li
					String x = next.tag;
					int pos = itemPos.pop();// pops out li position
					if (x.equals("<li>")) {// if next tag is li
						tags.get(pos).sibling = next;// assign sibling of li to next li in same list
					}
				}
				continue;
			}

			if (token.contains("<") && token.contains(">")) {// if it is a tag
				node.tag = token.substring(1, token.length() - 1);// getting rid of brackets
				TagNode next = tags.get(i + 1);// next node to point to
				node.firstChild = next;
				if (token.equals("<em>") || token.equals("<b>")) {
					emb = i;// pos of em or b
				}
				if (token.equals("<p>")) {
					p = i;// pos of p
				}
				if (token.equals("<tr>")) {
					tr = i;// pos of tr
				}
				if (token.equals("<td>")) {
					td = i;// pos of td
				}
				if (token.equals("<ol>")) {
					listsPos.push(i);// pushing in arraylist position of ol
				}
				if (token.equals("<ul>")) {
					listsPos.push(i);// pushing in arraylist position of ul
				}
				if (token.equals("<li>")) {
					itemPos.push(i);// pushing arralist position of li
				}
				if (token.equals("<table>")) {
					table = i;// pos of table
				}
			}

			else {// if it is plain text
				TagNode next = tags.get(i + 1);// next node to point to
				String nextTag = next.tag;
				node.firstChild = null;// first child is always null if text
				if (nextTag.equals("<em>") || nextTag.equals("<b>") || nextTag.equals("<ol>")
						|| nextTag.equals("<ul>")) {// if next tag is em,b,ol,ul
					node.sibling = next;// assign sibling to em or b
				}
			}

		} // for loop

	}

	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		replaceTag2(oldTag, newTag, root);
	}

	private void replaceTag2(String oldTag, String newTag, TagNode node) {
		if (node == null) {
			return;
		}
		if (node.tag.equals(oldTag)) {
			node.tag = newTag;
		}
		replaceTag2(oldTag, newTag, node.firstChild);
		replaceTag2(oldTag, newTag, node.sibling);
	}

	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The
	 * boldface (b) tag appears directly under the td tag of every column of this
	 * row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		int count = 1;
		boldRow2(row, root, count);
	}

	private void boldRow2(int row, TagNode node, int count) {
		if (node == null) {
			return;
		}
		if (node.tag.equals("tr")) {
			if (count == row) {
				boldRow3(node.firstChild);// first column of bolded row
			} else {
				boldRow2(row, node.sibling, count + 1);// goes to next row
			}
		} else {
			boldRow2(row, node.firstChild, count);
			boldRow2(row, node.sibling, count);
		}
	}

	private void boldRow3(TagNode node) {
		if (node == null) {
			return;
		}
		TagNode temp = node.firstChild;// content within column
		TagNode bold = new TagNode("b", temp, null);
		node.firstChild = bold;
		boldRow3(node.sibling);// next column
	}

	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b,
	 * all occurrences of the tag are removed. If the tag is ol or ul, then All
	 * occurrences of such a tag are removed from the tree, and, in addition, all
	 * the li tags immediately under the removed tag are converted to p tags.
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		if (tag.equals("p")) {
			removeP(tag, root);
		}
		if (tag.equals("b") || tag.equals("em")) {
			removeBEM(tag, root);
		} else {// ol or ul
			removeL(tag, root);
			replaceTag("nikhilkumar", "p");
		}
	}

	private void removeP(String tag, TagNode node) {
		if (node == null) {
			return;
		}
		if (node.firstChild == null) {
			if (node.sibling != null) {
				if (node.sibling.tag.equals("p")) {
					TagNode p = node.sibling;// p node
					TagNode content = p.firstChild;// content under p node
					node.sibling = content;// skipping p
					while (content.sibling != null) {// traversing to end of content
						content = content.sibling;
					}
					content.sibling = p.sibling;
					removeP(tag, content);
				}
			}
			removeP(tag, node.sibling);
			return;
		}
		if (node.firstChild.tag.equals("p")) {
			TagNode p = node.firstChild;// p node
			TagNode content = p.firstChild;// content under p node
			node.firstChild = content;// skipping <p>
			while (content.sibling != null) {// traversing to end of content
				content = content.sibling;
			}
			content.sibling = p.sibling;// assigning end of content to next sibling
			removeP(tag, content);
		}
		if (node.sibling == null) {
			removeP(tag, node.firstChild);
			return;
		}
		if (node.sibling.tag.equals("p")) {
			TagNode p = node.sibling;// p node
			TagNode content = p.firstChild;// content under p node
			node.sibling = content;// skipping p
			while (content.sibling != null) {// traversing to end of content
				content = content.sibling;
			}
			content.sibling = p.sibling;
			removeP(tag, content);
		} else {
			removeP(tag, node.firstChild);
			removeP(tag, node.sibling);
		}
	}

	private void removeBEM(String tag, TagNode node) {
		if (node == null) {
			return;
		}
		if (node.firstChild == null) {
			if (node.sibling != null) {// if node is plain text
				if (node.sibling.tag.equals(tag)) {
					TagNode bem = node.sibling;
					TagNode content = bem.firstChild;
					node.sibling = content;
					content.sibling = bem.sibling;
					bem.firstChild = null;
					removeBEM(tag, node);
				}
			}
			removeBEM(tag, node.sibling);
			return;
		}
		if (node.sibling == null) {
			removeBEM(tag, node.firstChild);
			return;
		}
		if (node.firstChild.tag.equals(tag)) {
			TagNode bem = node.firstChild;// b or em tag
			TagNode content = bem.firstChild;// content within tag
			node.firstChild = content;// skipping tag and pointing to content
			content.sibling = bem.sibling;// pointing content to bem sibling
			bem.firstChild = null;
			removeBEM(tag, node);// returning node that points to content
		}
		if (node.sibling.tag.equals(tag)) {
			TagNode bem = node.sibling;
			TagNode content = bem.firstChild;
			node.sibling = content;
			content.sibling = bem.sibling;
			bem.firstChild = null;
			removeBEM(tag, node);
		} else {
			removeBEM(tag, node.firstChild);
			removeBEM(tag, node.sibling);
		}
	}

	private void removeL(String tag, TagNode node) {
		if (node == null) {
			return;
		}
		if (node.firstChild == null) {
			if (node.sibling != null) {
				if (node.sibling.tag.equals(tag)) {
					TagNode olul = node.sibling;
					TagNode list = olul.firstChild;
					node.sibling = list;
					list.tag = "nikhilkumar";
					while (list.sibling != null) {
						list = list.sibling;
						list.tag = "nikhilkumar";
					}
					list.sibling = olul.sibling;
					removeL(tag, node.sibling);
				}
			}
			removeL(tag, node.sibling);
			return;
		}
		if (node.firstChild.tag.equals(tag)) {
			TagNode olul = node.firstChild;// olul tag
			TagNode list = olul.firstChild;// list tag under olul tag
			node.firstChild = list;// skipping olul tag
			list.tag = "nikhilkumar";
			while (list.sibling != null) {// traversing through list tags
				list = list.sibling;
				list.tag = "nikhilkumar";
			}
			list.sibling = olul.sibling;// last list item sibling points to olul sibling
			removeL(tag, node.firstChild);
		}
		if (node.sibling == null) {
			removeL(tag, node.firstChild);
			return;
		}
		if (node.sibling.tag.equals(tag)) {
			TagNode olul = node.sibling;
			TagNode list = olul.firstChild;
			node.sibling = list;
			list.tag = "nikhilkumar";
			while (list.sibling != null) {
				list = list.sibling;
				list.tag = "nikhilkumar";
			}
			list.sibling = olul.sibling;
			removeL(tag, node.sibling);
		} else {
			removeL(tag, node.firstChild);
			removeL(tag, node.sibling);
		}
	}

	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag  Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		addTag2(word, tag, root);
	}

	public void addTag2(String word, String tag, TagNode node) {
		if (node == null) {
			return;
		}
		if (node.firstChild != null) {
			if (node.tag.equals(tag)) {
				addTag2(word, tag, node.sibling);
				addTag2(word, tag, node.firstChild);
				return;
			}
			TagNode node2 = node.firstChild;
			if (node2.firstChild == null) {// if node is text
				int l = node2.tag.length() - word.length();
				int pos = 0;
				boolean atEnd = false;
				boolean begin = false;
				boolean isWord = false;
				for (int i = 0; i < l + 1; i++) {
					String seq = node2.tag.substring(i, i + word.length());
					if (seq.equalsIgnoreCase(word)) {
						isWord = true;
						pos = i;// position
						if (pos == 0 || node2.tag.charAt(pos - 1) == ' ') {// checking char before word
							if (pos == 0) {// at the beginning?
								begin = true;
							}
							if (node2.tag.substring(pos).equals(word)) {
								atEnd = true;// at the end of text
							}
						} else {// if char before word is any other char, continue
							isWord = false;
							continue;
						}
						if (atEnd == false) {// checking char after word
							char punc = node2.tag.charAt(pos + word.length());
							if (punc == '\'') {// checking apostrophe
								isWord = false;
								continue;
							}
							if (punc == '.' || punc == '!' || punc == '?' || punc == ',' || punc == ';'
									|| punc == ':') {// adding punctuation to word
								word = node2.tag.substring(pos, pos + word.length());
								int pos2 = node2.tag.indexOf(word) + word.length();// pos after last char in word
								if (pos2 == node2.tag.length()) {// if punctuation is last char in text
									atEnd = true;// word becomes atEnd
									break;
								}
								char punc2 = node2.tag.charAt(pos + word.length() + 1);
								if (punc2 == '.' || punc2 == '!' || punc2 == '?' || punc2 == ',' || punc2 == ';'
										|| punc2 == ':') {
									// extra punctuation, continue
									isWord = false;
									continue;
								} else {
									break;// anything after punctuation is good
								}
							}
							if (punc == ' ') {
								break;// if space after word proceed out of loop
							} else {// if there is any other character ahead of it
								isWord = false;
								continue;
							}
						} else {// word is at end of text
							break;
						}
					} // if seq
				} // for loop
				if (isWord == false) {
					addTag2(word, tag, node.firstChild);
					addTag2(word, tag, node.sibling);
					return;
				}
				if (begin) {
					TagNode tagger = new TagNode(tag, null, null);// em or b
					TagNode content = new TagNode(word, null, null);// tagged word
					tagger.firstChild = content;
					node.firstChild = tagger;
					if (atEnd) {// if text only has the word int it
						// node.firstChild = tagger;
						// node2.tag=node2.tag.substring(0,pos);
						tagger.sibling = node2.sibling;
						// node2.sibling=content;
						addTag2(word, tag, tagger);
						addTag2(word, tag, node.sibling);
						addTag2(word, tag, node.firstChild);
						return;
					}
					node2.tag = node2.tag.substring(word.length());
					// cutting word out from beginning
					tagger.sibling = node2;
					addTag2(word, tag, tagger);
					addTag2(word, tag, node.sibling);
					addTag2(word, tag, node.firstChild);
					return;
				}
				if (atEnd) {
					TagNode tagger = new TagNode(tag, null, null);// em or b
					TagNode content = new TagNode(word, null, null);// tagged word
					node2.tag = node2.tag.substring(0, pos);// cutting word from end out
					TagNode temp = node2.sibling;
					node2.sibling = tagger;
					tagger.firstChild = content;
					tagger.sibling = temp;
					addTag2(word, tag, tagger);
					addTag2(word, tag, node.sibling);
					addTag2(word, tag, node.firstChild);
				} else {// in middle of text
					TagNode tagger = new TagNode(tag, null, null);// em or b
					TagNode content = new TagNode(word, null, null);// tagged word
					String n2 = node2.tag.substring(pos + word.length());// last half of text
					node2.tag = node2.tag.substring(0, pos);// first half of text
					TagNode splitN2 = new TagNode(n2, null, null);// last half text node
					TagNode temp = node2.sibling;
					node2.sibling = tagger;
					tagger.firstChild = content;
					tagger.sibling = splitN2;
					splitN2.sibling = temp;
					addTag2(word, tag, tagger);
					addTag2(word, tag, node.sibling);
					addTag2(word, tag, node.firstChild);
					return;
				}
			} else {// if node is not text
				addTag2(word, tag, node.firstChild);
				addTag2(word, tag, node.sibling);
				// addTag2(word, tag, node2.firstChild);
				// addTag2(word, tag, node2.sibling);
				return;
			}
		}
		if (node.sibling != null) {
			TagNode node1 = node.sibling;
			if (node1.firstChild == null) {// if node is text
				int l = node1.tag.length() - word.length();
				int pos = 0;
				boolean atEnd = false;
				boolean begin1 = false;
				boolean isWord1 = false;
				for (int i = 0; i < l + 1; i++) {
					String seq = node1.tag.substring(i, i + word.length());
					if (seq.equalsIgnoreCase(word)) {
						isWord1 = true;
						pos = i;// position
						if (pos == 0 || node1.tag.charAt(pos - 1) == ' ') {// checking char before word
							if (pos == 0) {// at the beginning?
								begin1 = true;
							}
							if (node1.tag.substring(pos).equals(word)) {
								atEnd = true;// at the end of text
							}
						} else {// if char before word is any other char, continue
							isWord1 = false;
							continue;
						}
						if (atEnd == false) {// checking char after word
							char punc = node1.tag.charAt(pos + word.length() + 1);
							if (punc == '\'') {// checking apostrophe
								isWord1 = false;
								continue;
							}
							if (punc == '.' || punc == '!' || punc == '?' || punc == ',' || punc == ';'
									|| punc == ':') {// adding punctuation to word
								word = node1.tag.substring(pos, pos + word.length());
								int pos2 = node1.tag.indexOf(word) + word.length();// pos after last char in word
								if (pos2 == node1.tag.length()) {// if punctuation is last char in text
									atEnd = true;// word becomes atEnd
									break;
								}
								char punc2 = node1.tag.charAt(pos + word.length() + 1);
								if (punc2 == '.' || punc2 == '!' || punc2 == '?' || punc2 == ',' || punc2 == ';'
										|| punc2 == ':') {
									// extra punctuation, continue
									isWord1 = false;
									continue;
								} else {
									break;// anything after punctuation is good
								}
							}
							if (punc == ' ') {
								break;// if space after word proceed out of loop
							} else {// if there is any other character ahead of it
								isWord1 = false;
								continue;
							}
						} else {// word is at end of text
							break;
						}
					} // if seq
				} // for loop
				if (isWord1 == false) {
					addTag2(word, tag, node.firstChild);
					addTag2(word, tag, node.sibling);
					return;
				}
				if (begin1) {
					TagNode tagger = new TagNode(tag, null, null);// em or b
					TagNode content = new TagNode(word, null, null);// tagged word
					tagger.firstChild = content;
					node.sibling = tagger;
					if (atEnd) {// if text only has the word int it
						// node.firstChild = tagger;
						// node2.tag=node2.tag.substring(0,pos);
						tagger.sibling = node1.sibling;
						// node2.sibling=content;
						addTag2(tag, word, tagger);
						// addTag2(tag, word, node.sibling);
						return;
					}
					node1.tag = node1.tag.substring(word.length());
					// cutting word out from beginning
					tagger.sibling = node1;
					addTag2(word, tag, tagger);
					// addTag2(tag, word, node.sibling);
					return;
				}
				if (atEnd) {
					TagNode tagger = new TagNode(tag, null, null);// em or b
					TagNode content = new TagNode(word, null, null);// tagged word
					node1.tag = node1.tag.substring(0, pos);// cutting word from end out
					TagNode temp = node1.sibling;
					node1.sibling = tagger;
					tagger.firstChild = content;
					tagger.sibling = temp;
					addTag2(word, tag, tagger);
					// addTag2(tag, word, node.sibling);
				} else {// in middle of text
					TagNode tagger = new TagNode(tag, null, null);// em or b
					TagNode content = new TagNode(word, null, null);// tagged word
					String n2 = node1.tag.substring(pos + word.length());// last half of text
					node1.tag = node1.tag.substring(0, pos);// first half of text
					TagNode splitN2 = new TagNode(n2, null, null);// last half text node
					TagNode temp = node1.sibling;
					node1.sibling = tagger;
					tagger.firstChild = content;
					tagger.sibling = splitN2;
					splitN2.sibling = temp;
					addTag2(word, tag, tagger);
					// addTag2(tag, word, node.sibling);
					return;
				}
			} else {// if node is not text
				// addTag2(word, tag, node1.firstChild);
				// addTag2(word, tag, node1.sibling);
				addTag2(word, tag, node.sibling);
				addTag2(word, tag, node.firstChild);
				return;
			}
		}

	}

	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes new
	 * lines, so that when it is printed, it will be identical to the input file
	 * from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines.
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}

	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr = root; ptr != null; ptr = ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");
			}
		}
	}

	/**
	 * Prints the DOM tree.
	 *
	 */
	public void print() {
		print(root, 1);
	}

	private void print(TagNode root, int level) {
		for (TagNode ptr = root; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < level - 1; i++) {
				System.out.print("      ");
			}
			;
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level + 1);
			}
		}
	}
}

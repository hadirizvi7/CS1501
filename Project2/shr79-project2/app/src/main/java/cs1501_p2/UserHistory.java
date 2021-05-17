package cs1501_p2;

import java.util.*;

/*
Adaptation of an R-way trie using textbook authors code (Algorithms 4th Edition, specifically TrieST.java)

Link: https://algs4.cs.princeton.edu/52trie/TrieST.java.html 

Other Sources used to implement JCL functionality:

Hashmap:
https://www.w3schools.com/java/java_hashmap.asp

Priority Queue (Max Heap):
https://www.geeksforgeeks.org/priority-queue-class-in-java-2/

 */

public class UserHistory implements Dict
{

	private Node root;
	private int n;
	private String currPrefix;
	private HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
	private PriorityQueue<Integer> pq = new PriorityQueue<Integer>(Collections.reverseOrder());

	public UserHistory() {
		root = new Node();
		n = 0;
		currPrefix = "";
	}
	public String getPrefix()
	{
		return currPrefix;
	}

	public void add(String key)
	{
		if (contains(key)) {
			root = addRec(root, key, 0);
			return;
		}
		root = addRec(root, key, 0);
		n += 1;
	}

	private Node addRec(Node curr, String key, int i)
	{
		if (curr == null)
		{
			curr = new Node();
		}
		if (i == key.length()) 
		{
			curr.value += 1;
			return curr;
		}

		curr.next[(int)key.charAt(i)] = addRec(curr.next[(int)key.charAt(i)], key, i+1);
		return curr;
	}

	public boolean contains(String key)
	{
		Node result = containsRec(root, key, 0);
		if (result == null || result.value == 0) return false;
		return true;
	}

	private Node containsRec(Node curr, String key, int i)
	{
		if (curr == null) return null;

		if (i == key.length())
		{
			if (curr.value > 0)
			{
				return curr;
			}
			else return null;
		}

		return containsRec(curr.next[(int)key.charAt(i)], key, i+1);
	}

	public boolean containsPrefix(String pre)
	{
		Node curr = root;

		for (int i = 0; i < pre.length(); i++)
		{
			if (curr.next[(int)pre.charAt(i)] == null) {
				return false;
			}
			curr = curr.next[(int)pre.charAt(i)];
		}

		return true;
	}

	public int searchByChar(char next)
	{
		currPrefix += next;
		Node curr = root;
		boolean isPrefix = false;
		boolean isWord = false;

		for (int i = 0; i < currPrefix.length(); i++)
		{
			curr = curr.next[(int)currPrefix.charAt(i)];

			if (curr == null) return -1;

			if (i == currPrefix.length() - 1) {
				if (curr.value > 0) isWord = true;
				else isPrefix = true;
				for (int j = 0; j < 256; j++) 
				{
					if (curr.next[j] != null)
					{
						isPrefix = true;
					}
				}
			}

		}

		if (isPrefix && !isWord) return 0;

		else if (!isPrefix && isWord) return 1;

		else if (isPrefix && isWord) return 2;

		else return -1;

	}

	public void resetByChar()
	{
		currPrefix = "";
		hashmap = new HashMap<String, Integer>();
		pq = new PriorityQueue<Integer>(Collections.reverseOrder());
	}

	public ArrayList<String> suggest()
	{
		ArrayList<String> list = new ArrayList<String>();
		Node curr = root;

		for (int i = 0; i < currPrefix.length(); i++)
		{
			curr = curr.next[(int)currPrefix.charAt(i)];
			if (curr == null) return list;

			if (i == currPrefix.length() - 1)
			{
				suggestRec(curr, currPrefix);
			}
		}
		
		String[] pList = new String[5];
		int x = 0;
		while (pq.size() != 0)
		{
			if (x == 5) break;
			int max = pq.peek();
			for (String word : hashmap.keySet())
			{
				if (x == 5) break;
				if (hashmap.get(word) == max)
				{
					pList[x] = word;
					x += 1;
					pq.poll();
				}
			}
		}
		ArrayList<String> returnList = new ArrayList<String>();

		for (String word : pList)
		{
			if (word != null) returnList.add(word);
		}

		return returnList;
		
	}

	private void suggestRec(Node curr, String word)
	{
		if (curr == null) return;

		if (curr.value > 0) 
		{
			pq.add(curr.value);
			hashmap.put(word, curr.value);
		}	

		for (int i = 0; i < 256; i++)
		{
			suggestRec(curr.next[i], word + (char)i);
		}
	}

	public ArrayList<String> traverse()
	{
		ArrayList<String> list = new ArrayList<String>();
		list = traverseRec(root, list, "");
		Collections.sort(list);
		return list;
	}

	private ArrayList<String> traverseRec(Node curr, ArrayList<String> list, String result)
	{
		if (curr == null) return list;
		if (curr.value > 0)
		{
			for (int i = 0; i < curr.value; i++)
			{
				list.add(result);
			}
		}

		for (int i = 0; i < 256; i++)
		{
			traverseRec(curr.next[i], list, result + (char)i);
		}

		return list;
	}

	public int count()
	{
		return n;
	}

	class Node 
	{
		public int value;
		public Node[] next = new Node[256];
		
		public Node() {

		}

	}
}
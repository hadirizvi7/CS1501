package cs1501_p2;

import java.util.ArrayList;

public class DLB implements Dict
{
	private DLBNode root;
	private int n;
	private String currPrefix;

	public DLB()
	{
		root = null;
		n = 0;
		currPrefix = "";
	}

	public void add(String key)
	{
		if (contains(key)) {
			return;
		}
		key += "^";
		DLBNode curr = root;
		if (root == null)
		{
			root = new DLBNode(key.charAt(0));
			curr = root;
			for (int i = 1; i < key.length(); i++)
			{
				DLBNode newDown = setDownNode(key.charAt(i), curr);
				curr = newDown;
			}
			n += 1;
			return;
		}

		for (int i = 0; i < key.length(); i++)
		{
			DLBNode newDown = setDownNode(key.charAt(i), curr);
			curr = newDown;
		}
		n += 1;
	}

	private DLBNode setRightNode(char letter, DLBNode curr)
	{
		if (curr.getLet() == letter)
		{
			return curr.getDown();
		}
		if (curr.getRight() == null)
		{
			DLBNode temp = new DLBNode(letter);
			curr.setRight(temp);
			return curr.getRight();
		}

		else
		{
			while (curr.getRight() != null)
			{
				if (curr.getLet() == letter)
				{
					break;
				}
				curr = curr.getRight();
			}

			if (curr.getLet() == letter)
			{
				return curr.getDown();
			}
			else
			{
				DLBNode temp = new DLBNode(letter);
				curr.setRight(temp);
				return curr.getRight();
			}
		}
	}

	private DLBNode setDownNode(char letter, DLBNode curr)
	{
		if (curr.getLet() == '^')
		{
			return setRightNode(letter, curr);
		}

		else if (curr.getDown() == null)
		{
			DLBNode temp = new DLBNode(letter);
			curr.setDown(temp);
			return curr.getDown();
		}

		else
		{
			return setRightNode(letter, curr);
		}
	}

	public boolean contains(String key)
	{
		DLBNode curr = root;

		for (int i = 0; i < key.length(); i++)
		{
			curr = getRightNode(key.charAt(i), curr);

			if (curr == null)
			{
				return false;
			}
			curr = curr.getDown();
		}

		DLBNode endNode = getRightNode('^', curr);

		if (endNode != null) 
		{
			return true;
		}

		return false;
	}

	private DLBNode getRightNode(char letter, DLBNode curr)
	{
		while (curr != null)
		{
			if (curr.getLet() == letter)
			{
				break;
			}
			curr = curr.getRight();
		}
		return curr;
	}

	public boolean containsPrefix(String pre)
	{
		DLBNode curr = root;

		for (int i = 0; i < pre.length(); i++)
		{
			curr = getRightNode(pre.charAt(i), curr);

			if (curr == null)
			{
				return false;
			}
			curr = curr.getDown();
		}

		DLBNode endNode = getRightNode('^', curr);

		if (endNode == null)
		{
			return true;
		}
		else if (lengthOfList(curr) > 1)
		{
			return true;
		}
		return false;
	}


	public int searchByChar(char next)
	{
		currPrefix += next;
		DLBNode curr = root;
		boolean isPrefix = false;
		boolean isWord = false;

		for (int i = 0; i < currPrefix.length(); i++)
		{
			curr = getRightNode(currPrefix.charAt(i), curr);

			if (curr == null) return -1;
			curr = curr.getDown();

			if (i == currPrefix.length() - 1) {
				if (curr != null)
				{
					if (getRightNode('^',curr) != null) isWord = true;
					else isPrefix = true;
					if (curr.getRight() != null)
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
	}

	public ArrayList<String> suggest()
	{
		ArrayList<String> list = new ArrayList<String>();
		DLBNode curr = root;

		for (int i = 0; i < currPrefix.length(); i++)
		{
			curr = getRightNode(currPrefix.charAt(i),  curr);
			if (curr == null) return list;

			curr = curr.getDown();

			if (i == currPrefix.length() - 1)
			{
				list = suggestRec(curr, list, currPrefix);
			}
		}
		return list;
	}

	private ArrayList<String> suggestRec(DLBNode curr, ArrayList<String> list, String word)
	{
		if (curr == null) return list;
		if (list.size() == 5) return list;
		if (getRightNode('^', curr) != null)
		{
			list.add(word);
		}

		suggestRec(curr.getDown(), list, word+curr.getLet());
		suggestRec(curr.getRight(), list, word);
		return list;
	}

	public ArrayList<String> traverse()
	{
		ArrayList<String> list = new ArrayList<String>();
		return traverseRec(root, list, "");
	}

	private ArrayList<String> traverseRec(DLBNode curr, ArrayList<String> list, String result)
	{
		if (curr == null) return list;
		if (getRightNode('^', curr) != null)
		{
			list.add(result);
		}

		traverseRec(curr.getDown(), list, result+curr.getLet());
		traverseRec(curr.getRight(), list, result);
		return list;
	}

	private int lengthOfList(DLBNode curr)
	{
		int count = 0;

		if (curr == null) return 0;

		else if (curr.getRight() == null) return 1;

		while (curr != null)
		{
			count += 1;
			curr = curr.getRight();
		}

		return count;
	}

	public int count()
	{
		return n;
	}
}
package cs1501_p3;

public class rWayTrie
{
	private Node root;
	private int n;

	public rWayTrie() {
		root = new Node();
		n = 0;
	}

	public void add(String key, int index)
	{
		root = addRec(root, key, 0, index);
		n += 1;
	}
	public void update(String key, int index)
	{
		root = addRec(root, key, 0, index);
	}

	public void remove(String key)
	{
		removeRec(root, key, 0);
	}

	private Node removeRec(Node curr, String key, int i)
	{
		if (curr == null) return null;

		if (i == key.length())
		{
			curr.value = 0;
			return curr;
		}

		curr.next[(int)key.charAt(i)] = removeRec(curr.next[(int)key.charAt(i)], key, i+1);
		return curr;
	}

	private Node addRec(Node curr, String key, int i, int index)
	{
		if (curr == null)
		{
			curr = new Node();
		}
		if (i == key.length()) 
		{
			curr.value = index;
			return curr;
		}

		curr.next[(int)key.charAt(i)] = addRec(curr.next[(int)key.charAt(i)], key, i+1, index);
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

	public int get(String key)
	{
		Node result = getRec(root, key, 0);
		if (result == null || result.value == 0) return 0;
		return result.value;
	}

	private Node getRec(Node curr, String key, int i)
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

		return getRec(curr.next[(int)key.charAt(i)], key, i+1);
	}

	public int count()
	{
		return n;
	}

	class Node 
	{
		public int value;
		public Node[] next = new Node[256];
		
		public Node() 
		{

		}

	}
}
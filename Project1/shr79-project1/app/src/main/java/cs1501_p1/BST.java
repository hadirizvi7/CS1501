/*
 * Binary Search Tree class for CS1501 Project 1
 */
package cs1501_p1;

public class BST<T extends Comparable<T>> implements BST_Inter<T>
{

	private BTNode<T> root;

	public BST()
	{
		root = null;
	}

	public BST(BTNode<T> r)
	{
		root = r;
	}
/**
	 * Add a new key to the BST
	 *
	 * @param 	key Generic type value to be added to the BST
	 */
	public void put(T key)
	{
		root = putRec(root, key);
	}
/** 
	 * Add a new key to the BST recursively
	 *
	 *@param 	curr BTNode that keeps track of the current position in the BST
	 *
	 * @param 	key Generic type value to be added to the BST
	 *
	 * @return	curr node that should be inserted into the tree
	 */
	private BTNode<T> putRec(BTNode<T> curr, T key)
	{
		if (curr == null) return new BTNode(key);

		if (key.compareTo(curr.getKey()) < 0)
		{
			curr.setLeft(putRec(curr.getLeft(), key));
		}

		else if (key.compareTo(curr.getKey()) > 0)
		{
			curr.setRight(putRec(curr.getRight(), key));
		}

		return curr;
	}

	/**
	 * Check if the BST contains a key
	 *
	 * @param	key Generic type value to look for in the BST
	 *
	 * @return	true if key is in the tree, false otherwise
	 */
	public boolean contains(T key)
	{
		return containsRec(root, key);
	}

	/**
	 * Check if the BST contains a key recursively
	 *
	 * @param	curr Current position of our node in the BST
	 *
	 * @param	key Generic type value to look for in the BST
	 *
	 * @return	true if key is in the tree, false otherwise
	 */
	private boolean containsRec(BTNode<T> curr, T key)
	{
		if (curr == null) return false;

		if (key.compareTo(curr.getKey()) < 0)
		{
			return containsRec(curr.getLeft(), key);
		}

		else if (key.compareTo(curr.getKey()) > 0)
		{
			return containsRec(curr.getRight(), key);
		}
		else
		{
			return true;
		}
	}

	/**
	 * Remove a key from the BST, if key is present
	 * 
	 * @param	key Generic type value to remove from the BST
	 */
	public void delete(T key)
	{
		root = deleteRec(root, key);
	}

	/**
	 * Remove a key from the BST recursively, if key is present
	 * 
	 *@param	curr Tracks our current position in the BST
	 *
	 * @param	key Generic type value to remove from the BST
	 * @return	Node within the BST to be deleted, if found
	 */	

	private BTNode<T> deleteRec(BTNode<T> curr, T key)
	{
		if (curr == null) return curr;

		if (key.compareTo(curr.getKey()) < 0)
		{
			curr.setLeft(deleteRec(curr.getLeft(), key));
		}  

		else if (key.compareTo(curr.getKey()) > 0)
		{
			curr.setRight(deleteRec(curr.getRight(), key));
		}

		else
		{
			if (curr.getRight() == null)
			{
				return curr.getLeft();
			}
			else if (curr.getLeft() == null)
			{
				return curr.getRight();
			}

			BTNode<T> minNode = root;
			T minValue = root.getRight().getKey();

			while  (minNode.getLeft() != null)
			{
				minValue  = minNode.getLeft().getKey();
				minNode = minNode.getLeft();
			}
			curr.setKey(minValue);
			curr.setRight(deleteRec(curr.getRight(), curr.getKey()));
		}
		return curr;
	}

	/**
	 * Determine the height of the BST
	 *
	 * <p>
	 * A single node tree has a height of 1, an empty tree has a height of 0.
	 *
	 * @return	int value indicating the height of the BST
	 */
	public int height()
	{
		return heightRec(root);
	}

	/**
	 * Finds the height of the BST recursively
	 * 
	 *@param	curr Tracks our current position in the BST
	 *
	 * @return	height of the BST stored in count
	 */	

	private int heightRec(BTNode<T> curr)
	{
		if (curr == null) return 0;
		
		int leftHeight = 1 + heightRec(curr.getLeft());
		int rightHeight = 1 + heightRec(curr.getRight());

		if (leftHeight > rightHeight)
		{
			return leftHeight;
		}
		return rightHeight;
	}

	/**
	 * Determine if the BST is height-balanced
	 *
	 * <p>
	 * A height balanced binary tree is one where the left and right subtrees
	 * of all nodes differ in height by no more than 1.
	 *
	 * @return true if the BST is height-balanced, false if it is not
	 */
	public boolean isBalanced()
	{
		return isBalancedRec(root);
	}

	/**
	 * Finds the height of the BST recursively
	 * 
	 *@param	curr Tracks our current position in the BST
	 *
	 * @return	true if our BST is height-balanced, false if not
	 */	

	private boolean isBalancedRec(BTNode<T> curr)
	{
		if (curr == null) return true;

		int leftHeight = heightRec(curr.getLeft());
		int rightHeight = heightRec(curr.getRight());

		int diff = leftHeight - rightHeight;

		if (diff < 0)
		{
			diff *= -1;
		}

		if (diff <= 1 && isBalancedRec(curr.getLeft()) && isBalancedRec(curr.getRight())) return true;

		return false;
	}

	/**
	 * Produce a ':' separated String of all keys in ascending order
	 *
	 * <p>
	 * Perform an in-order traversal of the tree and produce a String
	 * containing the keys in ascending order, separated by ':'s.
	 * 
	 * @return	TBD if kept
	 */
	public String inOrderTraversal()
	{
		if (root != null)
		{
			String returnVal = inOrderRec(root, "");
			return returnVal.substring(0, returnVal.length() - 1);
		}
		return "";
	}

	/**
	 * Use recursion to perform an in-order traversal
	 *
	 *@param	curr Tracks our current position in the BST
	 *
	 *@param	output Tracks our current in-order traversal string
	 * 
	 * @return	Output string displaying the in-order traversal of the BST
	 */
	private String inOrderRec(BTNode<T> curr, String output)
	{	
		if (curr == null) return output;
		
		if (curr.getLeft() != null)
		{
			output = inOrderRec(curr.getLeft(), output);
		}

		output += curr.getKey() + ":";

		if (curr.getRight() != null)
		{
			output = inOrderRec(curr.getRight(), output);
		}

		return output;
	}

	/**
	 * Produce String representation of the BST
	 * 
	 * <p>
	 * Perform a pre-order traversal of the BST in order to produce a String
	 * representation of the BST. The reprsentation should be a comma separated
	 * list where each entry represents a single node. Each entry should take
	 * the form: *type*(*key*). You should track 4 node types:
	 *     `R`: The root of the tree
	 *     `I`: An interior node of the tree (e.g., not the root, not a leaf)
	 *     `L`: A leaf of the tree
	 *     `X`: A stand-in for a null reference
	 * For each node, you should list its left child first, then its right
	 * child. You do not need to list children of leaves. The `X` type is only
	 * for nodes that have one valid child.
	 * 
	 * @return	String representation of the BST
	 */
	public String serialize()
	{
		if (root != null)
		{
			String result = serializeRec(root, "");
			return result.substring(0, result.length()-1);
		}
		return "";
	}

	private String serializeRec(BTNode<T> curr, String serialized)
	{
		if (curr == null) return serialized;

		if (curr.getKey().compareTo(root.getKey()) == 0) // root
		{
			serialized += "R(" + curr.getKey() + "),";

			if (curr.getLeft() != null && curr.getRight() == null)
			{
				serialized = serializeRec(curr.getLeft(), serialized) + "X(NULL),";
			}
			else if (curr.getLeft() == null && curr.getRight() != null)
			{
				serialized += "X(NULL),";
				serialized = serializeRec(curr.getRight(), serialized);
			}
			else if (curr.getLeft() != null && curr.getRight() != null)
			{
				serialized = serializeRec(curr.getLeft(), serialized);
				serialized = serializeRec(curr.getRight(), serialized);
			}

			else if (curr.getLeft() == null && curr.getRight() == null)
			{
				return serialized;
			}
		}

		else if ((curr.getLeft() != null || curr.getRight() != null) || (curr.getLeft() != null && curr.getRight() != null)) // interior
		{

			serialized += "I(" + curr.getKey() + "),";

			if (curr.getLeft() != null && curr.getRight() == null)
			{
				serialized = serializeRec(curr.getLeft(), serialized) + "X(NULL),";
			}

			else if (curr.getLeft() == null && curr.getRight() != null)
			{
				serialized += "X(NULL),";
				serialized = serializeRec(curr.getRight(), serialized);
			}

			else if (curr.getLeft() != null && curr.getRight() != null)
			{
				serialized = serializeRec(curr.getLeft(), serialized);
				serialized = serializeRec(curr.getRight(), serialized);
			}
		}

		else if (curr.getLeft() == null && curr.getRight() == null) // leaf
		{
			serialized += "L(" + curr.getKey() + "),";
		}

		return serialized;
	}

	/**
	 * Produce a deep copy of the BST that is reversed (i.e., left children
	 * hold keys greater than the current key, right children hold keys less
	 * than the current key).
	 *
	 * @return	Deep copy of the BST reversed
	 */
	public BST_Inter<T> reverse()
	{ 	
		if (root != null)
		{
			BTNode<T> tree = reverseRec(root);
			BST<T> finalTree = new BST<T>(tree);
			return (BST_Inter<T>)finalTree;
		}
		return (BST_Inter<T>)new BST<T>();
	}

	private BTNode<T> reverseRec(BTNode<T> oldCurr)
	{
		if (oldCurr == null)
		{
			return null;
		}
		BTNode<T> node = new BTNode<T>(oldCurr.getKey());
		node.setLeft(reverseRec(oldCurr.getRight()));
		node.setRight(reverseRec(oldCurr.getLeft()));
		return node;
	}
}
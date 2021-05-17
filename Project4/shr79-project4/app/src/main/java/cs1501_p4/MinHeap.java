package cs1501_p4;
import java.lang.*;

public class MinHeap
{
	private int size;
	private Node[] heap;

	public MinHeap()
	{
		size = 0;
		heap = new Node[255];
	}

	public int count()
	{
		return size;
	}

	public Node getMin()
	{
		if (size != 0) return heap[1];
		return null;
	}

	private void resize()
	{
		
		Node[] newHeap = new Node[size*2];
		for (int i = 0; i <= heap.length; i++)
		{
			newHeap[i] = heap[i];
		}
		heap = newHeap;

	}	

	public void add(Node vertex)
	{
		size += 1;
		if (size >= heap.length)
		{
			resize();
		}
		heap[size] = vertex;
		swim(size);
	}

	public void swim(int k)
	{
		int childIndex = k;
		int parentIndex = childIndex/2;

		while (parentIndex > 0)
		{
			Node childKey = heap[childIndex];
			Node parentKey = heap[parentIndex];

			if (Double.compare(parentKey.getLatency(), childKey.getLatency()) < 0) return;

			//System.out.println("Child Node " + childKey.getLatency() + " > Parent Node " + parentKey.getLatency());

			swap(parentIndex, childIndex);
			childIndex = parentIndex;
			parentIndex = childIndex/2;
		}
	}

	private void sink(int k)
	{
		if (k == 0) return;

		int parentIndex = k;
		int childIndex = k*2;
		int nextChildIndex = childIndex + 1;

		if (childIndex <= size)
		{
			Node parentKey = heap[parentIndex];
			Node childKey = heap[childIndex];

			if (nextChildIndex <= size)
			{
				Node nextChildKey = heap[nextChildIndex];

				if (Double.compare(nextChildKey.getLatency(), childKey.getLatency()) < 0)
				{
					childIndex = nextChildIndex;
					childKey = nextChildKey;
				}
			}

			if (Double.compare(childKey.getLatency(), parentKey.getLatency()) < 0)
			{
				swap(parentIndex, childIndex);
				sink(childIndex);
			}
		}
	}

	public void swap(int i, int j)
	{
		Node temp = heap[i];
		heap[i] = heap[j];
		heap[j] = temp;
	}

	public void remove()
	{
		swap(1, size);
		heap[size] = null;
		size -= 1;

		swim(size);
		sink(size);
	}

	public String toString()
	{
		String result = "";
		for (int i = 1; i <= size; i++)
		{
			result += heap[i].getValue() + " ";
		}

		return result;
	}

}
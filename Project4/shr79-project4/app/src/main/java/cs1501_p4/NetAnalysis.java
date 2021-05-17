package cs1501_p4;
import java.util.*;
import java.io.*;

/*
	Undirected graph implemented through the use of an adjacency list
	Use of textbook authors code (https://algs4.cs.princeton.edu/41graph/Biconnected.java.html)
*/

public class NetAnalysis implements NetAnalysis_Inter
{
	private Node[] adjList;
	double[] distance;
	MinHeap pq = new MinHeap();
	boolean[] visited;
	int[] via;
	boolean[] visitedDFS;
	int[] low;
	int[] pre;
	boolean[] articulation;
	int cnt;

	public NetAnalysis(String filename)
	{
		try
		{
			File file = new File(filename);
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine())
			{
				String data = reader.nextLine();
				String[] dataArray = data.split(" ");
				if (dataArray.length == 1)
				{
					adjList = new Node[Integer.parseInt(dataArray[0])];
					for (int i = 0; i < adjList.length; i++)
					{
						adjList[i] = new Node(i);
					}
				}
				else
				{
					Node temp = adjList[Integer.parseInt(dataArray[0])];
					while (temp.getNext() != null)
					{
						temp = temp.getNext();
					}
					temp.setNext(new Node(Integer.parseInt(dataArray[1]), dataArray[2], Integer.parseInt(dataArray[3]), Integer.parseInt(dataArray[4]), Integer.parseInt(dataArray[0])));
					
					temp = adjList[Integer.parseInt(dataArray[1])];
					while (temp.getNext() != null)
					{
						temp = temp.getNext();
					}
					temp.setNext(new Node(Integer.parseInt(dataArray[0]), dataArray[2], Integer.parseInt(dataArray[3]), Integer.parseInt(dataArray[4]), Integer.parseInt(dataArray[0])));
				}

			}
			reader.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<Integer> lowestLatencyPath(int u, int w)
	{
		distance = new double[adjList.length];
		via = new int[adjList.length];
		ArrayList<Integer> path = new ArrayList<Integer>();
		visited = new boolean[adjList.length];

		for (int i = 0; i < distance.length; i++)
		{
			distance[i] = Integer.MAX_VALUE;
			visited[i] = false;
			via[i] = 0;
		}

		distance[u] = 0;
		pq.add(adjList[u]);

		while (!connected())
		{	
			Node temp = pq.getMin();
			pq.remove();
			visited[temp.getValue()] = true;
			findDistance(temp.getValue());
		}
		path.add(w);
		int source = via[w];
		while (!path.contains(u))
		{
			if (path.contains(source)) return null;
			path.add(source);
			source = via[source];
		}

		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i = path.size() - 1; i >= 0; i--)
		{
			result.add(path.get(i));
		}

		return result;

	}

	private void findDistance(int u)
	{
		double newDistance = -1;
		
		Node temp = adjList[u].getNext();

		while (temp != null)
		{
			if (!visited[temp.getValue()])
			{
				newDistance = distance[u] + temp.getLatency();

				if (newDistance < distance[temp.getValue()])
				{
					distance[temp.getValue()] = newDistance;
					via[temp.getValue()] = u;
					temp.setLatency(distance[temp.getValue()]);
				}
				pq.add(temp);
			}
			temp = temp.getNext();
		}
	}

	public int bandwidthAlongPath(ArrayList<Integer> p) throws IllegalArgumentException
	{
		int bandwidth = 0;

		for (int i = 0; i < p.size()-1; i++)
		{
			Node temp = adjList[p.get(i)];
			if (temp == null || temp.getNext() == null) throw new IllegalArgumentException();
			temp = temp.getNext();

			while (temp != null)
			{
				if (temp.getValue() == p.get(i+1))
				{
					bandwidth += temp.getBandwidth();
					break;
				}
				temp = temp.getNext();
			}

			if (temp == null) throw new IllegalArgumentException();
		}

		return bandwidth;
	}

	public boolean copperOnlyConnected()
	{
		boolean copperConnected = true;
		visitedDFS = new boolean[adjList.length];
		int i;
		for (i = 0; i < visitedDFS.length; i++)
		{
			visitedDFS[i] = false;
		}
		DFS(adjList[0]);

		for (i = 0; i < visitedDFS.length; i++)
		{
			if (!visitedDFS[i]) copperConnected = false;
		}

		return copperConnected;
	}

	private void DFS(Node temp)
	{
		visitedDFS[temp.getValue()] = true;
		temp = temp.getNext();

		while (temp != null)
		{
			if (!visitedDFS[temp.getValue()] && temp.getCable().equals("copper"))
			{
				DFS(adjList[temp.getValue()]);
			}
			temp = temp.getNext();
		}
	}

    private void dfs(int u, int v) {
        int children = 0;
        pre[v] = cnt++;
        low[v] = pre[v];
        Node temp = adjList[v];
        temp = temp.getNext();
        while (temp != null) 
        {
        	int w = temp.getValue();
            if (pre[w] == -1) 
            {
                children++;
                dfs(v, w);
                low[v] = Math.min(low[v], low[w]);
                if (low[w] >= pre[v] && u != v) 
                    articulation[v] = true;
            }

            else if (w != u)
            {
                low[v] = Math.min(low[v], pre[w]);
            }

            temp = temp.getNext();
        }

        if (u == v && children > 1)
            articulation[v] = true;
    }

	public boolean connectedTwoVertFail()
	{
		low = new int[adjList.length];
        pre = new int[adjList.length];
		articulation = new boolean[adjList.length];

        for (int i = 0; i < adjList.length; i++)
        {
            low[i] = -1;
            pre[i] = -1;
        }

        for (int v = 0; v < adjList.length; v++)
        {
        	if (pre[v] == -1)
        	{
        		dfs(v, v);
        	}
        }

        for (int j = 0; j < articulation.length; j++)
        {
        	if (articulation[j]) return false;
        }

        return true;
	}

	public ArrayList<STE> lowestAvgLatST()
	{
		ArrayList<STE> sTree = new ArrayList<STE>();
		pq = new MinHeap();
		visited = new boolean[adjList.length];
		for (int source = 0; source < adjList.length; source++)
		{
			if (sTree.size() == adjList.length - 1) return sTree;
			Node temp = adjList[source];
			visited[temp.getValue()] = true;
			temp = temp.getNext();

			while (temp != null)
			{
				pq.add(temp);
				temp = temp.getNext();
			}

			if (pq.count() == 0) return null;
			temp = pq.getMin();
			pq.remove();
			while (visited[temp.getValue()])
			{
				if (pq.count() == 0) return null;
				temp = pq.getMin();
				pq.remove();
			}
			sTree.add(new STE(temp.getParent(), temp.getValue()));

		}

		return sTree;

	}

	private boolean connected()
	{
		for (int i = 0; i < visited.length; i++)
		{
			if (!visited[i]) return false;
		}

		return true;
	}
}

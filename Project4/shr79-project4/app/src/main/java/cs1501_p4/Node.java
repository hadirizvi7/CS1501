package cs1501_p4;

public class Node
{
	private int value;
	private String cable;
	private int bandwidth;
	private int length;
	private Node next;
	private double latency;
	private int parent;

	public Node() {

	}

	public Node(int newValue)
	{
		this.value = newValue;
	}

	public Node(int value, String cable, int bandwidth, int length, int parent)
	{
		this.value = value;
		this.cable = cable;
		this.bandwidth = bandwidth;
		this.length = length;

		if (cable.equals("optical"))
		{
			this.latency = length/200000000.0;
		} 
		else
		{
			this.latency = length/230000000.0;
		}

		//this.latency = (double)length;
		this.parent = parent;
	}

	public int getValue()
	{
		return this.value;
	}

	public void setValue(int newValue)
	{
		this.value = newValue;
	}

	public String getCable()
	{
		return this.cable;
	}
	public void setCable(String newCable)
	{
		this.cable = newCable;
	}
	public int getBandwidth()
	{
		return this.bandwidth;
	}

	public void setBandwidth(int newBandwidth)
	{
		this.bandwidth = newBandwidth;
	}

	public int getLength()
	{
		return this.length;
	}

	public void setLength(int newLength)
	{
		this.length = newLength;
	}

	public Node getNext()
	{
		return this.next;
	}

	public void setNext(Node newNode)
	{
		this.next = newNode;
	}

	public double getLatency()
	{
		return this.latency;
	}

	public void setLatency(double newLatency)
	{
		this.latency = newLatency;
	}

	public int getParent()
	{
		return this.parent;
	}

	public void setParent(int newParent)
	{
		this.parent = newParent;
	}

}
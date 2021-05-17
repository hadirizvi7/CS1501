package cs1501_p3;

public class pqTrie
{
	private Node root;
	private int n;

	public pqTrie() {
		root = new Node();
		n = 0;
	}

	public void addPrice(String key, Car car)
	{
		root = addRec(root, key, 0, car, 0);
		n += 1;
	}

	public void addMile(String key, Car car)
	{
		root = addRec(root, key, 0, car, 1);
	}

	private Node addRec(Node curr, String key, int i, Car car, int flag)
	{
		if (curr == null)
		{
			curr = new Node();
		}
		if (i == key.length()) 
		{
			if (flag == 0)
			{
				CarsPQ temp = curr.pricePQ;
				temp.add(car);
				return curr;
			}
			else
			{
				CarsPQ temp = curr.milePQ;
				temp.add(car);
				return curr;
			}
		}

		curr.next[(int)key.charAt(i)] = addRec(curr.next[(int)key.charAt(i)], key, i+1, car, flag);
		return curr;
	}

	public void updatePrice(String key, Car car)
	{
		updateRec(root, key, 0, car, 0);
	}

	public void updateMile(String key, Car car)
	{
		updateRec(root, key, 0, car, 1);
	}

	public void updateRec(Node curr, String key, int i, Car car, int flag)
	{
		if (curr == null)
		{
			return;
		}

		if (i ==  key.length())
		{
			if (flag == 0)
			{
				String vinToFind =  car.getVIN();
				CarsPQ temp = curr.pricePQ;
				Car newCar = temp.get(vinToFind);
				newCar.setPrice(car.getPrice());
				return;
			}
			else
			{
				String vinToFind =  car.getVIN();
				CarsPQ temp = curr.milePQ;
				Car newCar = temp.get(vinToFind);
				newCar.setMileage(car.getMileage());
				return;
			}
		}

		updateRec(curr.next[(int)key.charAt(i)], key, i+1, car, flag);
	}

	public void removePrice(String key, String vin)
	{
		removeRec(root, key, 0, vin, 0);
	}

	public void removeMile(String key, String vin)
	{
		removeRec(root, key, 0, vin, 1);
	}

	private Node removeRec(Node curr, String key, int i, String vin, int flag)
	{
		if (curr == null) return null;

		if (i == key.length())
		{
			if (flag == 0)
			{
				CarsPQ temp = curr.pricePQ;
				temp.remove(vin);
				return curr;
			}
			else
			{
				CarsPQ temp = curr.milePQ;
				temp.remove(vin);
				return curr;
			}
		}

		curr.next[(int)key.charAt(i)] = removeRec(curr.next[(int)key.charAt(i)], key, i+1, vin, flag);
		return curr;
	}

	public boolean contains(String key)
	{
		Node result = containsRec(root, key, 0);
		if (result == null) return false;
		return true;
	}

	private Node containsRec(Node curr, String key, int i)
	{
		if (curr == null) return null;

		if (i == key.length())
		{
			if (curr != null)
			{
				if (curr.pricePQ.count() != 0 || curr.milePQ.count() != 0)
				{
					return curr;
				}
			}
			else return null;
		}
		return containsRec(curr.next[(int)key.charAt(i)], key, i+1);
	}

	public CarsPQ getPrice(String key)
	{
		Node result = getRec(root, key, 0);
		if (result == null) return null;
		return result.pricePQ;
	}

	public CarsPQ getMile(String key)
	{
		Node result = getRec(root, key, 0);
		if (result == null) return null;
		return result.milePQ;
	}

	private Node getRec(Node curr, String key, int i)
	{
		if (curr == null) return null;

		if (i == key.length())
		{
			return curr;
		}

		return getRec(curr.next[(int)key.charAt(i)], key, i+1);
	}

	public int count()
	{
		return n;
	}

	class Node 
	{
		public CarsPQ pricePQ;
		public CarsPQ milePQ;
		public Node[] next;
		
		public Node() 
		{
			pricePQ = new CarsPQ();
			milePQ = new CarsPQ();
			next = new Node[256];
		}

	}
}
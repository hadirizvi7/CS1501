package cs1501_p3;

import java.util.*;
import java.io.*;


public class CarsPQ implements CarsPQ_Inter
{
	private int size;
	private Car[] priceHeap;
	private Car[] mileHeap;
	private rWayTrie priceTable;
	private rWayTrie mileTable;
	private pqTrie makeTrie;
	private pqTrie modelTrie;

	public CarsPQ()
	{
		size = 0;
		priceHeap = new Car[255];
		mileHeap = new Car[255];
		priceTable = new rWayTrie();
		mileTable = new rWayTrie();

	}

	public CarsPQ(String filename) 
	{
		size = 0;
		priceHeap = new Car[255];
		mileHeap = new Car[255];
		priceTable = new rWayTrie();
		mileTable = new rWayTrie();
		makeTrie = new pqTrie();
		modelTrie = new pqTrie();

		try
		{
			File file = new File(filename);
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine())
			{
				String car = reader.nextLine();
				String[] data = car.split(":");
				if (data[0].charAt(0) == '#') continue;
				Car newCar = new Car(data[0], data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), data[5]);
				add(newCar);
			}
			reader.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public int count()
	{
		return size;
	}

	private void resize(int flag)
	{
		if (flag == 0)
		{
			Car[] newPriceHeap = new Car[size*2];
			for (int i = 0; i <= priceHeap.length; i++)
			{
				newPriceHeap[i] = priceHeap[i];
			}
			priceHeap = newPriceHeap;
			return;
		}

		else // flag == 1
		{
			Car[] newMileHeap = new Car[size*2];
			for (int i = 0; i < mileHeap.length; i++)
			{
				newMileHeap[i] = mileHeap[i];
			}
			mileHeap = newMileHeap;
			return;
		}
	}	


	public void add(Car c) throws IllegalStateException
	{
		if (c == null) return;
		if (contains(c)) throw new IllegalStateException();
		size += 1;
		if (size >= priceHeap.length)
		{
			resize(0);
		}
		if (size >= mileHeap.length)
		{
			resize(1);
		}
		priceHeap[size] = c;
		mileHeap[size] = c;
		priceTable.add(c.getVIN(), size);
		mileTable.add(c.getVIN(), size);
		priceSwim(size);
		mileSwim(size);

		if (makeTrie != null && modelTrie != null)
		{
			makeTrie.addPrice(c.getMake(), priceHeap[priceTable.get(c.getVIN())]);
			makeTrie.addMile(c.getMake(), mileHeap[mileTable.get(c.getVIN())]);
			modelTrie.addPrice(c.getModel(), priceHeap[priceTable.get(c.getVIN())]);
			modelTrie.addMile(c.getModel(), mileHeap[mileTable.get(c.getVIN())]);
		}

	}

	private void priceSwim(int k)
	{
		int childIndex = k;
		int parentIndex = childIndex/2;

		while (parentIndex > 0)
		{
			Car childKey = priceHeap[childIndex];
			Car parentKey = priceHeap[parentIndex];
			if (parentKey.getPrice() < childKey.getPrice()) return;

			swap(parentIndex, childIndex, 0);
			childIndex = parentIndex;
			parentIndex = childIndex/2;
		}
	}

	private void mileSwim(int k)
	{
		int childIndex = k;
		int parentIndex = childIndex/2;

		while (parentIndex > 0)
		{
			Car childKey = mileHeap[childIndex];
			Car parentKey = mileHeap[parentIndex];
			if (parentKey.getMileage() < childKey.getMileage()) return;

			swap(parentIndex, childIndex, 1);
			childIndex = parentIndex;
			parentIndex = childIndex/2;
		}
	}

	private void priceSink(int k)
	{
		int parentIndex = k;
		int childIndex = k*2;
		int nextChildIndex = childIndex + 1;

		if (childIndex <= size)
		{
			Car parentKey = priceHeap[parentIndex];
			Car childKey = priceHeap[childIndex];

			if (nextChildIndex <= size)
			{
				Car nextChildKey = priceHeap[nextChildIndex];
				if (nextChildKey.getPrice() < childKey.getPrice())
				{
					childIndex = nextChildIndex;
					childKey = nextChildKey;
				}
			}

			if (childKey.getPrice() < parentKey.getPrice())
			{
				swap(parentIndex, childIndex, 0);
				priceSink(childIndex);
			}
		}
	}

	private void mileSink(int k)
	{
		int parentIndex = k;
		int childIndex = k*2;
		int nextChildIndex = childIndex + 1;

		if (childIndex <= size)
		{
			Car parentKey = mileHeap[parentIndex];
			Car childKey = mileHeap[childIndex];

			if (nextChildIndex <= size)
			{
				Car nextChildKey = mileHeap[nextChildIndex];
				if (nextChildKey.getMileage() < childKey.getMileage())
				{
					childIndex = nextChildIndex;
					childKey = nextChildKey;
				}
			}

			if (childKey.getMileage() < parentKey.getMileage())
			{
				swap(parentIndex, childIndex, 1);
				mileSink(childIndex);
			}
		}
	}

	private void swap(int i, int j, int flag)
	{
		if (flag == 0)
		{
			Car temp = priceHeap[i];
			priceHeap[i] = priceHeap[j];
			priceHeap[j] = temp;
			priceTable.update(priceHeap[i].getVIN(), i);
			priceTable.update(priceHeap[j].getVIN(), j);

		}

		else //flag == 1
		{
			Car temp = mileHeap[i];
			mileHeap[i] = mileHeap[j];
			mileHeap[j] = temp;
			mileTable.update(mileHeap[i].getVIN(), i);
			mileTable.update(mileHeap[j].getVIN(), j);
		}
	}

	private boolean contains(Car tempCar)
	{
		if (priceTable.contains(tempCar.getVIN()) && mileTable.contains(tempCar.getVIN())) return true;
		return false;

	}

	public Car get(String vin) throws NoSuchElementException
	{
		int result = priceTable.get(vin);
		if (result == 0) throw new NoSuchElementException();
		else return priceHeap[result];
	}

	public void updatePrice(String vin, int newPrice) throws NoSuchElementException
	{
		if (!priceTable.contains(vin)) throw new NoSuchElementException();
		int index = priceTable.get(vin);
		Car key = priceHeap[index];

		key.setPrice(newPrice);
		priceSwim(index);
		priceSink(index);

		if (makeTrie != null && modelTrie != null)
		{
			makeTrie.updatePrice(key.getMake(), key);
			modelTrie.updatePrice(key.getModel(), key);
		}
	}

	public void updateMileage(String vin, int newMileage) throws NoSuchElementException
	{
		if (!mileTable.contains(vin)) throw new NoSuchElementException();
		int index = mileTable.get(vin);
		Car key = mileHeap[index];

		key.setMileage(newMileage);
		mileSwim(index);
		mileSink(index);

		if (makeTrie != null && modelTrie != null)
		{
			makeTrie.updateMile(key.getMake(), key);
			modelTrie.updateMile(key.getModel(), key);
		}
	}

	public void updateColor(String vin, String newColor) throws NoSuchElementException
	{
		if (!mileTable.contains(vin) || !priceTable.contains(vin)) throw new NoSuchElementException();
		int mileIndex = mileTable.get(vin);
		int priceIndex = priceTable.get(vin);

		Car mileCar = mileHeap[mileIndex];
		Car priceCar = priceHeap[priceIndex];

		mileCar.setColor(newColor);
		priceCar.setColor(newColor);
	}

	public void remove(String vin) throws NoSuchElementException
	{
		if (!priceTable.contains(vin) || !mileTable.contains(vin)) throw new NoSuchElementException();
		int priceIndex = priceTable.get(vin);
		int mileIndex = mileTable.get(vin);
		if (makeTrie != null && modelTrie != null)
		{
			makeTrie.removePrice(priceHeap[priceIndex].getMake(), priceHeap[priceIndex].getVIN());
			makeTrie.removeMile(mileHeap[mileIndex].getMake(), mileHeap[mileIndex].getVIN());
			modelTrie.removePrice(priceHeap[priceIndex].getModel(), priceHeap[priceIndex].getVIN());
			modelTrie.removeMile(mileHeap[mileIndex].getModel(), mileHeap[mileIndex].getVIN());
		}
		swap(priceIndex, size, 0);
		swap(mileIndex, size, 1);
		priceHeap[size] = null;
		mileHeap[size] = null;
		size -= 1;

		priceSwim(size);
		priceSink(size);

		mileSwim(size);
		mileSink(size);

		priceTable.remove(vin);
		mileTable.remove(vin);
	}

	public Car getLowPrice()
	{
		if (size > 0) return priceHeap[1];
		return null;
	}

	public Car getLowPrice(String make, String model)
	{
		CarsPQ tempMakes = makeTrie.getPrice(make);
		CarsPQ tempModels = modelTrie.getPrice(model);

		if (tempMakes == null || tempModels == null) return null;

		while (tempModels.count() > 0)
		{
			if (tempMakes.contains(tempModels.getLowPrice())) return tempModels.getLowPrice();
			tempModels.remove(tempModels.getLowPrice().getVIN());
		}
		return null;
		
	}

	public Car getLowMileage()
	{
		if (size > 0) return mileHeap[1];
		return null;
	}

	public Car getLowMileage(String make, String model)
	{
		CarsPQ tempMakes = makeTrie.getMile(make);
		CarsPQ tempModels = modelTrie.getMile(model);

		if (tempMakes == null || tempModels == null) return null;

		while (tempModels.count() > 0)
		{
			if (tempMakes.contains(tempModels.getLowMileage())) return tempModels.getLowMileage();
			tempModels.remove(tempModels.getLowMileage().getVIN());
		}
		return null;
	}
}
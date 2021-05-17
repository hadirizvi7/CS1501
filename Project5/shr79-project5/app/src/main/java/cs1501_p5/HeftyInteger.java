/**
 * HeftyInteger for CS1501 Project 5
 * @author	Dr. Farnan
 */
package cs1501_p5;

import java.util.Random;

public class HeftyInteger {

	private final byte[] ONE = {(byte) 1};

	private byte[] val;

	/**
	 * Construct the HeftyInteger from a given byte array
	 * @param b the byte array that this HeftyInteger should represent
	 */
	public HeftyInteger(byte[] b) {
		val = b;
	}

	/**
	 * Return this HeftyInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other HeftyInteger to sum with this
	 */
	public HeftyInteger add(HeftyInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		HeftyInteger res_li = new HeftyInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public HeftyInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		HeftyInteger neg_li = new HeftyInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.add(new HeftyInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other HeftyInteger to subtract from this
	 * @return difference of this and other
	 */
	public HeftyInteger subtract(HeftyInteger other) {
		return this.add(other.negate());
	}

	/**
	 * Compute the product of this and other
	 * @param other HeftyInteger to multiply by this
	 * @return product of this and other
	 */
	public HeftyInteger multiply(HeftyInteger other) {
		// YOUR CODE HERE (replace the return, too...)
		HeftyInteger a;
		HeftyInteger b;
		boolean flag = false;

		if (this.isNegative() && other.isNegative())
		{
			a = new HeftyInteger(this.negate().getVal());
			b = new HeftyInteger(other.negate().getVal());
		}

		else if (this.isNegative())
		{
			a = new HeftyInteger(this.negate().getVal());
			b = new HeftyInteger(other.getVal());
			flag = true;
		}

		else if (other.isNegative())
		{
			a = new HeftyInteger(this.getVal());
			b = new HeftyInteger(other.negate().getVal());
			flag = true;
		}

		else
		{
			a = new HeftyInteger(this.getVal());
			b = new HeftyInteger(other.getVal());
		}

		int i = a.length() - 1;
		int j;
		HeftyInteger value;
		int temp;
		HeftyInteger product = new HeftyInteger(new byte[1]);
		HeftyInteger tempInt;
		while (i >= 0)
		{
			j = b.length() - 1;
			while (j >= 0)
			{
				int shiftAmount = findShift(a, b, i, j);
				int result = mult(a, b, i, j);
				if ((byte)((result >>> 8) & 255) > 0) temp = 2;
				else if ((byte)((result >>> 8) & 255) < 0) temp = 3;
				else if ((byte)((result >>> 8) & 255) == 0)temp = 2;
				else temp = 3;

				value = new HeftyInteger(new byte[shiftAmount+temp]);
				int index = temp - 1;
				value.getVal()[index] = (byte)(result & 255);
				index -= 1;
				value.getVal()[index] = (byte)((result >>> 8) & 255);
				tempInt = new HeftyInteger(value.getVal());
				product = product.add(tempInt);
				j -= 1;
			}
			i -= 1;
		}

		if (flag) return product.negate();
		return product;
	}

	private int mult(HeftyInteger a, HeftyInteger b, int i, int j)
	{
		int first = (int)a.getVal()[i] & 255;
		int second = (int)b.getVal()[j] & 255;
		return (first*second);

	}

	private int findShift(HeftyInteger a, HeftyInteger b, int i, int j)
	{
		int result = a.length() - 1;
		result -= i;
		result += b.length() - 1;
		result -= j;
		return result; 
	}

	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another HeftyInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	 public HeftyInteger[] XGCD(HeftyInteger other) {
		// YOUR CODE HERE (replace the return, too...)
		HeftyInteger[] result = new HeftyInteger[3];

		int flag = this.compareTo(other);

		if (flag == 0)
		{
			result[0] = this;
			result[1] = new HeftyInteger(new byte[]{(byte) 0});
			result[2] = new HeftyInteger(new byte[]{(byte) 1});
		}

		else if (flag > 0) result = XGCDRec(this, other);

		else result = XGCDRec(other, this);

		HeftyInteger gcd = this.multiply(result[1]).add(other.multiply(result[2]));
		if (gcd.compareTo(result[0]) != 0)
		{
			HeftyInteger temp = result[1];
			result[1] = result[2];
			result[2] = temp;
		}
		return result;
	 }

	 private HeftyInteger[] XGCDRec(HeftyInteger p, HeftyInteger q)
	 {
	 	HeftyInteger[] result = new HeftyInteger[3];
	 	if (q.compareTo(new HeftyInteger(new byte[]{(byte) 0})) == 0)
	 	{
	 		result[0] = p;
	 		result[1] = new HeftyInteger(new byte[]{(byte) 1});
	 		result[2] = new HeftyInteger(new byte[]{(byte) 0});
	 		return result;
	 	}

		result = XGCDRec(q, mod(p, q));
		HeftyInteger product = div(p,q).multiply(result[2]);
		p = result[2];
		q = result[1].subtract(product);

	 	return new HeftyInteger[]{result[0], p, q};
	 }

	 private int[] convert(byte[] arr)
	 {
	 	int newLength = arr.length * 8;
	 	int[] returnArr = new int[newLength];
	 	int bitMask = 128;

	 	for (int i = 0; i < arr.length; i++)
	 	{
	 		for (int j = 0; j < 8; j++)
	 		{
	 			int index = 8*i + j;
	 			if ((((int)arr[i] & 255) & bitMask) > 0)
	 			{
	 				returnArr[index] = 1;
	 			}
	 			bitMask >>= 1;
	 		}
	 	}

	 	return returnArr;
	 }

	 private HeftyInteger div(HeftyInteger x, HeftyInteger y)
	 {
	 	HeftyInteger result = new HeftyInteger(new byte[]{(byte) 0});

	 	while (x.compareTo(y) >= 0)
	 	{
	 		result = result.add(new HeftyInteger(new byte[]{(byte) 1}));
	 		x = x.subtract(y);
	 	}

	 	return result;
	 }

	 private HeftyInteger mod(HeftyInteger x, HeftyInteger y)
	 {
	 	HeftyInteger result = new HeftyInteger(x.getVal());

	 	while (x.compareTo(y) >= 0)
	 	{
	 		x = x.subtract(y);
	 		result = new HeftyInteger(x.getVal());
	 	}
	 	return result;
	 }

	 private boolean findOne(int[] arr)
	 {
	 	for (int i = 0; i < arr.length; i++)
	 	{
	 		if (arr[i] == 1) return true;
	 	}

	 	return false;
	 }

	 private int greaterThan(int[] arr1, int[] arr2, boolean flag)
	 {
	 	if (flag)
	 	{
	 		for (int i = 0; i < arr1.length; i++)
	 		{
	 			if (arr1[i] > arr2[i]) return 1;
	 			else if (arr2[i] > arr1[i]) return -1;
	 			else {}
	 		}
	 	}
	 	else
	 	{
	 		for (int i = 0; i < arr1.length; i++)
	 		{
	 			if (arr1[i] > arr2[i]) return -1;
	 			else if (arr2[i] > arr1[i]) return 1;
	 			else {}
	 		}
	 	}

	 	return 0;
	 }

	 private int compareTo(HeftyInteger other)
	 {
	 	int result = this.length() - other.length();

	 	if (result == 0)
	 	{
	 		for (int i = 0; i < other.getVal().length; i++)
	 		{
	 			int[] temp1 = convert(new byte[]{this.getVal()[i]});
	 			int[] temp2 = convert(new byte[]{other.getVal()[i]});

	 			int value = greaterThan(temp1, temp2, true);
	 			if (value != 0) return value;
	 		}
	 	}

	 	else if (result > 0)
	 	{
	 		HeftyInteger temp = new HeftyInteger(new byte[result]);

	 		for (int i = 0; i < temp.length(); i++)
	 		{
	 			temp.getVal()[i] = this.getVal()[i];
	 		}

	 		if (findOne(convert(temp.getVal()))) return 1;

	 		for (int i = 0; i < other.getVal().length; i++)
	 		{
	 			int[] temp1 = convert(new byte[]{this.getVal()[result]});
	 			int[] temp2 = convert(new byte[]{other.getVal()[i]});

	 			int value = greaterThan(temp1, temp2, true);
	 			if (value != 0) return value;
	 			result += 1;
	 		}
	 	}

	 	else 
	 	{
	 		result *= -1;

	 		HeftyInteger temp = new HeftyInteger(new byte[result]);

	 		for (int i = 0; i < temp.length(); i++)
	 		{
	 			temp.getVal()[i] = other.getVal()[i];
	 		}

	 		if (findOne(convert(temp.getVal()))) return -1;

	 		for (int i = 0; i < this.length(); i++)
	 		{
	 			int[] temp1 = convert(new byte[]{other.getVal()[result]});
	 			int[] temp2 = convert(new byte[]{this.getVal()[i]});

	 			int value = greaterThan(temp1, temp2, false);
	 			if (value != 0) return value;
	 			result += 1;
	 		}
	 	}
	 	return 0;
	 }
}

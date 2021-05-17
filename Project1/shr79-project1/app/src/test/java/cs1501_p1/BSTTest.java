package cs1501_p1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;

public class BSTTest {

	private BST<Integer> setup() {
		int[] puts = {10,7,9,15,13};
		BST<Integer> t = new BST<Integer>();

		for (int i: puts) {
			t.put(i);
		}

		for (int i: puts) {
			assertTrue(t.contains(i), "Cannot check, put/contains not working");
		}

		return t;
	}

	@Test
	public void pc() {
		BST<Integer> t = setup();

		assertTrue(t.contains(13), "Contains isn't working");
	}

	@Test
	public void del() {
		BST<Integer> t = setup();

		t.delete(10);
		assertTrue(!t.contains(10), "Could not remove 3");
	}

	@Test
	public void height() {
		BST<Integer> t = setup();

		assertEquals(3, t.height(), "Height should equal 3");
	}

	@Test
	public void bal() {
		BST<Integer> t = setup();

		assertEquals(true, t.isBalanced(), "Tree should not be balanced");
	}

	@Test
	public void iot() {
		BST<Integer> t = setup();

		assertEquals("7:9:10:13:15", t.inOrderTraversal(), "Should produce \"7:9:10:13:15\"");
	}

	@Test
	public void serial() {
		BST<Integer> t = setup();
		
		assertEquals("R(10),I(7),X(NULL),L(9),I(15),L(13),X(NULL)", t.serialize(), "Should produce \"R(10),I(7),X(NULL),L(9),I(15),L(13),X(NULL)\"");
	}

	@Test
	public void rev() {
		BST<Integer> t = setup();

		BST<Integer> r = (BST<Integer>) t.reverse();
		assertEquals("R(10),I(15),X(NULL),L(13),I(7),L(9),X(NULL)", r.serialize(), "Should produce \"R(10),I(15),X(NULL),L(13),I(7),L(9),X(NULL)\"");
	}

	@Test
	public void submit() {
		assertTrue(true, "Somehow inproper submission flagged through testing?");
	}
}

package patricia.trie;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PatriciaTrieTest {

	private PatriciaTrie patricia;

	@Before
	public void setUp() throws Exception {
		patricia = new PatriciaTrie();
	}

	@Test
	public void testInsert() {
		/**
		 * nodes[0] == parent
		 * nodes[1] == current
		 * nodes[2] == left
		 * nodes[3] == right
		 */
		Node[] nodes = patricia.insert( "A" );
		notNullCheck( nodes );
		eachNodeNullCheck( nodes, 0 );
		eachNodeCheck( nodes, 1, 1, "A" );
		eachNodeNullCheck( nodes, 2 );
		eachNodeCheck( nodes, 3, 1, "A" );

		nodes = patricia.insert( "S" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 1, "A" );
		eachNodeCheck( nodes, 1, 3, "S" );
		eachNodeCheck( nodes, 2, 1, "A" );
		eachNodeCheck( nodes, 3, 3, "S" );

		nodes = patricia.insert( "E" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 3, "S" );
		eachNodeCheck( nodes, 1, 5, "E" );
		eachNodeCheck( nodes, 2, 1, "A" );
		eachNodeCheck( nodes, 3, 5, "E" );

		nodes = patricia.insert( "R" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 3, "S" );
		eachNodeCheck( nodes, 1, 7, "R" );
		eachNodeCheck( nodes, 2, 7, "R" );
		eachNodeCheck( nodes, 3, 3, "S" );

		nodes = patricia.insert( "C" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 5, "E" );
		eachNodeCheck( nodes, 1, 6, "C" );
		eachNodeCheck( nodes, 2, 1, "A" );
		eachNodeCheck( nodes, 3, 6, "C" );

		nodes = patricia.insert( "H" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 3, "S" );
		eachNodeCheck( nodes, 1, 4, "H" );
		eachNodeCheck( nodes, 2, 5, "E" );
		eachNodeCheck( nodes, 3, 4, "H" );

		nodes = patricia.insert( "I" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 4, "H" );
		eachNodeCheck( nodes, 1, 7, "I" );
		eachNodeCheck( nodes, 2, 4, "H" );
		eachNodeCheck( nodes, 3, 7, "I" );

		nodes = patricia.insert( "N" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 4, "H" );
		eachNodeCheck( nodes, 1, 5, "N" );
		eachNodeCheck( nodes, 2, 7, "I" );
		eachNodeCheck( nodes, 3, 5, "N" );

		nodes = patricia.insert( "G" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 5, "E" );
		eachNodeCheck( nodes, 1, 6, "G" );
		eachNodeCheck( nodes, 2, 5, "E" );
		eachNodeCheck( nodes, 3, 6, "G" );

		nodes = patricia.insert( "X" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 3, "S" );
		eachNodeCheck( nodes, 1, 4, "X" );
		eachNodeCheck( nodes, 2, 7, "R" );
		eachNodeCheck( nodes, 3, 4, "X" );

		nodes = patricia.insert( "M" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 5, "N" );
		eachNodeCheck( nodes, 1, 6, "M" );
		eachNodeCheck( nodes, 2, 6, "M" );
		eachNodeCheck( nodes, 3, 5, "N" );

		nodes = patricia.insert( "P" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 4, "X" );
		eachNodeCheck( nodes, 1, 6, "P" );
		eachNodeCheck( nodes, 2, 6, "P" );
		eachNodeCheck( nodes, 3, 7, "R" );

		nodes = patricia.insert( "L" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 6, "M" );
		eachNodeCheck( nodes, 1, 7, "L" );
		eachNodeCheck( nodes, 2, 7, "L" );
		eachNodeCheck( nodes, 3, 6, "M" );

		nodes = patricia.insert( null );
		assertNull( "expecting nodes==null but found not null", nodes );
		nodes = patricia.insert( "" );
		assertNull( "expecting nodes==null but found not null", nodes );
	}

	@Test
	public void testInsert2() {
		Node[] nodes = patricia.insert( "low" );
		notNullCheck( nodes );
		eachNodeNullCheck( nodes, 0 );
		eachNodeCheck( nodes, 1, 1, "low" );
		eachNodeNullCheck( nodes, 2 );
		eachNodeCheck( nodes, 3, 1, "low" );

		nodes = patricia.insert( "COLD" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 1, "low" );
		eachNodeCheck( nodes, 1, 2, "COLD" );
		eachNodeCheck( nodes, 2, 2, "COLD" );
		eachNodeCheck( nodes, 3, 1, "low" );

		nodes = patricia.insert( "Hello" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 2, "COLD" );
		eachNodeCheck( nodes, 1, 4, "Hello" );
		eachNodeCheck( nodes, 2, 2, "COLD" );
		eachNodeCheck( nodes, 3, 4, "Hello" );

		nodes = patricia.insert( "hello" );
		notNullCheck( nodes );
		eachNodeCheck( nodes, 0, 2, "COLD" );
		eachNodeCheck( nodes, 1, 5, "hello" );
		eachNodeCheck( nodes, 2, 5, "hello" );
		eachNodeCheck( nodes, 3, 1, "low" );
	}

	private void notNullCheck(Node[] nodes) {
		assertNotNull( "expecting nodes!=null but found null", nodes );
		assertTrue( "expecting nodes.length==4 but found " + nodes.length, nodes.length == 4 );
	}

	private void eachNodeNullCheck(Node[] nodes, int index) {
		assertNull( "expecting nodes[" + index + "]==null but found " + nodes[index], nodes[index] );
	}

	private void eachNodeCheck(Node[] nodes, int index, int bitIndex, String insertedKey) {
		assertTrue(
				"expecting nodes[" + index + "].getBitIndex()==" + bitIndex + " but found "
						+ nodes[index].getBitIndex(), nodes[index].getBitIndex() == bitIndex );
		assertTrue(
				"expecting nodes[" + index + "].getKey().equals( + " + insertedKey + " ) but found "
						+ nodes[index].getKey(), nodes[index].getKey().equals( insertedKey ) );
	}

	@Test
	public void testSearch() {
		patricia.insert( "A" );
		patricia.insert( "S" );
		patricia.insert( "E" );
		patricia.insert( "R" );
		patricia.insert( "C" );
		patricia.insert( "H" );
		patricia.insert( "I" );
		patricia.insert( "N" );
		patricia.insert( "G" );
		patricia.insert( "X" );
		patricia.insert( "M" );
		patricia.insert( "P" );
		patricia.insert( "L" );

		checkSearched( true, patricia.search( "A" ), "A" );
		checkSearched( true, patricia.search( "S" ), "S" );
		checkSearched( true, patricia.search( "E" ), "E" );
		checkSearched( true, patricia.search( "R" ), "R" );
		checkSearched( true, patricia.search( "C" ), "C" );
		checkSearched( true, patricia.search( "H" ), "H" );
		checkSearched( true, patricia.search( "I" ), "I" );
		checkSearched( true, patricia.search( "G" ), "N" );
		checkSearched( true, patricia.search( "X" ), "X" );
		checkSearched( true, patricia.search( "M" ), "M" );
		checkSearched( true, patricia.search( "P" ), "P" );
		checkSearched( true, patricia.search( "L" ), "L" );
		checkSearched( false, patricia.search( "Y" ), "Y" );
		checkSearched( false, patricia.search( "B" ), "B" );
		checkSearched( false, patricia.search( "D" ), "D" );
		checkSearched( false, patricia.search( null ), null );
		checkSearched( false, patricia.search( "" ), "" );
	}

	@Test
	public void testSearch2() {

		patricia.insert( "low" );
		patricia.insert( "COLD" );
		patricia.insert( "Hello" );
		patricia.insert( "hello" );
		patricia.insert( "Hello World" );
		patricia.insert( "HELLO WORLD WORLD" );
		patricia.insert( "H" );
		patricia.insert( "hey" );
		patricia.insert( "she" );
		patricia.insert( "a" );
		patricia.insert( "apple" );
		patricia.insert( "bee" );

		checkSearched( true, patricia.search( "low" ), "low" );
		checkSearched( true, patricia.search( "COLD" ), "COLD" );
		checkSearched( true, patricia.search( "Hello" ), "Hello" );
		checkSearched( true, patricia.search( "hello" ), "hello" );
		checkSearched( true, patricia.search( "Hello World" ), "Hello World" );
		checkSearched( true, patricia.search( "HELLO WORLD WORLD" ), "HELLO WORLD WORLD" );
		checkSearched( true, patricia.search( "H" ), "H" );
		checkSearched( true, patricia.search( "hey" ), "hey" );
		checkSearched( true, patricia.search( "she" ), "she" );
		checkSearched( true, patricia.search( "a" ), "a" );
		checkSearched( true, patricia.search( "apple" ), "apple" );
		checkSearched( true, patricia.search( "bee" ), "bee" );
		checkSearched( false, patricia.search( "hot" ), "hot" );
		checkSearched( false, patricia.search( "cold" ), "cold" );
		checkSearched( false, patricia.search( "Why not reinvent the wheel" ), "Why not reinvent the wheel" );

		patricia.insert( "Why not reinvent the wheel" );
		checkSearched( true, patricia.search( "Why not reinvent the wheel" ), "Why not reinvent the wheel" );
	}

	private void checkSearched(boolean expected, boolean res, String searchKey) {

		if ( expected ) {
			assertTrue( "expecting insert()==true but found " + res + " for searchKey=" + searchKey, res );
		}
		else {
			assertFalse( "expecting insert()==false but found " + res + " for searchKey=" + searchKey, res );

		}
	}

	@Test
	public void testSearchPrefix() {

		patricia.insert( "S" );
		patricia.insert( "A" );
		patricia.insert( "ABC" );
		patricia.insert( "B" );

		List<String> results = patricia.searchPrefix( "A" );
		checkPrefixSize( 2, results.size() );
	}

	@Test
	public void testSearchPrefix2() {

		patricia.insert( "Hello" );
		patricia.insert( "Huge" );
		patricia.insert( "Here" );
		patricia.insert( "there" );
		patricia.insert( "hello" );
		patricia.insert( "After" );
		patricia.insert( "health" );
		patricia.insert( "Health" );
		patricia.insert( "Health Center" );
		patricia.insert( "Hello World" );
		patricia.insert( "Help" );
		patricia.insert( "Hex" );
		patricia.insert( "Horn" );
		patricia.insert( "hH" );
		patricia.insert( "need some help" );

		List<String> results = patricia.searchPrefix( "z" );
		checkPrefixSize( 0, results.size() );

		results = patricia.searchPrefix( "h" );
		checkPrefixSize( 3, results.size() );
	}

	private void checkPrefixSize(int expected, int res) {
		assertTrue( "expecting results.size()==" + expected + " but found " + res, expected == res );

	}

	@Test
	public void testInOrder() {

		patricia.insert( "S" );
		patricia.insert( "A" );
		patricia.insert( "ABC" );
		patricia.insert( "B" );

		patricia.printInOrder();
	}
}

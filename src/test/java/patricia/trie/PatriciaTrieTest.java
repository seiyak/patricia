package patricia.trie;

import static org.junit.Assert.*;

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
		patricia.insert( "A" );
		patricia.insert( "S" );
	}
}

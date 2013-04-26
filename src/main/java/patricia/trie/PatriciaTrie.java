package patricia.trie;

import org.apache.log4j.Logger;

public class PatriciaTrie {

	private Node head;
	private static Logger log = Logger.getLogger( PatriciaTrie.class );

	public PatriciaTrie(String searchKey) {
		initializeHead( searchKey );
	}

	public PatriciaTrie() {

	}

	public Node insert(String searchKey) {

		if ( initializeHead( searchKey ) ) {
			return new Node( head.getBitIndex(), head.getKey() );
		}

		Node res = new Node();
		doInsert( head, searchKey, res );

		return res;
	}

	private boolean initializeHead(String searchKey) {
		if ( head == null ) {
			head = new Node( getLeftMostOneBit( searchKey ), null, null, head, searchKey );
			log.debug( "str:  " + searchKey + " binary expression: " + toBytes( searchKey ) );
			return true;
		}

		return false;
	}

	private void doInsert(Node node, String searchKey, Node res) {

		if ( node != null ) {
			// search left
			char bit = getBitAt( node.getBitIndex(), searchKey );
			if ( bit == '0' ) {

				if ( node.getLeft() == null ) {
					Node n = createNewNode( node, searchKey );
					n.setParent( node );
					node.setLeft( n );
					res.copy( n );
				}
				else if ( node.getBitIndex() >= node.getLeft().getBitIndex() ) {
					// find upward pointer
					Node newNode = createNewNode( node, searchKey );
					updateLeftOrRightPointerOnNewNode( newNode, node, searchKey );
					node.setLeft( newNode );
					if ( node.getBitIndex() > newNode.getBitIndex() ) {
						// new node comes above node
						newNode.setParent( node.getParent() );
						node.setParent( newNode );
					}
					else if ( node.getBitIndex() < newNode.getBitIndex() ) {
						// new node comes below node
						newNode.setParent( node );
					}

					res.copy( newNode );
				}
				else {
					doInsert( node.getLeft(), searchKey, res );
				}
			}
			// search right
			else if ( bit == '1' ) {

				if ( node.getRight() == null ) {
					Node n = createNewNode( node, searchKey );
					n.setParent( node );
					node.setRight( n );
					res.copy( n );
				}
				if ( node.getBitIndex() >= node.getRight().getBitIndex() ) {
					// find upward pointer
					Node newNode = createNewNode( node, searchKey );
					updateLeftOrRightPointerOnNewNode( newNode, node, searchKey );
					node.setRight( newNode );
					if ( node.getBitIndex() > newNode.getBitIndex() ) {
						// new node comes above node
						newNode.setParent( node.getParent() );
						node.setParent( newNode );
					}
					else if ( node.getBitIndex() < newNode.getBitIndex() ) {
						// new node comes below node
						newNode.setParent( node );
					}

					res.copy( newNode );
				}
				else {
					doInsert( node.getRight(), searchKey, res );
				}
			}
		}
	}

	private Node initializeNode(Node parent, Node left, Node right, int bitIndex, String searchKey) {

		return new Node( bitIndex, parent, left, right, searchKey );
	}

	private void updateLeftOrRightPointerOnNewNode(Node newNode, Node node, String searchKey) {

		if ( getBitAt( newNode.getBitIndex(), searchKey ) == '0' ) {
			newNode.setLeft( node );
		}
		else if ( getBitAt( newNode.getBitIndex(), searchKey ) == '1' ) {
			newNode.setRight( node );
		}
	}

	/**
	 * parent,left and right may need to update after this method call.
	 * This method doesn't set the pointers except self pointer since they are set differently depending on different
	 * conditions.
	 * 
	 * @param node
	 * @param searchKey
	 * @return
	 */
	private Node createNewNode(Node node, String searchKey) {

		int diffIndex = getDiffAtFrom( node.getKey(), searchKey, toBytes( searchKey ).length() - 1 );
		Node n = initializeNode( null, null, null, diffIndex, searchKey );
		setPointToItself( n, node, diffIndex, searchKey );

		return n;
	}

	private void setPointToItself(Node node, Node parent, int diffIndex, String searchKey) {

		if ( getBitAt( diffIndex, searchKey ) == '0' ) {
			node.setLeft( node );

			if ( getBitAt( diffIndex, parent.getKey() ) == '1' ) {
				node.setRight( parent );
			}
		}
		else {
			node.setRight( node );

			if ( getBitAt( diffIndex, parent.getKey() ) == '0' ) {
				node.setLeft( parent );
			}
		}
	}

	public final int getLeftMostOneBit(String str) {

		char[] binary = toBytes( str ).toCharArray();
		int index = 0;
		for ( char c : binary ) {
			if ( c == '1' ) {
				break;
			}

			index++;
		}

		return index;
	}

	/**
	 * TODO need to be reviewed later.
	 * 
	 * @param str
	 */
	public final String toBytes(String str) {

		byte[] bytes = str.getBytes();
		StringBuilder binary = new StringBuilder();
		for ( byte b : bytes ) {
			int val = b;
			for ( int i = 0; i < 8; i++ ) {
				binary.append( ( val & 128 ) == 0 ? 0 : 1 );
				val <<= 1;
			}
		}

		return binary.toString();
	}

	public final char getBitAt(int bitIndex, String str) {

		String binary = toBytes( str );

		log.debug( "str: " + str + " binary expression: " + binary );

		if ( ( bitIndex >= 0 ) && ( bitIndex < binary.length() ) ) {
			return binary.toCharArray()[bitIndex];
		}

		log.warn( "bitIndex," + bitIndex + " is out of range, " + binary.length()
				+ ". about to prepend zeros and return the corresponding bit," + bitIndex );

		return prependGaps( binary.toCharArray(), getGapsAsZeroBits( bitIndex - binary.toCharArray().length ) )[bitIndex - 1];
	}

	/**
	 * returning -1 means both of the parameters are the same. the parameter, from is inclusive.
	 * 
	 * @param nodeKey
	 *            Key within each node.
	 * @param searchKey
	 *            Key to be searched.
	 * @param from
	 *            Index to start.
	 * @return
	 */
	public final int getDiffAtFrom(String nodeKey, String searchKey, int from) {

		char[] nodeKeyArray = toBytes( nodeKey ).toCharArray();
		char[] searchKeyArray = toBytes( searchKey ).toCharArray();

		if ( searchKeyArray.length < from ) {
			if ( ( nodeKeyArray.length - searchKeyArray.length ) == 0 ) {
				nodeKeyArray = prependGaps( nodeKeyArray, getGapsAsZeroBits( from - nodeKeyArray.length ) );
				searchKeyArray = prependGaps( searchKeyArray, getGapsAsZeroBits( from - searchKeyArray.length ) );

			}
			else if ( ( nodeKeyArray.length - searchKeyArray.length ) > 0 ) {
				searchKeyArray = prependGaps( searchKeyArray, getGapsAsZeroBits( nodeKeyArray.length
						- searchKeyArray.length ) );
			}
		}

		int length = Math.min( nodeKeyArray.length, searchKeyArray.length );
		int index = 0;
		int start = ( length - from - 1 ) >= 0 ? ( length - from - 1 ) : length;
		for ( int i = start; i < length; i++ ) {
			if ( nodeKeyArray[i] != searchKeyArray[i] ) {
				break;
			}

			index++;
		}

		return index;
	}

	public final char[] getGapsAsZeroBits(int gap) {

		char[] gaps = new char[gap];
		for ( int i = 0; i < gaps.length; i++ ) {
			gaps[i] = '0';
		}

		return gaps;
	}

	public final char[] prependGaps(char[] original, char[] gaps) {

		char[] res = new char[original.length + gaps.length];
		int index = 0;

		for ( index = 0; index < original.length; index++ ) {
			res[index] = original[index];
		}
		for ( int i = 0; i < gaps.length; i++ ) {
			res[index++] = gaps[i];
		}

		return res;
	}
}

package io.github.bananapuncher714.brickboard.util;

import java.util.List;

public class Util {
	public static final < T > void reverseList( List< T > toReverse ) {
		int len = toReverse.size() - 1;
		for ( int index = 0; index < Math.ceil( len / 2.0 ); index++ ) {
			T object = toReverse.get( index );
			toReverse.set( index, toReverse.get( len - index ) );
			toReverse.set( len - index, object );
		}
	}

}
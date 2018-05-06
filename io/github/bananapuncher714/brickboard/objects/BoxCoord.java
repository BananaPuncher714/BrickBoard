package io.github.bananapuncher714.brickboard.objects;

/**
 * Comparable, with top left corner being the least and bottom right being the greatest
 * 
 * @author BananaPuncher714
 */
public class BoxCoord implements Comparable< BoxCoord >, Cloneable {
	protected int x, y, height, width;
	
	public BoxCoord( int x, int y, int height, int width ) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}
	
	public BoxCoord( int x, int y ) {
		this( x, y, 0, 0 );
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoxCoord other = (BoxCoord) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int compareTo( BoxCoord arg0 ) {
		int value = ( y * 310 ) + x;
		int other = ( arg0.getY() * 310 ) + arg0.getX();
		if ( value > other ) {
			return 1;
		} else if ( value == other ) {
			return 0;
		} else {
			return -1;
		}
	}
	
	@Override
	public BoxCoord clone() {
		return new BoxCoord( x, y, width, height );
	}
}

package io.github.bananapuncher714.brickboard.chat;

public class HoverAction implements Cloneable {
	protected Action action;
	protected String message;

	public HoverAction( Action action, String message ) {
		this.action = action;
		this.message = message;
	}
	
	public Action getAction() {
		return action;
	}

	public HoverAction setAction( Action action ) {
		this.action = action;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public HoverAction setMessage( String message ) {
		this.message = message;
		return this;
	}

	public enum Action {
		SHOW_ACHIEVEMENT, SHOW_ENTITY, SHOW_ITEM, SHOW_TEXT;
	}
	
	@Override
	public HoverAction clone() {
		return new HoverAction( action, message );
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		HoverAction other = (HoverAction) obj;
		if (action != other.action)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}
}

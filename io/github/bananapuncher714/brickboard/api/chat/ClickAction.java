package io.github.bananapuncher714.brickboard.api.chat;

import java.io.Serializable;

/**
 * Allow messages to do something when clicked on
 * 
 * @author BananaPuncher714
 */
public class ClickAction implements Cloneable, Serializable {
	protected Action action;
	protected String message;
	
	public ClickAction( Action action, String message ) {
		this.action = action;
		this.message = message;
	}

	public Action getAction() {
		return action;
	}

	public ClickAction setAction(Action action) {
		this.action = action;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ClickAction setMessage(String message) {
		this.message = message;
		return this;
	}

	public enum Action {
		OPEN_URL, RUN_COMMAND, SUGGEST_COMMAND;
	}
	
	@Override
	public ClickAction clone() {
		return new ClickAction( action, message );
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
		ClickAction other = (ClickAction) obj;
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

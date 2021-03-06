package io.github.bananapuncher714.brickboard.api.chat;

import java.io.Serializable;

import org.bukkit.ChatColor;

/**
 * Simple chat wrapper; The text should not contain any color codes!
 * 
 * @author BananaPuncher714
 */
public class ChatComponent implements Cloneable, Serializable {
	protected String text;
	protected boolean bold = false, underline = false, italic = false, random = false, strikethrough = false;
	protected ChatColor color = ChatColor.WHITE;
	
	ClickAction clickAction;
	HoverAction hoverAction;

	public ChatComponent( String text ) {
		this.text = text;
	}

	public ClickAction getClickAction() {
		return clickAction;
	}

	public ChatComponent setClickAction(ClickAction clickAction) {
		this.clickAction = clickAction;
		return this;
	}

	public HoverAction getHoverAction() {
		return hoverAction;
	}

	public ChatComponent setHoverAction(HoverAction hoverAction) {
		this.hoverAction = hoverAction;
		return this;
	}

	public ChatColor getColor() {
		return color;
	}

	public ChatComponent setColor(ChatColor color) {
		this.color = color;
		return this;
	}

	public String getText() {
		return text;
	}

	public ChatComponent setText( String text ) {
		this.text = text;
		return this;
	}

	public boolean isBold() {
		return bold;
	}

	public ChatComponent setBold( boolean bold ) {
		this.bold = bold;
		return this;
	}

	public boolean isUnderline() {
		return underline;
	}

	public ChatComponent setUnderline( boolean underline ) {
		this.underline = underline;
		return this;
	}

	public boolean isItalic() {
		return italic;
	}

	public ChatComponent setItalic( boolean italic ) {
		this.italic = italic;
		return this;
	}

	public boolean isRandom() {
		return random;
	}

	public ChatComponent setRandom( boolean random ) {
		this.random = random;
		return this;
	}

	public boolean isStrikethrough() {
		return strikethrough;
	}

	public ChatComponent setStrikethrough( boolean strikethrough ) {
		this.strikethrough = strikethrough;
		return this;
	}

	public String getLegacyText() {
		StringBuilder builder = new StringBuilder();
		builder.append( color );
		if ( bold ) {
			builder.append( ChatColor.BOLD );
		}
		if ( underline ) {
			builder.append( ChatColor.UNDERLINE );
		}
		if ( italic ) {
			builder.append( ChatColor.ITALIC );
		}
		if ( strikethrough ) {
			builder.append( ChatColor.STRIKETHROUGH );
		}
		if ( random ) {
			builder.append( ChatColor.MAGIC );
		}
		return builder.append( text ).toString();
	}
	
	public ChatComponent clearFormatting() {
		bold = false;
		italic = false;
		random = false;
		strikethrough = false;
		underline = false;
		return this;
	}
	
	@Override
	public String toString() {
		if ( clickAction == null || hoverAction == null ) {
			return getLegacyText();
		}
		StringBuilder builder = new StringBuilder();
		builder.append( "<" );
		builder.append( getLegacyText() );
		if ( hoverAction != null ) {
			builder.append( hoverAction.toString() );
		}
		if ( clickAction != null ) {
			builder.append( clickAction.toString() );
		}
		builder.append( ">" );
		return builder.toString();
		
	}
	
	@Override
	public ChatComponent clone() {
		ChatComponent c = new ChatComponent( text );
		c.setColor( color );
		c.setBold( bold );
		c.setItalic( italic );
		c.setRandom( random );
		c.setStrikethrough( strikethrough );
		c.setUnderline( underline );
		
		if ( clickAction != null ) {
			c.setClickAction( clickAction.clone() );
		}
		if ( hoverAction != null ) {
			c.setHoverAction( hoverAction.clone() );
		}
		return c;
	}
}

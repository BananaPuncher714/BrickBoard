package io.github.bananapuncher714.brickboard.objects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;

public class MinecraftFontContainer {
	/** Array of the start/end column (in upper/lower nibble) for every glyph in the /font directory. */
	private final byte[] glyphWidth = new byte[65536];
	protected final int[] charWidth = new int[256];
	/** the height in pixels of default text */
	public static final int FONT_HEIGHT = 9;

	public MinecraftFontContainer( InputStream defaultFont, InputStream glyph_sizes ) {
		try {
			glyph_sizes.read( glyphWidth );
			readFontTexture( defaultFont );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	private void readFontTexture( InputStream defaultFont ) throws IOException {
        BufferedImage bufferedimage;

		bufferedimage = ImageIO.read( defaultFont );


        int lvt_3_2_ = bufferedimage.getWidth();
        int lvt_4_1_ = bufferedimage.getHeight();
        int[] lvt_5_1_ = new int[lvt_3_2_ * lvt_4_1_];
        bufferedimage.getRGB(0, 0, lvt_3_2_, lvt_4_1_, lvt_5_1_, 0, lvt_3_2_);
        int lvt_6_1_ = lvt_4_1_ / 16;
        int lvt_7_1_ = lvt_3_2_ / 16;
        float lvt_9_1_ = 8.0F / (float)lvt_7_1_;

        for (int lvt_10_1_ = 0; lvt_10_1_ < 256; ++lvt_10_1_) {
            int j1 = lvt_10_1_ % 16;
            int k1 = lvt_10_1_ / 16;

            if (lvt_10_1_ == 32) {
                this.charWidth[lvt_10_1_] = 4;
            }

            int l1;

            for (l1 = lvt_7_1_ - 1; l1 >= 0; --l1) {
                int i2 = j1 * lvt_7_1_ + l1;
                boolean flag1 = true;

                for (int j2 = 0; j2 < lvt_6_1_ && flag1; ++j2) {
                    int k2 = (k1 * lvt_7_1_ + j2) * lvt_3_2_;

                    if ((lvt_5_1_[i2 + k2] >> 24 & 255) != 0) {
                        flag1 = false;
                    }
                }

                if (!flag1) {
                    break;
                }
            }

            ++l1;
            this.charWidth[lvt_10_1_] = (int)(0.5D + (double)((float)l1 * lvt_9_1_)) + 1;
        }
    }
	
	public int getCharWidth( char character )  {
		if (character == 160) {
			return 4; // forge: display nbsp as space. MC-2595
		}
		if (character == 167) {
			return -1;
		} else if (character == ' ') {
			return 4;
		} else if ( character == '\n' ) {
			return 0;
		} else {
			int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(character);

            if ( character > 0 && i != -1 ) {
                return this.charWidth[ i ];
            } else if (this.glyphWidth[character] != 0) {
                int j = this.glyphWidth[character] & 255;
                int k = j >>> 4;
                int l = j & 15;
                ++l;
                return (l - k) / 2 + 1;
            } else {
                return 4;
            }
		}
	}
	
	/**
     * Returns the width of this string. Equivalent of FontMetrics.stringWidth(String s).
     */
    public int getStringWidth( String text ) {
        if ( text == null ) {
            return 0;
        } else {
            int i = 0;
            boolean flag = false;

            for (int j = 0; j < text.length(); ++j) {
                char c0 = text.charAt(j);
                int k = getCharWidth(c0);
                
                if ( k < 0 && j < text.length() - 1 ) {
                    ++j;
                    c0 = text.charAt(j);

                    if (c0 != 'l' && c0 != 'L') {
                        if (c0 == 'r' || c0 == 'R') {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }
                    k = 0;
                }
                i += k;

                if (flag && k > 0) {
                    ++i;
                }
            }
            return i;
        }
    }
    
    public int getStringWidth( String message, boolean isBold ) {
    	int len = 0;
    	for ( char ch : ChatColor.stripColor( message ).toCharArray() ) {
    		if ( isBold ) {
    			len++;
    		}
    		len = len + getCharWidth( ch );
    	}
    	return len;
    }
}

/*⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼
  Copyright (C) 2020-2021 developed by Icovid and Apollo Development Team

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published
  by the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.
  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see https://www.gnu.org/licenses/.

  Contact: Icovid#3888 @ https://discord.com
 ⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼⎼*/

package net.apolloclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Custom unicode font renderer used though out the client
 *
 * @author Icovid | Icovid#3888
 * @since 1.2.0-BETA
 */
public class FontRenderer {


    public RendererCallback drawString(String str, int xPosition, int yPosition, int color, boolean shadow) {
        return null;
    }

    /**
     * Strip all of the color and emoji patterns from text
     *
     * @param str text that's being stripped
     *
     * @return the string without any color formatting and emoji patterns
     */
    public static String stripFormatting(final String str) {
        String tmp = Pattern.compile(":[^ :]*:").matcher(str).replaceAll("");
        return Pattern.compile("§[0123456789abcdefklmnor!]").matcher(str).replaceAll("");
    }


    /**
     * Callback used to get width and height a render method has taken
     * up with text
     *
     * @author Icovid | Icovid#3888
     * @since 1.2.0-BETA
     */
    public static class RendererCallback {

        /** height of rendered string */
        public final int width;
        /** width of rendered string */
        public final int height;

        /**
         * @param width  of rendered string
         * @param height width of rendered string
         */
        public RendererCallback(int width, int height) {
            this.width  = width;
            this.height = height;
        }
    }

    public static class ApolloUnicode extends UnicodeFont {

        /** cache of string widths discovered by renderer */
        private final Map<String, Integer> stringWidthMap = new HashMap<>();
        /** cache of string heights discovered by renderer */
        private final Map<String, Integer> stringHeightMap = new HashMap<>();

        /**
         * @param fontName string of .ttf font file as {@link InputStream}
         * @param fontSize size font will be renderer
         **/
        public ApolloUnicode(String fontName, float fontSize) throws FontFormatException, IOException, SlickException {
            this(ApolloUnicode.class.getResourceAsStream(fontName), fontSize);
        }

        /**
         * @param fontStream {@link InputStream} of .ttf font file
         * @param fontSize   size font will be renderer
         **/
        public ApolloUnicode(InputStream fontStream, float fontSize) throws FontFormatException, IOException, SlickException {
            super(Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontSize * new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor() / 2));
            this.addAsciiGlyphs();
            this.getEffects().add(new ColorEffect(Color.WHITE));
            this.loadGlyphs();
        }

        /**
         * Get height of string.
         *
         * @param input string to get height for
         *
         * @return height of string
         **/
        @Override
        public int getHeight(String input) {
            if (stringHeightMap.size() > 1000) stringHeightMap.clear();
            return stringHeightMap.computeIfAbsent(input, e -> (int) (super.getHeight(input) / 2.0F));
        }

        /**
         * Get width of string.
         *
         * @param input string to get width for
         *
         * @return width of string
         **/
        @Override
        public int getWidth(String input) {
            if (stringWidthMap.size() > 1000) stringWidthMap.clear();
            return stringWidthMap.computeIfAbsent(input, e -> super.getWidth(stripFormatting(input)) / new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor());
        }
    }
}

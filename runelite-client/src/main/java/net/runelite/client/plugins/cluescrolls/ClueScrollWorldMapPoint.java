/*
 * Copyright (c) 2018, Morgan Lewis <https://github.com/MESLewis>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.cluescrolls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;

class ClueScrollWorldMapPoint extends WorldMapPoint
{
	private final BufferedImage worldMapImage;
	private final Point imagePoint;
	private final BufferedImage edgeSnapImage;

	ClueScrollWorldMapPoint()
	{
		super(null, null);

		this.setSnapToEdge(true);
		this.setJumpOnClick(true);

		worldMapImage = new BufferedImage(ClueScrollPlugin.MAP_ARROW.getWidth(), ClueScrollPlugin.MAP_ARROW.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = worldMapImage.getGraphics();
		graphics.drawImage(ClueScrollPlugin.MAP_ARROW, 0, 0, null);
		graphics.drawImage(ClueScrollPlugin.CLUE_SCROLL_IMAGE, 0,  2, null);

		imagePoint = new Point(worldMapImage.getWidth() / 2, worldMapImage.getHeight());
		this.setImage(worldMapImage);
		this.setImagePoint(imagePoint);

		edgeSnapImage = ClueScrollPlugin.CLUE_SCROLL_IMAGE;
	}

	@Override
	public void onEdgeSnap()
	{
		this.setImage(edgeSnapImage);
		this.setImagePoint(null);
	}

	@Override
	public void onEdgeUnsnap()
	{
		this.setImage(worldMapImage);
		this.setImagePoint(imagePoint);
	}
}

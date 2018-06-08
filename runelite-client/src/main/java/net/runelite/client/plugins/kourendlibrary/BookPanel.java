/*
 * Copyright (c) 2018 Abex
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
package net.runelite.client.plugins.kourendlibrary;

import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

class BookPanel extends JPanel
{
	private JLabel location = new JLabel();

	BookPanel(Book b)
	{
		setBorder(new EmptyBorder(3, 3, 3, 3));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		JLabel image = new JLabel();
		b.getIcon().addTo(image);
		JLabel name = new JLabel(b.getShortName());
		location.setFont(FontManager.getRunescapeSmallFont());

		layout.setVerticalGroup(layout.createParallelGroup()
			.addComponent(image)
			.addGroup(layout.createSequentialGroup()
				.addComponent(name)
				.addComponent(location)
			)
		);

		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addComponent(image)
			.addGap(8)
			.addGroup(layout.createParallelGroup()
				.addComponent(name)
				.addComponent(location)
			)
		);

		// AWT's Z order is weird. This put image at the back of the stack
		setComponentZOrder(image, getComponentCount() - 1);
	}

	public void setLocation(String location)
	{
		this.location.setText(location);
	}

	public void setIsTarget(boolean target)
	{
		location.setForeground(target ? Color.GREEN : Color.WHITE);
	}
}
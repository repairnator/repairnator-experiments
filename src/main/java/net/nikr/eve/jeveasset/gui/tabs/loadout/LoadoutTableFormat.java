/*
 * Copyright 2009-2018 Contributors (see credits.txt)
 *
 * This file is part of jEveAssets.
 *
 * jEveAssets is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * jEveAssets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jEveAssets; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package net.nikr.eve.jeveasset.gui.tabs.loadout;

import ca.odell.glazedlists.GlazedLists;
import java.util.Comparator;
import net.nikr.eve.jeveasset.gui.shared.table.EnumTableColumn;
import net.nikr.eve.jeveasset.gui.shared.table.containers.ModulePriceValue;
import net.nikr.eve.jeveasset.i18n.TabsLoadout;

/**
 *
 * @author Candle
 */
public enum LoadoutTableFormat implements EnumTableColumn<Loadout> {
	NAME(String.class, GlazedLists.comparableComparator()) {
		@Override
		public String getColumnName() {
			return TabsLoadout.get().columnName();
		}
		@Override
		public Object getColumnValue(final Loadout from) {
			return from.getName();
		}
	},
	VALUE(ModulePriceValue.class, GlazedLists.comparableComparator()) {
		@Override
		public String getColumnName() {
			return TabsLoadout.get().columnValue();
		}
		@Override
		public Object getColumnValue(final Loadout from) {
			return from.getModulePriceValue();
		}
	};

	private final Class<?> type;
	private final Comparator<?> comparator;
	private LoadoutTableFormat(final Class<?> type, final Comparator<?> comparator) {
		this.type = type;
		this.comparator = comparator;
	}
	@Override
	public Class<?> getType() {
		return type;
	}
	@Override
	public Comparator<?> getComparator() {
		return comparator;
	}
	@Override
	public boolean isColumnEditable(final Object baseObject) {
		return false;
	}
	@Override
	public boolean isShowDefault() {
		return true;
	}
	@Override
	public boolean setColumnValue(final Object baseObject, final Object editedValue) {
		return false;
	}
	@Override
	public String toString() {
		return getColumnName();
	}
}

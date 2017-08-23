/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import { shallow } from 'enzyme';
import React from 'react';
import TagsSelector from '../TagsSelector';

const props = {
  position: { left: 0, top: 0 },
  tags: ['foo', 'bar', 'baz'],
  selectedTags: ['bar'],
  onSearch: () => {},
  onSelect: () => {},
  onUnselect: () => {}
};

it('should render with selected tags', () => {
  const tagsSelector = shallow(<TagsSelector {...props} />);
  expect(tagsSelector).toMatchSnapshot();
});

it('should render without tags at all', () => {
  expect(shallow(<TagsSelector {...props} tags={[]} selectedTags={[]} />)).toMatchSnapshot();
});

it('should validate tags correctly', () => {
  const validChars = 'abcdefghijklmnopqrstuvwxyz0123456789+-#.';
  const tagsSelector = shallow(<TagsSelector {...props} />).instance();
  expect(tagsSelector.validateTag('test')).toBe('test');
  expect(tagsSelector.validateTag(validChars)).toBe(validChars);
  expect(tagsSelector.validateTag(validChars.toUpperCase())).toBe(validChars);
  expect(tagsSelector.validateTag('T E$ST')).toBe('test');
  expect(tagsSelector.validateTag('T E$st!^àéèing1')).toBe('testing1');
});

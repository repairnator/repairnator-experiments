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
import React from 'react';
import { shallow } from 'enzyme';
import { BarChart } from '../bar-chart';

it('should display bars', () => {
  const data = [{ x: 1, y: 10 }, { x: 2, y: 30 }, { x: 3, y: 20 }];
  const chart = shallow(<BarChart data={data} width={100} height={100} barsWidth={20} />);
  expect(chart.find('.bar-chart-bar').length).toBe(3);
});

it('should display ticks', () => {
  const data = [{ x: 1, y: 10 }, { x: 2, y: 30 }, { x: 3, y: 20 }];
  const ticks = ['A', 'B', 'C'];
  const chart = shallow(
    <BarChart data={data} xTicks={ticks} width={100} height={100} barsWidth={20} />
  );
  expect(chart.find('.bar-chart-tick').length).toBe(3);
});

it('should display values', () => {
  const data = [{ x: 1, y: 10 }, { x: 2, y: 30 }, { x: 3, y: 20 }];
  const values = ['A', 'B', 'C'];
  const chart = shallow(
    <BarChart data={data} xValues={values} width={100} height={100} barsWidth={20} />
  );
  expect(chart.find('.bar-chart-tick').length).toBe(3);
});

it('should display bars, ticks and values', () => {
  const data = [{ x: 1, y: 10 }, { x: 2, y: 30 }, { x: 3, y: 20 }];
  const ticks = ['A', 'B', 'C'];
  const values = ['A', 'B', 'C'];
  const chart = shallow(
    <BarChart data={data} xTicks={ticks} xValues={values} width={100} height={100} barsWidth={20} />
  );
  expect(chart.find('.bar-chart-bar').length).toBe(3);
  expect(chart.find('.bar-chart-tick').length).toBe(6);
});

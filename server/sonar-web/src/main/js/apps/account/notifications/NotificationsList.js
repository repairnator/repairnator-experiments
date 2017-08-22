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
import Checkbox from '../../../components/controls/Checkbox';
import { translate } from '../../../helpers/l10n';
import { Notification, NotificationsState, ChannelsState, TypesState } from '../../../store/notifications/duck';

export default class NotificationsList extends React.Component {
  props: {
    onAdd: (n: Notification) => void,
    onRemove: (n: Notification) => void,
    channels: ChannelsState,
    checkboxId: (string, string) => string,
    types: TypesState,
    notifications: NotificationsState
  };

  isEnabled (type: string, channel: string): boolean {
    return !!this.props.notifications.find(notification => (
        notification.type === type && notification.channel === channel
    ));
  }

  handleCheck (type: string, channel: string, checked: boolean) {
    if (checked) {
      this.props.onAdd({ type, channel });
    } else {
      this.props.onRemove({ type, channel });
    }
  }

  render () {
    const { channels, checkboxId, types } = this.props;

    return (
        <tbody>
          {types.map(type => (
              <tr key={type}>
                <td>{translate('notification.dispatcher', type)}</td>
                {channels.map(channel => (
                    <td key={channel} className="text-center">
                      <Checkbox
                          checked={this.isEnabled(type, channel)}
                          id={checkboxId(type, channel)}
                          onCheck={checked => this.handleCheck(type, channel, checked)}/>
                    </td>
                ))}
              </tr>
          ))}
        </tbody>
    );
  }
}

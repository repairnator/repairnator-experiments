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
import { connect } from 'react-redux';
import { translate } from '../../../../helpers/l10n';
import { isPermissionsAppLoading } from '../../../../store/rootReducer';

class PageHeader extends React.Component {
  props: {
    loading?: boolean,
    organization?: {}
  };

  static defaultProps = {
    loading: false
  };

  render() {
    const title = this.props.organization
      ? translate('permissions.page')
      : translate('global_permissions.page');

    const description = this.props.organization
      ? translate('organization_permissions.page.description')
      : translate('global_permissions.page.description');

    return (
      <header className="page-header">
        <h1 className="page-title">{title}</h1>

        {this.props.loading && <i className="spinner" />}

        <div className="page-description">{description}</div>
      </header>
    );
  }
}

const mapStateToProps = state => ({
  loading: isPermissionsAppLoading(state)
});

export default connect(mapStateToProps)(PageHeader);

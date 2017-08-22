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
import { connect } from 'react-redux';
import React from 'react';
import { translate } from '../../../helpers/l10n';
import { getLanguageByKey } from '../../../store/rootReducer';

class LanguageFilterOption extends React.Component {
  static propTypes = {
    languageKey: React.PropTypes.string.isRequired,
    language: React.PropTypes.object
  }

  render () {
    const languageName = this.props.language ? this.props.language.name : this.props.languageKey;
    return (
      <span>{this.props.languageKey !== '<null>' ? languageName : translate('unknown')}</span>
    );
  }
}

const mapStateToProps = (state, ownProps) => ({
  language: getLanguageByKey(state, ownProps.languageKey)
});

export default connect(mapStateToProps)(LanguageFilterOption);

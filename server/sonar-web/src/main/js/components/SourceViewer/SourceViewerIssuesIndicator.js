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
// @flow
import React from 'react';
import { connect } from 'react-redux';
import SeverityIcon from '../shared/severity-icon';
import { getIssueByKey } from '../../store/rootReducer';
import { sortBySeverity } from '../../helpers/issues';

class SourceViewerIssuesIndicator extends React.Component {
  props: {
    issue: { severity: string }
  };

  render () {
    return (
      <SeverityIcon severity={this.props.issue.severity}/>
    );
  }
}

const mapStateToProps = (state, ownProps: { issues: Array<string> }) => {
  const issues = ownProps.issues.map(issueKey => getIssueByKey(state, issueKey));
  return { issue: sortBySeverity(issues)[0] };
};

export default connect(mapStateToProps)(SourceViewerIssuesIndicator);

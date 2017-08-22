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
import classNames from 'classnames';
import uniqBy from 'lodash/uniqBy';
import SourceViewerHeader from './SourceViewerHeader';
import SourceViewerCode from './SourceViewerCode';
import CoveragePopupView from '../source-viewer/popups/coverage-popup';
import DuplicationPopupView from '../source-viewer/popups/duplication-popup';
import LineActionsPopupView from '../source-viewer/popups/line-actions-popup';
import SCMPopupView from '../source-viewer/popups/scm-popup';
import MeasuresOverlay from '../source-viewer/measures-overlay';
import { TooltipsContainer } from '../mixins/tooltips-mixin';
import Source from '../source-viewer/source';
import loadIssues from './helpers/loadIssues';
import getCoverageStatus from './helpers/getCoverageStatus';
import {
  issuesByLine,
  locationsByLine,
  locationsByIssueAndLine,
  locationMessagesByIssueAndLine,
  duplicationsByLine,
  symbolsByLine
} from './helpers/indexing';
import { getComponentForSourceViewer, getSources, getDuplications, getTests } from '../../api/components';
import { translate } from '../../helpers/l10n';
import type { SourceLine } from './types';
import type { Issue } from '../issue/types';

// TODO react-virtualized

type Props = {
  aroundLine?: number,
  component: string,
  displayAllIssues: boolean,
  filterLine?: (line: SourceLine) => boolean,
  highlightedLine?: number,
  loadComponent: (string) => Promise<*>,
  loadIssues: (string, number, number) => Promise<*>,
  loadSources: (string, number, number) => Promise<*>,
  onLoaded?: (component: Object, sources: Array<*>, issues: Array<*>) => void,
  onIssueSelect: (string) => void,
  onIssueUnselect: () => void,
  onReceiveComponent: ({ canMarkAsFavorite: boolean, fav: boolean, key: string }) => void,
  onReceiveIssues: (issues: Array<*>) => void,
  selectedIssue: string | null,
};

type State = {
  component?: Object,
  displayDuplications: boolean,
  duplications?: Array<{
    blocks: Array<{
      _ref: string,
      from: number,
      size: number
    }>
  }>,
  duplicationsByLine: { [number]: Array<number> },
  duplicatedFiles?: Array<{ key: string }>,
  hasSourcesAfter: boolean,
  highlightedLine: number | null,
  highlightedSymbol: string | null,
  issues?: Array<Issue>,
  issuesByLine: { [number]: Array<string> },
  issueLocationsByLine: { [number]: Array<{ from: number, to: number }> },
  issueSecondaryLocationsByIssueByLine: {
    [string]: {
      [number]: Array<{ from: number, to: number }>
    }
  },
  issueSecondaryLocationMessagesByIssueByLine: {
    [issueKey: string]: {
      [line: number]: Array<{ msg: string, index?: number }>
    }
  },
  loading: boolean,
  loadingSourcesAfter: boolean,
  loadingSourcesBefore: boolean,
  notAccessible: boolean,
  notExist: boolean,
  sources?: Array<SourceLine>,
  symbolsByLine: { [number]: Array<string> }
};

const LINES = 500;

const loadComponent = (key: string): Promise<*> => {
  return getComponentForSourceViewer(key);
};

const loadSources = (key: string, from?: number, to?: number): Promise<Array<*>> => {
  return getSources(key, from, to);
};

export default class SourceViewerBase extends React.Component {
  mounted: boolean;
  node: HTMLElement;
  props: Props;
  state: State;

  static defaultProps = {
    displayAllIssues: false,
    onIssueSelect: () => { },
    onIssueUnselect: () => { },
    loadComponent,
    loadIssues,
    loadSources
  };

  constructor (props: Props) {
    super(props);
    this.state = {
      displayDuplications: false,
      duplicationsByLine: {},
      hasSourcesAfter: false,
      highlightedLine: props.highlightedLine || null,
      highlightedSymbol: null,
      issuesByLine: {},
      issueLocationsByLine: {},
      issueSecondaryLocationsByIssueByLine: {},
      issueSecondaryLocationMessagesByIssueByLine: {},
      loading: true,
      loadingSourcesAfter: false,
      loadingSourcesBefore: false,
      notAccessible: false,
      notExist: false,
      selectedIssue: props.defaultSelectedIssue || null,
      symbolsByLine: {}
    };
  }

  componentDidMount () {
    this.mounted = true;
    this.fetchComponent();
  }

  componentDidUpdate (prevProps: Props) {
    if (prevProps.component !== this.props.component) {
      this.fetchComponent();
    } else if (this.props.aroundLine != null && prevProps.aroundLine !== this.props.aroundLine &&
      this.isLineOutsideOfRange(this.props.aroundLine)) {
      this.fetchSources();
    }
  }

  componentWillUnmount () {
    this.mounted = false;
  }

  computeCoverageStatus (lines: Array<SourceLine>): Array<SourceLine> {
    return lines.map(line => ({ ...line, coverageStatus: getCoverageStatus(line) }));
  }

  isLineOutsideOfRange (lineNumber: number) {
    const { sources } = this.state;
    if (sources != null && sources.length > 0) {
      const firstLine = sources[0];
      const lastList = sources[sources.length - 1];
      return lineNumber < firstLine.line || lineNumber > lastList.line;
    } else {
      return true;
    }
  }

  fetchComponent () {
    this.setState({ loading: true });

    const loadIssues = (component, sources) => {
      this.props.loadIssues(this.props.component, 1, LINES).then(issues => {
        this.props.onReceiveIssues(issues);
        if (this.mounted) {
          const finalSources = sources.slice(0, LINES);
          this.setState({
            component,
            issues,
            issuesByLine: issuesByLine(issues),
            issueLocationsByLine: locationsByLine(issues),
            issueSecondaryLocationsByIssueByLine: locationsByIssueAndLine(issues),
            issueSecondaryLocationMessagesByIssueByLine: locationMessagesByIssueAndLine(issues),
            loading: false,
            hasSourcesAfter: sources.length > LINES,
            sources: this.computeCoverageStatus(finalSources),
            symbolsByLine: symbolsByLine(sources.slice(0, LINES))
          }, () => {
            if (this.props.onLoaded) {
              this.props.onLoaded(component, finalSources, issues);
            }
          });
        }
      });
    };

    const onFailLoadComponent = ({ response }) => {
      // TODO handle other statuses
      if (this.mounted && response.status === 404) {
        this.setState({ loading: false, notExist: true });
      }
    };

    const onFailLoadSources = (response, component) => {
      // TODO handle other statuses
      if (this.mounted) {
        if (response.status === 403) {
          this.setState({ component, loading: false, notAccessible: true });
        }
      }
    };

    const onResolve = component => {
      this.props.onReceiveComponent(component);
      this.loadSources().then(
        sources => loadIssues(component, sources),
        response => onFailLoadSources(response, component)
      );
    };

    this.props.loadComponent(this.props.component).then(onResolve, onFailLoadComponent);
  }

  fetchSources () {
    this.loadSources().then(sources => {
      if (this.mounted) {
        const finalSources = sources.slice(0, LINES);
        this.setState({
          sources: sources.slice(0, LINES),
          hasSourcesAfter: sources.length > LINES
        }, () => {
          if (this.props.onLoaded) {
            // $FlowFixMe
            this.props.onLoaded(this.state.component, finalSources, this.state.issues);
          }
        });
      }
    });
  }

  loadSources () {
    return new Promise((resolve, reject) => {
      const onFailLoadSources = ({ response }) => {
        // TODO handle other statuses
        if (this.mounted) {
          if (response.status === 403) {
            reject(response);
          } else if (response.status === 404) {
            resolve([]);
          }
        }
      };

      const from = this.props.aroundLine ? Math.max(1, this.props.aroundLine - LINES / 2 + 1) : 1;
      // request one additional line to define `hasSourcesAfter`
      const to = this.props.aroundLine ? this.props.aroundLine + LINES / 2 + 1 : LINES + 1;

      return this.props.loadSources(this.props.component, from, to).then(
        sources => resolve(sources),
        onFailLoadSources
      );
    });
  }

  loadSourcesBefore = () => {
    if (!this.state.sources) {
      return;
    }
    const firstSourceLine = this.state.sources[0];
    this.setState({ loadingSourcesBefore: true });
    const from = Math.max(1, firstSourceLine.line - LINES);
    this.props.loadSources(this.props.component, from, firstSourceLine.line - 1).then(sources => {
      this.props.loadIssues(this.props.component, from, firstSourceLine.line - 1).then(issues => {
        this.props.onReceiveIssues(issues);
        if (this.mounted) {
          this.setState(prevState => ({
            issues: uniqBy([...issues, ...prevState.issues], issue => issue.key),
            loadingSourcesBefore: false,
            sources: [...this.computeCoverageStatus(sources), ...prevState.sources],
            symbolsByLine: { ...prevState.symbolsByLine, ...symbolsByLine(sources) }
          }));
        }
      });
    });
  };

  loadSourcesAfter = () => {
    if (!this.state.sources) {
      return;
    }
    const lastSourceLine = this.state.sources[this.state.sources.length - 1];
    this.setState({ loadingSourcesAfter: true });
    const fromLine = lastSourceLine.line + 1;
    // request one additional line to define `hasSourcesAfter`
    const toLine = lastSourceLine.line + LINES + 1;
    this.props.loadSources(this.props.component, fromLine, toLine).then(sources => {
      this.props.loadIssues(this.props.component, fromLine, toLine).then(issues => {
        this.props.onReceiveIssues(issues);
        if (this.mounted) {
          this.setState(prevState => ({
            issues: uniqBy([...prevState.issues, ...issues], issue => issue.key),
            hasSourcesAfter: sources.length > LINES,
            loadingSourcesAfter: false,
            sources: [...prevState.sources, ...this.computeCoverageStatus(sources.slice(0, LINES))],
            symbolsByLine: { ...prevState.symbolsByLine, ...symbolsByLine(sources.slice(0, LINES)) }
          }));
        }
      });
    });
  };

  loadDuplications = (line: SourceLine, element: HTMLElement) => {
    getDuplications(this.props.component).then(r => {
      if (this.mounted) {
        this.setState({
          displayDuplications: true,
          duplications: r.duplications,
          duplicationsByLine: duplicationsByLine(r.duplications),
          duplicatedFiles: r.files
        }, () => {
          // immediately show dropdown popup if there is only one duplicated block
          if (r.duplications.length === 1) {
            this.handleDuplicationClick(0, line.line, element);
          }
        });
      }
    });
  };

  openNewWindow = () => {
    const { component } = this.state;
    if (component != null) {
      let query = 'id=' + encodeURIComponent(component.key);
      const windowParams = 'resizable=1,scrollbars=1,status=1';
      if (this.state.highlightedLine) {
        query = query + '&line=' + this.state.highlightedLine;
      }
      window.open(window.baseUrl + '/component/index?' + query, component.name, windowParams);
    }
  };

  showMeasures = () => {
    const model = new Source(this.state.component);
    const measuresOvervlay = new MeasuresOverlay({ model, large: true });
    measuresOvervlay.render();
  };

  handleCoverageClick = (line: SourceLine, element: HTMLElement) => {
    getTests(this.props.component, line.line).then(tests => {
      const popup = new CoveragePopupView({ line, tests, triggerEl: element });
      popup.render();
    });
  };

  handleDuplicationClick = (index: number, line: number) => {
    const duplication = this.state.duplications && this.state.duplications[index];
    let blocks = (duplication && duplication.blocks) || [];
    const inRemovedComponent = blocks.some(b => b._ref == null);
    let foundOne = false;
    blocks = blocks.filter(b => {
      const outOfBounds = b.from > line || b.from + b.size < line;
      const currentFile = b._ref === '1';
      const shouldDisplayForCurrentFile = outOfBounds || foundOne;
      const shouldDisplay = !currentFile || shouldDisplayForCurrentFile;
      const isOk = (b._ref != null) && shouldDisplay;
      if (b._ref === '1' && !outOfBounds) {
        foundOne = true;
      }
      return isOk;
    });

    const element = this.node.querySelector(`.source-line-duplications-extra[data-line-number="${line}"]`);
    if (element) {
      const popup = new DuplicationPopupView({
        blocks,
        inRemovedComponent,
        component: this.state.component,
        files: this.state.duplicatedFiles,
        triggerEl: element
      });
      popup.render();
    }
  };

  displayLinePopup (line: number, element: HTMLElement) {
    const popup = new LineActionsPopupView({
      line,
      triggerEl: element,
      component: this.state.component
    });
    popup.render();
  }

  handleLineClick = (line: number, element: HTMLElement) => {
    this.setState(prevState => ({
      highlightedLine: prevState.highlightedLine === line ? null : line
    }));
    this.displayLinePopup(line, element);
  };

  handleSymbolClick = (symbol: string) => {
    this.setState(prevState => ({
      highlightedSymbol: prevState.highlightedSymbol === symbol ? null : symbol
    }));
  };

  handleSCMClick = (line: SourceLine, element: HTMLElement) => {
    const popup = new SCMPopupView({ triggerEl: element, line });
    popup.render();
  };

  renderCode (sources: Array<SourceLine>) {
    const hasSourcesBefore = sources.length > 0 && sources[0].line > 1;
    return (
      <TooltipsContainer>
        <SourceViewerCode
          displayAllIssues={this.props.displayAllIssues}
          duplications={this.state.duplications}
          duplicationsByLine={this.state.duplicationsByLine}
          duplicatedFiles={this.state.duplicatedFiles}
          hasSourcesBefore={hasSourcesBefore}
          hasSourcesAfter={this.state.hasSourcesAfter}
          filterLine={this.props.filterLine}
          highlightedLine={this.state.highlightedLine}
          highlightedSymbol={this.state.highlightedSymbol}
          issues={this.state.issues}
          issuesByLine={this.state.issuesByLine}
          issueLocationsByLine={this.state.issueLocationsByLine}
          issueSecondaryLocationsByIssueByLine={this.state.issueSecondaryLocationsByIssueByLine}
          issueSecondaryLocationMessagesByIssueByLine={this.state.issueSecondaryLocationMessagesByIssueByLine}
          loadDuplications={this.loadDuplications}
          loadSourcesAfter={this.loadSourcesAfter}
          loadSourcesBefore={this.loadSourcesBefore}
          loadingSourcesAfter={this.state.loadingSourcesAfter}
          loadingSourcesBefore={this.state.loadingSourcesBefore}
          onCoverageClick={this.handleCoverageClick}
          onDuplicationClick={this.handleDuplicationClick}
          onIssueSelect={this.props.onIssueSelect}
          onIssueUnselect={this.props.onIssueUnselect}
          onLineClick={this.handleLineClick}
          onSCMClick={this.handleSCMClick}
          onSymbolClick={this.handleSymbolClick}
          selectedIssue={this.props.selectedIssue}
          sources={sources}
          symbolsByLine={this.state.symbolsByLine}/>
      </TooltipsContainer>
    );
  }

  render () {
    const { component, loading } = this.state;

    if (loading) {
      return null;
    }

    if (this.state.notExist) {
      return (
        <div className="alert alert-warning spacer-top">{translate('component_viewer.no_component')}</div>
      );
    }

    if (component == null) {
      return null;
    }

    const className = classNames('source-viewer', { 'source-duplications-expanded': this.state.displayDuplications });

    return (
      <div className={className} ref={node => this.node = node}>
        <SourceViewerHeader
          component={this.state.component}
          openNewWindow={this.openNewWindow}
          showMeasures={this.showMeasures}/>
        {this.state.notAccessible && (
          <div className="alert alert-warning spacer-top">
            {translate('code_viewer.no_source_code_displayed_due_to_security')}
          </div>
        )}
        {this.state.sources != null && this.renderCode(this.state.sources)}
      </div>
    );
  }
}

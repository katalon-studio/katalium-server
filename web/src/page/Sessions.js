import React from 'react';
import { Card, CardBody, CardHeader, Breadcrumb, BreadcrumbItem } from 'reactstrap';
import Services from '../utils/Services';
import DataTable from '../table/DataTable';
import MTableColumnDataMapping from '../table/MTableColumnDataMapping';
import { Link } from 'react-router-dom';

export default class Sessions extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      sessions: [],
    };
  }

  getSessions() {
    Services.getSessions()
      .then((sessions) => {
        this.setState({ sessions });
      });
  }

  componentDidMount() {
    this.getSessions();
  }

  render() {
    const columnMapping = [
      new MTableColumnDataMapping('Session ID', 'id', (name, row) => {
        const sessionId = row[name];
        return <Link to={'/grid/admin/KatalonConsole/sessions/' + sessionId}>{sessionId}</Link>;
      }),
      new MTableColumnDataMapping('Browser', 'browser'),
      new MTableColumnDataMapping('Version', 'version'),
      new MTableColumnDataMapping('Platform', 'platform'),
      new MTableColumnDataMapping('Created At', 'createdAt'),
    ];

    return (
      <>
        <Breadcrumb>
          <BreadcrumbItem active>
            <Link to={'/grid/admin/KatalonConsole/index.html'}>Sessions</Link>
          </BreadcrumbItem>
        </Breadcrumb>
        <Card>
          <CardHeader>
            Sessions
          </CardHeader>
          <CardBody>
            <DataTable data={this.state.sessions} columnMapping={columnMapping}/>
          </CardBody>
        </Card>
      </>
    );
  }
}
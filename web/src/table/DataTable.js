import React from 'react';
import { Table, Row, Col } from 'reactstrap';

class DataTable extends React.Component {

  render() {
    const rows = this.props.data.slice().map((row) => {
      const cols = this.props.columnMapping.map(col => (
        <td>{col.decorate(col.fieldName, row)}</td>
      ));
      return <tr>{cols}</tr>;
    });
    const headers = this.props.columnMapping.map(header =>
      <th>{header.headerCaption}</th>);

    return (
      <Row>
        <Col>
          <Table responsive className={this.props.className}>
            <thead>
              <tr>
                {headers}
              </tr>
            </thead>
            <tbody>
              {rows}
            </tbody>
          </Table>
        </Col>
      </Row>
    );
  }
}

DataTable.defaultProps = {
  data: [],
  columnMapping: []
};

export default DataTable;

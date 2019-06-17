import React from 'react';
import { ButtonGroup, Button, Card, CardBody, CardHeader, Breadcrumb, BreadcrumbItem, Col, Row } from 'reactstrap';
import { Link } from 'react-router-dom';
import Services from '../utils/Services';

export default class Session extends React.Component {

  constructor(props) {
    super(props);
    this.sessionId = this.props.match.params.sessionId;
    this.state = {
      session: null,
      maxRange: 0,
      selectedRange: 0,
      screenshots: [],
    };

    this.onRangeChange = this.onRangeChange.bind(this);
    this.next = this.next.bind(this);
    this.previous = this.previous.bind(this);
    this.keyChange = this.keyChange.bind(this);
  }

  getSession() {
    Services.getSession(this.sessionId)
      .then((result) => {
        const session = result;
        this.setState({ session });
      });
  }

  getScreenshots() {
    Services.getScreenshots(this.sessionId)
      .then((result) => {
        const screenshots = result;
        const maxRange = result.length - 1;
        this.setState({ screenshots, maxRange });
      });
  }

  componentDidMount() {
    this.getSession();
    this.getScreenshots();
  }

  keyChange(event) {
    console.log(event);
  }

  onRangeChange(event) {
    const selectedRange = parseInt(event.target.value);
    this.setState({ selectedRange });
  }

  next() {
    const { maxRange, selectedRange } = this.state;
    let newSelectedRange = selectedRange + 1;
    if (newSelectedRange > maxRange) {
      newSelectedRange = maxRange;
    }
    this.setState({ selectedRange: newSelectedRange })
  }

  previous() {
    const { selectedRange } = this.state;
    let newSelectedRange = selectedRange - 1;
    if (newSelectedRange < 0) {
      newSelectedRange = 0;
    }
    this.setState({ selectedRange: newSelectedRange })
  }

  render() {
    const {
      session,
      maxRange,
      selectedRange,
      screenshots
    } = this.state;

    if (session) {
      const selectedScreenshot = screenshots[selectedRange];
      return (
        <>
          <Breadcrumb>
            <BreadcrumbItem>
              <Link to={'/grid/admin/KatalonConsole/index.html'}>Sessions</Link>
            </BreadcrumbItem>
            <BreadcrumbItem active>
              <Link to={'/grid/admin/KatalonConsole/sessions/' + this.sessionId}>{this.sessionId}</Link>
            </BreadcrumbItem>
          </Breadcrumb>
          <Card>
            <CardHeader>
              Screenshots
            </CardHeader>
            <CardBody>
              <Row>
                <Col>
                  <div className="flex-container">
                    <div>
                      <ButtonGroup>
                        <Button color="secondary" onClick={this.previous}>Prev</Button>
                        <Button color="primary" onClick={this.next}>Next</Button>
                      </ButtonGroup>
                      <input
                        type="range" className="custom-range"
                        min="0" max={maxRange} value={selectedRange}
                        onChange={this.onRangeChange}
                      />
                    </div>
                    {selectedScreenshot &&
                    <div>
                      <p>
                        <span>{'Browser: ' + session.browser}</span>&emsp;&emsp;&emsp;
                        <span>{'Version: ' + session.version}</span>&emsp;&emsp;&emsp;
                        <span>{'Platform: ' + session.platform}</span>&emsp;&emsp;&emsp;
                        <span>{'Created At: ' + selectedScreenshot.createdAt}</span>
                      </p>
                    </div>
                    }
                    <div id="carouselExampleFade" className="carousel slide carousel-fade" data-ride="carousel">
                      <div className="carousel-inner">
                        {
                          screenshots.map((screenshot, index) => {
                            const className = `carousel-item  screenshot ${index === selectedRange ? 'active' : ''}`;
                            return (
                              <div className={className}>
                                <img
                                  className="d-block w-100"
                                  src={'data:image/png;base64, ' + screenshot.image}
                                  alt="..."
                                />
                              </div>
                            );
                          })
                        }
                      </div>
                    </div>
                  </div>
                </Col>
              </Row>
            </CardBody>
          </Card>
        </>
      );
    }
    return null;
  }
}
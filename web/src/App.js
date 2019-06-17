import React from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import Sessions from "./page/Sessions";
import Session from "./page/Session";

function App() {
  return (
    <Router>
      <div>
        <Route exact path="/" component={Sessions} />
        <Route exact path="/grid/admin/KatalonConsole/index.html" component={Sessions} />
        <Route exact path="/grid/admin/KatalonConsole/sessions/:sessionId" component={Session} />
      </div>
    </Router>
  );
}

export default App;

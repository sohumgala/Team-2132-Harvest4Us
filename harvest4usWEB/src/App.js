import './App.css';
import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Dashboard from "./components/Dashboard.js";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        username: "",
    }
    this.logIn = this.logIn.bind(this);
  }

  logIn(user) {
    this.setState({
      username: user
    });
  }

  render() {
      return (
        <Dashboard />
      );
  }
}

export default App;

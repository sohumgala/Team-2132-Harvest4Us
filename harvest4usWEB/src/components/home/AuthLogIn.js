import React, { useState } from "react";
import Login from "./Login.js";
import Registration from "./Registration.js";
import Dashboard from "../Dashboard.js"
import { Modal } from 'react-bootstrap';
import GlobalUser from "../GlobalUser.js"
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Redirect,
  } from "react-router-dom";

  export default function AuthLogIn(props) {
    return (
        <Router>
          <div>
              <Switch>
                <Route path="/Login">
                  <Login logIn={props.logIn}/>
                  <RegistrationContainer />
                </Route>

                <Route path="/Dashboard">
                  {GlobalUser.user === "test@test.com" ? <Dashboard /> : 
                  <Redirect to="/Login" />}
                </Route>

                <Route exact path="/">
                  <Redirect to="/Login" />
                </Route>

              </Switch>
            </div>
        </Router>
    )
  }

  function RegistrationContainer() {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    
    return (
        <>
        <p>Don't have an account? &nbsp;
        <a onClick={handleShow} style={{color: "#283618", textDecoration: "underline"}}>
          Register here.
        </a></p>
          <Modal size="lg" show={show} onHide={handleClose}>
            <Modal.Header closeButton>
              <Modal.Title>Create New Account</Modal.Title>
            </Modal.Header>
            <Modal.Body>

                <Registration />

            </Modal.Body>
            
          </Modal>
        </>
      );
    }

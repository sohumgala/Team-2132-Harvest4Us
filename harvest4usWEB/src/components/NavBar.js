import React, { Component } from "react";
import { Navbar, Nav, NavDropdown, Container } from 'react-bootstrap';
import {BrowserRouter as Router, Switch, Route, Link} from "react-router-dom";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Storefront from "./storefront/Storefront.js";
import UserProfile from "./profile/UserProfile";
import PendingSales from "./sales/PendingSales.js";

export default class NavBar extends Component {
    render() {
        return (
            <Router>
        <div>
            <div>
            <Navbar className="shadow-sm" bg="light" expand="lg">
                <Container>
                    <Navbar.Brand>
                        <img src="https://9193032-images.s3.amazonaws.com/harvest4uslogo.svg" 
                            width="140"
                            height="auto"
                            className="d-inline-block align-top"
                            alt="Harvest4Us Logo"/>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
                    <Nav className="mx-5">
                        <Nav.Link as={Link} to="/Storefront"> Storefront</Nav.Link>
                        <Nav.Link as={Link} to="/PendingSales">Pending Sales</Nav.Link>
                        <NavDropdown title={
                            <AccountCircleIcon></AccountCircleIcon>
                        } id="basic-nav-dropdown" >
                            <NavDropdown.Item as={Link} to="/Profile">View Profile</NavDropdown.Item>
                            <NavDropdown.Item>Account Settings</NavDropdown.Item>
                        <NavDropdown.Divider />
                            <NavDropdown.Item>Log Out</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            </div>
            <div>
                <Switch>
                    <Route path="/Storefront">
                        <Storefront />
                    </Route>
                    <Route path="/Profile">
                        <UserProfile />
                    </Route>
                    <Route path="/PendingSales">
                        <PendingSales />
                    </Route>
                </Switch>
            </div>
        </div>
        </Router>
        );
    }
}
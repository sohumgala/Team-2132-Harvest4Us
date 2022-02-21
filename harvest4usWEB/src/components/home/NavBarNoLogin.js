import React, { Component } from "react";
import { Navbar, Nav, Container} from 'react-bootstrap';

//Navbar -- before the log in screen is complete
export default class NavBar extends Component {
    render() {
        return (
            <div>
            <Navbar bg="light" expand="lg">
                <Container>
                    <Navbar.Brand>
                        <img src="https://9193032-images.s3.amazonaws.com/harvest4uslogo.svg" 
                            width="140"
                            height="auto"
                            className="d-inline-block align-top"
                            alt="Harvest4Us Logo"/>
                    </Navbar.Brand>.
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                    <Nav >
                        
                    </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            </div>
            

        );
    }
}
import React, { Component } from 'react';
import axios from 'axios';
import {Form, Button, Container, Row, Col} from 'react-bootstrap';


export default class Registration extends Component {
    constructor(props) {
        super(props);

        this.state = {
            first_name: "",
            last_name: "",
            username: "",
            password: "",
            password_confirmation: "",
            registrationErrors: "",
            errors: []
        }


        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    //determine if error is present before submit
    hasError(key) {
        return this.state.errors.indexOf(key) !== -1;
    }

    //on user input, handle the change
    handleChange(e) {
        this.setState({
            [e.target.name]: e.target.value,
        });
    }
     
    //determine if all inputs are valid on submit, and if so, post
    handleSubmit(e) {
        e.preventDefault();
       
        var errors = [];

        const nameExpression =  /^(?!.*[0-9])([a-zA-Z]).{1,}/;
        var validName = nameExpression.test(String(this.state.first_name).toLowerCase());
        if (!validName) {
            errors.push("first_name");
        }

        validName = nameExpression.test(String(this.state.last_name).toLowerCase());
        if (!validName) {
            errors.push("last_name");
        }

         //email
        const mailExpression = /\S+@\S+/;
        var validEmail = mailExpression.test(String(this.state.username).toLowerCase());

        if (!validEmail) {
            errors.push("username");
        }

        const passwordExpression =  /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
        var validPassword = passwordExpression.test(String(this.state.password));
        if (!validPassword) {
            errors.push("password");
        }

        if (!((this.state.password_confirmation).normalize() === (this.state.password).normalize())) {
            errors.push("password_confirmation");
        }

        this.setState({
            errors: errors
        });

        if (errors.length > 0) {
            return false;
        } else {
            axios.post("https://sc8yb3bdy5.execute-api.us-east-1.amazonaws.com/dev/new-user/", {
                user: {
                    first_name: this.state.first_name,
                    last_name: this.state.last_name,
                    username: this.state.username,
                    password: this.state.password
                }
            }
            //,{ withCredentials: true }
            ).then(response => {
                console.log("registration res", response);
                if (response["status"] === 200) {
                    alert("Account Created Successfully");
                }
            }).catch(error => {
                console.log("registration error", error);
                var er = error.toString();
                if (er.match(/code 304/)) {
                    alert("Account already exists under this email.");
                }
            });
        }
    }

    render() {
        return (
            <div>
                <Container>
                <Form noValidate onSubmit={this.handleSubmit}>
                    <Row className="mb-2">
                        <Col>
                            <Form.Group controlId="formFirstName">
                            <Form.Label>First Name</Form.Label>
                            <Form.Control
                            type="text" 
                            name="first_name" 
                            placeholder="First Name" 
                            value={this.state.first_name} 
                            onChange={this.handleChange}
                            className = {this.hasError("first_name") ? "form-control is-invalid" : "form-control"}
                            required 
                            />
                            <Form.Control.Feedback type="invalid">
                                First name must be at least two characters and cannot contain numbers.
                            </Form.Control.Feedback>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="formLastName">
                                <Form.Label>Last Name</Form.Label>
                                <Form.Control
                                type="text" 
                                name="last_name" 
                                placeholder="Last Name" 
                                value={this.state.last_name} 
                                onChange={this.handleChange}
                                className = {this.hasError("last_name") ? "form-control is-invalid" : "form-control"}
                                required 
                                />
                                <Form.Control.Feedback type="invalid">
                                Last name must be at least one character and cannot contain numbers.
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Col>
                    </Row>
                    <Row className="mb-2">
                        
                    <Form.Group controlId="formBasicEmail">
                        <Form.Label>Email Address</Form.Label>
                        <Form.Control 
                        type = "email"
                        name="username" 
                        placeholder="Email Address" 
                        value={this.state.email} 
                        onChange={this.handleChange}
                        className = {this.hasError("username") ? "form-control is-invalid" : "form-control"}
                        required/>
                        <Form.Control.Feedback type="invalid">
                        Please provide a valid email.
                        </Form.Control.Feedback>
                    </Form.Group>
                    </Row>
                    <Row className="mb-2">
                    <Form.Group controlId="formBasicPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                        type="password" 
                        name="password" 
                        placeholder="Password" 
                        value={this.state.password} 
                        onChange={this.handleChange}
                        className = {this.hasError("password") ? "form-control is-invalid" : "form-control"}
                        required/>
                        <Form.Text id="passwordHelpBlock" muted>
                            Password must be at least 8 characters long and contain at least one lowercase letter, uppercase letter, and number.
                        </Form.Text>
                    </Form.Group>
                    
                    </Row>
                    <Row className="mb-2">
                    <Form.Group controlId="formConfirmPassword">
                        <Form.Label>Confirm Password</Form.Label>
                        <Form.Control
                        type="password" 
                        name="password_confirmation" 
                        placeholder="Confirm Password" 
                        value={this.state.password_confirmation} 
                        onChange={this.handleChange}
                        className = {this.hasError("password_confirmation") ? "form-control is-invalid" : "form-control"}
                        required/>
                        <Form.Control.Feedback type="invalid">
                        Entered passwords must match.
                        </Form.Control.Feedback>
                    </Form.Group>
                    </Row>
                    <Row>
                    <Button variant="outline-primary" type="submit">
                        Submit
                    </Button>
                    </Row>
                </Form>
                </Container>
            </div>
        );
    } 
}
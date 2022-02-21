import React, { Component } from 'react';
import axios from 'axios';
import { Form, Button } from 'react-bootstrap';
import GlobalUser from "../GlobalUser.js";

export default class Login extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: "",
            password: "",
            registrationErrors: "",
            errors: []
        }


        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    //determines if error is present
    hasError(key) {
        return this.state.errors.indexOf(key) !== -1;
    }

    //on user input, updates change to the relevant item
    handleChange(e) {
        this.setState({
            [e.target.name]: e.target.value,
        });
    }
     
    //on submit, determine if all inputs are valid
    handleSubmit(e) {
        e.preventDefault();
       
        var errors = [];
       

         //email
        const mailExpression = /\S+@\S+/;
        var validEmail = mailExpression.test(String(this.state.username).toLowerCase());

        if (!validEmail) {
            errors.push("username");
        }


        this.setState({
            errors: errors
        });

        if (errors.length > 0) {
            return false;
        } else {
            var log = "https://ohn09n3sii.execute-api.us-east-1.amazonaws.com/dev/new-user/" + this.state.username + "||" + this.state.password;
            axios(log
            //,{ withCredentials: true }
            ).then(response => {
                console.log("registration res", response);
                if (response["status"] === 200) {
                    alert("Successfully Logged In");
                }
                //save user
                GlobalUser.user = this.state.username;
                Object.freeze(GlobalUser);
                this.props.logIn(GlobalUser.user);
            }).catch(error => {
                console.log("registration error", error);
                var er = error.toString();
                if (er.match(/code 401/)) {
                    alert("Invalid username or password.");
                }
            });
        }
    }

    render() {
        return (
                <div >
                    <h2>Sign In</h2>
                   <Form noValidate onSubmit={this.handleSubmit} className="w-100">
                        <Form.Group controlId="formBasicEmail" className="mb-2">
                            <Form.Label>Email Address</Form.Label>
                            <Form.Control 
                            type = "email"
                            name="username" 
                            placeholder="Email Address" 
                            value={this.state.email} 
                            onChange={this.handleChange}
                            required/>
                            <Form.Control.Feedback type="invalid">
                            Please provide a valid email.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId="formBasicPassword" className="mb-2">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                            type="password" 
                            name="password" 
                            placeholder="Password" 
                            value={this.state.password} 
                            onChange={this.handleChange}
                            required/>
                        </Form.Group>

                        <Button variant="outline-primary" className="w-100 mb-2" type="submit">
                            Submit
                        </Button>
                    </Form>
                </div>
        );
    } 
}


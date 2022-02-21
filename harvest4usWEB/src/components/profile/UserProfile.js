import React, { Component } from "react";
import {Button, Card, Row, ListGroup} from 'react-bootstrap';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import Person from '@mui/icons-material/Person';
import BusinessIcon from '@mui/icons-material/Business';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import FieldEditOn from "../FieldEditOn.js"
import axios from 'axios';
import GlobalUser from "../GlobalUser.js"

//Class which shows the User's Profile attributes, including first_name,
//last_name, business_name, business_street_address, business_city,
//business_state,business_zip, and a profile description.
//Attributes are editable by user.
export default class UserProfile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: GlobalUser.user,
            first_name: "",
            last_name: "",
            business_name: "",
            business_street_address: "",
            business_city: "",
            business_state: "",
            business_zip: "",
            about: "",
            disableEdits: true
        }

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleProfileUpdate = this.handleProfileUpdate.bind(this);
    }

    //Loads state from database after UserProfile mounts to the DOM
    componentDidMount() {
        var log = "https://ohn09n3sii.execute-api.us-east-1.amazonaws.com/dev/get-producers/" + this.state.user;
        axios(log).then( response => {
            console.log(response.data.message);
            this.setState({
                first_name: response.data.message.first_name,
                last_name: response.data.message.last_name,
                business_name: response.data.message.business_name,
                business_street_address: response.data.message.business_street,
                business_city: response.data.message.business_city,
                business_state: response.data.message.business_state,
                business_zip: response.data.message.business_zip,
                about: response.data.message.about
            });
        });
    }

    //handle key input
    handleInputChange(e) {
        this.setState({
            [e.target.name]: e.target.value,
          });
    }

    //Changes disableEdits state to the opposite (if disableEdits is true, now it is false)
    //If user is saving new data, the data is POSTed
    handleProfileUpdate(e) {
        this.setState({disableEdits: ! this.state.disableEdits});
        if (!this.state.disableEdits) {
            axios.post("https://ohn09n3sii.execute-api.us-east-1.amazonaws.com/dev/update-profile", {
                user_profile: {
                    username: this.state.user,
                    first_name: this.state.first_name,
                    last_name: this.state.last_name,
                    description: (this.state.about).toString(),
                    business_name: this.state.business_name,
                    street: this.state.business_street_address,
                    city: this.state.business_city,
                    st: this.state.business_state,
                    zip_code: this.state.business_zip
                }
            }).then(response => console.log(response));
        }
    }
    
    //Rendering the component
    render() {
        return(
            <div className="d-flex justify-content-around align-items-center" style={{height: "90vh"}}>
                <Card className="w-50">
                    <Card.Header> Profile </Card.Header>
                    <Row className="d-flex justify-content-around">
                        <AccountCircleIcon sx={{ fontSize: 150 }}></AccountCircleIcon>
                    </Row>
                <ListGroup variant="flush">
                    <ListGroup.Item>
                        <span style={{display:"inline-flex", width: "90%"}}>
                        <BusinessIcon></BusinessIcon>
                        {(this.state.disableEdits) ? this.state.business_name :
                        <FieldEditOn field="business_name" fieldValue={this.state.business_name} handleInputChange={this.handleInputChange} />}
                     </span>
                     </ListGroup.Item>

                    <ListGroup.Item>
                        <span style={{display:"inline-flex", width: "90%"}}>
                        <Person></Person>
                        {(this.state.disableEdits) ? (this.state.first_name + " " + this.state.last_name) :
                        <div>
                        <FieldEditOn field="first_name" fieldValue={this.state.first_name} handleInputChange={this.handleInputChange} />
                        <FieldEditOn field="last_name" fieldValue={this.state.last_name} handleInputChange={this.handleInputChange} />
                        </div>}
                        </span>
                    </ListGroup.Item>

                    <ListGroup.Item>
                        <span style={{display:"inline-flex", width: "90%"}}>
                        <LocationOnIcon></LocationOnIcon>
                        {(this.state.disableEdits) ? (this.state.business_street_address + ", " + this.state.business_city + ", " + this.state.business_state + " " + this.state.business_zip) :
                        <div>
                        <FieldEditOn field="business_street_address" fieldValue={this.state.business_street_address} handleInputChange={this.handleInputChange} />
                        <FieldEditOn field="business_city" fieldValue={this.state.business_city} handleInputChange={this.handleInputChange} />
                        <FieldEditOn field="business_state" fieldValue={this.state.business_state} handleInputChange={this.handleInputChange} />
                        <FieldEditOn field="business_zip" fieldValue={this.state.business_zip} handleInputChange={this.handleInputChange} />
                        </div>}
                        </span>
                    </ListGroup.Item>
                </ListGroup>

                <Card.Title className="pt-2 pb-0">About</Card.Title>
                <Card.Body className="pt-0">
                <span style={{display:"flex", width: "90%"}}>
                        {(this.state.disableEdits) ? this.state.about :
                        <FieldEditOn textArea="true" field="about" fieldValue={this.state.about} handleInputChange={this.handleInputChange} />}
                     </span>
                </Card.Body>

                <Button onClick={this.handleProfileUpdate}>{(this.state.disableEdits) ? "Edit Profile" : "Save Changes"}</Button>
                </Card>
            </div>
        );
    }
}

import React, { Component, useState } from "react";
import {ListGroup, ListGroupItem, Card, Row, Col, Button, ButtonGroup, ToggleButton } from "react-bootstrap";
import axios from 'axios';
import GlobalUser from "../GlobalUser.js"

export default class ProductDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: GlobalUser.user,
            id: 1,
            listingAttributes: [],
            acceptedColoring: ""
        };
        this.handleAcceptDeny = this.handleAcceptDeny.bind(this);
    }

    //Once component is mounted, load relevant listings from database
    componentDidMount() {
        this.loadListings();
    }

    //load listings from database
    //Listings represent items as they are in 
    //the current inventory; they do not reflect
    //requests.
    loadListings() {
        var log = "https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/get-by-id/" 
            + this.state.user + "||" + this.props.itemInfo.product_id;
        axios(log).then( response => {
            const arr = response.data.message;
            this.setState({
                listingAttributes: arr
            });
        });
    }

    handleAcceptDeny(e) {
        let variant = "";
        e.target.tabIndex ? variant = "success" : variant = "danger";
        this.setState({
            acceptedColoring: variant
        });
        this.props.handleAcceptDeny(e);
    }

    render() {
        return (
            <Card className="mb-3" bg={this.props.itemInfo.acceptItemSale ?
                                     "success" : 
                                     ((this.props.itemInfo.acceptItemSale === null) ? "" : "danger")} 
                                     border="secondary" >
                <Card.Header style={{display: "flex", justifyContent: "space-between"}}>
                    <div><b>
                        {this.state.listingAttributes.produceType}
                        &nbsp;{this.state.listingAttributes.produceCategory}
                        &nbsp;{this.state.listingAttributes.organic ? ", Organic" : ""}
                    </b></div>
                    <div>
                        <b>Item Subtotal:</b> $
                        {this.props.itemInfo.item_cost}
                        
                    </div>
                    
                </Card.Header>
                        <ListGroup variant="flush">
                            <ListGroupItem>
                                <b>Quantity Requested:</b>
                                &nbsp;{this.props.itemInfo.quantity_requested}
                                &nbsp;{this.state.listingAttributes.unit}
                            </ListGroupItem>
                            <ListGroupItem variant=
                            {(this.state.listingAttributes.availableQuantity < this.props.itemInfo.quantity_requested) ?
                                "warning" :
                                ""
                            }>
                                <b>Quantity Available:</b>
                                &nbsp;{this.state.listingAttributes.availableQuantity}
                                &nbsp;{this.state.listingAttributes.unit}

                            </ListGroupItem>
                            <ButtonGroup style={{background: "white"}} id={this.props.itemInfo.product_id}>
                                <ToggleButton variant="outline-success" tabIndex="1" onClick={this.handleAcceptDeny}> Accept </ToggleButton>
                                <ToggleButton variant="outline-danger" tabIndex="0" onClick={this.handleAcceptDeny}> Deny </ToggleButton>
                            </ButtonGroup>
                        </ListGroup>
                
            </Card>
        )
    };
}

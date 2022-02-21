import React, { Component } from "react";
import {Card, ListGroup, ListGroupItem, Button } from "react-bootstrap";
import SaleDetailContainer from "./SaleDetailContainer.js";
import axios from 'axios';

export default class SaleCard extends Component { 
    constructor(props) {
        super(props);
        this.state = {
            sale: props.sale,
        };
        this.handleAcceptDeny = this.handleAcceptDeny.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleAcceptDeny(e) {
        //e.target.parentNode.id - the parent ButtonGroup of Accept/Deny will have the same id as the produce it represents
        //e.target.tabIndex - which button is being pressed (1 for accept, 0 for deny)
        let arr_1 = this.state.sale.requested_items;
        for (let ind = 0; ind < arr_1.length; ind++) {
            if (arr_1[ind].product_id === Number(e.target.parentNode.id)) {
                arr_1[ind].acceptItemSale = e.target.tabIndex;
                break;
            } 
        }
        //now copy changes to copy of state array
        let arr_2 = this.state.sale;
        arr_2.requested_items = arr_1;

        //...and set state
        this.setState({
            sale: arr_2
        });
    }

    handleSubmit() {
        //first check that all selections have been made
        for (let ind = 0; ind < this.state.sale.requested_items.length; ind++) {
            if (this.state.sale.requested_items[ind].acceptItemSale === null) {
                alert("All items must be accepted or denied.")
                return;
            }
        };

        //then submit
        this.state.sale.requested_items.map((item) => {
            axios.post("https://naniidtff6.execute-api.us-east-1.amazonaws.com/dev/evaluate-order", {
                sale: {
                    producer: "test@test.com",
                    consumer: this.state.sale.consumer,
                    product_id: item.product_id,
                    sale_id: this.state.sale.sale_id,
                    approved: item.acceptItemSale
                }
            });
        });
        alert("Submitted!")
    }

     render() {
        return (
            <div className="mb-2">
                <Card style={{width: "80%"}}>
                    <Card.Header>Order #{this.state.sale.sale_id}</Card.Header>
                    <Card.Body>
                        <Card.Title>Date Placed: {this.state.sale.date_placed}</Card.Title>
                        <Card.Subtitle>Consumer: {this.state.sale.consumer}</Card.Subtitle>
                        <ListGroup className="list-group-flush">
                            <ListGroupItem>Number of Unique Produce Types: {this.state.sale.product_ids.length}</ListGroupItem>
                            <ListGroupItem>Produce Subtotal: ${this.state.sale.total_cost}</ListGroupItem>
                            <ListGroupItem>Shipping: ${this.state.sale.total_shipping}</ListGroupItem>
                        </ListGroup>
                    </Card.Body>
                    <SaleDetailContainer details={this.state.sale} handleAcceptDeny={this.handleAcceptDeny} handleSubmit={this.handleSubmit}/>
                    
                </Card>
            </div>      
        );
    } 
}


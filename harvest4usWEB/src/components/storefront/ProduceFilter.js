import React, { Component } from "react";
import { Accordion } from "react-bootstrap";

export default class Filter extends Component {
    render() {
        return (
            <div>
            <div style={{display:"flex", justifyContent: "space-around"}}>
                <h2>Filter</h2>
            </div>
            <Accordion>
            <Accordion.Item eventKey="0">
                <Accordion.Header>Produce Category</Accordion.Header>
                <Accordion.Body>
                    Future functionality goes here!
                </Accordion.Body>
            </Accordion.Item>
            <Accordion.Item eventKey="1">
                <Accordion.Header>Produce Type</Accordion.Header>
                <Accordion.Body>
                    Future functionality goes here!
                </Accordion.Body>
            </Accordion.Item>
            <Accordion.Item eventKey="2">
                <Accordion.Header>USDA Grade</Accordion.Header>
                <Accordion.Body>
                    Future functionality goes here!
                </Accordion.Body>
            </Accordion.Item>
            <Accordion.Item eventKey="3">
                <Accordion.Header>Price</Accordion.Header>
                <Accordion.Body>
                    Future functionality goes here!
                </Accordion.Body>
            </Accordion.Item>
            <Accordion.Item eventKey="4">
                <Accordion.Header>Available Quantity</Accordion.Header>
                <Accordion.Body>
                    Future functionality goes here!
                </Accordion.Body>
            </Accordion.Item>
            <Accordion.Item eventKey="5">
                <Accordion.Header>Date Edited</Accordion.Header>
                <Accordion.Body>
                    Future functionality goes here!
                </Accordion.Body>
            </Accordion.Item>
            </Accordion>
            </div>
        );
    }

}
import React from "react";
import { Card, Row, Col, Button } from "react-bootstrap";
import FieldEditOn from "../FieldEditOn.js"
import ProduceEditAttributes from "./ProduceEditAttributes.js"
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import CancelOutlinedIcon from '@mui/icons-material/CancelOutlined';

function ProduceListing(props) {


    return (<li key={props.key}>
        <div className="m-3" style={{minWidth: "200px", maxWidth:"400px"}}>
        <Card style={{width: "100%"}}>
        <Card.Header className="d-flex justify-content-between">
            {props.listing.produceCategory} 
            <Button variant="outline-secondary" name={props.listing.product_id} size="sm" onClick={props.clicked} >{props.listing.disableEdits ? "Edit" : "Cancel"}</Button>
        </Card.Header>
        <Card.Img variant="top" src={props.listing.imgLink} style={{width:"100%"}}/>
        <Card.Title style={{textAlign: "center"}}>{props.listing.produceType}</Card.Title>
        <Card.Body>
        {props.listing.disableEdits ? 
        <div>
                <Row>
                    <Col>
                    <b>USDA Grade: </b>
                    </Col>
                    <Col>
                    {props.listing.usdaGrade}
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <b>Price:</b>
                    </Col>
                    <Col>
                        {props.listing.disableEdits ? <div>${props.listing.price} / {props.listing.unit}</div> : <FieldEditOn field="price" fieldValue={props.listing.price} fieldID={props.listing.key} handleInputChange={props.edited} pattern="/^\d{0,2}\.\d{2}?$/"> </FieldEditOn>}
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <b>Quantity Remaining:</b>
                    </Col>
                    <Col>
                        {props.listing.disableEdits ? <div>{props.listing.availableQuantity} {props.listing.unit}</div> : <div><FieldEditOn field="availableQuantity" fieldValue={props.listing.availableQuantity} fieldID={props.listing.key} handleInputChange={props.edited}></FieldEditOn > {props.listing.unit}</div>}
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <b>Organic:</b>
                    </Col>
                    <Col>
                    {props.listing.organic ? "Yes" : "No"}
                    </Col>
                </Row>
                <Row>
                    <Col> 
                        <b>Active:</b>
                    </Col>
                    <Col>
                    {props.listing.active ? <CheckCircleOutlineIcon style={{color: "green"}}/> : <CancelOutlinedIcon style={{color: "red"}}/>}
                    </Col>
                </Row></div> 
                : <div>
                 <ProduceEditAttributes listing={props.listing} submitHandle={props.handleEdits} handleDelete={props.handleDelete}/>
                </div>

                }
        </Card.Body>
        <Card.Footer>
            Last Updated {props.listing.dateEdited}
        </Card.Footer>
        </Card>
        </div>
    </li>

    );           
}

export default ProduceListing;


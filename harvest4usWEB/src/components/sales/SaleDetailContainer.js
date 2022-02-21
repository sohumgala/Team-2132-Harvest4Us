import React, { useState } from "react";
import { Offcanvas, Button, ListGroup, ListGroupItem } from "react-bootstrap";
import ProductDetails from "./ProductDetails.js";

function SaleDetailContainer(props) {
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    //Collection of Cards (JSX) of items in sale
    var saleListings = [];

    props.details.requested_items.map((item) => {
        saleListings.push(
          <div>
            <ProductDetails allSaleInfo={props.details} itemInfo={item} handleAcceptDeny={props.handleAcceptDeny}/>
          </div>
        );
    });
  
    return (
      <>
        <Button variant="outline-secondary" onClick={handleShow}>
          Show Details
        </Button>
  
        <Offcanvas style={{background: "#AFB38C"}} className="w-50" show={show} onHide={handleClose} placement='end'>
          <Offcanvas.Header closeButton>
            <Offcanvas.Title>Order Details</Offcanvas.Title>
          </Offcanvas.Header>
          <Offcanvas.Body>
             {saleListings}
             <div className="d-grid">
              <ListGroup>
                <ListGroupItem>
                  Accepted Total:
                </ListGroupItem>
                <ListGroupItem>
                Shipping Total:
                </ListGroupItem>
              </ListGroup>
             <Button type="submit" onClick={props.handleSubmit}>Submit</Button>
             </div>
             
          </Offcanvas.Body>
        </Offcanvas>
      </>
    );
  }

  export default SaleDetailContainer;
 
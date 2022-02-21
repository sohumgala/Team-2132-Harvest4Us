import React, { useState, setShow } from "react";
import { Modal, Button, Container } from "react-bootstrap";
import ProduceAddWindow from "./ProduceAddWindow.js"

function StoreTools(props) {
    const [show, setShow] = useState(false);
  
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
  
    return (
      <div>
        <div style={{display:"flex", justifyContent: "space-around"}}>
        <h2>Tools</h2>
        </div>
        
        <Container>
            <Button className="w-100" onClick={handleShow}>Add New Produce</Button>
        </Container>
  
        <Modal show={show} onHide={handleClose} size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Add New Produce</Modal.Title>
          </Modal.Header>
          <ProduceAddWindow createNewProduce={props.createNewProduce} setSelectedCategory={props.setSelectedCategory} setSelectedType={props.setSelectedType}></ProduceAddWindow>
        </Modal>
      </div>
    );
  }
  
  export default StoreTools;

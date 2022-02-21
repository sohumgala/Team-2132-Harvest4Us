import React from "react";
import { Container } from "react-bootstrap";
import { Button} from 'react-bootstrap';
import {
    Link
  } from "react-router-dom";


  export default function ChooseLoginPath() {
    return (
        <Container>
            <Link to="LogIn"><Button variant="primary" style={{width:"100%"}}>Login</Button></Link>
            <Link to="Register"><Button variant="secondary" style={{width:"100%"}}>Register</Button></Link>
        </Container>
        
    );
} 

/*
<div className="w-50 d-flex" style={{height: "40vh", 
                    backgroundImage: 'url("https://9193032-images.s3.amazonaws.com/farmer_login_2.jpg")',
                    backgroundSize:"100%", 
                    borderRadius:"20px"}}>

            <div className="d-flex" style={{
                                            width: "100%",
                                            flexDirection: "column"}}>
                  <Link to="LogIn"><Button variant="primary" style={{width:"100%"}}>Login</Button></Link>
                  <Link to="Register"><Button variant="secondary" style={{width:"100%"}}>Register</Button></Link>
            </div>

        </div>
*/
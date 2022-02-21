import React, { Component } from "react";
import NavBarNoLogin from './NavBarNoLogin.js';
import AuthLogIn from "./AuthLogIn.js";

export default class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {
        }
    }
    render() {
        return (
            <div className="d-flex" style={{background: "linear-gradient(#606c38, #fefae0)", 
                                            height:"100vh",
                                            flexDirection: "column"
                                            }}>
                <NavBarNoLogin />
                <div className="d-flex w-50 shadow" style={{background: "#FFFBF6", 
                                        borderRadius: "20px",
                                        justifyContent: "flex-start",
                                        alignItems: "center",
                                        gap: "20px",
                                        margin: "auto"
                                        }}>

                            <img src="https://9193032-images.s3.amazonaws.com/farmer_login.jpg" 
                                style={{height: "60vh", width: "auto", borderRadius: "20px 0px 0px 20px"}}/>

                            <AuthLogIn logIn={this.props.logIn}/>

                </div>
                
            </div>
        );
    }
}

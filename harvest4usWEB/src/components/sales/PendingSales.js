import React, { Component } from "react";
import SaleCardGroup from './SaleCardGroup.js';

//Shows all pending sales
//loads pending sales as a collection of sale cards
export default class PendingSales extends Component { 
    render() {
        return (
            <div style={{height: "100vh"}}>
                <div style={{display: "flex", justifyContent: "space-around", background: "#DDA15E"}}>
                    <h1> Pending Sales </h1>
                </div>

                <SaleCardGroup showOnlyPending={true}/>
            </div>
        )
    } 
}
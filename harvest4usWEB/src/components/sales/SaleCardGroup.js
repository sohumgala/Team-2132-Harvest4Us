import React, { useEffect, useState } from "react";
import SaleCard from './SaleCard.js';
import { Container, Row } from "react-bootstrap";
import axios from 'axios';
/*
const tempOrders = [
    {
        producer: "test@test.com",
        sale_id: 1,
        product_id: 1,
        quantity: 11,
        consumer: "fff",
        date_placed: "12-21-21",
        shipping: 0.00,
        total_cost: 10.00,
        pendingReview: 1,
        approved: 0
    },
    {
        producer: "test@test.com",
        sale_id: 1,
        product_id: 2,
        quantity: 5,
        consumer: "fff",
        date_placed: "12-21-21",
        shipping: 2.00,
        total_cost: 19.00,
        pendingReview: 1,
        approved: 0
    },
    {
        producer: "test@test.com",
        sale_id: 2,
        product_id: 3,
        quantity: 5,
        consumer: "ggg",
        date_placed: "12-21-21",
        shipping: 2.00,
        total_cost: 19.20,
        pendingReview: 1,
        approved: 0
    },
    {
        producer: "test@test.com",
        sale_id: 2,
        product_id: 4,
        quantity: 5,
        consumer: "ggg",
        date_placed: "12-21-21",
        shipping: 0.00,
        total_cost: 19.20,
        pendingReview: 1,
        approved: 0
    },
    {
        producer: "test@test.com",
        sale_id: 2,
        product_id: 5,
        quantity: 5,
        consumer: "ggg",
        date_placed: "12-21-21",
        shipping: 0.00,
        total_cost: 19.20,
        pendingReview: 0,
        approved: 1
    },
    {
        producer: "test@test.com",
        sale_id: 2,
        product_id: 6,
        quantity: 5,
        consumer: "ggg",
        date_placed: "12-21-21",
        shipping: 0.00,
        total_cost: 19.20,
        pendingReview: 0,
        approved: 0
    },
    {
        producer: "test@test.com",
        sale_id: 3,
        product_id: 6,
        quantity: 5,
        consumer: "fff",
        date_placed: "12-21-21",
        shipping: 0.00,
        total_cost: 19.20,
        pendingReview: 1,
        approved: 0
    }
];
*/

function SaleCardGroup(props) {
    let salesRaw =[];
    const [salesCards, setSalesCards] = useState([]); //JSX fragment array for rendering
        useEffect(() => {
            let log = "https://naniidtff6.execute-api.us-east-1.amazonaws.com/dev/getOrdersByProducer/" + "test@test.com";
                axios(log).then(response => {
                    salesRaw = response.data.message;
                    //salesRaw = tempOrders;
                    console.log("sales raw", salesRaw);
                }).then( () => {
                    let uniqueSales = []; //array of unique sale IDs
                    let salesAggregate = []; //array which groups attributes by sale ID
                    
                    salesRaw.map((sale) => {
                        if (props.showOnlyPending) {
                            if (sale.pendingReview) {
                                if (uniqueSales.includes(sale.sale_id)) {
                                    let ind = uniqueSales.indexOf(sale.sale_id);
                                    salesAggregate[ind].product_ids.push(sale.product_id);
                                    salesAggregate[ind].total_shipping += sale.shipping;
                                    salesAggregate[ind].total_cost += sale.total_cost;
                                    salesAggregate[ind].requested_items.push({
                                        product_id: sale.product_id,
                                        item_cost: sale.total_cost,
                                        item_shipping: sale.shipping,
                                        quantity_requested: sale.quantity,
                                        acceptItemSale: null
                                    });
                                } else {
                                    console.log(uniqueSales);
                                    uniqueSales.push(sale.sale_id);
                                    salesAggregate.push({
                                        sale_id: sale.sale_id, 
                                        product_ids: [sale.product_id],
                                        consumer: sale.consumer,
                                        date_placed: sale.date_placed,
                                        total_shipping: sale.shipping,
                                        total_cost: sale.total_cost,
                                        requested_items: [{
                                            product_id: sale.product_id,
                                            item_cost: sale.total_cost,
                                            item_shipping: sale.shipping,
                                            quantity_requested: sale.quantity,
                                            acceptItemSale: null
                                        }]
                                    });
                                }
                            }
                        } else {
                            console.log("here");
                            if (!sale.pendingReview) {
                                salesAggregate.push();
                            }
                        } 
                    });
                    console.log("sales aggregate", salesAggregate);
                    var fragment = [];
                    salesAggregate.map((sales) => {
                        fragment.push(<Row> <SaleCard sale={sales} /> </Row>);
                    });
                    console.log("fragment", fragment);
                    setSalesCards([fragment]);
                });
        }, []);
            
        return (
        <Container>
            {salesCards}
        </Container>
        );
    
};

export default SaleCardGroup;


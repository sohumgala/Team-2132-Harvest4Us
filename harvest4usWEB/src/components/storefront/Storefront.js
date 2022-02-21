import React, { Component } from "react";
import Filter from "./ProduceFilter.js"
import StoreTools from "./StoreTools.js"
import ProduceListingGroup from "./ProduceListingGroup.js"
import axios from 'axios';
import GlobalUser from "../GlobalUser.js"

//Class Component which displays all producer listings, and allows editing
export default class Storefront extends Component {
    constructor(props) {
        super(props);
        this.state = {
            //itemsArr: the array of every produce listing
            itemsArr: [],
            selectedCategory: "",
            selectedType: "",
            user: GlobalUser.user
        }
        this.handleEditButton = this.handleEditButton.bind(this);
        this.handleEdits = this.handleEdits.bind(this);
        this.createNewProduce = this.createNewProduce.bind(this);
        this.setSelectedCategory = this.setSelectedCategory.bind(this);
        this.setSelectedType = this.setSelectedType.bind(this);
        this.loadListings = this.loadListings.bind(this);
        this.getDate = this.getDate.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.assignImageLink = this.assignImageLink.bind(this);
    }

    //Once component is mounted, load relevant listings from database
    componentDidMount() {
        this.loadListings();
    }

    //load listings from database
    loadListings() {
        var log = "https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/get-by-producer/" + this.state.user;
        axios(log).then( response => {
            const arr = response.data.message;
            arr.map((item) => {
                item["disableEdits"] = true;
                item["imgLink"] =  this.assignImageLink(item.produceCategory);
            });
            this.setState({
                itemsArr: arr
            });
        });
    }

    assignImageLink(category) {
        switch(category) {
            case 'Corn':
                return "https://9193032-images.s3.amazonaws.com/stock_corn.png";
            case 'Pear':
                return "https://9193032-images.s3.amazonaws.com/stock_pear.png";
            case 'Potato':
                return "https://9193032-images.s3.amazonaws.com/stock_potato.png";
            case 'Squash':
                return "https://9193032-images.s3.amazonaws.com/stock_squash.png";
            default: 
                return "https://9193032-images.s3.amazonaws.com/stock_apple.jpg";
        }
    }

    handleDelete(e) {
        console.log(this.state.user);
        console.log(e.target.id);
        axios.post("https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/delete-item", {
                delete: {
                    producer: this.state.user,
                    product_id: e.target.id
                }
            }).then(() => {
                this.loadListings();
            });
    }

    getDate() {
        let formatTwoDigits = (digit) => ("0" + digit).slice(-2);
        var tempDate = new Date();
        var date = `${tempDate.getFullYear()}-${formatTwoDigits(tempDate.getMonth()+1)}-${formatTwoDigits(tempDate.getDate())}`;
        return date;
    }

    //handle when edit button is clicked
    handleEditButton(e) {
        const arr = [...this.state.itemsArr];
        arr.map((item) => {
            if ((item.product_id) === Number(e.target.name)) {
                item.disableEdits = !(item.disableEdits);
            }
        });
        this.setState({
            itemsArr: arr
        });

    }

    //update state with new values input by user (as well as time stamp)
    handleEdits(values) {
        let keys = Object.keys(values);
        const arr = [...this.state.itemsArr];
        let ind;
        arr.map((item) => {
            if ((item.product_id) === values["key"]) {
                ind = this.state.itemsArr.indexOf(item);
                keys.map((key) => {
                    item[key] = values[key];
                    if (key === "availableQuantity" || key === "price"){
                        item[key] = Number(item[key]);
                    }
                });
                item["dateEdited"] = this.getDate();
                item.disableEdits = true;
            }
        });
        
        this.setState({
            itemsArr: arr
        }, () => {
            axios.post("https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/update-item/", {
                listingEdit: {
                    producer: this.state.itemsArr[ind].producer,
                    product_id: this.state.itemsArr[ind].product_id,
                    active : this.state.itemsArr[ind].active, 
                    availableQuantity : this.state.itemsArr[ind].availableQuantity,
                    dateEdited : this.state.itemsArr[ind].dateEdited,
                    price : this.state.itemsArr[ind].price
                }
            });
        });
    }

    //create a new produce listing
    createNewProduce(values) {
        axios.post("https://f6e1mmza5c.execute-api.us-east-1.amazonaws.com/dev/add-item", {
            listing: {
                producer : this.state.user,
                produceType : this.state.selectedType,
                unit : values["unit"],
                usdaGrade : values["usdaGrade"],
                active : false,
                availableQuantity : Number(values["availableQuantity"]),
                dateEdited : this.getDate().toString(),
                organic : values["organic"],
                price : Number(values["price"]),
                produceCategory : this.state.selectedCategory
            }
        }).catch(function (error){
            console.log(error);
        }).then(() => {
            this.loadListings();
        });

        
    }

    //set the selected category of a produce listing
    setSelectedCategory(c) {
        this.setState({
            selectedCategory: c
        });
    }

    //set the selected type of a produce listing
    setSelectedType(t) {
        this.setState({
            selectedType: t
        });
    }

    render() {
        return(
            <div className="d-flex" style={{height: "90vh"}}>
                <div className="w-25 border" style={{background: "white"}}>
                    <div className="border" style={{height: "70%", overflow: "scroll"}}><Filter></Filter></div>
                    <div style={{height: "30%"}}>
                        <StoreTools itemsArr={this.state.itemsArr} createNewProduce={this.createNewProduce} setSelectedCategory={this.setSelectedCategory} setSelectedType={this.setSelectedType}></StoreTools>
                    </div>
                </div>

                <div className="w-75" style={{overflowY: "scroll", overflowX: "hidden"}}>
                    <ProduceListingGroup itemsArr={this.state.itemsArr} handleEditButton={this.handleEditButton} handleEdits={this.handleEdits} handleDelete={this.handleDelete}></ProduceListingGroup>
                </div>
            </div>
        );
    }
}
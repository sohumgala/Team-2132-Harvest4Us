import React, { Component } from "react";
import ProduceAddCategories from "./ProduceAddCategories";
import ProduceAddTypes from "./ProduceAddTypes.js";
import ProduceAddAttributes from "./ProduceAddAttributes.js";

export default class ProduceAddWindow extends Component {

    constructor(props) {
        
        super(props);
        this.state = {
            allProduce: [{}],
            uniqueProduceCategories: [],
            selectedCategory: "",
            produceTypes: [],
            selectedType: "",
            formInputs: { 
                usdaGrade: "0",
                price: "",
                unit: "x",
                availableQuantity: "",
                organic: "0",
                dateEdited: "",
                active: false
            }
        }
        this.handleCategoryClick = this.handleCategoryClick.bind(this);
        this.handleTypeClick = this.handleTypeClick.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        const produce = [
            //future implementations: don't hardcode this, make a table and access via endpoint
            { category: "Apple", type: "Red Delicious", href: "https://9193032-images.s3.amazonaws.com/harvest4uslogo.svg" },
            { category: "Apple", type: "Fuji" },
            { category: "Apple", type: "Pink Lady" },
            { category: "Corn", type: "Flint" },
            { category: "Corn", type: "Dent" },
            { category: "Corn", type: "Sweet" },
            { category: "Pear", type: "Callery" },
            { category: "Pear", type: "European" },
            { category: "Potato", type: "Yukon Gold" },
            { category: "Potato", type: "Russian Blue" },
            { category: "Potato", type: "Melody" },
            { category: "Squash", type: "Butternut" },
            { category: "Squash", type: "Acorn" },

        ];

        var allP = [];
        produce.map((item) => {
            allP.push(item);
            if (this.state.uniqueProduceCategories.indexOf(item.category) === -1) {
                this.state.uniqueProduceCategories.push(item.category.toString());
            }   
        }, this.setState ({
            allProduce: allP
        }));

        this.forceUpdate();
    }


    handleCategoryClick = (e) => {
        var id = e.target.id;
        this.setState({
            selectedCategory: id
        }, function(){
            var newTypes = [];

        this.state.allProduce.map((item) => {
            if (item.category === this.state.selectedCategory) {
                newTypes.push(item.type);
            } 
        }, this.setState({
            produceTypes: newTypes
        }, this.props.setSelectedCategory(this.state.selectedCategory)));
        });

        
    }

    handleTypeClick = (e) => {
        this.setState({
            selectedType: e.target.value
        });

        this.props.setSelectedType(e.target.value);
    }


    handleSubmit = (values) => {
        {this.props.createNewProduce(values)}
    }

    
    render() {
        return (
            <div>
                    <ProduceAddCategories
                        categories = {this.state.uniqueProduceCategories} 
                        handleCategoryClick={this.handleCategoryClick}
                    />
                    <ProduceAddTypes
                        selectedCategory = {this.state.selectedCategory}
                        types = {this.state.produceTypes} 
                        handleTypeClick={this.handleTypeClick}
                    />

                    <ProduceAddAttributes 
                        handleMySubmit={this.handleSubmit}/>         
            </div>
        );
    }
}



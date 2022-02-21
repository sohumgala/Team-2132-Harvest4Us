import React from 'react';

//eventually replace this with get/post
const UserInfo =()=> {
    var info = {
        first_name: "Ford",
        last_name: "Prefect",
        business_name: "Don't Panic LLC.",
        business_street_address: "42 Thanks for All the Fish Way",
        business_city: "Manchester",
        business_state: "Tennessee"
    };
    return (
        {info}
    );
}

export default UserInfo;
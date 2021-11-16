import axios from "axios";

export default axios.create({
    baseURL: 'http://localhost:9000/api/v1/phone-book/',
    responseType: "json"
});
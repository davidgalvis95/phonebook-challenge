import { useDispatch } from "react-redux";
import { useCallback } from "react";
import phoneBookApi from "../Axios/PhoneBookApiConfig"
import allActions from "../Store/Actions/allActions"


const usePhoneBookApi = () => {
    
    const dispatch = useDispatch();

    const sendRequestToGetMatchingContacts = useCallback(async (params) => {
        dispatch(allActions.phoneBookApiActions.sendRequest());

        const query = `contacts?searchingParams=${params}`; 

        try{
            const result = await phoneBookApi.get(query);
            console.log(result);
            dispatch(allActions.phoneBookApiActions.processResponse(result.data, "GET"));
        }catch(error){
            dispatch(allActions.phoneBookApiActions.handleError(error.getMessage()));
        }
    },[]);

    const sendRequestToCreateAContact = useCallback(async (contact) => {
        dispatch(allActions.phoneBookApiActions.sendRequest());


        const query = `contact`; 

        try{
            const result = await phoneBookApi.post(query, contact);
            console.log(result);
            dispatch(allActions.phoneBookApiActions.processResponse(result.data, "CREATE"));
        }catch(error){
            dispatch(allActions.phoneBookApiActions.handleError(error.getMessage()));
        }
    },[]);

    const sendRequestToUpdateAContact = useCallback(async (contact, id) => {
        dispatch(allActions.phoneBookApiActions.sendRequest());

        const query = `contact?id=${id}`; 

        try{
            const result = await phoneBookApi.put(query, contact);
            console.log(result);
            dispatch(allActions.phoneBookApiActions.processResponse(result.data, "UPDATE"));
        }catch(error){
            dispatch(allActions.phoneBookApiActions.handleError(error.getMessage()));
        }
    },[]);

    const sendRequestToDeleteAContact = useCallback(async (id) => {
        dispatch(allActions.phoneBookApiActions.sendRequest());

        const query = `contact?id=${id}`; 

        try{
            const result = await phoneBookApi.delete(query);
            console.log(result);
            dispatch(allActions.phoneBookApiActions.processResponse(result.data, "DELETE"));
        }catch(error){
            dispatch(allActions.phoneBookApiActions.handleError(error.getMessage()));
        }
    },[]);

    const clearError = useCallback(() => dispatch(allActions.phoneBookApiActions.clearError()), []);

    return {
        sendRequestToGetMatchingContacts: sendRequestToGetMatchingContacts,
        sendRequestToCreateAContact: sendRequestToCreateAContact,
        sendRequestToUpdateAContact: sendRequestToUpdateAContact,
        sendRequestToDeleteAContact: sendRequestToDeleteAContact,
        clearError: clearError
    }
}

export default usePhoneBookApi;

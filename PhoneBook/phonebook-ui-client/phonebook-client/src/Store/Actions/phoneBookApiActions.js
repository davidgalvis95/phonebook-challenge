const sendRequest = () => {
    return {
        type: "SEND_REQUEST"
    }
}

const processResponse = (data, query) => {
    return {
        type: "PROCESS_RESPONSE",
        responseData: data,
        query: query
    }
}

const stopLoader = () => {
    return {
        type: "STOP_LOADER"
    }
}

const handleError = (errorMessage) => {
    return {
        type: "HANDLE_ERROR",
        errorMessage: errorMessage
    }
}

const clearError = () => {
    return {
        type: "CLEAR_ERROR"
    }
}

export default {
    sendRequest,
    processResponse,
    stopLoader,
    handleError,
    clearError,
}
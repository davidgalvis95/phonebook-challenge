export const defaultState = {
  loading: false,
  error: null,
  data: {
    getContactsData: null,
    createContactData: null,
    updateContactData: null,
    deleteContactData: null,
  },
};

const processResponseHandler = (state, data, query) => {
  switch (query) {
    case "GET":
      return {
        ...state,
        data: { ...state.data, getContactsData: data },
      };
    case "CREATE":
      return {
        ...state,
        data: { ...state.data, createContactData: data },
      };
    case "UPDATE":
      return {
        ...state,
        data: { ...state.data, updateContactData: data },
      };
    case "DELETE":
      return {
        ...state,
        data: { ...state.data, deleteContactData: data },
      };
    default:
      return state;
  }
};

const phoneBookApiReducer = (state = defaultState, action) => {
  switch (action.type) {
    case "SEND_REQUEST":
      return { ...state, loading: true };
    case "PROCESS_RESPONSE":
      return processResponseHandler(state, action.responseData, action.query);
    case "STOP_LOADER":
      return { ...state, loading: false };
    case "HANDLE_ERROR":
      return {
        ...state,
        loading: false,
        error: action.errorMessage,
        //   forecastData: {},
      };
    case "CLEAR-ERROR":
      return {
        ...state,
        loading: true,
        error: null,
      };
    default:
      return state;
  }
};

export default phoneBookApiReducer;

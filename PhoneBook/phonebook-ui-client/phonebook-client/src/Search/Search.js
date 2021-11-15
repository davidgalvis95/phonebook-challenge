import React, { useRef, useEffect, useState, useHistory } from "react";
import { useLocation } from "react-router-dom";
import "./Search.css";
import usePhoneBookApi from "../Hooks/usePhoneBookApi";

const Search = React.memo(() => {
  const [enteredFilter, setEnteredFilter] = useState("");
  const history = useHistory();
  const currentRoute = useLocation();
  const inputSearchRef = useRef();
  const { sendRequestToGetMatchingContacts } = usePhoneBookApi();

  useEffect(() => {
    const timer = setTimeout(() => {
      if ((enteredFilter = "")) {
        sendRequestToGetMatchingContacts(transformSearchParams(enteredFilter));
        if (currentRoute != "/contacts") {
          history.push("/contacts");
        }
      }

      if (
        enteredFilter != "" &&
        enteredFilter === inputSearchRef.current.value
      ) {
        const pattern = new RegExp(/^[A-Za-z0-9 _]*[A-Za-z0-9][A-Za-z0-9 _]*$/);
        if (enteredFilter.match(pattern)) {
          sendRequestToGetMatchingContacts(
            transformSearchParams(enteredFilter)
          );
          if (currentRoute != "/contacts") {
            history.push("/contacts");
          }
        } else {
          alert(
            "Please enter a valid search (letters and numbers separated by spaces)"
          );
        }
      }
      return () => {
        clearTimeout(timer);
      };
    }, 100);
  }, [enteredFilter, inputSearchRef]);

  const transformSearchParams = (paramsString) => {
    return paramsString.replace(/\s+/, ",");
  };

  return (
    <section className="search">
      <div className="search-input">
        <div>
          <label>Search contact</label>
          <input
            ref={inputSearchRef}
            type="text"
            value={enteredFilter}
            onChange={(event) => setEnteredFilter(event.target.value)}
          />
        </div>
      </div>
    </section>
  );
});

export default Search;

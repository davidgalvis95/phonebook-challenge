import React, { useRef, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import classes from "./Search.module.css";
import usePhoneBookApi from "../Hooks/usePhoneBookApi";

const Search = React.memo(() => {
  const [enteredFilter, setEnteredFilter] = useState("");
  const navigate = useNavigate();
  const currentRoute = useLocation();
  const inputSearchRef = useRef();
  const { sendRequestToGetMatchingContacts } = usePhoneBookApi();

  useEffect(() => {
    const timer = setTimeout(() => {
      if ((enteredFilter === "")) {
        sendRequestToGetMatchingContacts(transformSearchParams(enteredFilter));
        if (currentRoute !== "/contacts") {
          navigate("/contacts");
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
          if (currentRoute !== "/contacts") {
            navigate("/contacts");
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
    <div className={classes.search}>
      <div className={classes.searchInput}>
        <div>
          <input
            placeholder="Search contact"
            ref={inputSearchRef}
            type="text"
            value={enteredFilter}
            onChange={(event) => setEnteredFilter(event.target.value)}
          />
        </div>
      </div>
    </div>
  );
});

export default Search;

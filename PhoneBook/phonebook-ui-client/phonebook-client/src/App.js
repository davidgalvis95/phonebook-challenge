import './App.css';
import {
  BrowserRouter as Router,
  Routes,
  Route
} from "react-router-dom";
import Layout from './Layout/Layout';
import ContactCreateOrUpdateComp from "./ContactInfo/ContactCreateOrUpdateComp";
import ContactsContainer from "./Container/ContactsContainer";

function App() {

  const routes = (
    <Routes>
      <Route path="/contacts" element={<ContactsContainer/>} />
      <Route
        path="/contact/creation"
        element={<ContactCreateOrUpdateComp
          name1={""} name2={""} phoneX={""} operation={"CREATE"} id={""} handlePostCreateOrUpdate={() => {}}
        />}
      />
      {/* <Route path="/" component={GreetingComponent}/> */}
    </Routes>);

  return (
      <Layout>{routes}</Layout>
  );
}

export default App;

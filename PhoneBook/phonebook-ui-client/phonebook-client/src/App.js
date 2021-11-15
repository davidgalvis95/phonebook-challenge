import logo from './logo.svg';
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
          name1={""} name2={""} phoneX={""} operation={"CREATE"}
        />}
      />
      {/* <Route path="/" component={GreetingComponent}/> */}
    </Routes>);

  return (
    <Router>
      <Layout>{routes}</Layout>
    </Router>
  );
}

export default App;

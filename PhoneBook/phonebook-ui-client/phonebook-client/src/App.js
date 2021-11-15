import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import Layout from './Layout/Layout';
import ContactCreateOrUpdateComp from "./ContactInfo/ContactCreateOrUpdateComp";
import ContactsContainer from "./Container/ContactsContainer";

function App() {

  const routes = (
    <Switch>
      <Route path="/contacts" component={<ContactsContainer/>} />
      <Route
        path="/contact/creation"
        element={<ContactCreateOrUpdateComp
          name1={""} name2={""} phoneX={""} operation={"CREATE"}
        />}
      />
      {/* <Route path="/" component={GreetingComponent}/> */}
    </Switch>);

  return (
    <Router>
      <Layout>{routes}</Layout>
    </Router>
  );
}

export default App;

import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import Layout from './Layout/Layout';

function App() {

  const routes = (
    <Switch>
      <Route path="/contacts" component={Container} />
      <Route
        path="/contact/operation-request"
        component={CreateOrUpdateContactModal}
      />
      <Route path="/" component={GreetingComponent}/>
    </Switch>);

  return (
    <Router>
      <Layout>{routes}</Layout>
    </Router>
  );
}

export default App;

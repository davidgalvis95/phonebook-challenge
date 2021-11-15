import Navbar from "../Navbar/Navbar"


//TODO make some fancy layout
const Layout = (props) => {
  
  return (
    <div>
      <Navbar />
      {props.children}
    </div>
  );
};

export default Layout;
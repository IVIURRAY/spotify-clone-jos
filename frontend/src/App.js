import "./App.css";
import { Playlists, Links, Home, Browse, Radio, UserBubble } from "./components";
import { Route, Switch } from "react-router-dom";

function App() {
  return (
    <div className="App">
      <section className="left-panel">
        <Links />
        <Playlists />
      </section>
      <section className="middle-panel">
        <section className="header-panel">
          <div className="header-item">
            <h1>Home</h1>
          </div>
          <div className="header-item-2">
            <UserBubble />
          </div>
        </section>
        <section className="body-panel">
          <Switch>
            <Route exact path="/" component={Home} />
            <Route path="/browse" component={Browse} />
            <Route path="/radio" component={Radio} />
          </Switch>
        </section>
      </section>
      <section className="right-panel" />
    </div>
  );
}

export default App;

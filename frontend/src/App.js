import './App.css';
import { Example, Playlists } from './components';

function App() {
  return (
    <div className="App">
      <section className="left-panel">
        <Playlists />
      </section>
      <section className="middle-panel">      
        <h1>
          Spotify Clone!
        </h1>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <Example />
      </section>
      <section className="right-panel"/>
    </div>
  );
}

export default App;

import './App.css';
import { Example } from './components';

function App() {
  return (
    <div className="App">
      <header className="App-header">
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
      </header>
    </div>
  );
}

export default App;

import React, { useState, useEffect } from "react";
import './Playlists.css'

const Playlists = () => {
  const [playlists, setPlaylists] = useState({});
  const [isLoading, setLoading] = useState(true);

  async function fetchData() {
    const res = await fetch("user/playlist");
    res.json()
      .then(res => {
        setPlaylists(res.items.map(item => {
            return {
              name: item.name, 
              href: item.externalUrls.externalUrls.spotify
            }
          }, []))
        setLoading(false)
      })
      .catch(error => console.log(error));
  }

  useEffect(() => {
    fetchData();
  }, [setPlaylists, setLoading]);

  return (
    <div className="playlists-panel">
        {(isLoading ? <p>Getting playlists...</p> : 
          <div>
            <h3 className="playlists-title">Playlists</h3>
            {playlists.map(playlist => 
              <div className="playlists-item">
                <a href={playlist.href} className="playlists-link">{playlist.name}</a>
              </div>
            )}
          </div>
        )}
    </div>
  );
};
export default Playlists;
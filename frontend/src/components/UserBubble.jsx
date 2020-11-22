import React, { useState, useEffect } from "react";
import "./UserBubble.css";

const UserBubble = () => {
  const [user, setUser] = useState({});
  const [isLoading, setLoading] = useState(true);

  async function fetchData() {
    const res = await fetch("example");
    res
      .json()
      .then((json) => {
        setUser(json);
        setLoading(false);
        console.log(json);
      })
      .catch((error) => console.log(error));
  }

  useEffect(() => {
    fetchData();
  }, [setUser, setLoading]);

  return (
    <div>
      {isLoading ? (
        <p>Getting user...</p>
      ) : (
        <div className="user-bubble">
          <p>
            <img className="profile-icon" src={user.images[0].url} width="30" alt="profile" />
            <span className="user-name">{user.displayName}</span>
          </p>
        </div>
      )}
    </div>
  );
};
export default UserBubble;

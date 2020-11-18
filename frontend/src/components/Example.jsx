
import React, { useState, useEffect } from "react";

const Example = () => {
  const [user, setUser] = useState({});
  const [isLoading, setLoading] = useState(true);

  async function fetchData() {
    const res = await fetch("example");
    res
      .json()
      .then(json => {
          setUser(json)
          setLoading(false)
      })
      .catch(error => console.log(error));
  }

  useEffect(() => {
    fetchData();
  }, [setUser, setLoading]);

  return (
    <div>
        {(isLoading ? <p>Getting user...</p> : 
            <div>
                <h3>Hello, {user.displayName}!</h3>
            </div>
        )}
    </div>
  );
};
export default Example;
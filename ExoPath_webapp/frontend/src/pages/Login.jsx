import { useState } from "react";
import { useAuth } from "../services/AuthContext";
import { login as loginService} from "../services/AuthService";
import { useNavigate } from "react-router-dom";
import Header from '../components/Header';
import "../stylesheets/Login.css"
import PasswordChangeModal from "../components/PasswordChangeModal";

function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [userAuthData, setUserAuthData] = useState(null);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const userData = await loginService(username, password);
      
      setUserAuthData({
        userId: userData.userId,
        username: userData.username,
        role: userData.role
      });
      
      if(userData.needsPasswordChange)
      {
        setShowPasswordModal(true);
      }
      else{
      login(userData.username, userData.role);
      navigate("/home", { state: { userId: userData.userId, username: userData.username, role: userData.role } });
      }
    } catch (error) {
      setError(error.message);
    }
  };

  const handlePasswordChangeSuccess = () => {
    login(userAuthData.username, userAuthData.role);
      navigate("/home", { state: { userId: userAuthData.userId, username: userAuthData.username, role: userAuthData.role } });
  }

  return (
    <div>
      <Header />
      <div id="login_div">
        <h2>Login</h2>
        {error && <p style={{ color: "red" }}>{error}</p>}
        <form className="login_form" onSubmit={handleLogin}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button className="form_button" type="submit">Login</button>
        </form>
      </div>

      {/* Password Change Modal */}
      {userAuthData && (
        <PasswordChangeModal 
          show={showPasswordModal}
          onClose={() => {
            setShowPasswordModal(false);
          }}
          onSuccess={handlePasswordChangeSuccess}
          username={userAuthData.username}
        />
      )}

    </div>
  );
}

export default Login;



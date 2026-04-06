import { useState } from 'react';
import '../stylesheets/Header.css';
import logo from '../assets/logo.png';
import { useAuth } from "../services/AuthContext";
import PasswordChangeModal from './PasswordChangeModal';



function Header({username}) {
  const { logout } = useAuth();
  const [showDropdown, setShowDropdown] = useState(false);
  const [showPasswordModal, setShowPasswordModal] = useState(false);

  const toggleDropdown = () => {
    setShowDropdown(!showDropdown);
  };

  const handleLogout = () => {
    logout();
    setShowDropdown(false);
  };

  const handlePasswordChange = () => {
    setShowPasswordModal(true);
    setShowDropdown(false);
  };

  const handlePasswordChangeSuccess = () => {
    setShowPasswordModal(false);
  };

  return (
    <header className="header">
      <div>
        <img src={logo} style={{ height: '50px' }} alt="ExoPathLogo" />
        <h2 id="app_name">ExoPath</h2>
      </div>

      {username && (
        <div className="header-right">
          <div className="user-menu-container">
            <div className="user-menu-trigger" onClick={toggleDropdown}>
              <span id="account_icon" className="material-symbols-outlined">account_circle</span>
              <span className="username">{username}</span>
              <span id="dropdown-icon" className="material-symbols-outlined dropdown-icon">
                {showDropdown ? 'arrow_drop_up' : 'arrow_drop_down'}
              </span>
            </div>

            {showDropdown && (
              <div className="user-menu-dropdown">
                <button className="dropdown-item" onClick={handlePasswordChange}>
                  Change Password
                </button>
                <button className="dropdown-item" onClick={handleLogout}>
                  Logout
                </button>
              </div>
            )}
          </div>

          <PasswordChangeModal
            show={showPasswordModal}
            onClose={() => setShowPasswordModal(false)}
            onSuccess={handlePasswordChangeSuccess}
            username={username}
            type={2}
          />
        </div>
      )}
    </header>
  );
};

export default Header;

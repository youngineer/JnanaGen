import ThemeController from './ThemeController'
import { useNavigate } from 'react-router'

const NavbarTop = () => {
    const navigate = useNavigate();
    
    const handleLogout = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');
        navigate("/auth");
    }
    return (
        <div>
            <div className="navbar bg-neutral text-neutral-content shadow-sm py-1">
                <div className="flex-1">
                    <h3 className="btn btn-ghost text-xl">notes to quiz</h3>
                </div>
                <div className="flex-none">
                    <ThemeController />
                </div>
                <div>
                    <button className="btn mx-4 ml-6" onClick={handleLogout}>Logout</button>
                </div>
            </div>
        </div>
    )
}

export default NavbarTop
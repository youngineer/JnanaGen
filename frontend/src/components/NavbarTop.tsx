import { useEffect } from 'react';
import ThemeController from './ThemeController'
import { useNavigate } from 'react-router'
import { Link } from 'react-router-dom';

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
            <div className="navbar bg-neutral text-neutral-content shadow-sm py-1 px-4">
                <div className="flex w-full items-center justify-between">
                    {/* Left side */}
                    <div className="flex items-center gap-4">
                        <h3 className="text-xl">📖 notes to quiz</h3>
                        <Link to="/home" className="flex items-center gap-1 btn btn-ghost">
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                className="h-5 w-5"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth="2"
                                    d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
                                />
                            </svg>
                            <span>Dashboard</span>
                        </Link>
                    </div>
                    {/* Right side */}
                    <div className="flex items-center gap-4 ml-auto">
                        <ThemeController />
                        <button className="btn" onClick={handleLogout}>Logout</button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default NavbarTop
import { useEffect, useState } from 'react';
import ThemeController from './ThemeController'
import { useNavigate } from 'react-router'
import { Link } from 'react-router-dom';
import { checkAuthenticated, logout } from '../services/authServices';
import type { AlertState } from '../utils/interfaces';
import AlertMessage from './AlertMessage';

const NavbarTop = () => {
    const navigate = useNavigate();
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(true);
    const [alert, setAlert] = useState<AlertState>({
        show: false,
        isSuccess: false,
        message: ''
    });

    const handleLogout = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        try {
            const message: string = await logout();
            setAlert({
                show: true,
                isSuccess: true,
                message: message || "Logged out successfully."
            });
            setIsAuthenticated(false);
            setTimeout(() => {
                setAlert({ show: false, isSuccess: false, message: '' });
                navigate("/auth");
            }, 1500);
        } catch (error) {
            setAlert({
                show: true,
                isSuccess: false,
                message: "Logout failed. Please try again."
            });
        }
    };

    useEffect(() => {
        const checkAuth = async (): Promise<void> => {
            try {
                const response: boolean = await checkAuthenticated();
                setIsAuthenticated(response);
            } catch (error) {
                setIsAuthenticated(false);
                navigate("/auth");
            }
        };
        checkAuth();
    }, [navigate]);

    setTimeout(() => {
      setAlert(prev => ({
        ...prev,
        show: false
      }))
    }, 3000);

    return (
        <div>
            <div className="navbar bg-neutral text-neutral-content shadow-sm py-1 px-4">
                <div className="flex w-full items-center justify-between">
                    <div className="flex items-center gap-4">
                        <h3 className="text-xl">📖 notes to quiz</h3>
                        {
                            isAuthenticated && (
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
                            )
                        }
                    </div>
                    <div className="flex items-center bg-neutral text-neutral-content gap-4 ml-auto">
                        <ThemeController />
                        {isAuthenticated && (
                            <button className="btn" onClick={handleLogout}>Logout</button>
                        )}
                    </div>
                </div>
            </div>
            {alert.show && (
                <AlertMessage isSuccess={alert.isSuccess} message={alert.message} />
            )}
        </div>
    )
}

export default NavbarTop
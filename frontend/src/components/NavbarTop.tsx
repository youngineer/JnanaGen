import { useEffect, useState, type FC, type MouseEvent } from 'react'
import ThemeController from './ThemeController'
import { Link, useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router';

const NavbarTop: FC = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [isTokenPresent, setIsTokenPresent] = useState<boolean>(false);

    const handleLogout = (e: MouseEvent<HTMLButtonElement>): void => {
        localStorage.removeItem("token");
        navigate("/auth");
    }

    useEffect(() => {
    const token = localStorage.getItem("token");
    if(token) setIsTokenPresent(true);
    if (!token && location.pathname !== "/auth") {
        navigate("/auth");
    }
    }, [navigate, location.pathname]);

    const showNavLinks = isTokenPresent && location.pathname !== "/auth";

    return (
        <nav className="navbar bg-neutral shadow-sm px-4">
            <div className="flex-1 flex items-center gap-4">
                <a className="btn-ghost text-xl text-neutral-content font-bold">
                    notes to quiz
                </a>
                {showNavLinks && (<ul className="menu menu-horizontal bg-neutral text-base-100 rounded-box hidden md:flex">
                    <li>
                        <Link to={"/home"} className="flex items-center gap-2">
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                className="h-5 w-5"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth="2"
                                    d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
                                />
                            </svg>
                            Dashboard
                        </Link>
                    </li>
                </ul>)}
            </div>
            <div className="flex items-center gap-4">
                <ThemeController />
                {showNavLinks && (
                    <button className="btn flex items-center btn-warning" onClick={handleLogout}>
                    Logout
                </button>
            )
            }
            </div>
        </nav>
    )
}

export default NavbarTop
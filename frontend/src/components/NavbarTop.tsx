import Sidebar from './Sidebar'
import ThemeController from './ThemeController'
import { useState } from 'react'

const NavbarTop = () => {
    const [showSidebar, setShowSidebar] = useState<boolean>(false);

    const toggleSidebar = () => {
        setShowSidebar(!showSidebar);
    }

    return (
        <div>
            <div className="navbar shadow-sm bg-neutral">
                <div className="flex-none">
                    <button className="btn btn-square btn-ghost" onClick={toggleSidebar}>
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block h-5 w-5 stroke-current">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16"></path>
                        </svg>
                    </button>
                    
                    {showSidebar && <Sidebar />}
                </div>
                <div className="flex-1">
                    <a className="btn btn-ghost text-xl">Welcome, Kartik</a>
                </div>
                <div className="flex-none">
                    <ThemeController />
                </div>
            </div>
        </div>
    )
}

export default NavbarTop
import type { FC } from 'react'
import ThemeController from './ThemeController'

const NavbarTop: FC = () => {

    return (
        <div className='min-w-full max-h-10'>
            <div className="navbar shadow-sm bg-neutral">
                <div className="flex-1">
                    <a className="btn btn-ghost btn-neutral text-xl text-neutral-content">Welcome, Kartik</a>
                </div>
                <div className="flex-none">
                    <ThemeController />
                </div>
            </div>
        </div>
    )
}

export default NavbarTop
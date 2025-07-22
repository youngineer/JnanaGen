import type { FC, JSX } from 'react'
import Footer from './Footer'
import NavbarTop from './NavbarTop'
import { Outlet } from 'react-router'

const Body: FC = (): JSX.Element => {
  return (
    <div>
      {/* <NavbarTop /> */}
        <Outlet />
      {/* <Footer /> */}
    </div>
  )
}

export default Body
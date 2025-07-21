import type { FC, JSX } from 'react'
import Dashboard from './Dashboard'
import Footer from './Footer'
import NavbarTop from './NavbarTop'
import QuizPage from './QuizPage'
import AuthPage from './AuthPage'
import { Outlet } from 'react-router'

const Body: FC = (): JSX.Element => {
  return (
    <div>
      <NavbarTop />
        <Outlet />
      <Footer />
    </div>
  )
}

export default Body
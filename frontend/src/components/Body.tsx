import type { FC, JSX } from 'react'
import Dashboard from './Dashboard'
import Footer from './Footer'
import NavbarTop from './NavbarTop'
import QuizPage from './QuizPage'
import AuthPage from './AuthPage'

const Body: FC = (): JSX.Element => {
  return (
    <div>
      <NavbarTop />
      <AuthPage />
      <Footer />
    </div>
  )
}

export default Body
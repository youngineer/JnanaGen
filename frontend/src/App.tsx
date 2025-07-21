import type { JSX } from 'react'
import Body from './components/Body'
import AuthPage from './components/AuthPage'
import QuizPage from './components/QuizPage'
import Dashboard from './components/Dashboard'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import NavbarTop from './components/NavbarTop'
import Footer from './components/Footer'
import QuizSpecification from './components/QuizSpecification'

function App(): JSX.Element {
  return (
    <>
      <BrowserRouter basename='/'>
        <Routes>
          <Route path='/' element={<Body />}>
            <Route path='/auth' element={<AuthPage />}></Route>
            <Route path='/quiz' element={<QuizPage />}></Route>
            <Route path='/home' element={<QuizSpecification />}></Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
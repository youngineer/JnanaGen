import type { JSX } from 'react'
import Body from './components/Body'
import AuthPage from './components/AuthPage'
import QuizPage from './components/QuizPage'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import QuizInfo from './components/QuizInfo'
import Dashboard from './components/Dashboard'


function App(): JSX.Element {
  return (
    <>
      <BrowserRouter basename='/'>
        <Routes>
          <Route path='/' element={<Body />}>
            <Route path='/auth' element={<AuthPage />}></Route>
            <Route path='/quiz/:quizId' element={<QuizPage />}></Route>
            <Route path='/home' element={<Dashboard />}></Route>
            <Route path='/quizInfo/:id' element={<QuizInfo />}></Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
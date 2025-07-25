import type { JSX } from 'react'
import Body from './components/Body'
import AuthPage from './components/AuthPage'
import QuizPage from './components/QuizPage'
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom'
import QuizInfo from './components/QuizInfo'
import Dashboard from './components/Dashboard'


function App(): JSX.Element {
  return (
    <BrowserRouter basename='/'>
      <Routes>
        <Route path='/' element={<Body />}>
          <Route index element={<Navigate to="/auth" replace />} />
          <Route path='/auth' element={<AuthPage />} />
          <Route path='/quiz/:quizId' element={<QuizPage />} />
          <Route path='/home' element={<Dashboard />} />
          <Route path='/quizInfo/:id' element={<QuizInfo />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
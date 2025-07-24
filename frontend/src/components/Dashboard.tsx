import { type FC } from 'react'
import Sidebar from './Sidebar'
import QuizParams from './QuizParams'


const Dashboard: FC = () => {

  return (
    <div className="flex">
      <Sidebar />
      <QuizParams />
    </div>
  )
}

export default Dashboard
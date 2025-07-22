import { type FC } from 'react'
import QuizSpecification from './QuizSpecification'
import Sidebar from './Sidebar'


const Dashboard: FC = () => {

  return (
    <div className="flex">
      <Sidebar />
      <QuizSpecification />
    </div>
  )
}

export default Dashboard
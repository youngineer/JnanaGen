import { type FC, type JSX } from 'react'
import QuizSpecification from './QuizSpecification';

const Dashboard: FC = (): JSX.Element => {
  return (
    <div className='flex'>
        <QuizSpecification />
    </div>
  )
}

export default Dashboard;
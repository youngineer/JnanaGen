import { type FC, type JSX } from 'react'
import Sidebar from './Sidebar';

const Dashboard: FC = (): JSX.Element => {
  return (
    <div>
        <Sidebar />
    </div>
  )
}

export default Dashboard;
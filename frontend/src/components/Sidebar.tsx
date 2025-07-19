import { type FC, type JSX } from 'react'

const Sidebar: FC = (): JSX.Element => {
  return (
    <div className='max-w-1/7'>
        <div className='p-3 font-bold'>
            <h4>Welcome, Kartik</h4>
            <hr />
        </div>
    </div>
  )
}

export default Sidebar;
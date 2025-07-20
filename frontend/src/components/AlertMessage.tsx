


interface AlertMessageInterface {
    isSuccess: boolean,
    message: string
}

const AlertMessage = ({isSuccess, message}: AlertMessageInterface) => {
  return (
    <div>
        {isSuccess && (
            <div className="toast toast-top toast-center">
                <div className="alert alert-success">
                    <span>{message}</span>
                </div>
            </div>
        )}

        {!isSuccess && (
            <div className="toast toast-top toast-center">
                <div className="alert alert-error">
                    <span>{message}</span>
                </div>
            </div>
        )}
    </div>
  )
}

export default AlertMessage;
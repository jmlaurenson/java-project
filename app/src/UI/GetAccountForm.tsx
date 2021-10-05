import React, { useState }  from 'react';

import Button from '@material-ui/core/Button';

import TextField from '@material-ui/core/TextField';

/**
 * Returns a form where the user can enter in an account to look at the orders placed by that account
 */
export function GetAccountForm():React.ReactElement {
    //Declare state variables for the input boxes
    const [accountValue, setAccount] = useState(0); 
      
      const onAccountChange = (e: React.ChangeEvent<HTMLInputElement>) => setAccount(parseInt(e.target.value));

      return(
        <React.StrictMode>
          <form  noValidate autoComplete="off">
            <div>
                <TextField
            id="accountValue"
            label="Account"
            type="number"
            required
            onChange={onAccountChange}
            value={accountValue}
            InputLabelProps={{
              shrink: true,
            }}
          />
            
            </div>
          
          <Button variant="contained" /*onClick={() => dispatch(fetchAccount(accountValue))}*/>Get current orders</Button>
          </form>
        </React.StrictMode>
      )

}
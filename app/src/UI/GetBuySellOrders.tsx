import React from 'react';

import Button from '@material-ui/core/Button';


export function GetBuySellOrders():React.ReactElement {
    return (
      <React.StrictMode>
        <div>
          <div>
            <Button variant="contained" /*onClick={() => dispatch(fetchBuyOrders())}*/>Get Buy Orders</Button>
          </div>
          <div>
            <Button variant="contained" /*onClick={() => dispatch(fetchSellOrders())}*/>Get Sell Orders</Button>
          </div>
        </div>
      </React.StrictMode>
    );
  }
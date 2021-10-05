import React, { useState }  from 'react';

import Button from '@material-ui/core/Button';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import ClickAwayListener from '@material-ui/core/ClickAwayListener';
import Grow from '@material-ui/core/Grow';
import Paper from '@material-ui/core/Paper';
import Popper from '@material-ui/core/Popper';
import MenuItem from '@material-ui/core/MenuItem';
import MenuList from '@material-ui/core/MenuList';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';

import TextField from '@material-ui/core/TextField';



const options = ['Sell', 'Buy'];

export function NewOrderForm():React.ReactElement {
    const [open, setOpen] = React.useState(false);
    const anchorRef = React.useRef<HTMLDivElement>(null);
    const [actionValue, setActionValue] = React.useState(1);

    //Declare state variables for the input boxes
    const [accountValue, setAccount] = useState(0); 
    const [priceValue, setPrice] = useState("0"); 
    const [quantityValue, setQuantity] = useState(0); 

    const handleToggle = () => {
        setOpen((prevOpen) => !prevOpen);
      };
    
      const handleMenuItemClick = (
        event: React.MouseEvent<HTMLLIElement, MouseEvent>,
        index: number,
      ) => {
        setActionValue(index);
        setOpen(false);
      };
    
      const handleClose = (event: React.MouseEvent<Document, MouseEvent>) => {
        if (anchorRef.current && anchorRef.current.contains(event.target as HTMLElement)) {
          return;
        }
    
        setOpen(false);
      };

      
      const onAccountChange = (e: React.ChangeEvent<HTMLInputElement>) => setAccount(parseInt(e.target.value));
      const onPriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {setPrice(e.target.value)}
      const onQuantityChange = (e: React.ChangeEvent<HTMLInputElement>) => setQuantity(parseInt(e.target.value));
      const onAddOrder = (price:string) => {
        console.log(typeof parseFloat(parseFloat(price).toFixed(2)))
        if(isNaN(parseFloat(parseFloat(price).toFixed(2)))){
          alert("Invalid price")
        }
        else{
          //onDispatchOrder(parseFloat(parseFloat(price).toFixed(2)))
        }
      }
      //const onDispatchOrder = (price:number) => {dispatch(addOrder({account:accountValue, price:price,quantity:quantityValue,action:actionValue}))
      /*if(actionValue===1){
        dispatch(fetchBuyOrders())}
      else{
        dispatch(fetchSellOrders())
      }*/
    //};
      

      //Creates a form with three text box and a dropdown to select buy or sell orders
      return(
        <form  noValidate autoComplete="off">
          <div>
          <React.StrictMode>
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
              <TextField
          id="priceValue"
          label="Price (£)"
          required
          onChange={onPriceChange}
          value={priceValue}
          InputLabelProps={{
            shrink: true,
          }}
        />
              <TextField
          id="quantityValue"
          label="Quantity"
          type="number"
          required
          onChange={onQuantityChange}
          value={quantityValue}
          InputLabelProps={{
            shrink: true,
          }}
        />
          
            <ButtonGroup variant="contained" color="default" ref={anchorRef} aria-label="split button">
            <Button>{options[actionValue]}</Button>
              <Button
                color="default"
                size="small"
                aria-controls={open ? 'split-button-menu' : undefined}
                aria-expanded={open ? 'true' : undefined}
                aria-label="select merge strategy"
                aria-haspopup="menu"
                onClick={handleToggle}
              >
                <ArrowDropDownIcon />
              </Button>
            </ButtonGroup>
          </React.StrictMode>
          <Popper open={open} anchorEl={anchorRef.current} role={undefined} transition disablePortal>
            {({ TransitionProps, placement }) => (
              <Grow
                {...TransitionProps}
                style={{
                  transformOrigin: placement === 'bottom' ? 'center top' : 'center bottom',
                }}
              >
                <Paper>
                  <ClickAwayListener onClickAway={handleClose}>
                    <MenuList id="split-button-menu">
                      {options.map((option, index) => (
                        <MenuItem
                          key={option}
                          disabled={index === 2}
                          selected={index === actionValue}
                          onClick={(event) => handleMenuItemClick(event, index)}
                        >
                          {option}
                        </MenuItem>
                      ))}
                    </MenuList>
                  </ClickAwayListener>
                </Paper>
              </Grow>
            )}
          </Popper>
          <React.StrictMode> 
            <Button variant="contained" onClick={() => onAddOrder(priceValue)}>Add Order</Button>
          </React.StrictMode>
        </div>
        </form>
      )
}
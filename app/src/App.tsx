import React from 'react';
import './App.css';
import { GetBuySellOrders } from './UI/GetBuySellOrders';
import { GetAccountForm } from './UI/GetAccountForm';
import { NewOrderForm } from './UI/newOrderForm';

import { makeStyles, createStyles, Theme } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      flexGrow: 1,
    },
    paper: {
      height:'80%',
      minHeight: '90%',
      padding: theme.spacing(2),
      textAlign: 'center',
      color: theme.palette.text.secondary,
    },
    input: {
      height:'80%',
      padding: theme.spacing(2),
      textAlign: 'center',
      color: theme.palette.text.secondary,
    },
  }),
);


function App(): React.ReactElement | null {
  const classes = useStyles();

  return (
    <div className="App">
      <header className="App-header">
        
      </header>
      <div>
          <Grid container spacing={3}>
            <Grid item xs={4}>
              <Paper className={classes.input}><GetAccountForm/></Paper>
            </Grid>
            <Grid item xs={4}>
              <Paper className={classes.input}><NewOrderForm/></Paper>
            </Grid>
            <Grid item xs={4}>
              <Paper className={classes.input}><GetBuySellOrders/></Paper>
            </Grid>
          </Grid>
          <Grid container spacing={3}>
            <Grid item xs={4}>
              <Paper className={classes.paper}></Paper>
            </Grid>
            <Grid item xs={8}>
              <Paper className={classes.paper}></Paper>
            </Grid>
          </Grid>
        </div>
    </div>
  );
}

export default App;

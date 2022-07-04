import React, {useState} from 'react';
import './App.css';
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import SG_logo from './assets/SG_logo.png';


function App() {

  <style>
    @import url('https://fonts.googleapis.com/css2?family=Merriweather:wght@300&display=swap');
  </style>

  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState(new Date());
  const [searchValue, setSearchValue] = useState("");

  function allCompanies() {
    fetch(`https://localhost:8080/register`)
    .then((response) => response.json())
    .then((actualData) => console.log(actualData))
  }

  function search(searchValue) {
    fetch('https://localhost:8080/companies/`searchValue`')
    .then((response) => response.json())
    .then((actualData) => console.log(actualData))
  }

  function getCompany() {
    fetch('https://localhost:8080/companies/`searchValue`/`startDate`/`endDate`/')
    .then((response) => response.json())
    .then((actualData) => console.log(actualData))
  }

  return (
    <div className="App">
      <div className="strip">
        <img className='SGlogo' src={SG_logo} alt='SG_logo' />
        <button>Add</button>
        <button onClick={allCompanies()} >List all Companies</button>

        <div className='searchBar'>
          <input type='text' value={searchValue}></input>
          <button onClick={search(searchValue)}>Search</button>
        </div>
      </div>

      <div className='screen'>
        <br/>
        <label>Company Code</label>
        <input type='text' placeholder='XYZ'></input>
        <br/> <br/>
        <label>Company Name</label>
        <input type='text' placeholder='ABC'></input>
      </div>

      <div>
        <span>from</span>
        <span><DatePicker selected={startDate} onChange={(date) => setStartDate(date)} /></span>
        <span>to</span>
        <DatePicker onClick={getCompany()} selected={startDate} onChange={(date) => setEndDate(date)} />
      </div>

      <div className='sdt'>
        <button>Stock Price</button>
        <button>Date</button>
        <button>Time</button>
      </div>

      <div className='data'>
        {/* data */}
      </div>

      <div className='stats'>
        <label>Min: </label> <input type="text" placeholder='Min' value="" /> <br/> <br/>
        <label>Max: </label> <input type="text" placeholder='Max' value="" /> <br/> <br/>
        <label>AVG: </label> <input type="text" placeholder='Avg' value="" />
      </div>

    </div>
  );
}

export default App;

import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Main from './pages/main/Main';
import Header from './components/header/Header';
import TripSchedule1 from './pages/tripschedule/tripschedule-details/tipschedule1/TripSchedule1';
import TripSchedule2 from './pages/tripschedule/tripschedule2/TripSchedule2';

const App = () => {
    return (
        <BrowserRouter>
            <Header />
            <Routes>
                <Route path="/" element={<Main />} />
                <Route path="/tripschedule1" element={<TripSchedule1 />} />
                <Route path="/tripschedule2" element={<TripSchedule2 />} />
            </Routes>
        </BrowserRouter>
    );
};

export default App;

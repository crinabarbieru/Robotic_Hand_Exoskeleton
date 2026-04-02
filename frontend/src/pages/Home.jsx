import { useState } from "react";
import { useLocation } from "react-router-dom";

import "../stylesheets/Home.css"
import Header from '../components/Header';
import About from "../components/About";
import Help from "../components/Help";
import SideBar from '../components/SideBar';
import UserInfo from "../components/UserInfo";
import AddPatientForm from "../components/AddPatientForm";
import { addPatient } from "../services/AddPatient";
import ViewPatients from "../components/ViewPatients";
import ProgressTracker from "../components/ProgressTracker";
import ViewPatientsProgress from "../components/ViewPatientsProgress";
import SupervisorDashboard from "../components/SupervisorDashboard";
import ExerciseSession from "../components/ExerciseSession";
import PassiveMode from "../components/PassiveMode";


function Home() {
    const location = useLocation();
    const userId = location.state?.userId;
    const username = location.state?.username;
    const role = location.state?.role;

    const [activeSection, setActiveSection] = useState("default");

    function handleAddPatientSubmit(formData) {
        const fullData = {
            ...formData,
            supervisorId: userId,
        };

        addPatient(fullData)
            .then((response) => {
                alert(response.message || "Patient added successfully!");
            })
            .catch((err) => {
                console.error("Add patient error:", err);
                alert(err.message || "Failed to add patient");
            });
    }

    const renderSection = () => {
        switch (activeSection) {
            case "about":
                return <About></About>;
            case "help":
                return <Help></Help>;
            case "my_info":
                return <UserInfo userId={userId}></UserInfo>;
            case "exercise":
                return <ExerciseSession userId={userId}></ExerciseSession>;
            case "passive_mode":
                return <PassiveMode userId={Number(userId)} ></PassiveMode>
            case "my_progress":
                return <ProgressTracker userId={userId}></ProgressTracker>
            case "add_patient":
                return <AddPatientForm onSubmit={handleAddPatientSubmit}></AddPatientForm>;
            case "view_patients":
                return <ViewPatients userId={userId}></ViewPatients>;
            case "view_progress":
                return <ViewPatientsProgress userId={userId}></ViewPatientsProgress>
            case "supervisor_dashboard":
                return <SupervisorDashboard userId={userId}></SupervisorDashboard>    
            default:
                {
                    if(role == 2)
                        return <SupervisorDashboard userId={userId}></SupervisorDashboard> 
                    else
                    return <UserInfo userId={userId}></UserInfo>;
                }
        }
    };

    return (
        <div id="home_div">
            <Header username={username} />
            <div className="home_container">
                <SideBar role={role} onSectionChange={setActiveSection} />
                <div className="info_page">
                    {renderSection()}
                </div>
            </div>
        </div>
    )
}
export default Home;
import '../stylesheets/SideBar.css';
import MenuButton from './MenuBotton';

function SideBar({ role, onSectionChange }) {
    return (

        <div className="side_bar">
            <div className="side_menu">
                {role == 1 && (
                    <>
                        <MenuButton text="My Information" onClick={() => onSectionChange("my_info")} />
                        <MenuButton text="My Progress" onClick={() => onSectionChange("my_progress")} />
                        <MenuButton text="Perform Exercises" onClick={() => onSectionChange("exercise")} />
                        <MenuButton text="Passive Mode" onClick={() => onSectionChange("passive_mode")} />
                    </>
                )}

                {role == 2 && (
                    <>
                        <MenuButton text="Dashboard" onClick={() => onSectionChange("supervisor_dashboard")} />
                        <MenuButton text="Add Patient" onClick={() => onSectionChange("add_patient")} />
                        <MenuButton text="View Patients" onClick={() => onSectionChange("view_patients")} />
                        <MenuButton text="View Progress" onClick={() => onSectionChange("view_progress")} />
                    </>
                )}
                <div className='bottom_menu'>
                    <MenuButton text="About ExoPath" onClick={() => onSectionChange("about")}></MenuButton>
                    <MenuButton text="Help" onClick={() => onSectionChange("help")}></MenuButton>

                </div>
            </div>
        </div>

    )
}

export default SideBar;
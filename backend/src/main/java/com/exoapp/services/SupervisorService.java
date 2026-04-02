package com.exoapp.services;

import com.exoapp.database.SupervisorDBOp;
import com.exoapp.database.UserDBOp;
import com.exoapp.models.dto.ProgressTrackerDTO;
import com.exoapp.models.dto.SupervisorStatsDTO;
import com.exoapp.models.dto.UserInformationDTO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SupervisorService {

    private static final SupervisorDBOp supervisorDBOp = new SupervisorDBOp();

    public List<UserInformationDTO> getMyPatients(Long supervisorId) throws SQLException
    {
        return supervisorDBOp.getMyPatients(supervisorId);
    }

    public List<ProgressTrackerDTO> getPatientsProgress(Long supervisorId) throws SQLException{
        return supervisorDBOp.getPatientsProgress(supervisorId);

    }

    public SupervisorStatsDTO getSupervisorStats(Long supervisorId) throws SQLException{
        return supervisorDBOp.getSupervisorStats(supervisorId);
    }
}

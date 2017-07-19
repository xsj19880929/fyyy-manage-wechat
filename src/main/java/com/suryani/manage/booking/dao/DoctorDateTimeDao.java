package com.suryani.manage.booking.dao;

import com.suryani.manage.booking.domain.DoctorDateTime;
import com.suryani.manage.db.BaseDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class DoctorDateTimeDao extends BaseDao<DoctorDateTime> {


    @SuppressWarnings("unchecked")
    public DoctorDateTime findDoctorDateTime(DoctorDateTime doctorDateTime) {
        Query query = em.createQuery("from DoctorDateTime where orgCode=:orgCode and triageNo=:triageNo and doctorId=:doctorId and timeDesc=:timeDesc and selectDate=:selectDate")
                .setParameter("orgCode", doctorDateTime.getOrgCode())
                .setParameter("triageNo", doctorDateTime.getTriageNo())
                .setParameter("doctorId", doctorDateTime.getDoctorId())
                .setParameter("timeDesc", doctorDateTime.getTimeDesc())
                .setParameter("selectDate", doctorDateTime.getSelectDate());
        List<DoctorDateTime> list = query.getResultList();
        if (list != null && !list.isEmpty()) {
            return (DoctorDateTime) list.get(0);
        }
        return null;
    }

    public void delete(String id) {
        em.createQuery("delete from DoctorDateTime where id=?1").setParameter(1, id).executeUpdate();
    }

}

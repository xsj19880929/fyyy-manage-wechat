package com.suryani.manage.booking.dao;

import com.suryani.manage.booking.domain.DoctorDateTimeDetail;
import com.suryani.manage.db.BaseDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class DoctorDateTimeDetailDao extends BaseDao<DoctorDateTimeDetail> {


    @SuppressWarnings("unchecked")
    public List<DoctorDateTimeDetail> findByDoctorDateTimeId(String doctorDateTimeId) {
        Query query = em.createQuery("from DoctorDateTimeDetail where doctorDateTimeId=:doctorDateTimeId order by selectTimeInt asc").setParameter("doctorDateTimeId", doctorDateTimeId);
        return query.getResultList();
    }


    public void deleteByDoctorDateTimeId(String doctorDateTimeId) {
        em.createQuery("delete from DoctorDateTimeDetail where doctorDateTimeId=?1").setParameter(1, doctorDateTimeId).executeUpdate();
    }


}

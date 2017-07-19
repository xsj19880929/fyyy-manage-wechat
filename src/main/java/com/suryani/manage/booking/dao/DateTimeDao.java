package com.suryani.manage.booking.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.suryani.manage.booking.domain.DateTime;
import com.suryani.manage.db.BaseDao;

@Repository
@Scope(value = "prototype")
public class DateTimeDao extends BaseDao<DateTime> {

    @SuppressWarnings("unchecked")
    public List<DateTime> findAll(String doctorId, String selectDate) {
        Query query = em.createQuery("from DateTime where doctorId=?1 and selectDate=?2 order by selectTime asc");
        query.setParameter(1, doctorId);
        query.setParameter(2, selectDate);
        return query.getResultList();
    }

}

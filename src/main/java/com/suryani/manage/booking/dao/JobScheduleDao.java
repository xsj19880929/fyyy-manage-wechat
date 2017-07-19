package com.suryani.manage.booking.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.suryani.manage.booking.domain.JobSchedule;
import com.suryani.manage.db.BaseDao;

@Repository
public class JobScheduleDao extends BaseDao<JobSchedule> {

    private String builtHql() {
        StringBuffer hql = new StringBuffer(225);
        hql.append("from JobSchedule");
        return hql.toString();
    }

    @SuppressWarnings("unchecked")
    public List<JobSchedule> findAll(int offset, int fetchSize) {
        String hql = builtHql();
        Query query = em.createQuery(hql + " order by createdTime desc");

        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    public int getTotalSize() {
        String hql = builtHql();
        Query query = em.createQuery("select count(id) " + hql);
        return ((Long) query.getSingleResult()).intValue();
    }

    public void delete(String id) {
        em.createQuery("delete from JobSchedule where id=?1").setParameter(1, id).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<JobSchedule> getJobToDo() {
        Query query = em.createQuery("from JobSchedule order by createdTime desc");
        return query.getResultList();
    }

}

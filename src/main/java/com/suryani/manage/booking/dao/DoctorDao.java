package com.suryani.manage.booking.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.suryani.manage.booking.domain.Doctor;
import com.suryani.manage.db.BaseDao;

@Repository
public class DoctorDao extends BaseDao<Doctor> {

    private String builtHql() {
        StringBuffer hql = new StringBuffer(225);
        hql.append("from Doctor");
        return hql.toString();
    }

    @SuppressWarnings("unchecked")
    public List<Doctor> findAll(int offset, int fetchSize) {
        String hql = builtHql();
        Query query = em.createQuery(hql + " order by createdTime desc");
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    public Doctor getOneByDoctorId(String doctorId) {
        Query query = em.createQuery("from Doctor where doctorId=?1").setParameter(1, doctorId);
        return (Doctor) query.getSingleResult();
    }

    public int getTotalSize() {
        String hql = builtHql();
        Query query = em.createQuery("select count(id) " + hql);
        return ((Long) query.getSingleResult()).intValue();
    }

    public void delete(String id) {
        em.createQuery("delete from Doctor where id=?1").setParameter(1, id).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<Doctor> findByDeptId(String deptId) {
        Query query = em.createQuery("from Doctor where deptId=:deptId order by createdTime desc").setParameter("deptId", deptId);
        return query.getResultList();
    }

}

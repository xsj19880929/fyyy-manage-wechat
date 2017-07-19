package com.suryani.manage.booking.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.suryani.manage.booking.domain.Department;
import com.suryani.manage.db.BaseDao;

@Repository
public class DepartmentDao extends BaseDao<Department> {

    private String builtHql() {
        StringBuffer hql = new StringBuffer(225);
        hql.append("from Department");
        return hql.toString();
    }

    @SuppressWarnings("unchecked")
    public List<Department> findAll(int offset, int fetchSize) {
        String hql = builtHql();
        Query query = em.createQuery(hql);
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    public Department getOneByDoctorId(String triageNo) {
        Query query = em.createQuery("from Department where triageNo=?1").setParameter(1, triageNo);
        return (Department) query.getSingleResult();
    }

    public int getTotalSize() {
        String hql = builtHql();
        Query query = em.createQuery("select count(id) " + hql);
        return ((Long) query.getSingleResult()).intValue();
    }

    public void delete(String id) {
        em.createQuery("delete from Department where id=?1").setParameter(1, id).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<Department> findAllNoPaging() {
        String hql = builtHql();
        Query query = em.createQuery(hql);
        return query.getResultList();
    }

}

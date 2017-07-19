package com.suryani.manage.booking.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.suryani.manage.booking.domain.AgentIP;
import com.suryani.manage.db.BaseDao;

@Repository
public class AgentIPDao extends BaseDao<AgentIP> {

    private String builtHql() {
        StringBuffer hql = new StringBuffer(225);
        hql.append("from AgentIP");
        return hql.toString();
    }

    @SuppressWarnings("unchecked")
    public List<AgentIP> findAll(int offset, int fetchSize) {
        String hql = builtHql();
        Query query = em.createQuery(hql + " order by timeLong asc");
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<AgentIP> findAllNoPage() {
        String hql = builtHql();
        Query query = em.createQuery(hql + " order by timeLong asc");
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<AgentIP> findAllStatus() {
        Date now = new Date();
        Date afterDate = new Date(now.getTime() - 60000);
        String hql = builtHql();
        Query query = em.createQuery(hql + " where status=0 and useStatus=0 and (bookingTime<?1 or bookingTime is null) order by timeLong asc").setParameter(1, afterDate);
        return query.getResultList();
    }

    public AgentIP getOneById(String id) {
        Query query = em.createQuery("from AgentIP where id=?1").setParameter(1, id);
        return (AgentIP) query.getSingleResult();
    }

    public int getTotalSize() {
        String hql = builtHql();
        Query query = em.createQuery("select count(id) " + hql);
        return ((Long) query.getSingleResult()).intValue();
    }

    public int getSizeByIp(String ip) {
        Query query = em.createQuery("select count(id) from AgentIP where ip=?1").setParameter(1, ip);
        return ((Long) query.getSingleResult()).intValue();
    }

    public void delete(String id) {
        em.createQuery("delete from AgentIP where id=?1").setParameter(1, id).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<AgentIP> findByIp(String ip) {
        Query query = em.createQuery("from AgentIP where ip=?1").setParameter(1, ip);
        return query.getResultList();
    }

}

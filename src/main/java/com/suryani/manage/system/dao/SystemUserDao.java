package com.suryani.manage.system.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.suryani.manage.db.BaseDao;
import com.suryani.manage.system.domain.SystemUser;

@Repository
public class SystemUserDao extends BaseDao<SystemUser> {
    public void updateStatus(String id, String status) {
        em.createQuery("update SystemUser set status=?2 where id=?1").setParameter(1, id).setParameter(2, status).executeUpdate();
    }

    public void updatePwd(String id, String pwd) {
        em.createQuery("update SystemUser set password=?2 where id=?1").setParameter(1, id).setParameter(2, pwd).executeUpdate();
    }

    public SystemUser getByNameAndPwdWithStatus(String name, String pwd, String status) {
        Query q = em.createQuery("from SystemUser where name=?1 and password=?2 and status=?3").setParameter(1, name).setParameter(2, pwd).setParameter(3, status);
        Object rt = null;
        try {

            rt = q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rt != null ? (SystemUser) rt : null;
    }

    public SystemUser getByPhoneAndPwdWithStatus(String phone, String pwd, String status) {
        Query q = em.createQuery("from SystemUser where phone=?1 and password=?2 and status=?3").setParameter(1, phone).setParameter(2, pwd).setParameter(3, status);
        Object rt = null;
        try {

            rt = q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rt != null ? (SystemUser) rt : null;
    }

    private String builtHql(String name) {
        StringBuffer hql = new StringBuffer(225);
        hql.append("from SystemUser  where 1=1");
        if (StringUtils.hasText(name)) {
            hql.append(" and name like:name");
        }
        return hql.toString();
    }

    @SuppressWarnings("unchecked")
    public List<SystemUser> findAll(String name, int offset, int fetchSize) {
        String hql = builtHql(name);
        Query query = em.createQuery(hql + " order by updateTime desc");
        if (StringUtils.hasText(name)) {
            query.setParameter("name", "%" + name + "%");
        }
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    public int getTotalSize(String name) {
        String hql = builtHql(name);
        Query query = em.createQuery("select count(id) " + hql);
        if (StringUtils.hasText(name)) {
            query.setParameter("name", "%" + name + "%");
        }
        return ((Long) query.getSingleResult()).intValue();
    }

    public void delete(String id) {
        em.createQuery("delete from SystemUser where id=?1").setParameter(1, id).executeUpdate();
    }

    public int countUserByName(String name) {
        Query query = em.createQuery("select count(id) from SystemUser where name=?1").setParameter(1, name);
        return ((Long) query.getSingleResult()).intValue();
    }
    
    public int countUserByPhone(String phone) {
        Query query = em.createQuery("select count(id) from SystemUser where phone=?1").setParameter(1, phone);
        return ((Long) query.getSingleResult()).intValue();
    }
}
